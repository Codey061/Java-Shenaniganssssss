package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.physics.box2d.Body;

public class InputHandler extends InputAdapter {
    private final Body body;
    InputHandler(Body body){
        this.body = body;
    }
    @Override
    public boolean keyDown(int keyCode){
        float x = 0f;
        float y = 0f;
        if(keyCode== Input.Keys.LEFT||keyCode==Input.Keys.A)x-=100;
        if(keyCode == Input.Keys.RIGHT||keyCode == Input.Keys.D)x+=100;
        if (keyCode == Input.Keys.UP|| keyCode == Input.Keys.W)y+=100;
        body.setLinearVelocity(body.getLinearVelocity().x*0.5f+x*0.5f, y);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        return true;
    }
}
