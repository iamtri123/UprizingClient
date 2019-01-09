package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler implements IResourceManagerReloadListener, IUpdatePlayerListBox {

    private static final Logger logger = LogManager.getLogger();
    private static final Gson field_147699_c = new GsonBuilder().registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
    static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0D, 0.0D, false);
    private static final ParameterizedType field_147696_d = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[] { String.class, SoundList.class };
        }

        public Type getRawType() {
            return Map.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };

    private final SoundRegistry sndRegistry = new SoundRegistry();
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;

    public SoundHandler(IResourceManager p_i45122_1_, GameSettings p_i45122_2_) {
        this.mcResourceManager = p_i45122_1_;
        this.sndManager = new SoundManager(this, p_i45122_2_);
    }

    public void onResourceManagerReload(IResourceManager p_110549_1_) {
        this.sndManager.reloadSoundSystem();
        this.sndRegistry.clearMap();

        for (String var3 : p_110549_1_.getResourceDomains()) {
            try {
                List<IResource> var4 = p_110549_1_.getAllResources(new ResourceLocation(var3, "sounds.json"));

                for (IResource var6 : var4) {
                    try {
                        Map<String, SoundList> var7 = field_147699_c.fromJson(new InputStreamReader(var6.getInputStream()), field_147696_d);

                        for (Entry<String, SoundList> var9 : var7.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(var3, var9.getKey()), var9.getValue());
                        }
                    } catch (RuntimeException var10) {
                        logger.warn("Invalid sounds.json", var10);
                    }
                }
            } catch (IOException ignored) {}
        }
    }

    private void loadSoundResource(ResourceLocation p_147693_1_, SoundList p_147693_2_) {
        SoundEventAccessorComposite var3;

        if (this.sndRegistry.containsKey(p_147693_1_) && !p_147693_2_.canReplaceExisting()) {
            var3 = (SoundEventAccessorComposite) this.sndRegistry.getObject(p_147693_1_);
        } else {
            logger.debug("Registered/replaced new sound event location {}", p_147693_1_);
            var3 = new SoundEventAccessorComposite(p_147693_1_, 1.0D, 1.0D, p_147693_2_.getSoundCategory());
            this.sndRegistry.registerSound(var3);
        }

        for (final SoundList.SoundEntry soundEntry : p_147693_2_.getSoundList()) {
            String var6 = soundEntry.getName();
            ResourceLocation var7 = new ResourceLocation(var6);
            final String var8 = var6.contains(":") ? var7.getResourceDomain() : p_147693_1_.getResourceDomain();
            ISoundEventAccessor var9;

            switch (SwitchType.field_148765_a[soundEntry.getType().ordinal()]) {
                case 1:
                    ResourceLocation var10 = new ResourceLocation(var8, "sounds/" + var7.getResourcePath() + ".ogg");

                    try {
                        this.mcResourceManager.getResource(var10);
                    } catch (FileNotFoundException var12) {
                        logger.warn("File {} does not exist, cannot add it to event {}", var10, p_147693_1_);
                        continue;
                    } catch (IOException var13) {
                        logger.warn("Could not load sound file " + var10 + ", cannot add it to event " + p_147693_1_, var13);
                        continue;
                    }

                    var9 = new SoundEventAccessor(new SoundPoolEntry(var10, (double) soundEntry.getPitch(), (double) soundEntry.getVolume(), soundEntry.isStreaming()), soundEntry.getWeight());
                    break;

                case 2:
                    var9 = new ISoundEventAccessor() {
                        final ResourceLocation field_148726_a = new ResourceLocation(var8, soundEntry.getName());

                        public int func_148721_a() {
                            SoundEventAccessorComposite var1 = (SoundEventAccessorComposite) SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return var1 == null ? 0 : var1.func_148721_a();
                        }

                        public SoundPoolEntry func_148720_g() {
                            SoundEventAccessorComposite var1 = (SoundEventAccessorComposite) SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return var1 == null ? SoundHandler.missing_sound : var1.func_148720_g();
                        }
                    };

                    break;
                default:
                    throw new IllegalStateException("IN YOU FACE");
            }

            var3.addSoundToEventPool(var9);
        }
    }

    public SoundEventAccessorComposite getSound(ResourceLocation p_147680_1_) {
        return (SoundEventAccessorComposite) this.sndRegistry.getObject(p_147680_1_);
    }

    /**
     * Play a sound
     */
    public void playSound(ISound p_147682_1_) {
        this.sndManager.playSound(p_147682_1_);
    }

    /**
     * Plays the sound in n ticks
     */
    public void playDelayedSound(ISound p_147681_1_, int p_147681_2_) {
        this.sndManager.playDelayedSound(p_147681_1_, p_147681_2_);
    }

    public void setListener(EntityPlayer p_147691_1_, float p_147691_2_) {
        this.sndManager.setListener(p_147691_1_, p_147691_2_);
    }

    public void pauseSounds() {
        this.sndManager.pauseAllSounds();
    }

    public void stopSounds() {
        this.sndManager.stopAllSounds();
    }

    public void unloadSounds() {
        this.sndManager.unloadSoundSystem();
    }

    /**
     * Updates the JList with a new model.
     */
    public void update() {
        this.sndManager.updateAllSounds();
    }

    public void resumeSounds() {
        this.sndManager.resumeAllSounds();
    }

    public void setSoundLevel(SoundCategory p_147684_1_, float p_147684_2_) {
        if (p_147684_1_ == SoundCategory.MASTER && p_147684_2_ <= 0.0F) {
            this.stopSounds();
        }

        this.sndManager.setSoundCategoryVolume(p_147684_1_, p_147684_2_);
    }

    public void stopSound(ISound p_147683_1_) {
        this.sndManager.stopSound(p_147683_1_);
    }

    public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory... p_147686_1_) {
        ArrayList<SoundEventAccessorComposite> var2 = Lists.newArrayList();

        for (Object o : this.sndRegistry.getKeys()) {
            ResourceLocation var4 = (ResourceLocation) o;
            SoundEventAccessorComposite var5 = (SoundEventAccessorComposite) this.sndRegistry.getObject(var4);

            if (ArrayUtils.contains(p_147686_1_, var5.getSoundCategory())) {
                var2.add(var5);
            }
        }

        if (var2.isEmpty()) {
            return null;
        } else {
            return var2.get((new Random()).nextInt(var2.size()));
        }
    }

    public boolean isSoundPlaying(ISound p_147692_1_) {
        return this.sndManager.isSoundPlaying(p_147692_1_);
    }

    static final class SwitchType {

        static final int[] field_148765_a = new int[SoundList.SoundEntry.Type.values().length];

        static {
            try {
                field_148765_a[SoundList.SoundEntry.Type.FILE.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {}

            try {
                field_148765_a[SoundList.SoundEntry.Type.SOUND_EVENT.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {}
        }
    }
}