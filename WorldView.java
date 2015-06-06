import com.sun.prism.image.ViewPort;
import processing.core.PApplet;
import processing.core.PImage;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   public WorldView(int numCols, int numRows, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
   }



   private void drawBackground()
   {
      for (int row = 0; row < viewport.getNumRows(); row++)
      {
         for (int col = 0; col < viewport.getNumCols(); col++)
         {
            Point wPt = viewportToWorld(viewport, col, row);
            PImage img = world.getBackground(wPt).getImage();
            screen.image(img, col * tileWidth, row * tileHeight);
         }
      }
   }

   private void drawEntities()
   {
      for (WorldEntity entity : world.getEntities())
      {
         Point pt = entity.getPosition();
         if (viewport.contains(pt))
         {
            Point vPt = worldToViewport(viewport, pt.x, pt.y);
            screen.image(entity.getImage(), vPt.x * tileWidth,
               vPt.y * tileHeight);
         }
      }
   }

   public void updateView(int dx, int dy)
   {
      int new_x = clamp(viewport.getCol() + dx, 0,
         world.getNumCols() - viewport.getNumCols());
      int new_y = clamp(viewport.getRow() + dy, 0,
         world.getNumRows() - viewport.getNumRows());
      viewport.shift(new_y, new_x);
   }

   private static int clamp(int v, int min, int max)
   {
      return Math.min(max, Math.max(v, min));
   }

   private static Point viewportToWorld(Viewport viewport, int col, int row)
   {
      return new Point(col + viewport.getCol(), row + viewport.getRow());
   }

   private static Point worldToViewport(Viewport viewport, int col, int row)
   {
      return new Point(col - viewport.getCol(), row - viewport.getRow());
   }

   public Point create_ship(int x, int y, ImageStore imageStore, long ticks)
   {
      int x_start = x / 32;
      int y_start = y / 32;

      int k = 1;

      if (x_start + 24 < 40 && y_start + 19 < 30)
      {
         for(int i = 0; i < 4; i++)
         {
            for(int j = 0; j < 4; j++)
            {
               Point pt2 = viewportToWorld(viewport, x_start + i, y_start + j);
               if(world.isOccupied(pt2))
               {
                  world.removeEntityAt(pt2);
               }

               Ship ship1 = new Ship("ship", pt2,
                       imageStore.get(String.format("ship%d", k)));
               world.addEntity(ship1);
               k++;
            }
         }
         Point pt = viewportToWorld(viewport, x_start + 4, y_start + 4);
         Alien alien = new Alien("alien", pt, 300, 100, imageStore.get("alien"));
         alien.schedule(world, ticks + alien.getRate(), imageStore);
         world.addEntity(alien);

         return viewportToWorld(viewport, x_start, y_start);
      }
      return null;
   }

}
