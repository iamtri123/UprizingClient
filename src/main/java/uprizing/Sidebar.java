package uprizing;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import uprizing.option.Options;

import java.util.Collection;
import java.util.Iterator;

public class Sidebar extends Gui {

	private final transient Uprizing uprizing;

	public Sidebar(final Uprizing uprizing) {
		this.uprizing = uprizing;
	}

	public final void draw(ScoreObjective objective, int scaledHeight, int scaledWidth, FontRenderer fontRenderer) {
		Scoreboard scoreboard = objective.getScoreboard();
		Collection<Score> scores = scoreboard.func_96534_i(objective);

		if (scores.size() <= 15) {
			int stringWidth = fontRenderer.getStringWidth(objective.getDisplayName());
			String playerName;

			final boolean scoreboardScores = uprizing.getBoolean(Options.SCOREBOARD_SCORES);

			for (Iterator<Score> iterator = scores.iterator(); iterator.hasNext(); stringWidth = Math.max(stringWidth, fontRenderer.getStringWidth(playerName))) {
				Score score = iterator.next();
				ScorePlayerTeam playerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
				playerName = ScorePlayerTeam.formatPlayerName(playerTeam, score.getPlayerName() + (scoreboardScores ? ": " + EnumChatFormatting.RED + score.getScorePoints() : ""));
			}

			int var22 = scores.size() * fontRenderer.FONT_HEIGHT;
			int var23 = scaledHeight / 2 + var22 / 3;
			byte var24 = 3;
			int var25 = scaledWidth - stringWidth - var24;
			int count = 0;
			Iterator<Score> iterator = scores.iterator();

			final boolean flag = uprizing.getBoolean(Options.SCOREBOARD_TEXT_SHADOW);

			while (iterator.hasNext()) {
				Score score = iterator.next();
				++count;
				ScorePlayerTeam playerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
				String var16 = ScorePlayerTeam.formatPlayerName(playerTeam, score.getPlayerName());
				String var17 = EnumChatFormatting.RED + "" + score.getScorePoints();
				int var19 = var23 - count * fontRenderer.FONT_HEIGHT;
				int var20 = scaledWidth - var24 + 2;
				drawRect(var25 - 2, var19, var20, var19 + fontRenderer.FONT_HEIGHT, 1342177280);
				drawString(fontRenderer, var16, var25, var19, flag);

				if (scoreboardScores) {
					fontRenderer.drawString(var17, var20 - fontRenderer.getStringWidth(var17), var19, 553648127);
				}

				if (count == scores.size()) {
					String var21 = objective.getDisplayName();
					drawRect(var25 - 2, var19 - fontRenderer.FONT_HEIGHT - 1, var20, var19 - 1, 1610612736);
					drawRect(var25 - 2, var19 - 1, var20, var19, 1342177280);
					drawString(fontRenderer, var21, var25 + stringWidth / 2 - fontRenderer.getStringWidth(var21) / 2, var19 - fontRenderer.FONT_HEIGHT, flag);
				}
			}
		}
	}

	private void drawString(FontRenderer renderer, String text, int x, int z, boolean flag) {
		if (flag) {
			renderer.drawStringWithShadow(text, x, z, 553648127);
		} else {
			renderer.drawString(text, x, z, 553648127);
		}
	}
}