package com.threecubed.auber.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.AuberGame;
import com.threecubed.auber.Difficulty;
import com.threecubed.auber.World;
import com.threecubed.auber.Difficulty.Mode;
import com.threecubed.auber.ui.Button;


/**
 * The menu screen is the first screen that shows in the game and is responsible for controlling
 * when the game begins.
 *
 * @author Joseph Krystek-Walton
 * @version 1.0
 * @since 1.0
 * */
public class MenuScreen extends ScreenAdapter {
  World world;
  AuberGame game;

  Button playButton;
  Button demoButton;
  Button continueButton;
  Button difficultyButton;
  OrthogonalTiledMapRenderer renderer;
  Sprite background;
  Sprite instructions;
  Sprite title;
  SpriteBatch spriteBatch;
  public static boolean continueGame = false;
  Difficulty.Mode difficulty; 
  Boolean isDifficultyButtonTouched = false; 
  ArrayList<Sprite> difficultySprites; 

  /**
   * Instantiate the screen with the {@link AuberGame} object. Set the title and button up to be
   * rendered.
   *
   * @param game The game object
   * */
  public MenuScreen(final AuberGame game) {
    this.game = game;
    difficulty = Difficulty.Mode.EASY;

    spriteBatch = new SpriteBatch();

    background = game.atlas.createSprite("stars");
    instructions = game.atlas.createSprite("instructions");
    title = game.atlas.createSprite("auber_logo");

    Runnable onPlayClick = new Runnable() {
      @Override
      public void run() {
        continueGame = false;
        game.setScreen(new GameScreen(game, false, difficulty));
      }
    };

    playButton = new Button(
        new Vector2(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f - 150),
        1f, game.atlas.createSprite("playButton"), game, onPlayClick);

    Runnable onDemoClick = new Runnable() {
      @Override
      public void run() {
        continueGame = false;
        game.setScreen(new GameScreen(game, true, difficulty));
      }
    };

    demoButton = new Button(
        new Vector2(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f - 2 * 150f),
        1f, game.atlas.createSprite("demoButton"), game, onDemoClick);

    Runnable onContinueClick = new Runnable() {
      @Override
      public void run() {
        Preferences pre = Gdx.app.getPreferences("aubergame");
        continueGame = pre.getString("markForSaving", null) != null;
        game.setScreen(new GameScreen(game, false, difficulty));

      }
    };

    continueButton = new Button(
            new Vector2(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f - 3 * 150f),
            1f, game.atlas.createSprite("continueButton"), game, onContinueClick);

    Runnable onDifficultyClick = new Runnable() {
      @Override
      public void run() {        
        if (!isDifficultyButtonTouched) {
          isDifficultyButtonTouched = true;
          difficulty = difficulty.next();
          difficultyButton.setSprite(difficultySprites.get(difficulty.getValue())); 
        }       
      }
    };

    difficultySprites = new ArrayList<Sprite>();
    difficultySprites.add(game.atlas.createSprite("easyButton"));
    difficultySprites.add(game.atlas.createSprite("mediumButton"));
    difficultySprites.add(game.atlas.createSprite("hardButton"));

    difficultyButton = new Button(
      new Vector2(Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight() / 2f),
      1f, difficultySprites.get(difficulty.getValue()), game, onDifficultyClick);

  }

  @Override
  public void render(float deltaTime) {
    if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
      DisplayMode currentDisplayMode = Gdx.graphics.getDisplayMode();
      Gdx.graphics.setFullscreenMode(currentDisplayMode);
    }
    if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
      game.setScreen(new GameScreen(game, true, difficulty));
    }
    if (!Gdx.input.isTouched()) {
      isDifficultyButtonTouched = false;
    }

    // Set the background color
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    spriteBatch.begin();

    background.setPosition(0f, 0f);
    background.draw(spriteBatch);

    instructions.setPosition(900f, 125f);
    instructions.draw(spriteBatch);

    title.setScale(0.5f);
    title.setPosition(0f, 550f);
    title.draw(spriteBatch);

    playButton.render(spriteBatch);
    demoButton.render(spriteBatch);
    continueButton.render(spriteBatch);
    difficultyButton.render(spriteBatch);

    spriteBatch.end();
  }
}
