package com.vocom.GameWorld;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.vocom.GameObjects.Food;
import com.vocom.GameObjects.Food.EffectType;
import com.vocom.GameObjects.Food.Kind;
import com.vocom.GameObjects.Lorenzo;
import com.vocom.GameObjects.ScrollHandler;
import com.vocom.Helpers.AssetLoader;
import com.vocom.Helpers.SoundController;
import com.vocom.Lorenzo.LorenzoGame;
import com.vocom.Screens.MenuScreen;

public class GameWorld {

    private Lorenzo lorenzo;
    private ScrollHandler scroller;
    private GameState currentState;
    private LorenzoGame game; 

    public int gameWidth;
    public int gameHeight;
    
    private SoundController sound;
    
    private boolean specialTriggered = false;
    private String specialBgColor;
    
    ScheduledExecutorService schedulerW;
    ScheduledFuture<?> futureAmanitaRevert;
    ScheduledFuture<?> futureBeerRevert;
    
	public enum GameState {
       READY, RUNNING, PAUSED, GAMEOVER, HIGHSCORE
    }
    
    private int score = 0;   
    private int phantomScore = 0;
    private int lvl = 0;
    public static final int BASE_SCROLL_SPEED = -225;
    public static final int MAX_LEVEL = 15;
    public static final int LEVEL_SPEED_INCREMENT = 10;
    
    public GameWorld(int gameWidth, int gameHeight, LorenzoGame g) {
    	
    	this.gameHeight = gameHeight;
    	this.gameWidth = gameWidth;
    	this.game = g;
    	
    	schedulerW = Executors.newScheduledThreadPool(1);
    	
    	sound = new SoundController(!AssetLoader.getMuteStatus());
		sound.startBalticLevity();
		
		scroller = new ScrollHandler(this);	
		lorenzo = new Lorenzo(10, (gameHeight/2)-118, 95, 125, sound);		
		
		currentState = GameState.READY;	
		
    }
    
    public void update(float delta) {

        switch (currentState) {
        case READY:      
            updateReady(delta);
            break;  
        case PAUSED:
            updatePaused(delta);
            break; 
        case RUNNING:
        case GAMEOVER:
            updateRunning(delta);
            break;
        default:
            break;
        }    
    }

    private void updateReady(float delta) {
        lorenzo.updateReady(delta);
    }
    
    private void updatePaused(float delta) {
        lorenzo.updatePaused(delta); 
    }

    public void updateRunning(float delta) {
        // Delta cap, kdyby se to sekalo
        if (delta > .15f) {
            delta = .15f;
        }
        
        if (lorenzo.getY() > this.gameHeight) {lorenzo.setSunken(true);}        
        
        // death check
        if (lorenzo.isAlive())
        {
        	if (lorenzo.isLardDepleted() || lorenzo.isSunken()){
        	
        	lorenzo.die();
        	clearAllSpecialEffects();
        	scroller.stop();            
            sound.playDeathSound();
            currentState = GameState.GAMEOVER;
            
            AssetLoader.setLastScore(score);
            if (score > AssetLoader.getHighScore()) {
                AssetLoader.setHighScore(score);
                currentState = GameState.HIGHSCORE;
            }
            
        	}
        }
        
        lorenzo.update(delta);
        scroller.update(delta);
        
        // check for crash and for score increment, or pickup
        if (lorenzo.isAlive()){
	        char platformCrashState = scroller.crashedIntoPlatform(lorenzo);      
	        switch (platformCrashState) {
	        case 'L':
	        		lorenzo.land();
	                break;
	        case 'B': 
	        		lorenzo.bounceDown();
	                break;
	        case '0':
	        	    lorenzo.unland();
	        default: break;
	        }
	        
	        Food pickedFood = scroller.hasPickedUp(lorenzo);
	        if (pickedFood != null){
	        	if (pickedFood.getEffectType() == EffectType.BONUS || pickedFood.getEffectType() == EffectType.NEUTRAL) sound.playBonusSound();;
	        	if (pickedFood.getEffectType() == EffectType.MALUS) sound.playMalusSound();
	        	if (pickedFood.getEffectType() == EffectType.SPECIAL) {
	        		specialTriggered = true; 
	        		handleSpecialEffect(pickedFood.getKind());
	        	}       	
	        	lorenzo.changeFat(pickedFood.getLardEffect());	        	
	        }
	        
	        if (scroller.checkForScoreIncreasement(lorenzo)){
	        	addScore(1);
	        	
	        	if (score == AssetLoader.FREE_SCORE_CAP) {
	        		if (AssetLoader.PREMIUM == false) {
	        			lorenzo.die();
	                	clearAllSpecialEffects();
	                	scroller.stop();            
	                    sound.playDeathSound();
	                    currentState = GameState.GAMEOVER;	
	                    AssetLoader.setLastScore(score);
	                    if (score > AssetLoader.getHighScore()) {
	                        AssetLoader.setHighScore(score);
	                        currentState = GameState.HIGHSCORE;
	                    }
	        		}
	        	}
	        }
	        
	        //check for scroll speed update
	        if (phantomScore > 19) {
	        	if (lvl <= MAX_LEVEL) {
		        	lvl++;
			        scroller.scrollSpeed = scroller.scrollSpeed + LEVEL_SPEED_INCREMENT;		        	
			        scroller.resetScrollSpeedForAll();	        	
	        	}
		        phantomScore = 0;
	        }
	        
        }

        
    }
    
