package uprizing.gui;

import lombok.Getter;
import net.minecraft.client.gui.GuiButton;
import uprizing.Options;

public class GuiOptionButton extends GuiButton {

    @Getter
    private final Options option;

    public GuiOptionButton(int p_i45011_1_, int p_i45011_2_, int p_i45011_3_, String p_i45011_4_) {
        this(p_i45011_1_, p_i45011_2_, p_i45011_3_, null, p_i45011_4_);
    }

    public GuiOptionButton(int p_i45013_1_, int p_i45013_2_, int p_i45013_3_, Options p_i45013_4_, String p_i45013_5_) {
        super(p_i45013_1_, p_i45013_2_, p_i45013_3_, 150, 20, p_i45013_5_);
        this.option = p_i45013_4_;
    }
}