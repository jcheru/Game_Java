import com.sun.prism.image.ViewPort;
import processing.core.PApplet;
import processing.core.PImage;

import javax.swing.text.View;

public class WorldView
{
   private PApplet screen;
   private WorldModel world;
   private int tileWidth;
   private int tileHeight;
   private Viewport viewport;

   private PImage img1;
   private PImage img2;

   public WorldView(int numCols, int numRows, PApplet screen, WorldModel world,
      int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);

      // load path images
      this.img1 = screen.loadImage("black.bmp");
      this.img2 = screen.loadImage("red.bmp");
   }

   public void drawViewport()
   {
      drawBackground();
      drawEntities();
      find_mobile();
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

   private void find_mobile()
   {
      for(WorldEntity ent: world.getEntities())
      {
         if (ent instanceof MobileAnimatedActor)
         {
            MobileAnimatedActor mob_ent = (MobileAnimatedActor) ent;
            draw_path(mob_ent);
         }
      }
   }

   private void draw_path(MobileAnimatedActor ent)
   {
      Point pt = ent.getPosition();
      if (viewport.contains(pt))
      {
         Point vpt = worldToViewport(viewport, pt.x, pt.y);
         if (mouse_over_pt(vpt))
         {
            for (int i = 1; i < ent.get_path().size(); i++)
            {
               Node n1 = ent.get_path().get(i);
               if(viewport.contains(new Point(n1.x, n1.y)))
               {
                  Point vn1 = worldToViewport(viewport, n1.x, n1.y);
                  screen.image(img1, vn1.x * 32, vn1.y * 32);
               }
            }
         }
      }
   }

   private boolean mouse_over_pt(Point pt)
   {
      int x_low = pt.x * 32;
      int x_high = x_low + 32;
      int y_low = pt.y * 32;
      int y_high = y_low + 32;

      return (x_low < screen.mouseX && screen.mouseX < x_high
              && y_low < screen.mouseY && screen.mouseY < y_high);
   }

}
