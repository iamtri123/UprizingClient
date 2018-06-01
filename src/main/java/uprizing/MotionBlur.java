package uprizing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MotionBlur { // TODO: in the main gui, apply shader when the gui will be closed

    private static final String KEY = "uprizing";
    private int value;

    final int get() {
        return value;
    }

    final void set(int value) {
        this.value = value;
    }

    String getSuffix() {
        return value == 0 ? "OFF" : "" + value;
    }

    void next() {
        value = value == 9 ? 0 : value + 1;

        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.entityRenderer.theShaderGroup != null) {
            minecraft.entityRenderer.theShaderGroup.deleteShaderGroup();
        }

        registerShader(minecraft);

        if (value != 0) {
            try {
                minecraft.entityRenderer.theShaderGroup = new ShaderGroup(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getFramebuffer(), new ResourceLocation(KEY, "motion_blur_" + value + ".json"));
                minecraft.entityRenderer.theShaderGroup.createBindFramebuffers(minecraft.displayWidth, minecraft.displayHeight);
            } catch (JsonException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void registerShader(Minecraft minecraft) {
        final Map domainResourceManagers = ((SimpleReloadableResourceManager) minecraft.getResourceManager()).domainResourceManagers;
        if (!domainResourceManagers.containsKey(KEY)) {
            domainResourceManagers.put(KEY, new ResourceManager());
        }
    }

    private class ResourceManager implements IResourceManager {

        @Override
        public Set getResourceDomains() {
            return null;
        }

        @Override
        public IResource getResource(ResourceLocation p_110536_1_) {
            return new Ressource();
        }

        @Override
        public List getAllResources(ResourceLocation p_135056_1_) {
            return null;
        }
    }

    private class Ressource implements IResource {

        @Override
        public InputStream getInputStream() {
            final float value = (float) ((double) MotionBlur.this.value) / 10;
            return IOUtils.toInputStream("{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":" +
                "\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":" +
                "\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[" + value + ", " + value + ", " + value +
                "]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\"," +
                "\"outtarget\":\"minecraft:main\"}]}");
        }

        @Override
        public boolean hasMetadata() {
            return false;
        }

        @Override
        public IMetadataSection getMetadata(String p_110526_1_) {
            return null;
        }
    }
}