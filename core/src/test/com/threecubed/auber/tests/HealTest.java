package com.threecubed.auber.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GdxTestRunner.class)
public class HealTest {
  
  @Mock
  World worldMock = mock(World.class);

  @Test
  public void validHeal() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.camera = new OrthographicCamera();

    Player p = new Player(0, 0, worldMock);
    worldMock.player = p;
    p.health = 0.5f;
    worldMock.medbay = new RectangleMapObject();
    p.update(worldMock);
    assertEquals("Player should heal", 0.505f, p.health, 0.001f);
  }

  @Test
  public void invalidHeal() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.camera = new OrthographicCamera();

    Player p = new Player(96f, 640f, worldMock);
    worldMock.player = p;
    p.health = 0.5f;
    worldMock.medbay = new RectangleMapObject();
    p.update(worldMock);
    assertEquals("Player should heal", 0.5f, p.health, 0.001f);
  }

  @Test
  public void fullHeal() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.camera = new OrthographicCamera();

    Player p = new Player(0, 0, worldMock);
    worldMock.player = p;
    p.health = 1f;
    worldMock.medbay = new RectangleMapObject();
    p.update(worldMock);
    assertEquals("Player should heal", 1f, p.health, 0.001f);
  }
}
