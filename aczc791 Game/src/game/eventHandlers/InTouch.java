
package game.eventHandlers;

import city.cs.engine.*;
import game.levels.GameLevel;

/**
 * Checks if two bodies collide
 */
public class InTouch implements CollisionListener {

    private GameLevel gameLevel;

    public InTouch(GameLevel gameLevel) {
        this.gameLevel = gameLevel;

    }

    /**
     *Handles collision events via Collideable  interface implemented by all bodies having CollisionListener attached.
     */
    @Override
    public void collide(CollisionEvent e) {

        if (e.getReportingBody() instanceof Collideable) {
            Collideable c = (Collideable)e.getReportingBody();
            c.collisionResponse(e.getOtherBody());
        }
    }
}


