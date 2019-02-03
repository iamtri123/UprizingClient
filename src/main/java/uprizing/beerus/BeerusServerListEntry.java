package uprizing.beerus;

import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import uprizing.util.Constants;

public class BeerusServerListEntry extends ServerListEntryNormal {

    public static final int squareColor = new Color(255, 215, 0, 255).getRGB();
    private static final int squareBorder = 36;
    private static final int xDrop = 38 + 15;
    private static final int yDrop = 2;

    private final BeerusServer beerusServer;

    public BeerusServerListEntry(GuiMultiplayer guiMultiplayer, ServerData serverData, BeerusServer beerusServer) {
        super(guiMultiplayer, serverData);

        this.beerusServer = beerusServer;
    }

    protected int getProtocol() {
        return Constants.ALGERIAN_PROTOCOL;
    }

    public boolean isMovable() {
        return false;
    }

    public void drawEntry(int p_148279_1_, int x, int y, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_) {
        super.drawEntry(p_148279_1_, x, y, p_148279_4_, p_148279_5_, p_148279_6_, p_148279_7_, p_148279_8_, p_148279_9_);

        Gui.drawRect(x - xDrop, y - yDrop, x - xDrop + squareBorder, y - yDrop + squareBorder, squareColor);
    }
}