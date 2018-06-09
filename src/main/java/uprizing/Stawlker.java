package uprizing;

import net.minecraft.client.settings.KeyBinding;

import java.text.DecimalFormat;

public class Stawlker {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Deprecated
    public static double round(double value) {
        return Double.parseDouble(decimalFormat.format(value).replace(",", ".")); // TODO: Méthode blédarde à changer
    }

    public static String toString(double dbl) { // TODO: optimize
        return String.format("%.1f", dbl);
    }

    public static String configKey(String name) {
		final StringBuilder builder = new StringBuilder();

		for (int index = 0; index < name.length(); index++) {
			final char c = name.charAt(index);

			if (index == 0) {
				builder.append(Character.toLowerCase(c));
			} else if (c != ' ') {
				builder.append(c);
			}
		}

		return builder.toString();
	}

    public static boolean isDbl(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static KeyBinding keyBinding(String description, int keyCode, String category) {
        return new KeyBinding(description, keyCode, category);
    }
}