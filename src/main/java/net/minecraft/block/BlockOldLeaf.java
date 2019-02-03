package net.minecraft.block;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockOldLeaf extends BlockLeaves
{
    public static final String[][] field_150130_N = {{"leaves_oak", "leaves_spruce", "leaves_birch", "leaves_jungle"}, {"leaves_oak_opaque", "leaves_spruce_opaque", "leaves_birch_opaque", "leaves_jungle_opaque"}};
    public static final String[] field_150131_O = {"oak", "spruce", "birch", "jungle"};

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int meta)
    {
        return (meta & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((meta & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : super.getRenderColor(meta));
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        return (var5 & 3) == 1 ? ColorizerFoliage.getFoliageColorPine() : ((var5 & 3) == 2 ? ColorizerFoliage.getFoliageColorBirch() : super.colorMultiplier(worldIn, x, y, z));
    }

    protected void func_150124_c(World p_150124_1_, int p_150124_2_, int p_150124_3_, int p_150124_4_, int p_150124_5_, int p_150124_6_)
    {
        if ((p_150124_5_ & 3) == 0 && p_150124_1_.rand.nextInt(p_150124_6_) == 0)
        {
            this.dropBlockAsItem_do(p_150124_1_, p_150124_2_, p_150124_3_, p_150124_4_, new ItemStack(Items.apple, 1, 0));
        }
    }

    protected int func_150123_b(int p_150123_1_)
    {
        int var2 = super.func_150123_b(p_150123_1_);

        if ((p_150123_1_ & 3) == 3)
        {
            var2 = 40;
        }

        return var2;
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return (meta & 3) == 1 ? this.field_150129_M[this.field_150127_b][1] : ((meta & 3) == 3 ? this.field_150129_M[this.field_150127_b][3] : ((meta & 3) == 2 ? this.field_150129_M[this.field_150127_b][2] : this.field_150129_M[this.field_150127_b][0]));
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
        list.add(new ItemStack(itemIn, 1, 3));
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        for (int var2 = 0; var2 < field_150130_N.length; ++var2)
        {
            this.field_150129_M[var2] = new IIcon[field_150130_N[var2].length];

            for (int var3 = 0; var3 < field_150130_N[var2].length; ++var3)
            {
                this.field_150129_M[var2][var3] = reg.registerIcon(field_150130_N[var2][var3]);
            }
        }
    }

    public String[] func_150125_e()
    {
        return field_150131_O;
    }
}