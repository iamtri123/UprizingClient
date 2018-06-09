package uprizing.mods;

import net.minecraft.client.settings.KeyBinding;
import uprizing.TickType;

public interface Mod {

    ModMetadata getMetadata();

    void runTick(TickType tickType);

    KeyBinding[] getKeyBindings();

    int getKeyBindingSize();
}