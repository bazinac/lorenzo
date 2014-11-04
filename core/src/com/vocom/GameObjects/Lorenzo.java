package com.vocom.GameObjects;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.vocom.Helpers.SoundController;

public class Lorenzo {

	
	private Vector2 position;
	private Vector2 defPosition;
	private Vector2 velocity;
    private Vector2 acceleration;
    
    private Vector2 originalVelocity;

    private float rotation;
    private int width;
    private float height;   
	private int jumpGain;
    private int flyGain; 
    private boolean freeJumpMode;

	private float lastY;
    private boolean isAlive;
    private boolean isLanded;
	private boolean isJumped;
    private boolean isFlying;
    private boolean isSunken;
    private boolean lardDepleted;
    private boolean isFrozen;
    
    private int lardLevel;
    
    public static final int DEFAULT_FLYGAIN = -240;
    public static final int DEFAULT_JUMPGAIN = -210;
    
    public static final int DEFAULT_LARDLEVEL = 100;
    public static final int DEFAULT_LARDMAXLEVEL = 120;
    public static final int DEFAULT_LARDFLYLOSS = 5;
    
    public enum MessageType {
    	NEGATIVE, POSITIVE, NEUTRAL
    }
    private MessageType msgType;
    private String msg;
    
	private Circle boundingCircle;
    private Rectangle landingPad;
    
    private SoundController sound;
    
    ScheduledExecutorService scheduler;
    ScheduledFuture<?> future;
    
    
    public Lorenzo(float x, float y, int width, int height, SoundController sound) {
        this.width = width;
        this.height = height;
        this.lastY = y;
        
        this.sound = sound;
        
        scheduler = Executors.newScheduledThreadPool(1);
        
        position = new Vector2(x, y);
        defPosition = new Vector2(x, y);
        velocity = new Vector2(0, 0);
        originalVelocity = new Vector2(0, 0);
        acceleration = new Vector2(0, 250);        
        
        lardLevel = DEFAULT_LARDLEVEL;
        jumpGain = DEFAULT_JUMPGAIN;
        flyGain = DEFAULT_FLYGAIN;
        
        freeJumpMode = false;
        
        boundingCircle = new Circle();
        boundingCircle.set(position.x + width/2, position.y + height/2, ((height+width)/4.75f));
        
        landingPad = new Rectangle();
        landingPad.set(position.x+30, position.y+height/2f, width-40 , height/2.36f);
        
        isAlive = true;
        isFrozen = true;
    }

    public void update(float delta) {

    	//State update0
        velocity.add(acceleration.cpy().scl(delta));
        position.add(velocity.cpy().scl(delta));
        
        if (velocity.y > 300) {
            velocity.y = 300;
        }

        // Ceiling check
        if (position.y < 1) {
            position.y = 0;
            velocity.y = 100;
            acceleration.y = 150;
        }
        
        // Flying check
        if (isFlying == true){
        	if (velocity.y > 0) this.setFlying(false);
        }
       
        // Bounding circle (approx.)
        boundingCircle.set(position.x + width/2, position.y + height/2, ((height+width)/4.75f));
        landingPad.set(position.x+40, (position.y+height/2f), width-30 , height/2.36f);
    }

    public void updateReady(float runTime) {
    	
    	isFrozen = true;
        position.y = 2 * (float) Math.sin(7 * runTime) + lastY;
    }
    
    public void updatePaused(float runTime) {
    }   
        

    public void onClick() {
        if (isAlive && !isFrozen) {        	
        	if (isJumped){
        		sound.playFlySound();       		
        		isFlying = true;        		
        		if (!isFreeJumpMode()) decreaseFat(DEFAULT_LARDFLYLOSS);
                velocity.y = flyGain;        	
        	}
        	else{
                isJumped = true;
                velocity.y = jumpGain;         
        	}

        }
        isFrozen = false;
    }

	public void decreaseFat(int by) {
		if ((lardLevel - by) > 0 ){
			lardLevel = lardLevel - by;			
		}
		else {
			lardLevel = 0;
			setLardDepleted(true);
		}
		setMsg("-"+String.valueOf(by), MessageType.NEGATIVE);
	}
	
	public void increaseFat(int by) {
		if ((lardLevel + by) <= DEFAULT_LARDMAXLEVEL){
			lardLevel = lardLevel + by;
			
		}
		else lardLevel = DEFAULT_LARDMAXLEVEL; 
		setMsg("+"+String.valueOf(by), MessageType.POSITIVE);
	}
	
	public MessageType getMsgType() {
		return msgType;
	}

