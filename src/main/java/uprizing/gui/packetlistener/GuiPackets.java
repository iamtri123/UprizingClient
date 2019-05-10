package uprizing.gui.packetlistener;

import java.util.Iterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import uprizing.network.PacketLinker;
import uprizing.network.PacketListener;

public class GuiPackets extends GuiScreen {

    private final GuiScreen parentScreen;
    private GuiPacketList packetList;
    private boolean setAll = false;

    public GuiPackets(GuiScreen p_i1027_1_) {
        this.parentScreen = p_i1027_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.packetList = new GuiPacketList(this, this.mc);
        this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done")));
        this.buttonList.add(new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, "Set All to OFF"));
    }

    protected void actionPerformed(GuiButton button) {
        if (button.id == 200) {
            this.mc.displayGuiScreen(this.parentScreen);
        } else if (button.id == 201) {
            final PacketListener packetListener = mc.uprizing.packetListener;
            final Iterator<PacketLinker> iterator = packetListener.getLinkers().iterator();

            while (iterator.hasNext()) {
                iterator.next().setEnabled(setAll);
            }

            setAll = !setAll;

            packetList.update();

            buttonList.get(1).displayString = "Set All to " + (setAll ? "ON" : "OFF");
        }
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton != 0 || !this.packetList.func_148179_a(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
        if (state != 0 || !this.packetList.func_148181_b(mouseX, mouseY, state)) {
            super.mouseMovedOrUp(mouseX, mouseY, state);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.packetList.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}