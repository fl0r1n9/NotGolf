package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Florin on 26.01.2017.
 */

public class Ball {

    private BodyDef bodyDef;
    private FixtureDef fixDef;
    public Body body;
    private CircleShape shape;
    public Vector2 position;
    public Circle bounding;
    public Sprite sprite;

    public float radius;

    public Ball(float x, float y, float radius, World world){

        this.radius = radius;

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x,y);

        body = world.createBody(bodyDef);

        sprite = new Sprite(new Texture("img/ball.png"));
        sprite.setSize(radius * 4.5f,radius * 4.5f);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        body.setUserData(sprite);

        shape = new CircleShape();
        shape.setRadius(radius);

        position = new Vector2(x,y);

        bounding = new Circle();
        bounding.set(body.getPosition().x,body.getPosition().y,shape.getRadius());

        fixDef = new FixtureDef();
        fixDef.shape = shape;
        fixDef.density = 2.0f;
        fixDef.friction = 0.25f;
        fixDef.restitution = 0.5f;
        fixDef.filter.groupIndex = -1;

        body.createFixture(fixDef);

        shape.dispose();

    }

    public void update(float delta) {
        position = body.getPosition();
        bounding.set(body.getPosition().x,body.getPosition().y,radius);
    }


    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Circle getBounding() {
        return bounding;
    }

    public Body getBody() {
        return body;
    }

}
