package uprizing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Deprecated
public class Fontest {

	private final TextureManager renderEngine = Minecraft.getMinecraft().getTextureManager();
	private final ResourceLocation locationFontTexture = new ResourceLocation("uprizing/ascii.png");
	private float posX, posY;
	private final float[] charWidth = new float[256];
	private float scaleFactor = 1.0F;

	public Fontest() {
		readFontTexture();
	}

	private void readFontTexture() {
		BufferedImage bufferedimage;

		try {
			bufferedimage = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(this.locationFontTexture).getInputStream());
		} catch (IOException var18) {
			throw new RuntimeException(var18);
		}

		int imgWidth = bufferedimage.getWidth();
		int imgHeight = bufferedimage.getHeight();
		int charW = imgWidth / 16;
		int charH = imgHeight / 16;
		float kx = (float)imgWidth / 128.0F;
		this.scaleFactor = Config.limit(kx, 1.0F, 2.0F);
		int[] ai = new int[imgWidth * imgHeight];
		bufferedimage.getRGB(0, 0, imgWidth, imgHeight, ai, 0, imgWidth);
		int k = 0;

		while (k < 256) {
			int cx = k % 16;
			int cy = k / 16;
			boolean px = false;
			int var19 = charW - 1;

			while (true) {
				if (var19 >= 0) {
					int x = cx * charW + var19;
					boolean flag = true;

					for (int py = 0; py < charH && flag; ++py) {
						int ypos = (cy * charH + py) * imgWidth;
						int col = ai[x + ypos];
						int al = col >> 24 & 255;

						if (al > 16) {
							flag = false;
						}
					}

					if (flag) {
						--var19;
						continue;
					}
				}

				if (k == 65) {
					k = k;
				}

				if (k == 32) {
					if (charW <= 8) {
						var19 = (int)(2.0F * kx);
					} else {
						var19 = (int)(1.5F * kx);
					}
				}

				this.charWidth[k] = (float)(var19 + 1) / kx + 1.0F;
				++k;
				break;
			}
		}

		// TODO: this.readCustomCharWidths();
	}

	public void drawString(String text, int x, int y, int color) {
		drawString(text, x, y, color, true);
	}

	public void drawString(String text, int x, int y, int color, boolean shadow) {
		GL11.glEnable(GL11.GL_ALPHA_TEST);

		if (shadow) {
			this.renderString(text, x + 1, y + 1, color, true);
			this.renderString(text, x, y, color, false);
		} else {
			this.renderString(text, x, y, color, false);
		}
	}

	private void renderString(String text, int x, int y, int color, boolean jsp) {
		if ((color & -67108864) == 0) {
			color |= -16777216;
		}

		if (jsp) {
			color = (color & 16579836) >> 2 | color & -16777216;
		}

		final float r = (float) (color >> 16 & 255) / 255.0F;
		final float b = (float) (color >> 8 & 255) / 255.0F;
		final float g = (float) (color & 255) / 255.0F;
		final float a = (float) (color >> 24 & 255) / 255.0F;

		GL11.glColor4f(r, g, b, a);

		this.posX = (float) x;
		this.posY = (float) y;

		renderStringAtPos(text);
	}

	private void renderStringAtPos(String text) {
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			int var5 = "\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c);

			float var8 = this.renderCharAtPos(var5, c);

			this.posX += var8;
		}
	}

	private float renderCharAtPos(int par1, char c) {
		return c == 32 ? charWidth[c] : renderDefaultChar(par1);
	}

	private float renderDefaultChar(int par1) {
		float i = (float)(par1 % 16 * 8);
		float var4 = (float)(par1 / 16 * 8);
		float var5 = 0.0F;
		this.bindTexture(this.locationFontTexture);
		float var6 = 7.99F;
		GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		GL11.glTexCoord2f(i / 128.0F, var4 / 128.0F);
		GL11.glVertex3f(this.posX + var5, this.posY, 0.0F);
		GL11.glTexCoord2f(i / 128.0F, (var4 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX - var5, this.posY + 7.99F, 0.0F);
		GL11.glTexCoord2f((i + var6 - 1.0F) / 128.0F, var4 / 128.0F);
		GL11.glVertex3f(this.posX + var6 - 1.0F + var5, this.posY, 0.0F);
		GL11.glTexCoord2f((i + var6 - 1.0F) / 128.0F, (var4 + 7.99F) / 128.0F);
		GL11.glVertex3f(this.posX + var6 - 1.0F - var5, this.posY + 7.99F, 0.0F);
		GL11.glEnd();
		return charWidth[par1];
	}

	public void bindTexture(ResourceLocation location) {
		this.renderEngine.bindTexture(location);
	}
	
	private float zLevel = 0;

	public void square(int x, int y, int width, int height, int color) {
		float var10 = (float) (color >> 24 & 255) / 255.0F;
		float var6 = (float) (color >> 16 & 255) / 255.0F;
		float var7 = (float) (color >> 8 & 255) / 255.0F;
		float var8 = (float) (color & 255) / 255.0F;
		Tessellator var9 = Tessellator.instance;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(var6, var7, var8, var10);
		var9.startDrawingQuads();
		var9.addVertex((double)x, (double)(y + height), (double) zLevel);
		var9.addVertex((double)(x + width), (double)(y + height), (double) zLevel);
		var9.addVertex((double)(x + width), (double)(y), (double) zLevel);
		var9.addVertex((double) x, (double)(y), (double) zLevel);
		var9.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

	/**
	 * x, y: pos de la position ou l'élément doit être collé
	 * u, v: début en haut à gauche de l'élement qui doit être copié
	 * width, height: de l'élément coupé
	 */
	public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height) {
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;
		Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * var7), (double)((float)(v + height) * var8));
		var9.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * var7), (double)((float)(v + height) * var8));
		var9.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(u + width) * var7), (double)((float)(v + 0) * var8));
		var9.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * var7), (double)((float)(v + 0) * var8));
		var9.draw();
	}

	public void drawTexturedModalRect2(ResourceLocation resourceLocation, int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_) {
		float field_152443_c = 0;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * field_152443_c);
		bindTexture(resourceLocation);

		float var5 = 150.0F;
		float var6 = 0.0F;
		float var7 = (float) p_152436_3_ * 0.015625F;
		float var8 = 1.0F;
		float var9 = (float) (p_152436_3_ + 16) * 0.015625F;
		Tessellator var10 = Tessellator.instance;
		var10.startDrawingQuads();
		var10.addVertexWithUV((double)(p_152436_1_ - 16 - p_152436_4_), (double)(p_152436_2_ + 16), (double)var5, (double)var6, (double)var9);
		var10.addVertexWithUV((double)(p_152436_1_ - p_152436_4_), (double)(p_152436_2_ + 16), (double)var5, (double)var8, (double)var9);
		var10.addVertexWithUV((double)(p_152436_1_ - p_152436_4_), (double)(p_152436_2_ + 0), (double)var5, (double)var8, (double)var7);
		var10.addVertexWithUV((double)(p_152436_1_ - 16 - p_152436_4_), (double)(p_152436_2_ + 0), (double)var5, (double)var6, (double)var7);
		var10.draw();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}