package net.minecraft.util;

public class ChunkCoordinates implements Comparable
{
    public int posX;

    /** the y coordinate */
    public int posY;

    /** the z coordinate */
    public int posZ;

    public ChunkCoordinates() {}

    public ChunkCoordinates(int x, int y, int z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public ChunkCoordinates(ChunkCoordinates coords)
    {
        this.posX = coords.posX;
        this.posY = coords.posY;
        this.posZ = coords.posZ;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (!(p_equals_1_ instanceof ChunkCoordinates))
        {
            return false;
        }
        else
        {
            ChunkCoordinates var2 = (ChunkCoordinates)p_equals_1_;
            return this.posX == var2.posX && this.posY == var2.posY && this.posZ == var2.posZ;
        }
    }

    public int hashCode()
    {
        return this.posX + this.posZ << 8 + this.posY << 16;
    }

    public int compareTo(ChunkCoordinates p_compareTo_1_)
    {
        return this.posY == p_compareTo_1_.posY ? (this.posZ == p_compareTo_1_.posZ ? this.posX - p_compareTo_1_.posX : this.posZ - p_compareTo_1_.posZ) : this.posY - p_compareTo_1_.posY;
    }

    public void set(int x, int y, int z)
    {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    /**
     * Returns the squared distance between this coordinates and the coordinates given as argument.
     */
    public float getDistanceSquared(int x, int y, int z)
    {
        float var4 = (float)(this.posX - x);
        float var5 = (float)(this.posY - y);
        float var6 = (float)(this.posZ - z);
        return var4 * var4 + var5 * var5 + var6 * var6;
    }

    /**
     * Return the squared distance between this coordinates and the ChunkCoordinates given as argument.
     */
    public float getDistanceSquaredToChunkCoordinates(ChunkCoordinates other)
    {
        return this.getDistanceSquared(other.posX, other.posY, other.posZ);
    }

    public String toString()
    {
        return "Pos{x=" + this.posX + ", y=" + this.posY + ", z=" + this.posZ + '}';
    }

    public int compareTo(Object p_compareTo_1_)
    {
        return this.compareTo((ChunkCoordinates)p_compareTo_1_);
    }
}