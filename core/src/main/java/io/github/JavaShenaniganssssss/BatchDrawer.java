package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

public class BatchDrawer extends SpriteBatch {
    //personal drawer
    public void draw(Texture texture, Body body, float width, float height, float scale){
        draw(texture,
             body.getPosition().x - ((width * scale) / 2f),
             body.getPosition().y - ((height * scale) / 2f),
             (width * scale) / 2f,
             (height * scale) / 2f,
             width * scale,
             height * scale,
             1f,1f,
             body.getAngle() * MathUtils.radDeg,
             0, 0,
             texture.getWidth(),
             texture.getHeight(),
             false,
             false
             );
    }
    public void draw(Texture texture, Body body, float radius, float scale){
        draw(texture, body, radius * 2f , radius * 2f, scale);
    }
}
