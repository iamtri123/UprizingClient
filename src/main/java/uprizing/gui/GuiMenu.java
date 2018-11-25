package uprizing.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import uprizing.Constants;
import uprizing.Uprizing;
import uprizing.UprizingSettings;
import uprizing.draggables.Draggable;
import uprizing.settings.Setting;

public class GuiMenu extends GuiScreen {

	private final Uprizing uprizing;
	private final UprizingSettings settings;
	private final FontRenderer fontRenderer;
	private int lastMouseX;
	private int lastMouseY;
	private Draggable draggable = null;

	public GuiMenu(final Uprizing uprizing) {
		this.uprizing = uprizing;
		this.settings = uprizing.getSettings();
		this.fontRenderer = uprizing.getMinecraft().fontRenderer;
	}

	@Override
	public void initGui() {
		settings.getDrawer().createButtons(width, height);
	}

	@Override
	public void handleMouseInput() {
		super.handleMouseInput();

		final int value = Mouse.getEventDWheel();
		if (value != 0) {
			// TODO: menu scrolling
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect((width / Constants.F) - 1, // horizontal
			(height / Constants.F) + Constants.V + 1, // vertical
			width - (width / Constants.F) + 1, // horizontal
			height - (height / Constants.F) + Constants.V - 1, // vertical
				Constants.A);

		drawRect(width / Constants.F, // horizontal
			(height / Constants.F) + Constants.V, // vertical
			width - (width / Constants.F), // horizontal
			height - (height / Constants.F) + Constants.V, // vertical
				Constants.A);

		settings.getDrawer().draw(fontRenderer, mouseX, mouseY);
		uprizing.getDraggables().drawSlut(fontRenderer);
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
		// TODO: MenuGuiButton pour SettingButton && autres (close: [X])
		// TODO: optimize: check si mouse est dans la box puis select settings sinon draggables

		final Draggable hoveredDraggable = uprizing.getDraggables().getByMouse(mouseX, mouseY);
		if (hoveredDraggable != null) {
			draggable = hoveredDraggable;
			lastMouseX = mouseX;
			lastMouseY = mouseY;
		} else {
			final Setting setting = settings.getDrawer().getByMouse(mouseX, mouseY);
			if (setting != null) {
				//this.selectedSetting = setting;
				setting.pressButton(mc, mouseButton);
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int mouseX, int mouseY, int state) {
		if (state == 0 && draggable != null) {
			draggable = null;
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (draggable == null) return;

		draggable.move(mouseX - lastMouseX, mouseY - lastMouseY);

		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}

	@Override
	public void updateScreen() {
		// TODO: ne sert à rien, faire l'animation des boutons dans le drawScreen()
	}

	@Override
	public void onGuiClosed() {
		// TODO: LOGGER - System.out.println("handle close");
		// TODO: delete button UNIQUEMENT ceux render

		settings.getDrawer().destroyButtons();
		uprizing.saveSettings();

		uprizing.getMotionBlur().handleGuiClose();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}