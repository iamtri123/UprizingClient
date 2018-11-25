package net.minecraft.command.server;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CommandTestForBlock extends CommandBase
{
    private static final String __OBFID = "CL_00001181";

    public String getCommandName()
    {
        return "testforblock";
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
        return "commands.testforblock.usage";
    }

    public void processCommand(ICommandSender sender, String[] args)
    {
        if (args.length >= 4)
        {
            int var3 = sender.getPlayerCoordinates().posX;
            int var4 = sender.getPlayerCoordinates().posY;
            int var5 = sender.getPlayerCoordinates().posZ;
            var3 = MathHelper.floor_double(clamp_coord(sender, (double)var3, args[0]));
            var4 = MathHelper.floor_double(clamp_coord(sender, (double)var4, args[1]));
            var5 = MathHelper.floor_double(clamp_coord(sender, (double)var5, args[2]));
            Block var6 = Block.getBlockFromName(args[3]);

            if (var6 == null)
            {
                throw new NumberInvalidException("commands.setblock.notFound", args[3]);
            }
            else
            {
                int var7 = -1;

                if (args.length >= 5)
                {
                    var7 = parseIntBounded(sender, args[4], -1, 15);
                }

                World var8 = sender.getEntityWorld();

                if (!var8.blockExists(var3, var4, var5))
                {
                    throw new CommandException("commands.testforblock.outOfWorld");
                }
                else
                {
                    NBTTagCompound var9 = new NBTTagCompound();
                    boolean var10 = false;

                    if (args.length >= 6 && var6.hasTileEntity())
                    {
                        String var11 = getChatComponentFromNthArg(sender, args, 5).getUnformattedText();

                        try
                        {
                            NBTBase var12 = JsonToNBT.func_150315_a(var11);

                            if (!(var12 instanceof NBTTagCompound))
                            {
                                throw new CommandException("commands.setblock.tagError", "Not a valid tag");
                            }

                            var9 = (NBTTagCompound)var12;
                            var10 = true;
                        }
                        catch (NBTException var14)
                        {
                            throw new CommandException("commands.setblock.tagError", var14.getMessage());
                        }
                    }

                    Block var15 = var8.getBlock(var3, var4, var5);

                    if (var15 != var6)
                    {
                        throw new CommandException("commands.testforblock.failed.tile", Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5), var15.getLocalizedName(), var6.getLocalizedName());
                    }
                    else
                    {
                        if (var7 > -1)
                        {
                            int var16 = var8.getBlockMetadata(var3, var4, var5);

                            if (var16 != var7)
                            {
                                throw new CommandException("commands.testforblock.failed.data", Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5), Integer.valueOf(var16), Integer.valueOf(var7));
                            }
                        }

                        if (var10)
                        {
                            TileEntity var17 = var8.getTileEntity(var3, var4, var5);

                            if (var17 == null)
                            {
                                throw new CommandException("commands.testforblock.failed.tileEntity", Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5));
                            }

                            NBTTagCompound var13 = new NBTTagCompound();
                            var17.writeToNBT(var13);

                            if (!this.func_147181_a(var9, var13))
                            {
                                throw new CommandException("commands.testforblock.failed.nbt", Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5));
                            }
                        }

                        sender.addChatMessage(new ChatComponentTranslation("commands.testforblock.success", Integer.valueOf(var3), Integer.valueOf(var4), Integer.valueOf(var5)));
                    }
                }
            }
        }
        else
        {
            throw new WrongUsageException("commands.testforblock.usage");
        }
    }

    public boolean func_147181_a(NBTBase p_147181_1_, NBTBase p_147181_2_)
    {
        if (p_147181_1_ == p_147181_2_)
        {
            return true;
        }
        else if (p_147181_1_ == null)
        {
            return true;
        }
        else if (p_147181_2_ == null)
        {
            return false;
        }
        else if (!p_147181_1_.getClass().equals(p_147181_2_.getClass()))
        {
            return false;
        }
        else if (p_147181_1_ instanceof NBTTagCompound)
        {
            NBTTagCompound var3 = (NBTTagCompound)p_147181_1_;
            NBTTagCompound var4 = (NBTTagCompound)p_147181_2_;
            Iterator var5 = var3.getKeySet().iterator();
            String var6;
            NBTBase var7;

            do
            {
                if (!var5.hasNext())
                {
                    return true;
                }

                var6 = (String)var5.next();
                var7 = var3.getTag(var6);
            }
            while (this.func_147181_a(var7, var4.getTag(var6)));

            return false;
        }
        else
        {
            return p_147181_1_.equals(p_147181_2_);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return args.length == 4 ? getListOfStringsFromIterableMatchingLastWord(args, Block.blockRegistry.getKeys()) : null;
    }
}