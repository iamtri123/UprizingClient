package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S34PacketMaps extends Packet
{
    private int mapId;
    private byte[] field_149190_b;

    public S34PacketMaps() {}

    public S34PacketMaps(int p_i45202_1_, byte[] p_i45202_2_)
    {
        this.mapId = p_i45202_1_;
        this.field_149190_b = p_i45202_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.mapId = data.readVarIntFromBuffer();
        this.field_149190_b = new byte[data.readUnsignedShort()];
        data.readBytes(this.field_149190_b);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.mapId);
        data.writeShort(this.field_149190_b.length);
        data.writeBytes(this.field_149190_b);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleMaps(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("id=%d, length=%d", Integer.valueOf(this.mapId), Integer.valueOf(this.field_149190_b.length));
    }

    public int getMapId()
    {
        return this.mapId;
    }

    public byte[] getData()
    {
        return this.field_149190_b;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}