package uprizing.mods;

import lombok.Getter;
import net.minecraft.client.settings.KeyBinding;

@Getter
public class ModRepository {

	private transient Mod[] elements = {};

	private int cursor, size, keyBindingSize;

	public final void addMod(Mod mod) {
		elements = growCapacity();
		keyBindingSize += mod.getKeyBindingSize();
		elements[size++] = mod;
	}

	private Mod[] growCapacity() {
		final Mod[] result = new Mod[elements.length + 1];
		System.arraycopy(elements, 0, result, 0, elements.length);
		return result;
	}

	public final boolean hasNext() {
		return cursor != size;
	}

	public final Mod next() {
		return elements[cursor++];
	}

	public final void close() {
		cursor = 0;
	}

	public final KeyBinding[] getKeyBindings() {
		final KeyBinding[] result = new KeyBinding[keyBindingSize];

		int cursor = 0;
		for (Mod mod : elements) {
			final KeyBinding[] keyBindings = mod.getKeyBindings();
			if (keyBindings == null) continue;

			for (KeyBinding keyBinding : keyBindings) {
				result[cursor++] = keyBinding;
			}
		}

		return result;
	}
}