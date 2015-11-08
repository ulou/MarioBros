package com.marsel.game.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.marsel.game.MyGdxGame;
import com.marsel.game.screens.PlayScreen;

/**
 * Created by Marcel on 08.11.2015.
 */
public abstract class Item extends Sprite{
    protected World world;
    protected PlayScreen screen;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;


    public Item(PlayScreen screen, float x, float y){
        this.screen = screen;
        this.world = screen.getWorld();

        toDestroy = false;
        destroyed = false;

        setPosition(x, y);
        setBounds(getX(), getY(), 16 / MyGdxGame.PPM, 16 / MyGdxGame.PPM);

        defineItem();

    }

    public abstract void defineItem();
    public abstract void use();

    public void update(float dt) {
        if (toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch){
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy(){
        toDestroy = true;
    }

}
