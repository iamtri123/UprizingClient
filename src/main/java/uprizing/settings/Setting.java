package uprizing.settings;

import lombok.Getter;
import net.minecraft.client.Minecraft;

@Getter
public abstract class Setting {

	private final String name;
	private final String sexyName; // TODO: temporary
	private SettingType type = null;

	public Setting(final String name) {
		this.name = name;
		this.sexyName = name;
	}

	public Setting(final String name, final String prefix) {
		this.name = name;
		this.sexyName = prefix + name;
	}

	public final String name() {
		return name;
	}

	public final Setting type(SettingType type) {
		this.type = type;
		return this;
	}

	public abstract void foo(String string);

	public abstract String bar();

	public void pressButton(Minecraft minecraft, int mouseButton) {
		//minecraft.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
	}

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