package com.threecubed.auber.tests;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Infiltrator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class SystemTest {
  @Mock
  World worldMock = mock(World.class);
  @Mock
  Infiltrator infiltratorMock = mock(Infiltrator.class);


  @Test
  public void testSystemDestroyed() {
    worldMock.queueEntityAdd(infiltratorMock);
    infiltratorMock.position = new Vector2(32,32);
    infiltratorMock.attackNearbySystem(worldMock);
    assertNull("system should be destroyed", worldMock.getSystemState(32, 32));
  }

  @Test
  public void testTimeForDestroyed() {
    worldMock.queueEntityAdd(infiltratorMock);
    World.systems.add(new RectangleMapObject());
    infiltratorMock.position = new Vector2(32,32);
    float timeUsed = infiltratorMock.timer(worldMock,new Vector2(32,32));
    assertEquals("time test failed", 0, timeUsed, 0.0);
  }
}
