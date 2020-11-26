package com.mygdx.game.Listener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;


public class BallContactListener implements ContactListener {

    public boolean isTouching;

    public BallContactListener(){
        isTouching = false;
    }

    @Override
    public void beginContact(Contact contact) {
        System.out.println(contact.getChildIndexA());
        System.out.println(contact.getChildIndexB());
        System.out.println(contact.getFixtureA());
        System.out.println(contact.getFixtureB());
        isTouching = true;
        System.out.println("Contact begun");
    }

    @Override
    public void endContact(Contact contact) {
        isTouching = false;
        System.out.println("Contact ended");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
