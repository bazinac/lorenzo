package com.vocom.Helpers;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



public class AssetLoader {
	
	public static boolean PREMIUM = false;
	public static final int FREE_SCORE_CAP = 100;
	
	public static Texture buttonTexture, buttonSmallTexture, vocomLogoTexture, bazinacLogoTexture, gameLogoTexture, lorenzoTexture, farmTexture, rainbowTexture, japanTexture,
							bgTexture, cplatformTexture, lardTexture, coinTexture, bonusesTexture, malusesTexture, neutralsTexture, specialsTexture;
	public static TextureRegion vocomLogo, bazinacLogo, gameLogo, lorenzo, lorenzoRunFront, lorenzoRunBack, lorenzoFly1, lorenzoFly2, farm, rainbow, japan, bg, cplatform, 
								lardEmpty, lardFull , scoreCoin, buttonUp, buttonDown, buttonSmallUp, buttonSmallDown,
								beef, burger, cake, fries, gummi, hamburger, chicken, choco, pizza, salami, wurst,
								apple, broccoli, cabbage, carrot, garlic, orange, onion, pear, raddish, tomato,
								almonds, boletus, bread, eggs, fish, cheese, potato, rice, spaghetti, sushi,
								beer, amanita;
	public static Animation lorenzoRunAnim, lorenzoFlyAnim;
	public static Sound death, flight, bonus, malus;
	public static Music balticLevity, littleFaith, amanitaMusic, beerMusic;
	public static BitmapFont fontUpheaval, fontUpheavalGray, fontUpheavalRed, fontUpheavalGreen;
	public static String build;
	private static Preferences prefs;
    
