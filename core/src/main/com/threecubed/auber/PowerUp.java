package com.threecubed.auber;

import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.entities.GameEntity;
import com.threecubed.auber.entities.Infiltrator;

/**
 * power ups which change aspects of the game for a period of time.
 * depending on the type of power up
 * activated when the player steps on a specific tile
 *
 * @author Matt Rogan, Lewis McKenzie
 * @version 1.1
 * @since 1.1
 * */
public class PowerUp extends GameEntity {
  /** Identifies the type of power up. */
  public enum Type {
    IMMUNITY,
    INVISIBILITY,
    FIREWALL,
    SPEED,
    DETECT,
  }

  private Type powerType;
  private float duration;
  private float timer;
  private boolean active;
  private boolean used;

  /** 
   * Initialises power up.
   *
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @param world the world the power up will inhibit.
   * @param powerType the type of power up it is.  
  */
  public PowerUp(final float x, final float y, final World world, final Type powerType) {
    super(x, y, world.atlas.createSprite("projectile"));
    this.powerType = powerType;
    duration = 10f;
    active = false;
    used = false;
  }

  /** 
   * Activates the power up.
   *
   * @param world the world the power up inhibits.
  */
  public final void activate(final World world) {
    active = true;
    timer = duration;
    this.sprite.setAlpha(0f);
    switch (powerType) {
      case IMMUNITY:
        world.player.immune = true;
        break;
      case INVISIBILITY:
        world.player.invisible = true;
        world.player.sprite.setAlpha(0.5f);
        break;
      case FIREWALL:
        Infiltrator.canSabotage = false;
        break;
      case SPEED:
        world.player.speed *= 1.5;
        break;
      case DETECT:
        for (GameEntity e : world.getEntities()) {
          if (e instanceof Infiltrator) {
            ((Infiltrator) e).expose(world);
          }
        }
        break;
      default:
        break;
    }
  }

  /** 
   * Updates the power up.
   * To be called every frame.
   *
   * @param world the world the power up inhibits.
  */
  @Override
  public final void update(final World world) {
    // Assuming timeStep is 1/60 (FPS)
    if (active) {
      timer -= 1 / 60f;
      if (timer <= 0) {
        deactivate(world);
      }
    } else if (!used) {
      Vector2 playerPosition = world.player.position; // (x,y) component

      int[] tileMapCoords = world.navigationMesh.getTilemapCoordinates(position.x, position.y);
      int[] playerCoords = world.navigationMesh.getTilemapCoordinates(playerPosition.x, 
      playerPosition.y);

      if ((tileMapCoords[0] == playerCoords[0]) && (tileMapCoords[1] == playerCoords[1])) {
        activate(world);
      }
    }
  }

  /** 
   * Deactivates the power up.
   * called 10 seconds after being activated.
   *
   * @param world the world the power up inhibits.
  */
  public final void deactivate(final World world) {
    active = false;
    used = true;
    switch (powerType) {
      case IMMUNITY:
        world.player.immune = true;
        break;
      case INVISIBILITY:
        world.player.invisible = false;
        world.player.sprite.setAlpha(1f);
        break;
      case FIREWALL:
        Infiltrator.canSabotage = true;
        break;
      case SPEED:
        world.player.speed /= 1.5;
        break;
      case DETECT:
        for (GameEntity e : world.getEntities()) {
          if (e instanceof Infiltrator) {
            ((Infiltrator) e).unexpose(world);
          }
        }
        break;
      default:
        break;
    }
  }
}
