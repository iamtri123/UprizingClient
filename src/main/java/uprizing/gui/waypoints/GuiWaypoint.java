package uprizing.gui.waypoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import uprizing.Dimensions;
import uprizing.Stawlker;
import uprizing.Uprizing;
import uprizing.gui.GuiSlotUprizing;
import uprizing.mods.waypoints.Waypoint;

import java.util.Random;

public class GuiWaypoint extends GuiScreen { // TODO: Optimize

    private final GuiScreen prevScreen;
    private final Uprizing uprizing;
    private List list;
    private final Waypoint waypoint;
    private GuiTextField name, x, y, z;
    private String tooltip = null;
    private Dimensions selectedDimension;
    private static final Random random = new Random();
    private static final Dimensions[] DIMENSIONS = { Dimensions.NETHER, Dimensions.OVERWORLD, Dimensions.THE_END };

    public GuiWaypoint(final Uprizing uprizing) {
        this(null, uprizing, uprizing.getWaypointsMod().createWaypoint("",
            Stawlker.round(uprizing.getMinecraft().thePlayer.posX),
            Stawlker.round(uprizing.getMinecraft().thePlayer.posY),
            Stawlker.round(uprizing.getMinecraft().thePlayer.posZ),
            random.nextFloat(), random.nextFloat(), random.nextFloat(), true));
    }

