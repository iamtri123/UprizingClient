package net.minecraft.command;

import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerTimeout extends CommandBase
{

    public String getCommandName()
    {
        return "setidletimeout";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.setidletimeout.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            int var3 = parseIntWithMin(sender, args[0], 0);
            MinecraftServer.getServer().setPlayerIdleTimeout(var3);
            notifyOperators(sender, this, "commands.setidletimeout.success", Integer.valueOf(var3));
        }
        else
        {
            throw new WrongUsageException("commands.setidletimeout.usage");
        }
    }
}