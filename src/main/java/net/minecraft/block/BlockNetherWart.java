package net.minecraft.block;

import java.util.Random;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockNetherWart extends BlockBush
{
    private IIcon[] field_149883_a;
    private static final String __OBFID = "CL_00000274";

    protected BlockNetherWart()
    {
        this.setTickRandomly(true);
        float var1 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.0F, 0.5F - var1, 0.5F + var1, 0.25F, 0.5F + var1);
        this.setCreativeTab((CreativeTabs)null);
    }

    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.soul_sand;
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return this.canPlaceBlockOn(worldIn.getBlock(x, y - 1, z));
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if (var6 < 3 && random.nextInt(10) == 0)
        {
            ++var6;
            worldIn.setBlockMetadataWithNotify(x, y, z, var6, 2);
        }

        super.updateTick(worldIn, x, y, z, random);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return meta >= 3 ? this.field_149883_a[2] : (meta > 0 ? this.field_149883_a[1] : this.field_149883_a[0]);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 6;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        if (!worldIn.isClient)
        {
            int var8 = 1;

            if (meta >= 3)
            {
                var8 = 2 + worldIn.rand.nextInt(3);

                if (fortune > 0)
                {
                    var8 += worldIn.rand.nextInt(fortune + 1);
                }
            }

            for (int var9 = 0; var9 < var8; ++var9)
            {
                this.dropBlockAsItem_do(worldIn, x, y, z, new ItemStack(Items.nether_wart));
            }
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    /**
     * Gets an item for the block being called on. Args: world, x, y, z
     */
    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.nether_wart;
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        this.field_149883_a = new IIcon[3];

        for (int var2 = 0; var2 < this.field_149883_a.length; ++var2)
        {
            this.field_149883_a[var2] = reg.registerIcon(this.getTextureName() + "_stage_" + var2);
        }
    }
}