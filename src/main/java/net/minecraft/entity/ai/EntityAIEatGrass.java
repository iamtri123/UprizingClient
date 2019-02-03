package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityAIEatGrass extends EntityAIBase
{
    private final EntityLiving grassEaterEntity;
    private final World entityWorld;
    int eatingGrassTimer;

    public EntityAIEatGrass(EntityLiving p_i45314_1_)
    {
        this.grassEaterEntity = p_i45314_1_;
        this.entityWorld = p_i45314_1_.worldObj;
        this.setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.grassEaterEntity.getRNG().nextInt(this.grassEaterEntity.isChild() ? 50 : 1000) != 0)
        {
            return false;
        }
        else
        {
            int var1 = MathHelper.floor_double(this.grassEaterEntity.posX);
            int var2 = MathHelper.floor_double(this.grassEaterEntity.posY);
            int var3 = MathHelper.floor_double(this.grassEaterEntity.posZ);
            return this.entityWorld.getBlock(var1, var2, var3) == Blocks.tallgrass && this.entityWorld.getBlockMetadata(var1, var2, var3) == 1 || this.entityWorld.getBlock(var1, var2 - 1, var3) == Blocks.grass;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.grassEaterEntity, (byte)10);
        this.grassEaterEntity.getNavigator().clearPathEntity();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.eatingGrassTimer > 0;
    }

    public int getEatingGrassTimer()
    {
        return this.eatingGrassTimer;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);

        if (this.eatingGrassTimer == 4)
        {
            int var1 = MathHelper.floor_double(this.grassEaterEntity.posX);
            int var2 = MathHelper.floor_double(this.grassEaterEntity.posY);
            int var3 = MathHelper.floor_double(this.grassEaterEntity.posZ);

            if (this.entityWorld.getBlock(var1, var2, var3) == Blocks.tallgrass)
            {
                if (this.entityWorld.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                {
                    this.entityWorld.breakBlock(var1, var2, var3, false);
                }

                this.grassEaterEntity.eatGrassBonus();
            }
            else if (this.entityWorld.getBlock(var1, var2 - 1, var3) == Blocks.grass)
            {
                if (this.entityWorld.getGameRules().getGameRuleBooleanValue("mobGriefing"))
                {
                    this.entityWorld.playAuxSFX(2001, var1, var2 - 1, var3, Block.getIdFromBlock(Blocks.grass));
                    this.entityWorld.setBlock(var1, var2 - 1, var3, Blocks.dirt, 0, 2);
                }

                this.grassEaterEntity.eatGrassBonus();
            }
        }
    }
}