    public static void load() {
    	
    	build = "5.0";
        
    	// SPRITES, TEXTURES
    	
    	vocomLogoTexture = new Texture(Gdx.files.internal("data/vocomLogo.png"));
        vocomLogoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        vocomLogo = new TextureRegion(vocomLogoTexture, 0, 0, 442, 389);
        
    	bazinacLogoTexture = new Texture(Gdx.files.internal("data/bazinacLogo.png"));
        bazinacLogoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        bazinacLogo = new TextureRegion(bazinacLogoTexture, 0, 0, 442, 389);
        
    	gameLogoTexture = new Texture(Gdx.files.internal("data/gameLogo.png"));
    	gameLogoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    	gameLogo = new TextureRegion(gameLogoTexture, 0, 0, 672, 524);
    	gameLogo.flip(false, true);

        lorenzoTexture = new Texture(Gdx.files.internal("data/tex_lorenzo.png"));
        lorenzo = new TextureRegion(lorenzoTexture, 0, 0, 96, 125);
        lorenzo.flip(false, true);
        lorenzoRunFront = new TextureRegion(lorenzoTexture, 97, 0, 96, 125);
        lorenzoRunFront.flip(false, true);
        lorenzoRunBack = new TextureRegion(lorenzoTexture, 192, 0, 96, 125);
        lorenzoRunBack.flip(false, true);
        lorenzoFly1 = new TextureRegion(lorenzoTexture, 289, 0, 95, 125);
        lorenzoFly1.flip(false, true);
        lorenzoFly2 = new TextureRegion(lorenzoTexture, 385, 0, 96, 125);
        lorenzoFly2.flip(false, true);
        
        lardTexture =new Texture(Gdx.files.internal("data/lard.png"));
        lardEmpty = new TextureRegion(lardTexture, 0, 0, 74, 120);
        lardEmpty.flip(false, true);
        lardFull = new TextureRegion(lardTexture, 74, 0, 74, 120);
        lardFull.flip(false, true);
        
        coinTexture=new Texture(Gdx.files.internal("data/coin.png"));
        scoreCoin = new TextureRegion(coinTexture, 0, 0, 60, 60);
        scoreCoin.flip(false, true);
        
        farmTexture = new Texture(Gdx.files.internal("data/tex_farm.png"));
        farm = new TextureRegion(farmTexture, 0, 0, farmTexture.getWidth(), farmTexture.getHeight());
        farm.flip(false, true);
        
        rainbowTexture = new Texture(Gdx.files.internal("data/tex_rainbow.jpg"));
        rainbow = new TextureRegion(rainbowTexture, 0, 0, rainbowTexture.getWidth(), rainbowTexture.getHeight());
        rainbow.flip(false, true);
        
        japanTexture = new Texture(Gdx.files.internal("data/tex_japan.jpg"));
        japan = new TextureRegion(japanTexture, 0, 0, japanTexture.getWidth(), japanTexture.getHeight());
        japan.flip(false, true);
        
        bgTexture = new Texture(Gdx.files.internal("data/tex_bg.png"));
        bg = new TextureRegion(bgTexture, 0, 0, bgTexture.getWidth(), bgTexture.getHeight());
        bg.flip(false, true);
        
        cplatformTexture = new Texture(Gdx.files.internal("data/tex_platform.png"));
        cplatform = new TextureRegion(cplatformTexture, 0, 0, cplatformTexture.getWidth(), cplatformTexture.getHeight());
        cplatform.flip(false, true);  
        
        buttonTexture = new Texture(Gdx.files.internal("data/tex_button.png"));
        buttonTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        buttonUp = new TextureRegion(buttonTexture, 0, 0, 246, 50);
        buttonDown = new TextureRegion(buttonTexture, 0, 50, 246, 50);
        buttonUp.flip(false, true);
        buttonDown.flip(false, true);

        buttonSmallTexture = new Texture(Gdx.files.internal("data/tex_button_sml.png"));
        buttonSmallTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        buttonSmallUp = new TextureRegion(buttonSmallTexture, 0, 0, 80, 25);
        buttonSmallDown = new TextureRegion(buttonSmallTexture, 0, 25, 80, 25);
        buttonSmallUp.flip(false, true);
        buttonSmallDown.flip(false, true);
        
        bonusesTexture=new Texture(Gdx.files.internal("data/tex_bonuses.png"));
        beef = new TextureRegion(bonusesTexture, 0, 0, 75, 75);
        beef.flip(false, true);
        burger = new TextureRegion(bonusesTexture, 76, 0, 75, 75);
        burger.flip(false, true);
        cake = new TextureRegion(bonusesTexture, 151, 0, 75, 75);
        cake.flip(false, true);
        fries = new TextureRegion(bonusesTexture, 226, 0, 75, 75);
        fries.flip(false, true);
        gummi = new TextureRegion(bonusesTexture, 301, 0, 75, 75);
        gummi.flip(false, true);
        chicken = new TextureRegion(bonusesTexture, 0, 76, 75, 75);
        chicken.flip(false, true);
        choco = new TextureRegion(bonusesTexture, 76, 76, 75, 75);
        choco.flip(false, true);
        pizza = new TextureRegion(bonusesTexture, 151, 76, 75, 75);
        pizza.flip(false, true);
        salami = new TextureRegion(bonusesTexture, 226, 76, 75, 75);
        salami.flip(false, true);
        wurst = new TextureRegion(bonusesTexture, 301, 76, 75, 75);
        wurst.flip(false, true);

        malusesTexture=new Texture(Gdx.files.internal("data/tex_maluses.png"));
        apple = new TextureRegion(malusesTexture, 0, 0, 75, 75);
        apple.flip(false, true);
        broccoli = new TextureRegion(malusesTexture, 76, 0, 75, 75);
        broccoli.flip(false, true);
        cabbage = new TextureRegion(malusesTexture, 151, 0, 75, 75);
        cabbage.flip(false, true);
        carrot = new TextureRegion(malusesTexture, 226, 0, 75, 75);
        carrot.flip(false, true);
        garlic = new TextureRegion(malusesTexture, 301, 0, 75, 75);
        garlic.flip(false, true);
        onion = new TextureRegion(malusesTexture, 0, 76, 75, 75);
        onion.flip(false, true);
        orange = new TextureRegion(malusesTexture, 76, 76, 75, 75);
        orange.flip(false, true);
        pear = new TextureRegion(malusesTexture, 151, 76, 75, 75);
        pear.flip(false, true);
        raddish = new TextureRegion(malusesTexture, 226, 76, 75, 75);
        raddish.flip(false, true);
        tomato = new TextureRegion(malusesTexture, 301, 76, 75, 75);
        tomato.flip(false, true);
        
        neutralsTexture=new Texture(Gdx.files.internal("data/tex_neutrals.png"));
        almonds = new TextureRegion(neutralsTexture, 0, 0, 75, 75);
        almonds.flip(false, true);
        boletus = new TextureRegion(neutralsTexture, 76, 0, 75, 75);
        boletus.flip(false, true);
        bread = new TextureRegion(neutralsTexture, 151, 0, 75, 75);
        bread.flip(false, true);
        eggs = new TextureRegion(neutralsTexture, 226, 0, 75, 75);
        eggs.flip(false, true);
        cheese = new TextureRegion(neutralsTexture, 301, 0, 75, 75);
        cheese.flip(false, true);
        fish = new TextureRegion(neutralsTexture, 0, 76, 75, 75);
        fish.flip(false, true);
        potato = new TextureRegion(neutralsTexture, 76, 76, 75, 75);
        potato.flip(false, true);
        rice = new TextureRegion(neutralsTexture, 151, 76, 75, 75);
        rice.flip(false, true);
        spaghetti = new TextureRegion(neutralsTexture, 226, 76, 75, 75);
        spaghetti.flip(false, true);
        sushi = new TextureRegion(neutralsTexture, 301, 76, 75, 75);
        sushi.flip(false, true);        
        
        specialsTexture=new Texture(Gdx.files.internal("data/tex_specials.png"));
        amanita = new TextureRegion(specialsTexture, 0, 0, 75, 75);
        amanita.flip(false, true);
        beer = new TextureRegion(specialsTexture, 76, 0, 75, 75);
        beer.flip(false, true);
        
        TextureRegion[] lorenzoRun = {lorenzoRunFront,lorenzo,lorenzoRunBack};
        lorenzoRunAnim = new Animation(0.06f, lorenzoRun);
        lorenzoRunAnim.setPlayMode(PlayMode.LOOP_PINGPONG);

        TextureRegion[] lorenzoFly= {lorenzo,lorenzoFly1,lorenzoFly2};
        lorenzoFlyAnim = new Animation(0.06f, lorenzoFly);
        lorenzoFlyAnim.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        //SOUNDS
        littleFaith = Gdx.audio.newMusic(Gdx.files.internal("data/a_little_faith.mp3"));
        balticLevity = Gdx.audio.newMusic(Gdx.files.internal("data/baltic_levity.mp3"));
        amanitaMusic = Gdx.audio.newMusic(Gdx.files.internal("data/pigfair.mp3"));
        beerMusic = Gdx.audio.newMusic(Gdx.files.internal("data/drunken.mp3"));
             
        death = Gdx.audio.newSound(Gdx.files.internal("data/death.wav"));
        flight = Gdx.audio.newSound(Gdx.files.internal("data/fly.wav"));
        bonus = Gdx.audio.newSound(Gdx.files.internal("data/bonus.wav"));
        malus = Gdx.audio.newSound(Gdx.files.internal("data/malus.wav"));
        

        //FONTS        
        fontUpheaval = new BitmapFont(Gdx.files.internal("data/upheaval.fnt"));
        fontUpheaval.setScale(1f, -1f);
        
        fontUpheavalGray = new BitmapFont(Gdx.files.internal("data/upheaval2.fnt"));
        fontUpheavalGray.setScale(1f, -1f);

        fontUpheavalRed = new BitmapFont(Gdx.files.internal("data/upheavalRed.fnt"));
        fontUpheavalRed.setScale(1f, -1f);
        
        fontUpheavalGreen = new BitmapFont(Gdx.files.internal("data/upheavalGreen.fnt"));
        fontUpheavalGreen.setScale(1f, -1f);
        
        // Get or create file
        prefs = Gdx.app.getPreferences("LorenzoPrefs");

        // Defaultni hiscore
        if (!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
            prefs.flush();
        }
        
        // Defaultni muteStatus
        if (!prefs.contains("sound")) {
            prefs.putBoolean("sound", true);
            prefs.flush();
        }
        
        // Defaultni intro?
        if (!prefs.contains("introPlayed")) {
            prefs.putBoolean("introPlayed", false);
            prefs.flush();
        }
    }
    
