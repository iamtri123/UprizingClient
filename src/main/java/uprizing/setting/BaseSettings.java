package uprizing.setting;

public class BaseSettings {

	protected Setting[] elements = {};
	private int size;

	public final int count() {
		return size;
	}

	protected final void addSetting(Setting setting) {
		elements = growCapacity();
		elements[size] = setting.setIndex(size++);
	}

	private Setting[] growCapacity() {
		final Setting[] result = new Setting[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}

	public final Setting getByIndex(int index) {
		return elements[index];
	}

	public final Setting getByName(String name) {
		for (Setting setting : elements)
			if (setting.getName().equalsIgnoreCase(name)) {
				return setting;
			}
		return null;
	}
}