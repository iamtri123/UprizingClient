package net.minecraft.client.audio;

import java.util.Random;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MusicTicker implements IUpdatePlayerListBox {

    private final Random rand = new Random();
    private final Minecraft mc;
    private ISound currentMusic;
    private int timeUntilNextMusic = 100;

    public MusicTicker(Minecraft p_i45112_1_) {
        this.mc = p_i45112_1_;
    }

    /**
     * Updates the JList with a new model.
     */
    public void update() {
        MusicTicker.MusicType var1 = this.mc.getAmbientMusicType();

        if (this.currentMusic != null) {
            if (!var1.getMusicTickerLocation().equals(this.currentMusic.getSoundLocation())) {
                this.mc.getSoundHandler().stopSound(this.currentMusic);
                this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, var1.getMinDelay() / 2);
            }

            if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic)) {
                this.currentMusic = null;
                this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, var1.getMinDelay(), var1.getMaxDelay()), this.timeUntilNextMusic);
            }
        }

        if (this.currentMusic == null && this.timeUntilNextMusic-- <= 0) {
            this.currentMusic = PositionedSoundRecord.createPositionedSoundRecord(var1.getMusicTickerLocation());
            this.mc.getSoundHandler().playSound(this.currentMusic);
            this.timeUntilNextMusic = Integer.MAX_VALUE;
        }
    }

    public enum MusicType {

        MENU(new ResourceLocation("minecraft:music.menu"), 20, 600),
        GAME(new ResourceLocation("minecraft:music.game"), 12000, 24000),
        CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600),
        CREDITS(new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE),
        NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200, 3600),
        END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0),
        END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);

        @Getter private final ResourceLocation musicTickerLocation;
        @Getter private final int minDelay;
        @Getter private final int maxDelay;

        MusicType(ResourceLocation p_i45111_3_, int p_i45111_4_, int p_i45111_5_) {
            this.musicTickerLocation = p_i45111_3_;
            this.minDelay = p_i45111_4_;
            this.maxDelay = p_i45111_5_;
        }
    }
}