package net.minecraft.tileentity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TileEntity
{
    private static final Logger logger = LogManager.getLogger();

    /**
     * A HashMap storing string names of classes mapping to the actual java.lang.Class type.
     */
    private static final Map nameToClassMap = new HashMap();

    /**
     * A HashMap storing the classes and mapping to the string names (reverse of nameToClassMap).
     */
    private static final Map classToNameMap = new HashMap();

    /** the instance of the world the tile entity is in. */
    protected World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    protected boolean tileEntityInvalid;
    public int blockMetadata = -1;

    /** the Block type that this TileEntity is contained within */
    public Block blockType;

    private static void addMapping(Class cl, String id)
    {
        if (nameToClassMap.containsKey(id))
        {
            throw new IllegalArgumentException("Duplicate id: " + id);
        }
        else
        {
            nameToClassMap.put(id, cl);
            classToNameMap.put(cl, id);
        }
    }

    /**
     * Returns the worldObj for this tileEntity.
     */
    public World getWorldObj()
    {
        return this.worldObj;
    }

    /**
     * Sets the worldObj for this tileEntity.
     */
    public void setWorldObj(World worldIn)
    {
        this.worldObj = worldIn;
    }

    /**
     * Returns true if the worldObj isn't null.
     */
    public boolean hasWorldObj()
    {
        return this.worldObj != null;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        this.xCoord = compound.getInteger("x");
        this.yCoord = compound.getInteger("y");
        this.zCoord = compound.getInteger("z");
    }

    public void writeToNBT(NBTTagCompound compound)
    {
        String var2 = (String)classToNameMap.get(this.getClass());

        if (var2 == null)
        {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        else
        {
            compound.setString("id", var2);
            compound.setInteger("x", this.xCoord);
            compound.setInteger("y", this.yCoord);
            compound.setInteger("z", this.zCoord);
        }
    }

    public void updateEntity() {}

    /**
     * Creates a new entity and loads its data from the specified NBT.
     */
    public static TileEntity createAndLoadEntity(NBTTagCompound nbt)
    {
        TileEntity var1 = null;

        try
        {
            Class var2 = (Class)nameToClassMap.get(nbt.getString("id"));

            if (var2 != null)
            {
                var1 = (TileEntity)var2.newInstance();
            }
        }
        catch (Exception var3)
        {
            var3.printStackTrace();
        }

        if (var1 != null)
        {
            var1.readFromNBT(nbt);
        }
        else
        {
            logger.warn("Skipping BlockEntity with id " + nbt.getString("id"));
        }

        return var1;
    }

    public int getBlockMetadata()
    {
        if (this.blockMetadata == -1)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockMetadata;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        if (this.worldObj != null)
        {
            this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.markTileEntityChunkModified(this.xCoord, this.yCoord, this.zCoord, this);

            if (this.getBlockType() != Blocks.air)
            {
                this.worldObj.updateNeighborsAboutBlockChange(this.xCoord, this.yCoord, this.zCoord, this.getBlockType());
            }
        }
    }

    /**
     * Returns the square of the distance between this entity and the passed in coordinates.
     */
    public double getDistanceFrom(double p_145835_1_, double p_145835_3_, double p_145835_5_)
    {
        double var7 = (double)this.xCoord + 0.5D - p_145835_1_;
        double var9 = (double)this.yCoord + 0.5D - p_145835_3_;
        double var11 = (double)this.zCoord + 0.5D - p_145835_5_;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public double getMaxRenderDistanceSquared()
    {
        return 4096.0D;
    }

    /**
     * Gets the block type at the location of this entity (client-only).
     */
    public Block getBlockType()
    {
        if (this.blockType == null)
        {
            this.blockType = this.worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        }

        return this.blockType;
    }

    /**
     * Overriden in a sign to provide the text.
     */
    public Packet getDescriptionPacket()
    {
        return null;
    }

    public boolean isInvalid()
    {
        return this.tileEntityInvalid;
    }

    /**
     * invalidates a tile entity
     */
    public void invalidate()
    {
        this.tileEntityInvalid = true;
    }

    /**
     * validates a tile entity
     */
    public void validate()
    {
        this.tileEntityInvalid = false;
    }

    public boolean receiveClientEvent(int id, int type)
    {
        return false;
    }

    public void updateContainingBlockInfo()
    {
        this.blockType = null;
        this.blockMetadata = -1;
    }

    public void addInfoToCrashReport(CrashReportCategory reportCategory)
    {
        reportCategory.addCrashSectionCallable("Name", new Callable()
        {
            public String call()
            {
                return (String)TileEntity.classToNameMap.get(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        CrashReportCategory.addBlockInfo(reportCategory, this.xCoord, this.yCoord, this.zCoord, this.getBlockType(), this.getBlockMetadata());
        reportCategory.addCrashSectionCallable("Actual block type", new Callable()
        {
            public String call()
            {
                int var1 = Block.getIdFromBlock(TileEntity.this.worldObj.getBlock(TileEntity.this.xCoord, TileEntity.this.yCoord, TileEntity.this.zCoord));

                try
                {
                    return String.format("ID #%d (%s // %s)", Integer.valueOf(var1), Block.getBlockById(var1).getUnlocalizedName(), Block.getBlockById(var1).getClass().getCanonicalName());
                }
                catch (Throwable var3)
                {
                    return "ID #" + var1;
                }
            }
        });
        reportCategory.addCrashSectionCallable("Actual block data value", new Callable()
        {
            public String call()
            {
                int var1 = TileEntity.this.worldObj.getBlockMetadata(TileEntity.this.xCoord, TileEntity.this.yCoord, TileEntity.this.zCoord);

                if (var1 < 0)
                {
                    return "Unknown? (Got " + var1 + ")";
                }
                else
                {
                    String var2 = String.format("%4s", new Object[] {Integer.toBinaryString(var1)}).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", Integer.valueOf(var1), var2);
                }
            }
        });
    }

    static
    {
        addMapping(TileEntityFurnace.class, "Furnace");
        addMapping(TileEntityChest.class, "Chest");
        addMapping(TileEntityEnderChest.class, "EnderChest");
        addMapping(BlockJukebox.TileEntityJukebox.class, "RecordPlayer");
        addMapping(TileEntityDispenser.class, "Trap");
        addMapping(TileEntityDropper.class, "Dropper");
        addMapping(TileEntitySign.class, "Sign");
        addMapping(TileEntityMobSpawner.class, "MobSpawner");
        addMapping(TileEntityNote.class, "Music");
        addMapping(TileEntityPiston.class, "Piston");
        addMapping(TileEntityBrewingStand.class, "Cauldron");
        addMapping(TileEntityEnchantmentTable.class, "EnchantTable");
        addMapping(TileEntityEndPortal.class, "Airportal");
        addMapping(TileEntityCommandBlock.class, "Control");
        addMapping(TileEntityBeacon.class, "Beacon");
        addMapping(TileEntitySkull.class, "Skull");
        addMapping(TileEntityDaylightDetector.class, "DLDetector");
        addMapping(TileEntityHopper.class, "Hopper");
        addMapping(TileEntityComparator.class, "Comparator");
        addMapping(TileEntityFlowerPot.class, "FlowerPot");
    }
}