package net.minecraft.command;

import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.EnumDifficulty;

public class CommandDifficulty extends CommandBase
{

    public String getCommandName()
    {
        return "difficulty";
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
        return "commands.difficulty.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length > 0)
        {
            EnumDifficulty var3 = this.func_147201_h(sender, args[0]);
            MinecraftServer.getServer().setDifficultyForAllWorlds(var3);
            notifyOperators(sender, this, "commands.difficulty.success", new ChatComponentTranslation(var3.getDifficultyResourceKey()));
        }
        else
        {
            throw new WrongUsageException("commands.difficulty.usage");
        }
    }

    protected EnumDifficulty func_147201_h(ICommandSender p_147201_1_, String p_147201_2_)
    {
        return !p_147201_2_.equalsIgnoreCase("peaceful") && !p_147201_2_.equalsIgnoreCase("p") ? (!p_147201_2_.equalsIgnoreCase("easy") && !p_147201_2_.equalsIgnoreCase("e") ? (!p_147201_2_.equalsIgnoreCase("normal") && !p_147201_2_.equalsIgnoreCase("n") ? (!p_147201_2_.equalsIgnoreCase("hard") && !p_147201_2_.equalsIgnoreCase("h") ? EnumDifficulty.getDifficultyEnum(parseIntBounded(p_147201_1_, p_147201_2_, 0, 3)) : EnumDifficulty.HARD) : EnumDifficulty.NORMAL) : EnumDifficulty.EASY) : EnumDifficulty.PEACEFUL;
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, "peaceful", "easy", "normal", "hard"): null;
    }
}