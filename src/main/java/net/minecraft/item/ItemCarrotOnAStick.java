package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item
{

    public ItemCarrotOnAStick()
    {
        this.setCreativeTab(CreativeTabs.tabTransport);
        this.setMaxStackSize(1);
        this.setMaxDamage(25);
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D()
    {
        return true;
    }

    /**
     * Returns true if this item should be rotated by 180 degrees around the Y axis when being held in an entities
     * hands.
     */
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
        if (player.isRiding() && player.ridingEntity instanceof EntityPig)
        {
            EntityPig var4 = (EntityPig)player.ridingEntity;

            if (var4.getAIControlledByPlayer().isControlledByPlayer() && itemStackIn.getMaxDamage() - itemStackIn.getItemDamage() >= 7)
            {
                var4.getAIControlledByPlayer().boostSpeed();
                itemStackIn.damageItem(7, player);

                if (itemStackIn.stackSize == 0)
                {
                    ItemStack var5 = new ItemStack(Items.fishing_rod);
                    var5.setTagCompound(itemStackIn.stackTagCompound);
                    return var5;
                }
            }
        }

        return itemStackIn;
    }
}