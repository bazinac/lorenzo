package com.vocom.GameObjects;


import java.util.Random;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Cplatform extends Scrollable {

    private Random r;
    
	private Rectangle body;
    
    public static final int PLATFORM_WIDTH = 175;
    public static final int PLATFORM_HEIGHT  = 31;
    public static final int PLATFORM_MIN_POS = 230;

    private int worldHeight;
    
    private boolean isMet = false;
    private boolean isInitial = false;
    private float initY;


    public Cplatform(float x, float y, float scrollSpeed, int worldHeight, boolean initial) {
    	super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT, scrollSpeed);
        
    	r = new Random();
        body = new Rectangle();
        this.worldHeight = worldHeight;
        this.isInitial = initial;
        
        //override na y
        if (!isInitial){
          position.y = r.nextInt(worldHeight - PLATFORM_MIN_POS) + PLATFORM_MIN_POS -PLATFORM_HEIGHT;          
          }
        else {
          initY = y;	
        }
    }
    
    @Override
    public void update(float delta) {

        super.update(delta);
        body.set(position.x, position.y, width, PLATFORM_HEIGHT);

    }
    
    
	@Override
    public void reset(float newX) {		
		super.reset(newX);		
   	
        position.y = r.nextInt(worldHeight - PLATFORM_MIN_POS) + PLATFORM_MIN_POS - PLATFORM_HEIGHT;

        isMet = false;
    }
	
	public void resetScrollSpeed(int scrollSpeed) {		
		super.velocity = new Vector2(scrollSpeed, 0);
    }	
	
    public char isCrashedBy(Lorenzo lor) {
    	//chary urcuji typ kolize - B - vodspoda, U - shora, L - pristani
    	
    	if (position.x < lor.getLandingPad().x + lor.getLandingPad().width){
    		
    		if (position.y >= (lor.getLandingPad().y + (lor.getLandingPad().height/1.3f))){
    			if (Intersector.overlaps(lor.getLandingPad(), body)){
    				// je potreba trosku ochcat pristani
    				lor.correctYpos(position.y);
    				return 'L';
    			} ;  
    		}
    		else {
    			if (Intersector.overlaps(lor.getBoundingCircle(), body)) return 'B';
    		} 			
    	} 

    	return '0';
    }
	
	public void onRestart(float x, float scrollSpeed) {
	    velocity.x = scrollSpeed;
	    reset(x);
	    if (this.isInitial) {position.y = this.initY;}
	    
	}
	
	public boolean isMet() {
	    return isMet;
	}

	public void setMet(boolean b) {
	    isMet = b;
	}
	
    public Rectangle getBoundingRectangle() {
        return body;
    }



}