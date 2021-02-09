package com.threecubed.auber.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Civilian;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Npc;
import com.threecubed.auber.entities.Player;
import com.threecubed.auber.pathfinding.NavigationMesh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;

@RunWith(GdxTestRunner.class)
public class NPCNavigationTest {
  @Mock
  World worldMock = mock(World.class);
  @Mock
  NavigationMesh nav = mock(NavigationMesh.class);
  
  @Test
  public void stepTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    
    World.systems = new ArrayList<RectangleMapObject>();
    World.systems.add(new RectangleMapObject(1, 1, 0, 0));
    worldMock.navigationMesh = nav;

    ArrayList<Vector2> path = new ArrayList<Vector2>();
    path.add(new Vector2(1f, 1f));
    when(nav.generateWorldPathToPoint(new Vector2(0,0), new Vector2(1, 1))).thenReturn(path);
    
    Civilian n = new Civilian(0, 0, worldMock);
    Whitebox.setInternalState(n, "maxSpeed", 1);
    n.update(worldMock);
    assertEquals("NPC should move towards target", new Vector2(1,1), n.position);
  }

  @Test
  public void navigateToSystemTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    
    World.systems = new ArrayList<RectangleMapObject>();
    World.systems.add(new RectangleMapObject(80, 160, 0, 0));
    worldMock.navigationMesh = new NavigationMesh((TiledMapTileLayer) World.map.getLayers().get("navigation_layer"));
    
    Civilian n = new Civilian(64, 64, worldMock);
    assertEquals("NPC should have a path to the system", new Vector2(80, 160), n.getCurrentPath().get(n.getCurrentPath().size() -1));
  }

  @Test
  public void navigateToFleePointTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    
    float[] f = {560,640};
    
    worldMock.fleePoints = new ArrayList<float[]>();
    worldMock.fleePoints.add(f);
    worldMock.navigationMesh = new NavigationMesh((TiledMapTileLayer) World.map.getLayers().get("navigation_layer"));
    
    Civilian n = new Civilian(64, 64, worldMock);
    n.navigateToNearestFleepoint(worldMock);
    assertEquals("NPC should have a path towards the flee point", new Vector2(560,640), n.getCurrentPath().get(n.getCurrentPath().size() -1));
  }

  @Test
  public void moveToRandomLocationTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    
    worldMock.navigationMesh = new NavigationMesh((TiledMapTileLayer) World.map.getLayers().get("navigation_layer"));
    worldMock.spawnLocations = new ArrayList<float[]>();
    worldMock.spawnLocations.add(new float[] {560,640});

    Civilian n = new Civilian(64, 64, worldMock);
    n.moveToRandomLocation(worldMock);
    assertEquals("NPC should move to the point", new Vector2(560,640), n.position);
  }

  @Test
  public void navigateToFurthestPointTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    Whitebox.setInternalState(worldMock, "randomNumberGenerator", new Random());
    
    worldMock.navigationMesh = new NavigationMesh((TiledMapTileLayer) World.map.getLayers().get("navigation_layer"));
    Player p = new Player(64, 64, worldMock);
    worldMock.player = p;
    
    Civilian n = new Civilian(64, 64, worldMock);
    n.navigateToFurthestPointFromPlayer(worldMock);
    assertEquals("NPC should have a path to the system", new Vector2(704, 624), n.getCurrentPath().get(n.getCurrentPath().size() -1));
  }

}
