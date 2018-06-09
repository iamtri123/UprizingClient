package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback
{
    private int field_146347_a;
    private final boolean field_146346_f = false;
    private static final String __OBFID = "CL_00000690";

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.buttonList.clear();

        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
        {
            if (this.mc.isIntegratedServerRunning())
            {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld")));
            }
            else
            {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer")));
            }
        }
        else
        {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));

            if (this.mc.getSession() == null)
            {
                ((GuiButton)this.buttonList.get(1)).enabled = false;
            }
        }

        GuiButton var2;

        for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = false)
        {
            var2 = (GuiButton)var1.next();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {}

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        switch (p_146284_1_.id)
        {
            case 0:
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen)null);
                break;

            case 1:
                GuiYesNo var2 = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
                this.mc.displayGuiScreen(var2);
                var2.func_146350_a(20);
        }
    }

    public void confirmClicked(boolean p_73878_1_, int p_73878_2_)
    {
        if (p_73878_1_)
        {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
        else
        {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        boolean var4 = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        String var5 = var4 ? I18n.format("deathScreen.title.hardcore") : I18n.format("deathScreen.title");
        this.drawCenteredString(this.fontRendererObj, var5, this.width / 2 / 2, 30, 16777215);
        GL11.glPopMatrix();

        if (var4)
        {
            this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo"), this.width / 2, 144, 16777215);
        }

        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score") + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_146347_a;
        GuiButton var2;

        if (this.field_146347_a == 20)
        {
            for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = true)
            {
                var2 = (GuiButton)var1.next();
            }
        }
    }
}
