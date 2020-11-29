package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    protected Stage stage;
    protected Game game;
    protected World world;
    private Box2DDebugRenderer debugRenderer;
    protected Ball ball;

    protected static int playersAmount = 5;
    protected float timeBetweenUpdates = 0.1f;

    protected float timeSinceLastUpdate = 0;
    protected Player[] playerList;

    protected GameScreen(Game aGame) {
        game = aGame;

        Gdx.input.setInputProcessor(stage);

        stage = new Stage(new FitViewport(SCENE_WIDTH, SCENE_HEIGHT,  new OrthographicCamera(SCENE_WIDTH, SCENE_HEIGHT)));

        debugRenderer = new Box2DDebugRenderer();

        world = new World(new Vector2(0, 0), true);

        createStage();

        initializeGame();

    }

    private void createStage() {
        ball = new Ball(world, SCENE_WIDTH/5,SCENE_HEIGHT/2);
        stage.addActor(ball);
        stage.addActor(new Floor(world,0,0, SCENE_WIDTH,2,-30));
        stage.addActor(new Floor(world,0,0, 2, Gdx.graphics.getHeight(),-30));
        stage.addActor(new Floor(world,SCENE_WIDTH - 2, 0, 2, SCENE_HEIGHT,-30));
        stage.addActor(new Floor(world,0,SCENE_HEIGHT - 2, SCENE_WIDTH, 2 ,-30));
    }

    @Override
    public void show() {
        Gdx.app.log("MainScreen","show");
    }

    @Override
    public void render(float delta) {

        update(delta);

        //System.out.println(Gdx.graphics.getFramesPerSecond());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        debugRenderer.render(world, stage.getCamera().combined);

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
    }
}