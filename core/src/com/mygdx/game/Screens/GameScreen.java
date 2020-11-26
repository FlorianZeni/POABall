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

    private static final float SCENE_WIDTH = 120;
    private static final float SCENE_HEIGHT = 70;

    private Stage stage;
    private Game game;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player1;
    private Player player2;
    private Ball ball;
//    private BallContactListener contactListener;

    public GameScreen(Game aGame) {
        game = aGame;
        Gdx.input.setInputProcessor(stage);

        stage = new Stage(new FitViewport(SCENE_WIDTH, SCENE_HEIGHT,  new OrthographicCamera(SCENE_WIDTH, SCENE_HEIGHT)));
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        player1 = new Player(world,SCENE_WIDTH/4,SCENE_HEIGHT/2);
        player2 = new Player(world,SCENE_WIDTH/4 + 5,SCENE_HEIGHT/2 + 5);
        ball = new Ball(world, SCENE_WIDTH/5,SCENE_HEIGHT/2);

        stage.addActor(player1);
        stage.addActor(player2);
        stage.addActor(ball);
//        contactListener = new BallContactListener();
//        world.setContactListener(contactListener);
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


    private void update(float deltaTime) {
        Vector2 keys = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            keys.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            keys.x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            keys.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            keys.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) & player1.getBallDistance(ball) < 6)//& contactListener.isTouching)
            ball.shoot(player1.body.getPosition());

        player1.move(keys, deltaTime);
        stage.act();
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