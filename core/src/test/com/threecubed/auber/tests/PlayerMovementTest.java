package com.threecubed.auber.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

@RunWith(GdxTestRunner.class)
public class PlayerMovementTest {
  
  @Mock
  World worldMock = mock(World.class);
  @Mock 
  Input inputMock = mock(Input.class);

  @Test
  public void playerLeftInputTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.medbay = new RectangleMapObject();
    worldMock.camera = new OrthographicCamera();
    Player p = new Player(64f, 64f, worldMock);

    Gdx.input = inputMock;
    when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(true);
    p.update(worldMock);
    assertEquals("message", new Vector2(64f-0.4f, 64f), p.position);
  }

  @Test
  public void playerRightInputTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.medbay = new RectangleMapObject();
    worldMock.camera = new OrthographicCamera();
    Player p = new Player(64f, 64f, worldMock);

    Gdx.input = inputMock;
    when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(true);
    p.update(worldMock);
    assertEquals("message", new Vector2(64f+0.4f, 64f), p.position);
  }

  @Test
  public void playerUpInputTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.medbay = new RectangleMapObject();
    worldMock.camera = new OrthographicCamera();
    Player p = new Player(64f, 64f, worldMock);

    Gdx.input = inputMock;
    when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(true);
    p.update(worldMock);
    assertEquals("message", new Vector2(64f, 64f+0.4f), p.position);
  }

  @Test
  public void playerDownInputTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.medbay = new RectangleMapObject();
    worldMock.camera = new OrthographicCamera();
    Player p = new Player(64f, 64f, worldMock);

    Gdx.input = inputMock;
    when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(true);
    p.update(worldMock);
    assertEquals("message", new Vector2(64f, 64f-0.4f), p.position);
  }
}
