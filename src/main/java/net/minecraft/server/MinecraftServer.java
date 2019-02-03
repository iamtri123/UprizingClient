package net.minecraft.server;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer implements ICommandSender, Runnable, IPlayerUsage
{
    private static final Logger logger = LogManager.getLogger();
    public static final File USER_CACHE_FILE = new File("usercache.json");

    /** Instance of Minecraft Server. */
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;

    /** The PlayerUsageSnooper instance. */
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, getSystemTimeMillis());
    private final File anvilFile;

    /**
     * Collection of objects to update every tick. Type: List<IUpdatePlayerListBox>
     */
    private final List tickables = new ArrayList();
    private final ICommandManager commandManager;
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();

    /** The server's port. */
    private final int serverPort = -1;

    /** The server world instances. */
    public WorldServer[] worldServers;

    /** The ServerConfigurationManager instance. */
    private ServerConfigurationManager serverConfigManager;

    /**
     * Indicates whether the server is running or not. Set to false to initiate a shutdown.
     */
    private boolean serverRunning = true;

    /** Indicates to other classes that the server is safely stopped. */
    private boolean serverStopped;

    /** Incremented every tick. */
    private int tickCounter;
    protected final Proxy serverProxy;

    /**
     * The task the server is currently working on(and will output on outputPercentRemaining).
     */
    public String currentTask;

    /** The percentage of the current task finished so far. */
    public int percentDone;

    /** True if the server is in online mode. */
    private boolean onlineMode;

    /** True if the server has animals turned on. */
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;

    /** Indicates whether PvP is active on the server or not. */
    private boolean pvpEnabled;

    /** Determines if flight is allowed or not. */
    private boolean allowFlight;

    /** The server MOTD string. */
    private String motd;

    /** Maximum build height. */
    private int buildLimit;
    private int maxPlayerIdleMinutes = 0;
    public final long[] tickTimeArray = new long[100];

    /** Stats are [dimension][tick%100] system.nanoTime is stored. */
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;

    /** Username of the server owner (for integrated servers) */
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;

    /**
     * If true, there is no need to save chunks or stop the server, because that is already being done.
     */
    private boolean worldIsBeingDeleted;
    private final String serverTexturePack = "";
    private boolean serverIsRunning;

    /**
     * Set when warned for "Can't keep up", which triggers again after 15 seconds.
     */
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private long nanoTimeSinceStatusRefresh = 0L;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;

    public MinecraftServer(File workDir, Proxy proxy)
    {
        this.profileCache = new PlayerProfileCache(this, USER_CACHE_FILE);
        mcServer = this;
        this.serverProxy = proxy;
        this.anvilFile = workDir;
        this.networkSystem = new NetworkSystem(this);
        this.commandManager = new ServerCommandManager();
        this.anvilConverterForAnvilFile = new AnvilSaveConverter(workDir);
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }

    /**
     * Initialises the server and starts it.
     */
    protected abstract boolean startServer() throws IOException;

    protected void convertMapIfNeeded(String p_71237_1_)
    {
        if (this.getActiveAnvilConverter().isOldMapFormat(p_71237_1_))
        {
            logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(p_71237_1_, new IProgressUpdate()
            {
                private long field_96245_b = System.currentTimeMillis();
                public void displayProgressMessage(String p_73720_1_) {}
                public void resetProgressAndMessage(String p_73721_1_) {}
                public void setLoadingProgress(int p_73718_1_)
                {
                    if (System.currentTimeMillis() - this.field_96245_b >= 1000L)
                    {
                        this.field_96245_b = System.currentTimeMillis();
                        MinecraftServer.logger.info("Converting... " + p_73718_1_ + "%");
                    }
                }
                public void setDoneWorking() {}
                public void resetProgresAndWorkingMessage(String p_73719_1_) {}
            });
        }
    }

    /**
     * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
     */
    protected synchronized void setUserMessage(String message)
    {
        this.userMessage = message;
    }

    public synchronized String getUserMessage()
    {
        return this.userMessage;
    }

    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long p_71247_3_, WorldType p_71247_5_, String p_71247_6_)
    {
        this.convertMapIfNeeded(p_71247_1_);
        this.setUserMessage("menu.loadingLevel");
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler var7 = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
        WorldInfo var9 = var7.loadWorldInfo();
        WorldSettings var8;

        if (var9 == null)
        {
            var8 = new WorldSettings(p_71247_3_, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), p_71247_5_);
            var8.setWorldName(p_71247_6_);
        }
        else
        {
            var8 = new WorldSettings(var9);
        }

        if (this.enableBonusChest)
        {
            var8.enableBonusChest();
        }

        for (int var10 = 0; var10 < this.worldServers.length; ++var10)
        {
            byte var11 = 0;

            if (var10 == 1)
            {
                var11 = -1;
            }

            if (var10 == 2)
            {
                var11 = 1;
            }

            if (var10 == 0)
            {
                if (this.isDemo())
                {
                    this.worldServers[var10] = new DemoWorldServer(this, var7, p_71247_2_, var11);
                }
                else
                {
                    this.worldServers[var10] = new WorldServer(this, var7, p_71247_2_, var11, var8);
                }
            }
            else
            {
                this.worldServers[var10] = new WorldServerMulti(this, var7, p_71247_2_, var11, var8, this.worldServers[0]);
            }

            this.worldServers[var10].addWorldAccess(new WorldManager(this, this.worldServers[var10]));

            if (!this.isSinglePlayer())
            {
                this.worldServers[var10].getWorldInfo().setGameType(this.getGameType());
            }

            this.serverConfigManager.setPlayerManager(this.worldServers);
        }

        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    protected void initialWorldChunkLoad()
    {
        boolean var1 = true;
        boolean var2 = true;
        boolean var3 = true;
        boolean var4 = true;
        int var5 = 0;
        this.setUserMessage("menu.generatingTerrain");
        byte var6 = 0;
        logger.info("Preparing start region for level " + var6);
        WorldServer var7 = this.worldServers[var6];
        ChunkCoordinates var8 = var7.getSpawnPoint();
        long var9 = getSystemTimeMillis();

        for (int var11 = -192; var11 <= 192 && this.isServerRunning(); var11 += 16)
        {
            for (int var12 = -192; var12 <= 192 && this.isServerRunning(); var12 += 16)
            {
                long var13 = getSystemTimeMillis();

                if (var13 - var9 > 1000L)
                {
                    this.outputPercentRemaining("Preparing spawn area", var5 * 100 / 625);
                    var9 = var13;
                }

                ++var5;
                var7.theChunkProviderServer.loadChunk(var8.posX + var11 >> 4, var8.posZ + var12 >> 4);
            }
        }

        this.clearCurrentTask();
    }

    public abstract boolean canStructuresSpawn();

    public abstract WorldSettings.GameType getGameType();

    public abstract EnumDifficulty getDifficulty();

    /**
     * Defaults to false.
     */
    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    public abstract boolean func_152363_m();

    /**
     * Used to display a percent remaining given text and the percentage.
     */
    protected void outputPercentRemaining(String message, int percent)
    {
        this.currentTask = message;
        this.percentDone = percent;
        logger.info(message + ": " + percent + "%");
    }

    /**
     * Set current task to null and set its percentage to 0.
     */
    protected void clearCurrentTask()
    {
        this.currentTask = null;
        this.percentDone = 0;
    }

    /**
     * par1 indicates if a log message should be output.
     */
    protected void saveAllWorlds(boolean dontLog)
    {
        if (!this.worldIsBeingDeleted)
        {
            WorldServer[] var2 = this.worldServers;
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                WorldServer var5 = var2[var4];

                if (var5 != null)
                {
                    if (!dontLog)
                    {
                        logger.info("Saving chunks for level \'" + var5.getWorldInfo().getWorldName() + "\'/" + var5.provider.getDimensionName());
                    }

                    try
                    {
                        var5.saveAllChunks(true, (IProgressUpdate)null);
                    }
                    catch (MinecraftException var7)
                    {
                        logger.warn(var7.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        if (!this.worldIsBeingDeleted)
        {
            logger.info("Stopping server");

            if (this.getNetworkSystem() != null)
            {
                this.getNetworkSystem().terminateEndpoints();
            }

            if (this.serverConfigManager != null)
            {
                logger.info("Saving players");
                this.serverConfigManager.saveAllPlayerData();
                this.serverConfigManager.removeAllPlayers();
            }

            if (this.worldServers != null)
            {
                logger.info("Saving worlds");
                this.saveAllWorlds(false);

                for (int var1 = 0; var1 < this.worldServers.length; ++var1)
                {
                    WorldServer var2 = this.worldServers[var1];
                    var2.flush();
                }
            }

            if (this.usageSnooper.isSnooperRunning())
            {
                this.usageSnooper.stopSnooper();
            }
        }
    }

    public boolean isServerRunning()
    {
        return this.serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        this.serverRunning = false;
    }

    public void run()
    {
        try
        {
            if (this.startServer())
            {
                long var1 = getSystemTimeMillis();
                long var50 = 0L;
                this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
                this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.7.10", 5));
                this.addFaviconToStatusResponse(this.statusResponse);

                while (this.serverRunning)
                {
                    long var5 = getSystemTimeMillis();
                    long var7 = var5 - var1;

                    if (var7 > 2000L && var1 - this.timeOfLastWarning >= 15000L)
                    {
                        logger.warn("Can\'t keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", Long.valueOf(var7), Long.valueOf(var7 / 50L));
                        var7 = 2000L;
                        this.timeOfLastWarning = var1;
                    }

                    if (var7 < 0L)
                    {
                        logger.warn("Time ran backwards! Did the system time change?");
                        var7 = 0L;
                    }

                    var50 += var7;
                    var1 = var5;

                    if (this.worldServers[0].areAllPlayersAsleep())
                    {
                        this.tick();
                        var50 = 0L;
                    }
                    else
                    {
                        while (var50 > 50L)
                        {
                            var50 -= 50L;
                            this.tick();
                        }
                    }

                    Thread.sleep(Math.max(1L, 50L - var50));
                    this.serverIsRunning = true;
                }
            }
            else
            {
                this.finalTick((CrashReport)null);
            }
        }
        catch (Throwable var48)
        {
            logger.error("Encountered an unexpected exception", var48);
            CrashReport var2 = null;

            if (var48 instanceof ReportedException)
            {
                var2 = this.addServerInfoToCrashReport(((ReportedException)var48).getCrashReport());
            }
            else
            {
                var2 = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var48));
            }

            File var3 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (var2.saveToFile(var3))
            {
                logger.error("This crash report has been saved to: " + var3.getAbsolutePath());
            }
            else
            {
                logger.error("We were unable to save this crash report to disk.");
            }

            this.finalTick(var2);
        }
        finally
        {
            try
            {
                this.stopServer();
                this.serverStopped = true;
            }
            catch (Throwable var46)
            {
                logger.error("Exception stopping the server", var46);
            }
            finally
            {
                this.systemExitNow();
            }
        }
    }

    private void addFaviconToStatusResponse(ServerStatusResponse response)
    {
        File var2 = this.getFile("server-icon.png");

        if (var2.isFile())
        {
            ByteBuf var3 = Unpooled.buffer();

            try
            {
                BufferedImage var4 = ImageIO.read(var2);
                Validate.validState(var4.getWidth() == 64, "Must be 64 pixels wide");
                Validate.validState(var4.getHeight() == 64, "Must be 64 pixels high");
                ImageIO.write(var4, "PNG", new ByteBufOutputStream(var3));
                ByteBuf var5 = Base64.encode(var3);
                response.setFavicon("data:image/png;base64," + var5.toString(Charsets.UTF_8));
            }
            catch (Exception var9)
            {
                logger.error("Couldn\'t load server icon", var9);
            }
            finally
            {
                var3.release();
            }
        }
    }

    protected File getDataDirectory()
    {
        return new File(".");
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport report) {}

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    protected void systemExitNow() {}

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        long var1 = System.nanoTime();
        ++this.tickCounter;

        if (this.startProfiling)
        {
            this.startProfiling = false;
        }

        this.updateTimeLightAndEntities();

        if (var1 - this.nanoTimeSinceStatusRefresh >= 5000000000L)
        {
            this.nanoTimeSinceStatusRefresh = var1;
            this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] var3 = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int var4 = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - var3.length);

            for (int var5 = 0; var5 < var3.length; ++var5)
            {
                var3[var5] = ((EntityPlayerMP)this.serverConfigManager.playerEntityList.get(var4 + var5)).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(var3));
            this.statusResponse.getPlayerCountData().setPlayers(var3);
        }

        if (this.tickCounter % 900 == 0)
        {
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllWorlds(true);
        }

        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - var1;

        if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100)
        {
            this.usageSnooper.startSnooper();
        }

        if (this.tickCounter % 6000 == 0)
        {
            this.usageSnooper.addMemoryStatsToSnooper();
        }
    }

    public void updateTimeLightAndEntities()
    {
        int var1;

        for (var1 = 0; var1 < this.worldServers.length; ++var1)
        {
            long var2 = System.nanoTime();

            if (var1 == 0 || this.getAllowNether())
            {
                WorldServer var4 = this.worldServers[var1];

                if (this.tickCounter % 20 == 0)
                {
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(var4.getTotalWorldTime(), var4.getWorldTime(), var4.getGameRules().getGameRuleBooleanValue("doDaylightCycle")), var4.provider.dimensionId);
                }

                CrashReport var6;

                try
                {
                    var4.tick();
                }
                catch (Throwable var8)
                {
                    var6 = CrashReport.makeCrashReport(var8, "Exception ticking world");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }

                try
                {
                    var4.updateEntities();
                }
                catch (Throwable var7)
                {
                    var6 = CrashReport.makeCrashReport(var7, "Exception ticking world entities");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }

                var4.getEntityTracker().updateTrackedEntities();
            }

            this.timeOfLastDimensionTick[var1][this.tickCounter % 100] = System.nanoTime() - var2;
        }

        this.getNetworkSystem().networkTick();
        this.serverConfigManager.sendPlayerInfoToAllPlayers();

        for (var1 = 0; var1 < this.tickables.size(); ++var1)
        {
            ((IUpdatePlayerListBox)this.tickables.get(var1)).update();
        }
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public void startServerThread()
    {
        (new Thread("Server thread")
        {
            public void run()
            {
                MinecraftServer.this.run();
            }
        }).start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String fileName)
    {
        return new File(this.getDataDirectory(), fileName);
    }

    /**
     * Logs the message with a level of WARN.
     */
    public void logWarning(String msg)
    {
        logger.warn(msg);
    }

    /**
     * Gets the worldServer by the given dimension.
     */
    public WorldServer worldServerForDimension(int dimension)
    {
        return dimension == -1 ? this.worldServers[1] : (dimension == 1 ? this.worldServers[2] : this.worldServers[0]);
    }

    /**
     * Returns the server's Minecraft version as string.
     */
    public String getMinecraftVersion()
    {
        return "1.7.10";
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount()
    {
        return this.serverConfigManager.getCurrentPlayerCount();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers()
    {
        return this.serverConfigManager.getMaxPlayers();
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames()
    {
        return this.serverConfigManager.getAllUsernames();
    }

    public GameProfile[] getGameProfiles()
    {
        return this.serverConfigManager.getAllProfiles();
    }

    public String getServerModName()
    {
        return "vanilla";
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report)
    {
        report.getCategory().addCrashSectionCallable("Profiler Position", new Callable()
        {
            public String call()
            {
                return "N/A (disabled)";
            }
        });

        if (this.worldServers != null && this.worldServers.length > 0 && this.worldServers[0] != null)
        {
            report.getCategory().addCrashSectionCallable("Vec3 Pool Size", new Callable()
            {
                public String call()
                {
                    byte var1 = 0;
                    int var2 = 56 * var1;
                    int var3 = var2 / 1024 / 1024;
                    byte var4 = 0;
                    int var5 = 56 * var4;
                    int var6 = var5 / 1024 / 1024;
                    return var1 + " (" + var2 + " bytes; " + var3 + " MB) allocated, " + var4 + " (" + var5 + " bytes; " + var6 + " MB) used";
                }
            });
        }

        if (this.serverConfigManager != null)
        {
            report.getCategory().addCrashSectionCallable("Player Count", new Callable()
            {
                public String call()
                {
                    return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.playerEntityList;
                }
            });
        }

        return report;
    }

    /**
     * If par2Str begins with /, then it searches for commands, otherwise it returns players.
     */
    public List getPossibleCompletions(ICommandSender sender, String input)
    {
        ArrayList var3 = new ArrayList();

        if (input.startsWith("/"))
        {
            input = input.substring(1);
            boolean var10 = !input.contains(" ");
            List var11 = this.commandManager.getPossibleCommands(sender, input);

            if (var11 != null)
            {
                Iterator var12 = var11.iterator();

                while (var12.hasNext())
                {
                    String var13 = (String)var12.next();

                    if (var10)
                    {
                        var3.add("/" + var13);
                    }
                    else
                    {
                        var3.add(var13);
                    }
                }
            }

            return var3;
        }
        else
        {
            String[] var4 = input.split(" ", -1);
            String var5 = var4[var4.length - 1];
            String[] var6 = this.serverConfigManager.getAllUsernames();
            int var7 = var6.length;

            for (int var8 = 0; var8 < var7; ++var8)
            {
                String var9 = var6[var8];

                if (CommandBase.doesStringStartWith(var5, var9))
                {
                    var3.add(var9);
                }
            }

            return var3;
        }
    }

    /**
     * Gets mcServer.
     */
    public static MinecraftServer getServer()
    {
        return mcServer;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return "Server";
    }

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    public void addChatMessage(IChatComponent message)
    {
        logger.info(message.getUnformattedText());
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int permissionLevel, String command)
    {
        return true;
    }

    public ICommandManager getCommandManager()
    {
        return this.commandManager;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair()
    {
        return this.serverKeyPair;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner()
    {
        return this.serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an integrated server)
     */
    public void setServerOwner(String owner)
    {
        this.serverOwner = owner;
    }

    public boolean isSinglePlayer()
    {
        return this.serverOwner != null;
    }

    public String getFolderName()
    {
        return this.folderName;
    }

    public void setFolderName(String name)
    {
        this.folderName = name;
    }

    public void setWorldName(String p_71246_1_)
    {
        this.worldName = p_71246_1_;
    }

    public String getWorldName()
    {
        return this.worldName;
    }

    public void setKeyPair(KeyPair keyPair)
    {
        this.serverKeyPair = keyPair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty)
    {
        for (int var2 = 0; var2 < this.worldServers.length; ++var2)
        {
            WorldServer var3 = this.worldServers[var2];

            if (var3 != null)
            {
                if (var3.getWorldInfo().isHardcoreModeEnabled())
                {
                    var3.difficultySetting = EnumDifficulty.HARD;
                    var3.setAllowedSpawnTypes(true, true);
                }
                else if (this.isSinglePlayer())
                {
                    var3.difficultySetting = difficulty;
                    var3.setAllowedSpawnTypes(var3.difficultySetting != EnumDifficulty.PEACEFUL, true);
                }
                else
                {
                    var3.difficultySetting = difficulty;
                    var3.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }
    }

    protected boolean allowSpawnMonsters()
    {
        return true;
    }

    /**
     * Gets whether this is a demo or not.
     */
    public boolean isDemo()
    {
        return this.isDemo;
    }

    /**
     * Sets whether this is a demo or not.
     */
    public void setDemo(boolean demo)
    {
        this.isDemo = demo;
    }

    public void canCreateBonusChest(boolean enable)
    {
        this.enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter()
    {
        return this.anvilConverterForAnvilFile;
    }

    /**
     * WARNING : directly calls
     * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer[0].getSaveHandler().getWorldDirectoryName());
     */
    public void deleteWorldAndStopServer()
    {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();

        for (int var1 = 0; var1 < this.worldServers.length; ++var1)
        {
            WorldServer var2 = this.worldServers[var1];

            if (var2 != null)
            {
                var2.flush();
            }
        }

        this.getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].getSaveHandler().getWorldDirectoryName());
        this.initiateShutdown();
    }

    public String getTexturePack()
    {
        return this.serverTexturePack;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper)
    {
        playerSnooper.addClientStat("whitelist_enabled", Boolean.valueOf(false));
        playerSnooper.addClientStat("whitelist_count", Integer.valueOf(0));
        playerSnooper.addClientStat("players_current", Integer.valueOf(this.getCurrentPlayerCount()));
        playerSnooper.addClientStat("players_max", Integer.valueOf(this.getMaxPlayers()));
        playerSnooper.addClientStat("players_seen", Integer.valueOf(this.serverConfigManager.getAvailablePlayerDat().length));
        playerSnooper.addClientStat("uses_auth", Boolean.valueOf(this.onlineMode));
        playerSnooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        playerSnooper.addClientStat("run_time", Long.valueOf((getSystemTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L));
        playerSnooper.addClientStat("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(this.tickTimeArray) * 1.0E-6D)));
        int var2 = 0;

        for (int var3 = 0; var3 < this.worldServers.length; ++var3)
        {
            if (this.worldServers[var3] != null)
            {
                WorldServer var4 = this.worldServers[var3];
                WorldInfo var5 = var4.getWorldInfo();
                playerSnooper.addClientStat("world[" + var2 + "][dimension]", Integer.valueOf(var4.provider.dimensionId));
                playerSnooper.addClientStat("world[" + var2 + "][mode]", var5.getGameType());
                playerSnooper.addClientStat("world[" + var2 + "][difficulty]", var4.difficultySetting);
                playerSnooper.addClientStat("world[" + var2 + "][hardcore]", Boolean.valueOf(var5.isHardcoreModeEnabled()));
                playerSnooper.addClientStat("world[" + var2 + "][generator_name]", var5.getTerrainType().getWorldTypeName());
                playerSnooper.addClientStat("world[" + var2 + "][generator_version]", Integer.valueOf(var5.getTerrainType().getGeneratorVersion()));
                playerSnooper.addClientStat("world[" + var2 + "][height]", Integer.valueOf(this.buildLimit));
                playerSnooper.addClientStat("world[" + var2 + "][chunks_loaded]", Integer.valueOf(var4.getChunkProvider().getLoadedChunkCount()));
                ++var2;
            }
        }

        playerSnooper.addClientStat("worlds", Integer.valueOf(var2));
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper)
    {
        playerSnooper.addStatToSnooper("singleplayer", Boolean.valueOf(this.isSinglePlayer()));
        playerSnooper.addStatToSnooper("server_brand", this.getServerModName());
        playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        playerSnooper.addStatToSnooper("dedicated", Boolean.valueOf(this.isDedicatedServer()));
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return true;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode()
    {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean online)
    {
        this.onlineMode = online;
    }

    public boolean getCanSpawnAnimals()
    {
        return this.canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean spawnAnimals)
    {
        this.canSpawnAnimals = spawnAnimals;
    }

    public boolean getCanSpawnNPCs()
    {
        return this.canSpawnNPCs;
    }

    public void setCanSpawnNPCs(boolean spawnNpcs)
    {
        this.canSpawnNPCs = spawnNpcs;
    }

    public boolean isPVPEnabled()
    {
        return this.pvpEnabled;
    }

    public void setAllowPvp(boolean allowPvp)
    {
        this.pvpEnabled = allowPvp;
    }

    public boolean isFlightAllowed()
    {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean allow)
    {
        this.allowFlight = allow;
    }

    /**
     * Return whether command blocks are enabled.
     */
    public abstract boolean isCommandBlockEnabled();

    public String getMOTD()
    {
        return this.motd;
    }

    public void setMOTD(String motdIn)
    {
        this.motd = motdIn;
    }

    public int getBuildLimit()
    {
        return this.buildLimit;
    }

    public void setBuildLimit(int maxBuildHeight)
    {
        this.buildLimit = maxBuildHeight;
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return this.serverConfigManager;
    }

    public void setConfigManager(ServerConfigurationManager configManager)
    {
        this.serverConfigManager = configManager;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(WorldSettings.GameType gameMode)
    {
        for (int var2 = 0; var2 < this.worldServers.length; ++var2)
        {
            getServer().worldServers[var2].getWorldInfo().setGameType(gameMode);
        }
    }

    public NetworkSystem getNetworkSystem()
    {
        return this.networkSystem;
    }

    public boolean serverIsInRunLoop()
    {
        return this.serverIsRunning;
    }

    public boolean getGuiEnabled()
    {
        return false;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public abstract String shareToLAN(WorldSettings.GameType type, boolean allowCheats);

    public int getTickCounter()
    {
        return this.tickCounter;
    }

    public void enableProfiling()
    {
        this.startProfiling = true;
    }

    public PlayerUsageSnooper getPlayerUsageSnooper()
    {
        return this.usageSnooper;
    }

    /**
     * Return the position for this command sender.
     */
    public ChunkCoordinates getPlayerCoordinates()
    {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World getEntityWorld()
    {
        return this.worldServers[0];
    }

    /**
     * Return the spawn protection area's size.
     */
    public int getSpawnProtectionSize()
    {
        return 16;
    }

    /**
     * Returns true if a player does not have permission to edit the block at the given coordinates.
     */
    public boolean isBlockProtected(World inWorld, int x, int y, int z, EntityPlayer player)
    {
        return false;
    }

    public boolean getForceGamemode()
    {
        return this.isGamemodeForced;
    }

    public Proxy getServerProxy()
    {
        return this.serverProxy;
    }

    /**
     * returns the difference, measured in milliseconds, between the current system time and midnight, January 1, 1970
     * UTC.
     */
    public static long getSystemTimeMillis()
    {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes()
    {
        return this.maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout)
    {
        this.maxPlayerIdleMinutes = idleTimeout;
    }

    public IChatComponent getFormattedCommandSenderName()
    {
        return new ChatComponentText(this.getCommandSenderName());
    }

    public boolean isAnnouncingPlayerAchievements()
    {
        return true;
    }

    public MinecraftSessionService getMinecraftSessionService()
    {
        return this.sessionService;
    }

    public GameProfileRepository getGameProfileRepository()
    {
        return this.profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache()
    {
        return this.profileCache;
    }

    public ServerStatusResponse getServerStatusResponse()
    {
        return this.statusResponse;
    }

    public void refreshStatusNextTick()
    {
        this.nanoTimeSinceStatusRefresh = 0L;
    }
}