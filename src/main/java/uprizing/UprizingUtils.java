package uprizing;

import net.minecraft.client.settings.KeyBinding;
import sun.misc.FloatingDecimal;
import uprizing.settings.SettingType;

import java.text.DecimalFormat;

public class UprizingUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Deprecated
    public static double round(double value) {
        return Double.parseDouble(decimalFormat.format(value).replace(",", ".")); // TODO: Méthode blédarde à changer
    }

    public static String toString(double d) {
		final int k = (int) d;
		final int v = (int) ((d - k) * 100);
		return k + "," + v;
    }

    public static boolean isDbl(String string) {
        try {
			FloatingDecimal.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    public static KeyBinding keyBinding(String description, int keyCode, String category) {
        return new KeyBinding(description, keyCode, category);
    }

    public static SettingType[] types(int length, String... names) {
    	final SettingType[] types = new SettingType[length];
    	for (int index = 0; index < length; index++)
    		types[index] = new SettingType(names[index]);
    	return types;
	}
}