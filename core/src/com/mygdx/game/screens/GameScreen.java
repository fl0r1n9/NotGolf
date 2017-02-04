package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.HighScore;
import com.mygdx.game.objects.Ball;
import com.mygdx.game.objects.Goal;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.Obstacle;
import com.mygdx.game.objects.ObstacleObject;
import com.mygdx.game.objects.Wall;
import com.mygdx.game.objects.WallObject;
import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.input;

public class GameScreen implements Screen {

    private static final int VELOCITYITERATIONS = 8;
    private static final int POSITIONITERATIONS = 3;
    protected static final float CONVERTER = 40.0f;
    private final MyGdxGame game;
    public EndLevel endLevel;

    private Stage stage;
    public World world;
    public Box2DDebugRenderer renderer;

    ShapeRenderer shapeRenderer;

    private Vector2 movement = new Vector2();

    private Ball ball;
    private Obstacle obst;
    private Goal goal;
    private FileHandle file;

    private GameState gameState;
    public enum GameState{
        START,RUNNING,VICTORY
    }

    public int deathCounter, deaths;
    public long start;
    public long end;
    public float time;

    public ArrayList border;
    public ArrayList walls;
    public ArrayList obstacles;
    public ArrayList activeBalls;

    private SpriteBatch batch,batchBall,batchGoal;
    public Texture background;

    Preferences beaten = Gdx.app.getPreferences("Beaten");
    Preferences currentStats = Gdx.app.getPreferences("CurrentStats");

    public GameScreen(final MyGdxGame game){
        this.game = game;
        this.stage = new Stage(new FitViewport(game.WIDTH,game.HEIGHT, game.camera));
        gameState=GameState.START;

    }

    @Override
    public void show() {

        ShaderProgram.pedantic = false;

        world = new World(new Vector2(0.0f, 0.0f), true);

        renderer = new Box2DDebugRenderer();

        batch = new SpriteBatch();
        batchBall = new SpriteBatch();
        batchGoal = new SpriteBatch();
        background = new Texture(Gdx.files.internal("img/grass.png"));

        Gdx.input.setInputProcessor(stage);

        //dispose nicht vergessen
        border = new ArrayList<Wall>(4);
        //unten
        border.add(new Wall(0.0f, 0.0f  / CONVERTER, game.WIDTH / CONVERTER, 1.0f / CONVERTER, world));
        //oben
        border.add(new Wall(0.0f, game.HEIGHT / CONVERTER, game.WIDTH / CONVERTER, 1.0f / CONVERTER,world));
        //rechts
        border.add(new Wall(game.WIDTH / CONVERTER, 0.0f/ CONVERTER, 1.0f / CONVERTER, game.HEIGHT / CONVERTER,world));
        //links
        border.add(new Wall(0.0f / CONVERTER, 0.0f / CONVERTER, 1.0f/ CONVERTER, game.HEIGHT/ CONVERTER,world));

        activeBalls = new ArrayList<Ball>();
        walls = new ArrayList<Wall>();
        obstacles = new ArrayList<Obstacle>();


        ball = new Ball(5.0f,42.0f,0.75f,world);
        goal = new Goal(24.0f,1.5f,3.5f,2.0f,world);
        activeBalls.add(ball);

        deathCounter = 0;

        loadLevel();

    }

    @Override
    public void render(float delta) {

        //TODO prevent app from shutting down first
        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            game.setScreen(new MainMenu(game));
        }

       // Gdx.gl.glClearColor(0,0,0,1);
        //Fills the screen with the selected color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       //TODO scale to screen size
        batch.begin();
        batch.draw(background,0f,0f,game.WIDTH, game.HEIGHT + 30, 0,0,1024,994,false,false);
        batch.end();

        batchGoal.setProjectionMatrix(game.camera.combined.scl(CONVERTER));
        batchGoal.begin();
        if(goal.body.getUserData() instanceof Sprite){
            Sprite sprite2 = (Sprite) goal.body.getUserData();
            sprite2.setPosition(goal.body.getPosition().x - sprite2.getWidth() / 2, goal.body.getPosition().y - sprite2.getHeight() / 2);
            sprite2.draw(batchGoal);
        }
        batchGoal.end();

        game.camera.update();

        batchBall.setProjectionMatrix(game.camera.combined.scl(CONVERTER));
        batchBall.begin();
        if(ball.body.getUserData() instanceof Sprite){
            Sprite sprite = (Sprite) ball.body.getUserData();
            sprite.setPosition(ball.body.getPosition().x - sprite.getWidth() / 2, ball.body.getPosition().y - sprite.getHeight() / 2);
            sprite.setRotation(ball.body.getAngle() * MathUtils.radiansToDegrees);
            sprite.draw(batchBall);
        }
        batchBall.end();

        stage.draw();

        //renderer.render(world, game.camera.combined.scl(CONVERTER));

        switch (gameState){
            case START:
                updateStart(delta);
                gameState = GameState.RUNNING;
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case VICTORY:
                updateVictory(delta);
        }

