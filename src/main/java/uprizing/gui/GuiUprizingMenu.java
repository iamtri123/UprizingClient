package uprizing.gui;

import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import uprizing.ISelectable;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.draw.Drawer;
import uprizing.draw.Drawers;
import uprizing.util.Constants;

public class GuiUprizingMenu extends GuiScreen {

	private static final int backgroundColor = new Color(195, 255, 240, 255).getRGB();
	public static final int sexyColor = new Color(154, 205, 190, 255).getRGB();
	public static final int scrollbarBall = new Color(106, 146, 138, 255).getRGB();
	//public static final int test = new Color(255, 18, 18, 255).getRGB();

	private final Uprizing uprizing;
	private final FontRenderer fontRenderer;
	private Scrollbar scrollbar;
	private ISelectable selectable = null;
	private int lastMouseX;
	private int lastMouseY;

	public GuiUprizingMenu(final Uprizing uprizing) {
		this.uprizing = uprizing;
		this.fontRenderer = uprizing.getMinecraft().fontRenderer;
	}

	@Override
	public void initGui() {
		uprizing.getDrawers().createDrawers(width, height);

		scrollbar = new Scrollbar(width, height, uprizing.getDrawers());
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();

		final int value = Mouse.getEventDWheel();
		if (value == 0) return;

        scrollbar.move(value > 0 ? -1 : 1);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(width / 6,
			height / 6,
			width - (width / 6),
			height - (height / 6),
			backgroundColor);

		drawRect(width / 6 + 1,
			height / 6 - 1,
			width - (width / 6) - 1,
			height - (height / 6) + 1,
			backgroundColor);

		uprizing.getDrawers().draw(this, fontRenderer, scrollbar, mouseX, mouseY);

		scrollbar.draw();

		if (selectable != null) {
			selectable.move(mouseX - lastMouseX, mouseY - lastMouseY, width, height);

			lastMouseX = mouseX;
			lastMouseY = mouseY;
		}

		uprizing.getDraggables().forceDraw(fontRenderer);

		drawRect(width / 6 + 1,
			height / 6 - 1,
			width - (width / 6) - 1,
			height / 6 + 1 + 2 + 1,
			backgroundColor);

		drawRect(width / 6 + 1,
			height - (height / 6) - 1 - 2 - 1,
			width - (width / 6) - 1,
			height - (height / 6) + 1,
			backgroundColor);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) {
		if (keyCode == 1) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		final Draggable draggable = uprizing.getDraggables().getByMouse(mouseX, mouseY);
		if (draggable != null) {
			this.selectable = draggable;

			lastMouseX = mouseX;
			lastMouseY = mouseY;
		} else {
			final Drawer drawer = uprizing.getDrawers().getByMouse(mouseX, mouseY, scrollbar);
			if (drawer != null) {
				drawer.handleMouseClick(mc, mouseX, mouseY, mouseButton);
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		if (state == 0 && selectable != null) {
			selectable = null;
		}
	}

	@Override
	public void updateScreen() {
		// TODO: call chaque tick pour update les fields
	}

	@Override
	public void onGuiClosed() {
		uprizing.getDrawers().clear();
		uprizing.saveSettings();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

    public class Scrollbar {

        public final int x;
        public final int width = 2;
        public final int startY;
        public final int endY;

        public int posY = 0;
        int maxEntryPerPage;
        int totalEntry;

        Scrollbar(final int width, final int height, Drawers drawers) {
            x = (width / 6) + 2 + 2;
            startY = (height / 6) + 1 + 2 + 1;
            endY = height - (height / 6) - 1 - 2 - 1;

            maxEntryPerPage = endY - startY;
            totalEntry = drawers.getTotal();
        }

        public void draw() {
            Gui.drawRect(x, startY, x + width, endY, Constants.hoveredTextColor);

            final int height;

            if (totalEntry <= maxEntryPerPage) {
                height = maxEntryPerPage;
            } else {
                final float ratio = (maxEntryPerPage * 100.0f) / totalEntry; // pourcentage de value sur le total (genre 8 sur 10 = 80%)

                final float reste = 100.0f - ratio; // reste en pourcentage (genre 8 sur 10 = 20%)

                final float val = (float) maxEntryPerPage;

                height = maxEntryPerPage - (int) (reste * val / 100.f); // TODO: static field
            }

            Gui.drawRect(x - 1, startY + posY, x + width + 1, startY + height + posY, GuiUprizingMenu.scrollbarBall);
        }

        public void move(int posY) {
            this.posY += posY;

            if (this.posY < 0) {
                this.posY = 0;
            }
        }
    }
}