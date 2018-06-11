package uprizing.draggable;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import uprizing.Uprizing;
import uprizing.draggable.draggables.CPSDraggable;
import uprizing.draggable.draggables.FPSDraggable;

@Getter
public class Draggables { // TODO: draggable pour chaque keyStrokes

	private final Minecraft minecraft;
	private final Draggable[] elements;
	private final CPSDraggable cps; // reference for Minecraft class interactions

	public Draggables(final Uprizing uprizing) {
		this.minecraft = uprizing.getMinecraft();
		this.elements = new Draggable[] {
			cps = new CPSDraggable(),
			new FPSDraggable(uprizing)
		};
	}

	public final Draggable getByIndex(int index) {
		return elements[index];
	}

	public final void draw() {
		final boolean enabled = GL11.glIsEnabled(3042);
		GL11.glDisable(3042);

		final FontRenderer fontRenderer = minecraft.fontRenderer;

		for (Draggable draggable : elements) {
			draggable.draw(fontRenderer);
		}

		if (enabled) {
			GL11.glEnable(3042);
		}
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