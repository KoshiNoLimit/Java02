import java.util.LinkedList;
import java.util.Scanner;
import java.util.Queue;

public class BridgeNum {

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        Graph g = new Graph(read.nextInt());
        int m = read.nextInt();
        for (int i = 0; i < m; i++) g.addEdge(read.nextInt(), read.nextInt());
        g.DFS1();
        g.DFS2();
        System.out.println(g.findBridges());
    }
}

class Graph{
    private  Vertex[] g;
    private  Queue <Integer> q;
    private int n;
    private int component;
    private int concat;

    private class Vertex{
        private char mark='w';
        private int num;
        private int numc=-1;
        private Vertex next=null;
        private int parent=-1;

        Vertex(int num){ this.num=num; }
    }

    Graph(int n){
        this.n=n;
        g = new Vertex[n];
        q = new LinkedList<>();
        for(int i=0;i<n;i++) g[i] = new Vertex(i);
    }

    public void addEdge(int first, int second){
        Vertex v = g[first];
        while(v.next!=null) v=v.next;
        v.next=new Vertex(second);
        v = g[second];
        while(v.next!=null) v=v.next;
        v.next=new Vertex(first);
    }

    public void DFS1(){
        for(int i=0; i<n; i++)
            if(g[i].mark=='w'){
                concat++;
                VisitVertex1(i);
            }
    }

    private void VisitVertex1(int v){
        g[v].mark='g';
        q.offer(g[v].num);
        for(Vertex e=g[v].next; e!=null; e=e.next)
            if(g[e.num].mark=='w'){
                g[e.num].parent=v;
                VisitVertex1(e.num);
            }
    }

    public void DFS2(){
        int j;
        while(!q.isEmpty()){
            j=q.remove();
            if (g[j].numc==-1) {
                VisitVertex2(j);
                component++;
            }
        }
    }

    private void VisitVertex2(int v){
        g[v].numc=component;
        for(Vertex e=g[v].next; e!=null; e=e.next)
            if((g[e.num].numc==-1) && (g[e.num].parent!=v))
                VisitVertex2(e.num);
    }

    public int findBridges(){ return component - concat;}
}


