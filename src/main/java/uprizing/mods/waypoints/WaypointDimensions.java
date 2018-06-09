package uprizing.mods.waypoints;

import lombok.Getter;

@Getter
public class WaypointDimensions {

    private final boolean elements[] = { false, true, false };

    void init(String string) {
        final String[] parts = string.split(",");
        elements[0] = parts[0].equals("1");
        elements[1] = parts[1].equals("1");
        elements[2] = parts[2].equals("1");
    }

    boolean contains(int index) {
        return elements[index];
    }

    void update(int index) {
        elements[index] = !elements[index];
    }

    @Override
    public String toString() {
        return (elements[0] ? "1" : "0") + "," + (elements[1] ? "1" : "0") + "," + (elements[2] ? "1" : "0");
    }
}