package net.minecraft.client.audio;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;

public class SoundPoolEntry {

    @Getter private final ResourceLocation soundPoolEntryLocation;
    @Getter private final boolean streamingSound;
    @Getter @Setter private double pitch;
    @Getter @Setter private double volume;

    public SoundPoolEntry(ResourceLocation p_i45113_1_, double p_i45113_2_, double p_i45113_4_, boolean p_i45113_6_) {
        this.soundPoolEntryLocation = p_i45113_1_;
        this.pitch = p_i45113_2_;
        this.volume = p_i45113_4_;
        this.streamingSound = p_i45113_6_;
    }

    public SoundPoolEntry(SoundPoolEntry p_i45114_1_) {
        this.soundPoolEntryLocation = p_i45114_1_.soundPoolEntryLocation;
        this.pitch = p_i45114_1_.pitch;
        this.volume = p_i45114_1_.volume;
        this.streamingSound = p_i45114_1_.streamingSound;
    }
}