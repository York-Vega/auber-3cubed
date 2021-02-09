package com.threecubed.auber.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(GdxTestRunner.class)
public class TeleportTest {

  @Mock
  World worldMock = mock(World.class);

  @Test
  public void medbayTeleportTest() {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Player p = new Player(0,0,worldMock);
    float[] medbayCoords = {96f, 640f};
    World.MEDBAY_COORDINATES = medbayCoords;

    p.teleportToMedbay();
    assertEquals("Player should teleport to the medbay", new Vector2(96f, 640f), p.position);

  }

  @Test
  public void generalTeleportTest() {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Player p = new Player(96f, 640f,worldMock);

    p.sprite.setPosition(p.position.x, p.position.y);
    p.teleport();
    assertEquals("Player should teleport", new Vector2(448,112), p.position);
    p.sprite.setPosition(p.position.x, p.position.y);
    p.teleport();
    assertEquals("Player should teleport", new Vector2(560,640), p.position);
    p.sprite.setPosition(p.position.x, p.position.y);
    p.teleport();
    assertEquals("Player should teleport", new Vector2(448,112), p.position);

  }

  @Test
  public void invalidTeleportTest() {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Player p = new Player(0f, 0f,worldMock);

    p.sprite.setPosition(p.position.x, p.position.y);
    p.teleport();
    assertNotEquals("Player should not teleport", new Vector2(448,112), p.position);
    assertNotEquals("Player should not teleport", new Vector2(560,640), p.position);
    assertNotEquals("Player should not teleport", new Vector2(96, 640), p.position);
  }



}
