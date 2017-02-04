package com.mygdx.game.objects;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Florin on 26.01.2017.
 */

public class Obstacle{

    private BodyDef bodyDef;
    private FixtureDef fixDef;


    public Body body;
    private CircleShape shape;
    private Circle bounding;
    float xVal;
    float yVal;
    float radiusVal;


    public Obstacle(float x, float y, float radius, World world) {

        xVal = x;
        yVal = y;
        radiusVal = radius;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        shape = new CircleShape();
        shape.setRadius(radius);

        bounding = new Circle();
        bounding.set(x,y,radius);

        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.friction = 0.0f;
        fixDef.restitution = 0.0f;
        fixDef.filter.groupIndex = -1;
        body.createFixture(fixDef);

        shape.dispose();

    }


    public Circle getBounding() {
        return bounding;
    }

    public boolean collides(Circle boundingBall){
        return(boundingBall.overlaps(bounding));
    }
    public float getxVal() {
        return xVal;
    }

    public float getyVal() {
        return yVal;
    }

    public float getRadiusVal() {
        return radiusVal;
    }

    public Body getBody() {
        return body;
    }

}
