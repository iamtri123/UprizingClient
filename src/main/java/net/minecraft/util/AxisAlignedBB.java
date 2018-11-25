package net.minecraft.util;

public class AxisAlignedBB
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;
    private static final String __OBFID = "CL_00000607";

    /**
     * Returns a bounding box with the specified bounds. Args: minX, minY, minZ, maxX, maxY, maxZ
     */
    public static AxisAlignedBB getBoundingBox(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
    }

    protected AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        this.minX = x1;
        this.minY = y1;
        this.minZ = z1;
        this.maxX = x2;
        this.maxY = y2;
        this.maxZ = z2;
    }

    /**
     * Sets the bounds of the bounding box. Args: minX, minY, minZ, maxX, maxY, maxZ
     */
    public AxisAlignedBB setBounds(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        this.minX = x1;
        this.minY = y1;
        this.minZ = z1;
        this.maxX = x2;
        this.maxY = y2;
        this.maxZ = z2;
        return this;
    }

    /**
     * Adds the coordinates to the bounding box extending it if the point lies outside the current ranges. Args: x, y, z
     */
    public AxisAlignedBB addCoord(double x, double y, double z)
    {
        double var7 = this.minX;
        double var9 = this.minY;
        double var11 = this.minZ;
        double var13 = this.maxX;
        double var15 = this.maxY;
        double var17 = this.maxZ;

        if (x < 0.0D)
        {
            var7 += x;
        }

        if (x > 0.0D)
        {
            var13 += x;
        }

        if (y < 0.0D)
        {
            var9 += y;
        }

        if (y > 0.0D)
        {
            var15 += y;
        }

        if (z < 0.0D)
        {
            var11 += z;
        }

        if (z > 0.0D)
        {
            var17 += z;
        }

        return getBoundingBox(var7, var9, var11, var13, var15, var17);
    }

    /**
     * Returns a bounding box expanded by the specified vector (if negative numbers are given it will shrink). Args: x,
     * y, z
     */
    public AxisAlignedBB expand(double x, double y, double z)
    {
        double var7 = this.minX - x;
        double var9 = this.minY - y;
        double var11 = this.minZ - z;
        double var13 = this.maxX + x;
        double var15 = this.maxY + y;
        double var17 = this.maxZ + z;
        return getBoundingBox(var7, var9, var11, var13, var15, var17);
    }

    public AxisAlignedBB union(AxisAlignedBB other)
    {
        double var2 = Math.min(this.minX, other.minX);
        double var4 = Math.min(this.minY, other.minY);
        double var6 = Math.min(this.minZ, other.minZ);
        double var8 = Math.max(this.maxX, other.maxX);
        double var10 = Math.max(this.maxY, other.maxY);
        double var12 = Math.max(this.maxZ, other.maxZ);
        return getBoundingBox(var2, var4, var6, var8, var10, var12);
    }

    /**
     * Returns a bounding box offseted by the specified vector (if negative numbers are given it will shrink). Args: x,
     * y, z
     */
    public AxisAlignedBB getOffsetBoundingBox(double x, double y, double z)
    {
        return getBoundingBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateXOffset(AxisAlignedBB other, double p_72316_2_)
    {
        if (other.maxY > this.minY && other.minY < this.maxY)
        {
            if (other.maxZ > this.minZ && other.minZ < this.maxZ)
            {
                double var4;

                if (p_72316_2_ > 0.0D && other.maxX <= this.minX)
                {
                    var4 = this.minX - other.maxX;

                    if (var4 < p_72316_2_)
                    {
                        p_72316_2_ = var4;
                    }
                }

                if (p_72316_2_ < 0.0D && other.minX >= this.maxX)
                {
                    var4 = this.maxX - other.minX;

                    if (var4 > p_72316_2_)
                    {
                        p_72316_2_ = var4;
                    }
                }

                return p_72316_2_;
            }
            else
            {
                return p_72316_2_;
            }
        }
        else
        {
            return p_72316_2_;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateYOffset(AxisAlignedBB other, double p_72323_2_)
    {
        if (other.maxX > this.minX && other.minX < this.maxX)
        {
            if (other.maxZ > this.minZ && other.minZ < this.maxZ)
            {
                double var4;

                if (p_72323_2_ > 0.0D && other.maxY <= this.minY)
                {
                    var4 = this.minY - other.maxY;

                    if (var4 < p_72323_2_)
                    {
                        p_72323_2_ = var4;
                    }
                }

                if (p_72323_2_ < 0.0D && other.minY >= this.maxY)
                {
                    var4 = this.maxY - other.minY;

                    if (var4 > p_72323_2_)
                    {
                        p_72323_2_ = var4;
                    }
                }

                return p_72323_2_;
            }
            else
            {
                return p_72323_2_;
            }
        }
        else
        {
            return p_72323_2_;
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    public double calculateZOffset(AxisAlignedBB other, double p_72322_2_)
    {
        if (other.maxX > this.minX && other.minX < this.maxX)
        {
            if (other.maxY > this.minY && other.minY < this.maxY)
            {
                double var4;

                if (p_72322_2_ > 0.0D && other.maxZ <= this.minZ)
                {
                    var4 = this.minZ - other.maxZ;

                    if (var4 < p_72322_2_)
                    {
                        p_72322_2_ = var4;
                    }
                }

                if (p_72322_2_ < 0.0D && other.minZ >= this.maxZ)
                {
                    var4 = this.maxZ - other.minZ;

                    if (var4 > p_72322_2_)
                    {
                        p_72322_2_ = var4;
                    }
                }

                return p_72322_2_;
            }
            else
            {
                return p_72322_2_;
            }
        }
        else
        {
            return p_72322_2_;
        }
    }

    /**
     * Returns whether the given bounding box intersects with this one. Args: axisAlignedBB
     */
    public boolean intersectsWith(AxisAlignedBB other)
    {
        return (other.maxX > this.minX && other.minX < this.maxX) && ((other.maxY > this.minY && other.minY < this.maxY) && (other.maxZ > this.minZ && other.minZ < this.maxZ));
    }

    /**
     * Offsets the current bounding box by the specified coordinates. Args: x, y, z
     */
    public AxisAlignedBB offset(double x, double y, double z)
    {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    /**
     * Returns if the supplied Vec3D is completely inside the bounding box
     */
    public boolean isVecInside(Vec3 vec)
    {
        return (vec.xCoord > this.minX && vec.xCoord < this.maxX) && ((vec.yCoord > this.minY && vec.yCoord < this.maxY) && (vec.zCoord > this.minZ && vec.zCoord < this.maxZ));
    }

    /**
     * Returns the average length of the edges of the bounding box.
     */
    public double getAverageEdgeLength()
    {
        double var1 = this.maxX - this.minX;
        double var3 = this.maxY - this.minY;
        double var5 = this.maxZ - this.minZ;
        return (var1 + var3 + var5) / 3.0D;
    }

    /**
     * Returns a bounding box that is inset by the specified amounts
     */
    public AxisAlignedBB contract(double x, double y, double z)
    {
        double var7 = this.minX + x;
        double var9 = this.minY + y;
        double var11 = this.minZ + z;
        double var13 = this.maxX - x;
        double var15 = this.maxY - y;
        double var17 = this.maxZ - z;
        return getBoundingBox(var7, var9, var11, var13, var15, var17);
    }

    /**
     * Returns a copy of the bounding box.
     */
    public AxisAlignedBB copy()
    {
        return getBoundingBox(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public MovingObjectPosition calculateIntercept(Vec3 p_72327_1_, Vec3 p_72327_2_)
    {
        Vec3 var3 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.minX);
        Vec3 var4 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.maxX);
        Vec3 var5 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.minY);
        Vec3 var6 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.maxY);
        Vec3 var7 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.minZ);
        Vec3 var8 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.maxZ);

        if (!this.isVecInYZ(var3))
        {
            var3 = null;
        }

        if (!this.isVecInYZ(var4))
        {
            var4 = null;
        }

        if (!this.isVecInXZ(var5))
        {
            var5 = null;
        }

        if (!this.isVecInXZ(var6))
        {
            var6 = null;
        }

        if (!this.isVecInXY(var7))
        {
            var7 = null;
        }

        if (!this.isVecInXY(var8))
        {
            var8 = null;
        }

        Vec3 var9 = null;

        if (var3 != null && (var9 == null || p_72327_1_.squareDistanceTo(var3) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var3;
        }

        if (var4 != null && (var9 == null || p_72327_1_.squareDistanceTo(var4) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var4;
        }

        if (var5 != null && (var9 == null || p_72327_1_.squareDistanceTo(var5) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var5;
        }

        if (var6 != null && (var9 == null || p_72327_1_.squareDistanceTo(var6) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var6;
        }

        if (var7 != null && (var9 == null || p_72327_1_.squareDistanceTo(var7) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var7;
        }

        if (var8 != null && (var9 == null || p_72327_1_.squareDistanceTo(var8) < p_72327_1_.squareDistanceTo(var9)))
        {
            var9 = var8;
        }

        if (var9 == null)
        {
            return null;
        }
        else
        {
            byte var10 = -1;

            if (var9 == var3)
            {
                var10 = 4;
            }

            if (var9 == var4)
            {
                var10 = 5;
            }

            if (var9 == var5)
            {
                var10 = 0;
            }

            if (var9 == var6)
            {
                var10 = 1;
            }

            if (var9 == var7)
            {
                var10 = 2;
            }

            if (var9 == var8)
            {
                var10 = 3;
            }

            return new MovingObjectPosition(0, 0, 0, var10, var9);
        }
    }

    /**
     * Checks if the specified vector is within the YZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInYZ(Vec3 vec)
    {
        return vec != null && (vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
    }

    /**
     * Checks if the specified vector is within the XZ dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXZ(Vec3 vec)
    {
        return vec != null && (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
    }

    /**
     * Checks if the specified vector is within the XY dimensions of the bounding box. Args: Vec3D
     */
    private boolean isVecInXY(Vec3 vec)
    {
        return vec != null && (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY);
    }

    /**
     * Sets the bounding box to the same bounds as the bounding box passed in. Args: axisAlignedBB
     */
    public void setBB(AxisAlignedBB other)
    {
        this.minX = other.minX;
        this.minY = other.minY;
        this.minZ = other.minZ;
        this.maxX = other.maxX;
        this.maxY = other.maxY;
        this.maxZ = other.maxZ;
    }

    public String toString()
    {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}