    public static void dispose() {

        buttonTexture.dispose();
        buttonSmallTexture.dispose();
        vocomLogoTexture.dispose();
        bazinacLogoTexture.dispose();
        gameLogoTexture.dispose();
        farmTexture.dispose();
        rainbowTexture.dispose();
        japanTexture.dispose();
        bgTexture.dispose();        
        cplatformTexture.dispose();
        coinTexture.dispose();
        bonusesTexture.dispose();
        malusesTexture.dispose();
        neutralsTexture.dispose();
        specialsTexture.dispose();
        
        amanitaMusic.dispose();
        beerMusic.dispose();
        balticLevity.dispose();  
        littleFaith.dispose();
        
        death.dispose(); 
        flight.dispose();
        bonus.dispose(); 
        malus.dispose();
        
        fontUpheaval.dispose();
        fontUpheavalGray.dispose();
        fontUpheavalRed.dispose();
        fontUpheavalGreen.dispose();
    }
    
    
    

    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }
    
    public static void setLastScore(int val) {
        prefs.putInteger("lastScore", val);
        prefs.flush();
    }

    public static int getLastScore() {
        return prefs.getInteger("lastScore");
    }
    
    public static void setMuteStatus(Boolean val) {
        prefs.putBoolean("sound", val);
        prefs.flush();
    }

    public static boolean getMuteStatus() {
    	return prefs.getBoolean("sound");
    }

    public static void setIntroPlayedStatus(Boolean val) {
        prefs.putBoolean("introPlayed", val);
        prefs.flush();
    }

    public static boolean getIntroPlayedStatus() {
        return prefs.getBoolean("introPlayed");
    }
    
    
}