package com.marsel.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.marsel.game.MyGdxGame;
import com.marsel.game.scenes.Hud;
import com.marsel.game.screens.PlayScreen;

/**
 * Created by Marsel on 2015-10-18.
 */
public class Coin extends InteractiveTileObject {

    private final int BLANK_COIN = 27 + 1;
    private boolean scoreTaken = false;
    private TiledMapTileSet tileSet;

    public Coin(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Coin", "Collision");
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        if (!scoreTaken) {
            Hud.addScore(100);
            scoreTaken = true;
        }
    }
}
