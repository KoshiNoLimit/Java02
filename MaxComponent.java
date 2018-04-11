import java.util.Scanner;
import java.util.Stack;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.LinkedList;

public class MaxComponent {

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        Graph g = new Graph(read.nextInt());
        int m = read.nextInt();
        for (int i = 0; i < m; i++) g.addEdge(read.nextInt(), read.nextInt());
        g.DFS();
        g.watch();

    }
}

class Graph {
    private static Vertex[] g;
    private Component mc;
    private int n;
    private Queue <Vertex> q;

    private class Vertex {
        private char mark = 'w';
        private int num;
        private Vertex next = null;
        private int numc;

        Vertex(int num) { this.num = num; }
    }

    private class Component {
        private int vertexes;
        private int edges;
        private int numc;
    }

    Graph(int n) {
        this.n = n;
        g = new Vertex[n];
        mc = new Component();
        q = new LinkedList<>();
        for (int i = 0; i < n; i++) g[i] = new Vertex(i);
    }

    public void addEdge(int first, int second) {
        Vertex v = g[first];
        q.offer(v);
        while (v.next != null) v = v.next;
        v.next = new Vertex(second);
        v = g[second];
        q.offer(v);
        if(first!=second) {
            while (v.next != null) v = v.next;
            v.next = new Vertex(first);
        }
    }

    public void DFS() {
        Stack<Vertex> s = new Stack<>();
        for (int i = 0, num=1; i < n; i++) {
            if (g[i].numc==0) {
                Component c = new Component();
                c.numc = num;
                s.push(g[i]);
                while (!s.empty()) {
                    Vertex v = s.pop();
                    if (v.mark == 'w') {
                        v.mark = 'g';
                        v.numc = num;
                        c.vertexes++;
                        for (Vertex e = v.next; e != null; e = e.next, c.edges++)
                            if (g[e.num].mark == 'w') s.push(g[e.num]);

                    }
                }
                if ((c.vertexes > mc.vertexes) | ((c.vertexes == mc.vertexes) && (c.edges > mc.edges))) mc = c;
                num++;
            }
        }
    }

    public void watch() {
        PrintWriter w = new PrintWriter(System.out, false);
        w.println("graph{");
        for (Vertex v : g)
            if (v.numc == mc.numc) w.println("   " + v.num + " [color = red]");
            else w.println("   " + v.num);
        while (!q.isEmpty()) {
            Vertex v = q.remove();
            if (v.numc == mc.numc) w.println("   " + v.num + " -- " + q.remove().num + " [color = red]");
            else w.println("   " + v.num + " -- " + q.remove().num);
        }
        w.println("}");
        w.close();
    }
}