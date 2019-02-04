package uprizing.setting.defaults;

import uprizing.util.TextAndColor;
import uprizing.setting.Setting;

public class TextAndColorSetting extends Setting {

	private static final String SEPARATOR = "¤";

	private final TextAndColor textAndColor;

	public TextAndColorSetting(final TextAndColor textAndColor) {
		super("TextAndColor", "TextAndColor - " + textAndColor.getName());
		this.textAndColor = textAndColor;
	}

	@Override
	public final String serialize() {
		return textAndColor.getKey() + SEPARATOR + textAndColor.getValue() + SEPARATOR + (textAndColor.isChroma() ? "1" : "0");
	}

	@Override
	public final void deserialize(String configValue) {
		final String[] strings = configValue.split(SEPARATOR);
		textAndColor.setKey(strings[0]);
		textAndColor.setValue(Integer.parseInt(strings[1]));
		textAndColor.setChroma(strings[2].equals("1"));
	}
}