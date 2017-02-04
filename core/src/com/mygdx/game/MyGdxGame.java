package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.mygdx.game.screens.EndLevel;
import com.mygdx.game.screens.MainMenu;
import com.mygdx.game.screens.SignIn;

import java.util.ArrayList;


public class MyGdxGame extends Game {

    public SpriteBatch batch;
    //Texture img;
    private com.mygdx.game.screens.GameScreen gameScreen;
    private MainMenu mainMenu;
    private SignIn signIn;
    private EndLevel endLevel;
    public OrthographicCamera camera;

    public static final int WIDTH = 1080;
    public static final int HEIGHT = 1760;

    public ArrayList highscores1;
    public ArrayList highscores2;
    public ArrayList highscores3;
    public ArrayList highscores4;
    public ArrayList highscores5;

    public int levelID;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public int getLevelID() {
        return levelID;
    }

    public void setLevelID(int levelID) {
        this.levelID = levelID;
    }


    @Override
    public void create () {
        camera = new OrthographicCamera();
        camera.setToOrtho(false,WIDTH,HEIGHT);

        batch = new SpriteBatch();

        highscores1 = new ArrayList<HighScore>();
        highscores2 = new ArrayList<HighScore>();
        highscores3 = new ArrayList<HighScore>();
        highscores4 = new ArrayList<HighScore>();
        highscores5 = new ArrayList<HighScore>();


        gameScreen = new com.mygdx.game.screens.GameScreen(this);
        mainMenu = new MainMenu(this);
        signIn = new SignIn(this);
        endLevel = new EndLevel(this);

        levelID = 1;
        playerName = "";

        setScreen(signIn);
        Gdx.app.log("Game","created");
    }

    @Override
    public void render () {
		/*Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();*/

        super.render();
    }

    @Override
    public void dispose () {
	/*	batch.dispose();
		img.dispose();
        gameScreen.dispose();
        mainMenu.dispose();*/
    }
}
