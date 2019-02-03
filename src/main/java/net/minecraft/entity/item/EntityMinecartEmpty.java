package net.minecraft.entity.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityMinecartEmpty extends EntityMinecart
{

    public EntityMinecartEmpty(World p_i1722_1_)
    {
        super(p_i1722_1_);
    }

    public EntityMinecartEmpty(World p_i1723_1_, double p_i1723_2_, double p_i1723_4_, double p_i1723_6_)
    {
        super(p_i1723_1_, p_i1723_2_, p_i1723_4_, p_i1723_6_);
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer player)
    {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != player)
        {
            return true;
        }
        else if (this.riddenByEntity != null && this.riddenByEntity != player)
        {
            return false;
        }
        else
        {
            if (!this.worldObj.isClient)
            {
                player.mountEntity(this);
            }

            return true;
        }
    }

    public int getMinecartType()
    {
        return 0;
    }
}