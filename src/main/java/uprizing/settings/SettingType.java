package uprizing.settings;

public class SettingType {

	private final String name;
	int textX;
	int textY;

	public SettingType(final String name) {
		this.name = name + " Settings";
	}

	public final String name() {
		return name;
	}
}