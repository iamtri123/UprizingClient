package net.minecraft.util;

import uprizing.ToggleSprint;
import uprizing.keybinding.KeyBindings;

public class MovementInputFromOptions extends MovementInput {

	private final KeyBindings keyBindings;
	private final ToggleSprint toggleSprint;
	private boolean keyBindJumpPressed;
	private boolean keyBindSneakPressed;

	public MovementInputFromOptions(KeyBindings keyBindings, ToggleSprint toggleSprint) {
		this.keyBindings = keyBindings;
		this.toggleSprint = toggleSprint;
	}

	public void updatePlayerMoveState() {
		moveStrafe = 0.0F;
		moveForward = 0.0F;

		if (keyBindings.forward.getIsKeyPressed()) {
			++moveForward;
		}

		if (keyBindings.back.getIsKeyPressed()) {
			--moveForward;
		}

		if (keyBindings.left.getIsKeyPressed()) {
			++moveStrafe;
		}

		if (keyBindings.right.getIsKeyPressed()) {
			--moveStrafe;
		}

		if (keyBindings.jump.getIsKeyPressed()) {
			if (toggleSprint.alwaysJumping) {
				if (!keyBindJumpPressed) {
					jump = !jump;
					keyBindJumpPressed = true;
				}
			} else if (!jump) { // vanilla
				jump = true;
			}
		} else if (toggleSprint.alwaysJumping) {
			if (keyBindJumpPressed) {
				keyBindJumpPressed = false;
			}
		} else if (jump) { // vanilla
			jump = false;
		}

		if (keyBindings.sneak.getIsKeyPressed()) { // TODO: support fly, ride
			if (toggleSprint.alwaysSneaking) {
				if (!keyBindSneakPressed) {
					sneak = !sneak;
					keyBindSneakPressed = true;
				}
			} else if (!sneak) { // vanilla
				sneak = true;
			}

			toggleSprint.mode = ToggleSprint.SNEAKING_KEY_HELD;
		} else if (toggleSprint.alwaysSneaking) {
			if (keyBindSneakPressed) {
				keyBindSneakPressed = false;
				toggleSprint.mode = sneak ? ToggleSprint.SNEAKING_TOGGLED : ToggleSprint.OFF;
			}
		} else if (sneak) { // vanilla - sneak mais n'appuie plus sur la touche
			sneak = false;
			toggleSprint.mode = ToggleSprint.OFF;
		}

		if (sneak) {
			moveStrafe = (float) ((double) moveStrafe * 0.3D);
			moveForward = (float) ((double) moveForward * 0.3D);
		}
	}
}