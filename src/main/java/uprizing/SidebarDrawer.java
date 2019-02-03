package uprizing;

import java.util.Collection;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import uprizing.setting.Settings;

public class SidebarDrawer extends Gui {

	private final Uprizing uprizing;

	public SidebarDrawer(final Uprizing uprizing) {
		this.uprizing = uprizing;
	}

	public final void draw(ScoreObjective objective, int scaledHeight, int scaledWidth, FontRenderer fontRenderer) {
		final Scoreboard scoreboard = objective.getScoreboard();
		final Collection<Score> scores = scoreboard.getSortedScores(objective);

		if (scores.size() <= 15) {
			int maxStringWidth = fontRenderer.getStringWidth(objective.getDisplayName());
			String string;

			final boolean scoreboardScores = uprizing.getBoolean(Settings.SCOREBOARD_SCORES);

			for (Iterator<Score> iterator = scores.iterator(); iterator.hasNext(); maxStringWidth = Math.max(maxStringWidth, fontRenderer.getStringWidth(string))) {
				final Score score = iterator.next();
				final ScorePlayerTeam playerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
				string = ScorePlayerTeam.formatPlayerName(playerTeam, score.getPlayerName() + (scoreboardScores ? ": " + EnumChatFormatting.RED + score.getScorePoints() : ""));
			}

			final int maxFontHeight = scores.size() * fontRenderer.FONT_HEIGHT;

			final int var23 = scaledHeight / 2 + maxFontHeight / 3;
			final byte var24 = 3;
			final int var25 = scaledWidth - maxStringWidth - var24;
			int count = 0;
			final Iterator<Score> iterator = scores.iterator();

			final boolean scoreboardTextShadow = uprizing.getBoolean(Settings.SCOREBOARD_TEXT_SHADOW);

			while (iterator.hasNext()) {
				final Score score = iterator.next();

				++count;

				final ScorePlayerTeam playerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
				final String var16 = ScorePlayerTeam.formatPlayerName(playerTeam, score.getPlayerName());

				final int var19 = var23 - count * fontRenderer.FONT_HEIGHT;
				final int var20 = scaledWidth - var24 + 2;

				drawRect(var25 - 2, var19, var20, var19 + fontRenderer.FONT_HEIGHT, 1342177280);
				drawString(fontRenderer, var16, var25, var19, scoreboardTextShadow);

				if (scoreboardScores) {
					final String var17 = EnumChatFormatting.RED + "" + score.getScorePoints();
					fontRenderer.drawString(var17, var20 - fontRenderer.getStringWidth(var17), var19, 553648127);
				}

				if (count == scores.size()) {
					final String var21 = objective.getDisplayName();
					drawRect(var25 - 2, var19 - fontRenderer.FONT_HEIGHT - 1, var20, var19 - 1, 1610612736);
					drawRect(var25 - 2, var19 - 1, var20, var19, 1342177280);
					drawString(fontRenderer, var21, var25 + maxStringWidth / 2 - fontRenderer.getStringWidth(var21) / 2, var19 - fontRenderer.FONT_HEIGHT, scoreboardTextShadow);
				}
			}
		}
	}

	private void drawString(FontRenderer renderer, String text, int x, int z, boolean scoreboardTextShadow) {
		if (scoreboardTextShadow) {
			renderer.drawStringWithShadow(text, x, z, 553648127);
		} else {
			renderer.drawString(text, x, z, 553648127);
		}
	}
}