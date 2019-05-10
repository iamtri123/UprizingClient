package uprizing.gui.packetlistener;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import uprizing.network.PacketLinker;

public class GuiPacketList extends GuiListExtended {

    private final Minecraft minecraft;
    private final IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;

    public GuiPacketList(GuiPackets guiPackets, Minecraft minecraft) {
        super(minecraft, guiPackets.width, guiPackets.height, 63, guiPackets.height - 32, 20);

        this.minecraft = minecraft;

        final ArrayList<PacketLinker> linkers = minecraft.uprizing.packetListener.getLinkers();
        final ArrayList<IGuiListEntry> entries = new ArrayList<>();

        String current = null;

        for (int index = 0; index < linkers.size(); ++index) {
            final PacketLinker linker = linkers.get(index);
            final String categoryName = linker.getProtocolName();

            if (!categoryName.equals(current)) {
                entries.add(new GuiPacketList.CategoryEntry("§9§l" + categoryName));
                current = categoryName;
            }

            final int stringWidth = minecraft.fontRenderer.getStringWidth(linker.getPacketClass().getSimpleName());

            if (stringWidth > maxListLabelWidth) {
                maxListLabelWidth = stringWidth;
            }

            entries.add(new GuiPacketList.KeyEntry(linker));
        }

        listEntries = entries.toArray(new IGuiListEntry[0]);
    }

    @Deprecated
    public void update() {
        for (IGuiListEntry entry : listEntries) {
            if (entry instanceof GuiPacketList.KeyEntry) {
                final KeyEntry keyEntry = (KeyEntry) entry;
                keyEntry.btnToggle.displayString = !keyEntry.linker.isEnabled() ? "§c§lOFF" : "§a§lON";
            }
        }
    }

    protected int getSize() {
        return listEntries.length;
    }

    public IGuiListEntry getListEntry(int p_148180_1_) {
        return listEntries[p_148180_1_];
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    public class CategoryEntry implements IGuiListEntry {

        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String p_i45028_2_) {
            this.labelText = I18n.format(p_i45028_2_);
            this.labelWidth = GuiPacketList.this.minecraft.fontRenderer.getStringWidth(this.labelText);
        }

        public boolean isMovable() {
            return true;
        }

        public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
            GuiPacketList.this.minecraft.fontRenderer.drawString(this.labelText, GuiPacketList.this.minecraft.currentScreen.width / 2 - labelWidth / 2, y + p_148279_5_ - GuiPacketList.this.minecraft.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }

        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            return false;
        }

        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
    }

    public class KeyEntry implements IGuiListEntry {

        private final PacketLinker linker;
        private final String keyBindingName;
        private final GuiButton btnToggle;

        private KeyEntry(PacketLinker linker) {
            this.linker = linker;
            this.keyBindingName = "§e§l" + linker.getPacketName() + " §f§l(" + linker.getPacketId() + ")";
            this.btnToggle = new GuiButton(0, 0, 0, 50, 18, linker.isEnabled() ? "§a§lON" : "§c§lOFF");
        }

        public boolean isMovable() {
            return true;
        }

        public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
            GuiPacketList.this.minecraft.fontRenderer.drawString(this.keyBindingName, x + 90 - GuiPacketList.this.maxListLabelWidth, y + p_148279_5_ / 2 - GuiPacketList.this.minecraft.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnToggle.xPosition = x + 190;
            this.btnToggle.yPosition = y;
            this.btnToggle.drawButton(GuiPacketList.this.minecraft, p_148279_7_, p_148279_8_);
        }

        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            if (this.btnToggle.mousePressed(GuiPacketList.this.minecraft, p_148278_2_, p_148278_3_)) {
                linker.setEnabled(!linker.isEnabled());
                btnToggle.displayString = !linker.isEnabled() ? "§c§lOFF" : "§a§lON";
                return true;
            } else {
                return false;
            }
        }

        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
            this.btnToggle.mouseReleased(p_148277_2_, p_148277_3_);
        }
    }
}