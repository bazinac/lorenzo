package com.vocom.Lorenzo;

import com.badlogic.gdx.Game;
import com.vocom.Helpers.AssetLoader;
import com.vocom.Screens.SplashScreen;

public class LorenzoGame extends Game {

    @Override
    public void create() {
        AssetLoader.load();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        AssetLoader.dispose();
    }

}