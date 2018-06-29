package uprizing.setting;

import lombok.Getter;
import lombok.Setter;
import uprizing.Merguez;

@Getter @Setter
public class SubCategory {

	private final String name;

	private final Settings settings;

	private int textX, textY;

	private transient Setting[] elements = {};

	private int size;

	SubCategory(final Settings settings, final String name) {
		this.settings = settings;
		this.name = name + Merguez.S;
	}

	public final int size() {
		return size;
	}

	public final Setting get(int index) {
		return elements[index];
	}

	public SubCategory add(Setting setting) {
		elements = growCapacity();
		elements[size++] = setting;
		settings.add(setting);
		return this;
	}

	private Setting[] growCapacity() {
		final Setting[] result = new Setting[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}
}