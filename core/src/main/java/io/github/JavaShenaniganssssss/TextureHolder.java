package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.graphics.Texture;

public enum TextureHolder {
    RIGHT(new Texture("R-Head.png")),
    LEFT(new Texture("L-Head.png")),
    DOWN(new Texture("D-Head.png")),
    UP(new Texture("U-Head.png"));
    public final Texture texture;
    TextureHolder(Texture texture) {this.texture=texture;}
}
