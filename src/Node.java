public class Node {

    public Point p;
    public Node parent;
    public double f;
    public double g;

    Node(Point p, Node parent, double f, double g){
        this.p = p;
        this.parent = parent;
        this.f = f;
        this.g = g;
    }

//    public int getF() {
//        return f;
//    }

}
