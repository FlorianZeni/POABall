package com.mygdx.game.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;

import static java.lang.Math.abs;

/**
 * Created by julienvillegas on 31/01/2017.
 */

public class Ball extends Image  {

    private Body body;
    private World world;
    private Vector2 speed;
    int shootForce = 10000000;
    boolean shooting = false;
    int shootingResetCount = 0;
    int shootingResetCap = 10;
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


        // Create a body in the world using our definition
        body = world.createBody(bodyDef);
        speed = body.getLinearVelocity();

        // Now define the dimensions of the physics shape
        CircleShape shape = new CircleShape();
        shape.setRadius(0.0625f * 30);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
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

        // Force de frottements
        speed = body.getLinearVelocity();
        //body.applyForce(new Vector2( new Vector2(-50 * speed.x * abs(speed.x), -50 * speed.y * abs(speed.y))), body.getLocalCenter(), true);
        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);

//        if(shooting){
//            shootingResetCount += 1;
//            body.applyForceToCenter(shootingDirection.scl(shootForce), true);
//
//            if(shootingResetCap == shootingResetCount){
//                shooting = false;
//                shootingResetCount = 0;
//                System.out.println("Ended shootin time");
//            }
//        }
    }


    public void shoot(Vector2 positionShooter){
        shootingDirection = new Vector2(- positionShooter.x + body.getPosition().x, - positionShooter.y + body.getPosition().y);
//        System.out.println(positionShooter.toString() + " " + body.getLocalCenter());
        if(shootingResetCount == 0)
            shooting = true;
        body.applyLinearImpulse(shootingDirection.scl(shootForce), body.getLocalCenter(), true);
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public void setClientPosition(float x, float y){
        body.setTransform(new Vector2(x, y), 0);
    }
}