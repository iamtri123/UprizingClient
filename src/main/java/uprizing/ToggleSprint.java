package uprizing;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;
import uprizing.draggable.Draggable;
import uprizing.util.Constants;
import uprizing.util.TextAndColor;

public class ToggleSprint implements Draggable {

	public static final int OFF = -1;
	public static final int SNEAKING_KEY_HELD = 0;
	public static final int SNEAKING_TOGGLED = 1;
	public static final int JUMPING_VANILLA = 2;
	public static final int JUMPING_SPRINTING = 3;
	public static final int JUMPING_MAGIC = 4;
	public static final int JUMPING_TOGGLED = 5;
	public static final int SPRINTING_KEY_HELD = 6;
	public static final int SPRINTING_VANILLA = 7;
	public static final int SPRINTING_TOGGLED = 8;
	public static final int WALKING_VANILLA = 9;

	@Getter private final String name = "ToggleSprint";
	private final int width = 100;
	private final int height = 10;
	@Getter @Setter private int posX;
	@Getter @Setter private int posY;
	public int mode = OFF;
	@Getter public final TextAndColor[] textAndColors = new TextAndColor[10];
	public boolean showText = false;
	public boolean alwaysSneaking = false;
	public boolean alwaysJumping = false;
	public boolean doubleTapping = true;
	public boolean alwaysSprinting = false;

	/**
	 * TODO:
	 * - Riding
	 * - Flying, Flying (4x boost)
	 * - Descending
	 * - Dismounting
	 * - Jumping
	 *
	 * EntityPlayer.onLivingUpdate:
	 * - 1. début
	 * - 2. movementInput.updatePlayerMoveState()
	 * - 3. juste après le 2
	 */

	public ToggleSprint() {
		textAndColors[SNEAKING_KEY_HELD] = new TextAndColor("Sneaking 1", "[Sneaking (Key Held)]", Constants.sexyColor);
		textAndColors[SNEAKING_TOGGLED] = new TextAndColor("Sneaking 2", "[Sneaking (Toggled)]", Constants.sexyColor);
		textAndColors[JUMPING_VANILLA] = new TextAndColor("Jumping 1", "[Jumping (Vanilla)]", Constants.sexyColor);
		textAndColors[JUMPING_SPRINTING] = new TextAndColor("Jumping 1", "[Jumping (Sprinting)]", Constants.sexyColor);
		textAndColors[JUMPING_MAGIC] = new TextAndColor("Jumping 2", "[Jumping (Magic)]", Constants.sexyColor);
		textAndColors[JUMPING_TOGGLED] = new TextAndColor("Jumping 3", "[Jumping (Toggled)]", Constants.sexyColor);
		textAndColors[SPRINTING_KEY_HELD] = new TextAndColor("Sprinting 1", "[Sprinting (Key Held)]", Constants.sexyColor);
		textAndColors[SPRINTING_VANILLA] = new TextAndColor("Sprinting 2", "[Sprinting (Vanilla)]", Constants.sexyColor);
		textAndColors[SPRINTING_TOGGLED] = new TextAndColor("Sprinting 3", "[Sprinting (Toggled)]", Constants.sexyColor);
		textAndColors[WALKING_VANILLA] = new TextAndColor("Walking 1", "[Walking (Vanilla)]", Constants.sexyColor);

		Uprizing.getInstance().getDraggables().addDraggable(this);
	}



	@Override
	public final boolean isHovered(int mouseX, int mouseY) {
		return showText && mouseX >= posX && mouseY >= posY && mouseX < posX + width && mouseY < posY + height;
	}

	@Override
	public final void draw(FontRenderer fontRenderer) {
		if (!showText || mode == OFF) return;

		final TextAndColor textAndColor = textAndColors[mode];

		fontRenderer.drawStringWithShadow(textAndColor.getKey(), posX, posY, textAndColor.getValue());
	}

	@Override
	public void forceDraw(FontRenderer fontRenderer) {
		if (!showText) return;

		final TextAndColor textAndColor = textAndColors[SPRINTING_TOGGLED];

		fontRenderer.drawStringWithShadow(textAndColor.getKey(), posX, posY, textAndColor.getValue());
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