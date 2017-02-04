package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Florin on 29.01.2017.
 */

public class SignIn implements Screen {

    private Stage stage;
    private Table table;
    private TextButton submit;
    private Label heading;
    private TextureAtlas atlas;
    private Skin skin;
    public BitmapFont font;
    private final MyGdxGame game;
    private TextField textField;
    private GameScreen gameScreen;
    private MainMenu mainMenu;
    boolean marker;

    public SignIn(final MyGdxGame game){
        this.game = game;
        stage = new Stage(new FitViewport(game.WIDTH,game.HEIGHT,game.camera));
    }

    @Override
    public void show() {

        marker = false;

        mainMenu = new MainMenu(game);

        Gdx.input.setInputProcessor(stage);

        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("ui/Roboto-Regular.ttf"));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 70;

        font = fontGenerator.generateFont(parameter);

       // font = new BitmapFont(Gdx.files.internal("ui/Roboto-Thin-ldpi.fnt"));
       // atlas = new TextureAtlas(Gdx.files.internal("ui/Holo-dark-ldpi.atlas"));
        skin = new Skin(Gdx.files.internal("ui/Holo-dark-ldpi.json"));

        skin.getFont("default-font").getData().setScale(4.5f,4.5f);

        table = new Table(skin);
        table.setSize(stage.getWidth(), stage.getHeight());
        table.align(Align.center);
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Label.LabelStyle style1 = new Label.LabelStyle(font, Color.WHITE);

        heading = new Label("Please enter your name:",style1);
       // heading.setFontScale(5.0f);

        table.add(heading).spaceBottom(100.0f);
        table.row();

        TextField.TextFieldStyle style2 = new TextField.TextFieldStyle();
        style2.font = font;
        style2.fontColor = Color.WHITE;
        style2.cursor = skin.getDrawable("textfield_cursor");
        style2.cursor.setMinWidth(2.0f);
        style2.selection = skin.getDrawable("textfield_selection");
        //TODO set color
        style2.background = skin.getDrawable("textfield_disabled");

        textField = new TextField("",skin);
        textField.setColor(Color.WHITE);
        textField.setCursorPosition(0);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = skin.getDrawable("btn_default_normal");
        style.down = skin.getDrawable("btn_default_pressed");

        submit = new TextButton("OK", style);
        //submit.getLabel().setFontScale(3.5f);

        table.add(textField).size(stage.getWidth() - 100.0f ,200.0f).spaceBottom(20.0f);
        table.row();
        table.add(submit).size(stage.getWidth(),200.0f);

        submit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(textField.getText() != ""){
                game.setScreen(mainMenu);
                game.setPlayerName(textField.getText());
                } else if(!marker){
                    table.row();
                    table.add("Don't leave the textfield empty","default-font",Color.RED);
                    marker = true;
                }
            }
        });

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
