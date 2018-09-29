package uprizing.settings;

public class BaseSettings {

	protected transient Setting[] elements = {};
	private int size;

	public final int count() {
		return size;
	}

	protected final void addSetting(Setting setting) {
		elements = growCapacity();
		elements[size++] = setting;
	}

	private Setting[] growCapacity() {
		final Setting[] result = new Setting[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}

	public final Setting get(int index) {
		return elements[index];
	}
}