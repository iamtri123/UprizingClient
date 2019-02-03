package net.minecraft.block;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockButtonStone extends BlockButton
{

    protected BlockButtonStone()
    {
        super(false);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return Blocks.stone.getBlockTextureFromSide(1);
    }
}