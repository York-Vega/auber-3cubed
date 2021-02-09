package com.threecubed.auber.tests;

import static org.junit.Assert.*;
import com.threecubed.auber.Difficulty;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.Difficulty.Mode;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class DifficultyTest {

  @Test
  public void EasyTest() throws Exception {
    Difficulty.load(Mode.EASY);
    assertEquals(Mode.EASY, Difficulty.difficultyMode);
    assertEquals(0.5f, Difficulty.damageMultiplier, 0f);    
    assertEquals(0.5f, Difficulty.sabotageMultiplier, 0f);
    assertEquals(0.5f, Difficulty.speedMultiplier, 0f);
  }

  @Test
  public void MediumTest() throws Exception {
    Difficulty.load(Mode.MEDIUM);
    assertEquals(Mode.MEDIUM, Difficulty.difficultyMode);
    assertEquals(1f, Difficulty.damageMultiplier, 0f);    
    assertEquals(1f, Difficulty.sabotageMultiplier, 0f);
    assertEquals(1f, Difficulty.speedMultiplier, 0f);
  }

  @Test
  public void HardTest() throws Exception {
    Difficulty.load(Mode.HARD);
    assertEquals(Mode.HARD, Difficulty.difficultyMode);
    assertEquals(2f, Difficulty.damageMultiplier, 0f);    
    assertEquals(2f, Difficulty.sabotageMultiplier, 0f);
    assertEquals(2f, Difficulty.speedMultiplier, 0f);
  }
}
