package uprizing.option;

import net.minecraft.client.gui.FontRenderer;
import uprizing.utils.Merguez;

public abstract class AbstractOptions {
	
	private transient SubCategory[] elements = {};
	
	private int size;
	
	void addSubCategory(SubCategory subCategory) {
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
				final Option option = subCategory.get(j);
				option.drawButton(fontRenderer, mouseX, mouseY);
			}
		}
	}

	public Option getByMouse(int mouseX, int mouseY) {
		for (int i = 0; i < size; i++) {
			final SubCategory subCategory = elements[i];
			for (int j = 0; j < subCategory.size(); j++) {
				final Option option = subCategory.get(j);
				if (option.isHovered(mouseX, mouseY)) {
					return option;
				}
			}
		}
		return null;
	}

	public void updateButtons(int width, int height) {
		int count = 0;

		for (int i = 0; i < size; i++) {
			final SubCategory subCategory = elements[i];

			subCategory.setTextX((width / Merguez.F) + 8 + 1);
			subCategory.setTextY((height / Merguez.F) + Merguez.V + (count * 10) + 1 + Merguez.G);

			count++;

			for (int j = 0; j < subCategory.size(); j++) {
				final Option option = subCategory.get(j);

				final int buttonX = (width / Merguez.F) + 8;
				final int buttonY = (height / Merguez.F) + Merguez.V + (count * 10) + Merguez.G;
				final int buttonWidth = width - (buttonX * 2);

				count++;

				option.updateButton(buttonX, buttonY, buttonWidth);
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