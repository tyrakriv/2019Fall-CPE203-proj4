import java.util.Comparator;

public class fComparator implements Comparator<Node> {

    @Override
    public int compare(Node a, Node b) {
        return (int)(a.f - b.f);
    }
}
