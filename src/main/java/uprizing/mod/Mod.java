package uprizing.mod;

import net.minecraft.client.settings.KeyBinding;

public interface Mod {

    ModMetadata getMetadata();

    void onRenderTick();

    KeyBinding[] getKeyBindings();

    int getKeyBindingSize();
}