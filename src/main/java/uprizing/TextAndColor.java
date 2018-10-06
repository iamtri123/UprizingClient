package uprizing;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class TextAndColor {

	private final String name;
	private String key;
	private int value;

	TextAndColor(final String name, final String key, final int value) {
		this.name = name;
		this.key = key;
		this.value = value;
	}
}