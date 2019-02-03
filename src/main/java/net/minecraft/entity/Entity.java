package net.minecraft.entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public abstract class Entity
{
    private static int nextEntityID;
    private int entityId;
    public double renderDistanceWeight;

    /**
     * Blocks entities from spawning when they do their AABB check to make sure the spot is clear of entities that can
     * prevent spawning.
     */
    public boolean preventEntitySpawning;

    /** The entity that is riding this entity */
    public Entity riddenByEntity;

    /** The entity we are currently riding */
    public Entity ridingEntity;
    public boolean forceSpawn;

    /** Reference to the World object. */
    public World worldObj;
    public double prevPosX;
    public double prevPosY;
    public double prevPosZ;

    /** Entity position X */
    public double posX;

    /** Entity position Y */
    public double posY;

    /** Entity position Z */
    public double posZ;

    /** Entity motion X */
    public double motionX;

    /** Entity motion Y */
    public double motionY;

    /** Entity motion Z */
    public double motionZ;

    /** Entity rotation Yaw */
    public float rotationYaw;

    /** Entity rotation Pitch */
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;

    /** Axis aligned bounding box. */
    public final AxisAlignedBB boundingBox;
    public boolean onGround;

    /**
     * True if after a move this entity has collided with something on X- or Z-axis
     */
    public boolean isCollidedHorizontally;

    /**
     * True if after a move this entity has collided with something on Y-axis
     */
    public boolean isCollidedVertically;

    /**
     * True if after a move this entity has collided with something either vertically or horizontally
     */
    public boolean isCollided;
    public boolean velocityChanged;
    protected boolean isInWeb;
    public boolean field_70135_K;

    /**
     * Gets set by setDead, so this must be the flag whether an Entity is dead (inactive may be better term)
     */
    public boolean isDead;
    public float yOffset;

    /** How wide this entity is considered to be */
    public float width;

    /** How high this entity is considered to be */
    public float height;

    /** The previous ticks distance walked multiplied by 0.6 */
    public float prevDistanceWalkedModified;

    /** The distance walked multiplied by 0.6 */
    public float distanceWalkedModified;
    public float distanceWalkedOnStepModified;
    public float fallDistance;

    /**
     * The distance that has to be exceeded in order to triger a new step sound and an onEntityWalking event on a block
     */
    private int nextStepDistance;

    /**
     * The entity's X coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosX;

    /**
     * The entity's Y coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosY;

    /**
     * The entity's Z coordinate at the previous tick, used to calculate position during rendering routines
     */
    public double lastTickPosZ;
    public float ySize;

    /**
     * How high this entity can step up when running into a block to try to get over it (currently make note the entity
     * will always step up this amount and not just the amount needed)
     */
    public float stepHeight;

    /**
     * Whether this entity won't clip with collision or not (make note it won't disable gravity)
     */
    public boolean noClip;

    /**
     * Reduces the velocity applied by entity collisions by the specified percent.
     */
    public float entityCollisionReduction;
    protected Random rand;

    /** How many ticks has this entity had ran since being alive */
    public int ticksExisted;

    /**
     * The amount of ticks you have to stand inside of fire before be set on fire
     */
    public int fireResistance;
    private int fire;

    /**
     * Whether this entity is currently inside of water (if it handles water movement that is)
     */
    protected boolean inWater;

    /**
     * Remaining time an entity will be "immune" to further damage after being hurt.
     */
    public int hurtResistantTime;
    private boolean firstUpdate;
    protected boolean isImmuneToFire;
    protected DataWatcher dataWatcher;
    private double entityRiderPitchDelta;
    private double entityRiderYawDelta;

    /** Has this entity been added to the chunk its within */
    public boolean addedToChunk;
    public int chunkCoordX;
    public int chunkCoordY;
    public int chunkCoordZ;
    public int serverPosX;
    public int serverPosY;
    public int serverPosZ;

    /**
     * Render entity even if it is outside the camera frustum. Only true in EntityFish for now. Used in RenderGlobal:
     * render if ignoreFrustumCheck or in frustum.
     */
    public boolean ignoreFrustumCheck;
    public boolean isAirBorne;
    public int timeUntilPortal;

    /** Whether the entity is inside a Portal */
    protected boolean inPortal;
    protected int portalCounter;

    /** Which dimension the player is in (-1 = the Nether, 0 = normal world) */
    public int dimension;
    protected int teleportDirection;
    private boolean invulnerable;
    protected UUID entityUniqueID;
    public Entity.EnumEntitySize myEntitySize;

    public int getEntityId()
    {
        return this.entityId;
    }

    public void setEntityId(int id)
    {
        this.entityId = id;
    }

    public Entity(World worldIn)
    {
        this.entityId = nextEntityID++;
        this.renderDistanceWeight = 1.0D;
        this.boundingBox = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.field_70135_K = true;
        this.width = 0.6F;
        this.height = 1.8F;
        this.nextStepDistance = 1;
        this.rand = new Random();
        this.fireResistance = 1;
        this.firstUpdate = true;
        this.entityUniqueID = UUID.randomUUID();
        this.myEntitySize = Entity.EnumEntitySize.SIZE_2;
        this.worldObj = worldIn;
        this.setPosition(0.0D, 0.0D, 0.0D);

        if (worldIn != null)
        {
            this.dimension = worldIn.provider.dimensionId;
        }

        this.dataWatcher = new DataWatcher(this);
        this.dataWatcher.addObject(0, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(1, Short.valueOf((short)300));
        this.entityInit();
    }

    protected abstract void entityInit();

    public DataWatcher getDataWatcher()
    {
        return this.dataWatcher;
    }

    public boolean equals(Object p_equals_1_)
    {
        return p_equals_1_ instanceof Entity && ((Entity) p_equals_1_).entityId == this.entityId;
    }

    public int hashCode()
    {
        return this.entityId;
    }

    /**
     * Keeps moving the entity up so it isn't colliding with blocks and other requirements for this entity to be spawned
     * (only actually used on players though its also on Entity)
     */
    protected void preparePlayerToSpawn()
    {
        if (this.worldObj != null)
        {
            while (this.posY > 0.0D)
            {
                this.setPosition(this.posX, this.posY, this.posZ);

                if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty())
                {
                    break;
                }

                ++this.posY;
            }

            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.rotationPitch = 0.0F;
        }
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        this.isDead = true;
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float width, float height)
    {
        float var3;

        if (width != this.width || height != this.height)
        {
            var3 = this.width;
            this.width = width;
            this.height = height;
            this.boundingBox.maxX = this.boundingBox.minX + (double)this.width;
            this.boundingBox.maxZ = this.boundingBox.minZ + (double)this.width;
            this.boundingBox.maxY = this.boundingBox.minY + (double)this.height;

            if (this.width > var3 && !this.firstUpdate && !this.worldObj.isClient)
            {
                this.moveEntity((double)(var3 - this.width), 0.0D, (double)(var3 - this.width));
            }
        }

        var3 = width % 2.0F;

        if ((double)var3 < 0.375D)
        {
            this.myEntitySize = Entity.EnumEntitySize.SIZE_1;
        }
        else if ((double)var3 < 0.75D)
        {
            this.myEntitySize = Entity.EnumEntitySize.SIZE_2;
        }
        else if ((double)var3 < 1.0D)
        {
            this.myEntitySize = Entity.EnumEntitySize.SIZE_3;
        }
        else if ((double)var3 < 1.375D)
        {
            this.myEntitySize = Entity.EnumEntitySize.SIZE_4;
        }
        else if ((double)var3 < 1.75D)
        {
            this.myEntitySize = Entity.EnumEntitySize.SIZE_5;
        }
        else
        {
            this.myEntitySize = Entity.EnumEntitySize.SIZE_6;
        }
    }

    /**
     * Sets the rotation of the entity
     */
    protected void setRotation(float yaw, float pitch)
    {
        this.rotationYaw = yaw % 360.0F;
        this.rotationPitch = pitch % 360.0F;
    }

    /**
     * Sets the x,y,z of the entity from the given parameters. Also seems to set up a bounding box.
     */
    public void setPosition(double x, double y, double z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        float var7 = this.width / 2.0F;
        float var8 = this.height;
        this.boundingBox.setBounds(x - (double)var7, y - (double)this.yOffset + (double)this.ySize, z - (double)var7, x + (double)var7, y - (double)this.yOffset + (double)this.ySize + (double)var8, z + (double)var7);
    }

    /**
     * Adds par1*0.15 to the entity's yaw, and *subtracts* par2*0.15 from the pitch. Clamps pitch from -90 to 90. Both
     * arguments in degrees.
     */
    public void setAngles(float yaw, float pitch)
    {
        float var3 = this.rotationPitch;
        float var4 = this.rotationYaw;
        this.rotationYaw = (float)((double)this.rotationYaw + (double)yaw * 0.15D);
        this.rotationPitch = (float)((double)this.rotationPitch - (double)pitch * 0.15D);

        if (this.rotationPitch < -90.0F)
        {
            this.rotationPitch = -90.0F;
        }

        if (this.rotationPitch > 90.0F)
        {
            this.rotationPitch = 90.0F;
        }

        this.prevRotationPitch += this.rotationPitch - var3;
        this.prevRotationYaw += this.rotationYaw - var4;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        this.onEntityUpdate();
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        if (this.ridingEntity != null && this.ridingEntity.isDead)
        {
            this.ridingEntity = null;
        }

        this.prevDistanceWalkedModified = this.distanceWalkedModified;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRotationPitch = this.rotationPitch;
        this.prevRotationYaw = this.rotationYaw;
        int var2;

        if (!this.worldObj.isClient && this.worldObj instanceof WorldServer)
        {
            MinecraftServer var1 = ((WorldServer)this.worldObj).func_73046_m();
            var2 = this.getMaxInPortalTime();

            if (this.inPortal)
            {
                if (var1.getAllowNether())
                {
                    if (this.ridingEntity == null && this.portalCounter++ >= var2)
                    {
                        this.portalCounter = var2;
                        this.timeUntilPortal = this.getPortalCooldown();
                        byte var3;

                        if (this.worldObj.provider.dimensionId == -1)
                        {
                            var3 = 0;
                        }
                        else
                        {
                            var3 = -1;
                        }

                        this.travelToDimension(var3);
                    }

                    this.inPortal = false;
                }
            }
            else
            {
                if (this.portalCounter > 0)
                {
                    this.portalCounter -= 4;
                }

                if (this.portalCounter < 0)
                {
                    this.portalCounter = 0;
                }
            }

            if (this.timeUntilPortal > 0)
            {
                --this.timeUntilPortal;
            }
        }

        if (this.isSprinting() && !this.isInWater())
        {
            int var5 = MathHelper.floor_double(this.posX);
            var2 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
            int var6 = MathHelper.floor_double(this.posZ);
            Block var4 = this.worldObj.getBlock(var5, var2, var6);

            if (var4.getMaterial() != Material.air)
            {
                this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(var4) + "_" + this.worldObj.getBlockMetadata(var5, var2, var6), this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.boundingBox.minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);
            }
        }

        this.handleWaterMovement();

        if (this.worldObj.isClient)
        {
            this.fire = 0;
        }
        else if (this.fire > 0)
        {
            if (this.isImmuneToFire)
            {
                this.fire -= 4;

                if (this.fire < 0)
                {
                    this.fire = 0;
                }
            }
            else
            {
                if (this.fire % 20 == 0)
                {
                    this.attackEntityFrom(DamageSource.onFire, 1.0F);
                }

                --this.fire;
            }
        }

        if (this.handleLavaMovement())
        {
            this.setOnFireFromLava();
            this.fallDistance *= 0.5F;
        }

        if (this.posY < -64.0D)
        {
            this.kill();
        }

        if (!this.worldObj.isClient)
        {
            this.setFlag(0, this.fire > 0);
        }

        this.firstUpdate = false;
    }

    /**
     * Return the amount of time this entity should stay in a portal before being transported.
     */
    public int getMaxInPortalTime()
    {
        return 0;
    }

    /**
     * Called whenever the entity is walking inside of lava.
     */
    protected void setOnFireFromLava()
    {
        if (!this.isImmuneToFire)
        {
            this.attackEntityFrom(DamageSource.lava, 4.0F);
            this.setFire(15);
        }
    }

    /**
     * Sets entity to burn for x amount of seconds, cannot lower amount of existing fire.
     */
    public void setFire(int seconds)
    {
        int var2 = seconds * 20;
        var2 = EnchantmentProtection.getFireTimeForEntity(this, var2);

        if (this.fire < var2)
        {
            this.fire = var2;
        }
    }

    /**
     * Removes fire from entity.
     */
    public void extinguish()
    {
        this.fire = 0;
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
        this.setDead();
    }

    /**
     * Checks if the offset position from the entity's current position is inside of liquid. Args: x, y, z
     */
    public boolean isOffsetPositionInLiquid(double x, double y, double z)
    {
        AxisAlignedBB var7 = this.boundingBox.getOffsetBoundingBox(x, y, z);
        List var8 = this.worldObj.getCollidingBoundingBoxes(this, var7);
        return var8.isEmpty() && !this.worldObj.isAnyLiquid(var7);
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    public void moveEntity(double x, double y, double z)
    {
        if (this.noClip)
        {
            this.boundingBox.offset(x, y, z);
            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
            this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
        }
        else
        {
            this.ySize *= 0.4F;
            double var7 = this.posX;
            double var9 = this.posY;
            double var11 = this.posZ;

            if (this.isInWeb)
            {
                this.isInWeb = false;
                x *= 0.25D;
                y *= 0.05000000074505806D;
                z *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double var13 = x;
            double var15 = y;
            double var17 = z;
            AxisAlignedBB var19 = this.boundingBox.copy();
            boolean var20 = this.onGround && this.isSneaking() && this instanceof EntityPlayer;

            if (var20)
            {
                double var21;

                for (var21 = 0.05D; x != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(x, -1.0D, 0.0D)).isEmpty(); var13 = x)
                {
                    if (x < var21 && x >= -var21)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= var21;
                    }
                    else
                    {
                        x += var21;
                    }
                }

                for (; z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0D, -1.0D, z)).isEmpty(); var17 = z)
                {
                    if (z < var21 && z >= -var21)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= var21;
                    }
                    else
                    {
                        z += var21;
                    }
                }

                while (x != 0.0D && z != 0.0D && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(x, -1.0D, z)).isEmpty())
                {
                    if (x < var21 && x >= -var21)
                    {
                        x = 0.0D;
                    }
                    else if (x > 0.0D)
                    {
                        x -= var21;
                    }
                    else
                    {
                        x += var21;
                    }

                    if (z < var21 && z >= -var21)
                    {
                        z = 0.0D;
                    }
                    else if (z > 0.0D)
                    {
                        z -= var21;
                    }
                    else
                    {
                        z += var21;
                    }

                    var13 = x;
                    var17 = z;
                }
            }

            List var36 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(x, y, z));

            for (int var22 = 0; var22 < var36.size(); ++var22)
            {
                y = ((AxisAlignedBB)var36.get(var22)).calculateYOffset(this.boundingBox, y);
            }

            this.boundingBox.offset(0.0D, y, 0.0D);

            if (!this.field_70135_K && var15 != y)
            {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            }

            boolean var37 = this.onGround || var15 != y && var15 < 0.0D;
            int var23;

            for (var23 = 0; var23 < var36.size(); ++var23)
            {
                x = ((AxisAlignedBB)var36.get(var23)).calculateXOffset(this.boundingBox, x);
            }

            this.boundingBox.offset(x, 0.0D, 0.0D);

            if (!this.field_70135_K && var13 != x)
            {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            }

            for (var23 = 0; var23 < var36.size(); ++var23)
            {
                z = ((AxisAlignedBB)var36.get(var23)).calculateZOffset(this.boundingBox, z);
            }

            this.boundingBox.offset(0.0D, 0.0D, z);

            if (!this.field_70135_K && var17 != z)
            {
                z = 0.0D;
                y = 0.0D;
                x = 0.0D;
            }

            double var25;
            double var27;
            int var30;
            double var38;

            if (this.stepHeight > 0.0F && var37 && (var20 || this.ySize < 0.05F) && (var13 != x || var17 != z))
            {
                var38 = x;
                var25 = y;
                var27 = z;
                x = var13;
                y = (double)this.stepHeight;
                z = var17;
                AxisAlignedBB var29 = this.boundingBox.copy();
                this.boundingBox.setBB(var19);
                var36 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(var13, y, var17));

                for (var30 = 0; var30 < var36.size(); ++var30)
                {
                    y = ((AxisAlignedBB)var36.get(var30)).calculateYOffset(this.boundingBox, y);
                }

                this.boundingBox.offset(0.0D, y, 0.0D);

                if (!this.field_70135_K && var15 != y)
                {
                    z = 0.0D;
                    y = 0.0D;
                    x = 0.0D;
                }

                for (var30 = 0; var30 < var36.size(); ++var30)
                {
                    x = ((AxisAlignedBB)var36.get(var30)).calculateXOffset(this.boundingBox, x);
                }

                this.boundingBox.offset(x, 0.0D, 0.0D);

                if (!this.field_70135_K && var13 != x)
                {
                    z = 0.0D;
                    y = 0.0D;
                    x = 0.0D;
                }

                for (var30 = 0; var30 < var36.size(); ++var30)
                {
                    z = ((AxisAlignedBB)var36.get(var30)).calculateZOffset(this.boundingBox, z);
                }

                this.boundingBox.offset(0.0D, 0.0D, z);

                if (!this.field_70135_K && var17 != z)
                {
                    z = 0.0D;
                    y = 0.0D;
                    x = 0.0D;
                }

                if (!this.field_70135_K && var15 != y)
                {
                    z = 0.0D;
                    y = 0.0D;
                    x = 0.0D;
                }
                else
                {
                    y = (double)(-this.stepHeight);

                    for (var30 = 0; var30 < var36.size(); ++var30)
                    {
                        y = ((AxisAlignedBB)var36.get(var30)).calculateYOffset(this.boundingBox, y);
                    }

                    this.boundingBox.offset(0.0D, y, 0.0D);
                }

                if (var38 * var38 + var27 * var27 >= x * x + z * z)
                {
                    x = var38;
                    y = var25;
                    z = var27;
                    this.boundingBox.setBB(var29);
                }
            }

            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
            this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
            this.isCollidedHorizontally = var13 != x || var17 != z;
            this.isCollidedVertically = var15 != y;
            this.onGround = var15 != y && var15 < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            this.updateFallState(y, this.onGround);

            if (var13 != x)
            {
                this.motionX = 0.0D;
            }

            if (var15 != y)
            {
                this.motionY = 0.0D;
            }

            if (var17 != z)
            {
                this.motionZ = 0.0D;
            }

            var38 = this.posX - var7;
            var25 = this.posY - var9;
            var27 = this.posZ - var11;

            if (this.canTriggerWalking() && !var20 && this.ridingEntity == null)
            {
                int var39 = MathHelper.floor_double(this.posX);
                var30 = MathHelper.floor_double(this.posY - 0.20000000298023224D - (double)this.yOffset);
                int var31 = MathHelper.floor_double(this.posZ);
                Block var32 = this.worldObj.getBlock(var39, var30, var31);
                int var33 = this.worldObj.getBlock(var39, var30 - 1, var31).getRenderType();

                if (var33 == 11 || var33 == 32 || var33 == 21)
                {
                    var32 = this.worldObj.getBlock(var39, var30 - 1, var31);
                }

                if (var32 != Blocks.ladder)
                {
                    var25 = 0.0D;
                }

                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(var38 * var38 + var27 * var27) * 0.6D);
                this.distanceWalkedOnStepModified = (float)((double)this.distanceWalkedOnStepModified + (double)MathHelper.sqrt_double(var38 * var38 + var25 * var25 + var27 * var27) * 0.6D);

                if (this.distanceWalkedOnStepModified > (float)this.nextStepDistance && var32.getMaterial() != Material.air)
                {
                    this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;

                    if (this.isInWater())
                    {
                        float var34 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.35F;

                        if (var34 > 1.0F)
                        {
                            var34 = 1.0F;
                        }

                        this.playSound(this.getSwimSound(), var34, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    }

                    this.playStepSound(var39, var30, var31, var32);
                    var32.onEntityWalking(this.worldObj, var39, var30, var31, this);
                }
            }

            try
            {
                this.doBlockCollisions();
            }
            catch (Throwable var35)
            {
                CrashReport var41 = CrashReport.makeCrashReport(var35, "Checking entity block collision");
                CrashReportCategory var42 = var41.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(var42);
                throw new ReportedException(var41);
            }

            boolean var40 = this.isWet();

            if (this.worldObj.func_147470_e(this.boundingBox.contract(0.001D, 0.001D, 0.001D)))
            {
                this.dealFireDamage(1);

                if (!var40)
                {
                    ++this.fire;

                    if (this.fire == 0)
                    {
                        this.setFire(8);
                    }
                }
            }
            else if (this.fire <= 0)
            {
                this.fire = -this.fireResistance;
            }

            if (var40 && this.fire > 0)
            {
                this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.fireResistance;
            }
        }
    }

    protected String getSwimSound()
    {
        return "game.neutral.swim";
    }

    protected void doBlockCollisions()
    {
        int var1 = MathHelper.floor_double(this.boundingBox.minX + 0.001D);
        int var2 = MathHelper.floor_double(this.boundingBox.minY + 0.001D);
        int var3 = MathHelper.floor_double(this.boundingBox.minZ + 0.001D);
        int var4 = MathHelper.floor_double(this.boundingBox.maxX - 0.001D);
        int var5 = MathHelper.floor_double(this.boundingBox.maxY - 0.001D);
        int var6 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001D);

        if (this.worldObj.checkChunksExist(var1, var2, var3, var4, var5, var6))
        {
            for (int var7 = var1; var7 <= var4; ++var7)
            {
                for (int var8 = var2; var8 <= var5; ++var8)
                {
                    for (int var9 = var3; var9 <= var6; ++var9)
                    {
                        Block var10 = this.worldObj.getBlock(var7, var8, var9);

                        try
                        {
                            var10.onEntityCollidedWithBlock(this.worldObj, var7, var8, var9, this);
                        }
                        catch (Throwable var14)
                        {
                            CrashReport var12 = CrashReport.makeCrashReport(var14, "Colliding entity with block");
                            CrashReportCategory var13 = var12.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(var13, var7, var8, var9, var10, this.worldObj.getBlockMetadata(var7, var8, var9));
                            throw new ReportedException(var12);
                        }
                    }
                }
            }
        }
    }

    protected void playStepSound(int x, int y, int z, Block blockIn)
    {
        Block.SoundType var5 = blockIn.stepSound;

        if (this.worldObj.getBlock(x, y + 1, z) == Blocks.snow_layer)
        {
            var5 = Blocks.snow_layer.stepSound;
            this.playSound(var5.func_150498_e(), var5.func_150497_c() * 0.15F, var5.func_150494_d());
        }
        else if (!blockIn.getMaterial().isLiquid())
        {
            this.playSound(var5.func_150498_e(), var5.func_150497_c() * 0.15F, var5.func_150494_d());
        }
    }

    public void playSound(String name, float volume, float pitch)
    {
        this.worldObj.playSoundAtEntity(this, name, volume, pitch);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return true;
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double distanceFallenThisTick, boolean isOnGround)
    {
        if (isOnGround)
        {
            if (this.fallDistance > 0.0F)
            {
                this.fall(this.fallDistance);
                this.fallDistance = 0.0F;
            }
        }
        else if (distanceFallenThisTick < 0.0D)
        {
            this.fallDistance = (float)((double)this.fallDistance - distanceFallenThisTick);
        }
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox()
    {
        return null;
    }

    /**
     * Will deal the specified amount of damage to the entity if the entity isn't immune to fire damage. Args:
     * amountDamage
     */
    protected void dealFireDamage(int amount)
    {
        if (!this.isImmuneToFire)
        {
            this.attackEntityFrom(DamageSource.inFire, (float)amount);
        }
    }

    public final boolean isImmuneToFire()
    {
        return this.isImmuneToFire;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float distance)
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.fall(distance);
        }
    }

    /**
     * Checks if this entity is either in water or on an open air block in rain (used in wolves).
     */
    public boolean isWet()
    {
        return this.inWater || this.worldObj.canLightningStrikeAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) || this.worldObj.canLightningStrikeAt(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + (double)this.height), MathHelper.floor_double(this.posZ));
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    public boolean isInWater()
    {
        return this.inWater;
    }

    /**
     * Returns if this entity is in water and will end up adding the waters velocity to the entity
     */
    public boolean handleWaterMovement()
    {
        if (this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.4000000059604645D, 0.0D).contract(0.001D, 0.001D, 0.001D), Material.water, this))
        {
            if (!this.inWater && !this.firstUpdate)
            {
                float var1 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.20000000298023224D + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.20000000298023224D) * 0.2F;

                if (var1 > 1.0F)
                {
                    var1 = 1.0F;
                }

                this.playSound(this.getSplashSound(), var1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                float var2 = (float)MathHelper.floor_double(this.boundingBox.minY);
                int var3;
                float var4;
                float var5;

                for (var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3)
                {
                    var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.spawnParticle("bubble", this.posX + (double)var4, (double)(var2 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY - (double)(this.rand.nextFloat() * 0.2F), this.motionZ);
                }

                for (var3 = 0; (float)var3 < 1.0F + this.width * 20.0F; ++var3)
                {
                    var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                    this.worldObj.spawnParticle("splash", this.posX + (double)var4, (double)(var2 + 1.0F), this.posZ + (double)var5, this.motionX, this.motionY, this.motionZ);
                }
            }

            this.fallDistance = 0.0F;
            this.inWater = true;
            this.fire = 0;
        }
        else
        {
            this.inWater = false;
        }

        return this.inWater;
    }

    protected String getSplashSound()
    {
        return "game.neutral.swim.splash";
    }

    /**
     * Checks if the current block the entity is within of the specified material type
     */
    public boolean isInsideOfMaterial(Material materialIn)
    {
        double var2 = this.posY + (double)this.getEyeHeight();
        int var4 = MathHelper.floor_double(this.posX);
        int var5 = MathHelper.floor_float((float)MathHelper.floor_double(var2));
        int var6 = MathHelper.floor_double(this.posZ);
        Block var7 = this.worldObj.getBlock(var4, var5, var6);

        if (var7.getMaterial() == materialIn)
        {
            float var8 = BlockLiquid.getLiquidHeightPercent(this.worldObj.getBlockMetadata(var4, var5, var6)) - 0.11111111F;
            float var9 = (float)(var5 + 1) - var8;
            return var2 < (double)var9;
        }
        else
        {
            return false;
        }
    }

    public float getEyeHeight()
    {
        return 0.0F;
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement()
    {
        return this.worldObj.isMaterialInBB(this.boundingBox.expand(-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D), Material.lava);
    }

    /**
     * Used in both water and by flying objects
     */
    public void moveFlying(float strafe, float forward, float friction)
    {
        float var4 = strafe * strafe + forward * forward;

        if (var4 >= 1.0E-4F)
        {
            var4 = MathHelper.sqrt_float(var4);

            if (var4 < 1.0F)
            {
                var4 = 1.0F;
            }

            var4 = friction / var4;
            strafe *= var4;
            forward *= var4;
            float var5 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F);
            float var6 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F);
            this.motionX += (double)(strafe * var6 - forward * var5);
            this.motionZ += (double)(forward * var6 + strafe * var5);
        }
    }

    public int getBrightnessForRender(float p_70070_1_)
    {
        int var2 = MathHelper.floor_double(this.posX);
        int var3 = MathHelper.floor_double(this.posZ);

        if (this.worldObj.blockExists(var2, 0, var3))
        {
            double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
            int var6 = MathHelper.floor_double(this.posY - (double)this.yOffset + var4);
            return this.worldObj.getLightBrightnessForSkyBlocks(var2, var6, var3, 0);
        }
        else
        {
            return 0;
        }
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_)
    {
        int var2 = MathHelper.floor_double(this.posX);
        int var3 = MathHelper.floor_double(this.posZ);

        if (this.worldObj.blockExists(var2, 0, var3))
        {
            double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66D;
            int var6 = MathHelper.floor_double(this.posY - (double)this.yOffset + var4);
            return this.worldObj.getLightBrightness(var2, var6, var3);
        }
        else
        {
            return 0.0F;
        }
    }

    /**
     * Sets the reference to the World object.
     */
    public void setWorld(World worldIn)
    {
        this.worldObj = worldIn;
    }

    /**
     * Sets the entity's position and rotation. Args: posX, posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation(double x, double y, double z, float yaw, float pitch)
    {
        this.prevPosX = this.posX = x;
        this.prevPosY = this.posY = y;
        this.prevPosZ = this.posZ = z;
        this.prevRotationYaw = this.rotationYaw = yaw;
        this.prevRotationPitch = this.rotationPitch = pitch;
        this.ySize = 0.0F;
        double var9 = (double)(this.prevRotationYaw - yaw);

        if (var9 < -180.0D)
        {
            this.prevRotationYaw += 360.0F;
        }

        if (var9 >= 180.0D)
        {
            this.prevRotationYaw -= 360.0F;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.setRotation(yaw, pitch);
    }

    /**
     * Sets the location and Yaw/Pitch of an entity in the world
     */
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
    {
        this.lastTickPosX = this.prevPosX = this.posX = x;
        this.lastTickPosY = this.prevPosY = this.posY = y + (double)this.yOffset;
        this.lastTickPosZ = this.prevPosZ = this.posZ = z;
        this.rotationYaw = yaw;
        this.rotationPitch = pitch;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistanceToEntity(Entity entityIn)
    {
        float var2 = (float)(this.posX - entityIn.posX);
        float var3 = (float)(this.posY - entityIn.posY);
        float var4 = (float)(this.posZ - entityIn.posZ);
        return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
    }

    /**
     * Gets the squared distance to the position. Args: x, y, z
     */
    public double getDistanceSq(double x, double y, double z)
    {
        double var7 = this.posX - x;
        double var9 = this.posY - y;
        double var11 = this.posZ - z;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    /**
     * Gets the distance to the position. Args: x, y, z
     */
    public double getDistance(double x, double y, double z)
    {
        double var7 = this.posX - x;
        double var9 = this.posY - y;
        double var11 = this.posZ - z;
        return (double)MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
    }

    /**
     * Returns the squared distance to the entity. Args: entity
     */
    public double getDistanceSqToEntity(Entity entityIn)
    {
        double var2 = this.posX - entityIn.posX;
        double var4 = this.posY - entityIn.posY;
        double var6 = this.posZ - entityIn.posZ;
        return var2 * var2 + var4 * var4 + var6 * var6;
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn) {}

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn)
    {
        if (entityIn.riddenByEntity != this && entityIn.ridingEntity != this)
        {
            double var2 = entityIn.posX - this.posX;
            double var4 = entityIn.posZ - this.posZ;
            double var6 = MathHelper.abs_max(var2, var4);

            if (var6 >= 0.009999999776482582D)
            {
                var6 = (double)MathHelper.sqrt_double(var6);
                var2 /= var6;
                var4 /= var6;
                double var8 = 1.0D / var6;

                if (var8 > 1.0D)
                {
                    var8 = 1.0D;
                }

                var2 *= var8;
                var4 *= var8;
                var2 *= 0.05000000074505806D;
                var4 *= 0.05000000074505806D;
                var2 *= (double)(1.0F - this.entityCollisionReduction);
                var4 *= (double)(1.0F - this.entityCollisionReduction);
                this.addVelocity(-var2, 0.0D, -var4);
                entityIn.addVelocity(var2, 0.0D, var4);
            }
        }
    }

    /**
     * Adds to the current velocity of the entity. Args: x, y, z
     */
    public void addVelocity(double x, double y, double z)
    {
        this.motionX += x;
        this.motionY += y;
        this.motionZ += z;
        this.isAirBorne = true;
    }

    /**
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked()
    {
        this.velocityChanged = true;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            this.setBeenAttacked();
            return false;
        }
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return false;
    }

    /**
     * Adds a value to the player score. Currently not actually used and the entity passed in does nothing. Args:
     * entity, scoreToAdd
     */
    public void addToPlayerScore(Entity entityIn, int amount) {}

    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        double var7 = this.posX - x;
        double var9 = this.posY - y;
        double var11 = this.posZ - z;
        double var13 = var7 * var7 + var9 * var9 + var11 * var11;
        return this.isInRangeToRenderDist(var13);
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double var3 = this.boundingBox.getAverageEdgeLength();
        var3 *= 64.0D * this.renderDistanceWeight;
        return distance < var3 * var3;
    }

    /**
     * Like writeToNBTOptional but does not check if the entity is ridden. Used for saving ridden entities with their
     * riders.
     */
    public boolean writeMountToNBT(NBTTagCompound tagCompund)
    {
        String var2 = this.getEntityString();

        if (!this.isDead && var2 != null)
        {
            tagCompund.setString("id", var2);
            this.writeToNBT(tagCompund);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Either write this entity to the NBT tag given and return true, or return false without doing anything. If this
     * returns false the entity is not saved on disk. Ridden entities return false here as they are saved with their
     * rider.
     */
    public boolean writeToNBTOptional(NBTTagCompound tagCompund)
    {
        String var2 = this.getEntityString();

        if (!this.isDead && var2 != null && this.riddenByEntity == null)
        {
            tagCompund.setString("id", var2);
            this.writeToNBT(tagCompund);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Save the entity to NBT (calls an abstract helper method to write extra data)
     */
    public void writeToNBT(NBTTagCompound tagCompund)
    {
        try
        {
            tagCompund.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY + (double)this.ySize, this.posZ));
            tagCompund.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
            tagCompund.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
            tagCompund.setFloat("FallDistance", this.fallDistance);
            tagCompund.setShort("Fire", (short)this.fire);
            tagCompund.setShort("Air", (short)this.getAir());
            tagCompund.setBoolean("OnGround", this.onGround);
            tagCompund.setInteger("Dimension", this.dimension);
            tagCompund.setBoolean("Invulnerable", this.invulnerable);
            tagCompund.setInteger("PortalCooldown", this.timeUntilPortal);
            tagCompund.setLong("UUIDMost", this.getUniqueID().getMostSignificantBits());
            tagCompund.setLong("UUIDLeast", this.getUniqueID().getLeastSignificantBits());
            this.writeEntityToNBT(tagCompund);

            if (this.ridingEntity != null)
            {
                NBTTagCompound var2 = new NBTTagCompound();

                if (this.ridingEntity.writeMountToNBT(var2))
                {
                    tagCompund.setTag("Riding", var2);
                }
            }
        }
        catch (Throwable var5)
        {
            CrashReport var3 = CrashReport.makeCrashReport(var5, "Saving entity NBT");
            CrashReportCategory var4 = var3.makeCategory("Entity being saved");
            this.addEntityCrashInfo(var4);
            throw new ReportedException(var3);
        }
    }

    /**
     * Reads the entity from NBT (calls an abstract helper method to read specialized data)
     */
    public void readFromNBT(NBTTagCompound tagCompund)
    {
        try
        {
            NBTTagList var2 = tagCompund.getTagList("Pos", 6);
            NBTTagList var6 = tagCompund.getTagList("Motion", 6);
            NBTTagList var7 = tagCompund.getTagList("Rotation", 5);
            this.motionX = var6.getDoubleAt(0);
            this.motionY = var6.getDoubleAt(1);
            this.motionZ = var6.getDoubleAt(2);

            if (Math.abs(this.motionX) > 10.0D)
            {
                this.motionX = 0.0D;
            }

            if (Math.abs(this.motionY) > 10.0D)
            {
                this.motionY = 0.0D;
            }

            if (Math.abs(this.motionZ) > 10.0D)
            {
                this.motionZ = 0.0D;
            }

            this.prevPosX = this.lastTickPosX = this.posX = var2.getDoubleAt(0);
            this.prevPosY = this.lastTickPosY = this.posY = var2.getDoubleAt(1);
            this.prevPosZ = this.lastTickPosZ = this.posZ = var2.getDoubleAt(2);
            this.prevRotationYaw = this.rotationYaw = var7.getFloatAt(0);
            this.prevRotationPitch = this.rotationPitch = var7.getFloatAt(1);
            this.fallDistance = tagCompund.getFloat("FallDistance");
            this.fire = tagCompund.getShort("Fire");
            this.setAir(tagCompund.getShort("Air"));
            this.onGround = tagCompund.getBoolean("OnGround");
            this.dimension = tagCompund.getInteger("Dimension");
            this.invulnerable = tagCompund.getBoolean("Invulnerable");
            this.timeUntilPortal = tagCompund.getInteger("PortalCooldown");

            if (tagCompund.hasKey("UUIDMost", 4) && tagCompund.hasKey("UUIDLeast", 4))
            {
                this.entityUniqueID = new UUID(tagCompund.getLong("UUIDMost"), tagCompund.getLong("UUIDLeast"));
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.readEntityFromNBT(tagCompund);

            if (this.shouldSetPosAfterLoading())
            {
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        catch (Throwable var5)
        {
            CrashReport var3 = CrashReport.makeCrashReport(var5, "Loading entity NBT");
            CrashReportCategory var4 = var3.makeCategory("Entity being loaded");
            this.addEntityCrashInfo(var4);
            throw new ReportedException(var3);
        }
    }

    protected boolean shouldSetPosAfterLoading()
    {
        return true;
    }

    /**
     * Returns the string that identifies this Entity's class
     */
    protected final String getEntityString()
    {
        return EntityList.getEntityString(this);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected abstract void readEntityFromNBT(NBTTagCompound tagCompund);

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected abstract void writeEntityToNBT(NBTTagCompound tagCompound);

    public void onChunkLoad() {}

    /**
     * creates a NBT list from the array of doubles passed to this function
     */
    protected NBTTagList newDoubleNBTList(double ... numbers)
    {
        NBTTagList var2 = new NBTTagList();
        double[] var3 = numbers;
        int var4 = numbers.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            double var6 = var3[var5];
            var2.appendTag(new NBTTagDouble(var6));
        }

        return var2;
    }

    /**
     * Returns a new NBTTagList filled with the specified floats
     */
    protected NBTTagList newFloatNBTList(float ... numbers)
    {
        NBTTagList var2 = new NBTTagList();
        float[] var3 = numbers;
        int var4 = numbers.length;

        for (int var5 = 0; var5 < var4; ++var5)
        {
            float var6 = var3[var5];
            var2.appendTag(new NBTTagFloat(var6));
        }

        return var2;
    }

    public float getShadowSize()
    {
        return this.height / 2.0F;
    }

    public EntityItem dropItem(Item itemIn, int size)
    {
        return this.dropItemWithOffset(itemIn, size, 0.0F);
    }

    public EntityItem dropItemWithOffset(Item itemIn, int size, float p_145778_3_)
    {
        return this.entityDropItem(new ItemStack(itemIn, size, 0), p_145778_3_);
    }

    /**
     * Drops an item at the position of the entity.
     */
    public EntityItem entityDropItem(ItemStack itemStackIn, float offsetY)
    {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null)
        {
            EntityItem var3 = new EntityItem(this.worldObj, this.posX, this.posY + (double)offsetY, this.posZ, itemStackIn);
            var3.delayBeforeCanPickup = 10;
            this.worldObj.spawnEntityInWorld(var3);
            return var3;
        }
        else
        {
            return null;
        }
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive()
    {
        return !this.isDead;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock()
    {
        for (int var1 = 0; var1 < 8; ++var1)
        {
            float var2 = ((float)((var1 >> 0) % 2) - 0.5F) * this.width * 0.8F;
            float var3 = ((float)((var1 >> 1) % 2) - 0.5F) * 0.1F;
            float var4 = ((float)((var1 >> 2) % 2) - 0.5F) * this.width * 0.8F;
            int var5 = MathHelper.floor_double(this.posX + (double)var2);
            int var6 = MathHelper.floor_double(this.posY + (double)this.getEyeHeight() + (double)var3);
            int var7 = MathHelper.floor_double(this.posZ + (double)var4);

            if (this.worldObj.getBlock(var5, var6, var7).isNormalCube())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer player)
    {
        return false;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return null;
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        if (this.ridingEntity.isDead)
        {
            this.ridingEntity = null;
        }
        else
        {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.onUpdate();

            if (this.ridingEntity != null)
            {
                this.ridingEntity.updateRiderPosition();
                this.entityRiderYawDelta += (double)(this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);

                for (this.entityRiderPitchDelta += (double)(this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch); this.entityRiderYawDelta >= 180.0D; this.entityRiderYawDelta -= 360.0D)
                {
                }

                while (this.entityRiderYawDelta < -180.0D)
                {
                    this.entityRiderYawDelta += 360.0D;
                }

                while (this.entityRiderPitchDelta >= 180.0D)
                {
                    this.entityRiderPitchDelta -= 360.0D;
                }

                while (this.entityRiderPitchDelta < -180.0D)
                {
                    this.entityRiderPitchDelta += 360.0D;
                }

                double var1 = this.entityRiderYawDelta * 0.5D;
                double var3 = this.entityRiderPitchDelta * 0.5D;
                float var5 = 10.0F;

                if (var1 > (double)var5)
                {
                    var1 = (double)var5;
                }

                if (var1 < (double)(-var5))
                {
                    var1 = (double)(-var5);
                }

                if (var3 > (double)var5)
                {
                    var3 = (double)var5;
                }

                if (var3 < (double)(-var5))
                {
                    var3 = (double)(-var5);
                }

                this.entityRiderYawDelta -= var1;
                this.entityRiderPitchDelta -= var3;
            }
        }
    }

    public void updateRiderPosition()
    {
        if (this.riddenByEntity != null)
        {
            this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
        }
    }

    /**
     * Returns the Y Offset of this entity.
     */
    public double getYOffset()
    {
        return (double)this.yOffset;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return (double)this.height * 0.75D;
    }

    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    public void mountEntity(Entity entityIn)
    {
        this.entityRiderPitchDelta = 0.0D;
        this.entityRiderYawDelta = 0.0D;

        if (entityIn == null)
        {
            if (this.ridingEntity != null)
            {
                this.setLocationAndAngles(this.ridingEntity.posX, this.ridingEntity.boundingBox.minY + (double)this.ridingEntity.height, this.ridingEntity.posZ, this.rotationYaw, this.rotationPitch);
                this.ridingEntity.riddenByEntity = null;
            }

            this.ridingEntity = null;
        }
        else
        {
            if (this.ridingEntity != null)
            {
                this.ridingEntity.riddenByEntity = null;
            }

            if (entityIn != null)
            {
                for (Entity var2 = entityIn.ridingEntity; var2 != null; var2 = var2.ridingEntity)
                {
                    if (var2 == this)
                    {
                        return;
                    }
                }
            }

            this.ridingEntity = entityIn;
            entityIn.riddenByEntity = this;
        }
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int rotationIncrements)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
        List var10 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(0.03125D, 0.0D, 0.03125D));

        if (!var10.isEmpty())
        {
            double var11 = 0.0D;

            for (int var13 = 0; var13 < var10.size(); ++var13)
            {
                AxisAlignedBB var14 = (AxisAlignedBB)var10.get(var13);

                if (var14.maxY > var11)
                {
                    var11 = var14.maxY;
                }
            }

            y += var11 - this.boundingBox.minY;
            this.setPosition(x, y, z);
        }
    }

    public float getCollisionBorderSize()
    {
        return 0.1F;
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3 getLookVec()
    {
        return null;
    }

    /**
     * Called by portal blocks when an entity is within it.
     */
    public void setInPortal()
    {
        if (this.timeUntilPortal > 0)
        {
            this.timeUntilPortal = this.getPortalCooldown();
        }
        else
        {
            double var1 = this.prevPosX - this.posX;
            double var3 = this.prevPosZ - this.posZ;

            if (!this.worldObj.isClient && !this.inPortal)
            {
                this.teleportDirection = Direction.getMovementDirection(var1, var3);
            }

            this.inPortal = true;
        }
    }

    /**
     * Return the amount of cooldown before this entity can use a portal again.
     */
    public int getPortalCooldown()
    {
        return 300;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
    }

    public void handleHealthUpdate(byte p_70103_1_) {}

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation() {}

    public ItemStack[] getLastActiveItems()
    {
        return null;
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn) {}

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning()
    {
        boolean var1 = this.worldObj != null && this.worldObj.isClient;
        return !this.isImmuneToFire && (this.fire > 0 || var1 && this.getFlag(0));
    }

    /**
     * Returns true if the entity is riding another entity, used by render to rotate the legs to be in 'sit' position
     * for players.
     */
    public boolean isRiding()
    {
        return this.ridingEntity != null;
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking()
    {
        return this.getFlag(1);
    }

    /**
     * Sets the sneaking flag.
     */
    public void setSneaking(boolean sneaking)
    {
        this.setFlag(1, sneaking);
    }

    /**
     * Get if the Entity is sprinting.
     */
    public boolean isSprinting()
    {
        return this.getFlag(3);
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting)
    {
        this.setFlag(3, sprinting);
    }

    public boolean isInvisible()
    {
        return this.getFlag(5);
    }

    /**
     * Only used by renderer in EntityLivingBase subclasses.\nDetermines if an entity is visible or not to a specfic
     * player, if the entity is normally invisible.\nFor EntityLivingBase subclasses, returning false when invisible
     * will render the entity semitransparent.
     */
    public boolean isInvisibleToPlayer(EntityPlayer player)
    {
        return this.isInvisible();
    }

    public void setInvisible(boolean invisible)
    {
        this.setFlag(5, invisible);
    }

    public boolean isEating()
    {
        return this.getFlag(4);
    }

    public void setEating(boolean eating)
    {
        this.setFlag(4, eating);
    }

    /**
     * Returns true if the flag is active for the entity. Known flags: 0) is burning; 1) is sneaking; 2) is riding
     * something; 3) is sprinting; 4) is eating
     */
    protected boolean getFlag(int flag)
    {
        return (this.dataWatcher.getWatchableObjectByte(0) & 1 << flag) != 0;
    }

    /**
     * Enable or disable a entity flag, see getEntityFlag to read the know flags.
     */
    protected void setFlag(int flag, boolean set)
    {
        byte var3 = this.dataWatcher.getWatchableObjectByte(0);

        if (set)
        {
            this.dataWatcher.updateObject(0, Byte.valueOf((byte)(var3 | 1 << flag)));
        }
        else
        {
            this.dataWatcher.updateObject(0, Byte.valueOf((byte)(var3 & ~(1 << flag))));
        }
    }

    public int getAir()
    {
        return this.dataWatcher.getWatchableObjectShort(1);
    }

    public void setAir(int air)
    {
        this.dataWatcher.updateObject(1, Short.valueOf((short)air));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt)
    {
        this.dealFireDamage(5);
        ++this.fire;

        if (this.fire == 0)
        {
            this.setFire(8);
        }
    }

    /**
     * This method gets called when the entity kills another one.
     */
    public void onKillEntity(EntityLivingBase entityLivingIn) {}

    protected boolean pushOutOfBlocks(double x, double y, double z)
    {
        int var7 = MathHelper.floor_double(x);
        int var8 = MathHelper.floor_double(y);
        int var9 = MathHelper.floor_double(z);
        double var10 = x - (double)var7;
        double var12 = y - (double)var8;
        double var14 = z - (double)var9;
        List var16 = this.worldObj.func_147461_a(this.boundingBox);

        if (var16.isEmpty() && !this.worldObj.isBlockFullCube(var7, var8, var9))
        {
            return false;
        }
        else
        {
            boolean var17 = !this.worldObj.isBlockFullCube(var7 - 1, var8, var9);
            boolean var18 = !this.worldObj.isBlockFullCube(var7 + 1, var8, var9);
            boolean var19 = !this.worldObj.isBlockFullCube(var7, var8 - 1, var9);
            boolean var20 = !this.worldObj.isBlockFullCube(var7, var8 + 1, var9);
            boolean var21 = !this.worldObj.isBlockFullCube(var7, var8, var9 - 1);
            boolean var22 = !this.worldObj.isBlockFullCube(var7, var8, var9 + 1);
            byte var23 = 3;
            double var24 = 9999.0D;

            if (var17 && var10 < var24)
            {
                var24 = var10;
                var23 = 0;
            }

            if (var18 && 1.0D - var10 < var24)
            {
                var24 = 1.0D - var10;
                var23 = 1;
            }

            if (var20 && 1.0D - var12 < var24)
            {
                var24 = 1.0D - var12;
                var23 = 3;
            }

            if (var21 && var14 < var24)
            {
                var24 = var14;
                var23 = 4;
            }

            if (var22 && 1.0D - var14 < var24)
            {
                var24 = 1.0D - var14;
                var23 = 5;
            }

            float var26 = this.rand.nextFloat() * 0.2F + 0.1F;

            if (var23 == 0)
            {
                this.motionX = (double)(-var26);
            }

            if (var23 == 1)
            {
                this.motionX = (double)var26;
            }

            if (var23 == 2)
            {
                this.motionY = (double)(-var26);
            }

            if (var23 == 3)
            {
                this.motionY = (double)var26;
            }

            if (var23 == 4)
            {
                this.motionZ = (double)(-var26);
            }

            if (var23 == 5)
            {
                this.motionZ = (double)var26;
            }

            return true;
        }
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb()
    {
        this.isInWeb = true;
        this.fallDistance = 0.0F;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        String var1 = EntityList.getEntityString(this);

        if (var1 == null)
        {
            var1 = "generic";
        }

        return StatCollector.translateToLocal("entity." + var1 + ".name");
    }

    /**
     * Return the Entity parts making up this Entity (currently only for dragons)
     */
    public Entity[] getParts()
    {
        return null;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn)
    {
        return this == entityIn;
    }

    public float getRotationYawHead()
    {
        return 0.0F;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float rotation) {}

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem()
    {
        return true;
    }

    /**
     * Called when a player attacks an entity. If this returns true the attack will not happen.
     */
    public boolean hitByEntity(Entity entityIn)
    {
        return false;
    }

    public String toString()
    {
        return String.format("%s[\'%s\'/%d, l=\'%s\', x=%.2f, y=%.2f, z=%.2f]", this.getClass().getSimpleName(), this.getCommandSenderName(), Integer.valueOf(this.entityId), this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(), Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ));
    }

    /**
     * Return whether this entity is invulnerable to damage.
     */
    public boolean isEntityInvulnerable()
    {
        return this.invulnerable;
    }

    /**
     * Sets this entity's location and angles to the location and angles of the passed in entity.
     */
    public void copyLocationAndAnglesFrom(Entity entityIn)
    {
        this.setLocationAndAngles(entityIn.posX, entityIn.posY, entityIn.posZ, entityIn.rotationYaw, entityIn.rotationPitch);
    }

    /**
     * Copies important data from another entity to this entity. Used when teleporting entities between worlds, as this
     * actually deletes the teleporting entity and re-creates it on the other side. Params: Entity to copy from, unused
     * (always true)
     */
    public void copyDataFrom(Entity entityIn, boolean unused)
    {
        NBTTagCompound var3 = new NBTTagCompound();
        entityIn.writeToNBT(var3);
        this.readFromNBT(var3);
        this.timeUntilPortal = entityIn.timeUntilPortal;
        this.teleportDirection = entityIn.teleportDirection;
    }

    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    public void travelToDimension(int dimensionId)
    {
        if (!this.worldObj.isClient && !this.isDead)
        {
            MinecraftServer var2 = MinecraftServer.getServer();
            int var3 = this.dimension;
            WorldServer var4 = var2.worldServerForDimension(var3);
            WorldServer var5 = var2.worldServerForDimension(dimensionId);
            this.dimension = dimensionId;

            if (var3 == 1 && dimensionId == 1)
            {
                var5 = var2.worldServerForDimension(0);
                this.dimension = 0;
            }

            this.worldObj.removeEntity(this);
            this.isDead = false;
            var2.getConfigurationManager().transferEntityToWorld(this, var3, var4, var5);
            Entity var6 = EntityList.createEntityByName(EntityList.getEntityString(this), var5);

            if (var6 != null)
            {
                var6.copyDataFrom(this, true);

                if (var3 == 1 && dimensionId == 1)
                {
                    ChunkCoordinates var7 = var5.getSpawnPoint();
                    var7.posY = this.worldObj.getTopSolidOrLiquidBlock(var7.posX, var7.posZ);
                    var6.setLocationAndAngles((double)var7.posX, (double)var7.posY, (double)var7.posZ, var6.rotationYaw, var6.rotationPitch);
                }

                var5.spawnEntityInWorld(var6);
            }

            this.isDead = true;
            var4.resetUpdateEntityTick();
            var5.resetUpdateEntityTick();
        }
    }

    public float getExplosionResistance(Explosion explosionIn, World worldIn, int x, int y, int z, Block blockIn)
    {
        return blockIn.getExplosionResistance(this);
    }

    public boolean func_145774_a(Explosion explosionIn, World worldIn, int x, int y, int z, Block blockIn, float unused)
    {
        return true;
    }

    /**
     * The number of iterations PathFinder.getSafePoint will execute before giving up.
     */
    public int getMaxSafePointTries()
    {
        return 3;
    }

    public int getTeleportDirection()
    {
        return this.teleportDirection;
    }

    public boolean doesEntityNotTriggerPressurePlate()
    {
        return false;
    }

    public void addEntityCrashInfo(CrashReportCategory category)
    {
        category.addCrashSectionCallable("Entity Type", new Callable()
        {
            public String call()
            {
                return EntityList.getEntityString(Entity.this) + " (" + Entity.this.getClass().getCanonicalName() + ")";
            }
        });
        category.addCrashSection("Entity ID", Integer.valueOf(this.entityId));
        category.addCrashSectionCallable("Entity Name", new Callable()
        {
            public String call()
            {
                return Entity.this.getCommandSenderName();
            }
        });
        category.addCrashSection("Entity\'s Exact location", String.format("%.2f, %.2f, %.2f", Double.valueOf(this.posX), Double.valueOf(this.posY), Double.valueOf(this.posZ)));
        category.addCrashSection("Entity\'s Block location", CrashReportCategory.getLocationInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
        category.addCrashSection("Entity\'s Momentum", String.format("%.2f, %.2f, %.2f", Double.valueOf(this.motionX), Double.valueOf(this.motionY), Double.valueOf(this.motionZ)));
    }

    /**
     * Return whether this entity should be rendered as on fire.
     */
    public boolean canRenderOnFire()
    {
        return this.isBurning();
    }

    public UUID getUniqueID()
    {
        return this.entityUniqueID;
    }

    public boolean isPushedByWater()
    {
        return true;
    }

    public IChatComponent getFormattedCommandSenderName()
    {
        return new ChatComponentText(this.getCommandSenderName());
    }

    public void func_145781_i(int p_145781_1_) {}

    public enum EnumEntitySize
    {
        SIZE_1("SIZE_1", 0),
        SIZE_2("SIZE_2", 1),
        SIZE_3("SIZE_3", 2),
        SIZE_4("SIZE_4", 3),
        SIZE_5("SIZE_5", 4),
        SIZE_6("SIZE_6", 5);

        private static final Entity.EnumEntitySize[] $VALUES = {SIZE_1, SIZE_2, SIZE_3, SIZE_4, SIZE_5, SIZE_6};

        EnumEntitySize(String p_i1581_1_, int p_i1581_2_) {}

        public int multiplyBy32AndRound(double p_75630_1_)
        {
            double var3 = p_75630_1_ - ((double)MathHelper.floor_double(p_75630_1_) + 0.5D);

            switch (Entity.SwitchEnumEntitySize.field_96565_a[this.ordinal()])
            {
                case 1:
                    if (var3 < 0.0D)
                    {
                        if (var3 < -0.3125D)
                        {
                            return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
                        }
                    }
                    else if (var3 < 0.3125D)
                    {
                        return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
                    }

                    return MathHelper.floor_double(p_75630_1_ * 32.0D);

                case 2:
                    if (var3 < 0.0D)
                    {
                        if (var3 < -0.3125D)
                        {
                            return MathHelper.floor_double(p_75630_1_ * 32.0D);
                        }
                    }
                    else if (var3 < 0.3125D)
                    {
                        return MathHelper.floor_double(p_75630_1_ * 32.0D);
                    }

                    return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

                case 3:
                    if (var3 > 0.0D)
                    {
                        return MathHelper.floor_double(p_75630_1_ * 32.0D);
                    }

                    return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

                case 4:
                    if (var3 < 0.0D)
                    {
                        if (var3 < -0.1875D)
                        {
                            return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
                        }
                    }
                    else if (var3 < 0.1875D)
                    {
                        return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
                    }

                    return MathHelper.floor_double(p_75630_1_ * 32.0D);

                case 5:
                    if (var3 < 0.0D)
                    {
                        if (var3 < -0.1875D)
                        {
                            return MathHelper.floor_double(p_75630_1_ * 32.0D);
                        }
                    }
                    else if (var3 < 0.1875D)
                    {
                        return MathHelper.floor_double(p_75630_1_ * 32.0D);
                    }

                    return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);

                case 6:
                default:
                    if (var3 > 0.0D)
                    {
                        return MathHelper.ceiling_double_int(p_75630_1_ * 32.0D);
                    }
                    else
                    {
                        return MathHelper.floor_double(p_75630_1_ * 32.0D);
                    }
            }
        }
    }

    static final class SwitchEnumEntitySize
    {
        static final int[] field_96565_a = new int[Entity.EnumEntitySize.values().length];

        static
        {
            try
            {
                field_96565_a[Entity.EnumEntitySize.SIZE_1.ordinal()] = 1;
            }
            catch (NoSuchFieldError var6)
            {
            }

            try
            {
                field_96565_a[Entity.EnumEntitySize.SIZE_2.ordinal()] = 2;
            }
            catch (NoSuchFieldError var5)
            {
            }

            try
            {
                field_96565_a[Entity.EnumEntitySize.SIZE_3.ordinal()] = 3;
            }
            catch (NoSuchFieldError var4)
            {
            }

            try
            {
                field_96565_a[Entity.EnumEntitySize.SIZE_4.ordinal()] = 4;
            }
            catch (NoSuchFieldError var3)
            {
            }

            try
            {
                field_96565_a[Entity.EnumEntitySize.SIZE_5.ordinal()] = 5;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                field_96565_a[Entity.EnumEntitySize.SIZE_6.ordinal()] = 6;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}