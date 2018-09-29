package uprizing.settings;

public class StringSetting extends Setting {

	private static final char BITE = '\'';
	private String value;

	public StringSetting(final String name, final String defaultValue) {
		super(name);
		this.value = defaultValue;
	}

	@Override
	public final void foo(String string) {
		if (string.charAt(0) == BITE)
			string = string.substring(1, string.length() - 1);
		value = string;
	}

	@Override
	public final String bar() {
		return BITE + value + BITE;
	}

	@Override
	public final String getAsString() {
		return value;
	}
}