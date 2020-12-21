package com.mygdx.game.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Ball extends Image  {

    private Body body;
    private World world;
    private Vector2 speed;
    int shootForce = 10000000;
    boolean shooting = false;
    Vector2 shootingDirection;

    public Ball(World aWorld, float pos_x, float pos_y){
        super(new Texture("ball.png"));
        this.setPosition(pos_x,pos_y);
        this.setScale( 0.00625f, 0.00625f);

        world = aWorld;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 1.0f;
        bodyDef.bullet = true;
        bodyDef.position.set(pos_x, pos_y);

        body = world.createBody(bodyDef);
        speed = body.getLinearVelocity();

        CircleShape shape = new CircleShape();
        shape.setRadius(0.0625f * 30);

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

        speed = body.getLinearVelocity();

        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
    }


    public void shoot(Vector2 positionShooter){
        shootingDirection = new Vector2(- positionShooter.x + body.getPosition().x, - positionShooter.y + body.getPosition().y);

        body.applyLinearImpulse(shootingDirection.scl(shootForce), body.getLocalCenter(), true);
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public void setClientPosition(float x, float y){
        body.setTransform(new Vector2(x, y), 0);
    }

    public void resetVelocity(){
        body.setLinearVelocity(0,0);
    }

}