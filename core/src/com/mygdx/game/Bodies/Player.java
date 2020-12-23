package com.mygdx.game.Bodies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.Utils.Functions;


public class Player extends Image  {

    public Body body;
    private World world;
    private static int playerAmount = 1;

    public Player(World aWorld, float pos_x, float pos_y){
        super(new Texture("player" + playerAmount + ".png"));

        playerAmount ++;

        this.setPosition(pos_x,pos_y);
        this.setScale( 0.0125f, 0.0125f);

        world = aWorld;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 2.0f;
        bodyDef.bullet = true;
        bodyDef.position.set(pos_x, pos_y);


        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        CircleShape shape = new CircleShape();

        shape.setRadius(0.125f * 30);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution= 0f;
        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
    }


    public void move(Vector2 keysPressed, float deltaTime){
        float force = 5000;
        body.applyLinearImpulse(keysPressed.scl(force* deltaTime), body.getLocalCenter(), true);
    }

    public Vector2 getPosition(){
        return new Vector2(body.getPosition());
    }

    public double getBallDistance(Ball ball){
        return Functions.Distance(this.getPosition(), ball.getPosition());
    }

    public void setClientPosition(Vector2 pos){
        body.setTransform(pos, 0);
    }

    public void resetVelocity(){
        body.setLinearVelocity(0,0);
    }

}