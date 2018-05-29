package uprizing.mod;

import net.minecraft.client.settings.KeyBinding;
import uprizing.TickType;

public interface IMod {

    String getName();

    void runTick(TickType tickType);

    KeyBinding[] getKeyBindings();

    int getKeyBindingSize();
}