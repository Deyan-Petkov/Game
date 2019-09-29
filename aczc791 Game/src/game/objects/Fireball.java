package game.objects;

import city.cs.engine.*;
import game.eventHandlers.Collideable;

public class Fireball extends Walker implements Collideable {

    private Hero hero;


    private static final Shape firaballShape = new PolygonShape(0.264f, 0.43f, 0.3f, -0.226f, 0.142f, -0.458f, -0.162f, -0.462f, -0.322f, -0.218f, -0.272f, 0.408f);
    private static BodyImage image = new BodyImage("data/images/objects/fireball.png", 1f);

    public Fireball(World world, Hero hero) {
        super(world, firaballShape);
        this.hero = hero;
        addImage(image);
    }

    @Override
    public void collisionResponse(Body b) {
        if (b == hero) {
            hero.decrementLifes();
            System.out.println("Ouch,that burns!...-1 life points \nCurrent lives: " + hero.getLifes());
            destroy();
            if (hero.getLifes() <= 0) {
                System.out.println("Game Over");
                System.exit(0);
            }
        } else {
            destroy();
        }
    }
}