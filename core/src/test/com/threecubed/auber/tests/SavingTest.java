package com.threecubed.auber.tests;


import com.badlogic.gdx.math.Vector2;
import com.threecubed.auber.*;
import com.threecubed.auber.entities.Infiltrator;
import com.threecubed.auber.entities.Player;
import com.threecubed.auber.screens.GameScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import java.util.HashMap;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


@RunWith(GdxTestRunner.class)
public class SavingTest {

  @Mock
  World worldMock = mock(World.class);
  @Mock
  Player playerMock = mock(Player.class);
  @Mock
  Infiltrator infiltratorMock = mock(Infiltrator.class);

  @Test
  public void playerSaveTest() throws Exception{

     String testFile = "test";
     worldMock.player = playerMock;
     playerMock.health = 1f;
     playerMock.position = new Vector2(100,100);
     playerMock.confused = true;
     playerMock.blinded = true;
     playerMock.slowed = true;
     DataManager dataManager = new DataManager(testFile);
     dataManager.savePlayerData(worldMock);

    assertEquals("Player Health not saved correctly ",
            dataManager.preferences.getFloat("health"), playerMock.health, 0.0);
    assertEquals("Player positionX not saved correctly ",
            dataManager.preferences.getFloat("PlayerPositionX"), playerMock.position.x, 0.0);
    assertEquals("Player positionY not saved correctly ",
            dataManager.preferences.getFloat("PlayerPositionY"), playerMock.position.y, 0.0);
    assertEquals("Player confused status not saved correctly ",
            dataManager.preferences.getBoolean("confused"), playerMock.confused);
    assertEquals("Player blinded status not saved correctly ",
            dataManager.preferences.getBoolean("blinded"), playerMock.blinded);
    assertEquals("Player slowed status not saved correctly ",
            dataManager.preferences.getBoolean("slowed"), playerMock.slowed);
  }


  @Test
  public void infiltratorSaveTest() throws Exception{

    String testFile = "test";
    Integer infiltratorNo = 1;
    HashMap<Infiltrator,Integer> enemyTrack = new HashMap<>();

    infiltratorMock.exposed = true;
    infiltratorMock.position = new Vector2(50f,50f);
    enemyTrack.put(infiltratorMock,infiltratorNo);

    GameScreen.enemyTrack = enemyTrack;
    DataManager dataManager = new DataManager(testFile);
    dataManager.saveInfiltratorData();

    assertEquals("Infiltrator exposed status not saved correctly",
            dataManager.preferences.getBoolean("InfiltratorExposed" + infiltratorNo),
            infiltratorMock.exposed);
    assertEquals("Infiltrator position X not saved correctly",
            dataManager.preferences.getFloat("InfiltratorPositionX" + infiltratorNo),
            infiltratorMock.position.x, 0.0);
    assertEquals("Infiltrator position Y not saved correctly",
            dataManager.preferences.getFloat("InfiltratorPositionY" + infiltratorNo),
            infiltratorMock.position.y, 0.0);

  }


  @Test
  public void systemSaveTest() throws Exception {
    String testFile = "test";
    float testSystemPositionX = 120f;
    float testSystemPositionY = 120f;
    World.SystemStates testState = World.SystemStates.DESTROYED;
    HashMap<String, Enum<World.SystemStates>> testSystemStatesMap = new HashMap<>();
    testSystemStatesMap.put(Float.toString(testSystemPositionX) + "/" + Float.toString(testSystemPositionY),
            testState);
    World.systemStatesMap = testSystemStatesMap;

    DataManager dataManager = new DataManager(testFile);
    dataManager.saveSystemData();

    assertSame("System states not saved correctly",
            dataManager.loadingSystemData(testSystemPositionX, testSystemPositionY), testState);
  }


}
