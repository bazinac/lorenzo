package com.vocom.GameWorld;


import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.vocom.GameObjects.Cplatform;
import com.vocom.GameObjects.Food;
import com.vocom.GameObjects.Lorenzo;
import com.vocom.GameObjects.ScrollHandler;
import com.vocom.GameObjects.Lorenzo.MessageType;
import com.vocom.Helpers.AssetLoader;
import com.vocom.TweenAccessors.Value;
import com.vocom.TweenAccessors.ValueAccessor;
import com.vocom.UI.Button;

public class GameRenderer {	

	private GameWorld myWorld;
	private OrthographicCamera cam;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;
		
    private int gameWidth;
    private int gameHeight;
    private int midY;
 
    private Random r; 
    
 // Game Objects
    private Lorenzo lorenzo;
    private ScrollHandler scroller;
    private Cplatform platform1, platform2, platform3;
    private Food food1, food2, food3, food4, food5, food6;

  // Game Assets
    private TextureRegion lr, bg, cplatform, lardE, lardF, scoreCoin;
    private TextureRegion buttonDown, buttonUp, buttonSmallDown, buttonSmallUp;
    
	private Button againButton, exitButton, pauseButton, resumeButton, buyButton;
    private Color transitionColor, defaultBg = new Color(142/255.0f, 170/255.0f, 181/255.0f, 1);
    
	private Animation lorenzoRunAnim, lorenzoFlyAnim;
    
    // Tween stuff
    private TweenManager manager;
    private Value alpha = new Value();
	    
    public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {
		myWorld = world;

		this.gameWidth = gameWidth;
	    this.gameHeight = gameHeight;
		this.midY = (int) (gameHeight / 2);

		cam = new OrthographicCamera();
		cam.setToOrtho(true, gameWidth, gameHeight);
        
		batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);
        
