import processing.core.PImage;

import java.util.List;

public abstract class Entity {

    private final String id;
    private Point position;
    private final List<PImage> images;
    private int imageIndex;


    public Entity(String id, Point position,
                    List<PImage> images)
    {
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
    }

    public Point getPosition() {
        return position;

    }
    public void setPosition(Point position) {
        this.position = position;
    }

    public List<PImage> getImages() { return images; }
    public int getImageIndex() { return imageIndex; }

    public String getId() { return id; }

    public PImage getCurrentImage()
    {
        return getImages().get(getImageIndex());
    }

    public void removeEntity(WorldModel world, Entity entity)
    {
        removeEntityAt(world, entity.getPosition());
    }

    public void removeEntityAt(WorldModel world, Point pos)
    {
        if (world.withinBounds(pos)
                && world.getOccupancyCell(pos) != null)
        {
            Entity entity = world.getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
            this.position = new Point(-1, -1);
            world.getEntities().remove(this);
            world.setOccupancyCell( pos, null);
        }
    }
}
