package com.threecubed.auber.tests;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class PlayerSpawnTest {

  @Mock
  Player playerMock = mock(Player.class);
  @Mock
  World worldMock = mock(World.class);

  @Test
  public void spawnTest(){
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    playerMock.position = new Vector2(64,64);
    worldMock.generatePlayer();
    worldMock.player = playerMock;
    assertEquals("player not spawned correctly", 64, worldMock.player.position.x, 0.0);
    assertEquals("player not spawned correctly", 64, worldMock.player.position.y, 0.0);
  }
}