        // Call helper methods to initialize instance variables
        initGameObjects();
        initAssets();
        prepareFonts();
        
		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);
        
        setupTweens();
        
        r = new Random();
	}

    private void setupTweens() {
        Tween.registerAccessor(Value.class, new ValueAccessor());
        manager = new TweenManager();
        Tween.to(alpha, -1, .5f).target(0).ease(TweenEquations.easeOutQuad)
                .start(manager);
    }

    public void render(float delta, float runTime) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        
        //Shapes
        shapeRenderer.begin(ShapeType.Filled);  
        	// BG color 
        	if (myWorld.getSpecialBgColor() != null){
        		 if (myWorld.getSpecialBgColor() == "PINK+PURPLE+GREEN") {    	
    	        		int rr = (r.nextInt(3));
    	        		switch (rr){
    	        			case 0:
    	        				shapeRenderer.setColor(Color.PINK);
    	        				break;
    	        			case 1:
    	        				shapeRenderer.setColor(Color.PURPLE);
    	        				break;
    	        			case 2:
    	        				shapeRenderer.setColor(Color.GREEN);
    	        				break;
    	        		}
    	         }
    	          else if(myWorld.getSpecialBgColor() == "BEER"){
    	        		shapeRenderer.setColor(new Color(192/255.0f, 124/255.0f, 59/255.0f, 1));

        		}
        	}
        	else{
        		shapeRenderer.setColor(defaultBg);        		
        	}
		    shapeRenderer.rect(0, 0, gameWidth, gameHeight);
                 
        shapeRenderer.end();

        //Sprites and textures
        batcher.begin();  
        
        batcher.disableBlending();
        	
        batcher.enableBlending();
        
    
        
	        if (myWorld.isRunning()) {	        	
	        	drawBg();
	        	drawLorenzo(runTime);
	        	drawLorenzosMsg();
	        	drawPlatforms();
	        	drawFood();
	        	drawScoreCoin(); 
	        	drawLardJar(lorenzo.getLardLevel());	
	        	drawPauseButton();
			} else if (myWorld.isReady()) {
				drawBg();				 
				drawPlatforms();
				drawLorenzoReady(runTime);
	        	drawScoreCoin();
	        	drawLardJar(lorenzo.getLardLevel());
	        	drawGoAnnouncement();
	        
			} else if (myWorld.isPaused()) {
				drawBg();				 
				drawPlatforms();
				drawLorenzoPaused(runTime);
				drawLorenzosMsg();
				drawFood();				
	        	drawScoreCoin();
	        	drawLardJar(lorenzo.getLardLevel());
	        	drawPauseMenu();
	        	
			} else if (myWorld.isGameOver() || myWorld.isHighScore()) {
				drawBg();
				drawPlatforms();
				drawFood();
				drawLorenzoReady(runTime);
	        	drawScoreCoin(); 
	        	drawLardJar(lorenzo.getLardLevel());
	        	drawHiScore();
	        	drawButtons();
			}
			
        batcher.end();
        
        //Object boundaries
   /*
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.ORANGE);
        
        
        if (food1.isVisible()) drawBoundingCircle(food1.getBoundingCircle());
        if (food2.isVisible()) drawBoundingCircle(food2.getBoundingCircle());
        if (food3.isVisible()) drawBoundingCircle(food3.getBoundingCircle());
        if (food4.isVisible()) drawBoundingCircle(food4.getBoundingCircle());
        if (food5.isVisible()) drawBoundingCircle(food5.getBoundingCircle());
        if (food6.isVisible()) drawBoundingCircle(food6.getBoundingCircle());
        
        
    	drawBoundingRectangle(lorenzo.getLandingPad());  
        shapeRenderer.setColor(Color.PINK);
        	drawBoundingCircle(lorenzo.getBoundingCircle());
        shapeRenderer.setColor(Color.GREEN);
        	drawBoundingRectangle(platform1.getBoundingRectangle());  
        shapeRenderer.end();   
      
      */ 
        drawTransition(delta);

    }
   
    private void drawGoAnnouncement() {
    	AssetLoader.fontUpheavalGray.draw(batcher, "Click to go!", this.gameWidth/2 -100 , this.gameHeight/2-200);
    }

    private void drawButtons() {
    	
    	if (AssetLoader.PREMIUM == false) buyButton = new Button(gameWidth/2 - buttonUp.getRegionWidth()/2 , 25, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
    	againButton = new Button(gameWidth/2 - buttonUp.getRegionWidth()/2, buttonUp.getRegionHeight() + 50, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
    	exitButton = new Button(gameWidth/2 - buttonUp.getRegionWidth()/2 , 2*(buttonUp.getRegionHeight()) + 75, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
    	
    	if (AssetLoader.PREMIUM == false) buyButton.draw(batcher);
 	    againButton.draw(batcher); 	   
    	exitButton.draw(batcher);
        
    	if (AssetLoader.PREMIUM == false) AssetLoader.fontUpheavalGray.draw(batcher, "BUY PREMIUM", buyButton.getX()+buyButton.getWidth()/2 - 100, buyButton.getY()+15);
    	AssetLoader.fontUpheavalGray.draw(batcher, "TRY AGAIN", againButton.getX()+againButton.getWidth()/2 - 70, againButton.getY()+15);
        AssetLoader.fontUpheavalGray.draw(batcher, "EXIT", exitButton.getX()+exitButton.getWidth()/2 - 35, exitButton.getY()+15);

    }
    
    private void drawPauseMenu() {
    	if (AssetLoader.PREMIUM == false) buyButton = new Button(gameWidth/2 - buttonUp.getRegionWidth()/2 , 25, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
    	resumeButton = new Button(gameWidth/2 - buttonUp.getRegionWidth()/2, buttonUp.getRegionHeight() + 50, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
 	    exitButton = new Button(gameWidth/2 - buttonUp.getRegionWidth()/2 , 2*(buttonUp.getRegionHeight()) + 75, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
    	
 	    if (AssetLoader.PREMIUM == false) buyButton.draw(batcher);
 	    resumeButton.draw(batcher);
    	exitButton.draw(batcher);
        
    	if (AssetLoader.PREMIUM == false) AssetLoader.fontUpheavalGray.draw(batcher, "BUY PREMIUM", buyButton.getX()+buyButton.getWidth()/2 - 100, buyButton.getY()+15);
    	AssetLoader.fontUpheavalGray.draw(batcher, "RESUME", resumeButton.getX()+resumeButton.getWidth()/2 - 70, resumeButton.getY()+15);
        AssetLoader.fontUpheavalGray.draw(batcher, "EXIT", exitButton.getX()+exitButton.getWidth()/2 - 35, exitButton.getY()+15);

    }
    
    private void drawPauseButton() {
    	pauseButton = new Button(gameWidth - buttonSmallUp.getRegionWidth()-5, lardE.getRegionHeight() + 10, buttonSmallUp.getRegionWidth(), buttonSmallUp.getRegionHeight(), buttonSmallUp, buttonSmallDown);
    	pauseButton.draw(batcher);
    }
    
    private void drawLardJar(int lardLevel) {
        batcher.draw(lardE, this.gameWidth - 83, 5,lardE.getRegionWidth(), lardE.getRegionHeight());
        batcher.flush();
        
        int showLevel = lardLevel;
        if (showLevel <= 0) {showLevel = 1;} 	
        
        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = new Rectangle(this.gameWidth - 83,125,lardF.getRegionWidth(), -showLevel);
        ScissorStack.calculateScissors(cam, batcher.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);

        batcher.draw(lardF, this.gameWidth - 83, 5,lardF.getRegionWidth(), lardF.getRegionHeight());        
        batcher.flush();
        ScissorStack.popScissors();
        
        int length = String.valueOf(lardLevel).length();
        AssetLoader.fontUpheaval.draw(batcher, String.valueOf(lardLevel), this.gameWidth - 55 - (3*length), 60f);

    }
    
    private void drawBg() {
        batcher.draw(bg, 0, this.gameHeight - (bg.getRegionHeight()),bg.getRegionWidth(), bg.getRegionHeight());
    }


    
    private void drawPlatforms() {
        batcher.draw(cplatform, platform1.getX(), platform1.getY(), platform1.getWidth(), platform1.getHeight());
        batcher.draw(cplatform, platform2.getX(), platform2.getY(), platform2.getWidth(), platform2.getHeight());
        batcher.draw(cplatform, platform3.getX(), platform3.getY(), platform3.getWidth(), platform3.getHeight());
        
    }
    
    private void drawFood() {
    	
        if (food1.isVisible()) batcher.draw(food1.getSprite(), food1.getX(), food1.getY(), food1.getWidth(), food1.getHeight());
        if (food2.isVisible()) batcher.draw(food2.getSprite(), food2.getX(), food2.getY(), food2.getWidth(), food2.getHeight());
        if (food3.isVisible()) batcher.draw(food3.getSprite(), food3.getX(), food3.getY(), food3.getWidth(), food3.getHeight());
        if (food4.isVisible()) batcher.draw(food4.getSprite(), food4.getX(), food4.getY(), food4.getWidth(), food4.getHeight());
        if (food5.isVisible()) batcher.draw(food5.getSprite(), food5.getX(), food5.getY(), food5.getWidth(), food5.getHeight());
        if (food6.isVisible()) batcher.draw(food6.getSprite(), food6.getX(), food6.getY(), food6.getWidth(), food6.getHeight());
        
    }

    private void drawLorenzoReady(float runTime) {

    	 batcher.draw(lr, lorenzo.getX(), lorenzo.getY(), lorenzo.getWidth(), lorenzo.getHeight(),
         		lorenzo.getWidth(), lorenzo.getHeight(), 1, 1, lorenzo.getRotation());  	 

    }
    
    private void drawLorenzoPaused(float runTime) {

   	 batcher.draw(lr, lorenzo.getX(), lorenzo.getY(), lorenzo.getWidth(), lorenzo.getHeight(),
        		lorenzo.getWidth(), lorenzo.getHeight(), 1, 1, lorenzo.getRotation());  	 

   }

    private void drawLorenzo(float runTime) {

    	if (lorenzo.isFlying()) {
    		batcher.draw(lorenzoFlyAnim.getKeyFrame(runTime), lorenzo.getX(),
    	    lorenzo.getY(), lorenzo.getWidth(),
    	    lorenzo.getHeight(), lorenzo.getWidth(), lorenzo.getHeight(),
    	     1, 1, lorenzo.getRotation());
    	} 
    	
    	else if (lorenzo.isJumped()) {
    		batcher.draw(lr, lorenzo.getX(), lorenzo.getY(),
            lorenzo.getWidth(), lorenzo.getHeight(),
            lorenzo.getWidth(), lorenzo.getHeight(), 1, 1, lorenzo.getRotation());
    	}   	
    	
    	else if (lorenzo.isLanded()) {
    		batcher.draw(lorenzoRunAnim.getKeyFrame(runTime), lorenzo.getX(),
            lorenzo.getY(), lorenzo.getWidth(),
            lorenzo.getHeight(), lorenzo.getWidth(), lorenzo.getHeight(),
            1, 1, lorenzo.getRotation());
    	}

    }
    
	private void drawLorenzosMsg() {
		String msg = lorenzo.getMsg();
		if (msg != "" && msg != null){ 
			int length = msg.length();
				
				AssetLoader.fontUpheavalGreen.setScale(1.2f, -1.2f);
			
				if (lorenzo.getMsgType() == MessageType.POSITIVE) {
					AssetLoader.fontUpheavalGreen.setScale(1.2f, -1.2f);
					AssetLoader.fontUpheavalGreen.draw(batcher, msg, lorenzo.getX()-(3*length), lorenzo.getY()-5);
					AssetLoader.fontUpheavalGreen.setScale(1f, -1f);
					}
				else if (lorenzo.getMsgType() == MessageType.NEGATIVE) {
					AssetLoader.fontUpheavalRed.setScale(1.2f, -1.2f);
					AssetLoader.fontUpheavalRed.draw(batcher, msg, lorenzo.getX()-(3*length), lorenzo.getY()-5);
					AssetLoader.fontUpheavalRed.setScale(1f, -1f);
					}
				else if (lorenzo.getMsgType() == MessageType.NEUTRAL) {
					AssetLoader.fontUpheavalGray.setScale(1.2f, -1.2f);
					AssetLoader.fontUpheavalGray.draw(batcher, msg, lorenzo.getX()-(3*length), lorenzo.getY()-5);
					AssetLoader.fontUpheavalGray.setScale(1f, -1f);
				}
			}		
    }
	

    private void drawScoreCoin() {
    	batcher.draw(scoreCoin, this.gameWidth - 160, 5,scoreCoin.getRegionWidth(), scoreCoin.getRegionHeight());
        int length = ("" + myWorld.getScore()).length();
        AssetLoader.fontUpheaval.draw(batcher, "" + myWorld.getScore(),  this.gameWidth - 135 - (3 * length), 25);
    }
    
    private void drawHiScore() {
    	if (AssetLoader.getLastScore() == AssetLoader.FREE_SCORE_CAP && !AssetLoader.PREMIUM){
    		AssetLoader.fontUpheaval.drawMultiLine(batcher, "MADE IT THROUGH FREE VERSION (" + AssetLoader.getLastScore()+ " KM)\n\nDO YOU WANT TO GET LORENZO CLOSER TO JAPAN?\n\nPLEASE GO PREMIUM...", 200, midY + 50);
    	}
    	else {
	    	if (myWorld.isHighScore()){    	
	    		AssetLoader.fontUpheaval.draw(batcher, "NICE! YOU'VE SET A NEW RECORD OF " + AssetLoader.getHighScore()+ " KM",	200, midY + 50);
	    	}
	    	else {
	    		AssetLoader.fontUpheaval.draw(batcher,"JUST " + AssetLoader.getLastScore() + "? TRY HARDER! WE HAVE ALREADY SEEN " + AssetLoader.getHighScore() + "KM!",	130, midY + 50);
    	}
    	}

	}
	
    /*
	private void drawBoundingCircle(Circle c) {
		shapeRenderer.circle(c.x, c.y, c.radius);
	}
	
	private void drawBoundingRectangle(Rectangle r) {
		shapeRenderer.rect(r.x,r.y,r.width,r.height);
	}
    */
    
	 private void initGameObjects() {
	        lorenzo = myWorld.getLorenzo();       
	        scroller = myWorld.getScroller();
	        platform1 = scroller.getPlatform1();
	        platform2 = scroller.getPlatform2();
	        platform3 = scroller.getPlatform3();
	        
	        food1 = scroller.getFood1();
	        food2 = scroller.getFood2();
	        food3 = scroller.getFood3();
	        food4 = scroller.getFood4();
	        food5 = scroller.getFood5();
	        food6 = scroller.getFood6();
	        
	   }

	private void initAssets() {	       
	        lr = AssetLoader.lorenzo;
	        bg = AssetLoader.bg;
	        cplatform = AssetLoader.cplatform;
	        lardE = AssetLoader.lardEmpty;
	        lardF = AssetLoader.lardFull;
	        scoreCoin = AssetLoader.scoreCoin;
	        
			buttonDown = AssetLoader.buttonDown;
			buttonUp = AssetLoader.buttonUp;
			buttonSmallUp = AssetLoader.buttonSmallUp;
			buttonSmallDown = AssetLoader.buttonSmallDown;
				
	        lorenzoRunAnim = AssetLoader.lorenzoRunAnim;
	        lorenzoFlyAnim = AssetLoader.lorenzoFlyAnim;
	
	   }
	    
	    public void prepareTransition(int r, int g, int b, float duration) {
			transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
			alpha.setValue(1);
			Tween.registerAccessor(Value.class, new ValueAccessor());
			manager = new TweenManager();
			Tween.to(alpha, -1, duration).target(0)
					.ease(TweenEquations.easeOutQuad).start(manager);
		}
    
	    
	    private void drawTransition(float delta) {
	        if (alpha.getValue() > 0) {
	            manager.update(delta);
	            Gdx.gl.glEnable(GL30.GL_BLEND);
	            Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
	            shapeRenderer.begin(ShapeType.Filled);
	            shapeRenderer.setColor(1, 1, 1, alpha.getValue());
	            shapeRenderer.rect(0, 0, 960, 720);
	            shapeRenderer.end();
	            Gdx.gl.glDisable(GL30.GL_BLEND);

	        }
	    }
	    
	    private void prepareFonts(){
	    	AssetLoader.fontUpheavalGray.setColor(1, 1, 1, 1);
	    	AssetLoader.fontUpheavalGreen.setColor(1, 1, 1, 1);
	    	AssetLoader.fontUpheavalRed.setColor(1, 1, 1, 1);
	    	AssetLoader.fontUpheaval.setColor(1, 1, 1, 1);
	    }
	    public Button getAgainButton() {
			return againButton;
		}

		public Button getExitButton() {
			return exitButton;
		}
	
		public Button getPauseButton() {
			return pauseButton;
		}
		
		public Button getResumeButton() {
			return resumeButton;
		}
		
		public Button getBuyButton() {
			return buyButton;
		}
		
		public Color getDefaultBg() {
			return defaultBg;
		}

		public void setDefaultBg(Color defaultBg) {
			this.defaultBg = defaultBg;
		}


}
