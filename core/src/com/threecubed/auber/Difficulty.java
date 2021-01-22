package com.threecubed.auber;

/**
 * Difficulty multipliers for NPCs 
 * these depend on the difficulty selected by the user in the main menu
 *
 * @author Lewis McKenze
 * @version 1.1
 * @since 1.1
 * */
public abstract class Difficulty {
    public enum Mode {
        EASY, 
        MEDIUM, 
        HARD {
            @Override
            public Mode next() {
                return Mode.EASY;
            };
        };
        public Mode next() {
            return values()[ordinal() + 1];
        }
    }
    public static Mode difficultyMode;
    public static float speedMultiplier;
    public static float sabotageMultiplier;
    public static float damageMultiplier;
    
    public static void load(Mode difficultyMode) {
        Difficulty.difficultyMode = difficultyMode;
        if (difficultyMode == Difficulty.Mode.EASY) {
            speedMultiplier = 0.5f;
            sabotageMultiplier = 0.5f;
            damageMultiplier = 0.5f;
        } else if (difficultyMode == Difficulty.Mode.MEDIUM) {
            speedMultiplier = 1;
            sabotageMultiplier = 1;
            damageMultiplier = 1;
        } else {
            speedMultiplier = 2f;
            sabotageMultiplier = 2f;
            damageMultiplier = 2f;
        }
    }
}