	public void changeFat(int by) {
		String msg = "";
		MessageType msgType = null;
		
		if (by == 0){
			msgType = MessageType.NEUTRAL;
		}
		else if (by > 0){ 
			msg = "+";
			msgType = MessageType.POSITIVE;
		}
		else if (by < 0){ 

			msgType = MessageType.NEGATIVE;
		}
		
		
		if ((lardLevel + by) >= DEFAULT_LARDMAXLEVEL){
			lardLevel = DEFAULT_LARDMAXLEVEL; 
		} 
		else if ((lardLevel + by) <= 0)
		{
			lardLevel = 0;
		}
		else lardLevel = lardLevel + by;
				
		msg = msg + String.valueOf(by);
		setMsg(msg, msgType);
	}
    
    public void land() {
    	isJumped = false;
    	isFlying = false;
    	isLanded = true;
        velocity.y = 0;
        acceleration.y = 0;
    }

    public void unland() {
    	isLanded = false;
    	isJumped = true;
    	acceleration.y = 250;

    }   
    
    public void die() {
        isAlive = false;
        velocity.y = 0;
    }
    
    public void pause() {
    	originalVelocity = velocity;
    	velocity.x = 0;
        velocity.y = 0;
    }
    
    public void resume() {
    	velocity = originalVelocity;
    }

    public void bounceDown() {
    	isFlying = false;
    	isLanded = false;
    	isJumped = true;    	
    	velocity.y = 225;
    }

    public void onRestart() {
        rotation = 0;        
        position = defPosition;
        velocity.x = 0;
        velocity.y = 0;
        originalVelocity  = new Vector2(0, 0);
        acceleration = new Vector2(0, 250); 
        
        isAlive = true;
        isFrozen = true;
        isJumped = false;
    	isFlying = false;
        isSunken = false;
        
        lardDepleted = false;
        lardLevel = 100;
    }
    
    public void correctYpos(float platformY){
    	float Ydif = (position.y + this.height) - (this.landingPad.getY() - this.landingPad.getHeight());
    	
    	position.y = platformY - Ydif + 1;   
    	
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getRotation() {
        return rotation;
    }

    public Circle getBoundingCircle() {
        return boundingCircle;
    }
    
    public Rectangle getLandingPad() {
        return landingPad;
    }

    public boolean isAlive() {
        return isAlive;
    }
    
    public boolean isJumped() {
		return isJumped;
	}

	public void setJumped(boolean isJumped) {
		this.isJumped = isJumped;
	}

	public boolean isFlying() {
		return isFlying;
	}

	public void setFlying(boolean isFlying) {
		this.isFlying = isFlying;
	}

	public boolean isLanded() {
		return isLanded;
	}

	public void setLanded(boolean isLanded) {
		this.isLanded = isLanded;
	}
	
	public boolean isSunken() {
		return isSunken;
	}

	public void setSunken(boolean isSunken) {
		this.isSunken = isSunken;
	}
	
    public int getLardLevel() {
		return lardLevel;
	}

	public void setLardLevel(int lardLevel) {
		this.lardLevel = lardLevel;
	}

	public boolean isLardDepleted() {
		return lardDepleted;
	}

	public void setLardDepleted(boolean lardDepleted) {
		this.lardDepleted = lardDepleted;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg, MessageType msgType) {
		this.msg = msg;
		this.msgType = msgType;				
	   
	    Runnable clearMsgTask = new clearMsgTask();
	    
	    if (future != null) {
	    	if (!future.isDone()) future.cancel(false);	    
	    }
	    
	    future = scheduler.schedule(clearMsgTask, 750, TimeUnit.MILLISECONDS);		    
	}
	
	private class clearMsgTask implements Runnable {
		    @Override 
		    public void run() {
		      msg = "";
        	  msgType = null;
		    }
	  }
	
    public int getJumpGain() {
		return jumpGain;
	}

	public void setJumpGain(int jumpGain) {
		this.jumpGain = jumpGain;
	}

	public int getFlyGain() {
		return flyGain;
	}

	public void setFlyGain(int flyGain) {
		this.flyGain = flyGain;
	}
	
	public void resetGains() {
		this.jumpGain = DEFAULT_JUMPGAIN;
		this.flyGain = DEFAULT_FLYGAIN;		
		freeJumpMode = false;
	}
	
    public boolean isFreeJumpMode() {
		return freeJumpMode;
	}

	public void setFreeJumpMode(boolean freeJumpMode) {
		this.freeJumpMode = freeJumpMode;
	}

	
	
}