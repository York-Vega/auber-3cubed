package com.threecubed.auber.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Npc;
import com.threecubed.auber.entities.Player;
import com.threecubed.auber.pathfinding.NavigationMesh;
import com.threecubed.auber.screens.GameScreen;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;


@RunWith(GdxTestRunner.class)
public class HostileArrestTest {

  @Mock
  World worldMock = mock(World.class);
  @Mock
  NavigationMesh nav = mock(NavigationMesh.class);

  @Test
  public void validFlee() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.camera = new OrthographicCamera();
    worldMock.medbay = new RectangleMapObject();
    worldMock.navigationMesh = nav;
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    World.systems = new ArrayList<RectangleMapObject>();

    Infiltrator i = new Infiltrator(0,0,worldMock);
    i.position = new Vector2(0,0);
    ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
    entities.add(i);
    when(worldMock.getEntities()).thenReturn(entities);

    Player p = new Player(0, 0, worldMock);
    when(nav.getFurthestPointFromEntity(worldMock.player)).thenReturn(new Vector2(0,0));
    worldMock.player = p;
    worldMock.auberTeleporterCharge = 1;
    GameScreen.enemyTrack = new HashMap<Infiltrator, Integer>();

    p.update(worldMock);

    assertTrue("Infiltrator should be exposed", i.exposed);
    assertEquals("Infiltrator should be fleeing", Npc.States.FLEEING, i.state);
  }

  @Test
  public void invalidFlee() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.camera = new OrthographicCamera();
    worldMock.medbay = new RectangleMapObject();
    worldMock.navigationMesh = nav;
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    World.systems = new ArrayList<RectangleMapObject>();

    Infiltrator i = new Infiltrator(0,0,worldMock);
    i.position = new Vector2(0,0);
    ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
    entities.add(i);
    when(worldMock.getEntities()).thenReturn(entities);

    Player p = new Player(96, 640, worldMock);
    when(nav.getFurthestPointFromEntity(worldMock.player)).thenReturn(new Vector2(0,0));
    worldMock.player = p;
    worldMock.auberTeleporterCharge = 1;
    GameScreen.enemyTrack = new HashMap<Infiltrator, Integer>();

    p.update(worldMock);

    assertFalse("Infiltrator should not be exposed", i.exposed);
    assertNotEquals("Infiltrator should not be fleeing", Npc.States.FLEEING, i.state);
  }

  @Test
  public void validArrest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    worldMock.camera = new OrthographicCamera();
    worldMock.medbay = new RectangleMapObject();
    worldMock.navigationMesh = nav;
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    World.systems = new ArrayList<RectangleMapObject>();

    Infiltrator i = new Infiltrator(0,0,worldMock);
    i.position = new Vector2(0,0);
    i.exposed = true;
    ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
    entities.add(i);
    when(worldMock.getEntities()).thenReturn(entities);

    Player p = new Player(0, 0, worldMock);
    when(nav.getFurthestPointFromEntity(worldMock.player)).thenReturn(new Vector2(0,0));
    worldMock.player = p;
    worldMock.auberTeleporterCharge = 1;
    GameScreen.enemyTrack = new HashMap<Infiltrator, Integer>();

    p.update(worldMock);

    assertTrue("Infiltrator should be arrested", i.arrested);
    assertFalse("Infiltrator ai should be disabled", i.aiEnabled);
  }

}