package com.threecubed.auber.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.threecubed.auber.Difficulty;
import com.threecubed.auber.Utils;
import com.threecubed.auber.World;
import com.threecubed.auber.pathfinding.NavigationMesh;
import java.util.ArrayList;
import java.util.Random;


/**
 * Npc is the class from which all AI controlled {@link GameEntity}s must extend.
 * It contains the code that allows for those entities to interact with the
 * {@link NavigationMesh} in the pathfinding package, along with handling the
 * state of an entity
 *
 * @author Daniel O'Brien
 * @version 1.0
 * @since 1.0
 * */
public abstract class Npc extends GameEntity {
  private ArrayList<Vector2> currentPath = new ArrayList<>();
  private Vector2 targetDirection = new Vector2();
  private NavigationMesh navigationMesh;

  protected float maxSpeed;

  private static String[] textureNames = {"alienA", "alienB", "alienC"};

  public States state = States.IDLE;

  public enum States {
    IDLE,
    NAVIGATING,
    REACHED_DESTINATION,
    FLEEING,
    ATTACKING_SYSTEM,
  }

  public boolean aiEnabled = true;
  protected Timer npcTimer = new Timer();

  /**
   * Initialise an NPC with a given texture.
   *
   * @param x The x coordinate to initialise the NPC at
   * @param y The y coordinate to initialise the NPC at
   * @param sprite The NPC sprite
   * @param navigationMesh The navigation mesh.
   * */
  public Npc(float x, float y, Sprite sprite, NavigationMesh navigationMesh) {
    super(x, y, sprite);
    maxSpeed = 1.3f * Difficulty.speedMultiplier;
    Random rng = new Random(); // TODO: Switch to use the world RNG
    maxSpeed *= Utils.randomFloatInRange(rng, World.NPC_SPEED_VARIANCE[0],
        World.NPC_SPEED_VARIANCE[1]);
    this.navigationMesh = navigationMesh;
  }

  /**
   * Initialise an NPC with a random NPC sprite.
   *
   * @param x The x coordinate to initialise the NPC at
   * @param y The y coordinate to initialise the NPC at
   * @param world The game world
   * */
  public Npc(float x, float y, World world) {
    this(x, y,
        world.atlas.createSprite(
          textureNames[Utils.randomIntInRange(world.randomNumberGenerator, 0,
            textureNames.length - 1)]),
        world.navigationMesh);
  }

  /**
   * Initialise the NPC at a random location.
   *
   * @param world The game world
   * */
  public Npc(World world) {
    this(0f, 0f, world);
    moveToRandomLocation(world);
  }

  /**
   * Update the NPC by stepping it in the direction of its current target.
   *
   * @param world The game world
   * */
  protected void stepTowardsTarget(World world) {
    if (aiEnabled) {
      if (currentPath.size() == 0) {
        return;
      }
      Vector2 targetCoordinates = currentPath.get(0);
      Vector2 currentDirection = getCurrentDirection();

      // Rotate the entity to face the direction its heading
      rotation = currentDirection.angleDeg();

      boolean entityMoved = false;
      if (currentDirection.x == targetDirection.x && targetDirection.x != 0) {
        float velocityX = Math.signum(targetCoordinates.x - position.x) * maxSpeed;
        if (state == States.FLEEING) {
          velocityX *= World.NPC_FLEE_MULTIPLIER;
        }
        position.x += velocityX;
        entityMoved = true;
      }

      if (currentDirection.y == targetDirection.y && targetDirection.y != 0) {
        float velocityY = Math.signum(targetCoordinates.y - position.y) * maxSpeed;
        if (state == States.FLEEING) {
          velocityY *= World.NPC_FLEE_MULTIPLIER;
        }
        position.y += velocityY;
        entityMoved = true;
      }

      if (!entityMoved) {
        // If the entity hasn't moved, it must have reached its target node.
        // Remove the node and recalculate the current direction to head in.
        currentPath.remove(0);
        if (!currentPath.isEmpty()) {
          targetDirection = getCurrentDirection();
        } else {
          state = States.REACHED_DESTINATION;
        }
      }
    }
  }

  /**
   * Navigate to the furthest point from the player.
   *
   * @param world The game world
   * */
  public void navigateToFurthestPointFromPlayer(World world) {
    Vector2 furthestPoint = navigationMesh.getFurthestPointFromEntity(world.player);
    currentPath = navigationMesh.generateWorldPathToPoint(position, furthestPoint);
  }

