import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class Moveable extends AnimationEntity{

    public PathingStrategy p;

    public Moveable(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy p) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.p = p;
    }

    public boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent(target.getPosition())) {
            if (this instanceof OctoNotFull) {
                ((OctoNotFull) this).setResourceCount(((OctoNotFull) this).getResourceCount() + 1);
                target.removeEntity(world, target);
                scheduler.unscheduleAllEvents(target);
            }
            if (this instanceof Crab) {
                target.removeEntity(world, target);
                scheduler.unscheduleAllEvents(target);
            }
            return true;
        } else {
            Point nextPos = this.nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    protected abstract Point nextPosition(WorldModel world, Point destPos);

    boolean neighbors(Point p1, Point p2)
    {
        return p1.x+1 == p2.x && p1.y == p2.y ||
                p1.x-1 == p2.x && p1.y == p2.y ||
                p1.x == p2.x && p1.y+1 == p2.y ||
                p1.x == p2.x && p1.y-1 == p2.y;
    }
}
