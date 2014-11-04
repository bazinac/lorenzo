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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.vocom.Helpers.AssetLoader;
import com.vocom.Helpers.InputHandler;
import com.vocom.Helpers.SoundController;
import com.vocom.Lorenzo.LorenzoGame;
import com.vocom.TweenAccessors.BitmapFontAccessor;
import com.vocom.TweenAccessors.SpriteAccessor;


public class IntroScreen implements Screen {

	private SoundController sound;
	
	private TweenManager manager;
    private SpriteBatch batcher;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera cam;
    private LorenzoGame game;
    private float runTime;
    private boolean introOnly;
    
    private float gameWidth;
    private float gameHeight;
    
    
    private Sprite farmSprite;
    private Sprite rainbowSprite;
    private Sprite japanSprite;
    private Sprite cplatformSprite1, cplatformSprite2;
    private Sprite healthy, unhealthy;
    
    private Sprite lorenzoSprite;    
    private Animation lorenzoFlyAnim;
    private boolean flying;
    private boolean storyPart;
    
    private BitmapFont fontBox;
    private String currentTxt;
    
    public static final String TX1 ="Sad, so utmost sad life\nat swinery in Lety.\n\nSad place with tragic history,\nwhere hundreds of piggies lead\ntheir miserable lives.";
    public static final String TX2 = "No future for young generation.\n\nEven rainbow has no color there.";
    public static final String TX3 = "But one of pigs, Lorenzo,\nhas chance for a better life.\n\nDue to rare precursor\nmutation c.2284del4,\nit has developed a nice\npair of wings.";
    public static final String TX4 = "Driven by its swinish instincts,\nour hero waits for\nopportunity to escape.";
    public static final String TX5 = "One morning, when skies are clear,\nhe spreads his little wings\nand his demanding journey begins.";
    public static final String TX6 = "9000 km ahead. Quite a lot\nfor a little grunty aeronaut.\nDestination is ...";
    public static final String TX7 = "JAPAN!\n\nEastern realm,\nwhere they treat pigs\nwith massive respect.";
    public static final String TX8 = "Make use of cloud platforms.\nJump from one to another.";
    public static final String TX9 = "Tap screen once to jump,\ntap it one more time when jumped\nin order to fly.";
    public static final String TX10 = "But flying consumes fat,\nwhich is pretty precious stuff\nup there in the skies.\n\nRemember.\n\nOut of fat => awful death!";
    public static final String TX11 = "So try to keep fat level\nas high as possible by\neating properly.\n\nAim for high caloric stuff\nthat will keep pig in good shape.\n\nAvoid veggies that\nimpair endurance greatly.";
    public static final String TX12 = "Bon voyage!\nHopefully you can make\nit straight to Japan!";
    
    
    public IntroScreen(LorenzoGame game, boolean introOnly) {
        this.game = game;
        this.introOnly = introOnly;

        sound = new SoundController(!AssetLoader.getMuteStatus());
        sound.startLittleFaith();
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        this.gameWidth = 960;
        this.gameHeight = screenHeight / (screenWidth / gameWidth);
        
        cam = new OrthographicCamera();
        cam.setToOrtho(true, this.gameWidth, this.gameHeight);
        
        Gdx.input.setInputProcessor(new InputHandler(this, screenWidth / gameWidth, screenHeight / gameHeight));
        Gdx.input.setCatchBackKey(true);
        
        setupSprites();
        setupTween(this.game);
        
        storyPart = true;
        
    }

    public void show() {

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
 
    }
    

