package com.vocom.GameObjects;

import com.badlogic.gdx.math.Vector2;

public class Scrollable {
    
    protected Vector2 position;
    protected Vector2 velocity;
    protected int width;
    protected int height;
    protected boolean isScrolledLeft;
    
    private float last_velX;
   

    public Scrollable(float x, float y, int width, int height, float scrollSpeed) {
        position = new Vector2(x, y);
        velocity = new Vector2(scrollSpeed, 0);
        this.width = width;
        this.height = height;
        isScrolledLeft = false;
    }

    public void update(float delta) {
        position.add(velocity.cpy().scl(delta));

        // If the Scrollable object is no longer visible:
        if (position.x + width < 0) {
            isScrolledLeft = true;
        }
    }
    
    public void stop() {
    	last_velX = velocity.x;
    	velocity.x = 0;
        
    }
    
    public void go() {
        velocity.x = last_velX; 
    }

    // Reset: Should Override in subclass for more specific behavior.
    public void reset(float newX) {
    	position.x = newX;    	
        isScrolledLeft = false;
        last_velX = 0;
    }

    // Getters for instance variables
    public boolean isScrolledLeft() {
        return isScrolledLeft;
    }

    public float getTailX() {
        return position.x + width;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}