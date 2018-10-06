package uprizing;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.FontRenderer;
import uprizing.draggables.Draggable;

@Getter
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

	public boolean enabled = false;
	private final String name = "ToggleSprint";
	private final int width = 100;
	private final int height = 10;
	@Setter private int posX;
	@Setter private int posY;
	private final String sexyText = "[Stawlker (Master)]";
	public int mode = -1;
	public final TextAndColor[] textAndColors = new TextAndColor[10];
	public boolean showText = false;
	public boolean sneakEnabled = false;
	public boolean jumpEnabled = false;
	public boolean sprintEnabled = false; // TODO: public int sprintMode = Off, On, Double-Tapping.
	public boolean alwaysSprinting = false;
	public double flyingBoost = 0;
	private double[] storage = { 1, 1, 0, 1, 1, 4 };

	public ToggleSprint() {
		textAndColors[SNEAKING_KEY_HELD] = new TextAndColor("Sneaking 1", "[Sneaking (Key Held)]", -1);
		textAndColors[SNEAKING_TOGGLED] = new TextAndColor("Sneaking 2", "[Sneaking (Toggled)]", -1);
		textAndColors[JUMPING_VANILLA] = new TextAndColor("Jumping 1", "[Jumping (Vanilla)]", -1);
		textAndColors[JUMPING_SPRINTING] = new TextAndColor("Jumping 1", "[Jumping (Sprinting)]", -1);
		textAndColors[JUMPING_MAGIC] = new TextAndColor("Jumping 2", "[Jumping (Magic)]", -1);
		textAndColors[JUMPING_TOGGLED] = new TextAndColor("Jumping 3", "[Jumping (Toggled)]", -1);
		textAndColors[SPRINTING_KEY_HELD] = new TextAndColor("Sprinting 1", "[Sprinting (Key Held)]", -1);
		textAndColors[SPRINTING_VANILLA] = new TextAndColor("Sprinting 2", "[Sprinting (Vanilla)]", -1);
		textAndColors[SPRINTING_TOGGLED] = new TextAndColor("Sprinting 3", "[Sprinting (Toggled)]", -1);
		textAndColors[WALKING_VANILLA] = new TextAndColor("Walking 1", "[Walking (Vanilla)]", -1);

		/**
		 * TODO:
		 * - Riding
		 * - Flying, Flying (4x boost)
		 * - Descending
		 * - Dismounting
		 * - Jumping
		 */
	}

	public final TextAndColor[] getTextAndColors() {
		return textAndColors;
	}

	public void enable() {
		enabled = true;

		showText = storage[0] == 1;
		sneakEnabled = storage[1] == 1;
		jumpEnabled = storage[2] == 1;
		sprintEnabled = storage[3] == 1;
		alwaysSprinting = storage[4] == 1;
		flyingBoost = storage[5];
	}

	public void disable() {
		storage[0] = showText ? 1 : 0;
		storage[1] = sneakEnabled ? 1 : 0;
		storage[2] = jumpEnabled ? 1 : 0;
		storage[3] = sprintEnabled ? 1 : 0;
		storage[4] = alwaysSprinting ? 1 : 0;
		storage[5] = flyingBoost;

		enabled = showText = sneakEnabled = jumpEnabled = sprintEnabled = alwaysSprinting = false;
		flyingBoost = 0;
	}

	@Override
	public final boolean isHovered(int mouseX, int mouseY) {
		return enabled && mouseX >= posX && mouseY >= posY && mouseX < posX + width && mouseY < posY + height;
	}

	@Override
	public final void move(int mouseX, int mouseY) {
		posX += mouseX;
		posY += mouseY;
	}

	@Override
	public final void draw(FontRenderer fontRenderer) {
		if (!showText || mode == -1) return;

		final TextAndColor textAndColor = textAndColors[mode];

		fontRenderer.drawStringWithShadow(textAndColor.getKey(), posX, posY, textAndColor.getValue());
	}

	@Override
	public final void drawSlut(FontRenderer fontRenderer) {
		if (!enabled) return;

		fontRenderer.drawStringWithShadow(sexyText, posX, posY, -1);
	}
}