import processing.core.PImage;

import java.util.List;

public class Alien
    extends MobileAnimatedActor
{

    private static final int QUAKE_ANIMATION_RATE = 100;

    public Alien(String name, Point position, int rate, int animation_rate,
                 List<PImage> imgs)
    {
        super(name, position, rate, animation_rate, imgs);
    }

    protected boolean canPassThrough(WorldModel world, Point pt)
    {
        return !world.isOccupied(pt);
    }

    public Action createAction(WorldModel world, ImageStore imageStore)
    {
        Action[] action = { null };
        action[0] = ticks -> {
            removePendingAction(action[0]);

            WorldEntity target = world.findNearest(getPosition(), Miner.class);
            long nextTime = ticks + getRate();

            if (target != null)
            {
                Point tpt = target.getPosition();
                if(move(world,target))
                {
                    Quake quake = createQuake(world, tpt, ticks, imageStore);
                    world.addEntity(quake);
                    nextTime = nextTime + getRate();
                }
            }

            scheduleAction(world, this, createAction(world, imageStore),
                    nextTime);
        };
        return action[0];
    }

    public void gonna_die(WorldModel world, long ticks, ImageStore imageStore)
    {
        Quake quake = createQuake(world, this.getPosition(), ticks, imageStore);
        world.addEntity(quake);
    }

    protected boolean move(WorldModel world, WorldEntity miner)
    {
        if (miner == null)
        {
            return false;
        }

        if (adjacent(getPosition(), miner.getPosition()))
        {
            miner.remove(world);
            return true;
        }
        else
        {
            world.moveEntity(this, nextPosition(world, miner.getPosition()));
            return false;
        }
    }

    private Quake createQuake(WorldModel world, Point pt, long ticks,
                              ImageStore imageStore)
    {
        Quake quake = new Quake("quake", pt, QUAKE_ANIMATION_RATE,
                imageStore.get("quake"));
        quake.schedule(world, ticks, imageStore);
        return quake;
    }
}
