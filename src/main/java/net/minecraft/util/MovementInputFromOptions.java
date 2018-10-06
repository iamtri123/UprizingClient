package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;
import uprizing.ToggleSprint;

public class MovementInputFromOptions extends MovementInput {

	private final GameSettings gameSettings;
	private final ToggleSprint toggleSprint;
	private boolean keyBindJumpPressed;
	private boolean keyBindSneakPressed;

	public MovementInputFromOptions(GameSettings p_i1237_1_, ToggleSprint toggleSprint) {
		this.gameSettings = p_i1237_1_;
		this.toggleSprint = toggleSprint;
	}

	public void updatePlayerMoveState() {
		this.moveStrafe = 0.0F;
		this.moveForward = 0.0F;

		if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
			++this.moveForward;
		}

		if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
			--this.moveForward;
		}

		if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
			++this.moveStrafe;
		}

		if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
			--this.moveStrafe;
		}

		if (gameSettings.keyBindJump.getIsKeyPressed()) {
			if (toggleSprint.jumpEnabled) {
				if (!keyBindJumpPressed) {
					jump = !jump;
					keyBindJumpPressed = true;
				}
			} else if (!jump) { // vanilla
				jump = true;
			}
		} else if (toggleSprint.jumpEnabled) {
			if (keyBindJumpPressed) {
				keyBindJumpPressed = false;
			}
		} else if (jump) { // vanilla
			jump = false;
		}

		if (gameSettings.keyBindSneak.getIsKeyPressed()) { // TODO: support fly, ride
			if (toggleSprint.sneakEnabled) {
				if (!keyBindSneakPressed) {
					sneak = !sneak;
					keyBindSneakPressed = true;
				}
			} else if (!sneak) { // vanilla
				sneak = true;
			}

			toggleSprint.mode = ToggleSprint.SNEAKING_KEY_HELD;
		} else if (toggleSprint.sneakEnabled) {
			if (keyBindSneakPressed) {
				keyBindSneakPressed = false;
				toggleSprint.mode = sneak ? ToggleSprint.SNEAKING_TOGGLED : ToggleSprint.OFF;
			}
		} else if (sneak) { // vanilla
			sneak = false;
			toggleSprint.mode = ToggleSprint.OFF;
		}

		if (this.sneak) {
			this.moveStrafe = (float) ((double) this.moveStrafe * 0.3D);
			this.moveForward = (float) ((double) this.moveForward * 0.3D);
		}
	}
}