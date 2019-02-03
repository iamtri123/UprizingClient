package uprizing.setting;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import uprizing.draw.Drawed;
import uprizing.draw.Drawer;
import uprizing.draw.defaults.SettingDrawer;

@Getter
public abstract class Setting implements Drawed {

	private int index = -1; // initialized with setIndex() method
	private final String name;
	private final String key;
	private SettingCategory category = null;

	public Setting(final String name) {
		this(name, name);
	}

	public Setting(final String name, final String key) {
		this.name = name;
		this.key = key;
	}

	public final Setting setIndex(int index) {
		this.index = index;
		return this;
	}

	public final Setting setCategory(SettingCategory category) {
		this.category = category;
		return this;
	}

	/* -- File -- */

	public abstract String serialize();

	public abstract void deserialize(String string);

	/* -- Button -- */

	@Override
	public int getHeight() {
		return 10;
	}

	public Drawer createDrawer(final int x, final int y, final int width) {
		return new SettingDrawer(this, x, y, width);
	}

	public void handleMouseClick(Minecraft minecraft, int mouseButton) {
		minecraft.getSoundHandler().playSound(PositionedSoundRecord.createPositionedSoundRecord(new ResourceLocation("gui.button.press"), 1.0F));
	}

	/* -- ??? -- */

	public boolean getAsBoolean() {
		return false;
	}

	public String getAsString() {
		return null;
	}

	public long getAsLong() {
		return 0;
	}

	public int getAsInt() {
		return 0;
	}
}