package com.vocom.GameObjects;


import java.util.Random;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.vocom.Helpers.AssetLoader;

public class Food extends Scrollable{
	
    public static final int BODY_WIDTH = 75;
    public static final int BODY_HEIGHT  = 75;	
    public static final int FOOD_MIN_POS = 75;
	
    private Circle body;
    private TextureRegion sprite;

	public enum Kind {
        NOTHING,
        ALMOND, AMANITA, APPLE, BEEF, BEER, BOLETUS, BREAD, BROCCOLI, BURGER, CABBAGE, CAKE,
        CARROT, EGGS, FISH, FRIES, CHEESE, GARLIC, GUMMI, CHICKEN, CHOCO, ONION, ORANGE, PEAR,
        PIZZA, POTATO, RADDISH, RICE, SALAMI, SPAGHETTI, SUSHI, TOMATO, WURST
    }
    
    public enum EffectType {
        BONUS, MALUS, NEUTRAL, SPECIAL, NOTHING
    }
    
    public enum PlaceType {
        ONPLATFORM, AERIAL
    }
    
    private Random r;    
	private int worldHeight;
	
	private int kindCode;
	private Kind kind;
	private EffectType effectType;
	private PlaceType placeType;
	
	private int lardEffect;
	private boolean isInitial;
	private boolean isVisible;
    private boolean isPickedUp;
    
    private boolean specialsAllowed;
   

	public Food(float x, float y, float scrollSpeed, int worldHeight, PlaceType pType, boolean initial)  {
    	super(x, y, BODY_WIDTH, BODY_HEIGHT, scrollSpeed);
        
    	r = new Random();
        body = new Circle();
        this.worldHeight = worldHeight;
        this.placeType = pType;
        this.isInitial = initial;
        this.specialsAllowed = true;
        
        //pozice, override na nulove y
        if (y == 0){
        	position.y = r.nextInt(worldHeight-FOOD_MIN_POS)-BODY_HEIGHT+FOOD_MIN_POS; 
        }      

	    //vlastnosti        
	    // prvni na zacatku hry
	    if (this.isInitial ) {this.forceNothing();}
	    else this.setRandomKind(null, true);
	 }
    
    public Food(Food another) {
    	super(another.position.x, another.position.y, BODY_WIDTH, BODY_HEIGHT, 0);
    	
    	this.kindCode = another.kindCode;
    	this.kind = another.kind;
    	this.effectType = another.effectType;
    	this.placeType = another.placeType;
    	
    	this.lardEffect = another.lardEffect;
    	this.isInitial = another.isInitial;
    	this.isVisible = another.isVisible;
        this.isPickedUp = another.isPickedUp;
    	
        this.position = another.position;
        this.sprite = another.sprite;
    	
      }
    
    @Override
    public void update(float delta) {

    	super.update(delta);
        body.set(position.x+BODY_WIDTH/2, position.y+BODY_WIDTH/2, BODY_WIDTH/2);

    }
      
	@Override
    public void reset(float newX) {		
		super.reset(newX);		
		this.isVisible = true;
		this.isInitial = false;
		
		this.setRandomKind(null, true);
		
		position.y = r.nextInt(worldHeight-FOOD_MIN_POS)-BODY_HEIGHT+FOOD_MIN_POS; 
        
    }
	
	public void resetScrollSpeed(int scrollSpeed) {		
		super.velocity = new Vector2(scrollSpeed, 0);
    }	
	
	
    public void reset(float newX, float forcedY) {		
		super.reset(newX);		
		this.isVisible = true;
		this.isInitial = false;
		
		this.setRandomKind(null, true);
		
		position.y = forcedY; 
        
    }
    
    public Food isCrashedBy(Lorenzo lor) {
     	
    	if (this.kind != Kind.NOTHING){
	    	if (position.x < (lor.getBoundingCircle().x + lor.getBoundingCircle().radius)){
	    			if (Intersector.overlaps(lor.getBoundingCircle(), body)) {   				
	    				return new Food(this);
	    			}						
	    	}		
    	}

    	return null;
    }
    
	
	public void onRestart(float x, float scrollSpeed, boolean initial) {
	    velocity.x = scrollSpeed;
	    reset(x);	    
	    
	    this.isInitial = initial;
	    this.specialsAllowed = true;
	    
	    // prvni na zacatku hry
	    if (this.isInitial ) {this.forceNothing();}
	    else this.setRandomKind(null, true);
	}
	
