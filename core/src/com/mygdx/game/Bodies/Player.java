package com.mygdx.game.Bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Utils.Functions;

import static java.lang.Math.abs;

/**
 * Created by julienvillegas on 31/01/2017.
 */

public class Player extends Image  {

    public Body body;
    private World world;
    private Vector2 speed;

    public Player(World aWorld, float pos_x, float pos_y){
        super(new Texture("player.png"));
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
        speed = body.getLinearVelocity();

        // Now define the dimensions of the physics shape
//        PolygonShape shape = new PolygonShape();
        CircleShape shape = new CircleShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        shape.setRadius(0.125f * 30);
//        shape.setAsBox(this.getWidth()/2, this.getHeight()/2);

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
        speed = body.getLinearVelocity();
        //this.setRotation(body.getAngle()*  MathUtils.radiansToDegrees);
        //body.applyForce(new Vector2( new Vector2(-500 * speed.x * abs(speed.x), -500 * speed.y * abs(speed.y))), body.getLocalCenter(), true);
        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);

    }


    public void move(Vector2 keysPressed, float deltaTime){
        float force = 5000;
        body.applyLinearImpulse(keysPressed.scl(force* deltaTime), body.getLocalCenter(), true);
    }

    public Body getBody(){
        return body;
    }

    public Vector2 getPosition(){
        return body.getPosition();
    }

    public double getBallDistance(Ball ball){
        return Functions.Distance(this.getPosition(), ball.getPosition());
    }

    public void setClientPosition(float x, float y){
        body.setTransform(new Vector2(x, y), 0);
    }

}