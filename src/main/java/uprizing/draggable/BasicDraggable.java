package uprizing.draggable;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import uprizing.setting.SettingCategory;
import uprizing.util.Constants;

@Getter @Setter
public abstract class BasicDraggable implements Draggable {

	private final Tessellator tessellator = Tessellator.instance;
	private final int extraPosY = 3;

	private final String name;
	private final int width = 58;
	private final int height = 13;
	private int posX = 1;
	private int posY = 1;
	private boolean enabled = true;
	private int textColor = Constants.sexyColor;
	private boolean showBackground = true;
	private int backgroundColor = 1140850688;

	public BasicDraggable(final String name) {
		this.name = name;

		if (name.equalsIgnoreCase("FramesPerSecond")) {
			posY += height + 1;
		}
	}

	public abstract SettingCategory getSettingCategory();

	public abstract String getText();

	public abstract int getTextWidth();

	@Override
	public final boolean isHovered(int mouseX, int mouseY) {
		return enabled && mouseX >= posX && mouseY >= posY && mouseX < posX + width && mouseY < posY + height;
	}

	@Override
	public final void draw(FontRenderer fontRenderer) {
		if (!enabled) return;

		if (showBackground) {
			Gui_drawRect(posX, posY, posX + width, posY + height, backgroundColor);
		}

		fontRenderer.drawString(getText(), posX + getTextWidth(), posY + extraPosY, textColor);
	}

	@Override
	public void forceDraw(FontRenderer fontRenderer) {
		draw(fontRenderer);
	}

	private void Gui_drawRect(int left, int top, int right, int bottom, int color) {
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

	@Override
	public final void move(int mouseX, int mouseY, int width, int height) {
		posX += mouseX;
		posY += mouseY;

		if (posX < 0) {
			posX = 0;
		} else if (posX > width - this.width) {
			posX = width - this.width;
		}

		if (posY < 0) {
			posY = 0;
		} else if (posY > height - this.height) {
			posY = height - this.height;
		}
	}
}