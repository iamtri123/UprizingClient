package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class SoundList {

    private final List<SoundEntry> field_148577_a = Lists.newArrayList();
    private boolean replaceExisting;
    private SoundCategory field_148576_c;

    public List<SoundEntry> getSoundList() {
        return this.field_148577_a;
    }

    public boolean canReplaceExisting() {
        return this.replaceExisting;
    }

    public void setReplaceExisting(boolean p_148572_1_) {
        this.replaceExisting = p_148572_1_;
    }

    public SoundCategory getSoundCategory() {
        return this.field_148576_c;
    }

    public void setSoundCategory(SoundCategory p_148571_1_) {
        this.field_148576_c = p_148571_1_;
    }

    public static class SoundEntry {

        @Getter @Setter private String name;
        @Getter @Setter private float volume = 1.0F;
        @Getter @Setter private float pitch = 1.0F;
        @Getter @Setter private int weight = 1;
        @Getter @Setter private SoundList.SoundEntry.Type type;
        @Getter @Setter private boolean streaming;

        public SoundEntry() {
            this.type = SoundList.SoundEntry.Type.FILE;
            this.streaming = false;
        }

        public enum Type {

            FILE("file"),
            SOUND_EVENT("event");

            private final String field_148583_c;

            Type(String p_i45109_3_) {
                this.field_148583_c = p_i45109_3_;
            }

            public static SoundList.SoundEntry.Type getType(String p_148580_0_) {
                for (Type var4 : values()) {
                    if (var4.field_148583_c.equals(p_148580_0_)) {
                        return var4;
                    }
                }

                return null;
            }
        }
    }
}