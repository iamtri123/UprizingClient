package uprizing.waypoint;

import java.io.File;
import lombok.Getter;
import net.minecraft.client.Minecraft;

@Getter
public class Waypoints extends WaypointsContainer {

    private final Minecraft minecraft;

    public Waypoints(final Minecraft minecraft, final File mainDir) {
        super(mainDir);
        this.minecraft = minecraft;
        this.loadWaypointsFromFile();
    }

    public final Waypoint createWaypoint(String name, double x, double y, double z, float red, float blue, float green, boolean enabled) {
        if (minecraft.isIntegratedServerRunning()) {
            return new SingleplayerWaypoint(minecraft.getIntegratedServer().getWorldName(), name, x, y, z, red, blue, green, enabled);
        } else {
            return new MultiplayerWaypoint(minecraft.uprizing.getServerHostAddress(), name, x, y, z, red, blue, green, enabled);
        }
    }

    @Override
    public final String toString() {
        return "Waypoints";
    }
}