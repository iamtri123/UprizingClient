package net.minecraft.scoreboard;

public abstract class Team
{

    /**
     * Same as ==
     */
    public boolean isSameTeam(Team other)
    {
        return other != null && this == other;
    }

    /**
     * Retrieve the name by which this team is registered in the scoreboard
     */
    public abstract String getRegisteredName();

    public abstract String formatString(String input);

    public abstract boolean func_98297_h();

    public abstract boolean getAllowFriendlyFire();
}