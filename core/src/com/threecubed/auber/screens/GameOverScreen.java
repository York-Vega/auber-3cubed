package com.threecubed.auber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.threecubed.auber.AuberGame;


/**
 * The game over screen is the screen that the game is set to when a win/lose condition has been
 * reached.
 *
 * @author Joseph Krystek-Walton
 * @version 1.0
 * @since 1.0
 * */
public class GameOverScreen extends ScreenAdapter {
  AuberGame game;

  BitmapFont font = new BitmapFont();
  SpriteBatch batch = new SpriteBatch();

  public GameOverScreen(AuberGame game) {
    this.game = game;
  }

  @Override
  public void render(float deltaTime) {
    // Set the background color
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    batch.begin();
    font.draw(batch, "GAME OVER", 200f, 200f);
    batch.end();
  }
}
