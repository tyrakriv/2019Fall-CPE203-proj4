import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Fish extends ActivityEntity{


    private static final String CRAB_KEY = "crab";
    private static final String CRAB_ID_SUFFIX = " -- crab";
    private static final int CRAB_PERIOD_SCALE = 4;
    private static final int CRAB_ANIMATION_MIN = 50;
    private static final int CRAB_ANIMATION_MAX = 150;
    public static final Random rand = new Random();

    public Fish(String id, Point position, int actionPeriod, List<PImage> images) {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        this.removeEntity(world, this);
        scheduler.unscheduleAllEvents(this);

        Entity crab = new Crab(this.getId() + CRAB_ID_SUFFIX,
                pos, this.getActionPeriod() / CRAB_PERIOD_SCALE,
                CRAB_ANIMATION_MIN +
                        rand.nextInt(CRAB_ANIMATION_MAX - CRAB_ANIMATION_MIN),
                imageStore.getImageList(CRAB_KEY), new SingleStepPathingStrategy());

        world.addEntity(crab);
        ((ActivityEntity)crab).scheduleActions(scheduler, world, imageStore);
    }
}
