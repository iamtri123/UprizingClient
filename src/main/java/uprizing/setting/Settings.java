package uprizing.setting;

public class Settings {

	public static final int CHAT_BACKGROUND = 0, GLASS_RENDERING = 1, WORLD_TIME = 2, SCOREBOARD_SCORES = 3, SCOREBOARD_TEXT_SHADOW = 4;

	private transient Setting[] elements = {};

	private int size;

	public final int size() {
		return size;
	}

	public final Setting get(int index) {
		return elements[index];
	}

	public void add(Setting setting) {
		elements = growCapacity();
		elements[size++] = setting;
	}

	private Setting[] growCapacity() {
		final Setting[] result = new Setting[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}
}