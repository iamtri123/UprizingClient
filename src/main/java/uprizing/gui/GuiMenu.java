package uprizing.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import uprizing.Merguez;
import uprizing.Uprizing;
import uprizing.category.Category;
import uprizing.setting.Setting;

public class GuiMenu extends GuiScreen {

	private final Uprizing uprizing;
	private final Category category; // TODO: change

	public GuiMenu(final Uprizing uprizing) {
		this.uprizing = uprizing;
		this.category = uprizing.getDefaultCategory();
	}

	@Override
	public void initGui() {
		category.updateButtons(width, height);
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

		drawString(fontRenderer, category.getName(),
			width / Merguez.F + 8 + 1, // x
			(height / Merguez.F) + Merguez.V + 6 + 1, // y
			Merguez.B);

		category.draw(fontRenderer, mouseX, mouseY);
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
		// TODO: LOGGER - System.out.println("mouseClicked");

		if (mouseButton != 0) return; // pas un clique droit

		// TODO: MenuGuiButton pour SettingButton && autres (close: [X])

		final Setting setting = category.getByMouse(mouseX, mouseY);
		if (setting != null) {
			//this.selectedSetting = setting;
			setting.pressButton(mc);
		}
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		// TODO: LOGGER - System.out.println("mouseMovedOrUp");
	}

	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_, long p_146273_4_) {
		// TODO: LOGGER - System.out.println("mouseClickMove");
	}

	@Override
	public void updateScreen() {
		// TODO: ne sert à rien, faire l'animation des boutons dans le drawScreen()
	}

	@Override
	public void onGuiClosed() {
		// TODO: LOGGER - System.out.println("handle close");
		// TODO: delete button UNIQUEMENT ceux render

		category.resetButtons();
		uprizing.saveSettings();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
}