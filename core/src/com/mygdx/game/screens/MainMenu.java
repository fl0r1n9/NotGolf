package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

import java.awt.Font;

/**
 * Created by Florin on 23.01.2017.
 */

public class MainMenu implements Screen {

    private Stage stage;
    private Table table;
    private TextButton button1, button2, button3, button4,button5;
    private Label heading;
    private TextureAtlas atlas;
    private Skin skin;
    public BitmapFont font;
    private final MyGdxGame game;
    private GameScreen gameScreen;

    public MainMenu(final MyGdxGame game){
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
        table = new Table(skin);
        table.setSize(stage.getWidth(), stage.getHeight());
        table.align(Align.center);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage.addActor(table);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();

        style.font = font;
        style.up = skin.getDrawable("btn_default_normal");
        style.down = skin.getDrawable("btn_default_pressed");

        button1 = new TextButton("VERY EASY", style);
        button2 = new TextButton("EASY", style);
        button3 = new TextButton("MEDIUM", style);
        button4 = new TextButton("HARD", style);
        button5 = new TextButton("VERY HARD", style);

       /* button1.getLabel().setFontScale(3.5f);
        button2.getLabel().setFontScale(3.5f);
        button3.getLabel().setFontScale(3.5f);
        button4.getLabel().setFontScale(3.5f);
        button5.getLabel().setFontScale(3.5f);*/

        button1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLevelID(1);
                game.setScreen(new GameScreen(game));

            }
        });

        if(gameScreen.beaten.getBoolean("beaten1")){
        button2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLevelID(2);
                game.setScreen(new GameScreen(game));
            }
        });}else {
            button2.setColor(Color.DARK_GRAY);
        }

        if(gameScreen.beaten.getBoolean("beaten2")){
        button3.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLevelID(3);
                game.setScreen(new GameScreen(game));
            }
        });} else {
            button3.setColor(Color.DARK_GRAY);
        }

        if(gameScreen.beaten.getBoolean("beaten3")) {
            button4.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setLevelID(4);
                    game.setScreen(new GameScreen(game));
                }
            });
        } else {
            button4.setColor(Color.DARK_GRAY);
        }
        if(gameScreen.beaten.getBoolean("beaten4")){
        button5.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLevelID(5);
                game.setScreen(new GameScreen(game));
            }
        });} else {
            button5.setColor(Color.DARK_GRAY);
        }

        Label.LabelStyle style1 = new Label.LabelStyle(font, Color.WHITE);

        heading = new Label("Stage Select",style1);
      //  heading.setFontScale(6.0f);

        table.add(heading).spaceBottom(200.0f);
        table.row();
        table.add(button1).size(stage.getWidth(),200.0f).spaceBottom(50.0f);
        table.row();
        table.add(button2).size(stage.getWidth(),200.0f).spaceBottom(50.0f);
        table.row();
        table.add(button3).size(stage.getWidth(),200.0f).spaceBottom(50.0f);
        table.row();
        table.add(button4).size(stage.getWidth(),200.0f).spaceBottom(50.0f);
        table.row();
        table.add(button5).size(stage.getWidth(),200.0f).spaceBottom(50.0f);

        stage.addActor(table);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            game.setScreen(new SignIn(game));
        }

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
