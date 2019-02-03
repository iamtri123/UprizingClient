package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S09PacketHeldItemChange extends Packet
{
    private int field_149387_a;

    public S09PacketHeldItemChange() {}

    public S09PacketHeldItemChange(int p_i45215_1_)
    {
        this.field_149387_a = p_i45215_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_149387_a = data.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeByte(this.field_149387_a);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleHeldItemChange(this);
    }

    public int func_149385_c()
    {
        return this.field_149387_a;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}