	private void setupSprites() {
		farmSprite = new Sprite(AssetLoader.farm);
    	farmSprite.setColor(1, 1, 1, 0);

    	rainbowSprite = new Sprite(AssetLoader.rainbow);
    	rainbowSprite.setColor(1, 1, 1, 0);
    	
    	japanSprite = new Sprite(AssetLoader.japan);
    	japanSprite.setColor(1, 1, 1, 0);
    	
    	lorenzoSprite = new Sprite(AssetLoader.lorenzo);
    	lorenzoSprite.setColor(1, 1, 1, 0);
    	
    	cplatformSprite1 = new Sprite(AssetLoader.cplatform);
    	cplatformSprite1.setColor(1, 1, 1, 0);
    	
    	cplatformSprite2 = new Sprite(AssetLoader.cplatform);
    	cplatformSprite2.setColor(1, 1, 1, 0);
    	
    	unhealthy = new Sprite(AssetLoader.burger);
    	unhealthy.setColor(1, 1, 1, 0);
    	
    	healthy =  new Sprite(AssetLoader.broccoli);
    	healthy.setColor(1, 1, 1, 0);
    	
        float farmScale = this.gameWidth / farmSprite.getWidth();
        farmSprite.setBounds(0,this.gameHeight - (farmSprite.getHeight() * farmScale) , farmSprite.getWidth() * farmScale, farmSprite.getHeight() * farmScale);
        
        float rainbowScale = this.gameWidth / rainbowSprite.getWidth();
        rainbowSprite.setBounds(0, 0, rainbowSprite.getWidth() * rainbowScale, rainbowSprite.getHeight() * rainbowScale);
        
        float japanScale = this.gameWidth / japanSprite.getWidth();
        japanSprite.setBounds(0,this.gameHeight - (japanSprite.getHeight() * japanScale) , japanSprite.getWidth() * japanScale, japanSprite.getHeight() * japanScale);

        lorenzoSprite.setBounds(15, (gameHeight)-180, 95, 125);        
        lorenzoFlyAnim = AssetLoader.lorenzoFlyAnim;

        cplatformSprite1.setBounds(100, gameHeight*0.66f, 175, 31);
        cplatformSprite2.setBounds(575, gameHeight*0.66f, 175, 31);
        
        unhealthy.setBounds(150, gameHeight*0.66f-75, 75, 75);
        healthy.setBounds(625, gameHeight*0.66f-75, 75, 75);
        
        fontBox = AssetLoader.fontUpheavalRed;
        fontBox.setColor(1, 1, 1, 0);
        fontBox.setScale(1.5f,-1.5f);        
        currentTxt = TX1;

	}
	

