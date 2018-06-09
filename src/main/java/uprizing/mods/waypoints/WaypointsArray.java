package uprizing.mods.waypoints;

public class WaypointsArray {

    transient Waypoint[] elements = {};

    int size, cursor;

    public int size() {
        return size;
    }

    public Waypoint get(int index) {
        return elements[index];
    }

    public void add(Waypoint waypoint) {
        growCapacity(size + 1);
        elements[size++] = waypoint;
    }

    private void growCapacity(int minCapacity) {
        if (minCapacity - elements.length > 0) {
            final Waypoint[] result = new Waypoint[minCapacity];
            System.arraycopy(elements, 0, result, 0, elements.length);
            elements = result;
        }
    }

    public void remove(Waypoint waypoint) {
        for (int index = 0; index < size; index++)
            if (waypoint == elements[index]) {
                fastRemove(index);
            }
    }

    private void fastRemove(int index) {
        final int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elements, index+1, elements, index,
                             numMoved);
        elements[--size] = null; // clear to let GC do its work
    }

    void clear() {
        // clear to let GC do its work
        for (int i = 0; i < size; i++)
            elements[i] = null;

        size = 0;
    }
}