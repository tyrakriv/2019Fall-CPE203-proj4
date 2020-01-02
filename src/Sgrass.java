import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Sgrass extends ActivityEntity{

    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;
    private static final String FISH_KEY = "fish";
    public static final Random rand = new Random();

    public Sgrass(String id, Point position,
                    List<PImage> images, int actionPeriod)
    {
        super(id, position, images, actionPeriod);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Fish fish = new Fish(FISH_ID_PREFIX + this.getId(),
                    openPt.get(), FISH_CORRUPT_MIN + rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                    imageStore.getImageList(FISH_KEY));
            world.addEntity(fish);
            (fish).scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                new ActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }
}
