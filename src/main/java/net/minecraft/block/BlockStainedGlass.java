package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStainedGlass extends BlockBreakable
{
    private static final IIcon[] field_149998_a = new IIcon[16];

    public BlockStainedGlass(Material p_i45427_1_)
    {
        super("glass", p_i45427_1_, false);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return field_149998_a[meta % field_149998_a.length];
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta;
    }

    public static int func_149997_b(int p_149997_0_)
    {
        return ~p_149997_0_ & 15;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < field_149998_a.length; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        for (int var2 = 0; var2 < field_149998_a.length; ++var2)
        {
            field_149998_a[var2] = reg.registerIcon(this.getTextureName() + "_" + ItemDye.dyeIcons[func_149997_b(var2)]);
        }
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }
}