package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S13PacketDestroyEntities extends Packet
{
    private int[] field_149100_a;
    private static final String __OBFID = "CL_00001320";

    public S13PacketDestroyEntities() {}

    public S13PacketDestroyEntities(int ... p_i45211_1_)
    {
        this.field_149100_a = p_i45211_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149100_a = new int[data.readByte()];

        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2)
        {
            this.field_149100_a[var2] = data.readInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_149100_a.length);

        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2)
        {
            data.writeInt(this.field_149100_a[var2]);
        }
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleDestroyEntities(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        StringBuilder var1 = new StringBuilder();

        for (int var2 = 0; var2 < this.field_149100_a.length; ++var2)
        {
            if (var2 > 0)
            {
                var1.append(", ");
            }

            var1.append(this.field_149100_a[var2]);
        }

        return String.format("entities=%d[%s]", Integer.valueOf(this.field_149100_a.length), var1);
    }

    public int[] func_149098_c()
    {
        return this.field_149100_a;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}