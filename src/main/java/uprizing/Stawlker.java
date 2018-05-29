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

    public static boolean isDbl(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static KeyBinding keyBinding(String name, int keyId, String group) {
        return new KeyBinding(name, keyId, group);
    }
}