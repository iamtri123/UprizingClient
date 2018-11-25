package net.minecraft.block;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockButtonWood extends BlockButton
{
    private static final String __OBFID = "CL_00000336";

    protected BlockButtonWood()
    {
        super(true);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.planks.getBlockTextureFromSide(1);
    }
}