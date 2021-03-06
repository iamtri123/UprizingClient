package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C10PacketCreativeInventoryAction extends Packet
{
    private int slotId;
    private ItemStack stack;

    public C10PacketCreativeInventoryAction() {}

    public C10PacketCreativeInventoryAction(int p_i45263_1_, ItemStack p_i45263_2_)
    {
        this.slotId = p_i45263_1_;
        this.stack = p_i45263_2_ != null ? p_i45263_2_.copy() : null;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processCreativeInventoryAction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.slotId = data.readShort();
        this.stack = data.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeShort(this.slotId);
        data.writeItemStackToBuffer(this.stack);
    }

    public int getSlotId()
    {
        return this.slotId;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}