    GuiWaypoint(final GuiScreen prevScreen, final Uprizing uprizing, final Waypoint waypoint) {
        this.prevScreen = prevScreen;
        this.uprizing = uprizing;
        this.waypoint = waypoint;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        name = new GuiTextField(fontRendererObj, width / 2 - 100, height / 6 + 13, 200, 20);
        name.setText(waypoint.getName());
        name.setFocused(true);

        x = new GuiTextField(fontRendererObj, width / 2 - 100, height / 6 + 41 + 13, 56, 20);
        x.func_146203_f(128);
        x.setText("" + waypoint.getX());

        y = new GuiTextField(fontRendererObj, width / 2 - 28, height / 6 + 41 + 13, 56, 20);
        y.func_146203_f(128);
        y.setText("" + waypoint.getY());

        z = new GuiTextField(fontRendererObj, width / 2 + 44, height / 6 + 41 + 13, 56, 20);
        z.func_146203_f(128);
        z.setText("" + waypoint.getZ());

        buttonList.add(new GuiButton(0, width / 2 - 155, height / 6 + 168, 150, 20, "Done"));
        buttonList.add(new GuiButton(1, width / 2 + 5, height / 6 + 168, 150, 20, "Cancel"));
        buttonList.add(new GuiButton(2, width / 2 - 101, height / 6 + 82 + 6, 100, 20, "Enabled: " + (waypoint.isEnabled() ? "ON" : "OFF")));
        buttonList.get(0).enabled = name.getText().length() > 0;

        list = new List(this);
        list.registerScrollButtons(7, 8);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled) {
            if (button.id == 2) {
                waypoint.setEnabled(!waypoint.isEnabled());
            } else if (button.id == 1) {
                mc.displayGuiScreen(prevScreen);
            } else if (button.id == 0) {
                waypoint.setName(name.getText());
                waypoint.setX(Double.parseDouble(x.getText()));
                waypoint.setY(Double.parseDouble(y.getText()));
                waypoint.setZ(Double.parseDouble(z.getText()));

                if (prevScreen == null) {
                    uprizing.getWaypointsMod().addWaypoint(waypoint);
                }

                uprizing.getWaypointsMod().saveWaypoints();
                mc.displayGuiScreen(null);
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        buttonList.get(2).displayString = "Enabled: " + (waypoint.isEnabled() ? "ON" : "OFF");
        drawDefaultBackground();
        tooltip = null;
        list.drawScreen(par1, par2, par3);
        drawCenteredString(fontRendererObj, prevScreen == null ? "New Waypoint" : "Edit Waypoint", width / 2, 20, 16777215);

        drawString(fontRendererObj, "Waypoint Name", width / 2 - 100, height / 6, 10526880);
        drawString(fontRendererObj, "X", width / 2 - 100, height / 6 + 41, 10526880);
        drawString(fontRendererObj, "Y", width / 2 - 28, height / 6 + 41, 10526880);
        drawString(fontRendererObj, "Z", width / 2 + 44, height / 6 + 41, 10526880);
        drawString(fontRendererObj, "Choose Color:", width / 2 + 10, (height / 6 + 82 + 11) + 1, 10526880);

        name.drawTextBox();
        x.drawTextBox();
        y.drawTextBox();
        z.drawTextBox();
        GL11.glColor4f(waypoint.getRed(), waypoint.getGreen(), waypoint.getBlue(), 1.0F);

        GL11.glBindTexture(3553, -1);
        drawTexturedModalRect(width / 2 + 85, height / 6 + 82 + 11, 0, 0, 16, 10);
        super.drawScreen(par1, par2, par3);

        if (tooltip != null) {
            drawTooltip(tooltip, par1, par2);
        }
    }

    private void drawTooltip(String par1Str, int par2, int par3) {
        int var4 = par2 + 12;
        int var5 = par3 - 12;
        int var6 = fontRendererObj.getStringWidth(par1Str);
        drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
        fontRendererObj.drawStringWithShadow(par1Str, var4, var5, -1);
    }

    @Override
    public void updateScreen() {
        name.updateCursorCounter();
        x.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        super.mouseClicked(par1, par2, par3);
        name.mouseClicked(par1, par2, par3);
        x.mouseClicked(par1, par2, par3);
        z.mouseClicked(par1, par2, par3);
        y.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2) { // TODO: opti
        name.textboxKeyTyped(par1, par2);
        x.textboxKeyTyped(par1, par2);
        z.textboxKeyTyped(par1, par2);
        y.textboxKeyTyped(par1, par2);

        if (par1 == '\t') {
            if (name.isFocused()) {
                name.setFocused(false);
                x.setFocused(true);
                y.setFocused(false);
                z.setFocused(false);
            } else if (x.isFocused()) {
                name.setFocused(false);
                x.setFocused(false);
                y.setFocused(true);
                z.setFocused(false);
            } else if (y.isFocused()) {
                name.setFocused(false);
                x.setFocused(false);
                y.setFocused(false);
                z.setFocused(true);
            } else if (z.isFocused()) {
                name.setFocused(true);
                x.setFocused(false);
                y.setFocused(false);
                z.setFocused(false);
            }
        } else if (par1 == '\r') {
            actionPerformed((GuiButton) buttonList.get(0));
        }

        boolean enabled = name.getText().length() > 0;
        if (enabled) {
            enabled = Stawlker.isDbl(x.getText());

            if (enabled) {
                enabled = Stawlker.isDbl(y.getText());

                if (enabled) {
                    enabled = Stawlker.isDbl(z.getText());
                }
            }
        }

        ((GuiButton) buttonList.get(0)).enabled = enabled;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    class List extends GuiSlotUprizing {

        private final GuiWaypoint prevScreen;

        public List(final GuiWaypoint prevScreen) {
            super(uprizing.getMinecraft(), prevScreen.width, prevScreen.height, prevScreen.height / 6 + 123 - 14, prevScreen.height / 6 + 164 + 3, 18);
            this.prevScreen = prevScreen;
            setSlotWidth(175);
            setSlotXBoundsFromLeft((prevScreen.width - slotWidth) / 2);
            setShowSelectionBox(false);
            setShowTopBottomBG(false);
            setShowSlotBG(false);
        }

        @Override
        protected int getSize() {
            return DIMENSIONS.length;
        }

        @Override
        protected void elementClicked(int par1, boolean par2, int x, int y) {
            selectedDimension = Dimensions.getByValue(par1);

            int leftEdge = prevScreen.width / 2 - slotWidth / 2;
            byte padding = 4, iconWidth = 16;

            int width = slotWidth;
            if (mouseX >= leftEdge + width - iconWidth - padding && mouseX <= leftEdge + width) {
                waypoint.updateDimension(par1);
            } else if (par2) {
                Mouse.next();
                waypoint.updateDimension(par1);
            }
        }

        @Override
        protected boolean isSelected(int par1) {
            return selectedDimension != null && selectedDimension.getId() == par1 - 1; // TODO: test (par1 -> index dans la liste)
        }

        @Override
        protected int getContentHeight() {
            return getSize() * 18;
        }

        @Override
        protected void drawBackground() {}

        @Override
        protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator tessellator, int x, int y) {
            final Dimensions dimension = DIMENSIONS[par1];
            drawCenteredString(fontRendererObj, dimension.getName(), prevScreen.width / 2, par3 + 3, 16777215);

            byte padding = 4;
            par2 = prevScreen.width / 2 - 175 / 2;
            int width = slotWidth;

            if (mouseX >= par2 + padding && mouseY >= par3 && mouseX <= par2 + width + padding && mouseY <= par3 + slotHeight) {
                if (mouseX >= par2 + width - 16 - padding && mouseX <= par2 + width) {
                    tooltip = waypoint.isInDimension(par1) ? "Visible in this dimension" : "Not visible in this dimension";
                } else {
                    tooltip = null;
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            uprizing.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/beacon.png")); // TODO: Optimize ?
            drawTexturedModalRect(par2 + width - 16, par3 - 2, waypoint.isInDimension(par1) ? 91 : 113, 222, 16, 16);
        }
    }
}