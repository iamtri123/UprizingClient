package net.minecraft.command.server;

import io.netty.buffer.ByteBuf;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public abstract class CommandBlockLogic implements ICommandSender
{
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss");
    private int successCount;
    private boolean trackOutput = true;
    private IChatComponent lastOutput = null;
    private String commandStored = "";
    private String customName = "@";

    public int getSuccessCount()
    {
        return this.successCount;
    }

    public IChatComponent getLastOutput()
    {
        return this.lastOutput;
    }

    public void writeDataToNBT(NBTTagCompound p_145758_1_)
    {
        p_145758_1_.setString("Command", this.commandStored);
        p_145758_1_.setInteger("SuccessCount", this.successCount);
        p_145758_1_.setString("CustomName", this.customName);

        if (this.lastOutput != null)
        {
            p_145758_1_.setString("LastOutput", IChatComponent.Serializer.componentToJson(this.lastOutput));
        }

        p_145758_1_.setBoolean("TrackOutput", this.trackOutput);
    }

    public void readDataFromNBT(NBTTagCompound p_145759_1_)
    {
        this.commandStored = p_145759_1_.getString("Command");
        this.successCount = p_145759_1_.getInteger("SuccessCount");

        if (p_145759_1_.hasKey("CustomName", 8))
        {
            this.customName = p_145759_1_.getString("CustomName");
        }

        if (p_145759_1_.hasKey("LastOutput", 8))
        {
            this.lastOutput = IChatComponent.Serializer.jsonToComponent(p_145759_1_.getString("LastOutput"));
        }

        if (p_145759_1_.hasKey("TrackOutput", 1))
        {
            this.trackOutput = p_145759_1_.getBoolean("TrackOutput");
        }
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int permissionLevel, String command)
    {
        return permissionLevel <= 2;
    }

    public void setCommand(String p_145752_1_)
    {
        this.commandStored = p_145752_1_;
    }

    public String getCustomName()
    {
        return this.commandStored;
    }

    public void func_145755_a(World p_145755_1_)
    {
        if (p_145755_1_.isClient)
        {
            this.successCount = 0;
        }

        MinecraftServer var2 = MinecraftServer.getServer();

        if (var2 != null && var2.isCommandBlockEnabled())
        {
            ICommandManager var3 = var2.getCommandManager();
            this.successCount = var3.executeCommand(this, this.commandStored);
        }
        else
        {
            this.successCount = 0;
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return this.customName;
    }

    public IChatComponent getFormattedCommandSenderName()
    {
        return new ChatComponentText(this.getCommandSenderName());
    }

    public void func_145754_b(String p_145754_1_)
    {
        this.customName = p_145754_1_;
    }

    /**
     * Notifies this sender of some sort of information.  This is for messages intended to display to the user.  Used
     * for typical output (like "you asked for whether or not this game rule is set, so here's your answer"), warnings
     * (like "I fetched this block for you by ID, but I'd like you to know that every time you do this, I die a little
     * inside"), and errors (like "it's not called iron_pixacke, silly").
     */
    public void addChatMessage(IChatComponent message)
    {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isClient)
        {
            this.lastOutput = (new ChatComponentText("[" + timestampFormat.format(new Date()) + "] ")).appendSibling(message);
            this.func_145756_e();
        }
    }

    public abstract void func_145756_e();

    public abstract int func_145751_f();

    public abstract void func_145757_a(ByteBuf p_145757_1_);

    public void func_145750_b(IChatComponent p_145750_1_)
    {
        this.lastOutput = p_145750_1_;
    }
}