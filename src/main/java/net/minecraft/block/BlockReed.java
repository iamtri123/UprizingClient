package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockReed extends Block
{

    protected BlockReed()
    {
        super(Material.plants);
        float var1 = 0.375F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 1.0F, 0.5F + var1);
        this.setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.getBlock(x, y - 1, z) == Blocks.reeds || this.func_150170_e(worldIn, x, y, z))
        {
            if (worldIn.isAirBlock(x, y + 1, z))
            {
                int var6;

                for (var6 = 1; worldIn.getBlock(x, y - var6, z) == this; ++var6)
                {
                }

                if (var6 < 3)
                {
                    int var7 = worldIn.getBlockMetadata(x, y, z);

                    if (var7 == 15)
                    {
                        worldIn.setBlock(x, y + 1, z, this);
                        worldIn.setBlockMetadataWithNotify(x, y, z, 0, 4);
                    }
                    else
                    {
                        worldIn.setBlockMetadataWithNotify(x, y, z, var7 + 1, 4);
                    }
                }
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        Block var5 = worldIn.getBlock(x, y - 1, z);
        return var5 == this || (var5 == Blocks.grass || var5 == Blocks.dirt || var5 == Blocks.sand && (worldIn.getBlock(x - 1, y - 1, z).getMaterial() == Material.water || (worldIn.getBlock(x + 1, y - 1, z).getMaterial() == Material.water || (worldIn.getBlock(x, y - 1, z - 1).getMaterial() == Material.water || worldIn.getBlock(x, y - 1, z + 1).getMaterial() == Material.water))));
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.func_150170_e(worldIn, x, y, z);
    }

    protected final boolean func_150170_e(World p_150170_1_, int p_150170_2_, int p_150170_3_, int p_150170_4_)
    {
        if (!this.canBlockStay(p_150170_1_, p_150170_2_, p_150170_3_, p_150170_4_))
        {
            this.dropBlockAsItem(p_150170_1_, p_150170_2_, p_150170_3_, p_150170_4_, p_150170_1_.getBlockMetadata(p_150170_2_, p_150170_3_, p_150170_4_), 0);
            p_150170_1_.setBlockToAir(p_150170_2_, p_150170_3_, p_150170_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return this.canPlaceBlockAt(worldIn, x, y, z);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.reeds;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 1;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.reeds;
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        return worldIn.getBiomeGenForCoords(x, z).getBiomeGrassColor(x, y, z);
    }
}