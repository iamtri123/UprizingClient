package uprizing.mod;

import lombok.Getter;
import net.minecraft.client.settings.KeyBinding;

@Getter
public class ModRepository {

    private transient IMod[] mods = {};

    private int cursor, size, keyBindingSize;

    public void addMod(IMod mod) {
        mods = growCapacity();
        keyBindingSize += mod.getKeyBindingSize();
        mods[size++] = mod;
    }

    private IMod[] growCapacity() {
        final IMod[] result = new IMod[mods.length + 1];
        System.arraycopy(mods, 0, result, 0, mods.length);
        return result;
    }

    public boolean hasNext() {
        return cursor != size;
    }

    public final IMod next() {
        return mods[cursor++];
    }

    public void close() {
        cursor = 0;
    }

    public final KeyBinding[] getKeyBindings() {
        final KeyBinding[] result = new KeyBinding[keyBindingSize];

        int cursor = 0;
        for (IMod mod : mods) {
            final KeyBinding[] keyBindings = mod.getKeyBindings();
            if (keyBindings == null) continue;

            for (KeyBinding keyBinding : keyBindings) {
                result[cursor++] = keyBinding;
            }
        }

        return result;
    }
}