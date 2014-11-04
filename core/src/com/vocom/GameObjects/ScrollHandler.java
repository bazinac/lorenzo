package com.vocom.GameObjects;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.vocom.GameObjects.Food.EffectType;
import com.vocom.GameObjects.Food.PlaceType;
import com.vocom.GameWorld.GameWorld;

public class ScrollHandler {

    private Cplatform platform1, platform2, platform3;
	private Food food1, food3, food5; // onplatforms
    private Food food2, food4, food6;// aerials 
    
	
    public static final int PLATFORM_GAP = 300;
    public static final int BASE_SCROLL_SPEED = -225;
    
    public int scrollSpeed = -225;
    
    @SuppressWarnings("unused")
	private GameWorld gameWorld;
    @SuppressWarnings("unused")
    private int gameWidth;
    private int gameHeight;    
    
    ScheduledExecutorService schedulerScrl;
    ScheduledFuture<?> futureScrl;

    public ScrollHandler(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.gameHeight = gameWorld.gameHeight;
        this.gameWidth = gameWorld.gameWidth;
        
        schedulerScrl = Executors.newScheduledThreadPool(1);

        platform1 = new Cplatform(0, (gameHeight/2), scrollSpeed, gameHeight, true);
        platform2 = new Cplatform(475, 0 , scrollSpeed, gameHeight, false);
        platform3 = new Cplatform(950, 0, scrollSpeed, gameHeight, false);
        
        //fake init - jen kvuli sirce :) bad practice jako svine
        food5 = new Food(0,0,0,gameHeight,PlaceType.ONPLATFORM, true);
        		
        food1 = new Food(0 + ((platform1.width-food5.width) / 2) , platform1.getY()-food5.height, scrollSpeed, gameHeight, PlaceType.ONPLATFORM, true);
        food3 = new Food(475 + ((platform2.width-food1.width) / 2) , platform2.getY()-food1.height, scrollSpeed, gameHeight, PlaceType.ONPLATFORM, false); 
        food5 = new Food(950 + ((platform3.width-food3.width) / 2), platform3.getY()-food3.height, scrollSpeed, gameHeight, PlaceType.ONPLATFORM, false); 
        
        food2 = new Food(325 - (food1.getWidth()/2), 0, scrollSpeed, gameHeight, PlaceType.AERIAL, false); 
        food4 = new Food(food2.getX() + (platform2.width + PLATFORM_GAP), 0, scrollSpeed, gameHeight, PlaceType.AERIAL, false);
        food6 = new Food(food4.getX() + (platform3.width + PLATFORM_GAP), 0, scrollSpeed, gameHeight, PlaceType.AERIAL, false); 

    }

    public void resetScrollSpeedForAll() {
    	platform1.resetScrollSpeed(scrollSpeed);
    	platform2.resetScrollSpeed(scrollSpeed);
    	platform3.resetScrollSpeed(scrollSpeed);
    	
    	food1.resetScrollSpeed(scrollSpeed);
    	food2.resetScrollSpeed(scrollSpeed);
    	food3.resetScrollSpeed(scrollSpeed);
    	food4.resetScrollSpeed(scrollSpeed);
    	food5.resetScrollSpeed(scrollSpeed);
    	food6.resetScrollSpeed(scrollSpeed);
    
    }

    public void update(float delta) {
    	
    	boolean specialSummoned = false;
    	
        // Update objects
        platform1.update(delta);
        platform2.update(delta);
        platform3.update(delta);
        
        food1.update(delta);
        food2.update(delta);
        food3.update(delta);
        food4.update(delta);
        food5.update(delta);
        food6.update(delta);

        // Check if some pforms need to be reseted
        if (platform1.isScrolledLeft()) {
            platform1.reset(platform3.getTailX() + PLATFORM_GAP);
            food1.reset(food5.getX() + PLATFORM_GAP + (platform1.width),platform1.getY()-food1.height);
            if (food1.getEffectType() == EffectType.SPECIAL) specialSummoned = true;
        } else if (platform2.isScrolledLeft()) {
            platform2.reset(platform1.getTailX() + PLATFORM_GAP);            
            food3.reset(food1.getX() + PLATFORM_GAP + (platform2.width),platform2.getY()-food3.height);
            if (food3.getEffectType() == EffectType.SPECIAL) specialSummoned = true;
        } else if (platform3.isScrolledLeft()) {
            platform3.reset(platform2.getTailX() + PLATFORM_GAP);
            food5.reset(food3.getX() + PLATFORM_GAP + (platform3.width),platform3.getY()-food5.height);
            if (food5.getEffectType() == EffectType.SPECIAL) specialSummoned = true;
        }
        
        // Check if some foods need to be reseted  
        if (food2.isScrolledLeft()) {
            food2.reset(food6.getX() + ((platform3.width + PLATFORM_GAP)));
            if (food2.getEffectType() == EffectType.SPECIAL) specialSummoned = true;
        } else if (food4.isScrolledLeft()) {
        	food4.reset(food2.getX() + ((platform2.width + PLATFORM_GAP)));
            if (food4.getEffectType() == EffectType.SPECIAL) specialSummoned = true;
        } else if (food6.isScrolledLeft()) {
        	food6.reset(food4.getX() + ((platform1.width + PLATFORM_GAP)));
            if (food6.getEffectType() == EffectType.SPECIAL) specialSummoned = true;
        }        

        if (specialSummoned) {
        	setSpecialStatus(false);
        	
    	    Runnable specialsTask = new clearSpecialsTask();   
    	    if (futureScrl != null) {
    	    	if (!futureScrl.isDone()) futureScrl.cancel(false);	    
    	    }
    	    futureScrl = schedulerScrl.schedule(specialsTask, 14000, TimeUnit.MILLISECONDS);		
        }

    }
    
