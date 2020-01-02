import processing.core.PImage;

import java.util.List;

public abstract class ActivityEntity extends Entity {

    private final int actionPeriod;

    public ActivityEntity(String id, Point position, List<PImage> images, final int actionPeriod) {
        super(id, position, images);
        this.actionPeriod = actionPeriod;
    }

    public int getActionPeriod() {
        return actionPeriod;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                getActionPeriod());
    }
}
