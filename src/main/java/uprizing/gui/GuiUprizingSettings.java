package uprizing.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.resources.I18n;
import uprizing.Options;
import uprizing.Uprizing;

public class GuiUprizingSettings extends GuiScreen {

    private final GuiScreen prevScreen;
    private final Uprizing uprizing;
    private static final String title = "Uprizing Settings";
    private static final Options[] options = { Options.SCOREBOARD_NUMBERS };
    private int lastMouseX = 0, lastMouseY = 0;
    private long mouseStillTime = 0L;

    public GuiUprizingSettings(final GuiScreen guiScreen, final Uprizing uprizing) {
        this.prevScreen = guiScreen;
        this.uprizing = uprizing;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        int i = 0;

        for (Options option : options) {
            int x = width / 2 - 155 + i % 2 * 160;
            int y = height / 6 + 21 * (i / 2) - 10;

            buttonList.add(new GuiOptionButton(option.ordinal(), x, y, option, uprizing.getKeyBinding(option)));

            ++i;
        }

        buttonList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168 + 11, I18n.format("gui.done")));
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
                Options option = ((GuiOptionButton) guibutton).getOption();
                uprizing.setOptionValue(option);
                guibutton.displayString = uprizing.getKeyBinding(option);
            }

            if (guibutton.id == 200) {
                mc.uprizing.saveOptions();
                mc.displayGuiScreen(prevScreen);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int x, int y, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, title, width / 2, 20, 16777215);
        super.drawScreen(x, y, f);

        if (Math.abs(x - lastMouseX) <= 5 && Math.abs(y - lastMouseY) <= 5) {
            short activateDelay = 700;

            if (System.currentTimeMillis() >= mouseStillTime + (long)activateDelay) {
                int x1 = width / 2 - 150;
                int y1 = height / 6 - 5;

                if (y <= y1 + 98) {
                    y1 += 105;
                }

                int x2 = x1 + 150 + 150;
                int y2 = y1 + 84 + 10;
                GuiButton button = getSelectedButton(x, y);

                if (button != null) {
                    String s = getButtonName(button.displayString);

                    String[] lines = getTooltipLines(s);

                    if (lines == null) {
                        return;
                    }

                    drawGradientRect(x1, y1, x2, y2, -536870912, -536870912);

                    for (int i = 0; i < lines.length; ++i) {
                        String line = lines[i];
                        fontRendererObj.drawStringWithShadow(line, x1 + 5, y1 + 5 + i * 11, 14540253);
                    }
                }
            }
        } else {
            lastMouseX = x;
            lastMouseY = y;
            mouseStillTime = System.currentTimeMillis();
        }
    }

    private String[] getTooltipLines(String btnName) {
        return btnName.equals("Scoreboard Numbers") ? new String[] { "Remove Scoreboard Numbers", "ON - Salope", "OFF - Pute"} : null;
    }

    private String getButtonName(String displayString) {
        final int pos = displayString.indexOf(58);
        return pos < 0 ? displayString : displayString.substring(0, pos);
    }

    private GuiButton getSelectedButton(int i, int j) {
        for (int k = 0; k < this.buttonList.size(); ++k) {
            final GuiButton button = (GuiButton) this.buttonList.get(k);
            int width = GuiVideoSettings.getButtonWidth(button);
            int height = GuiVideoSettings.getButtonHeight(button);

            if (i >= button.field_146128_h && j >= button.field_146129_i && i < button.field_146128_h + width && j < button.field_146129_i + height) {
                return button;
            }
        }

        return null;
    }
}