package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;

public class C0EPacketClickWindow extends Packet {

    private int windowId;
    private int slotId;
    private int usedButton;
    private short actionNumber;
    private ItemStack clickedItem;
    private int mode;

    public C0EPacketClickWindow() {}

    public C0EPacketClickWindow(int p_i45246_1_, int p_i45246_2_, int p_i45246_3_, int p_i45246_4_, ItemStack p_i45246_5_, short p_i45246_6_) {
        this.windowId = p_i45246_1_;
        this.slotId = p_i45246_2_;
        this.usedButton = p_i45246_3_;
        this.clickedItem = p_i45246_5_ != null ? p_i45246_5_.copy() : null;
        this.actionNumber = p_i45246_6_;
        this.mode = p_i45246_4_;
    }

    public void processPacket(INetHandlerPlayServer handler) {
        handler.processClickWindow(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.windowId = data.readByte();
        this.slotId = data.readShort();
        this.usedButton = data.readByte();
        this.actionNumber = data.readShort();
        this.mode = data.readByte();
        this.clickedItem = data.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeByte(this.windowId);
        data.writeShort(this.slotId);
        data.writeByte(this.usedButton);
        data.writeShort(this.actionNumber);
        data.writeByte(this.mode);
        data.writeItemStackToBuffer(this.clickedItem);
    }

    /**
     * Returns a string formatted as comma separated [field]=[value] values. Used by Minecraft for logging purposes.
     */
    public String serialize() {
        return this.clickedItem != null ? String.format("id=%d, slot=%d, button=%d, type=%d, itemid=%d, itemcount=%d, itemaux=%d", this.windowId, this.slotId, this.usedButton, this.mode, Item.getIdFromItem(this.clickedItem.getItem()), this.clickedItem.stackSize, this.clickedItem.getItemDamage()) : String.format("id=%d, slot=%d, button=%d, type=%d, itemid=-1", this.windowId, this.slotId, this.usedButton, this.mode);
    }

    public int windowId() {
        return this.windowId;
    }

    public int slot() {
        return this.slotId;
    }

    public int button() {
        return this.usedButton;
    }

    public short actionNumber() {
        return this.actionNumber;
    }

    public ItemStack clickedItem() {
        return this.clickedItem;
    }

    public int mode() {
        return this.mode;
    }

    public void processPacket(INetHandler handler) {
        this.processPacket((INetHandlerPlayServer) handler);
    }
}