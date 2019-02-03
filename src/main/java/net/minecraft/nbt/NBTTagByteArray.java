package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase
{
    /** The byte array stored in the tag. */
    private byte[] byteArray;

    NBTTagByteArray() {}

    public NBTTagByteArray(byte[] p_i45128_1_)
    {
        this.byteArray = p_i45128_1_;
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException
    {
        output.writeInt(this.byteArray.length);
        output.write(this.byteArray);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        int var4 = input.readInt();
        sizeTracker.addSpaceRead((long)(8 * var4));
        this.byteArray = new byte[var4];
        input.readFully(this.byteArray);
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return (byte)7;
    }

    public String toString()
    {
        return "[" + this.byteArray.length + " bytes]";
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy()
    {
        byte[] var1 = new byte[this.byteArray.length];
        System.arraycopy(this.byteArray, 0, var1, 0, this.byteArray.length);
        return new NBTTagByteArray(var1);
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Arrays.equals(this.byteArray, ((NBTTagByteArray) p_equals_1_).byteArray);
    }

    public int hashCode()
    {
        return super.hashCode() ^ Arrays.hashCode(this.byteArray);
    }

    public byte[] getByteArray()
    {
        return this.byteArray;
    }
}