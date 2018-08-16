package uprizing.option;

import lombok.Getter;
import lombok.Setter;
import uprizing.Merguez;

@Getter @Setter
public class SubCategory { // TODO: rename

	private final String name;

	private final Options settings;

	private int textX, textY;

	private transient Option[] elements = {};

	private int size;

	SubCategory(final Options settings, final String name) {
		this.settings = settings;
		this.name = name + Merguez.S;
	}

	public final int size() {
		return size;
	}

	public final Option get(int index) {
		return elements[index];
	}

	public SubCategory add(Option option) {
		elements = growCapacity();
		elements[size++] = option;
		settings.add(option);
		return this;
	}

	private Option[] growCapacity() {
		final Option[] result = new Option[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}
}