  /**
   * Control the state the NPC is in and fire any necessary events when need be.
   *
   * @param world The game world
   * */
  @Override
  public void update(World world) {
    if (aiEnabled) {
      switch (state) {
        case NAVIGATING:
        case FLEEING:
          stepTowardsTarget(world);
          break;
        case REACHED_DESTINATION:
          handleDestinationReached(world);
          break;
        case IDLE:
          break;
        default:
          break;
      }
    }
  }

  /**
   * Update the {@link Npc#currentPath} to a given set of x and y coordinates.
   *
   * @param x The x coordinate to navigate to
   * @param y The y coordinate to navigate to
   * @param world The game world
   * */
  public void updatePath(float x, float y, World world) {
    if (!currentPath.isEmpty()) {
      currentPath.clear();
    }
    currentPath = navigationMesh.generateWorldPathToPoint(position, new Vector2(x, y));
    targetDirection = getCurrentDirection();
  }

  /**
   * Pick a random system in the game world and navigate towards it.
   *
   * @param world The game world
   * */

  protected void navigateToRandomSystem(World world) {
    if (!World.systems.isEmpty()) {
      state = States.NAVIGATING;
      RectangleMapObject system = World.systems.get(
          Utils.randomIntInRange(world.randomNumberGenerator,
            0, World.systems.size() - 1));

      updatePath(system.getRectangle().getX(), system.getRectangle().getY(), world);
    }
  }

  /**
   * Handle the event of the NPC reaching its current destination. For {@link Infiltrator}s this
   * might be to sabotage a system and for {@link Civilian}s this might be to idle for a bit
   *
   * @param world The game world
   * */
  public abstract void handleDestinationReached(World world);

  /**
   * Handle the event of being shot with Auber's teleporting ray gun.
   *
   * @param world The game world
   * */
  public abstract void handleTeleporterShot(World world);

  /**
   * Return a {@link Vector2} representing the direction the NPC is currently heading in.
   *
   * @return A {@link Vector2} representing the direction the NPC is currently heading in.
   * */
  public Vector2 getCurrentDirection() {
    return new Vector2(
        Math.signum(currentPath.get(0).x - position.x),
        Math.signum(currentPath.get(0).y - position.y)
        );
  }

  protected void idleForGivenTime(final World world, float seconds) {
    npcTimer.scheduleTask(new Task() {
      @Override
      public void run() {
        if (aiEnabled) {
          state = States.NAVIGATING;

          // Pick new system to navigate to
          navigateToRandomSystem(world);
        }
      }
    }, seconds);
  }

  /**
   * Create a path to the nearest flee point (euclidian) for the NPC to flee to.
   *
   * @param world The game world
   * */
  public void navigateToNearestFleepoint(final World world) {
    state = States.FLEEING;

    ArrayList<Float> distances = new ArrayList<>();
    ArrayList<float[]> closestFleePoints = new ArrayList<>();

    Circle minimumFleeRange = new Circle(position, World.NPC_MIN_FLEE_DISTANCE);

    for (float[] fleePoint : world.fleePoints) {
      float newDistance = NavigationMesh.getEuclidianDistance(fleePoint,
                                                           new float[] {position.x, position.y});

      if (!minimumFleeRange.contains(fleePoint[0], fleePoint[1])) {
        if (distances.size() < 2) {
          distances.add(newDistance);
          closestFleePoints.add(fleePoint);
          continue;
        }
        for (int i = 0; i < distances.size(); i++) {
          float distance = distances.get(i);
          if (newDistance < distance) {
            distances.set(i, distance);
            closestFleePoints.set(i, fleePoint);
            break;
          }
        }
      }
    }
    float[] chosenFleePoint = closestFleePoints.get(Utils.randomIntInRange(
      world.randomNumberGenerator, 0, closestFleePoints.size() - 1)
    );

    currentPath = navigationMesh.generateWorldPathToPoint(
        position,
        new Vector2(chosenFleePoint[0], chosenFleePoint[1])
        );

    // Fleeing takes priority over all tasks
    npcTimer.clear();
    npcTimer.scheduleTask(new Task() {
      @Override
      public void run() {
        if (aiEnabled) {
          state = States.NAVIGATING;
          navigateToRandomSystem(world);
        }
      }
    }, World.NPC_FLEE_TIME);
  }

  public States getState() {
    return state;
  }

  /**
   * Move the entity to a random location within the world.
   *
   * @param world The game world
   **/
  public void moveToRandomLocation(World world) {
    float[] location = world.spawnLocations.get(Utils.randomIntInRange(
        world.randomNumberGenerator, 0,
        world.spawnLocations.size() - 1));
    position.x = location[0];
    position.y = location[1];
  }
}
