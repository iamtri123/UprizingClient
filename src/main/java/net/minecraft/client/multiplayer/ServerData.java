package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ServerData
{
    public String serverName;
    public String serverIP;

    /**
     * the string indicating number of players on and capacity of the server that is shown on the server browser (i.e.
     * "5/20" meaning 5 slots used out of 20 slots total)
     */
    public String populationInfo;

    /**
     * (better variable name would be 'hostname') server name as displayed in the server browser's second line (grey
     * text)
     */
    public String serverMOTD;

    /** last server ping that showed up in the server browser */
    public long pingToServer;
    public int version;

    /** Game version for this server. */
    public String gameVersion;
    public boolean field_78841_f;
    public String playerList;
    private ServerData.ServerResourceMode resourceMode;
    private String serverIcon;
    private boolean lanServer;
    private static final String __OBFID = "CL_00000890";

    public ServerData(String p_i1193_1_, String p_i1193_2_)
    {
        this.version = 5;
        this.gameVersion = "1.7.10";
        this.resourceMode = ServerData.ServerResourceMode.PROMPT;
        this.serverName = p_i1193_1_;
        this.serverIP = p_i1193_2_;
    }

    public ServerData(String p_i46395_1_, String p_i46395_2_, boolean p_i46395_3_)
    {
        this(p_i46395_1_, p_i46395_2_);
        this.lanServer = p_i46395_3_;
    }

    /**
     * Returns an NBTTagCompound with the server's name, IP and maybe acceptTextures.
     */
    public NBTTagCompound getNBTCompound()
    {
        NBTTagCompound var1 = new NBTTagCompound();
        var1.setString("name", this.serverName);
        var1.setString("ip", this.serverIP);

        if (this.serverIcon != null)
        {
            var1.setString("icon", this.serverIcon);
        }

        if (this.resourceMode == ServerData.ServerResourceMode.ENABLED)
        {
            var1.setBoolean("acceptTextures", true);
        }
        else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED)
        {
            var1.setBoolean("acceptTextures", false);
        }

        return var1;
    }

    public ServerData.ServerResourceMode getResourceMode()
    {
        return this.resourceMode;
    }

    public void setResourceMode(ServerData.ServerResourceMode mode)
    {
        this.resourceMode = mode;
    }

    /**
     * Takes an NBTTagCompound with 'name' and 'ip' keys, returns a ServerData instance.
     */
    public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound)
    {
        ServerData var1 = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"));

        if (nbtCompound.hasKey("icon", 8))
        {
            var1.setBase64EncodedIconData(nbtCompound.getString("icon"));
        }

        if (nbtCompound.hasKey("acceptTextures", 1))
        {
            if (nbtCompound.getBoolean("acceptTextures"))
            {
                var1.setResourceMode(ServerData.ServerResourceMode.ENABLED);
            }
            else
            {
                var1.setResourceMode(ServerData.ServerResourceMode.DISABLED);
            }
        }
        else
        {
            var1.setResourceMode(ServerData.ServerResourceMode.PROMPT);
        }

        return var1;
    }

    public String getBase64EncodedIconData()
    {
        return this.serverIcon;
    }

    public void setBase64EncodedIconData(String icon)
    {
        this.serverIcon = icon;
    }

    public void copyFrom(ServerData serverDataIn)
    {
        this.serverIP = serverDataIn.serverIP;
        this.serverName = serverDataIn.serverName;
        this.setResourceMode(serverDataIn.getResourceMode());
        this.serverIcon = serverDataIn.serverIcon;
    }

    public boolean isLanServer()
    {
        return this.lanServer;
    }

    public enum ServerResourceMode
    {
        ENABLED("ENABLED", 0, "enabled"),
        DISABLED("DISABLED", 1, "disabled"),
        PROMPT("PROMPT", 2, "prompt");
        private final IChatComponent motd;

        private static final ServerData.ServerResourceMode[] $VALUES = {ENABLED, DISABLED, PROMPT};
        private static final String __OBFID = "CL_00001833";

        ServerResourceMode(String p_i1053_1_, int p_i1053_2_, String p_i1053_3_)
        {
            this.motd = new ChatComponentTranslation("addServer.resourcePack." + p_i1053_3_);
        }

        public IChatComponent getMotd()
        {
            return this.motd;
        }
    }
}