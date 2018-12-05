package uprizing.protocol;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

import java.io.IOException;

public class C18PacketUprizing extends Packet {

	private int type;

	public C18PacketUprizing() {}

	public C18PacketUprizing(int type) {
		this.type = type;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer data) throws IOException {
		this.type = data.readByte();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer data) throws IOException {
		data.writeByte(type);
	}

	public void processPacket(INetHandlerPlayServer handler) {
		handler.processUprizing(this);
	}

	/**
	 * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
	 */
	public String serialize() {
		return String.format("type=%d", type);
	}

	public void processPacket(INetHandler handler) {
		this.processPacket((INetHandlerPlayServer) handler);
	}

	@Override
	public boolean hasPriority() {
		return true;
	}
}