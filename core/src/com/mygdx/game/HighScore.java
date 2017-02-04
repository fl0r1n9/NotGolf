package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;


/**
 * Created by Florin on 23.01.2017.
 */

public class HighScore {

    public float time;
    public String playerName;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public HighScore(float time, String playerName){
        this.time = time;
        this.playerName = playerName;

    }

}
