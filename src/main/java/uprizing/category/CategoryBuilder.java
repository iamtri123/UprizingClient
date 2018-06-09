package uprizing.category;

import uprizing.Uprizing;
import uprizing.setting.Setting;
import uprizing.setting.Settings;

public class CategoryBuilder {

	private final Settings settings = Uprizing.getInstance().getSettings();
	private transient Setting[] elements = {};
	private int size;

	public CategoryBuilder add(Setting setting) {
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

	public final Setting[] get() {
		final Setting[] result = elements;
		elements = null; // clear to let GC do its work
		return result;
	}
}