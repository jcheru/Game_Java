import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;

public class Worker
   extends MobileAnimatedActor
{
   private static final int QUAKE_ANIMATION_RATE = 100;

   public Worker(String name, Point position, int rate,
                     int animation_rate, List<PImage> imgs)
   {
      super(name, position, rate, animation_rate, imgs);
   }

   public String toString()
   {
      return String.format("moving_blacksmith %s %d %d", this.getName(),
         this.getPosition().x, this.getPosition().y);
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

         WorldEntity target = world.findNearest(getPosition(), Ship.class);
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
         else
         {
            List<Alien> aliens = new ArrayList<>();
            for (WorldEntity ent : world.getEntities())
            {
               if (ent instanceof Alien)
               {
                  Alien alien = (Alien) ent;
                  aliens.add(alien);
               }
            }

            for(Alien alien : aliens)
            {
               alien.gonna_die(world, ticks, imageStore);
               nextTime = nextTime + getRate();
            }
         }

         scheduleAction(world, this, createAction(world, imageStore),
                 nextTime);
      };
      return action[0];
   }

   protected boolean move(WorldModel world, WorldEntity ship)
   {
      if (ship == null)
      {
         return false;
      }

      if (adjacent(getPosition(), ship.getPosition()))
      {
         ship.remove(world);
         return true;
      }
      else
      {
         world.moveEntity(this, nextPosition(world, ship.getPosition()));
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