        game.camera.update();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.camera.combined.scl(CONVERTER));

        for (int i = 0; i < walls.size(); i++) {

            Wall wall = (Wall) walls.get(i);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.OLIVE);
            shapeRenderer.rect(wall.body.getPosition().x-wall.getSizeXVal(),wall.body.getPosition().y-wall.getSizeYVal(),wall.getSizeXVal()*2,wall.getSizeYVal()*2);
            shapeRenderer.end();
        }
            game.camera.update();

        //TODO adjust form
        for (int i = 0; i < obstacles.size(); i++) {
           Obstacle obst = (Obstacle) obstacles.get(i);

            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(obst.getxVal(),obst.getyVal(),obst.getRadiusVal(),17);
            shapeRenderer.end();
        }
            game.camera.update();

    }


    public void loadLevel(){

        switch (game.getLevelID()){
            case 1:
                file = Gdx.files.internal("Level1.json");
                parseJson(file);
                break;
            case 2:
                file = Gdx.files.internal("Level2.json");
                parseJson(file);
                break;
            case 3:
                file = Gdx.files.internal("Level3.json");
                parseJson(file);
                break;
            case 4:
                file = Gdx.files.internal("Level4.json");
                parseJson(file);
                break;
            case 5:
                file = Gdx.files.internal("Level5.json");
                parseJson(file);
                break;
        }
    }

    private void parseJson(FileHandle file){

        WallObject obj;
        ObstacleObject obj2;

        JsonValue json = new JsonReader().parse(file);
        JsonValue wallsJson = json.get("walls");
        for(JsonValue wallJson : wallsJson.iterator()){
            obj = new WallObject();
            obj.x = wallJson.getFloat("x");
            obj.y = wallJson.getFloat("y");
            obj.sizeX = wallJson.getFloat("sizeX");
            obj.sizeY = wallJson.getFloat("sizeY");

            walls.add(new Wall(obj.x,obj.y,obj.sizeX,obj.sizeY,world));
        }
        JsonValue obstaclesJson = json.get("obstacles");
        for(JsonValue obstJson : obstaclesJson.iterator()){
            obj2 = new ObstacleObject();
            obj2.x = obstJson.getFloat("x");
            obj2.y = obstJson.getFloat("y");
            obj2.radius = obstJson.getFloat("radius");

            obstacles.add(new Obstacle(obj2.getX(),obj2.getY(),obj2.getRadius(),world));
        }
    }

    private void updateRunning(float delta){

        maneuver();
        world.step(delta, VELOCITYITERATIONS, POSITIONITERATIONS);
        stage.act(delta);
        ball.update(delta);

        if ((ball.body.getPosition().x >= 21.0f && ball.body.getPosition().y <= 2.0f)){

            end = TimeUtils.millis();
            time = (end - start);
            deaths = deathCounter;

            HighScore highScore = new HighScore(time,game.getPlayerName());

            currentStats.putInteger("deaths",deaths);
            currentStats.putFloat("time",time);
            currentStats.flush();

            switch(game.getLevelID()){
                case 1:
                    game.highscores1.add(highScore);
                    sort(game.highscores1);
                    //marker for beaten levels, save locally
                    beaten.putBoolean("beaten1",true);
                    beaten.flush();
                    break;
                case 2:
                    game.highscores2.add(highScore);
                    sort(game.highscores2);
                    beaten.putBoolean("beaten2",true);
                    beaten.flush();
                    break;
                case 3:
                    game.highscores3.add(highScore);
                    sort(game.highscores3);
                    beaten.putBoolean("beaten3",true);
                    beaten.flush();
                    break;
                case 4:
                    game.highscores4.add(highScore);
                    sort(game.highscores4);
                    beaten.putBoolean("beaten4",true);
                    beaten.flush();
                    break;
                case 5:
                    game.highscores5.add(highScore);
                    sort(game.highscores5);
                    beaten.putBoolean("beaten5",true);
                    beaten.flush();
                    break;
            }
            gameState = GameState.VICTORY;
        }

        for (int i = 0; i < obstacles.size(); i++) {
            obst = (Obstacle) obstacles.get(i);
            if (obst.collides(ball.getBounding())) {
                activeBalls.clear();
                world.destroyBody(ball.body);
                gameState = GameState.START;
                deathCounter++;
            }
        }

    }

    private void updateStart(float delta){
        start = TimeUtils.millis();
        if(activeBalls.size() == 0) {
            ball = new Ball(5.0f, 42.0f, 0.75f, world);
            activeBalls.add(ball);
        }
    }

    private void updateVictory(float delta) {
        game.setScreen(new EndLevel(game));
        deathCounter = 0;
    }

    private ArrayList<HighScore> sort(ArrayList<HighScore> list){
        boolean unsorted  = true;
        HighScore temp;

        while (unsorted){
            unsorted = false;
            for (int i = 0; i < list.size() - 1; i++) {
                if(list.get(i).getTime() > list.get(i+1).getTime()){
                    temp = list.get(i);
                    list.set(i,list.get(i+1));
                    list.set(i+1,temp);
                    unsorted = true;
                }
            }
        }
        return list;
    }

    private void maneuver(){

        /*
        Roll rechts: x++, x<360
        ""   links: x--, x>-360
        Pitch hoch: y--, y>-180
        Pitch unten: y++, y<180*/

        if(input.isPeripheralAvailable(Input.Peripheral.Compass)){
            movement.y = Gdx.input.getPitch() / CONVERTER / 2 ;
            movement.x = Gdx.input.getRoll() / CONVERTER / 2;
            ball.body.setLinearVelocity(ball.body.getLinearVelocity().add(movement));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,false);
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
        batch.dispose();
        shapeRenderer.dispose();
        world.dispose();
        renderer.dispose();
        stage.dispose();
        ball.sprite.getTexture().dispose();
        goal.sprite.getTexture().dispose();
    }
}