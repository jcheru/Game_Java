import processing.core.PImage;

import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.abs;

public abstract class MobileAnimatedActor
   extends AnimatedActor
{
   private List<Node> path = new LinkedList<>();

   public MobileAnimatedActor(String name, Point position, int rate,
      int animation_rate, List<PImage> imgs)
   {
      super(name, position, rate, animation_rate, imgs);
   }

   public List<Node> get_path()
   {
      return this.path;
   }

   protected Point nextPosition(WorldModel world, Point dest_pt)
   {
      LinkedList<Node> list = A_star(this.getPosition(), dest_pt, world);
      Point next = new Point(list.get(1).x, list.get(1).y);
      return next;
   }

   protected static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && abs(p1.x - p2.x) == 1);
   }

   protected abstract boolean canPassThrough(WorldModel world, Point new_pt);

   public LinkedList A_star(Point start1, Point goal1, WorldModel world)
   {
      Node[][] nodeWorld = nodes_init(world);

      Node start = nodeWorld[start1.y][start1.x];
      Node goal = nodeWorld[goal1.y][goal1.x];

      LinkedList<Node> closedset = new LinkedList<>();
      OrderedList<Node> openset = new OrderedList<>();

      openset.insert(start, start.get_f());

      start.set_g(0);
      start.set_f(start.get_g() + manhattan(start, goal));

      while (openset.size() > 0)
      {
         Node current = openset.head().item;
         if(current.x == goal.x && current.y == goal.y)
         {
            this.path = reconstruct_path(current);
            return reconstruct_path(current);
         }

         openset.remove(current);
         closedset.add(current);

         for(Node neighbor : neighbor_nodes(current, world, goal, nodeWorld))
         {
            if(closedset.contains(neighbor))
            {
               continue;
            }
            int tentative_gscore = current.get_g() + 1;

            if ( (!openset.contains(neighbor)) || tentative_gscore < neighbor.get_g())
            {
               neighbor.set_came_from(current);
               neighbor.set_g(tentative_gscore);
               neighbor.set_f(neighbor.get_g() + manhattan(neighbor, goal));

               if (!openset.contains(neighbor))
               {
                  openset.insert(neighbor, neighbor.get_f());
               }
            }
         }
      }
      return null;
   }

   public static LinkedList reconstruct_path(Node current)
   {
      LinkedList<Node> path = new LinkedList<>();
      while(current.get_came_from()!= null)
      {
         current = current.get_came_from();
         path.add(0, current);
      }
      return path;
   }

   public static Node[][] nodes_init(WorldModel world)
   {
      Node[][] nodeWorld = new Node[world.getNumRows()][world.getNumCols()];

      for(int i = 0; i < world.getNumCols(); i++)
      {
         for(int j = 0; j < world.getNumRows(); j++)
         {
            Node node = new Node(i, j);
            nodeWorld[j][i] = node;
         }
      }
      return nodeWorld;
   }

   public static LinkedList<Node> get_nodes(WorldModel world, Node[][] nodeWorld)
   {
      LinkedList<Node> returnlist = new LinkedList<>();
      for(int i = 0; i < world.getNumRows(); i++)
      {
         for(int j = 0; j < world.getNumCols(); j++)
         {
            Node node = nodeWorld[i][j];
            if (!(world.isOccupied(new Point(j, i))))
            {
               returnlist.add(node);
            }
         }
      }
      return returnlist;
   }

   public static int manhattan(Node start, Node goal)
   {
      return ((goal.y - start.y) + (goal.x - start.x));
   }

   public static LinkedList<Node> neighbor_nodes(Node current, WorldModel world, Node goal, Node[][] nodeWorld)
   {
      LinkedList<Node> returnlist= new LinkedList<>();
      for (Node node : get_nodes(world, nodeWorld))
      {
         if(adjacent(new Point(node.x, node.y), new Point(current.x, current.y)))
         {
            returnlist.add(node);
         }
      }
      if(adjacent(new Point(goal.x, goal.y), new Point(current.x, current.y)))
      {
         returnlist.add(goal);
      }
      return returnlist;
   }


}
