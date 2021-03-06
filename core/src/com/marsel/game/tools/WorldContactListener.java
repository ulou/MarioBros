package com.marsel.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.marsel.game.MyGdxGame;
import com.marsel.game.sprites.enemies.Enemy;
import com.marsel.game.sprites.tileObjects.InteractiveTileObject;

/**
 * Created by Marsel on 2015-10-19.
 */
public class WorldContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if(object.getUserData() instanceof InteractiveTileObject)
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
        }

        switch (cDef){
            case MyGdxGame.ENEMY_HEAD_BIT | MyGdxGame.MARIO_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT){
                    ((Enemy)fixA.getUserData()).hitOnHead();
                } else if(fixB.getFilterData().categoryBits == MyGdxGame.ENEMY_HEAD_BIT){
                    ((Enemy)fixB.getUserData()).hitOnHead();
                }
                break;
            case MyGdxGame.ENEMY_BIT | MyGdxGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits == MyGdxGame.ENEMY_BIT){
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                } else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case MyGdxGame.MARIO_BIT | MyGdxGame.ENEMY_BIT:
                Gdx.app.log("Mario", "died");
                break;
            case MyGdxGame.ENEMY_BIT | MyGdxGame.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
