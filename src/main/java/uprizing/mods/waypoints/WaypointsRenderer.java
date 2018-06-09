package uprizing.mods.waypoints;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import uprizing.Stawlker;

public class WaypointsRenderer extends WaypointsArray {

    private final RenderManager renderManager = RenderManager.instance;

    private final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

    private final Tessellator tessellator = Tessellator.instance;

    public void render(ICamera frustrum) {
        if (size == 0) return; // || minecraft.renderViewEntity.isPlayerSleeping()) {

        while (cursor != size) {
            final Waypoint waypoint = elements[cursor++];
            if (waypoint.isEnabled()) { // || !waypoint.isInFrustum(frustrum)) {
                render(waypoint);
            }
        }

        cursor = 0;
    }

    private void render(Waypoint waypoint) { // TODO: optimize
        GL11.glAlphaFunc(516, 0.1F);
        double distance = Math.sqrt(getDistance(waypoint, renderManager.livingPlayer));

        String displayName = waypoint.getName() + " (" + Stawlker.toString(distance) + "m)";
        double maxDistance = gameSettings.getOptionFloatValue(GameSettings.Options.RENDER_DISTANCE) * 16.0F * 0.75D;
        double adjustedDistance = distance;

        double offX = 0 - renderManager.renderPosX + waypoint.getX();
        double offY = 0 - renderManager.renderPosY + waypoint.getY();
        double offZ = 0 - renderManager.renderPosZ + waypoint.getZ();

        if (distance > maxDistance) {
            offX = offX / distance * maxDistance;
            offY = offY / distance * maxDistance;
            offZ = offZ / distance * maxDistance;
            adjustedDistance = maxDistance;
        }

        FontRenderer fontRenderer = renderManager.getFontRenderer();
        float var14 = ((float) adjustedDistance * 0.1F + 1.0F) * 0.0266F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) offX, (float) offY, (float) offZ);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-var14, -var14, var14);
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        byte var16 = 0;
        GL11.glDisable(3553);
        int displayNameWidth = fontRenderer.getStringWidth(displayName);
        int var17 = displayNameWidth / 2;

        GL11.glEnable(2929);
        if (distance < maxDistance) {
            GL11.glDepthMask(true);
        }

        GL11.glEnable(32823);
        GL11.glPolygonOffset(1.0F, 3.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(waypoint.getRed(), waypoint.getGreen(), waypoint.getBlue(), 0.6F);
        tessellator.addVertex(-var17 - 2, -2 + var16, 0.0D);
        tessellator.addVertex(-var17 - 2, 9 + var16, 0.0D);
        tessellator.addVertex(var17 + 2, 9 + var16, 0.0D);
        tessellator.addVertex(var17 + 2, -2 + var16, 0.0D);
        tessellator.draw();
        GL11.glPolygonOffset(1.0F, 1.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.15F);

        tessellator.addVertex(-var17 - 1, -1 + var16, 0.0D);
        tessellator.addVertex(-var17 - 1, 8 + var16, 0.0D);
        tessellator.addVertex(var17 + 1, 8 + var16, 0.0D);
        tessellator.addVertex(var17 + 1, -1 + var16, 0.0D);
        tessellator.draw();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glPolygonOffset(1.0F, 7.0F);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(waypoint.getRed(), waypoint.getGreen(), waypoint.getBlue(), 0.15F);

        tessellator.addVertex(-var17 - 2, -2 + var16, 0.0D);
        tessellator.addVertex(-var17 - 2, 9 + var16, 0.0D);
        tessellator.addVertex(var17 + 2, 9 + var16, 0.0D);
        tessellator.addVertex(var17 + 2, -2 + var16, 0.0D);
        tessellator.draw();
        GL11.glPolygonOffset(1.0F, 5.0F);

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.15F);
        tessellator.addVertex(-var17 - 1, -1 + var16, 0.0D);
        tessellator.addVertex(-var17 - 1, 8 + var16, 0.0D);
        tessellator.addVertex(var17 + 1, 8 + var16, 0.0D);
        tessellator.addVertex(var17 + 1, -1 + var16, 0.0D);
        tessellator.draw();

        GL11.glDisable(32823);
        GL11.glEnable(3553);
        fontRenderer.drawString(displayName, -displayNameWidth / 2, var16, -3355444);
        GL11.glEnable(2929);
        fontRenderer.drawString(displayName, -displayNameWidth / 2, var16, -1);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glEnable(2912);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private double getDistance(Waypoint waypoint, Entity entity) {
        final double x = waypoint.getX() - entity.posX, y = waypoint.getY() - entity.posY, z = waypoint.getZ() - entity.posZ;
        return x * x + y * y + z * z;
    }
}