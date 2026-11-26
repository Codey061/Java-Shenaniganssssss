package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;


public class InputHandler extends InputAdapter {
    private final Main main;
    private byte controls = 0;

    public InputHandler(Main main) {
        this.main = main;
    }

    @Override
    public boolean keyDown(int keyCode){
        switch (keyCode){
            case(Input.Keys.A):
                controls |= 0b10000;
                break;
            case(Input.Keys.LEFT):
                controls |= 1;
                break;
            case(Input.Keys.D):
                controls |= 0b100000;
                break;
            case(Input.Keys.RIGHT):
                controls |= 0b10;
                break;
            case(Input.Keys.W):
                controls |= 0b1000000;
                break;
            case(Input.Keys.UP):
                controls |= 0b100;
                break;
            case(Input.Keys.S):
                controls |= (byte)0b10000000;
                break;
            case(Input.Keys.DOWN):
                controls |= 0b1000;
                break;
            case(Input.Keys.R):
                ContactHandler.hasWon = false;
                main.boxBody.setTransform(400f, 240f, main.boxBody.getAngle());
                main.boxBody.setLinearVelocity(0f, 0f);
                return true;
            default:
                return false;
        }
        speedUpdate();
        return true;
    }
    void speedUpdate(){
        Body body = main.player;
        float speed = 100f;
        byte x = 0;
        byte y = 0;
        if((controls & 0b1000000) != 0) y++;
        if((controls & 0b100) != 0) y++;
        if((controls & 0b10000) != 0) x--;
        if((controls & 1) != 0) x--;
        if((controls & 0b10000000) != 0) y--;
        if((controls & 0b1000) != 0) y--;
        if((controls & 0b100000) != 0) x++;
        if((controls & 0b10) != 0) x++;
        if(x >= 1) main.playerTexture = TextureHolder.RIGHT.texture;
        else if(x <= -1) main.playerTexture = TextureHolder.LEFT.texture;
        else if(y >= 1) main.playerTexture = TextureHolder.UP.texture;
        else if(y <= -1) main.playerTexture = TextureHolder.DOWN.texture;
        body.setLinearVelocity(speed * MathUtils.clamp(x, -1, 1), speed * MathUtils.clamp(y, -1, 1));
    }

    @Override
    public boolean keyUp(int keyCode) {
        switch (keyCode){
            case(Input.Keys.A):
                controls &= ~0b10000;
                break;
            case(Input.Keys.LEFT):
                controls &= ~1;
                break;
            case(Input.Keys.D):
                controls &= ~0b100000;
                break;
            case(Input.Keys.RIGHT):
                controls &= ~0b10;
                break;
            case(Input.Keys.W):
                controls &= ~0b1000000;
                break;
            case(Input.Keys.UP):
                controls &= ~0b100;
                break;
            case(Input.Keys.S):
                controls &= ~(byte)0b10000000;
                break;
            case(Input.Keys.DOWN):
                controls &= ~0b1000;
                break;
            case(Input.Keys.ESCAPE):
                Gdx.app.exit();
                return true;
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
