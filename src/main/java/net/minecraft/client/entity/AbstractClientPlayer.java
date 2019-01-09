package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;

public abstract class AbstractClientPlayer extends EntityPlayer implements SkinManager.SkinAvailableCallback {

    public static final ResourceLocation locationStevePng = new ResourceLocation("textures/entity/steve.png");

    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    @Setter private ResourceLocation locationOfCape = null;
    @Getter private String nameClear;

    public AbstractClientPlayer(World world, GameProfile gameProfile) {
        super(world, gameProfile);
        String var3 = this.getCommandSenderName();

        if (!var3.isEmpty()) {
            final SkinManager skinManager = Minecraft.getMinecraft().getSkinManager();
            skinManager.func_152790_a(gameProfile, this, true);
        }

        nameClear = gameProfile.getName();

        if (nameClear != null && !nameClear.isEmpty()) {
            nameClear = StringUtils.stripControlCodes(nameClear);
        }

        CapeUtils.downloadCape(this);
        PlayerConfigurations.getPlayerConfiguration(this);
    }

    public boolean hasCape() {
        return Config.isShowCapes() && (this.locationOfCape != null || this.locationCape != null);
    }

    public boolean hasSkin() {
        return this.locationSkin != null;
    }

    public ResourceLocation getLocationSkin() {
        return this.locationSkin == null ? locationStevePng : this.locationSkin;
    }

    public ResourceLocation getLocationCape() {
        return !Config.isShowCapes() ? null : (this.locationOfCape != null ? this.locationOfCape : this.locationCape);
    }

    public void onSkinAvailable(Type skinPart, ResourceLocation skinLoc) {
        switch (AbstractClientPlayer.SwitchType.SKIN_PART_TYPES[skinPart.ordinal()]) {
            case 1:
                this.locationSkin = skinLoc;
                break;

            case 2:
                this.locationCape = skinLoc;
        }
    }

    static final class SwitchType {

        static final int[] SKIN_PART_TYPES = new int[Type.values().length];

        static {
            try {
                SKIN_PART_TYPES[Type.SKIN.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {}

            try {
                SKIN_PART_TYPES[Type.CAPE.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {}
        }
    }
}