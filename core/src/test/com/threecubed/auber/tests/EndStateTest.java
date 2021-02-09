package com.threecubed.auber.tests;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;


@RunWith(GdxTestRunner.class)
public class EndStateTest {
  @Mock
  World worldMock = mock(World.class);

  @Test
  public void winTest() {
    worldMock.infiltratorCount = 0;
    worldMock.infiltratorsAddedCount = 8;
    World.systems.add(new RectangleMapObject());// make sure when test win, systems list not empty;
    worldMock.checkForEndState(true);
    assertTrue("User should win",World.userWon);
  }

  @Test
  public void loseTest() {
    worldMock.infiltratorCount = 3;
    World.systems.clear();
    boolean loseMock = worldMock.checkForEndState(true);
    assertFalse("User should lose", loseMock);
  }
}
