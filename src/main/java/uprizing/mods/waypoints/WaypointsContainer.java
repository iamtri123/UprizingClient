package uprizing.mods.waypoints;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.ICamera;
import uprizing.Dimensions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

@Getter
public class WaypointsContainer extends WaypointsArray {

    private final File file;
    private final WaypointsRenderer renderer = new WaypointsRenderer();
    private boolean updating = false;

    public WaypointsContainer(final File mainDir) {
        this.file = new File(mainDir, "waypoints.txt");
    }

    public void renderWaypoints(ICamera frustrum) {
        if (updating) return;

        renderer.render(frustrum);
    }

    public void handleWorldLoading(Minecraft minecraft) {
        updating = true;

        renderer.clear();

        for (int i = 0; i < size; i++) {
            final Waypoint waypoint = elements[i];
            if (waypoint.onWorldLoading(minecraft)) {
                renderer.add(waypoint);
                waypoint.setIndex(i);
            }
        }

        System.out.println("[WaypointsMod] Loaded " + renderer.size + " waypoint" + (renderer.size <= 1 ? "" : "s") + " for the "
            + Dimensions.getByValue(minecraft.thePlayer.dimension).getName() + " dimension on the "
            + (minecraft.isIntegratedServerRunning() ? minecraft.getIntegratedServer().getWorldName() + " world." : minecraft.func_147104_D().serverIP + " server."));

        updating = false;
    }

    public void addWaypoint(Waypoint waypoint) {
        super.add(waypoint);
        renderer.add(waypoint);
    }

    public void deleteWaypoint(Waypoint waypoint) {
        super.remove(waypoint);
        renderer.remove(waypoint);
        saveWaypoints();
    }

    protected void loadWaypoints() {
        try {
            if (!file.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            while ((line = reader.readLine()) != null) {
                try {
                    final String[] parts = line.split(":");

                    final String name = parts[2];
                    final double x = Double.parseDouble(parts[4]);
                    final double y = Double.parseDouble(parts[5]);
                    final double z = Double.parseDouble(parts[6]);
                    final float red = Float.parseFloat(parts[7]);
                    final float green = Float.parseFloat(parts[8]);
                    final float blue = Float.parseFloat(parts[9]);
                    final boolean enabled = parts[10].equals("1");

                    final Waypoint waypoint;

                    if (parts[0].equals("1")) {
                        final String serverHostAddress = parts[1];
                        waypoint = new MultiplayerWaypoint(serverHostAddress, name, x, y, z, red, green, blue, enabled);
                    } else {
                        final String worldName = parts[1];
                        waypoint = new SingleplayerWaypoint(worldName, name, x, y, z, red, green, blue, enabled);
                    }

                    waypoint.initDimensions(parts[3]);
                    add(waypoint);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            reader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void saveWaypoints() {
        try {
            final PrintWriter writer = new PrintWriter(new FileWriter(file));
            int index = 0;
            while (index != size())
                writer.println(elements[index++]);
            writer.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}