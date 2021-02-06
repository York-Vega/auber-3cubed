package com.threecubed.auber.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.PowerUp;
import com.threecubed.auber.Utils;
import com.threecubed.auber.World;
import com.threecubed.auber.PowerUp.Type;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Player;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GdxTestRunner.class)
public class PowerUpsTest {

  @Mock
  World worldMock = mock(World.class);
  @Mock 
  Player playerMock = mock(Player.class);
  
  @Test
  public void immunityTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber.atlas");
    worldMock.player = playerMock;

    PowerUp p = new PowerUp(0, 0, worldMock, Type.IMMUNITY);
    p.activate(worldMock);
    assertTrue("Player should be immune but is not", worldMock.player.immune);
    p.deactivate(worldMock);
    assertFalse("Power up effect should end", worldMock.player.immune);
  } 

  @Test
  public void invisibilityTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber.atlas");
    playerMock.sprite = worldMock.atlas.createSprite("player");
    worldMock.player = playerMock;

    PowerUp p = new PowerUp(0, 0, worldMock, Type.INVISIBILITY);
    p.activate(worldMock);
    assertEquals("Player should be invisible but is not", 0.5f, worldMock.player.sprite.getColor().a, 0.01f);
    p.deactivate(worldMock);
    assertEquals("Power up effect should end", 1f, worldMock.player.sprite.getColor().a, 0f);
  } 

  @Test
  public void firewallTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber.atlas");
    worldMock.player = playerMock;

    PowerUp p = new PowerUp(0, 0, worldMock, Type.FIREWALL);
    p.activate(worldMock);
    assertFalse("Infiltrators should not be able to sabbotage", Infiltrator.canSabotage);
    p.deactivate(worldMock);
    assertTrue("Power up effect should end", Infiltrator.canSabotage);
  } 

  @Test
  public void speedTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber.atlas");
    playerMock.speed = 0.4f;
    worldMock.player = playerMock;

    PowerUp p = new PowerUp(0, 0, worldMock, Type.SPEED);
    p.activate(worldMock);
    assertEquals("Player speed should increace", 0.4 * 1.5, worldMock.player.speed, 0.01f);
    p.deactivate(worldMock);
    assertEquals("Power up effect should end", 0.4 , worldMock.player.speed, 0.01f);
  } 

  @Test
  public void detectTest() throws Exception {
    worldMock.atlas = new TextureAtlas("auber.atlas");
    worldMock.player = playerMock;

    Infiltrator i = new Infiltrator(0, 0, worldMock);
    ArrayList<GameEntity> entities = new ArrayList<GameEntity>();
    entities.add(i);

    PowerUp p = new PowerUp(0, 0, worldMock, Type.DETECT);
    when(worldMock.getEntities()).thenReturn(entities);
    when(worldMock.randomNumberGenerator).thenReturn(new Random()); 
    p.activate(worldMock);
    assertTrue("Infiltrator should be exposed", i.exposed);
    p.deactivate(worldMock);
    assertFalse("Power up effect should end", i.exposed);
  } 

}
