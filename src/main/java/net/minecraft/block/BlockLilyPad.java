package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLilyPad extends BlockBush
{
    private static final String __OBFID = "CL_00000332";

    protected BlockLilyPad()
    {
        float var1 = 0.5F;
        float var2 = 0.015625F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, var2, 0.5F + var1);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 23;
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        if (collider == null || !(collider instanceof EntityBoat))
        {
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return AxisAlignedBB.getBoundingBox((double)x + this.field_149759_B, (double)y + this.field_149760_C, (double)z + this.field_149754_D, (double)x + this.field_149755_E, (double)y + this.field_149756_F, (double)z + this.maxZ);
    }

    public int getBlockColor()
    {
        return 2129968;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int meta)
    {
        return 2129968;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return 2129968;
    }

    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.water;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return (y >= 0 && y < 256) && (worldIn.getBlock(x, y - 1, z).getMaterial() == Material.water && worldIn.getBlockMetadata(x, y - 1, z) == 0);
    }
}