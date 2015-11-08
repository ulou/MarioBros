package com.marsel.game.sprites.items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Marcel on 08.11.2015.
 */
public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
