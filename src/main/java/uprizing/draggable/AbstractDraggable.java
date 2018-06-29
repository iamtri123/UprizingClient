package uprizing.draggable;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

@Getter @Setter
public abstract class AbstractDraggable implements Draggable {

	private final Tessellator tessellator = Tessellator.instance;
	private boolean enabled;
	private final String name;
	private final int width, height;
	private int posX, posY, textColor = -1, backgroundColor = 1140850688;
	private double scale = 1;
	private boolean showBackground = true;

	public AbstractDraggable(final String name, final int width, final int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	@Override
	public boolean isHovered(int mouseX, int mouseY) {
		return enabled && mouseX >= posX && mouseY >= posY && mouseX < posX + width && mouseY < posY + height;
	}

	public final void move(int mouseX, int mouseY) {
		posX += mouseX;
		posY += mouseY;
	}

	public abstract String getText();

	public abstract int getTextWidth();

	public final void draw(FontRenderer fontRenderer) {
		if (!enabled) return;

		final String text = getText();
		final int textWidth = getTextWidth();

		if (showBackground) {
			drawRect(posX, posY, posX + width, posY + height, backgroundColor);
		}

		fontRenderer.drawString(text, posX + textWidth, posY + 3, textColor);
	}

	private void drawRect(int left, int top, int right, int bottom, int color) {
		int var5;

		if (left < right) {
			var5 = left;
			left = right;
			right = var5;
		}

		if (top < bottom) {
			var5 = top;
			top = bottom;
			bottom = var5;
		}

		float var10 = (float) (color >> 24 & 255) / 255.0F;
		float var6 = (float) (color >> 16 & 255) / 255.0F;
		float var7 = (float) (color >> 8 & 255) / 255.0F;
		float var8 = (float) (color & 255) / 255.0F;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(var6, var7, var8, var10);
		tessellator.startDrawingQuads();
		tessellator.addVertex((double) left, (double) bottom, 0.0D);
		tessellator.addVertex((double) right, (double) bottom, 0.0D);
		tessellator.addVertex((double) right, (double) top, 0.0D);
		tessellator.addVertex((double) left, (double) top, 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}
}