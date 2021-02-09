package com.threecubed.auber.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.threecubed.auber.Utils;
import com.threecubed.auber.World;
import com.threecubed.auber.pathfinding.NavigationMesh;
import com.threecubed.auber.screens.GameScreen;


/**
 * The player entity that the user controls. Handles keyboard input, and interaction with other
 * entities and tiles in the game world.
 *
 * @author Daniel O'Brien
 * @version 1.0
 * @since 1.0
 * */
public class Player extends GameEntity {
  public Timer playerTimer = new Timer();
  private Vector2 teleporterRayCoordinates = new Vector2();

  /** Health of Auber - varies between 1 and 0. */
  public float health = 1;

  public boolean confused = false;
  public boolean slowed = false;
  public boolean blinded = false;

  public Boolean immune = false;
  public Boolean invisible = false;
  public Boolean firewall = false;
  public Boolean speedUp = false;
  public Boolean detect = false;

  private ShapeRenderer rayRenderer = new ShapeRenderer();

  public Player(float x, float y, World world) {
    super(x, y, world.atlas.createSprite("player"));
  }

  /**
   * Handle player controls such as movement, interaction and firing the teleporing gun.
   *
   * @param world The game world
   * */
  @Override
  public void update(World world) {
    if (!world.demoMode) {
      if (Gdx.input.isKeyJustPressed(Input.Keys.Q) || health <= 0) {
        teleportToMedbay();
      }

      // Increment Auber's health if in medbay
      if (world.medbay.getRectangle().contains(position.x, position.y)) {
        health += World.AUBER_HEAL_RATE;
        health = Math.min(1f, health);
      }
      // Slow down Auber when they charge their weapon. Should be stopped when weapon half charged,
      // hence the * 2
      float speedModifier = Math.min(world.auberTeleporterCharge * speed * 2, speed);
      if (slowed) {
        velocity.scl(0.5f);
      }

      // Flip the velocity before new velocity calculated if confused. Otherwise, second iteration
      // of flipped velocity will cancel out the first
      if (confused) {
        velocity.set(-velocity.x, -velocity.y);
      }

      if (Gdx.input.isKeyPressed(Input.Keys.W)) {
        velocity.y = Math.min(velocity.y + speed - speedModifier, maxSpeed);
      }
      if (Gdx.input.isKeyPressed(Input.Keys.A)) {
        velocity.x = Math.max(velocity.x - speed + speedModifier, -maxSpeed);
      }
      if (Gdx.input.isKeyPressed(Input.Keys.S)) {
        velocity.y = Math.max(velocity.y - speed + speedModifier, -maxSpeed);
      }
      if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        velocity.x = Math.min(velocity.x + speed - speedModifier, maxSpeed);
      }


      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && teleporterRayCoordinates.isZero()) {
        world.auberTeleporterCharge = Math.min(world.auberTeleporterCharge
            + World.AUBER_CHARGE_RATE, 1f);
      } else {
        if (world.auberTeleporterCharge > 0.95f) {
          world.auberTeleporterCharge = 0;

          // Scare entities
          teleporterRayCoordinates = handleRayCollisions(world);
          for (GameEntity entity : world.getEntities()) {
            float entityDistance = NavigationMesh.getEuclidianDistance(
                new float[] {teleporterRayCoordinates.x, teleporterRayCoordinates.y},
                new float[] {entity.position.x, entity.position.y}
                );
            if (entityDistance < World.NPC_EAR_STRENGTH && entity instanceof Npc) {
              if (entity instanceof Infiltrator) {
                Infiltrator infiltrator = (Infiltrator) entity;

                // Exposed infiltrators shouldn't flee
                // remove the infiltrator from enemyTrack
                if (infiltrator.exposed) {
                  GameScreen.enemyTrack.remove(infiltrator);
                  continue;
                }
              }
              Npc npc = (Npc) entity;
              npc.navigateToNearestFleepoint(world);
            }
          }

          playerTimer.scheduleTask(new Task() {
            @Override
            public void run() {
              teleporterRayCoordinates.setZero();
            }
          }, World.AUBER_RAY_TIME);
        } else {
          world.auberTeleporterCharge = Math.max(world.auberTeleporterCharge
              - World.AUBER_CHARGE_RATE, 0f);
        }
      }
      if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
        teleport();
      }

      Vector2 mousePosition = Utils.getMouseCoordinates(world.camera);

      // Set the rotation to the angle theta where theta is the angle between the mouse cursor and
      // player position. Correct the player position to be measured from the centre of the sprite.
      rotation = (float) (Math.toDegrees(Math.atan2(
              (mousePosition.y - getCenterY()),
              (mousePosition.x - getCenterX()))
            ) - 90f);

      // Handle the confused debuff
      if (confused) {
        velocity.set(-velocity.x, -velocity.y);
      }

      move(velocity, World.map);  
    }
  }

  /**
   * Teleports the player to the medbay.
   */
  public void teleportToMedbay() {
    position.set(World.MEDBAY_COORDINATES[0], World.MEDBAY_COORDINATES[1]);
    confused = false;
    slowed = false;
    teleporterRayCoordinates.setZero();
  }

  /**
   * Teleports player to the next teleporter if the player is on a telepad.  
   */
  public void teleport() {
    // Interact with an object
    RectangleMapObject nearbyObject = getNearbyObjects(World.map);

    if (nearbyObject != null) {
      MapProperties properties = nearbyObject.getProperties();
      String type = properties.get("type", String.class);

      switch (type) {
        case "teleporter":
          MapObjects objects = World.map.getLayers().get("object_layer").getObjects();

          String linkedTeleporterId = properties.get("linked_teleporter", String.class);
          RectangleMapObject linkedTeleporter = (RectangleMapObject) objects.get(
              linkedTeleporterId
              );
          velocity.setZero();
          position.x = linkedTeleporter.getRectangle().getX();
          position.y = linkedTeleporter.getRectangle().getY();
          break;

        default:
          break;
      }
    }
  }

  /**
   * Overrides the GameEntity render method to render the player's teleporter raygun, as well
   * as the player itself.
   *
   * @param batch The batch to draw to
   * @param camera The world's camera
   * */
  @Override
  public void render(Batch batch, Camera camera) {
    if (!teleporterRayCoordinates.isZero()) {
      batch.end();
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      rayRenderer.setProjectionMatrix(camera.combined);
      rayRenderer.begin(ShapeType.Filled);
      rayRenderer.rectLine(getCenterX(), getCenterY(),
          teleporterRayCoordinates.x, teleporterRayCoordinates.y, 0.5f,
          World.rayColorA, World.rayColorB);
      rayRenderer.end();

      batch.begin();
    }
    super.render(batch, camera);
  }

  /**
   * Handle teleporter ray collisions and return the coordinates of the object it collides with.
   *
   * @param world The game world
   * @return The coordinates the ray hit
   * */
  private Vector2 handleRayCollisions(World world) {
    Vector2 output = new Vector2();

    Vector2 targetCoordinates = new Vector2(Utils.getMouseCoordinates(world.camera));
    float alpha = 0.1f;
    boolean rayIntersected = false;
    // Allow the ray to go 20x the distance between the mouse and player,
    // prevents game from hanging if ray escapes map
    while (!rayIntersected && alpha < 20) {
      output.x = getCenterX();
      output.y = getCenterY();

      output.lerp(targetCoordinates, alpha);

      // Check for entity collisions
      for (GameEntity entity : world.getEntities()) {
        if (!(entity instanceof Player)) {
          if (entity.sprite.getBoundingRectangle().contains(output)) {
            rayIntersected = true;
            if (entity instanceof Npc) {
              Npc npc = (Npc) entity;
              npc.handleTeleporterShot(world);
            }
            break;
          }
        }
      }

      // Check for tile collisions
      TiledMapTileLayer collisionLayer = (TiledMapTileLayer) World.map.getLayers()
          .get("collision_layer");
      Cell targetCell = collisionLayer.getCell(
          (int) output.x / collisionLayer.getTileWidth(),
          (int) output.y / collisionLayer.getTileHeight()
      );
      if (targetCell != null) {
        rayIntersected = true;
      }
      alpha += 0.1f;
    }
    return output;
  }
}
