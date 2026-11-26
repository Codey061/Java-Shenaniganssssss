package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;



/** {@link ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private static final short BOX = 0b1;
    private static final short SEN = 0b10;
    private static final short PLAY = 0b100;
    private static final short WALL = 0b1000;
    private final Vector2 TOP_LEFT = new Vector2(0f, 480f);
    private final Vector2 TOP_RIGHT = new Vector2(800f, 480f);
    private final Vector2 BOTTOM_LEFT = new Vector2(0f, 0f);
    private final Vector2 BOTTOM_RIGHT = new Vector2(800f, 0f);
    private final float playerRadius = (float)Math.sqrt(400/Math.PI);
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private BatchDrawer batch;
    private BitmapFont font;
    public Body player;
    public Body boxBody;
    public Texture playerTexture;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //create new world with no gravity
        world = new World(new Vector2(0f, 0f),true);
        debugRenderer = new Box2DDebugRenderer();


        player = bodyMaker(BodyDef.BodyType.DynamicBody, 10f, 100f);
        player.setFixedRotation(true);

        boxBody = bodyMaker(BodyDef.BodyType.DynamicBody, 400f, 240f);
        boxBody.setFixedRotation(true);

        Body goalBody = bodyMaker(BodyDef.BodyType.KinematicBody, 0f, 0f);

        Body groundBody = bodyMaker(BodyDef.BodyType.StaticBody, 0f, 0f);
        goalBody.setFixedRotation(true);

        //800, 480
        CircleShape ball = new CircleShape();
        Filter playerFilter = new Filter();
        playerFilter.categoryBits = PLAY;
        playerFilter.maskBits = BOX | WALL;
        ball.setRadius(playerRadius);
        addToBody(player, ball, 100f, "Player").setFilterData(playerFilter);
        ball.dispose();

        PolygonShape box = new PolygonShape();
        Filter boxFilter = new Filter();
        boxFilter.categoryBits = BOX;
        boxFilter.maskBits = WALL | PLAY | SEN;
        box.setAsBox(10f, 10f);
        addToBody(boxBody, box, 50f, "Box").setFilterData(boxFilter);
        boxBody.setLinearDamping(2f);
        boxBody.getFixtureList().get(0).setRestitution(0f);
        box.dispose();

        PolygonShape win = new PolygonShape();
        win.setAsBox(10f, 10f, new Vector2(790f, 230), 0f);
        Filter winFilter = new Filter();
        winFilter.categoryBits = SEN;
        winFilter.maskBits = BOX;
        addToBody(goalBody, win, 0f, true, "Win").setFilterData(winFilter);
        win.dispose();

        newEdgeShape(groundBody, BOTTOM_LEFT, BOTTOM_RIGHT);
        newEdgeShape(groundBody, BOTTOM_RIGHT, TOP_RIGHT);
        newEdgeShape(groundBody, TOP_RIGHT, TOP_LEFT);
        newEdgeShape(groundBody, TOP_LEFT, BOTTOM_LEFT);

        Filter wallFilter = new Filter();
        wallFilter.categoryBits = WALL;
        wallFilter.maskBits = BOX | PLAY;
        for(Fixture fixture:
            groundBody.getFixtureList()) {
            fixture.setFilterData(wallFilter);
            fixture.setUserData("Wall");
        }

        batch = new BatchDrawer();
        font = new BitmapFont();
        font.getData().setScale(1f);
        font.setColor(Color.WHITE);

        InputHandler inputHandler = new InputHandler(this);
        ContactHandler contactHandler = new ContactHandler(this, inputHandler);
        Gdx.input.setInputProcessor(inputHandler);
        world.setContactListener(contactHandler);

        playerTexture = new Texture("R-Head.png");
    }
    @Override
    public void render() {
        ScreenUtils.clear(0.0625f, 0.0625f, 0.0625f, 1f);

        camera.update();

        //step the physics simulator every frame
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        //Render the Physics bodies
        //REMOVE WHEN DONE TESTING
        debugRenderer.render(world, camera.combined);

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(playerTexture, player, playerRadius, 1f);
        //Adding font.getCapHeight() to y makes the pixel your moving now the bottom left one
        font.draw(batch, "R - Reset\nEsc - Leave\nWASD/Arow Keys - Move", 0f, font.getCapHeight()*6);
        if(!ContactHandler.hasWon)font.draw(batch, "Move The Box To The Blue Square To Win!", 0f, font.getCapHeight());
        else font.draw(batch, "You Win!", 0f, font.getCapHeight());
        batch.end();
    }

    @Override
    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
        font.dispose();
        playerTexture.dispose();
    }

    private void newEdgeShape(Body body, Vector2 start, Vector2 end){
        EdgeShape edge = new EdgeShape();
        edge.set(start, end);
        body.createFixture(edge, 0f);
        edge.dispose();
    }

    //private void newEdgeShape(Body body, float startX, float startY, float endX, float endY){newEdgeShape(body, startX, startY, endX, endY, 0f);}

    //private void newEdgeShape(Body body, float startX, float startY, float endX, float endY, float density){newEdgeShape(body, new Vector2(startX, startY), new Vector2(endX, endY), density);}

    private Body bodyMaker(BodyDef.BodyType bodyType, Vector2 position){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(position);
        return world.createBody(bodyDef);
    }

    private Body bodyMaker(BodyDef.BodyType bodyType, float x, float y){return bodyMaker(bodyType, new Vector2(x, y));}

    private Fixture addToBody(Body body, Shape shape, float density, boolean sensor, @Null Object userData){
        Fixture fixture = body.createFixture(shape, density);
        fixture.setSensor(sensor);
        if(userData!=null)fixture.setUserData(userData);
        return fixture;
    }

    //private Fixture addToBody(Body body, Shape shape, float density, boolean sensor){
        //return addToBody(body, shape, density, sensor, null);
    //}

    private Fixture addToBody(Body body, Shape shape, float density, Object userData){
        return addToBody(body,shape, density, false, userData);
    }

    //private Fixture addToBody(Body body, Shape shape, float density){
        //return addToBody(body, shape, density, false, null);
    //}
}
