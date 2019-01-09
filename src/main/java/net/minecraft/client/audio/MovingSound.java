package net.minecraft.client.audio;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;

public abstract class MovingSound extends PositionedSound implements ITickableSound {

    @Getter protected boolean donePlaying = false;

    protected MovingSound(ResourceLocation p_i45104_1_) {
        super(p_i45104_1_);
    }
}