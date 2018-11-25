package net.minecraft.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockPotato extends BlockCrops
{
    private IIcon[] field_149869_a;
    private static final String __OBFID = "CL_00000286";

    /**
     * Gets the block's texture. Args: side, meta
     */
    public IIcon getIcon(int side, int meta)
    {
        if (meta < 7)
        {
            if (meta == 6)
            {
                meta = 5;
            }

            return this.field_149869_a[meta >> 1];
        }
        else
        {
            return this.field_149869_a[3];
        }
    }

    protected Item getSeed()
    {
        return Items.potato;
    }

    protected Item getCrop()
    {
        return Items.potato;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);

        if (!worldIn.isClient)
        {
            if (meta >= 7 && worldIn.rand.nextInt(50) == 0)
            {
                this.dropBlockAsItem_do(worldIn, x, y, z, new ItemStack(Items.poisonous_potato));
            }
        }
    }

    public void registerBlockIcons(IIconRegister reg)
    {
        this.field_149869_a = new IIcon[4];

        for (int var2 = 0; var2 < this.field_149869_a.length; ++var2)
        {
            this.field_149869_a[var2] = reg.registerIcon(this.getTextureName() + "_stage_" + var2);
        }
    }
}