package net.minecraft.command;

import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandXP extends CommandBase
{
    private static final String __OBFID = "CL_00000398";

    public String getCommandName()
    {
        return "xp";
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
        return "commands.xp.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.xp.usage");
        }
        else
        {
            String var4 = args[0];
            boolean var5 = var4.endsWith("l") || var4.endsWith("L");

            if (var5 && var4.length() > 1)
            {
                var4 = var4.substring(0, var4.length() - 1);
            }

            int var6 = parseInt(sender, var4);
            boolean var7 = var6 < 0;

            if (var7)
            {
                var6 *= -1;
            }

            EntityPlayerMP var3;

            if (args.length > 1)
            {
                var3 = getPlayer(sender, args[1]);
            }
            else
            {
                var3 = getCommandSenderAsPlayer(sender);
            }

            if (var5)
            {
                if (var7)
                {
                    var3.addExperienceLevel(-var6);
                    notifyOperators(sender, this, "commands.xp.success.negative.levels", Integer.valueOf(var6), var3.getCommandSenderName());
                }
                else
                {
                    var3.addExperienceLevel(var6);
                    notifyOperators(sender, this, "commands.xp.success.levels", Integer.valueOf(var6), var3.getCommandSenderName());
                }
            }
            else
            {
                if (var7)
                {
                    throw new WrongUsageException("commands.xp.failure.widthdrawXp");
                }

                var3.addExperience(var6);
                notifyOperators(sender, this, "commands.xp.success", Integer.valueOf(var6), var3.getCommandSenderName());
            }
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 2 ? getListOfStringsMatchingLastWord(args, this.getAllUsernames()) : null;
    }

    protected String[] getAllUsernames()
    {
        return MinecraftServer.getServer().getAllUsernames();
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 1;
    }
}