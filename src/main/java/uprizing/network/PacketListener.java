package uprizing.network;

import java.util.ArrayList;
import java.util.HashMap;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.Packet;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import uprizing.util.NameAndValue;

public class PacketListener {

    /**
     * TODO:
     * - Packet.getIndex()
     * - Packet.getName()
     *
     * - classé par protocol (server - client)
     */

    @Getter public boolean enabled = false;
    private final Minecraft minecraft;
    private final HashMap<Class<Packet>, Integer> indexes = new HashMap<>();
    @Getter private final ArrayList<PacketLinker> linkers = new ArrayList<>();
    private int size;

    public PacketListener(final Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void add(int packetId, Class<Packet> packetClass, String protocolName, boolean server) {
        indexes.put(packetClass, size++);
        linkers.add(new PacketLinker(packetId, packetClass, (server ? "Server" : "Client") + " - " + protocolName));
    }

    public void toggle(EntityClientPlayerMP player) {
        player.addChatMessage(new ChatComponentText("PacketListener set to: " + (enabled ? "Yes" : "No")).setChatStyle(new ChatStyle().setColor(enabled ? EnumChatFormatting.RED : EnumChatFormatting.GREEN).setBold(true)));
        enabled = !enabled;
    }

    public void print(Packet packet, boolean send, boolean priority) {
        //System.out.println("[" + System.currentTimeMillis() + "] " + (send ? "SEND" : "RECEIVE") + " (P=" + (priority ? 1 : 0) + "): " + packet.sex());

        final int index = indexes.get(packet.getClass());
        final PacketLinker linker = linkers.get(index);

        if (!linker.isEnabled()) return;

        if (minecraft.thePlayer != null) {
            final NameAndValue[] sex = packet.sex();
            final HoverEvent hoverEvent;

            if (sex != null) {
                final StringBuilder builder = new StringBuilder();

                for (NameAndValue nameAndValue : sex) {
                    if (builder.length() != 0) {
                        builder.append("\n");
                    }

                    builder.append("§7").append(nameAndValue.getKey())
                        .append(": §f").append(nameAndValue.getValue());
                }

                hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(builder.toString()));
            } else {
                hoverEvent = null;
            }

            minecraft.thePlayer.addChatMessage(new ChatComponentText("[Uprizing]")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY).setBold(true)
                    .setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7" + (System.currentTimeMillis() / 1000) + " seconds"))))
                .appendSibling(new ChatComponentText(" (" + (send ? "C to S" : "S to C") + ") ")
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_PURPLE).setBold(true))
                .appendSibling(new ChatComponentText(priority ? "*" + linker.getPacketName() : linker.getPacketName())
                    .setChatStyle(new ChatStyle().setColor(send ?
                        hoverEvent != null ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.GREEN :
                        hoverEvent != null ? EnumChatFormatting.DARK_AQUA : EnumChatFormatting.AQUA).setBold(true)
                    .setChatHoverEvent(hoverEvent))))
            );
        }
    }
}