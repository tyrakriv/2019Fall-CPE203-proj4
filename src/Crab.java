import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class Crab extends Moveable{

    private static final String QUAKE_KEY = "quake";

    public Crab(String id, Point position,
                  int actionPeriod, int animationPeriod, List<PImage> images, PathingStrategy p)
    {
        super(id, position, images, actionPeriod, animationPeriod, p);
    }

    public Point nextPosition(WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//
//        Optional<Entity> occupant = world.getOccupant(newPos);
//
//        if (horiz == 0 ||
//                (occupant.isPresent() && !(occupant.get() instanceof Fish)))
//        {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//            occupant = world.getOccupant(newPos);
//
//            if (vert == 0 ||
//                    (occupant.isPresent() && !(occupant.get() instanceof Fish)))
//                {
//                newPos = this.getPosition();
//            }
//        }
//        return newPos;
//    }

//        PathingStrategy strategy = new SingleStepPathingStrategy();
        PathingStrategy strategy = new AStarPathingStrategy();

        Predicate<Point> pred = p -> (world.withinBounds(p)) && !(world.isOccupied(p) &&!(world.getOccupancyCell(p) instanceof Fish));
        BiPredicate<Point, Point> bipred = (p1, p2) -> neighbors(p1, p2);
        List<Point> list = strategy.computePath(getPosition(), destPos, pred, bipred, PathingStrategy.CARDINAL_NEIGHBORS);

        if (list.size() == 0) {
            return this.getPosition();
        }
        return list.get(0);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(this.getPosition(), Sgrass.class);
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveTo(world, crabTarget.get(), scheduler))
            {
                Entity quake = new Quake(tgtPos, imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                ((ActivityEntity)quake).scheduleActions(scheduler, world, imageStore);
            }
        }
        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore),
                nextPeriod);
    }
}
