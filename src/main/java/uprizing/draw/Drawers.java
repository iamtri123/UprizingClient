package uprizing.draw;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import uprizing.setting.Settings;
import uprizing.setting.SettingCategory;
import uprizing.draw.defaults.SettingCategoryDrawer;
import uprizing.gui.GuiUprizingMenu;
import uprizing.setting.Setting;

public class Drawers extends Gui {

	private static final int border = 8;

	private final Settings settings;
	private Drawer[] elements = {};
	private int size;

	public Drawers(final Settings settings) {
		this.settings = settings;
	}

	public final void createDrawers(int width, int height) {
		if (size != 0) {
			clear();
		}

		final int buttonWidth = width - ((width / 6) * 2) - 4 - 2 - 2 - border;

		int startX = (width / 6) + 2 + 2 + border;
		int startY = (height / 6) + 1 + 2;
		
		SettingCategory currentCategory = null;

		for (int index = 0; index < settings.count(); index++) {
			final Setting setting = settings.getByIndex(index);
			final SettingCategory category = setting.getCategory();

			if (category == null) continue; // is not drawable

			if (category != currentCategory) { // set category coords
				currentCategory = category;

				addDrawer(new SettingCategoryDrawer(category, startX, startY, buttonWidth));

				startY += currentCategory.getHeight();
			}

			addDrawer(setting.createDrawer(startX, startY, buttonWidth));

			startY += setting.getHeight();
		}
	}

	private void addDrawer(Drawer drawer) {
		final Drawer[] result = new Drawer[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		elements = result;
		elements[size++] = drawer;
	}

	public final void clear() {
		elements = new Drawer[size = 0];
	}

	public final void draw(GuiUprizingMenu gui, FontRenderer fontRenderer, GuiUprizingMenu.Scrollbar scrollbar, int mouseX, int mouseY) {
		for (int index = 0; index < size; index++) {
			final Drawer drawer = elements[index];

			if (drawer.getY() + drawer.getDrawingHeight() + 1 < scrollbar.startY + scrollbar.posY) {
				continue;
			}

			drawer.draw(gui, fontRenderer, scrollbar, mouseX, mouseY);

			if (drawer.getY() > scrollbar.endY + scrollbar.posY) {
				break;
			}
		}
	}

	public final Drawer getByMouse(int mouseX, int mouseY, GuiUprizingMenu.Scrollbar scrollbar) {
		for (int index = 0; index < size; index++) {
			// TODO: check is in the box

			if (elements[index].isHovered(mouseX, mouseY, scrollbar)) {
				return elements[index];
			}
		}

		return null;
	}

	@Deprecated
	public final int getTotal() {
		int total = 0;

		for (int index = 0; index < size; index++) {
			total += elements[index].getDrawingHeight();
		}

		return total;
	}
}