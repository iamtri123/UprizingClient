package uprizing.setting;

import lombok.Getter;
import uprizing.draw.Drawed;

public class SettingCategory implements Drawed {

	public static final SettingCategory GENERAL = new SettingCategory("General");
	public static final SettingCategory CHAT = new SettingCategory("Chat");
	public static final SettingCategory SCOREBOARD = new SettingCategory("Scoreboard");
	public static final SettingCategory CPS = new SettingCategory("ClicksPerSecond");
	public static final SettingCategory FPS = new SettingCategory("FramesPerSecond");
	public static final SettingCategory TOGGLESPRINT = new SettingCategory("ToggleSprint");

	@Getter private final String name;

	public SettingCategory(final String name) {
		this.name = name + " Settings";
	}

	@Override
	public final int getHeight() {
		return 16;
	}
}