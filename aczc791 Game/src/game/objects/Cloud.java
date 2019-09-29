package game.objects;

import city.cs.engine.*;

public class Cloud extends Walker {

    public Cloud(World world) {
        super(world);


        Shape cloudShape = new PolygonShape(-0.56f, 1.36f, -1.19f, 0.76f, -1.18f, -1.44f, 1.22f, -1.41f, 1.14f, 0.71f, 0.74f, 1.33f);
        Fixture cloudFixture = new GhostlyFixture(this, cloudShape);
        addImage(new BodyImage("data/images/objects/cloud.png", 3f));

    }
}