	public void onRestart(float x, float forcedY, float scrollSpeed, boolean initial) {
	    velocity.x = scrollSpeed;
	    reset(x,forcedY);	    
	    
	    this.isInitial = initial;
	    this.specialsAllowed = true;
	    
	    // prvni na zacatku hry
	    if (this.isInitial ) {this.forceNothing();}
	    else this.setRandomKind(null, true);
	}
	

	public void forceNothing() {
		this.kindCode = 0;
		this.kind = Kind.NOTHING;
		this.effectType = EffectType.NOTHING;
		
		this.isVisible = false;		
		this.sprite = null;	
		this.lardEffect = 0;
	}
	
	private void setRandomKind(EffectType onlyOfType, boolean nothingsAllowed){
		int minInt;
		int maxInt;
		
		// meze
		if (onlyOfType != null){
			switch (onlyOfType) {
	        case BONUS:
			    		minInt = 1;
						maxInt = 10;
	                 break;
	        case NEUTRAL:
			    		minInt = 11;
						maxInt = 20;
	                 break;
	        case MALUS:
			    		minInt = 21;
						maxInt = 30;
	                 break;
	        case SPECIAL:
			    		minInt = 31;
						maxInt = 32;					
	                 break;
	        default:
				minInt = 1;
				maxInt = 32;
			}
		}
		else {
			minInt = 1;
			maxInt = 32;
		}
		
		//nothing (1:1)?
		if (nothingsAllowed) {
			if (r.nextInt(2) == 0) {
				forceNothing();
				return;
			};
		}

		//losovacka
		this.isVisible = true;
		this.kindCode = r.nextInt(maxInt - minInt + 1) + minInt;
		switch (kindCode) {
        case 1:  
        		 this.kind = Kind.BEEF;
        		 this.effectType = EffectType.BONUS;
        		 this.lardEffect = 7;
        		 this.sprite = AssetLoader.beef;        		 
                 break;
        case 2:          		 
        		 this.kind = Kind.BURGER;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 8;
				 this.sprite = AssetLoader.burger;        		 
		         break;
        case 3:          		 
		   		 this.kind = Kind.CAKE;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 8;
				 this.sprite = AssetLoader.cake;        		 
			     break; 
	    case 4:          		 
	  		 	 this.kind = Kind.FRIES;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 9;
				 this.sprite = AssetLoader.fries;        		 
		         break;
	    case 5:          		 
		   		 this.kind = Kind.GUMMI;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 7;
				 this.sprite = AssetLoader.gummi;        		 
			     break; 
	    case 6:          		 
		 	 	 this.kind = Kind.CHICKEN;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 5;
				 this.sprite = AssetLoader.chicken;        		 
				 break;
		case 7:          		 
		 		 this.kind = Kind.CHOCO;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 6;
				 this.sprite = AssetLoader.choco;        		 
			     break; 		
		case 8:          		 
		 		 this.kind = Kind.PIZZA;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 6;
				 this.sprite = AssetLoader.pizza;        		 
			     break;
		case 9:          		 
		 		 this.kind = Kind.SALAMI;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 9;
				 this.sprite = AssetLoader.salami;        		 
			     break; 
		case 10:          		 
		 		 this.kind = Kind.WURST;
				 this.effectType = EffectType.BONUS;
				 this.lardEffect = 10;
				 this.sprite = AssetLoader.wurst;        		 
			     break; 

			     
        case 11:  
		   		 this.kind = Kind.ALMOND;
		   		 this.effectType = EffectType.NEUTRAL;
		   		 this.lardEffect = 1;
		   		 this.sprite = AssetLoader.almonds;        		 
		         break;
	    case 12:          		 
		   		 this.kind = Kind.BOLETUS;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 2;
				 this.sprite = AssetLoader.boletus;        		 
			     break; 
	    case 13:          		 
	 		 	 this.kind = Kind.BREAD;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 4;
				 this.sprite = AssetLoader.bread;        		 
		         break;
	    case 14:          		 
		   		 this.kind = Kind.EGGS;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 3;
				 this.sprite = AssetLoader.eggs;        		 
			     break; 
	    case 15:          		 
		 	 	 this.kind = Kind.FISH;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 1;
				 this.sprite = AssetLoader.fish;        		 
				 break;
		case 16:          		 
		 		 this.kind = Kind.CHEESE;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 3;
				 this.sprite = AssetLoader.cheese;        		 
			     break; 		
		case 17:          		 
		 		 this.kind = Kind.POTATO;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 2;
				 this.sprite = AssetLoader.potato;        		 
			     break;
		case 18:          		 
		 		 this.kind = Kind.RICE;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 2;
				 this.sprite = AssetLoader.rice;        		 
			     break; 
		case 19:          		 
		 		 this.kind = Kind.SPAGHETTI;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 4;
				 this.sprite = AssetLoader.spaghetti;        		 
			     break; 
	    case 20:          		 
	  		 	 this.kind = Kind.SUSHI;
				 this.effectType = EffectType.NEUTRAL;
				 this.lardEffect = 3;
				 this.sprite = AssetLoader.sushi;        		 
		         break;
		      
		         
        case 21:  
		   		 this.kind = Kind.APPLE;
		   		 this.effectType = EffectType.MALUS;
		   		 this.lardEffect = -3;
		   		 this.sprite = AssetLoader.apple;        		 
		         break;
	    case 22:          		 
		   		 this.kind = Kind.BROCCOLI;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -5;
				 this.sprite = AssetLoader.broccoli;        		 
			     break; 
	    case 23:          		 
	 		 	 this.kind = Kind.CABBAGE;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -5;
				 this.sprite = AssetLoader.cabbage;        		 
		         break;
	    case 24:          		 
		   		 this.kind = Kind.CARROT;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -4;
				 this.sprite = AssetLoader.carrot;        		 
			     break; 
	    case 25:          		 
		 	 	 this.kind = Kind.GARLIC;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -2;
				 this.sprite = AssetLoader.garlic;        		 
				 break;
		case 26:          		 
		 		 this.kind = Kind.ONION;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -2;
				 this.sprite = AssetLoader.onion;        		 
			     break; 		
		case 27:          		 
		 		 this.kind = Kind.ORANGE;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -1;
				 this.sprite = AssetLoader.orange;        		 
			     break;
		case 28:          		 
		 		 this.kind = Kind.PEAR;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -1;
				 this.sprite = AssetLoader.pear;        		 
			     break; 
		case 29:          		 
		 		 this.kind = Kind.RADDISH;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -4;
				 this.sprite = AssetLoader.raddish;        		 
			     break; 
	    case 30:          		 
	  		 	 this.kind = Kind.TOMATO;
				 this.effectType = EffectType.MALUS;
				 this.lardEffect = -3;
				 this.sprite = AssetLoader.tomato;        		 
		         break;
		         
		         
		case 31:       
			 	if (specialsAllowed){
			 		 this.kind = Kind.AMANITA;
					 this.effectType = EffectType.SPECIAL;
					 this.lardEffect = 5;
					 this.sprite = AssetLoader.amanita; 
				 }
				 else forceNothing();
				 break;
		case 32: 
				 if (specialsAllowed){
		 		 	 this.kind = Kind.BEER;
					 this.effectType = EffectType.SPECIAL;
					 this.lardEffect = 15;
					 this.sprite = AssetLoader.beer;         
				 }
				 else forceNothing();
				 break;
		}
		

		
		
		
	}
	
    public Circle getBoundingCircle() {
        return body;
    }
    
    public boolean isVisible() {
		return isVisible;
	}
    
    public TextureRegion getSprite() {
		return sprite;
	}


	public int getKindCode() {
		return kindCode;
	}


	public Kind getKind() {
		return kind;
	}


	public EffectType getEffectType() {
		return effectType;
	}


	public PlaceType getPlaceType() {
		return placeType;
	}


	public int getLardEffect() {
		return lardEffect;
	}

	public boolean isPickedUp() {
		return isPickedUp;
	}

	public boolean isSpecialsAllowed() {
		return specialsAllowed;
	}

	public void setSpecialsAllowed(boolean specialsAllowed) {
		this.specialsAllowed = specialsAllowed;
	}



}