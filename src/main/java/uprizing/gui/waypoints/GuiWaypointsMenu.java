package uprizing.gui.waypoints;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import uprizing.Uprizing;
import uprizing.mods.waypoints.Waypoint;

public class GuiWaypointsMenu extends GuiScreen implements GuiYesNoCallback { // TODO: Optimize

	private static final ResourceLocation inventoryTextures = new ResourceLocation("textures/gui/container/inventory.png");
    private final Uprizing uprizing;
    private List list;
    private Waypoint selectedWaypoint;
    private GuiButton buttonDelete, buttonEdit;
    private boolean yesNoClicked;
    private String tooltip = null;

    public GuiWaypointsMenu(final Uprizing uprizing) {
        this.uprizing = uprizing;
    }

    @Override
    public void initGui() {
        list = new List();
        buttonList.add(buttonEdit = new GuiButton(-1, width / 2 - 50, height - 52, 100, 20, "Edit"));
        buttonList.add(buttonDelete = new GuiButton(-2, width / 2 - 50, height - 28, 100, 20, "Delete"));
        buttonList.add(new GuiButton(-3, width / 2 - 154, height - 52, 100, 20, "New Waypoint"));
        buttonList.add(new GuiButton(-4, width / 2 + 4 + 50, height - 52, 100, 20, "Done"));

        if (selectedWaypoint == null) {
            buttonEdit.enabled = buttonDelete.enabled = false;
        }
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        if (par1GuiButton.enabled) {
            //if (par1GuiButton.id > 0) {
            //options.setSort(par1GuiButton.id);
            //changedSort = true;
            if (par1GuiButton.id == -1) {
                uprizing.getMinecraft().displayGuiScreen(new GuiWaypoint(this, uprizing, selectedWaypoint));
            } else if (par1GuiButton.id == -2) {
                yesNoClicked = true;
                uprizing.getMinecraft().displayGuiScreen(new GuiYesNo(this, "Are you sure you want to remove this waypoint?", "will be lost forever! (A long time!)", "Delete", "Cancel", selectedWaypoint.getIndex())); // TODO: update index dans le worldLoading
            } else if (par1GuiButton.id == -3) {
                uprizing.getMinecraft().displayGuiScreen(new GuiWaypoint(uprizing));
                selectedWaypoint = null;
            } else if (par1GuiButton.id == -4) {
                uprizing.getMinecraft().displayGuiScreen(null);
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawDefaultBackground();
        tooltip = null;
        list.func_148128_a(par1, par2, par3);

        drawCenteredString(fontRendererObj, "Waypoints", width / 2, 20, 16777215);
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
    public void confirmClicked(boolean par1, int par2) {
        if (yesNoClicked) {
            if (par1) {
                uprizing.getWaypointsMod().deleteWaypoint(selectedWaypoint);
                selectedWaypoint = null;
            }

            uprizing.getMinecraft().displayGuiScreen(this);
            yesNoClicked = false;
        }
    }

    private void setSelectedWaypoint(Waypoint waypoint) {
        selectedWaypoint = waypoint;

        final boolean enabled = selectedWaypoint != null;
        buttonEdit.enabled = enabled;
        buttonDelete.enabled = enabled;
    }

    class List extends GuiSlot {

        public List() {
            super(uprizing.getMinecraft(), width, height, 54, height - 65 + 4, 18);
        }

        @Override
        protected int getSize() {
            return uprizing.getWaypointsMod().getRenderer().size();
        }

        @Override
        protected void elementClicked(int par1, boolean par2, int par3, int par4) {
            setSelectedWaypoint(uprizing.getWaypointsMod().getRenderer().get(par1));

            final int leftEdge = width / 2 - 92 - 16;
            final byte padding = 3;
            final int width = 215;

            if (field_148150_g >= leftEdge + width - 16 - padding && field_148150_g <= leftEdge + width + padding) {
                selectedWaypoint.setEnabled(!selectedWaypoint.isEnabled());
                uprizing.getWaypointsMod().saveWaypoints();
            } else if (par2) {
                Mouse.next();
                uprizing.getMinecraft().displayGuiScreen(new GuiWaypoint(GuiWaypointsMenu.this, uprizing, selectedWaypoint));
            }
        }

        @Override
        protected boolean isSelected(int par1) {
            return selectedWaypoint == uprizing.getWaypointsMod().getRenderer().get(par1);
        }

        @Override
        protected void drawBackground() {}

        @Override
        protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator tessellator, int par6, int par7) {
            final Waypoint waypoint = uprizing.getWaypointsMod().getRenderer().get(par1);
            drawCenteredString(fontRendererObj, waypoint.getName(), width / 2, par3 + 3, waypoint.getUnified());

            byte padding = 3;
            if (field_148150_g >= par2 - padding && field_148162_h >= par3 && field_148150_g <= par2 + 215 + padding && field_148162_h <= par3 + field_148149_f) {
                if (field_148150_g >= par2 + 215 - 16 - padding && field_148150_g <= par2 + 215 + padding) {
                    tooltip = waypoint.isEnabled() ? "Disable Waypoint" : "Enable Waypoint";
                } else {
                    tooltip = "X: " + waypoint.getX() + " Y: " + waypoint.getY() + " Z: " + waypoint.getZ();
                }
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            uprizing.getMinecraft().getTextureManager().bindTexture(inventoryTextures);
            drawTexturedModalRect(par2 + 198, par3 - 2, waypoint.isEnabled() ? 72 : 90, 216, 16, 16);
        }
    }
}