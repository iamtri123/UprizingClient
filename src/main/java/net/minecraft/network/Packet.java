package net.minecraft.network;

import com.google.common.collect.BiMap;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Packet
{
    private static final Logger logger = LogManager.getLogger();

    /**
     * Returns a packet instance, given the params: BiMap<int, (Packet) Class> and (int) id
     */
    public static Packet generatePacket(BiMap protocolMap, int packetId)
    {
        try
        {
            Class var2 = (Class)protocolMap.get(Integer.valueOf(packetId));
            return var2 == null ? null : (Packet)var2.newInstance();
        }
        catch (Exception var3)
        {
            logger.error("Couldn\'t create packet " + packetId, var3);
            return null;
        }
    }

    /**
     * Will write a byte array to supplied ByteBuf as a separately defined structure by prefixing the byte array with
     * its length
     */
    public static void writeBlob(ByteBuf buffer, byte[] blob)
    {
        buffer.writeShort(blob.length);
        buffer.writeBytes(blob);
    }

    /**
     * Will read a byte array from the supplied ByteBuf, the first short encountered will be interpreted as the size of
     * the byte array to read in
     */
    public static byte[] readBlob(ByteBuf buffer) throws IOException
    {
        short var1 = buffer.readShort();

        if (var1 < 0)
        {
            throw new IOException("Key was smaller than nothing!  Weird key!");
        }
        else
        {
            byte[] var2 = new byte[var1];
            buffer.readBytes(var2);
            return var2;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public abstract void readPacketData(PacketBuffer data) throws IOException;

    /**
     * Writes the raw packet data to the data stream.
     */
    public abstract void writePacketData(PacketBuffer data) throws IOException;

    public abstract void processPacket(INetHandler handler);

    /**
     * If true, the network manager will process the packet immediately when received, otherwise it will queue it for
     * processing. Currently true for: Disconnect, LoginSuccess, KeepAlive, ServerQuery/Info, Ping/Pong
     */
    public boolean hasPriority()
    {
        return false;
    }

    public String toString()
    {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return "";
    }
}