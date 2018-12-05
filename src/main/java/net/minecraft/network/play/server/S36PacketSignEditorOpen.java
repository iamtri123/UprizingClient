package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S36PacketSignEditorOpen extends Packet {

    private int x;
    private int y;
    private int z;
    private String[] lines;

    public S36PacketSignEditorOpen() {}

    public S36PacketSignEditorOpen(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void processPacket(INetHandlerPlayClient handler) {
        handler.handleSignEditorOpen(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        x = data.readInt();
        y = data.readInt();
        z = data.readInt();

        if (data.isReadable()) {
            lines = new String[4];

            for (int index = 0; index < 4; ++index) {
                lines[index] = data.readStringFromBuffer(15);
            }
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeInt(x);
        data.writeInt(y);
        data.writeInt(z);

        if (lines != null) {
            for (int index = 0; index < 4; ++index) {
                data.writeStringToBuffer(lines[index]);
            }
        }
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int z() {
        return z;
    }

    public String[] lines() {
        return lines;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayClient) handler);
    }
}