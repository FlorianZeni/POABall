package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.Bodies.Ball;
import com.mygdx.game.Bodies.Floor;
import com.mygdx.game.Bodies.Player;

public class GameScreen implements Screen {

    protected static final float SCENE_WIDTH = 120;
    protected static final float SCENE_HEIGHT = 70;

    private final int goalSize = 20;

    protected Stage stage;
    protected Game game;
    protected World world;
   // private Box2DDebugRenderer debugRenderer;
    protected Ball ball;

    private BitmapFont font = new BitmapFont();
    private Batch batch = new SpriteBatch();

    protected static int playersAmount = 4;
    protected float timeBetweenUpdates = 0.05f;

    protected float timeSinceLastUpdate = 0;
    protected Player[] playerList;

    protected int scoreRed = 0;
    protected int scoreBlue = 0;

    protected GameScreen(Game aGame) {
        game = aGame;

        Gdx.input.setInputProcessor(stage);

        stage = new Stage(new FitViewport(SCENE_WIDTH, SCENE_HEIGHT,  new OrthographicCamera(SCENE_WIDTH, SCENE_HEIGHT)));

        //debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, 0), true);

        createStage();

        initializeGame();

    }

    private void createStage() {
        ball = new Ball(world, SCENE_WIDTH/5,SCENE_HEIGHT/2);
        stage.addActor(ball);
        stage.addActor(new Floor(world,0,0, SCENE_WIDTH,2,0));

        stage.addActor(new Floor(world,0,0, 2, (SCENE_HEIGHT - goalSize) / 2,0));
        stage.addActor(new Floor(world,0,(SCENE_HEIGHT + goalSize) / 2, 2, (SCENE_HEIGHT - goalSize) / 2,0));

        stage.addActor(new Floor(world, -7,(SCENE_HEIGHT - goalSize) / 2, 2, goalSize,0));

        stage.addActor(new Floor(world,SCENE_WIDTH - 2, 0, 2, (SCENE_HEIGHT - goalSize) / 2,0));
        stage.addActor(new Floor(world,SCENE_WIDTH - 2, (SCENE_HEIGHT + goalSize) / 2, 2, (SCENE_HEIGHT - goalSize) / 2,-30));

        stage.addActor(new Floor(world,SCENE_WIDTH + 5, (SCENE_HEIGHT - goalSize) / 2, 2, goalSize, 0));


        stage.addActor(new Floor(world,0,SCENE_HEIGHT - 2, SCENE_WIDTH, 2 ,0));
    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
    }

    @Override
    public void render(float delta) {

        update(delta);

//        System.out.println(Gdx.graphics.getFramesPerSecond());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        batch.begin();
        font.draw(batch, "RED  -   " + scoreRed +"  :  " + scoreBlue + "   -  BLUE", 320, 480);
        batch.end();

        //debugRenderer.render(world, stage.getCamera().combined);

        world.step(delta, 3, 3);
    }


    protected void update(float deltaTime) {

        stage.act();
    }

    private void initializeGame(){
        playerList = new Player[playersAmount];
        for(int i = 0; i < playersAmount; i++){
            playerList[i] = new Player(world,SCENE_WIDTH/4,SCENE_HEIGHT/4 + 10 * i);
            stage.addActor(playerList[i]);
        }
    }

    protected boolean checkForGoal(){
        if(ball.getPosition().x < 0){
            scoreRed += 1;
            return true;
        }
        else if(ball.getPosition().x > SCENE_WIDTH){
            scoreBlue += 1;
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        dispose();
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
        font.dispose();
        batch.dispose();
        world.dispose();
        Gdx.app.exit();
    }
}