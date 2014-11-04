package com.vocom.Helpers;

public class SoundController {
	private boolean muted; 
	
	public SoundController(boolean muted) {
		this.muted = muted;        
	}
	
	public void startBalticLevity() {
		if (!muted){
		AssetLoader.balticLevity.setLooping(true);
		AssetLoader.balticLevity.play();	
		}	
	}
	
	public void stopBalticLevity() {
		if (!muted){
		AssetLoader.balticLevity.setLooping(false);
		AssetLoader.balticLevity.stop();	
		}
	}
	
	public void startLittleFaith() {
		if (!muted){
			AssetLoader.littleFaith.play();	
		}	
	}
	
	public void stopLittleFaith() {
		if (!muted){
			if (AssetLoader.littleFaith.isPlaying()) AssetLoader.littleFaith.stop();
		}
	}
	
	public void playBeerMusic() {
		if (!muted){
			if (AssetLoader.balticLevity.isPlaying()) stopBalticLevity();
			AssetLoader.beerMusic.play();	
		}	
	}
	
	public void stopBeerMusic() {
		if (!muted){
			if (AssetLoader.beerMusic.isPlaying()) AssetLoader.beerMusic.stop();
		}
	}
	
	
	public void playAmanitaMusic() {
		if (!muted){
			if (AssetLoader.balticLevity.isPlaying()) stopBalticLevity();
			AssetLoader.amanitaMusic.play();	
		}	
	}
	public void stopAmanitaMusic() {
		if (!muted){
			if (AssetLoader.amanitaMusic.isPlaying()) AssetLoader.amanitaMusic.stop();;
		}
	}
	
	public void playFlySound() {
		if (!muted){			
			AssetLoader.flight.play();	
		}
	}
		

	public void playDeathSound() {
		if (!muted){			
			AssetLoader.death.play();	
		}
	}
	
	public void playBonusSound() {
		if (!muted){			
			AssetLoader.bonus.play();	
		}	
	}
	
	public void playMalusSound() {
		if (!muted){			
			AssetLoader.malus.play();	
		}
	}	

}
