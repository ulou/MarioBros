package com.marsel.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.Array;
import com.marsel.game.MyGdxGame;
import com.marsel.game.scenes.Hud;
import com.marsel.game.sprites.enemies.Enemy;
import com.marsel.game.sprites.Mario;
import com.marsel.game.sprites.items.Item;
import com.marsel.game.sprites.items.ItemDef;
import com.marsel.game.sprites.items.Mushroom;
import com.marsel.game.tools.B2WorldCreator;
import com.marsel.game.tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Marsel on 2015-10-15.
 */
public class PlayScreen implements Screen {

    private MyGdxGame game;
    private TextureAtlas atlas;

    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    private Mario player;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(MyGdxGame game){

        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gameCam);
        hud = new Hud(game.batch);

        //load map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/MyGdxGame.PPM);

        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

        // create Gdx world
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        player = new Mario(this);
        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();

        creator = new B2WorldCreator(this);

        // collision
        world.setContactListener(new WorldContactListener());

    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }


    public void handlerInput(float dt){
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && player.getState() != Mario.State.JUMPING && player.getState() != Mario.State.FALLING)
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
        if(Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

    }

    public void update(float dt){
        handlerInput(dt);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);

        player.update(dt);

        hud.update(dt);

        for(Enemy enemy: creator.getGoombas()){
            enemy.update(dt);
            if(enemy.getX() < player.getX() + (16*15)/MyGdxGame.PPM)
                enemy.b2body.setActive(true);
        }

        for (Item item: items){
            item.update(dt);
        }

        gameCam.position.x = player.b2body.getPosition().x;
        //update game with correct cords
        gameCam.update();
        //renderer to draw only what camera see
        renderer.setView(gameCam);

    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void render(float delta) {
        update(delta);

        //black screen game
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render game map
        renderer.render();

        //renderer Box2DDebugRenderer
        b2dr.render(world, gameCam.combined);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy: creator.getGoombas()){
            enemy.draw(game.batch);
        }
        for (Item item: items){
            item.draw(game.batch);
        }

        game.batch.end();

        // set batch to draw what we see.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
