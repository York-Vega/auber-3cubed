package com.threecubed.auber;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.threecubed.auber.screens.MenuScreen;

public class AuberGame extends Game {
  public TextureAtlas atlas;

  @Override
  public void create() {
    atlas = new TextureAtlas("auber_.atlas");
    Gdx.graphics.setWindowedMode(1920, 1080);
    setScreen(new MenuScreen(this));
  }

  @Override
  public void dispose() {
    atlas.dispose();
  }
}
