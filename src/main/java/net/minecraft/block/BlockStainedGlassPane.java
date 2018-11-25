package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStainedGlassPane extends BlockPane
{
    private static final IIcon[] field_150106_a = new IIcon[16];
    private static final IIcon[] field_150105_b = new IIcon[16];
    private static final String __OBFID = "CL_00000313";

    public BlockStainedGlassPane()
    {
        super("glass", "glass_pane_top", Material.glass, false);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public IIcon getItemIcon(int side, int meta)
    {
        return field_150106_a[meta % field_150106_a.length];
    }

    public IIcon func_150104_b(int p_150104_1_)
    {
        return field_150105_b[~p_150104_1_ & 15];
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return this.getItemIcon(side, ~meta & 15);
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int meta)
    {
        return meta;
    }

    public static int func_150103_c(int p_150103_0_)
    {
        return p_150103_0_ & 15;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < field_150106_a.length; ++var4)
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
        super.registerBlockIcons(reg);

        for (int var2 = 0; var2 < field_150106_a.length; ++var2)
        {
            field_150106_a[var2] = reg.registerIcon(this.getTextureName() + "_" + ItemDye.dyeIcons[func_150103_c(var2)]);
            field_150105_b[var2] = reg.registerIcon(this.getTextureName() + "_pane_top_" + ItemDye.dyeIcons[func_150103_c(var2)]);
        }
    }
}