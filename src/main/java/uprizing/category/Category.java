package uprizing.category;

import lombok.Getter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import uprizing.Merguez;
import uprizing.setting.Setting;

@Getter
public class Category extends Gui { // TODO: retirer

	private final String name;

	private transient SubCategory[] elements = {};
	private int size;

	public Category(final String name) {
		this.name = name + Merguez.S;
	}

	protected void addSubCategory(SubCategory subCategory) {
		elements = growCapacity();
		elements[size++] = subCategory;
	}

	private SubCategory[] growCapacity() {
		final SubCategory[] result = new SubCategory[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}

	public void draw(FontRenderer fontRenderer, int mouseX, int mouseY) {
		for (int i = 0; i < size; i++) {
			final SubCategory subCategory = elements[i];
			fontRenderer.drawStringWithShadow(subCategory.getName(), subCategory.getTextX(), subCategory.getTextY(), Merguez.E);

			for (int j = 0; j < subCategory.size(); j++) {
				final Setting setting = subCategory.get(j);
				setting.drawButton(fontRenderer, mouseX, mouseY);
			}
		}
	}

	public Setting getByMouse(int mouseX, int mouseY) {
		for (int i = 0; i < size; i++) {
			final SubCategory subCategory = elements[i];
			for (int j = 0; j < subCategory.size(); j++) {
				final Setting setting = subCategory.get(j);
				if (setting.isHovered(mouseX, mouseY)) {
					return setting;
				}
			}
		}
		return null;
	}

	public void updateButtons(int width, int height) {
		int count = 0;

		for (int i = 0; i < size; i++) {
			final SubCategory subCategory = elements[i];
			count++;

			subCategory.setTextX((width / Merguez.F) + 8 + 4 + 1);
			subCategory.setTextY((height / Merguez.F) + Merguez.V + (count * 10) + 1 + Merguez.G);

			for (int j = 0; j < subCategory.size(); j++) {
				final Setting setting = subCategory.get(j);
				count++;

				final int buttonX = (width / Merguez.F) + 8;
				final int buttonY = (height / Merguez.F) + Merguez.V + (count * 10) + Merguez.G;
				final int buttonWidth = width - (buttonX * 2);

				setting.updateButton(buttonX, buttonY, buttonWidth);
			}
		}
	}

	public void resetButtons() {
		for (int i = 0; i < size; i++) {
			final SubCategory subCategory = elements[i];
			for (int j = 0; j < subCategory.size(); j++) {
				subCategory.get(j).resetButton();
			}
		}
	}
}