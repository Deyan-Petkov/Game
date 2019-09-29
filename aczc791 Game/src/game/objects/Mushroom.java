package game.objects;

import city.cs.engine.*;
import game.eventHandlers.Collideable;
import game.levels.GameLevel;

public class Mushroom extends Walker implements Collideable {

    private Hero hero;
    private GameLevel gameLevel;

    private static final Shape mushroomShape = new PolygonShape(0.246f, 0.444f, 0.476f, 0.14f, 0.476f, -0.18f, 0.194f, -0.476f, -0.218f, -0.472f, -0.486f, -0.156f, -0.484f, 0.172f, -0.226f, 0.458f);

    private static BodyImage image = new BodyImage("data/images/objects/mushroom.png", 1);

    public Mushroom(World world, Hero hero,GameLevel gameLevel) {
        super(world, mushroomShape);
        addImage(image);
        this.hero = hero;
        this.gameLevel = gameLevel;
    }

    @Override
    public void collisionResponse(Body b) {
        if (b == hero) {
            hero.decrementLifes();
            System.out.println("I feel Ill!  -1 Life points" + hero.getLifes());
            if (hero.getLifes() <= 0) {
                System.out.println("0 lives ...Game Over");
                //Close current game window
                System.exit(0);
            }
            destroy();
        }
        // makes mushrooms to jump over the nettle
        if (b instanceof Nettle) {
            this.jump(2);
        }

        if(b == gameLevel.getlWall()){
            this.startWalking(3);
        }else if(b == gameLevel.getrWall()){
            this.startWalking(-3);
        }

        //if mushroom collide with another one exchange positions and keep same direction
        else if (b instanceof Mushroom) {
            this.setPosition(b.getPosition());
        }
        //if mushrooms collide with the gong - try to jump over it
        else if (b instanceof Door) {
            this.stopWalking();
            this.jump(14);
            this.startWalking(-3);
        }
    }
}
