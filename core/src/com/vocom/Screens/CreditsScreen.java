package com.vocom.Screens;


import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.vocom.Helpers.AssetLoader;
import com.vocom.Helpers.InputHandler;
import com.vocom.Lorenzo.LorenzoGame;
import com.vocom.TweenAccessors.BitmapFontAccessor;


public class CreditsScreen implements Screen {
	
	private LorenzoGame game; 
	private TweenManager manager;
    private SpriteBatch batcher;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;

    private float gameWidth;
    private float gameHeight;
    
    private BitmapFont fontBox, versionBox, hiscoreBox;
    private String main, ver, hiscore;

    public CreditsScreen(LorenzoGame game) {
        this.game = game;
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        this.gameWidth = 960;
        this.gameHeight = screenHeight / (screenWidth / gameWidth);
        
        cam = new OrthographicCamera();
        cam.setToOrtho(true, this.gameWidth, this.gameHeight);
        
        Gdx.input.setInputProcessor(new InputHandler(this, screenWidth / gameWidth, screenHeight / gameHeight));
        Gdx.input.setCatchBackKey(true);
                
        fontBox = AssetLoader.fontUpheavalRed;
        fontBox.setColor(1, 1, 1, 0);
        
        hiscoreBox = AssetLoader.fontUpheavalGray;
        hiscoreBox.setColor(1, 1, 1, 0);
        
        versionBox = AssetLoader.fontUpheavalGray;
        versionBox.setColor(1, 1, 1, 0);

        main = "DEV:      			Vojtech Bazinac\n\n\n"
        	 + "MUSIC: 	  			Kevin MacLeod, Klankbeeld\n\n\n"
        	 + "SPECIAL THANKS TO: 	Kilobolt\n\n\n\n\n"
        	 + "(c) Vojtech Bazinac, 2014";

        ver = "build: " + AssetLoader.build;        		
        		
        hiscore = "highscore: " + AssetLoader.getHighScore();
        
        setupTween(this.game);
    }

    public void show() {

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
         
    }

    private void setupTween(final LorenzoGame g) {
        Tween.registerAccessor(BitmapFont.class, new BitmapFontAccessor());
        manager = new TweenManager();
        
        TweenCallback cbFinal = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {
        		game.setScreen(new MenuScreen(game));
        	}	 
           
        };
        
        
        Timeline.createSequence()
        .beginParallel()
        	.push(Tween.to(fontBox, BitmapFontAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
        	.push(Tween.to(hiscoreBox, BitmapFontAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
        	.push(Tween.to(versionBox, BitmapFontAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
        .end()  
   
         .pushPause(5f)	
	     .push(Tween.call(cbFinal))
	                      
        .start(manager);
       
    }
          
    @Override
    public void render(float delta) {
    	
    	manager.update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        
        // BG        
        shapeRenderer.begin(ShapeType.Filled);  
        	shapeRenderer.setColor(Color.WHITE);
        	shapeRenderer.rect(0, 0, this.gameWidth, this.gameHeight);        
        shapeRenderer.end();
        
        
        
        batcher.begin();
	    	fontBox.drawMultiLine(batcher,main,20, 40);
	    	hiscoreBox.drawMultiLine(batcher,hiscore,20, gameHeight*0.75f);	    	
	    	versionBox.drawMultiLine(batcher,ver,20, gameHeight*0.85f);
	    	
        batcher.end();
    }
    
    public void skipCredits() {
    	game.setScreen(new MenuScreen(game));    	
    }

    public void returnToMenu() {
    	game.setScreen(new MenuScreen(game));    	
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