	private class clearSpecialsTask implements Runnable {
	    @Override 
	    public void run() {
	    	setSpecialStatus(true);

	    }
  }

    public void stop() {
        platform1.stop();
        platform2.stop();
        platform3.stop();
        
        food1.stop();
        food2.stop();
        food3.stop();
        food4.stop();
        food5.stop();
        food6.stop();
    }
    
    public void go() {
        platform1.go();
        platform2.go();
        platform3.go();
        
        food1.go();
        food2.go();
        food3.go();
        food4.go();
        food5.go();
        food6.go();
    }
    
    public char crashedIntoPlatform(Lorenzo pig) {
    	char p1crashState = platform1.isCrashedBy(pig);
    	char p2crashState = platform2.isCrashedBy(pig);
    	char p3crashState = platform3.isCrashedBy(pig);
    	
    	if (p1crashState != '0') return p1crashState;
    	if (p2crashState != '0') return p2crashState; 
    	if (p3crashState != '0') return p3crashState; 
    	return '0';
    }
    
    public Food hasPickedUp(Lorenzo pig) {
    	Food f1pickup = food1.isCrashedBy(pig);
    	Food f2pickup = food2.isCrashedBy(pig);
    	Food f3pickup = food3.isCrashedBy(pig);
    	Food f4pickup = food4.isCrashedBy(pig);
    	Food f5pickup = food5.isCrashedBy(pig);
    	Food f6pickup = food6.isCrashedBy(pig);
    	
    	if (f1pickup != null) {
    		food1.forceNothing();	
    		return f1pickup;
    	}
    	if (f2pickup != null) {
    		food2.forceNothing();	
    		return f2pickup;
    	}
    	if (f3pickup != null) {
    		food3.forceNothing();	
    		return f3pickup;
    	}
    	if (f4pickup != null) {
    		food4.forceNothing();	
    		return f4pickup;
    	}
    	if (f5pickup != null) {
    	    food5.forceNothing();	
    		return f5pickup;
    	}
    	if (f6pickup != null) {
    	 	food6.forceNothing();	
    		return f6pickup;
    	}
    	
    	return null;
    }
    
    
    public boolean checkForScoreIncreasement(Lorenzo pig) {
        // Check if some pfroms are passed
        if (!platform1.isMet() && (platform1.getX() + (platform1.getWidth() / 2)) < (pig.getX() + pig.getWidth())) {
            platform1.setMet(true);
            return true;
        }
        if (!platform2.isMet() && (platform2.getX() + (platform2.getWidth() / 2)) < (pig.getX() + pig.getWidth())) {
            platform2.setMet(true);
            return true;
        }
        if (!platform3.isMet() && (platform3.getX() + (platform3.getWidth() / 2)) < (pig.getX() + pig.getWidth())) {
            platform3.setMet(true);
            return true;
        }
        return false;
    }

    public void setSpecialStatus(boolean order){
    	food1.setSpecialsAllowed(order);
    	food2.setSpecialsAllowed(order);
    	food3.setSpecialsAllowed(order);
    	food4.setSpecialsAllowed(order);
    	food5.setSpecialsAllowed(order);
    	food6.setSpecialsAllowed(order);
    }
    
    public void onRestart() {
    	scrollSpeed = BASE_SCROLL_SPEED;
    	
        platform1.onRestart(0,scrollSpeed);
        platform2.onRestart(platform1.getTailX() + PLATFORM_GAP, scrollSpeed);
        platform3.onRestart(platform2.getTailX() + PLATFORM_GAP, scrollSpeed);
        
        food1.onRestart((50), platform1.getY()-food5.height,scrollSpeed, true);
        food3.onRestart(food1.getX() + PLATFORM_GAP + platform2.width,platform2.getY()-food1.height, scrollSpeed, false);
        food5.onRestart(food3.getX() + PLATFORM_GAP + platform3.width,platform3.getY()-food5.height, scrollSpeed, false);
        
        food2.onRestart(325 - (food1.getWidth()/2), scrollSpeed, false);
        food4.onRestart(food2.getX() + (platform2.width + PLATFORM_GAP), scrollSpeed, false); 
        food6.onRestart(food4.getX() + (platform3.width + PLATFORM_GAP), scrollSpeed, false); 
        
    }
    
    public Cplatform getPlatform1() {
		return platform1;
	}

	public Cplatform getPlatform2() {
		return platform2;
	}

	public Cplatform getPlatform3() {
		return platform3;
	}
	
    public Food getFood1() {
		return food1;
	}

	public Food getFood3() {
		return food3;
	}

	public Food getFood5() {
		return food5;
	}

	public Food getFood2() {
		return food2;
	}

	public Food getFood4() {
		return food4;
	}
	
	public Food getFood6() {
		return food6;
	}
	
}