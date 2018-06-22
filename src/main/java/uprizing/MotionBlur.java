package uprizing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public class MotionBlur {

	private static final String KEY = "uprizing";
	private static final int MAXIMUM = 11;
	private static final Minecraft minecraft = Minecraft.getMinecraft();
	public int index;

	public final void load(int index) {
		this.index = index;
	}

	public boolean reload() {
		final boolean active = index != 0;
		if (active) registerAndUpdateShaderGroup(minecraft.entityRenderer);
		return active;
	}

	public final void reset() {
		if (index != 0) {
			index = 0;
		}
	}

	private void registerAndUpdateShaderGroup(EntityRenderer entityRenderer) {
		if (!minecraft.getResourceManager().domainResourceManagers.containsKey(KEY)) {
			minecraft.getResourceManager().domainResourceManagers.put(KEY, new ResourceManager());
		}

		try {
			entityRenderer.theShaderGroup = new ShaderGroup(minecraft.getTextureManager(), minecraft.getResourceManager(), minecraft.getFramebuffer(), new ResourceLocation(KEY, "motion_blur_" + index + ".json"));
			entityRenderer.theShaderGroup.createBindFramebuffers(minecraft.displayWidth, minecraft.displayHeight);
		} catch (JsonException exception) {
			exception.printStackTrace();
		}
	}

	public final void increment() {
		index = index == MAXIMUM ? 0 : index + 1;

		if (minecraft.entityRenderer.shaderIndex != EntityRenderer.shaderCount) { // reset super secret settings
			minecraft.entityRenderer.shaderIndex = EntityRenderer.shaderCount;
		}
	}

	public final void decrement() {
		index = index == 0 ? MAXIMUM : index - 1;

		if (minecraft.entityRenderer.shaderIndex != EntityRenderer.shaderCount) { // reset super secret settings
			minecraft.entityRenderer.shaderIndex = EntityRenderer.shaderCount;
		}
	}

	public void handleGuiClose() {
		final EntityRenderer entityRenderer = minecraft.entityRenderer;

		if (entityRenderer.theShaderGroup != null) { // reset the current motion blur effect
			entityRenderer.theShaderGroup.deleteShaderGroup();
		}

		if (index != 0) {
			registerAndUpdateShaderGroup(entityRenderer);
		} else {
			entityRenderer.theShaderGroup = null; // remove on debug info (f3)
		}
	}

	private class ResourceManager implements IResourceManager {

		@Override
		public Set getResourceDomains() {
			return null;
		}

		@Override
		public IResource getResource(ResourceLocation p_110536_1_) {
			return new Resource();
		}

		@Override
		public List getAllResources(ResourceLocation p_135056_1_) {
			return null;
		}
	}

	private class Resource implements IResource {

		@Override
		public InputStream getInputStream() {
			final float value = (float) ((double) index) / 10;
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