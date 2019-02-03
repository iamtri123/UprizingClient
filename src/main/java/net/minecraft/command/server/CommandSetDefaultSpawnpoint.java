package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChunkCoordinates;

public class CommandSetDefaultSpawnpoint extends CommandBase
{

    public String getCommandName()
    {
        return "setworldspawn";
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
        return "commands.setworldspawn.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length == 3)
        {
            if (sender.getEntityWorld() == null)
            {
                throw new WrongUsageException("commands.setworldspawn.usage");
            }

            byte var3 = 0;
            int var7 = var3 + 1;
            int var4 = parseIntBounded(sender, args[var3], -30000000, 30000000);
            int var5 = parseIntBounded(sender, args[var7++], 0, 256);
            int var6 = parseIntBounded(sender, args[var7++], -30000000, 30000000);
            sender.getEntityWorld().setSpawnLocation(var4, var5, var6);
            notifyOperators(sender, this, "commands.setworldspawn.success", Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var6));
        }
        else
        {
            if (args.length != 0)
            {
                throw new WrongUsageException("commands.setworldspawn.usage");
            }

            ChunkCoordinates var8 = getCommandSenderAsPlayer(sender).getPlayerCoordinates();
            sender.getEntityWorld().setSpawnLocation(var8.posX, var8.posY, var8.posZ);
            notifyOperators(sender, this, "commands.setworldspawn.success", Integer.valueOf(var8.posX), Integer.valueOf(var8.posY), Integer.valueOf(var8.posZ));
        }
    }
}