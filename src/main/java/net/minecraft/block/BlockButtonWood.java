package net.minecraft.block;

import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class BlockButtonWood extends BlockButton
{

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