    private void setupTween(final LorenzoGame g) {
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
        Tween.registerAccessor(BitmapFont.class, new BitmapFontAccessor());
        manager = new TweenManager();
        
        
        
        TweenCallback cb1 = new TweenCallback() {

			@Override
            public void onEvent(int type, BaseTween<?> source) {
				System.out.println(String.valueOf(type));				
				currentTxt = TX2;
            }
        };
        
        TweenCallback cb2 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX3;
             }           
        };
        
        TweenCallback cb3 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX4;
             }           
        };
        
        TweenCallback cb4 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX5;
        		flying = true;
             }           
        };
        
        TweenCallback cb5 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX6;
        		flying = true;
             }           
        };
        
        TweenCallback cb6 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX7;
        		flying = false;   			
             }           
        };
        
        TweenCallback cb7 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX8;
        		storyPart = false;
        		flying = false;
        		lorenzoSprite.setBounds(150, (gameHeight*0.66f)-120, 95, 125);
        	}           
        };
        
        TweenCallback cb8 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		currentTxt = TX9;
        		storyPart = false;
        		flying = false;
        	}           
        };
        
        TweenCallback cb9 = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {				
        		storyPart = false;
        		flying = true;
        	}           
        };
        
        TweenCallback c10= new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {
        		currentTxt = TX10;
        		storyPart = false;
        		flying = false;        		
        	}           
        };
        
        TweenCallback c11= new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {
        		currentTxt = TX11;
        		storyPart = false;
        		flying = false;        		
        	}           
        };
        
       TweenCallback c12= new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {
        		currentTxt = TX12;
        		storyPart = false;
        		flying = false;        		
        	}           
        };
        
        
        TweenCallback cbFinal = new TweenCallback() {
        	@Override
            public void onEvent(int type, BaseTween<?> source) {
				
          		 AssetLoader.setIntroPlayedStatus(true);
          		 sound.stopLittleFaith();
				 
 				 if (!introOnly) {
 					 game.setScreen(new GameScreen(game));
 				 }
 				 else game.setScreen(new MenuScreen(game));
        	}	 
           
        };

        
        Timeline.createSequence()
        		 .beginParallel()
	             	 .push(Tween.to(farmSprite, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(rainbowSprite, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(lorenzoSprite, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(fontBox, BitmapFontAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))	                 
	             .end()	             
	             .pushPause(10f)	             
	             
	             .push(Tween.call(cb1))
	             .pushPause(5f)
	             
	             .push(Tween.call(cb2))
	             .pushPause(10f)
	             
	             .push(Tween.call(cb3))	            
	             .pushPause(8f)
	             
	             
	             .beginParallel()
	                     .push(Tween.call(cb4))
	                     .push(Tween.to(lorenzoSprite, SpriteAccessor.XPOS, 3f).target(300))
	                     .push(Tween.to(lorenzoSprite, SpriteAccessor.YPOS, 3f).target(gameHeight-500))
	             .end()
	             .pushPause(5f)

	             .push(Tween.call(cb5))
	             .pushPause(5f)
	 	             
	             .beginParallel()
	             	 .push(Tween.to(farmSprite, SpriteAccessor.ALPHA, 0.5f).target(0).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(rainbowSprite, SpriteAccessor.ALPHA, 0.5f).target(0).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(lorenzoSprite, SpriteAccessor.ALPHA, 0.1f).target(0).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(fontBox, BitmapFontAccessor.ALPHA, 1f).target(0).ease(TweenEquations.easeInSine))	                 
                  	 .push(Tween.to(fontBox, BitmapFontAccessor.ALPHA, 1f).target(1).ease(TweenEquations.easeInSine))
	                 .push(Tween.to(japanSprite, SpriteAccessor.ALPHA, 2f).target(1f).ease(TweenEquations.easeInOutSine))	                 
	                 .push(Tween.call(cb6))
	              .end()               	              
	              .pushPause(5f)
	              
	              .beginParallel()
	              	.push(Tween.to(japanSprite, SpriteAccessor.ALPHA, 2f).target(0f).ease(TweenEquations.easeInOutSine))	
	              .end()
	              
	              .beginParallel()
	              	.push(Tween.call(cb7))
	              	.push(Tween.to(cplatformSprite1, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
	              	.push(Tween.to(cplatformSprite2, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
	              	.push(Tween.to(lorenzoSprite, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))	              	
	              .end()
	              .pushPause(5f)
	              
	              .beginParallel()
	              	.push(Tween.call(cb8))
	                .push(Tween.to(lorenzoSprite, SpriteAccessor.XPOS, 3f).target(400))
	                .push(Tween.to(lorenzoSprite, SpriteAccessor.YPOS, 3f).target((gameHeight*0.66f)-120-100))          	
	              .end()
	              
	              .beginParallel()
	              	.push(Tween.call(cb9))
	                .push(Tween.to(lorenzoSprite, SpriteAccessor.XPOS, 3f).target(1000))
	                .push(Tween.to(lorenzoSprite, SpriteAccessor.YPOS, 3f).target((gameHeight*0.66f)-120-200-125))          	
	              .end()
	       	              
	              .push(Tween.call(c10))
	              .pushPause(10f)
	              
	              .beginParallel()
	              	.push(Tween.call(c11))
			      	.push(Tween.to(healthy, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
			      	.push(Tween.to(unhealthy, SpriteAccessor.ALPHA, 2f).target(1).ease(TweenEquations.easeInSine))
	              .end()
	              .pushPause(12f)
	              
	              .beginParallel()
	              	.push(Tween.call(c12))
			      	.push(Tween.to(healthy, SpriteAccessor.ALPHA, 0.5f).target(0).ease(TweenEquations.easeInSine))
			      	.push(Tween.to(unhealthy, SpriteAccessor.ALPHA, 0.5f).target(0).ease(TweenEquations.easeInSine))
			      	.push(Tween.to(cplatformSprite1, SpriteAccessor.ALPHA, 1f).target(0).ease(TweenEquations.easeInSine))
			      	.push(Tween.to(cplatformSprite2, SpriteAccessor.ALPHA, 1f).target(0).ease(TweenEquations.easeInSine))
	              .end()
	              .pushPause(3f)
	              
	              .push(Tween.call(cbFinal))
	                      
        .start(manager);
        
        

        
     
    }
         
    @Override
    public void render(float delta) {
        runTime += delta;
        this.render(delta, runTime);
    }
    

    public void render(float delta, float runTime) {
    	manager.update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        
        // BG        
        shapeRenderer.begin(ShapeType.Filled);  
        	shapeRenderer.setColor(Color.LIGHT_GRAY);
        	shapeRenderer.rect(0, 0, this.gameWidth, this.gameHeight);        
        shapeRenderer.end();

        
        // sprity
        batcher.begin();
           if (storyPart){
	           japanSprite.draw(batcher);
		       rainbowSprite.draw(batcher); 
		       farmSprite.draw(batcher);	      
           }
           else
           {  
        	   cplatformSprite1.draw(batcher);
        	   cplatformSprite2.draw(batcher);
        	   healthy.draw(batcher);
        	   unhealthy.draw(batcher);
           }
           
	       if (flying){
	    	   batcher.draw(lorenzoFlyAnim.getKeyFrame(runTime), lorenzoSprite.getX(), lorenzoSprite.getY(), 95, 125, 95, 125, 1, 1, 1);
	       }
	       else	 lorenzoSprite.draw(batcher);

	       fontBox.drawMultiLine(batcher,currentTxt,20, 40);

	       
        
        batcher.end();
    }

    public void skipIntro() {
    	 sound.stopLittleFaith();
    	 if (!introOnly) {
				 game.setScreen(new GameScreen(game));
			 }
			 else game.setScreen(new MenuScreen(game));
	}	 
    
    public void returnToMenu() {
   	 	sound.stopLittleFaith();
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

