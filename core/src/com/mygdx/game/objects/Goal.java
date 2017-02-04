package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Florin on 27.01.2017.
 */


public class Goal {

    private BodyDef bodyDef;

    public Body body;
    private PolygonShape box;
    private FixtureDef fixtureDef;
    public Rectangle bounding;
    public Sprite sprite;

    public Goal(float x, float y, float sizeX, float sizeY, World world){

        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(x ,y));

        body = world.createBody(bodyDef);

        sprite = new Sprite(new Texture("img/goal.png"),0,0,200,107);
        sprite.setSize(sizeX * 2, sizeY * 2);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        body.setUserData(sprite);

        box = new PolygonShape();
        box.setAsBox(sizeX, sizeY);

        fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.filter.groupIndex = -1;

        body.createFixture(fixtureDef);

        box.dispose();

        bounding = new Rectangle();
        bounding.set(21.0f,0.0f,4.0f,3.0f);

    }

    public void dispose(){
        box.dispose();
    }

    public boolean collides(Ball ball){
        return(Intersector.overlaps(ball.getBounding(),bounding));
    }

    public Body getBody() {
        return body;
    }


}
