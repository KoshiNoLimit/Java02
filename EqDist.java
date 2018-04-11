import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class EqDist {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        Graph g = new Graph(read.nextInt());
        for (int i = read.nextInt(); i > 0; i--) g.addEdge(read.nextInt(), read.nextInt());
        int k = read.nextInt();
        int [][] ar = new int[k][g.n];
        for(int i = 0; i < k; i++) g.BFS(ar[i], read.nextInt());
        Queue<Integer> write = new LinkedList<>();
        for(int i = 0, j; i < g.n; i++){
            for(j = 1; j < k; j++) if((ar[j][i]!=ar[j-1][i])|ar[j][i]==0) break;
            if(j==k)write.offer(i);
        }
        if(write.isEmpty()) System.out.println("-");
        else while(!write.isEmpty()) System.out.print(write.remove() + " ");
    }
}

class Graph {
    private static Vertex[] g;
    public int n;

    private class Vertex {
        private int num;
        private Vertex next = null;

        Vertex(int num) { this.num = num; }
    }

    Graph(int n) {
        this.n = n;
        g = new Vertex[n];
        for (int i = 0; i < n; i++) g[i] = new Vertex(i);
    }

    public void addEdge(int first, int second) {
        Vertex v = g[first];
        while (v.next != null) v = v.next;
        v.next = new Vertex(second);
        v = g[second];
        while (v.next != null) v = v.next;
        v.next = new Vertex(first);
    }

    public void BFS(int[] ar, int v){
        boolean[] mark = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        q.offer(v);
        mark[v]=true;
        while(!q.isEmpty()){
            int j = q.remove();
            for(Vertex e=g[j].next; e!=null; e=e.next){
                if(!mark[e.num]){
                    q.offer(e.num);
                    mark[e.num]=true;
                    ar[e.num] = ar[j] + 1;
                }
            }
        }
    }
}
