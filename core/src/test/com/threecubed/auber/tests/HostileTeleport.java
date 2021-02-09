package com.threecubed.auber.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

@RunWith(GdxTestRunner.class)
public class HostileTeleport {
  
  @Mock
  World worldMock = mock(World.class);
  @Mock
  Player playerMock = mock(Player.class);

  @Test
  public void validHostileTeleport() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas"); 
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    World.systems = new ArrayList<RectangleMapObject>();
    Infiltrator i = new Infiltrator(0, 0, worldMock);
    i.exposed = true;

    i.handleTeleporterShot(worldMock);
    assertFalse("AI should be disabled", i.aiEnabled);
    assertTrue("Infiltrator should be in the brig", (World.BRIG_BOUNDS[0][0] <= i.position.x) &&  (i.position.x <= World.BRIG_BOUNDS[1][0]) &&
    (World.BRIG_BOUNDS[0][1] <= i.position.y) &&  (i.position.y <= World.BRIG_BOUNDS[1][1]));
  }
}
