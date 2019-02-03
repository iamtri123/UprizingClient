package uprizing;

import java.text.DecimalFormat;
import sun.misc.FloatingDecimal;

public class UprizingUtils {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Deprecated
    public static double round(double value) {
        return Double.parseDouble(decimalFormat.format(value).replace(",", ".")); // TODO: Méthode blédarde à changer
    }

    public static boolean isDbl(String string) {
        try {
			FloatingDecimal.parseDouble(string);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }
}