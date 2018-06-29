import java.util.Scanner;
import java.util.Stack;

public class GraphBase {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        //find components
        Graph g = new Graph(in.nextInt());
        for(int i = in.nextInt(); i > 0; i--) g.addEdge(in.nextInt(), in.nextInt());
        g.Tarjan();
        //condensation
        Graph cond = new Graph(g.count);
        for(Graph.Vertex v : g.ar)
            for(Graph.Vertex e = v.next; e != null; e = e.next )
                if (g.ar[e.num].comp != v.comp) cond.addEdge(v.comp, g.ar[e.num].comp);
        //base of condensation
        for(Graph.Vertex v : cond.ar)
            for( Graph.Vertex e = v.next; e != null; e = e.next) cond.chekFriends(cond.ar[e.num]);
        //find min
        for(Graph.Vertex v : cond.ar)
            if(v.color == 0) {
                int min = g.n;
                for (Graph.Vertex u : g.ar)
                    if(u.comp == v.num && u.num < min) min = u.num;
                if(min<g.n) System.out.println(min);
            }
    }

    static class Graph{
        private Vertex[] ar;
        private int n;
        private int time = 1;
        private int count = 1;

        class Vertex{
            private int t1;
            private int low;
            private int num;
            private int comp;
            private int color;
            private Vertex next = null;
            Vertex (int num) { this.num = num; }
        }

        Graph(int n){
            this.n = n;
            ar = new Vertex[n];
            for (int i = 0; i < n; i++ ) ar[i] = new Vertex(i);
        }

        void addEdge(int o1, int o2){
            Vertex v = ar[o1];
            for(; v.next != null; v=v.next)
                if(v.num == o2) break;
            if(v.next==null) v.next = new Vertex(o2);
        }

        void Tarjan() {
            Stack<Integer> s = new Stack<>();
            for (Vertex v : ar)
                if (v.t1 == 0) VisitVertex_Tarjan(v.num, s);
        }

        private void VisitVertex_Tarjan(Integer i, Stack<Integer> s){
            ar[i].t1 = ar[i].low = time++;
            s.push(i);
            for(Vertex e=ar[i].next; e!=null; e=e.next){
                if(ar[e.num].t1 == 0) VisitVertex_Tarjan(e.num, s);
                if(ar[e.num].comp == 0 && ar[i].low > ar[e.num].low) ar[i].low = ar[e.num].low;
            }
            if(ar[i].t1 == ar[i].low) {
                for (; ;) {
                    Integer e = s.pop();
                    ar[e].comp = count;
                    if (e.equals(i)) break;
                }
                ++count;
            }
        }

        private void chekFriends(Vertex v){
            if(v.color == 0){
                ar[v.num].color = 1;
                for(Vertex e = ar[v.num].next; e != null;e.color=1, e=e.next) {
                    chekFriends(ar[e.num]);
                }
            }
        }
    }
}
