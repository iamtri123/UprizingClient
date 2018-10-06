package uprizing.draggables;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import uprizing.ClicksPerSecond;
import uprizing.ToggleSprint;
import uprizing.draggables.defaults.CPSDraggable;
import uprizing.draggables.defaults.FPSDraggable;

@Getter
public class Draggables { // TODO: draggable pour chaque keyStrokes

	private final Draggable[] elements = new Draggable[3];
	private int size;
	private int cursor;

	public Draggables(final ClicksPerSecond clicksPerSecond, final Minecraft minecraft, final ToggleSprint toggleSprint) {
		elements[size++] = new CPSDraggable(clicksPerSecond);
		elements[size++] = new FPSDraggable(minecraft.getFpsCounter());
		elements[size++] = toggleSprint;
	}

	public final void draw(FontRenderer fontRenderer) {
		final boolean enabled = GL11.glIsEnabled(3042);
		GL11.glDisable(3042);

		while (cursor != size) {
			elements[cursor++].draw(fontRenderer);
		}

		cursor = 0;

		if (enabled) {
			GL11.glEnable(3042);
		}
	}

	public final void drawSlut(FontRenderer fontRenderer) {
		while (cursor != size) {
			elements[cursor++].drawSlut(fontRenderer);
		}

		cursor = 0;
	}

	public final Draggable getByIndex(int index) {
		return elements[index];
	}

	public final Draggable getByMouse(int mouseX, int mouseY) {
		for (Draggable draggable : elements) {
			if (draggable.isHovered(mouseX, mouseY)) {
				return draggable;
			}
		}
		return null;
	}
}