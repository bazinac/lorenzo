package com.vocom.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.vocom.GameObjects.Lorenzo;
import com.vocom.GameWorld.GameRenderer;
import com.vocom.GameWorld.GameWorld;
import com.vocom.Screens.CreditsScreen;
import com.vocom.Screens.IntroScreen;
import com.vocom.Screens.MenuScreen;


public class InputHandler implements InputProcessor{

	private Lorenzo myLorenzo;
	private GameWorld myWorld;
	private MenuScreen menu;
	private IntroScreen intro;
	private CreditsScreen credits;
	private GameRenderer rend;
	
	private int clickCntr = 0;
		
    private float scaleFactorX;
    private float scaleFactorY;
    
	public InputHandler(GameWorld myWorld, GameRenderer rend, float scaleFactorX, float scaleFactorY) {
		this.menu = null;
		this.intro = null;
		this.credits = null;
	    this.myWorld = myWorld;
	    this.rend = rend;
	    this.myLorenzo = myWorld.getLorenzo();
	    
	    this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;
        
	}
	
	public InputHandler(MenuScreen menu, float scaleFactorX, float scaleFactorY) {

	    this.menu = menu;
	    
	    this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

	}
	
	public InputHandler(IntroScreen intro, float scaleFactorX, float scaleFactorY) {

	    this.intro = intro;
	    
	    this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

	}
	
	public InputHandler(CreditsScreen credits, float scaleFactorX, float scaleFactorY) {

	    this.credits = credits;
	    
	    this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

	}
	
	
	
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        
        //menu
        if (menu != null){
        	if (menu.getPlayButton().isTouchDown(screenX, screenY) || menu.getPlayButton().isClicked(screenX, screenY)){
        		menu.startNewGame();        		
            }
        	
        	if (menu.getExitButton().isTouchDown(screenX, screenY) || menu.getExitButton().isClicked(screenX, screenY)) {
        		Gdx.app.exit();
            }
        	
        	if (menu.getIntroButton().isTouchDown(screenX, screenY) || menu.getIntroButton().isClicked(screenX, screenY)){
        		menu.playIntro();      		
            }
        	
        	if (menu.getInfoButton().isTouchDown(screenX, screenY) || menu.getInfoButton().isClicked(screenX, screenY)){
        		menu.playCredits();      		
            }
        	
        	if (menu.getMuteButton().isTouchDown(screenX, screenY) || menu.getMuteButton().isClicked(screenX, screenY)){
        		if (AssetLoader.getMuteStatus()) {
        			AssetLoader.setMuteStatus(false);        		
        		}
        		else AssetLoader.setMuteStatus(true);  
            }
        	
        }
        else if (intro != null) 
        {
        	clickCntr++;
        	if (clickCntr > 5)  {
        		intro.skipIntro();
        	}
        }
        
        else if (credits != null)  {        	
        	clickCntr++;
        	if (clickCntr > 5)  {
        		credits.skipCredits();
        	}
    	}
        
        else {
	        if (myWorld.isHighScore() || myWorld.isGameOver()) {
		           	if (rend.getAgainButton().isTouchDown(screenX, screenY) || rend.getAgainButton().isClicked(screenX, screenY)){
		           		myWorld.restart();	        		
		            }
		           	else if (rend.getExitButton().isTouchDown(screenX, screenY) || rend.getExitButton().isClicked(screenX, screenY)){
		           		myWorld.returnToMenu();
		           	}		           	
		           	else if (rend.getBuyButton().isTouchDown(screenX, screenY) || rend.getBuyButton().isClicked(screenX, screenY)){
		           		myWorld.goBuy();
		           	}
		        }
	        
	         else if (myWorld.isPaused()) {		        	
	        	    if (rend.getResumeButton().isTouchDown(screenX, screenY) || rend.getResumeButton().isClicked(screenX, screenY)){
		           		myWorld.resume();	        		
		            }
		           	else if (rend.getExitButton().isTouchDown(screenX, screenY) || rend.getExitButton().isClicked(screenX, screenY)){
		           		myWorld.returnToMenu();
		           	}
		           	else if (rend.getBuyButton().isTouchDown(screenX, screenY) || rend.getBuyButton().isClicked(screenX, screenY)){
		           		myWorld.goBuy();
		           	}
			}
	        
	    
		    else if (myWorld.isReady()) {		        	
			      myWorld.start();
			      myLorenzo.onClick();
			}

	     
		    else if (myWorld.isRunning()) {		    
			    if (rend.getPauseButton().isTouchDown(screenX, screenY)) {
			    	myWorld.paused();
			    }			    
			    else myLorenzo.onClick();
		    }

        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        screenX = scaleX(screenX);
        screenY = scaleY(screenY);
        
        //menu
        if ((menu != null)){
        	menu.getPlayButton().isTouchUp(screenX, screenY);
        	menu.getExitButton().isTouchUp(screenX, screenY);
        	menu.getIntroButton().isTouchUp(screenX, screenY); 
        	menu.getInfoButton().isTouchUp(screenX, screenY); 
        	menu.getMuteButton().isTouchUp(screenX, screenY); 
        	
        }
        else if (intro != null) 
        { }
        
        else if (credits != null) 
        { }
        else {
        	if (myWorld.isHighScore() || myWorld.isGameOver()) {
            	rend.getAgainButton().isTouchUp(screenX, screenY);
            	rend.getExitButton().isTouchUp(screenX, screenY);
            	rend.getBuyButton().isTouchUp(screenX, screenY);
            }
        	
        	if (myWorld.isPaused()) {
            	rend.getResumeButton().isTouchUp(screenX, screenY);
            	rend.getExitButton().isTouchUp(screenX, screenY);
            	rend.getBuyButton().isTouchUp(screenX, screenY);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
    	// space
    	if (keycode == Keys.SPACE){     	
    		if (myWorld != null){
    			// space na kompu to taky zapne
    			if (myWorld.isReady()) {
 	                myWorld.start();
 	            }
 	
 	            myLorenzo.onClick();	
    		}
    	}	
    	
    	//back tlacitko
    	else if (keycode == Keys.BACK){
    		if (menu != null) Gdx.app.exit();
    		if (credits != null) credits.returnToMenu();
    		if (intro != null) intro.returnToMenu();
    		if (myWorld != null)myWorld.returnToMenu();
    		
    	}

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
    	return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

}
