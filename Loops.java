import java.util.*;
import java.util.stream.IntStream;
public class Loops {
    private static int n;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Graph> ar = new ArrayList<>();
        HashMap<Integer, Integer> map = new HashMap<>();
        int j = in.nextInt();
        IntStream.range(0, j).forEach(i -> ar.add(new Graph()));
        IntStream.range(0, j).forEach(i -> {
            map.put(ar.get(i).val=in.nextInt(), i);
            String word = in.next();
            if (i<j-1 && !word.equals("JUMP")) {
                ar.get(i).ls.add(ar.get(i + 1));
                ar.get(i + 1).edge.add(ar.get(i));
            }
            if (word.equals("ACTION")) ar.get(i).oper = -1;
            else ar.get(i).oper = in.nextInt();
        });

        ar.stream().filter(G -> G.oper != -1).forEach(G -> {
            int i = map.get(G.oper);
            Graph v = ar.get(i);
            G.ls.add(v);
            v.edge.add(G);
        });
        VisitGraph(ar.get(0));
        DFS(ar);
        Collections.sort(ar);
        Dominators(ar);
        int res=0;
        for (Graph v : ar)
            for (Graph e : v.edge) {
                while (e != v  && e != null) e = e.dom;
                if (e==v) {
                    ++res;
                    break;
                }
            }
        System.out.println(res);
    }

    static class Graph implements Comparable<Graph> {
        char mark = 'w';
        int count;
        int val = -1;
        int oper = -1;
        Graph ancestor;
        Graph dom;
        Graph sdom;
        Graph label;
        Graph parent;
        ArrayList<Graph> edge = new ArrayList<>();
        ArrayList<Graph> ls = new ArrayList<>();
        ArrayList<Graph> bucket;

        @Override
        public int compareTo(Graph o) {
            return Integer.compare(this.count, o.count);
        }
    }
    private static void VisitGraph(Graph G) {
        G.count = n++;
        G.mark = 'b';
        for (Graph w : G.ls) {
            if (w.mark == 'w') {
                w.parent = G;
                VisitGraph(w);
            }
        }
    }
    private static void DFS(ArrayList<Graph> G) {
        for (int i = 0; i < G.size();)
            if (G.get(i).mark=='b') {
                ArrayList<Graph> e = G.get(i++).edge;
                for (int j = 0; j < e.size();)
                    if (e.get(j).mark=='w') e.remove(j);
                    else j++;
            }
            else G.remove(i);
    }
    private static Graph FindMin(Graph v) {
        if (v.ancestor == null) return v;
        Graph u = v;
        Stack<Graph> s = new Stack<>();
        while (u.ancestor.ancestor != null){
            s.push(u);
            u = u.ancestor;
        }
        while (!s.isEmpty()) {
            v = s.pop();
            if (v.ancestor.label.sdom.count < v.label.sdom.count) v.label = v.ancestor.label;
            v.ancestor = u.ancestor;
        }
        return v.label;
    }
    private static void Dominators(ArrayList<Graph> G) {
        G.forEach(w -> {
            w.sdom = w.label = w;
            w.ancestor = null;
            w.bucket = new ArrayList<>();
        });
        for (int i=G.size()-1; i>0; i--) {
            Graph w = G.get(i);
            w.edge.stream().map(Loops::FindMin).filter(u -> u.sdom.count < w.sdom.count).forEach(u -> w.sdom = u.sdom);
            w.ancestor = w.parent;
            w.sdom.bucket.add(w);
            w.parent.bucket.forEach(v -> {
                Graph u = FindMin(v);
                if (u.sdom == v.sdom) v.dom = w.parent;
                else v.dom = u;
            });
            w.parent.bucket.clear();
        }
        int n = G.size();
        IntStream.range(1, n).mapToObj(G::get).filter(w -> w.dom != w.sdom).forEach(w -> w.dom = w.dom.dom);
        G.get(0).dom = null;
    }
}
