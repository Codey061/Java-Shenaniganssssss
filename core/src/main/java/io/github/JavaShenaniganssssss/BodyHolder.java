package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.physics.box2d.Body;

public class BodyHolder {
    public final Body BODY;
    public final float SPEED;
    public byte controls = 0x0;
    public BodyHolder(Body body, float speed){
        this.BODY = body;
        this.SPEED = speed;
    }
}
