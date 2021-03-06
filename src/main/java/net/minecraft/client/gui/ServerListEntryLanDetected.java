package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

public class ServerListEntryLanDetected implements GuiListExtended.IGuiListEntry
{
    private final GuiMultiplayer field_148292_c;
    protected final Minecraft field_148293_a;
    protected final LanServerDetector.LanServer field_148291_b;
    private long field_148290_d = 0L;

    protected ServerListEntryLanDetected(GuiMultiplayer p_i45046_1_, LanServerDetector.LanServer p_i45046_2_)
    {
        this.field_148292_c = p_i45046_1_;
        this.field_148291_b = p_i45046_2_;
        this.field_148293_a = Minecraft.getInstance();
    }

    public boolean isMovable() {
        return true;
    }

    public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
    {
        this.field_148293_a.fontRenderer.drawString(I18n.format("lanServer.title"), x + 32 + 3, y + 1, 16777215);
        this.field_148293_a.fontRenderer.drawString(this.field_148291_b.getServerMotd(), x + 32 + 3, y + 12, 8421504);

        if (this.field_148293_a.gameSettings.hideServerAddress)
        {
            this.field_148293_a.fontRenderer.drawString(I18n.format("selectServer.hiddenAddress"), x + 32 + 3, y + 12 + 11, 3158064);
        }
        else
        {
            this.field_148293_a.fontRenderer.drawString(this.field_148291_b.getServerIpPort(), x + 32 + 3, y + 12 + 11, 3158064);
        }
    }

    public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
    {
        this.field_148292_c.selectServer(p_148278_1_);

        if (Minecraft.getSystemTime() - this.field_148290_d < 250L)
        {
            this.field_148292_c.connectToSelected();
        }

        this.field_148290_d = Minecraft.getSystemTime();
        return false;
    }

    public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}

    public LanServerDetector.LanServer getLanServer()
    {
        return this.field_148291_b;
    }
}