package com.threecubed.auber.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.threecubed.auber.Difficulty;
import com.threecubed.auber.GdxTestRunner;
import com.threecubed.auber.World;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Player;
import com.threecubed.auber.entities.Projectile;
import com.threecubed.auber.entities.Projectile.CollisionActions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

@RunWith(GdxTestRunner.class)
public class DamageTest {

  @Mock
  World worldMock = mock(World.class);
  @Mock
  Player playerMock = mock(Player.class);
  @Mock
  Infiltrator infiltratorMock = mock(Infiltrator.class);
  
  @Test
  public void maxHealth() throws Exception{
    playerMock.immune = false;    
    playerMock.health = 1f;
    playerMock.playerTimer = new Timer();
    playerMock.position = new Vector2(0,0);
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    playerMock.sprite = worldMock.atlas.createSprite("player"); 
    worldMock.player = playerMock;

    Difficulty.damageMultiplier = 1;

    Projectile p = new Projectile(0, 0, new Vector2(0,0), infiltratorMock, CollisionActions.BLIND, worldMock);  
    p.update(worldMock);
    assertEquals("player's health is : " + worldMock.player.health,
            0.8f, worldMock.player.health, 0.0);
  }

  @Test
  public void minHealth() throws Exception{
    playerMock.immune = false;    
    playerMock.health = 0f;
    playerMock.playerTimer = new Timer();
    playerMock.position = new Vector2(0,0);
    worldMock.atlas = new TextureAtlas("auber_.atlas");
    playerMock.sprite = worldMock.atlas.createSprite("player"); 
    worldMock.player = playerMock;

    Difficulty.damageMultiplier = 1;

    Projectile p = new Projectile(0, 0, new Vector2(0,0), infiltratorMock, CollisionActions.BLIND, worldMock);  
    p.update(worldMock);
    assertEquals("player's health is : " + worldMock.player.health,
            -0.2f, worldMock.player.health, 0.0);
  }
}
