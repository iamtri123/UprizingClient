package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockSand extends BlockFalling
{
    public static final String[] field_149838_a = {"default", "red"};
    private static IIcon sandIcon;
    private static IIcon redSandIcon;

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        return meta == 1 ? redSandIcon : sandIcon;
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        sandIcon = reg.registerIcon("sand");
        redSandIcon = reg.registerIcon("red_sand");
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
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    public MapColor getMapColor(int meta)
    {
        return meta == 1 ? MapColor.dirtColor : MapColor.sandColor;
    }
}