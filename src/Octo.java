import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Octo extends Moveable {
    private static int resourceLimit;
    private static int resourceCount;

    public Octo(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, PathingStrategy strategy) {
        super(id, position, images, actionPeriod, animationPeriod, strategy);
    }

    public Point nextPosition(WorldModel world, Point destPos) {

//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz,
//                this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x,
//                    this.getPosition().y + vert);
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = this.getPosition();
//            }
//        }
//        return newPos;

//        PathingStrategy strategy = new SingleStepPathingStrategy();
        PathingStrategy strategy = new AStarPathingStrategy();

            Predicate<Point> pred = p -> (world.withinBounds(p)) && !(world.isOccupied(p));
            BiPredicate<Point, Point> bipred = (p1, p2) -> neighbors(p1, p2);
            List<Point> list = strategy.computePath(getPosition(), destPos, pred, bipred, PathingStrategy.CARDINAL_NEIGHBORS);

            if (list.size() == 0){
                return this.getPosition();
            }
//        System.out.println(list.get(list.size() - 1));
        return list.get(0);
    }


    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Octo octo = transformHelper();

        if (octo != null){
            this.removeEntity(world, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(octo);
            octo.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }

    protected abstract Octo transformHelper();

    public int getResourceLimit() { return resourceLimit; }
    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }
    public int getResourceCount() {
        return resourceCount;
    }
}
