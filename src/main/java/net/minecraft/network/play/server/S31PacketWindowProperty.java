package net.minecraft.network.play.server;

import java.io.IOException;

import lombok.Getter;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

@Getter
public class S31PacketWindowProperty extends Packet {
	
    private int windowId, property, value;
    private String title;

    public S31PacketWindowProperty() {}

    public S31PacketWindowProperty(int p_i45187_1_, int p_i45187_2_, int p_i45187_3_) {
        this.windowId = p_i45187_1_;
        this.property = p_i45187_2_;
        this.value = p_i45187_3_;
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleWindowProperty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        windowId = data.readUnsignedByte();
        property = data.readShort();

        if (property == 3) {
        	title = data.readStringFromBuffer(32);
		} else {
			value = data.readShort();
		}
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(windowId);
        data.writeShort(property);

        if (property == 3) {
        	data.writeStringToBuffer(title);
		} else {
			data.writeShort(value);
		}
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}