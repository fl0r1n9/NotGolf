package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.HighScore;
import com.mygdx.game.MyGdxGame;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.ArrayList;

/**
 * Created by Florin on 29.01.2017.
 */

public class EndLevel implements Screen {

    private Stage stage;
    private Table table;
    private TextButton proceed, back;
    private Label congrats, time, deaths, highscore;
    private TextureAtlas atlas;
    private Skin skin,skindef;
    public BitmapFont font;
    private final MyGdxGame game;
    private GameScreen gameScreen;
    private ArrayList array;

   public EndLevel(MyGdxGame game){
       this.game = game;
       stage = new Stage(new FitViewport(game.WIDTH,game.HEIGHT,game.camera));
   }

    @Override
    public void show() {

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Roboto-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;

        font = fontGenerator.generateFont(parameter);

        gameScreen = new GameScreen(game);

        Gdx.input.setInputProcessor(stage);

       // font = new BitmapFont(Gdx.files.internal("ui/Roboto-Thin-ldpi.fnt"));
        atlas = new TextureAtlas(Gdx.files.internal("ui/Holo-dark-ldpi.atlas"));

        skin = new Skin(atlas);
        skindef = new Skin(Gdx.files.internal("ui/Holo-dark-ldpi.json"));

        skindef.getFont("default-font").getData().setScale(4.0f,4.0f);

        table = new Table(skindef);
        table.setSize(stage.getWidth(), stage.getHeight());
        table.align(Align.center);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Label.LabelStyle style1 = new Label.LabelStyle(font, Color.WHITE);

        congrats = new Label("Stage clear!",style1);
       // congrats.setFontScale(6.0f);

        time = new Label("Your time: " + gameScreen.currentStats.getFloat("time")/1000.0 + " sec.", style1);
        //time.setFontScale(4.0f);

        deaths = new Label("Restarts: " + gameScreen.currentStats.getInteger("deaths"), style1);
       // deaths.setFontScale(4.0f);

        highscore = new Label("High scores for this stage:", style1);
        //highscore.setFontScale(4.0f);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = font;
        style.up = skin.getDrawable("btn_default_normal");
        style.down = skin.getDrawable("btn_default_pressed");

        proceed = new TextButton("NEXT STAGE", style);
        back = new TextButton("RETURN TO STAGE SELECT",style);

      //  proceed.getLabel().setFontScale(3.5f);
      //  back.getLabel().setFontScale(3.5f);

        proceed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLevelID(game.getLevelID()+1);
                game.setScreen(new GameScreen(game));
            }
        });

        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });


        table.add(congrats);
        table.row();
        table.add(time);
        table.row();
        table.add(deaths);
        table.row();
        table.add(highscore);
        table.row();

        array = new ArrayList<HighScore>();

        switch (game.getLevelID()) {
            case 1:
                array = game.highscores1;
                break;
            case 2:
                array = game.highscores2;
                break;
            case 3:
                array = game.highscores3;
                break;
            case 4:
                array = game.highscores4;
                break;
            case 5:
                array = game.highscores5;
                break;
        }

        int position = 1;

        for (int i = 0; i < array.size(); i++) {
            HighScore h = (HighScore) array.get(i);
            String entry = position + ". " + h.getPlayerName() + "   " + h.getTime() / 1000.0 + " sec.";
            table.add(entry);
            table.row();
            ++position;
            if (position == 11) {
                break;
            }
        }

        table.row();
        if(game.getLevelID() <= 4) {
            table.add(proceed).size(stage.getWidth(), 200.0f);
        }
        table.row();
        table.add(back).size(stage.getWidth(),200.0f);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        stage.dispose();
        atlas.dispose();
        skin.dispose();
    }
}
