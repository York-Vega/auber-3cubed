package com.threecubed.auber.tests;

import com.badlogic.gdx.Gdx;
import com.threecubed.auber.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class AssetsTest {

  static final String[] assets = { "auber_.atlas", "auber-0.png","auber-1.png",  "map.tmx",
          "packer.tpproj", "tileset.png", "tileset.tsx"}; // put all assets' paths which need to be tested

  @Test
  public void allAssetsExist() throws Exception{
    for(String asset : assets ) {
      assertTrue("Test failed, without finding asset : " + asset, Gdx.files
              .internal("assets/" + asset).exists());
    }

  }

}
