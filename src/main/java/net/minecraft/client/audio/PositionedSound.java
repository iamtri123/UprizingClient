package net.minecraft.client.audio;

import lombok.Getter;
import net.minecraft.util.ResourceLocation;

public abstract class PositionedSound implements ISound {

    @Getter protected final ResourceLocation soundLocation;
    @Getter protected float volume = 1.0F;
    @Getter protected float pitch = 1.0F;
    @Getter protected float xPosF;
    @Getter protected float yPosF;
    @Getter protected float zPosF;
    protected boolean repeat = false;
    @Getter protected int repeatDelay = 0;
    @Getter protected ISound.AttenuationType attenuationType;

    protected PositionedSound(ResourceLocation soundResource) {
        this.attenuationType = ISound.AttenuationType.LINEAR;
        this.soundLocation = soundResource;
    }

    public boolean canRepeat() {
        return this.repeat;
    }
}