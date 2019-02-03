package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStoneBrick extends Block
{
    public static final String[] field_150142_a = {"default", "mossy", "cracked", "chiseled"};
    public static final String[] field_150141_b = {null, "mossy", "cracked", "carved"};
    private IIcon[] field_150143_M;

    public BlockStoneBrick()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        if (meta < 0 || meta >= field_150141_b.length)
        {
            meta = 0;
        }

        return this.field_150143_M[meta];
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < 4; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        this.field_150143_M = new IIcon[field_150141_b.length];

        for (int var2 = 0; var2 < this.field_150143_M.length; ++var2)
        {
            String var3 = this.getTextureName();

            if (field_150141_b[var2] != null)
            {
                var3 = var3 + "_" + field_150141_b[var2];
            }

            this.field_150143_M[var2] = reg.registerIcon(var3);
        }
    }
}