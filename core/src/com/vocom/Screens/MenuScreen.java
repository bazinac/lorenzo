package com.vocom.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vocom.Helpers.AssetLoader;
import com.vocom.Helpers.InputHandler;
import com.vocom.Lorenzo.LorenzoGame;
import com.vocom.UI.Button;

public class MenuScreen implements Screen {


    private SpriteBatch batcher;
    private OrthographicCamera cam;
    private LorenzoGame game;    
    private TextureRegion buttonDown, buttonUp;
    private Sprite sprite;

	private Button playButton, exitButton, introButton, muteButton, infoButton;

	public MenuScreen(LorenzoGame g) {
        this.game = g;

		cam = new OrthographicCamera();
		
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float gameWidth = 960;
        float gameHeight = screenHeight / (screenWidth / gameWidth);

        cam.setToOrtho(true, gameWidth, gameHeight);
        
        Gdx.input.setInputProcessor(new InputHandler(this, screenWidth / gameWidth, screenHeight / gameHeight));
        Gdx.input.setCatchBackKey(true);
    }

    public void show() {
        initAssets();
        
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        
        float gameWidth = 960;
        float gameHeight = screenHeight / (screenWidth / gameWidth);
		
        playButton = new Button(gameWidth - buttonUp.getRegionWidth() - 30, buttonUp.getRegionHeight() + 50, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
	    introButton = new Button(gameWidth - buttonUp.getRegionWidth() - 30 , 2*(buttonUp.getRegionHeight()) + 75, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
	    muteButton = new Button(gameWidth - buttonUp.getRegionWidth()- 30 , 3*(buttonUp.getRegionHeight()) + 100, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
	    infoButton = new Button(gameWidth - buttonUp.getRegionWidth() - 30, 4*(buttonUp.getRegionHeight()) + 125, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);
	    exitButton = new Button(gameWidth - buttonUp.getRegionWidth() - 30, 5*(buttonUp.getRegionHeight()) + 150, buttonUp.getRegionWidth(), buttonUp.getRegionHeight(), buttonUp, buttonDown);

	    sprite.setBounds(0,0,gameWidth,gameHeight);
	    
           
        batcher = new SpriteBatch();  
        batcher.setProjectionMatrix(cam.combined);
    }

    public void startNewGame() {
    	 if (!AssetLoader.getIntroPlayedStatus()){
    		 game.setScreen(new IntroScreen(game, false));
    	 }
    	 else {
    		 game.setScreen(new GameScreen(game));
    	 }
    }

    public void playIntro() {
   		 game.setScreen(new IntroScreen(game, true));
    }

    public void playCredits() {
  		 game.setScreen(new CreditsScreen(game));
   }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        batcher.begin();
        	sprite.draw(batcher);
        	
        	playButton.draw(batcher);
        	introButton.draw(batcher);
        	exitButton.draw(batcher);
        	muteButton.draw(batcher);
        	infoButton.draw(batcher);
        	
        	AssetLoader.fontUpheavalGray.setColor(1, 1, 1, 1);
            AssetLoader.fontUpheavalGray.draw(batcher, "PLAY", playButton.getX()+playButton.getWidth()/2 - 50, playButton.getY()+15);
            AssetLoader.fontUpheavalGray.draw(batcher, "INTRO", introButton.getX()+introButton.getWidth()/2 - 50, introButton.getY()+15);
            AssetLoader.fontUpheavalGray.draw(batcher, "INFO", infoButton.getX()+infoButton.getWidth()/2 - 50, infoButton.getY()+15);
            AssetLoader.fontUpheavalGray.draw(batcher, "EXIT", exitButton.getX()+exitButton.getWidth()/2 - 50, exitButton.getY()+15);
            
            String muteButString;            

            if (AssetLoader.getMuteStatus())
            	{muteButString = "SOUND: ON";}
            else {muteButString = "SOUND: OFF";}      
            
            AssetLoader.fontUpheavalGray.draw(batcher, muteButString, muteButton.getX()+muteButton.getWidth()/2 - 50, muteButton.getY()+15);

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
    
	private void initAssets() {	       
        
		buttonDown = AssetLoader.buttonDown;
		buttonUp = AssetLoader.buttonUp;
		sprite = new Sprite(AssetLoader.gameLogo);
		
   }

    public Button getPlayButton() {
		return playButton;
	}

	public Button getExitButton() {
		return exitButton;
	}
	
	public Button getIntroButton() {
		return introButton;
	}
	
	public Button getInfoButton() {
		return infoButton;
	}

	public Button getMuteButton() {
		return muteButton;
	}

}

