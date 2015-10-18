package com.marsel.game.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.marsel.game.MyGdxGame;

/**
 * Created by Marsel on 2015-10-18.
 */
public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;


        // define object
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth()/2) / MyGdxGame.PPM, (bounds.getY() + bounds.getHeight()/2) / MyGdxGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth()/2) / MyGdxGame.PPM, (bounds.getHeight()/2) / MyGdxGame.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }
}