    private void handleSpecialEffect(Kind knd){
    	switch (knd) {
        case AMANITA:
        		sound.playAmanitaMusic();
        		this.specialBgColor = "PINK+PURPLE+GREEN";
        		scroller.scrollSpeed =  BASE_SCROLL_SPEED - 100 + (LEVEL_SPEED_INCREMENT * lvl);
        		scroller.resetScrollSpeedForAll();
        		lorenzo.setFreeJumpMode(true);
        	    
        	    Runnable arTask = new AmanitaRevertTask();    
        		if (futureAmanitaRevert != null) {
        			if (!futureAmanitaRevert.isDone()) futureAmanitaRevert.cancel(false);	    
        		}    	  
        	    futureAmanitaRevert = schedulerW.schedule(arTask, 11000, TimeUnit.MILLISECONDS);	
        	    
                break;
        case BEER: 
        		sound.playBeerMusic();
        		this.specialBgColor = "BEER"; 
        		scroller.scrollSpeed =  BASE_SCROLL_SPEED + 50 + (LEVEL_SPEED_INCREMENT * lvl);
        		scroller.resetScrollSpeedForAll();
        		lorenzo.setFlyGain(-300);
        		lorenzo.setJumpGain(-320);
        		lorenzo.setFreeJumpMode(true);final
        		
        		Runnable brTask = new AmanitaRevertTask(); 
        		if (futureBeerRevert != null) {
        			if (!futureBeerRevert.isDone()) futureBeerRevert.cancel(false);	    
        		}   
        	    futureBeerRevert = schedulerW.schedule(brTask, 11000, TimeUnit.MILLISECONDS);	
        	    
        		break;
		default:
			break;
		}
    	
    }
    
    class AmanitaRevertTask  implements Runnable {
    	
	    @Override 
        public void run() {
	        sound.startBalticLevity();
	          clearSpecialBgColor();
	          scroller.scrollSpeed = BASE_SCROLL_SPEED + (LEVEL_SPEED_INCREMENT * lvl);
	          scroller.resetScrollSpeedForAll();
	          lorenzo.resetGains(); 
	          specialTriggered = false;
	 	   }
    }

    class BeerRevertTask  implements Runnable {
	    @Override 
        public void run() {
        	 sound.startBalticLevity();
        	  clearSpecialBgColor();
        	  scroller.scrollSpeed = BASE_SCROLL_SPEED + (LEVEL_SPEED_INCREMENT * lvl);
        	  scroller.resetScrollSpeedForAll();
        	  lorenzo.resetGains(); 
          	  specialTriggered = false;
        	}
    }
    
    public void returnToMenu(){
    	sound.stopBalticLevity();
    	sound.stopBeerMusic();
    	sound.stopAmanitaMusic();
    	game.setScreen(new MenuScreen(game));
    }    
    
    public void goBuy(){
    	Gdx.net.openURI("market://details?id=com.vocom.piggy.android_premium");   	
    }  
    
    public Lorenzo getLorenzo() {
        return lorenzo;
    }
    
    public ScrollHandler getScroller() {
        return scroller;
    }
    
    public int getScore() {
        return score;
    }
    
    public boolean isHighScore() {
        return currentState == GameState.HIGHSCORE;
    }

    public void addScore(int increment) {
        score += increment;
        phantomScore +=increment;
    }
    
    public void start() {
        currentState = GameState.RUNNING;
    }
    
    public void paused(){
    	
    	clearAllSpecialEffects();
    	
    	scroller.stop();
    	lorenzo.pause();
        currentState = GameState.PAUSED;
    }
    
    public void resume(){
    	scroller.go();
    	lorenzo.resume();
        currentState = GameState.RUNNING;
    }

    public void ready() {
        currentState = GameState.READY;
    }

    public void restart() {
        if (specialTriggered){
         	clearAllSpecialEffects();
        }
    	score = 0;
        phantomScore = 0;
        lvl = 0;
        
    	currentState = GameState.READY;        
        lorenzo.onRestart();
        scroller.onRestart();
        clearSpecialBgColor();
        currentState = GameState.READY;
    }

	private void clearAllSpecialEffects() {
		if (futureAmanitaRevert != null) {
			if (!futureAmanitaRevert.isDone()) futureAmanitaRevert.cancel(false);	    
		}    	    

		if (futureBeerRevert != null) {
			if (!futureBeerRevert.isDone()) futureBeerRevert.cancel(false);	    
		}    
		
		sound.stopBeerMusic();
		sound.stopAmanitaMusic();
		sound.startBalticLevity();
		
		lorenzo.resetGains(); 
		scroller.scrollSpeed = BASE_SCROLL_SPEED + (LEVEL_SPEED_INCREMENT * lvl);
		scroller.resetScrollSpeedForAll();
		clearSpecialBgColor();       	

		specialTriggered = false;
	}
        
    
    public boolean isGameOver() {
        return currentState == GameState.GAMEOVER;
    }
    
    public boolean isReady() {
        return currentState == GameState.READY;
    }

	public boolean isRunning() {
		return  currentState == GameState.RUNNING;
	}	

	public boolean isPaused() {
		return  currentState == GameState.PAUSED;
	}

	
	public String getSpecialBgColor() {
		return this.specialBgColor;
	}
	
	public void clearSpecialBgColor() {
		this.specialBgColor = null;
	}
	
    public int getLvl() {
		return lvl;
	}


}

