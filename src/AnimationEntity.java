import processing.core.PImage;

import java.util.List;

public abstract class AnimationEntity extends ActivityEntity{

    private final int animationPeriod;
    private int imageIndex;

    public AnimationEntity(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod) {
        super(id, position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
        this.imageIndex = 0;
    }

    public int getAnimationPeriod(){
        return animationPeriod;
    }
    public int getImageIndex() { return imageIndex; }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore){
        scheduler.scheduleEvent(this, new ActivityAction(this, world, imageStore), getActionPeriod());
        scheduler.scheduleEvent(this, new AnimationAction(this, 0), getAnimationPeriod());
    }

    public void nextImage()
    {
        this.imageIndex = (this.imageIndex + 1) % this.getImages().size();
    }

}
