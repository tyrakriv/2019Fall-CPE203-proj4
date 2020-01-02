import processing.core.PImage;

import java.util.List;

public class Quake extends AnimationEntity{

    private static final String QUAKE_ID = "quake";
    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(Point position, List<PImage> images)
    {
        super(QUAKE_ID, position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);

    }

    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        this.removeEntity(world, this);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                QUAKE_ACTION_PERIOD);
        scheduler.scheduleEvent(this,
                new AnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT), getAnimationPeriod());
    }
}
