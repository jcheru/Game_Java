public class Point
{
   public final int x;
   public final int y;

   private int gvalue;
   private int hvalue;
   private int fvalue;
   private Point came_from;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;

      this.gvalue = 0;
      this.hvalue = 0;
      this.fvalue = 0;
      this.came_from = null;
   }

   public boolean equals(Point other)
   {
      if (this.x == other.x && this.y == other.y)
      {
         return true;
      }
      return false;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }


   public int get_g()
   {
      return this.gvalue;
   }

   public void set_g(int i)
   {
      this.gvalue = i;
   }

   public int get_h(int i)
   {
      return this.hvalue;
   }

   public void set_h(int i)
   {
      this.hvalue = i;
   }

   public int get_f()
   {
      return this.fvalue;
   }

   public void set_f(int i)
   {
      this.fvalue = i;
   }

   public Point get_came_from()
   {
      return this.came_from;
   }

   public void set_came_from(Point node)
   {
      this.came_from = node;
   }


}
