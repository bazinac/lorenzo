package com.vocom.TweenAccessors;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class BitmapFontAccessor implements TweenAccessor<BitmapFont> {

    public static final int ALPHA = 1;

    @Override
    public int getValues(BitmapFont target, int tweenType, float[] returnValues) {
        switch (tweenType) {
        case ALPHA:
            returnValues[0] = target.getColor().a;
            return 1;
      
        default:
            return 0;
        }
    }

    @Override
    public void setValues(BitmapFont target, int tweenType, float[] newValues) {
        switch (tweenType) {
        case ALPHA:
            target.setColor(1, 1, 1, newValues[0]);
            break;       
    	
        }
    }

	

}
