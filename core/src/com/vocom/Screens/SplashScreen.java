package com.vocom.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.vocom.Helpers.AssetLoader;
import com.vocom.Lorenzo.LorenzoGame;
import com.vocom.TweenAccessors.SpriteAccessor;


public class SplashScreen implements Screen {

    private TweenManager manager;
    private SpriteBatch batcher;
    private Sprite vocomLogo;
    private Sprite bazinacLogo;
    private LorenzoGame game;


    public SplashScreen(LorenzoGame game) {
        this.game = game;
    }

    public void show() {

    	vocomLogo = new Sprite(AssetLoader.vocomLogo);
    	vocomLogo.setColor(1, 1, 1, 0);
    	
    	bazinacLogo = new Sprite(AssetLoader.bazinacLogo);
    	bazinacLogo.setColor(1, 1, 1, 0);

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float desiredWidth = width * .6f;
        float scale = desiredWidth / vocomLogo.getWidth();

        vocomLogo.setBounds((width / 2) - (vocomLogo.getWidth() * scale / 2), (height / 2) - (vocomLogo.getHeight() * scale / 2), vocomLogo.getWidth() * scale, vocomLogo.getHeight() * scale);
        bazinacLogo.setBounds((width / 2) - (bazinacLogo.getWidth() * scale / 2), (height / 2) - (bazinacLogo.getHeight() * scale / 2), bazinacLogo.getWidth() * scale, bazinacLogo.getHeight() * scale);

        setupTween(this.game);
        batcher = new SpriteBatch();
    }

    private void setupTween(final LorenzoGame g) {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        manager = new TweenManager();
        
        TweenCallback cbFinal = new TweenCallback() {

			@Override
            public void onEvent(int type, BaseTween<?> source) {
                game.setScreen(new MenuScreen(game));
            }
        };

        Timeline.createSequence()
        	.push(Tween.to(vocomLogo, SpriteAccessor.ALPHA, 1f).target(1).ease(TweenEquations.easeInSine))
        	.pushPause(2f)
        	 .beginParallel()
        	 	.push(Tween.to(vocomLogo, SpriteAccessor.ALPHA, 0.5f).target(0).ease(TweenEquations.easeInSine))
        	 	.push(Tween.to(bazinacLogo, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInCirc))     	
        	.end()
        	.pushPause(3f)
        	
        	.push(Tween.call(cbFinal))	                      
        .start(manager);
        
        
                
    }

    @Override
    public void render(float delta) {
        manager.update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        vocomLogo.draw(batcher);
        bazinacLogo.draw(batcher);
        batcher.end();
    }

    @Override
    public void resize(int width, int height) {
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

