package com.vocom.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.vocom.GameWorld.GameRenderer;
import com.vocom.GameWorld.GameWorld;
import com.vocom.Helpers.InputHandler;
import com.vocom.Lorenzo.LorenzoGame;


public class GameScreen implements Screen {
	
	private GameWorld world;
	private GameRenderer renderer;
	
	
	private float runTime = 0;
   
    public GameScreen(LorenzoGame game) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float gameWidth = 960;
        float gameHeight = screenHeight / (screenWidth / gameWidth);        
     
        world = new GameWorld((int)gameWidth, (int)gameHeight, game);
        renderer = new GameRenderer(world, (int) gameWidth, (int) gameHeight);
        
        Gdx.input.setInputProcessor(new InputHandler(world, renderer, screenWidth / gameWidth, screenHeight / gameHeight));
        Gdx.input.setCatchBackKey(true);	
        
    }

    @Override
    public void render(float delta) {
        runTime += delta;
        world.update(delta);
        renderer.render(delta, runTime);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
  
    }

    @Override
    public void pause() {
  
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }

}
