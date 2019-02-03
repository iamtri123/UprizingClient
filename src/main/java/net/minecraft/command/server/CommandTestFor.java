package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

public class CommandTestFor extends CommandBase
{

    public String getCommandName()
    {
        return "testfor";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.testfor.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length != 1)
        {
            throw new WrongUsageException("commands.testfor.usage");
        }
        else if (!(sender instanceof CommandBlockLogic))
        {
            throw new CommandException("commands.testfor.failed");
        }
        else
        {
            getPlayer(sender, args[0]);
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}