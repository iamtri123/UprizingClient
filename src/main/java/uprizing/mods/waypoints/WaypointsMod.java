package uprizing.mods.waypoints;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import uprizing.Stawlker;
import uprizing.TickType;
import uprizing.Uprizing;
import uprizing.gui.waypoints.GuiWaypoint;
import uprizing.gui.waypoints.GuiWaypointsMenu;
import uprizing.mods.Mod;
import uprizing.mods.ModMetadata;

import java.io.File;

@Getter
public class WaypointsMod extends WaypointsContainer implements Mod {

    private final transient Uprizing uprizing;
    private final transient Minecraft minecraft;
    private final ModMetadata metadata = new ModMetadata("Waypoints", "Stawlker");
    private final KeyBinding addWaypointKeyBinding = Stawlker.keyBinding("Add Waypoint", Keyboard.KEY_B, "Uprizing Client"); // 48
    private final KeyBinding openWaypointsMenuKeyBinding = Stawlker.keyBinding("Open Waypoints Menu", Keyboard.KEY_M, "Uprizing Client"); // 50

    public WaypointsMod(final Uprizing uprizing, final File mainDir) {
        super(mainDir);
        this.uprizing = uprizing;
        this.minecraft = uprizing.getMinecraft();
        this.loadWaypoints();
    }

    @Override
    public void runTick(TickType tickType) {
        if (minecraft.currentScreen == null) {
            if (addWaypointKeyBinding.isPressed()) {
                minecraft.displayGuiScreen(new GuiWaypoint(uprizing));
            } else if (openWaypointsMenuKeyBinding.isPressed()) {
                minecraft.displayGuiScreen(new GuiWaypointsMenu(uprizing));
            }
        }
    }

    public final Waypoint createWaypoint(String name, double x, double y, double z, float red, float blue, float green, boolean enabled) {
        if (minecraft.isIntegratedServerRunning()) {
            return new SingleplayerWaypoint(minecraft.getIntegratedServer().getWorldName(), name, x, y, z, red, blue, green, enabled);
        } else {
            return new MultiplayerWaypoint(minecraft.sexy().getServerHostAddress(), name, x, y, z, red, blue, green, enabled);
        }
    }

    @Override
    public final KeyBinding[] getKeyBindings() {
        return new KeyBinding[] { addWaypointKeyBinding, openWaypointsMenuKeyBinding };
    }

    @Override
    public final int getKeyBindingSize() {
        return 2;
    }

    @Override
    public String toString() {
        return metadata.getName();
    }
}