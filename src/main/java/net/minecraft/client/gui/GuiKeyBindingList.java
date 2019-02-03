package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import uprizing.keybinding.KeyBinding;
import uprizing.keybinding.KeyBindings;

public class GuiKeyBindingList extends GuiListExtended {

    private final GuiControls guiControls;
    private final Minecraft minecraft;
    private final GuiListExtended.IGuiListEntry[] listEntries;
    private int maxListLabelWidth = 0;

    public GuiKeyBindingList(GuiControls guiControls, Minecraft minecraft) {
        super(minecraft, guiControls.width, guiControls.height, 63, guiControls.height - 32, 20);

        this.guiControls = guiControls;
        this.minecraft = minecraft;

        final KeyBindings keyBindings = minecraft.keyBindings;
        listEntries = new GuiListExtended.IGuiListEntry[keyBindings.size() + KeyBindings.Categories.SIZE];

        String current = null;
        int cursor = 0;

        for (int index = 0; index < keyBindings.size(); ++index) {
            final KeyBinding keyBinding = keyBindings.getByIndex(index);
            final String categoryName = keyBinding.getCategoryName();

            if (!categoryName.equals(current)) {
                listEntries[cursor++] = new GuiKeyBindingList.CategoryEntry(categoryName);
                current = categoryName;
            }

            final int stringWidth = minecraft.fontRenderer.getStringWidth(I18n.format(keyBinding.getName()));

            if (stringWidth > maxListLabelWidth) {
                maxListLabelWidth = stringWidth;
            }

            listEntries[cursor++] = new GuiKeyBindingList.KeyEntry(keyBinding, null);
        }
    }

    protected int getSize() {
        return listEntries.length;
    }

    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_) {
        return listEntries[p_148180_1_];
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 15;
    }

    public int getListWidth() {
        return super.getListWidth() + 32;
    }

    public class CategoryEntry implements GuiListExtended.IGuiListEntry {

        private final String labelText;
        private final int labelWidth;

        public CategoryEntry(String p_i45028_2_) {
            this.labelText = I18n.format(p_i45028_2_);
            this.labelWidth = GuiKeyBindingList.this.minecraft.fontRenderer.getStringWidth(this.labelText);
        }

        public boolean isMovable() {
            return true;
        }

        public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
            GuiKeyBindingList.this.minecraft.fontRenderer.drawString(this.labelText, GuiKeyBindingList.this.minecraft.currentScreen.width / 2 - labelWidth / 2, y + p_148279_5_ - GuiKeyBindingList.this.minecraft.fontRenderer.FONT_HEIGHT - 1, 16777215);
        }

        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            return false;
        }

        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
    }

    public class KeyEntry implements GuiListExtended.IGuiListEntry {

        private final KeyBinding keyBinding;
        private final String keyBindingName;
        private final GuiButton btnChangeKeyBinding;
        private final GuiButton btnReset;

        private KeyEntry(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
            this.keyBindingName = I18n.format(keyBinding.getName());
            this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18, I18n.format(keyBinding.getName()));
            this.btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset"));
        }

        public boolean isMovable() {
            return true;
        }

        public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
            boolean var10 = GuiKeyBindingList.this.guiControls.buttonId == this.keyBinding;
            GuiKeyBindingList.this.minecraft.fontRenderer.drawString(this.keyBindingName, x + 90 - GuiKeyBindingList.this.maxListLabelWidth, y + p_148279_5_ / 2 - GuiKeyBindingList.this.minecraft.fontRenderer.FONT_HEIGHT / 2, 16777215);
            this.btnReset.xPosition = x + 190;
            this.btnReset.yPosition = y;
            this.btnReset.enabled = this.keyBinding.getKeyCode() != this.keyBinding.getDefaultKeyCode();
            this.btnReset.drawButton(GuiKeyBindingList.this.minecraft, p_148279_7_, p_148279_8_);
            this.btnChangeKeyBinding.xPosition = x + 105;
            this.btnChangeKeyBinding.yPosition = y;
            this.btnChangeKeyBinding.displayString = GameSettings.getKeyDisplayString(this.keyBinding.getKeyCode());
            boolean var11 = false;

            if (keyBinding.getKeyCode() != 0) {
                KeyBindings keyBindings = GuiKeyBindingList.this.minecraft.keyBindings;

                for (int index = 0; index < keyBindings.size(); ++index) {
                    KeyBinding keyBinding = keyBindings.getByIndex(index);

                    if (keyBinding != this.keyBinding && keyBinding.getKeyCode() == this.keyBinding.getKeyCode()) {
                        var11 = true;
                        break;
                    }
                }
            }

            if (var10) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
            } else if (var11) {
                this.btnChangeKeyBinding.displayString = EnumChatFormatting.RED + this.btnChangeKeyBinding.displayString;
            }

            this.btnChangeKeyBinding.drawButton(GuiKeyBindingList.this.minecraft, p_148279_7_, p_148279_8_);
        }

        public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_) {
            if (this.btnChangeKeyBinding.mousePressed(GuiKeyBindingList.this.minecraft, p_148278_2_, p_148278_3_)) {
                GuiKeyBindingList.this.guiControls.buttonId = this.keyBinding;
                return true;
            } else if (this.btnReset.mousePressed(GuiKeyBindingList.this.minecraft, p_148278_2_, p_148278_3_)) {
                GuiKeyBindingList.this.minecraft.keyBindings.setKeyCodeSave(this.keyBinding, this.keyBinding.getDefaultKeyCode());
                minecraft.keyBindings.saveToFile();
                minecraft.keyBindings.resetKeyBindingArrayAndHash();
                return true;
            } else {
                return false;
            }
        }

        public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {
            this.btnChangeKeyBinding.mouseReleased(p_148277_2_, p_148277_3_);
            this.btnReset.mouseReleased(p_148277_2_, p_148277_3_);
        }

        KeyEntry(KeyBinding p_i45030_2_, Object p_i45030_3_) {
            this(p_i45030_2_);
        }
    }
}