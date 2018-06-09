package uprizing;

import lombok.Getter;

@Getter
public class UprizingUser {

	private final int id; // uprizing unique id stored in database

	private final int groupId; // for future cape and client -> server interaction: staff panel, donator cosmetics, etc..

	public UprizingUser(final int id, final int groupId) {
		this.id = id;
		this.groupId = groupId;
	}
}