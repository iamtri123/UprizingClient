package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;

import java.io.File;

public abstract class AbstractClientPlayer extends EntityPlayer implements SkinManager.SkinAvailableCallback
{
    public static final ResourceLocation locationStevePng = new ResourceLocation("textures/entity/steve.png");
    private ResourceLocation locationSkin;
    private ResourceLocation locationCape;
    private static final String __OBFID = "CL_00000935";
    private ResourceLocation locationOfCape = null;
    private String nameClear = null;

    public AbstractClientPlayer(World p_i45074_1_, GameProfile p_i45074_2_)
    {
        super(p_i45074_1_, p_i45074_2_);
        String var3 = this.getCommandSenderName();

        if (!var3.isEmpty())
        {
            SkinManager var4 = Minecraft.getMinecraft().getSkinManager();
            var4.func_152790_a(p_i45074_2_, this, true);
        }

        this.nameClear = p_i45074_2_.getName();

        if (this.nameClear != null && !this.nameClear.isEmpty())
        {
            this.nameClear = StringUtils.stripControlCodes(this.nameClear);
        }

        CapeUtils.downloadCape(this);
        PlayerConfigurations.getPlayerConfiguration(this);
    }

    public boolean hasCape()
    {
        return Config.isShowCapes() && (this.locationOfCape != null || this.locationCape != null);
    }

    public boolean hasSkin()
    {
        return this.locationSkin != null;
    }

    public ResourceLocation getLocationSkin()
    {
        return this.locationSkin == null ? locationStevePng : this.locationSkin;
    }

    public ResourceLocation getLocationCape()
    {
        return !Config.isShowCapes() ? null : (this.locationOfCape != null ? this.locationOfCape : this.locationCape);
    }

    public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation par0ResourceLocation, String par1Str)
    {
        TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
        Object var3 = var2.getTexture(par0ResourceLocation);

        if (var3 == null)
        {
            var3 = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(par1Str)), locationStevePng, new ImageBufferDownload());
            var2.loadTexture(par0ResourceLocation, (ITextureObject)var3);
        }

        return (ThreadDownloadImageData)var3;
    }

    public static ResourceLocation getLocationSkin(String par0Str)
    {
        return new ResourceLocation("skins/" + StringUtils.stripControlCodes(par0Str));
    }

    public void onSkinAvailable(Type skinPart, ResourceLocation skinLoc)
    {
        switch (AbstractClientPlayer.SwitchType.SKIN_PART_TYPES[skinPart.ordinal()])
        {
            case 1:
                this.locationSkin = skinLoc;
                break;

            case 2:
                this.locationCape = skinLoc;
        }
    }

    public String getNameClear()
    {
        return this.nameClear;
    }

    public ResourceLocation getLocationOfCape()
    {
        return this.locationOfCape;
    }

    public void setLocationOfCape(ResourceLocation locationOfCape)
    {
        this.locationOfCape = locationOfCape;
    }

    static final class SwitchType
    {
        static final int[] SKIN_PART_TYPES = new int[Type.values().length];
        private static final String __OBFID = "CL_00001832";

        static
        {
            try
            {
                SKIN_PART_TYPES[Type.SKIN.ordinal()] = 1;
            }
            catch (NoSuchFieldError var2)
            {
            }

            try
            {
                SKIN_PART_TYPES[Type.CAPE.ordinal()] = 2;
            }
            catch (NoSuchFieldError var1)
            {
            }
        }
    }
}