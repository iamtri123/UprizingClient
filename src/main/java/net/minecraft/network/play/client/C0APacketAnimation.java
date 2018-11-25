package net.minecraft.network.play.client;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C0APacketAnimation extends Packet {

    private int id, type;

    public C0APacketAnimation() {}

    public C0APacketAnimation(Entity p_i45238_1_, int p_i45238_2_) {
        this.id = p_i45238_1_.getEntityId();
        this.type = p_i45238_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.id = data.readInt();
        this.type = data.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeInt(this.id);
        data.writeByte(this.type);
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processAnimation(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize() {
        return String.format("id=%d, type=%d", this.id, this.type);
    }

    public int getType() {
        return this.type;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }
}