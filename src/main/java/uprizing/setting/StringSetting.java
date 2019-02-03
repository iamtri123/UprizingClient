package uprizing.setting;

public class StringSetting extends Setting {

	private static final char BITE = '\'';

	private String value;

	public StringSetting(final String name, final String key, final String defaultValue) {
		super(name, key);
		this.value = defaultValue;
	}

	@Override
	public final void deserialize(String string) {
		if (string.charAt(0) == BITE)
			string = string.substring(1, string.length() - 1);
		value = string;
	}

	@Override
	public final String serialize() {
		return BITE + value + BITE;
	}

	@Override
	public final String getAsString() {
		return value;
	}
}