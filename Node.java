public class Node
{
    public final int x;
    public final int y;

    private int gvalue;
    private int hvalue;
    private int fvalue;
    private Node came_from;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;

        this.came_from = null;
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

    public Node get_came_from()
    {
        return this.came_from;
    }

    public void set_came_from(Node node)
    {
        this.came_from = node;
    }

}
