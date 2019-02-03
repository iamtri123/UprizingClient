package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDirt extends Block
{
    public static final String[] field_150009_a = {"default", "default", "podzol"};
    private IIcon field_150008_b;
    private IIcon field_150010_M;

    protected BlockDirt()
    {
        super(Material.ground);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        if (meta == 2)
        {
            if (side == 1)
            {
                return this.field_150008_b;
            }

            if (side != 0)
            {
                return this.field_150010_M;
            }
        }

        return this.blockIcon;
    }

    public IIcon getIcon(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if (var6 == 2)
        {
            if (side == 1)
            {
                return this.field_150008_b;
            }

            if (side != 0)
            {
                Material var7 = worldIn.getBlock(x, y + 1, z).getMaterial();

                if (var7 == Material.snow || var7 == Material.craftedSnow)
                {
                    return Blocks.grass.getIcon(worldIn, x, y, z, side);
                }

                Block var8 = worldIn.getBlock(x, y + 1, z);

                if (var8 != Blocks.dirt && var8 != Blocks.grass)
                {
                    return this.field_150010_M;
                }
            }
        }

        return this.blockIcon;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return 0;
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int meta)
    {
        if (meta == 1)
        {
            meta = 0;
        }

        return super.createStackedBlock(meta);
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 2));
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        super.registerBlockIcons(reg);
        this.field_150008_b = reg.registerIcon(this.getTextureName() + "_" + "podzol_top");
        this.field_150010_M = reg.registerIcon(this.getTextureName() + "_" + "podzol_side");
    }

    /**
     * Get the block's damage value (for use with pick block).
     */
    public int getDamageValue(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);

        if (var5 == 1)
        {
            var5 = 0;
        }

        return var5;
    }
}