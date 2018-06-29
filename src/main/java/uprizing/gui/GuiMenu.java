package uprizing.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;
import uprizing.Merguez;
import uprizing.Uprizing;
import uprizing.draggable.Draggable;
import uprizing.setting.Setting;
import uprizing.setting.Settings;

public class GuiMenu extends GuiScreen {

	private final Uprizing uprizing;
	private final Settings settings;
	private Draggable draggable = null;
	private int lastMouseX, lastMouseY;

	public GuiMenu(final Uprizing uprizing) {
		this.uprizing = uprizing;
		this.settings = uprizing.getSettings();
	}

	@Override
	public void initGui() {
		settings.updateButtons(width, height);
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
		drawRect((width / Merguez.F) - 1, // horizontal
			(height / Merguez.F) + Merguez.V + 1, // vertical
			width - (width / Merguez.F) + 1, // horizontal
			height - (height / Merguez.F) + Merguez.V - 1, // vertical
				Merguez.A);

		drawRect(width / Merguez.F, // horizontal
			(height / Merguez.F) + Merguez.V, // vertical
			width - (width / Merguez.F), // horizontal
			height - (height / Merguez.F) + Merguez.V, // vertical
				Merguez.A);

		final FontRenderer fontRenderer = uprizing.getMinecraft().fontRenderer;

		drawString(fontRenderer, "General Settings",
			width / Merguez.F + 8 + 1, // x
			(height / Merguez.F) + Merguez.V + 6 + 1, // y
			Merguez.B);

		settings.draw(fontRenderer, mouseX, mouseY);

		uprizing.getDraggables().draw();
	}

	@Override
	protected void keyTyped(char p_73869_1_, int p_73869_2_) {
		if (p_73869_2_ == 1) {
			mc.displayGuiScreen(null);
			mc.setIngameFocus();
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		// TODO: MenuGuiButton pour SettingButton && autres (close: [X])
		// TODO: optimize: check si mouse est dans la box puis select settings sinon draggables

		final Setting setting = settings.getByMouse(mouseX, mouseY);
		if (setting != null) {
			//this.selectedSetting = setting;
			setting.pressButton(mc, mouseButton);
		} else {
			final Draggable hoveredDraggable = uprizing.getDraggables().getByMouse(mouseX, mouseY);
			if (hoveredDraggable != null) {
				draggable = hoveredDraggable;
				lastMouseX = mouseX;
				lastMouseY = mouseY;
			}
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		if (p_146286_3_ == 0 && draggable != null) {
			draggable = null;
		}
	}

	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		if (draggable == null) return;

		draggable.move(p_146273_1_ - lastMouseX, p_146273_2_ - lastMouseY);

		lastMouseX = p_146273_1_;
		lastMouseY = p_146273_2_;
	}

	@Override
	public void updateScreen() {
		// TODO: ne sert à rien, faire l'animation des boutons dans le drawScreen()
	}

	@Override
	public void onGuiClosed() {
		// TODO: LOGGER - System.out.println("handle close");
		// TODO: delete button UNIQUEMENT ceux render

		settings.resetButtons();
		uprizing.saveSettings();

		uprizing.getMotionBlur().handleGuiClose();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}