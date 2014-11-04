package com.vocom.TweenAccessors;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteAccessor implements TweenAccessor<Sprite> {

    public static final int ALPHA = 1;
    public static final int XPOS = 2;
    public static final int YPOS = 3;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
        case ALPHA:
            returnValues[0] = target.getColor().a;
            return 1;
        case XPOS:
        	returnValues[0] = target.getX();
            return 1;
        case YPOS:
        	returnValues[0] = target.getY();
            return 1;    
        default:
            return 0;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
        case ALPHA:
            target.setColor(1, 1, 1, newValues[0]);
            break;       
    	case XPOS:
    		target.setX(newValues[0]);
    		break;
    	case YPOS:
    		target.setY(newValues[0]);
    		break;
        }
    }		

}
