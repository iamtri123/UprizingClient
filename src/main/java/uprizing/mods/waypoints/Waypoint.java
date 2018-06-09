package uprizing.mods.waypoints;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustrum;

@Getter @Setter
public abstract class Waypoint {

    private String name;

    private int index;

    private double x, y, z;

    private float red, green, blue;

    protected final WaypointDimensions dimensions = new WaypointDimensions();

    private boolean enabled = true;

    Waypoint(final String name, final double x, final double y, final double z, final float red, final float green, final float blue, final boolean enabled) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.enabled = enabled;
    }

    public abstract boolean onWorldLoading(Minecraft minecraft);

    public final int getUnified() { // TODO: storage ?
        return -16777216 + ((int) (red * 255.0F) << 16) + ((int) (green * 255.0F) << 8) + (int) (blue * 255.0F);
    }

    public boolean isInFrustum(Frustrum frustum) {
        return frustum.isBoxInFrustum(x, y, z, x, y, z);
    }

    public boolean isInDimension(int index) {
        return dimensions.contains(index);
    }

    void initDimensions(String string) {
        dimensions.init(string);
    }

    public void updateDimension(int index) {
        dimensions.update(index);
    }

    @Override
    public boolean equals(Object object) {
        return this == object;
    }

    @Override
    public String toString() { // name:dimensions:x:y:z:red:green:blue:enabled
        return name + ":" + dimensions + ":" + x + ":" + y + ":" + z + ":" + red + ":" + blue + ":" + green + ":" + (enabled ? 1 : 0);
    }
}