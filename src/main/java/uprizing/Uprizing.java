package uprizing;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import optifine.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uprizing.mod.ModRepository;
import uprizing.mods.WaypointsMod;
import uprizing.world.WorldTimeMode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

@Getter
public class Uprizing {

    /** I'm Gay */
    private static final Logger logger = LogManager.getLogger();
    private transient final Minecraft minecraft;
    private final File file;

    /** Mods */
    private final ModRepository modRepository = new ModRepository();
    private transient WaypointsMod waypointsMod;

    /** Others */
    public WorldTimeMode worldTimeMode = new WorldTimeMode();

    /** Options */
    public boolean scoreboardNumbers = false;
    public boolean scoreboardShadow = false;
    public boolean chatBackground = false;
    public boolean clearGlass = true;

    public Uprizing(final Minecraft minecraft, final File mainDir) {
        this.minecraft = minecraft;
        this.file = new File(mainDir, "uprizing.txt");
        this.loadOptions();
        this.initMods(mainDir);
        this.initKeyBindings();
        Config.initUprizing(this);
    }

    private void initMods(File mainDir) {
        waypointsMod = new WaypointsMod(this, mainDir);
        modRepository.addMod(waypointsMod);
    }

    private void initKeyBindings() { // TODO: KeyBindings object in IMod (for mod disabling)
        KeyBinding[] gameSettings = minecraft.gameSettings.keyBindings;
        KeyBinding[] uprizing = modRepository.getKeyBindings();

        KeyBinding[] keyBindings = new KeyBinding[gameSettings.length + uprizing.length];
        System.arraycopy(gameSettings, 0, keyBindings, 0, gameSettings.length);
        System.arraycopy(uprizing, 0, keyBindings, gameSettings.length, uprizing.length);

        minecraft.gameSettings.keyBindings = keyBindings;
    }

    public void runTick(TickType tickType) {
        while (modRepository.hasNext())
            modRepository.next().runTick(tickType);
        modRepository.close();
    }

    private void loadOptions() {
        try {
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    final String[] args = line.split(":");

                    if (args[0].equals("scoreboardNumbers")) {
                        scoreboardNumbers = args[1].equals("true");
                    } else if (args[0].equals("scoreboardShadow")) {
                        scoreboardShadow = args[1].equals("true");
                    } else if (args[0].equals("chatBackground")) {
                        chatBackground = args[1].equals("true");
                    } else if (args[0].equals("clearGlass")) {
                        clearGlass = args[1].equals("true");
                    } else if (args[0].equals("worldTimeMode")) {
                        worldTimeMode.set(Integer.parseInt(args[1]));
                    }
                } catch (Exception var91) {
                    logger.warn("Skipping bad option: " + line);
                    var91.printStackTrace();
                }
            }

            reader.close();
        } catch (Exception exception) {
            logger.error("Failed to load options", exception);
        }
    }

    public void saveOptions() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));
            writer.println("scoreboardNumbers:" + scoreboardNumbers);
            writer.println("scoreboardShadow:" + scoreboardShadow);
            writer.println("chatBackground:" + chatBackground);
            writer.println("clearGlass:" + clearGlass);
            writer.println("worldTimeMode:" + worldTimeMode.get());
            writer.close();
        } catch (Exception exception) {
            logger.error("Failed to save options", exception);
        }
    }

    public String getKeyBinding(Options option) {
        final String prefix = option.getName() + ": ";

        if (option == Options.SCOREBOARD_NUMBERS) {
            return scoreboardNumbers ? prefix + "ON" : prefix + "OFF";
        } else if (option == Options.SCOREBOARD_SHADOW) {
            return scoreboardShadow ? prefix + "ON" : prefix + "OFF";
        } else if (option == Options.CHAT_BACKGROUND) {
            return chatBackground ? prefix + "ON" : prefix + "OFF";
        } else if (option == Options.CLEAR_GLASS) {
            return clearGlass ? prefix + "ON" : prefix + "OFF";
        } else if (option == Options.WORLD_TIME_MODE) {
            return prefix + worldTimeMode.getName();
        } else {
            return prefix;
        }
    }

    public void setOptionValue(Options option) {
        if (option == Options.SCOREBOARD_NUMBERS) {
            scoreboardNumbers = !scoreboardNumbers;
        } else if (option == Options.SCOREBOARD_SHADOW) {
            scoreboardShadow = !scoreboardShadow;
        } else if (option == Options.CHAT_BACKGROUND) {
            chatBackground = !chatBackground;
        } else if (option == Options.CLEAR_GLASS) {
            clearGlass = !clearGlass;
            minecraft.renderGlobal.loadRenderers();
        } else if (option == Options.WORLD_TIME_MODE) {
            worldTimeMode.next();
        }
    }
}