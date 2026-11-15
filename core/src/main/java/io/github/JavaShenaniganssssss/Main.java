package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.Graphics.DisplayMode;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private final int screenWidth;
    private final int screenHeight;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture image;
    private BitmapFont font;

    public Main(){
        this.screenHeight=1;
        this.screenWidth=1;
    }
    public Main(int w, int h){
        this.screenWidth = w;
        this.screenHeight = h;
    }
    public Main(DisplayMode displayMode){
        this.screenWidth = displayMode.width;
        this.screenHeight = displayMode.height;
    }

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //create new world with gravity pointing down
        world = new World(new Vector2(15f, -20f),true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10f, 100f);

        BodyDef groundBodyDef = new BodyDef();
        BodyDef groundBodyDef2 = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef2.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0f, 0f);
        groundBodyDef2.position.set(400f, 0f);

        Body groundBody = world.createBody(groundBodyDef);
        Body groundBody2 = world.createBody(groundBodyDef2);
        Body ballBody = world.createBody(bodyDef);

        //800, 480
        PolygonShape groundBox = new PolygonShape();
        PolygonShape groundBox2 = new PolygonShape();
        groundBox.setAsBox(800f, 0.5f);
        groundBox2.setAsBox(0.5f, 480f);
        groundBody.createFixture(groundBox, 0f);
        groundBody2.createFixture(groundBox2, 0f);


        CircleShape ball = new CircleShape();
        ball.setRadius(10f);
        ballBody.createFixture(ball, 0.5f);

        ball.dispose();
        groundBox.dispose();
        groundBox2.dispose();
        batch = new SpriteBatch();
        image = new Texture("libgdx.png");
        font = new BitmapFont();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0.09375f, 1f);

        camera.update();

        //step the physics simulator every frame
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        //Render the Physics bodies
        debugRenderer.render(world, camera.combined);

        batch.begin();
        batch.draw(image, 140, 210);
        try {
            font.draw(batch, "Display Screen = Width: " + screenWidth/800+ ", Height: " + screenHeight/480, screenWidth/2, screenHeight/2);
        } catch (NullPointerException e) {
            font.draw(batch, e.getMessage(), 400, 240);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
        image.dispose();
        font.dispose();

    }

}
