import java.util.*;
import java.util.stream.IntStream;
import static java.lang.Integer.MAX_VALUE;

public class MapRoute {
    static Scanner in = new Scanner(System.in);

    public static void main (String[] args){
        Graph G = new Graph(in.nextInt());
        G.Dijkstra(G.g[0][0]);
    }

    static class Graph {
        Vertex[][] g;
        int n;
        PriorityQueue<Vertex> q;

        Graph(int n){
            this.n = n;
            g = new Vertex[n][n];
            q = new PriorityQueue<>();
            for(int i=0; i<n; i++)
                for(int j=0; j<n; j++) g[i][j] = new Vertex(in.nextInt(), i, j);
            for(int i=0; i<n; i++)
                for(Vertex v : g[i]) addEdge(v.oy, v.ox);
        }

        private class Vertex implements Comparable<Vertex> {
            private int ox;
            private int oy;
            private int index;
            private int dist;
            private int w;
            private Vertex next;

            Vertex(int w, int oy, int ox) {
                this.w = w;
                this.oy = oy;
                this.ox = ox;
                dist = MAX_VALUE-10;
                next = null;
            }
            @Override
            public int compareTo(Vertex o) {
                return Integer.compare(this.dist, o.dist);
            }
        }

        void addEdge(int y, int x){
            Vertex v = g[y][x];
            if(x>0) {
                v.next = new Vertex (g[y][x-1].w, y, x-1);
                v = v.next;
            }
            if(x<n-1){
                v.next = new Vertex (g[y][x+1].w, y, x+1);
                v = v.next;
            }
            if(y>0) {
                v.next = new Vertex (g[y-1][x].w, y-1, x);
                v = v.next;
            }
            if(y<n-1){
                v.next = new Vertex (g[y+1][x].w, y+1, x);
            }
        }

        void Dijkstra(Vertex s){
            s.dist = s.w;
            //IntStream.range(0, n).mapToObj(i -> Arrays.asList(g[i]).subList(0, n)).forEach(q::addAll);
            q.add(s);
            while (!q.isEmpty()) {
                Vertex v = q.peek();
                q.remove(q.peek());
                if(v.dist>g[v.oy][v.ox].dist) continue;
                g[v.oy][v.ox].index = -1;
                for (Vertex e = g[v.oy][v.ox].next; e != null; e = e.next)
                    if (g[e.oy][e.ox].index != -1 && g[v.oy][v.ox].dist+g[e.oy][e.ox].w<g[e.oy][e.ox].dist) {
                        g[e.oy][e.ox].dist = g[v.oy][v.ox].dist + g[e.oy][e.ox].w;
                        q.add(g[e.oy][e.ox]);
                    }
            }
            System.out.println(g[n-1][n-1].dist);
        }
    }
}


