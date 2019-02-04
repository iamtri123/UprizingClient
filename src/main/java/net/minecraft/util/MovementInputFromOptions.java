package net.minecraft.util;

import net.minecraft.client.entity.EntityPlayerSP;
import uprizing.ToggleSprint;
import uprizing.keybinding.KeyBindings;

public class MovementInputFromOptions extends MovementInput {

    private final EntityPlayerSP player;
    private final KeyBindings keyBindings;
    private final ToggleSprint toggleSprint;

    private boolean keyBindJumpPressed;

    public boolean isDisabled;
    public boolean canDoubleTap;
    private long lastPressed;
    private long lastSprintPressed;
    private boolean handledSneakPress;
    private boolean handledSprintPress;
    private boolean wasRiding;
    public boolean sprint = false;
    public boolean sprintHeldAndReleased = false;
    public boolean sprintDoubleTapped = false;

    public MovementInputFromOptions(EntityPlayerSP player, KeyBindings keyBindings, ToggleSprint toggleSprint) {
        this.player = player;
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

        /* -- Jumping -- */

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

        /* -- ToggleSprint -- */

        if (toggleSprint.alwaysSneaking) {
            /* - Key Pressed - */

            if (keyBindings.sneak.getIsKeyPressed() && !this.handledSneakPress) {
                if (player.isRiding() || player.capabilities.isFlying) { // Descend if we are flying, note if we were riding (so we can unsneak once dismounted)
                    sneak = true;
                    wasRiding = player.isRiding();
                } else {
                    sneak = !sneak;
                }

                lastPressed = System.currentTimeMillis();
                handledSneakPress = true;
            }

            /* - Key Released - */

            if (!keyBindings.sneak.getIsKeyPressed() && handledSneakPress) {
                if (player.capabilities.isFlying || wasRiding) { // If we are flying or riding, stop sneaking after descent/dismount.
                    sneak = false;
                    wasRiding = false;
                } else if (System.currentTimeMillis() - lastPressed > 300L) { // If the key was held down for more than 300ms, stop sneaking upon release.
                    sneak = false;
                }

                handledSneakPress = false;
            }
        } else {
            sneak = keyBindings.sneak.getIsKeyPressed();
        }

        if (sneak) {
            moveStrafe = (float) ((double) moveStrafe * 0.3D);
            moveForward = (float) ((double) moveForward * 0.3D);
        }

        // Establish conditions where we don't want to start a sprint - sneaking, riding, flying, hungry

        final boolean enoughHunger = (float) player.getFoodStats().getFoodLevel() > 6.0F || player.capabilities.isFlying;
        final boolean canSprint = !sneak && !player.isRiding() && !player.capabilities.isFlying && enoughHunger;

        isDisabled = !toggleSprint.alwaysSprinting;
        canDoubleTap = toggleSprint.doubleTapping;

        /* - Key Pressed - */

        if ((canSprint || isDisabled) && keyBindings.sprint.getIsKeyPressed() && !handledSprintPress) {
            if (!isDisabled) {
                sprint = !this.sprint;
                lastSprintPressed = System.currentTimeMillis();
                handledSprintPress = true;
                sprintHeldAndReleased = false;
            }
        }

        /* - Key Released - */

        if ((canSprint || isDisabled) && !keyBindings.sprint.getIsKeyPressed() && handledSprintPress) {
            if (System.currentTimeMillis() - lastSprintPressed > 300L) { // Was key held for longer than 300ms? If so, mark it so we can resume vanilla behavior
                sprintHeldAndReleased = true;
            }

            handledSprintPress = false;
        }

        updateMode();
    }

    public void updateSprint(boolean newValue, boolean doubleTapped) {
        sprint = newValue;
        sprintDoubleTapped = doubleTapped;
    }

    // Detect any changes in movement state and update HUD - Added 4/14/2014
    private void updateMode() {
        if (toggleSprint.showText) {
            int mode = ToggleSprint.OFF;

            final boolean isFlying = player.capabilities.isFlying;
            final boolean isRiding = player.isRiding();
            final boolean isHoldingSneak = keyBindings.sneak.getIsKeyPressed();
            final boolean isHoldingSprint = keyBindings.sprint.getIsKeyPressed();

            if (isFlying) {
                //if (toggleSprint.optionEnableFlyBoost && isHoldingSprint) {
                //output += "[Flying (" + toggleSprint.optionFlyBoostAmount + "x boost)]  ";
                //} else {
                //output += "[Flying]  ";
                //}

                mode = ToggleSprint.FLYING;
            }

            if (isRiding) {
                mode = ToggleSprint.RIDING;
            }

            if (player.test || jump) {
                if (toggleSprint.alwaysJumping) {
                    mode = ToggleSprint.JUMPING_TOGGLED;
                } else {
                    mode = ToggleSprint.JUMPING_VANILLA;
                }
            } else if (sneak) {
                if (isFlying) {
                    mode = ToggleSprint.DESCENDING;
                } else if (isRiding) {
                    mode = ToggleSprint.DISMOUNTING;
                } else if (isHoldingSneak) {
                    mode = ToggleSprint.SNEAKING_KEY_HELD;
                } else {
                    mode = ToggleSprint.SNEAKING_TOGGLED;
                }
            } else if (sprint) {
                if (!isFlying && !isRiding) {
                    // Detect Vanilla conditions - ToggleSprint disabled, DoubleTapped and Hold & Release
                    final boolean isVanilla = sprintHeldAndReleased || isDisabled || sprintDoubleTapped;

                    if (isHoldingSprint) {
                        mode = ToggleSprint.SPRINTING_KEY_HELD;
                    } else if (isVanilla) {
                        if (player.isSprinting()) {
                            mode = ToggleSprint.SPRINTING_VANILLA;
                        } else if (moveForward > 0.8) {
                            mode = ToggleSprint.WALKING_VANILLA;
                        }
                    } else {
                        mode = ToggleSprint.SPRINTING_TOGGLED;
                    }
                }
            }

            toggleSprint.mode = mode;
        }
    }
}