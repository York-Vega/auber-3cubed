package com.threecubed.auber.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.World;


public class GameUi {
  private static final int CHARGE_METER_WIDTH = 20;
  private static final int CHARGE_METER_MAX_HEIGHT = 100;
  private static final Vector2 CHARGE_METER_POSITION = new Vector2(50f, 50f);

  private static final Vector2 HEALTHBAR_POSITION = new Vector2(250f, 50f);
  private static final int HEALTHBAR_WIDTH = 20;
  private static final int HEALTHBAR_MAX_HEIGHT = 100;

  private static final Vector2 HEALTH_WARNINGS_POSITION = new Vector2(350f, 70f);
  private static final Vector2 POWER_NAME_POSITION = new Vector2(50f, 150f);
  private static final Vector2 SYSTEM_WARNINGS_POSITION = new Vector2(1750f, 50f);

  private ShapeRenderer shapeRenderer = new ShapeRenderer();

  private Sprite arrowSprite;
  private Color blindedColor = new Color(0f, 0f, 0f, 1f);

  private BitmapFont uiFont = new BitmapFont();

  public GameUi(AuberGame game) {
    arrowSprite = game.atlas.createSprite("arrow2");
  }

  /**
   * Render the different elements of the UI to the screen.
   *
   * @param world The game world.
   * @param screenBatch The batch to draw the UI to
   * */
  public void render(World world, SpriteBatch screenBatch) {
    if (world.player.blinded) {
      shapeRenderer.begin(ShapeType.Filled);
      shapeRenderer.setColor(blindedColor);
      shapeRenderer.rect(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      shapeRenderer.setColor(Color.WHITE);
      shapeRenderer.end();
    }

    drawChargeMeter(world, screenBatch);
    drawHealthbar(world, screenBatch);
    drawHealthWarnings(world, screenBatch);
    drawSystemWarnings(world, screenBatch);
    drawPowerName(world, screenBatch);
  }

  /**
   * Draw the teleporter charge meter.
   *
   * @param world The game world
   * @param screenBatch The batch to draw to
   * */
  private void drawChargeMeter(World world, SpriteBatch screenBatch) {
    screenBatch.begin();
    uiFont.draw(screenBatch, "Teleporter Ray Charge",
        CHARGE_METER_POSITION.x,
        CHARGE_METER_POSITION.y + (CHARGE_METER_MAX_HEIGHT / 2));
    screenBatch.end();

    float chargeMeterHeight = world.auberTeleporterCharge * CHARGE_METER_MAX_HEIGHT;
    
    // Make the charge meter green if weapon is charged
    if (chargeMeterHeight > CHARGE_METER_MAX_HEIGHT * 0.95f) {
      shapeRenderer.setColor(Color.GREEN);
    } else {
      shapeRenderer.setColor(Color.RED);
    }

    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.rect(CHARGE_METER_POSITION.x + 160f, CHARGE_METER_POSITION.y, CHARGE_METER_WIDTH,
        chargeMeterHeight);
    shapeRenderer.end();
  }

  private void drawHealthbar(World world, SpriteBatch screenBatch) {
    screenBatch.begin();
    uiFont.draw(screenBatch, "Health",
        HEALTHBAR_POSITION.x,
        HEALTHBAR_POSITION.y + (HEALTHBAR_MAX_HEIGHT / 2));
    screenBatch.end();

    float healthbarHeight = world.player.health * HEALTHBAR_MAX_HEIGHT;

    // Set the healthbar colour based on amount of health
    if (healthbarHeight > CHARGE_METER_MAX_HEIGHT * 0.8f) {
      shapeRenderer.setColor(Color.GREEN);
    } else if (healthbarHeight > CHARGE_METER_MAX_HEIGHT * 0.5) {
      shapeRenderer.setColor(Color.ORANGE);
    } else {
      shapeRenderer.setColor(Color.RED);
    }

    shapeRenderer.begin(ShapeType.Filled);
    shapeRenderer.rect(HEALTHBAR_POSITION.x + 60f, HEALTHBAR_POSITION.y, HEALTHBAR_WIDTH,
        healthbarHeight);
    shapeRenderer.end();
  }

  /**
   * Draw any health status warnings for Auber to the screen.
   *
   * @param world The game world
   * @param screenBatch The batch to draw to
   * */
  private void drawHealthWarnings(World world, SpriteBatch screenBatch) {
    screenBatch.begin();
    uiFont.setColor(Color.RED);
    if (world.player.confused) {
      uiFont.draw(screenBatch, "CONFUSED", HEALTH_WARNINGS_POSITION.x, HEALTH_WARNINGS_POSITION.y);
    }
    if (world.player.slowed) {
      uiFont.draw(screenBatch, "SLOWED", HEALTH_WARNINGS_POSITION.x,
          HEALTH_WARNINGS_POSITION.y + 20f);
    }
    if (world.player.blinded) {
      uiFont.draw(screenBatch, "BLINDED", HEALTH_WARNINGS_POSITION.x,
          HEALTH_WARNINGS_POSITION.y + 40f);
    }
    uiFont.setColor(Color.WHITE);
    screenBatch.end();
  }

  /**
   * Draw any power up name for Auber to the screen.
   *
   * @param world The game world
   * @param screenBatch The batch to draw to
   * */
  private void drawPowerName(World world, SpriteBatch screenBatch) {
    screenBatch.begin();
    uiFont.setColor(Color.GREEN);
    if (world.player.immune) {
      uiFont.draw(screenBatch, "IMMUNITY", POWER_NAME_POSITION.x, POWER_NAME_POSITION.y);
    }
    if (world.player.invisible) {
      uiFont.draw(screenBatch, "INVISIBILITY", POWER_NAME_POSITION.x,
      POWER_NAME_POSITION.y + 20f);
    }
    if (world.player.firewall) {
      uiFont.draw(screenBatch, "FIREWALL", POWER_NAME_POSITION.x,
      POWER_NAME_POSITION.y + 40f);
    }
    if (world.player.speedUp) {
      uiFont.draw(screenBatch, "SPEED", POWER_NAME_POSITION.x,
      POWER_NAME_POSITION.y + 60f);
    }
    if (world.player.detect) {
      uiFont.draw(screenBatch, "DETECT", POWER_NAME_POSITION.x,
      POWER_NAME_POSITION.y + 80f);
    }
    uiFont.setColor(Color.WHITE);
    screenBatch.end();
  }

  /**
   * Draw the state of each system to the screen.
   *
   * @param world The world object
   * @param screenBatch The batch to draw to
   * */
  private void drawSystemWarnings(World world, SpriteBatch screenBatch) {
    screenBatch.begin();
    int offset = 0;
    for (RectangleMapObject system : World.systems) {
      Rectangle systemRectangle = system.getRectangle();
      Vector2 systemAngleVector = new Vector2(systemRectangle.getX() - world.player.position.x,
                                              systemRectangle.getY() - world.player.position.y);

      arrowSprite.setPosition(SYSTEM_WARNINGS_POSITION.x - 20f, SYSTEM_WARNINGS_POSITION.y
          + offset - 10f);
      arrowSprite.setRotation(systemAngleVector.angleDeg() - 90f);
      arrowSprite.draw(screenBatch);

      String systemName = system.getName();
      // No need for DESTROYED case as when system destroyed, removed from systems list
      switch (world.getSystemState(system)) {
        case WORKING:
          uiFont.setColor(Color.GREEN);
          break;
        case ATTACKED:
          uiFont.setColor(Color.RED);
          break;
        default:
          break;
      }

      uiFont.draw(screenBatch, systemName, SYSTEM_WARNINGS_POSITION.x,
          SYSTEM_WARNINGS_POSITION.y + offset);
      offset += 25f;

    }
    uiFont.setColor(Color.WHITE);
    screenBatch.end();
  }
}
