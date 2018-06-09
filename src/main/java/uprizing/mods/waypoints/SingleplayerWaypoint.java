package uprizing.mods.waypoints;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

@Getter @Setter
public class SingleplayerWaypoint extends Waypoint {

    private String worldName;

    public SingleplayerWaypoint(final String worldName, final String name, final double x, final double y, final double z, final float red, final float green, final float blue, final boolean enabled) {
        super(name, x, y, z, red, green, blue, enabled);
        this.worldName = worldName;
    }

    @Override
    public boolean onWorldLoading(Minecraft minecraft) {
        return minecraft.isIntegratedServerRunning()
            && dimensions.contains(minecraft.thePlayer.dimension + 1)
            && worldName.equals(minecraft.getIntegratedServer().getWorldName());
    }

    @Override
    public String toString() {
        return 0 + ":" + worldName + ":" + super.toString();
    }
}