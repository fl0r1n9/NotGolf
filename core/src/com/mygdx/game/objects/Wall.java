package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Florin on 25.01.2017.
 */

public class Wall {


    private BodyDef bodyDef;
    public Body body;
    private PolygonShape box;
    float xVal,yVal, sizeXVal, sizeYVal;


    public Wall(float x, float y, float sizeX, float sizeY, World world){

        xVal = x;
        yVal = y;
        sizeXVal = sizeX;
        sizeYVal = sizeY;


        bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x ,y));

        body = world.createBody(bodyDef);

        box = new PolygonShape();
        box.setAsBox(sizeX, sizeY);
        body.createFixture(box, 0.0f);

        box.dispose();


    }

    public void dispose(){
        box.dispose();
    }



    public float getxVal() {
        return xVal;
    }

    public float getyVal() {
        return yVal;
    }

    public float getSizeXVal() {
        return sizeXVal;
    }

    public float getSizeYVal() {
        return sizeYVal;
    }
}
