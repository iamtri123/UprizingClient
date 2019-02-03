package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;

public class ItemEmptyMap extends ItemMapBase
{

    protected ItemEmptyMap()
    {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        ItemStack var4 = new ItemStack(Items.filled_map, 1, worldIn.getUniqueDataId("map"));
        String var5 = "map_" + var4.getItemDamage();
        MapData var6 = new MapData(var5);
        worldIn.setItemData(var5, var6);
        var6.scale = 0;
        int var7 = 128 * (1 << var6.scale);
        var6.xCenter = (int)(Math.round(player.posX / (double)var7) * (long)var7);
        var6.zCenter = (int)(Math.round(player.posZ / (double)var7) * (long)var7);
        var6.dimension = (byte)worldIn.provider.dimensionId;
        var6.markDirty();
        --itemStackIn.stackSize;

        if (itemStackIn.stackSize <= 0)
        {
            return var4;
        }
        else
        {
            if (!player.inventory.addItemStackToInventory(var4.copy()))
            {
                player.dropPlayerItemWithRandomChoice(var4, false);
            }

            return itemStackIn;
        }
    }
}