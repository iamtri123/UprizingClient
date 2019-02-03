package uprizing.draggable;

import net.minecraft.client.gui.FontRenderer;

public class Draggables {

	public static final int CPS = 0;
	public static final int FPS = 1;
	public static final int TS = 2;

	private Draggable[] elements = {};
	private int size;
	private int cursor;

	public final void draw(FontRenderer fontRenderer) {
		while (cursor != size)
			elements[cursor++].draw(fontRenderer);
		cursor = 0;
	}

	public final void forceDraw(FontRenderer fontRenderer) {
		while (cursor != size)
			elements[cursor++].forceDraw(fontRenderer);
		cursor = 0;
	}

	public void addDraggable(Draggable draggable) {
		final Draggable[] result = new Draggable[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		elements = result;
		elements[size++] = draggable;
	}

	public final Draggable getByMouse(int mouseX, int mouseY) {
		for (Draggable draggable : elements)
			if (draggable.isHovered(mouseX, mouseY)) {
				return draggable;
			}
		return null;
	}

	public final Draggable[] getAsArray() {
		return elements;
	}
}