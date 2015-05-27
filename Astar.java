import java.util.LinkedList;

public class Astar
{

    public static LinkedList A_star(Point start, Point goal, WorldModel world)
    {
        LinkedList<Point> closedset = new LinkedList<>();
        OrderedList<Point> openset = new OrderedList<>();
        LinkedList<Point> came_from = new LinkedList<>();

        openset.insert(start, start.get_f());

        start.set_g(0);
        start.set_f(start.get_g() + manhattan(start, goal));


        while (openset != null)
        {
            System.out.println(closedset.size());
            Point current = openset.head().item;
            if(current.equals(goal))
            {
                return reconstruct_path(came_from, goal);
            }

            openset.remove(current);
            closedset.add(current);

            for(Point neighbor : neighbor_nodes(current, world, goal))
            {
                if(closedset.contains(neighbor))
                {
                    continue;
                }
                int tentative_gscore = current.get_g() + 1;

                if (!(openset.contains(neighbor)) || tentative_gscore < neighbor.get_g())
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

    public static LinkedList reconstruct_path(LinkedList came_from, Point current)
    {
        if (current.get_came_from() != null)
        {
            came_from.add(current);
        }
        return came_from;
    }


    public static OrderedList<Point> get_nodes1(WorldModel world)
    {
        OrderedList<Point> returnlist = new OrderedList<>();
        for(int i = 0; i < world.getNumRows(); i++)
        {
            for(int j = 0; j < world.getNumCols(); j++)
            {
                Point node = new Point(j, i);
                if (!(world.isOccupied(node)))
                {
                    returnlist.insert(node, node.get_f());
                }
            }
        }
        return returnlist;
    }

    public static LinkedList<Point> get_nodes2(WorldModel world)
    {
        LinkedList<Point> returnlist = new LinkedList<>();
        for(int i = 0; i < world.getNumRows(); i++)
        {
            for(int j = 0; j < world.getNumCols(); j++)
            {
                Point node = new Point(j, i);
                if (!(world.isOccupied(node)))
                {
                    returnlist.add(node);
                }

            }
        }
        return returnlist;
    }

    public static int manhattan(Point start, Point goal)
    {
        return ((goal.y - start.y) + (goal.x - start.x));
    }

    public static LinkedList<Point> neighbor_nodes(Point current, WorldModel world, Point goal)
    {
        LinkedList<Point> returnlist= new LinkedList<>();
        for (Point node : get_nodes2(world))
        {
            if(MobileAnimatedActor.adjacent(node, current))
            {
                returnlist.add(node);
            }
        }
        if(MobileAnimatedActor.adjacent(goal, current))
        {
            returnlist.add(goal);
        }
        return returnlist;
    }
}
