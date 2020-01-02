import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{

    //check if point is in open
    //if g value is less than point thats already in open list, than replace


    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {

        HashMap<Point, Node> open = new HashMap<>();
        HashMap<Point, Node> close = new HashMap<>();
        List<Node> fvals = new ArrayList<>();

        List<Point> points = new ArrayList<>();

//        Node e = new Node(end, null, fval(end, start), gval(start, end));
        Node cur = new Node(start, null, 0, 0 );

//        System.out.println(end);

        fvals.add(cur);
        open.put(start, cur);

        // check if current node is the end node
        while (!(withinReach.test(cur.p, end))) {
//            fvals.remove(0);
//            open.remove(cur.p);

            List<Point> neighbors = potentialNeighbors.apply(cur.p)
                    .filter(canPassThrough)
                    .filter(pt -> !close.containsKey(pt))
                    .collect(Collectors.toList());

//            System.out.println("neighbors" + neighbors);

            // go through every point in neighbors and determine smallest f value
            for (Point p : neighbors) {

                if (!(close.containsKey(p))) {
                    // create new neighbor node

                    Node neighbor = new Node(p, cur, fval(cur, p, end), gval(p, cur));

                    double gval = 0;

                    if (!(close.get(p) == null)) {
                        gval = gval(p, close.get(p));
                    }

                    if (open.containsKey(p) && gval < cur.g) {
                        neighbor.g = gval;
                        neighbor.f = hval(end, p) + gval;
                        close.replace(p, neighbor);
                    }

                    if (!(open.containsKey(p))) {
                        open.put(p, neighbor);
                        fvals.add(neighbor);
                    }
                }
            }
            close.put(cur.p, cur);
            fComparator comp = new fComparator();
            Collections.sort(fvals, comp);

            if (fvals.size() == 0){
                return points;
            }
            cur = fvals.get(0);
            fvals.remove(0);
//                System.out.println(cur.p);


            open.remove(cur.p);
//            fvals.remove(0);
//
//            System.out.println(fvals.get(0).p);
//            cur = fvals.get(0);
//            System.out.println(cur.p);
        }




        while (!(cur.p == start)){

//            System.out.println("points" + cur.p);
            points.add((cur).p);
            close.remove(cur.p);
            cur = cur.parent;

        }
//        System.out.println(points);
        Collections.reverse(points);
//        System.out.println(points);
        return points;
    }


    public double hval(Point end, Point p){
        return Math.abs(end.x - p.x) + Math.abs(end.y - p.y);
    }

    public double gval(Point p, Node cur){
        //check if current point cur.p is adjacent to previous point p , if so add one to currents gval
        if (    cur.p.x == p.x && cur.p.y -1 == p.y ||
                cur.p.x == p.x && cur.p.y +1 == p.y ||
                cur.p.x +1 == p.x && cur.p.y == p.y  ||
                cur.p.x -1 == p.x && cur.p.y == p.y)
            return cur.g + 1.0;
        else{
            return cur.g + 1.4;
        }
    }
    public double fval(Node cur, Point p, Point e){
        return gval(p, cur) + hval(e, p);
    }
}

