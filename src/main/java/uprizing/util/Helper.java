package uprizing.util;

import java.awt.Color;
import net.minecraft.client.gui.Gui;

public class Helper {

    private static final int COLOR = new Color(255, 0, 0).getRGB();
    private static final int HORIZONTAL_LINES = 5 + 1;
    private static final int VERTICAL_LINES = 5 + 1;

    public static void drawRedLines(final int width, int height) {
        final int foo = height / HORIZONTAL_LINES;

        for (int index = 0; index != HORIZONTAL_LINES; index++) {
            drawHorizontalLine(0, width, foo * index, COLOR);
        }

        drawHorizontalLine(0, width, height - 1, COLOR);

        final int bar = width / VERTICAL_LINES;

        for (int index = 0; index != VERTICAL_LINES; index++) {
            drawVerticalLine(bar * index, 0, height, COLOR);
        }

        drawVerticalLine(width - 1, 0, height, COLOR);
    }

    private static void drawHorizontalLine(int startX, int endX, int y, int color) {
        Gui.drawRect(startX, y, endX + 1, y + 1, color);
    }

    private static void drawVerticalLine(int x, int startY, int endY, int color) {
        Gui.drawRect(x, startY + 1, x + 1, endY, color);
    }
}