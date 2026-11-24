package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.HashMap;
import java.util.Map;

public class InputHandler extends InputAdapter {
    private final HashMap<String, BodyHolder> bodies;

    public InputHandler(Map<String, BodyHolder> bodies) {
        HashMap<String, BodyHolder> map = new HashMap<>();
        for(String str : bodies.keySet()){
            map.put(str.toLowerCase().trim(), bodies.get(str));
        }
        this.bodies = map;
    }

    @Override
    public boolean keyDown(int keyCode){
        switch (keyCode){
            case(Input.Keys.A):
                bodies.get("ball").controls |= (1<<4);
                break;
            case(Input.Keys.LEFT):
                bodies.get("box").controls |= 1;
                break;
            case(Input.Keys.D):
                bodies.get("ball").controls |= (1<<5);
                break;
            case(Input.Keys.RIGHT):
                bodies.get("box").controls |= (1<<1);
                break;
            case(Input.Keys.W):
                bodies.get("ball").controls |= (1<<6);
                break;
            case(Input.Keys.UP):
                bodies.get("box").controls |= (1<<2);
                break;
            case(Input.Keys.S):
                bodies.get("ball").controls |= (byte)(1<<7);
                break;
            case(Input.Keys.DOWN):
                bodies.get("box").controls |= (1<<3);
                break;
            default:
                return false;
        }
        speedUpdate();
        return true;
    }
    private void speedUpdate(){
        for(String str : bodies.keySet()){
            speedUpdate(str);
        }
    }
    private void speedUpdate(String str){
        Body body = bodies.get(str).BODY;
        float speed = bodies.get(str).SPEED;
        byte controls = bodies.get(str).controls;
        byte x = 0;
        byte y = 0;
        if((controls&(1<<6))!=0)y++;
        if((controls&(1<<2))!=0)y++;
        if((controls&(1<<4))!=0)x--;
        if((controls&(1))!=0)x--;
        if((controls&(1<<7))!=0)y--;
        if((controls&(1<<3))!=0)y--;
        if((controls&(1<<5))!=0)x++;
        if((controls&(1<<1))!=0)x++;
        body.setLinearVelocity(speed * MathUtils.clamp(x, -1, 1), speed * MathUtils.clamp(y, -1, 1));
    }

    @Override
    public boolean keyUp(int keyCode) {
        switch (keyCode){
            case(Input.Keys.A):
                bodies.get("ball").controls &= ~(1<<4);
                break;
            case(Input.Keys.LEFT):
                bodies.get("box").controls &= ~1;
                break;
            case(Input.Keys.D):
                bodies.get("ball").controls &= ~(1<<5);
                break;
            case(Input.Keys.RIGHT):
                bodies.get("box").controls &= ~(1<<1);
                break;
            case(Input.Keys.W):
                bodies.get("ball").controls &= ~(1<<6);
                break;
            case(Input.Keys.UP):
                bodies.get("box").controls &= ~(1<<2);
                break;
            case(Input.Keys.S):
                bodies.get("ball").controls &= ~(byte)(1<<7);
                break;
            case(Input.Keys.DOWN):
                bodies.get("box").controls &= ~(1<<3);
                break;
            default:
                return false;
        }
        speedUpdate();
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //returns false without giving me a warning ;p
        return screenX<Integer.MIN_VALUE+1;
    }
}
