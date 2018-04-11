import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

public class Prim {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Graph g = new Graph(in.nextInt());
        for(int m = in.nextInt(); m > 0; m--)
            g.addEdge(in.nextInt(), in.nextInt(), in.nextInt());
        System.out.println(g.MST_Prim());
    }
}

class Graph {
    private static Vertex[] g;

    private class Vertex implements Comparable<Vertex> {
        private int index=-1;
        private int key;
        private Edge next = null;

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(this.key, o.key);
        }
    }

    private class Edge {
        private int value;
        private int len;
        private Edge next=null;

        Edge(int value, int len){
            this.value = value;
            this.len = len;
        }
    }

    Graph(int n) {
        g = new Vertex[n];
        for (int i = 0; i < n; i++) g[i] = new Vertex();
    }

    public void addEdge(int start, int value, int len ) {
        if(g[start].next==null) g[start].next = new Edge(value, len);
        else {
            Edge e = g[start].next;
            while(e.next!=null) e=e.next;
            e.next=new Edge(value, len);
        }
        if(start!=value)
            if(g[value].next==null) g[value].next = new Edge(start, len);
            else {
                Edge e = g[value].next;
                while(e.next!=null) e=e.next;
                e.next=new Edge(start, len);
            }
    }

    public int MST_Prim(){
        int road=0;
        Queue<Vertex> q = new PriorityQueue<>();
        Vertex v = g[ new Random().nextInt(g.length) ];
        for(;;){
            v.index=-2;
            for(Edge e=v.next; e!=null; e=e.next){
                Vertex u = g[e.value];
                if(u.index==-1){
                    u.key=e.len;
                    u.index++;
                    q.add(u);
                }
                else if((u.index!=-2) && (e.len < u.key)) {
                    q.remove(u);
                    u.key = e.len;
                    q.add(u);
                }
            }
            if(q.isEmpty()) break;
            v=q.poll();
            road+=v.key;
        }
        return road;
    }
}