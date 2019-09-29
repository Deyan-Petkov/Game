//Create Nettle body
package game.objects;

import city.cs.engine.*;
import game.eventHandlers.Collideable;

public class Nettle extends StaticBody implements Collideable {
    private Hero hero;

    //Constructor
    public Nettle(World world, Hero hero) {
        super(world);
        this.hero = hero;
        //borders for the base of the plant
        Shape grassRootShape = new PolygonShape(-0.724f, -0.388f, 0.865f,
                -0.325f, 0.851f, -0.585f, -0.71f, -0.571f);
        //Creates "heavy" fixture simulating roots
        SolidFixture grassRootFixture = new SolidFixture(this, grassRootShape, 25.0f);
        //Borders for the leafs
        Shape grassLeafShape = new PolygonShape(-0.749f, -0.353f, 0.816f, -0.339f, 0.865f, 0.596f, -0.823f, 0.276f);
        //kind of simulate the soft leafs of the plant
        Fixture grassLeafFixture = new GhostlyFixture(this, grassLeafShape);
        //attach the image to the shape
        addImage(new BodyImage("data/images/objects/grass2.png", 1.2f));
    }

    @Override
    public void collisionResponse(Body b) {
        if (b == hero) {
            hero.decrementLifes();
            System.out.println("Ouch,that stings!...-1 life points \nCurrent lives: " + hero.getLifes());

            if (hero.getLifes() <= 0) {
                System.out.println("0 lives ...Game Over");
                //Close current game window
                System.exit(0);
            }
        }
    }
}
