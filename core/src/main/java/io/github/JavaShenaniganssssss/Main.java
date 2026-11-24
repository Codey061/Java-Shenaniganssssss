package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.Graphics.DisplayMode;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import java.util.Map;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private final Vector2 TOP_LEFT = new Vector2(0f, 480f);
    private final Vector2 TOP_RIGHT = new Vector2(800f, 480f);
    private final Vector2 BOTTOM_LEFT = new Vector2(0f, 0f);
    private final Vector2 BOTTOM_RIGHT = new Vector2(800f, 0f);
    private final int screenWidth;
    private final int screenHeight;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private boolean stop = false;

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

        //create new world with no gravity
        world = new World(new Vector2(0f, 0f),true);
        debugRenderer = new Box2DDebugRenderer();


        Body ballBody = bodyMaker(BodyDef.BodyType.DynamicBody, 10f, 100f);
        ballBody.setFixedRotation(true);

        Body boxBody = bodyMaker(BodyDef.BodyType.DynamicBody, 400f, 240f);
        boxBody.setFixedRotation(true);

        Body goalBody = bodyMaker(BodyDef.BodyType.KinematicBody, 0f, 0f);

        Body groundBody = bodyMaker(BodyDef.BodyType.StaticBody, 0f, 0f);
        goalBody.setFixedRotation(true);

        //800, 480
        CircleShape ball = new CircleShape();
        ball.setRadius((float)Math.sqrt(400/Math.PI));
        ballBody.createFixture(ball, 100f).setUserData("Player");
        ball.dispose();

        PolygonShape box = new PolygonShape();
        box.setAsBox(10f, 10f);
        boxBody.createFixture(box, 100f);
        box.dispose();

        PolygonShape win = new PolygonShape();
        win.setAsBox(10f, 10f, new Vector2(790f, 230), 0f);
        Fixture goal = goalBody.createFixture(win, 0f);
        goal.setSensor(true);
        goal.setUserData("Win");
        win.dispose();

        newEdgeShape(groundBody, BOTTOM_LEFT, BOTTOM_RIGHT);
        newEdgeShape(groundBody, BOTTOM_RIGHT, TOP_RIGHT);
        newEdgeShape(groundBody, TOP_RIGHT, TOP_LEFT);
        newEdgeShape(groundBody, TOP_LEFT, BOTTOM_LEFT);

        batch = new SpriteBatch();
        font = new BitmapFont();
        Gdx.input.setInputProcessor(new InputHandler(Map.of(
            "Ball", new BodyHolder(ballBody, 100f),
            "Box", new BodyHolder(boxBody,100f)
        )));
        world.setContactListener(new ContactHandler());
    }
    @Override
    public void render() {
        ScreenUtils.clear(0.0625f, 0.0625f, 0.0625f, 1f);

        camera.update();

        //step the physics simulator every frame
        try{
            world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        } catch (Exception e) {
            if(e.getMessage().equals("You Win!"))stop = true;
            else throw e;
        }

        //Render the Physics bodies
        debugRenderer.render(world, camera.combined);

        batch.begin();
        font.getData().setScale(10f);
        font.setColor(Color.WHITE);
        //Adding font.getCapHeight() to y makes the pixel your moving now the bottom left one
        if(!stop)font.draw(batch, "Touch The Blue Square To Win!", 0f, font.getCapHeight());
        else font.draw(batch, "You Win!", 0f, font.getCapHeight());
        batch.end();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
        font.dispose();

    }
    private void newEdgeShape(Body body, Vector2 start, Vector2 end){newEdgeShape(body, start, end, 0f);}
    private void newEdgeShape(Body body, Vector2 start, Vector2 end, float density){
        EdgeShape edge = new EdgeShape();
        edge.set(start, end);
        body.createFixture(edge, density);
        edge.dispose();
    }
    private void newEdgeShape(Body body, float startX, float startY, float endX, float endY){newEdgeShape(body, startX, startY, endX, endY, 0f);}
    private void newEdgeShape(Body body, float startX, float startY, float endX, float endY, float density){newEdgeShape(body, new Vector2(startX, startY), new Vector2(endX, endY), density);}
    private Body bodyMaker(BodyDef.BodyType bodyType, Vector2 position){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        return world.createBody(bodyDef);
    }
    private Body bodyMaker(BodyDef.BodyType bodyType, float x, float y){
        return bodyMaker(bodyType, new Vector2(x, y));}

}
