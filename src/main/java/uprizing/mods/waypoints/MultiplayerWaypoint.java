package uprizing.mods.waypoints;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;

@Getter @Setter
public class MultiplayerWaypoint extends Waypoint {

    private String serverHostAddress;

    public MultiplayerWaypoint(final String serverHostAddress, final String name, final double x, final double y, final double z, final float red, final float green, final float blue, final boolean enabled) {
        super(name, x, y, z, red, green, blue, enabled);
        this.serverHostAddress = serverHostAddress;
    }

    @Override
    public boolean onWorldLoading(Minecraft minecraft) {
        return !minecraft.isIntegratedServerRunning()
            && dimensions.contains(minecraft.thePlayer.dimension + 1)
            && serverHostAddress.equals(minecraft.sexy().getServerHostAddress());
    }

    @Override
    public String toString() {
        return 1 + ":" + serverHostAddress + ":" + super.toString();
    }
}