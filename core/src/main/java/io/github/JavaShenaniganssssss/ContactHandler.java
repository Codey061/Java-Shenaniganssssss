package io.github.JavaShenaniganssssss;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.HashMap;

public class ContactHandler implements ContactListener {
    private final Main main;
    private final InputHandler inputHandler;
    public static boolean hasWon = false;
    public final HashMap<Object, Float> linearDampening = new HashMap<>();
    public final HashMap<Object, Integer> contacts = new HashMap<>();
    ContactHandler(Main main, InputHandler inputHandler){
        this.main = main;
        this.inputHandler = inputHandler;
    }

    @Override
    public void beginContact(Contact contact){
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData().equals("Wall")||fixtureB.getUserData().equals("Wall"))return;
        if(fixtureA.getUserData()==null)fixtureA.setUserData(fixtureA.getBody());
        if(fixtureB.getUserData()==null)fixtureB.setUserData(fixtureB.getBody());
        if((fixtureA.isSensor()||fixtureB.isSensor())){
            //linearDampening.putIfAbsent(fixtureA.getUserData(), fixtureA.getBody().getLinearDamping());
            //linearDampening.putIfAbsent(fixtureB.getUserData(), fixtureB.getBody().getLinearDamping());
            hasWon = true;
        }else{
            add(fixtureA.getUserData(), fixtureB.getUserData());
            linearDampening.putIfAbsent(fixtureA.getUserData(), fixtureA.getBody().getLinearDamping());
            linearDampening.putIfAbsent(fixtureB.getUserData(), fixtureB.getBody().getLinearDamping());
            fixtureA.getBody().setLinearDamping(0f);
            fixtureB.getBody().setLinearDamping(0f);
        }
        if(fixtureA.getBody().equals(main.player)||fixtureB.getBody().equals(main.player))inputHandler.speedUpdate();
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData().equals("Wall")||fixtureB.getUserData().equals("Wall"))return;
        if(fixtureA.isSensor()||fixtureB.isSensor())return;
        remove(fixtureA.getUserData(), fixtureB.getUserData());
        if(contacts.get(fixtureA.getUserData())==0){
            fixtureA.getBody().setLinearDamping(linearDampening.remove(fixtureA.getUserData()));
            if(fixtureA.getUserData().equals(fixtureA.getBody()))fixtureA.setUserData(null);
        }
        if(contacts.get(fixtureB.getUserData())==0){
            fixtureB.getBody().setLinearDamping(linearDampening.remove(fixtureB.getUserData()));
            if(fixtureB.getUserData().equals(fixtureB.getBody()))fixtureB.setUserData(null);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
    private void add(Object a, Object b){
        if(contacts.containsKey(a))contacts.put(a, contacts.get(a)+1);
        else contacts.put(a, 1);
        if(contacts.containsKey(b))contacts.put(b, contacts.get(b)+1);
        else contacts.put(b, 1);
    }
    private void remove(Object a, Object b){
        contacts.put(a, contacts.get(a)-1);
        contacts.put(b, contacts.get(b)-1);
        if(contacts.get(a)<0||contacts.get(b)<0)throw new IllegalStateException("Negative Value Found In Contacts Map");
    }
}
