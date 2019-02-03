package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailDetector extends BlockRailBase
{
    private IIcon[] field_150055_b;

    public BlockRailDetector()
    {
        super(true);
        this.setTickRandomly(true);
    }

    public int tickRate(World worldIn)
    {
        return 20;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (!worldIn.isClient)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if ((var6 & 8) == 0)
            {
                this.func_150054_a(worldIn, x, y, z, var6);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isClient)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if ((var6 & 8) != 0)
            {
                this.func_150054_a(worldIn, x, y, z, var6);
            }
        }
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return (worldIn.getBlockMetadata(x, y, z) & 8) != 0 ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return (worldIn.getBlockMetadata(x, y, z) & 8) == 0 ? 0 : (side == 1 ? 15 : 0);
    }

    private void func_150054_a(World p_150054_1_, int p_150054_2_, int p_150054_3_, int p_150054_4_, int p_150054_5_)
    {
        boolean var6 = (p_150054_5_ & 8) != 0;
        boolean var7 = false;
        float var8 = 0.125F;
        List var9 = p_150054_1_.getEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox((double)((float)p_150054_2_ + var8), (double)p_150054_3_, (double)((float)p_150054_4_ + var8), (double)((float)(p_150054_2_ + 1) - var8), (double)((float)(p_150054_3_ + 1) - var8), (double)((float)(p_150054_4_ + 1) - var8)));

        if (!var9.isEmpty())
        {
            var7 = true;
        }

        if (var7 && !var6)
        {
            p_150054_1_.setBlockMetadataWithNotify(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_5_ | 8, 3);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_, p_150054_4_, this);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_ - 1, p_150054_4_, this);
            p_150054_1_.markBlockRangeForRenderUpdate(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_2_, p_150054_3_, p_150054_4_);
        }

        if (!var7 && var6)
        {
            p_150054_1_.setBlockMetadataWithNotify(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_5_ & 7, 3);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_, p_150054_4_, this);
            p_150054_1_.notifyBlocksOfNeighborChange(p_150054_2_, p_150054_3_ - 1, p_150054_4_, this);
            p_150054_1_.markBlockRangeForRenderUpdate(p_150054_2_, p_150054_3_, p_150054_4_, p_150054_2_, p_150054_3_, p_150054_4_);
        }

        if (var7)
        {
            p_150054_1_.scheduleBlockUpdate(p_150054_2_, p_150054_3_, p_150054_4_, this, this.tickRate(p_150054_1_));
        }

        p_150054_1_.updateNeighborsAboutBlockChange(p_150054_2_, p_150054_3_, p_150054_4_, this);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        this.func_150054_a(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z));
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        if ((worldIn.getBlockMetadata(x, y, z) & 8) > 0)
        {
            float var6 = 0.125F;
            List var7 = worldIn.getEntitiesWithinAABB(EntityMinecartCommandBlock.class, AxisAlignedBB.getBoundingBox((double)((float)x + var6), (double)y, (double)((float)z + var6), (double)((float)(x + 1) - var6), (double)((float)(y + 1) - var6), (double)((float)(z + 1) - var6)));

            if (var7.size() > 0)
            {
                return ((EntityMinecartCommandBlock)var7.get(0)).func_145822_e().getSuccessCount();
            }

            List var8 = worldIn.selectEntitiesWithinAABB(EntityMinecart.class, AxisAlignedBB.getBoundingBox((double)((float)x + var6), (double)y, (double)((float)z + var6), (double)((float)(x + 1) - var6), (double)((float)(y + 1) - var6), (double)((float)(z + 1) - var6)), IEntitySelector.selectInventories);

            if (var8.size() > 0)
            {
                return Container.calcRedstoneFromInventory((IInventory)var8.get(0));
            }
        }

        return 0;
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        this.field_150055_b = new IIcon[2];
        this.field_150055_b[0] = reg.registerIcon(this.getTextureName());
        this.field_150055_b[1] = reg.registerIcon(this.getTextureName() + "_powered");
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return (meta & 8) != 0 ? this.field_150055_b[1] : this.field_150055_b[0];
    }
}