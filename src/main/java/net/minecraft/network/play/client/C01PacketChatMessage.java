package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C01PacketChatMessage extends Packet
{
    private String message;

    public C01PacketChatMessage() {}

    public C01PacketChatMessage(String messageIn)
    {
        if (messageIn.length() > 100)
        {
            messageIn = messageIn.substring(0, 100);
        }

        this.message = messageIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.message = data.readStringFromBuffer(100);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeStringToBuffer(this.message);
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processChatMessage(this);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize()
    {
        return String.format("message=\'%s\'", this.message);
    }

    public String getMessage()
    {
        return this.message;
    }

    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }
}