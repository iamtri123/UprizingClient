package uprizing.network;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.Packet;

public class PacketLinker {

    @Getter private final int packetId;
    @Getter private final Class<Packet> packetClass;
    @Getter private final String protocolName;
    @Getter private final String packetName;
    @Getter @Setter private boolean enabled = true;

    public PacketLinker(final int packetId, final Class<Packet> packetClass, final String protocolName) {
        this.packetId = packetId;
        this.packetClass = packetClass;
        this.protocolName = protocolName;
        this.packetName = packetClass.getSimpleName().substring(3).replaceAll("Packet", "");
    }
}