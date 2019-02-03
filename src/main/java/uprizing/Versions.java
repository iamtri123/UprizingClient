package uprizing;

import lombok.Getter;

public class Versions {

    private static final Version ONE = new Version(1, "1.0.0");

    public static final Version CURRENT;

    static {
        CURRENT = ONE;
    }

    public static class Version {

        @Getter private final int id;
        private final String toString;

        public Version(final int id, String tag) {
            this.id = id;
            this.toString = "v" + tag;
        }

        @Override
        public final String toString() {
            return toString;
        }
    }
}