package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public interface ISound {

    ResourceLocation getSoundLocation();

    boolean canRepeat();

    int getRepeatDelay();

    float getVolume();

    float getPitch();

    float getXPosF();

    float getYPosF();

    float getZPosF();

    ISound.AttenuationType getAttenuationType();

    enum AttenuationType {

        NONE(0),
        LINEAR(2);

        private final int field_148589_c;

        AttenuationType(int p_i45110_3_) {
            this.field_148589_c = p_i45110_3_;
        }

        public int getTypeInt() {
            return this.field_148589_c;
        }
    }
}