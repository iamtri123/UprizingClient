package uprizing;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ChatComponentText;

public class Stawlker {

    public static void printCurrentTeams(Minecraft minecraft) {
        for (Object object : minecraft.theWorld.getScoreboard().getTeams()) {
            final Team team = (Team) object;
            minecraft.thePlayer.addChatMessage(new ChatComponentText(team.sex()));
        }
    }
}