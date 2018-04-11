import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Mars {
    public static Scanner in = new Scanner(System.in);

    public static void main(String[] args){
        int n = in.nextInt();
        new Graph(n);
        ArrayList<DM> mass = new ArrayList<>();
        ArrayList<Integer> base = new ArrayList<>();
        for(int c=1; c<=Graph.component; c+=2){
            ArrayList<Integer> m1 = new ArrayList<>();
            ArrayList<Integer> m2 = new ArrayList<>();
            for(Graph.Vertex v : Graph.g)
                if(v.mark==c) m1.add(v.num+1);
                else if(v.mark==c-1) m2.add(v.num+1);
            if(m1.size()==m2.size()){
                Collections.sort(m1);
                Collections.sort(m2);
                base.addAll(comp(m1, m2));
            }
            else mass.add(new DM(m1,m2));
        }
        ArrayList<Integer> friendly = new ArrayList<>();
        for(Graph.Vertex v : Graph.g)
            if(v.mark==-1) friendly.add(v.num+1);
        ArrayList<Integer> first = new ArrayList<>(base);
        for(int var = (int) Math.pow(2, mass.size())-1; var>=0; var--){
            int  ch=var;
            ArrayList<Integer> test = new ArrayList<>(base);
            for(DM d : mass){
                test.addAll(d.array.get(ch%2));
                ch>>=1;
            }
            for(int i : friendly) if(test.size()>=n/2) break; else  test.add(i);
            Collections.sort(test);
            if(test.size()<=n/2 && test.size()>=first.size())
                if(test.size()>first.size()) first=test;
                else first=comp(first, test);
        }
        for(int i : first) System.out.print(i+ " ");
    }

    private static ArrayList<Integer> comp(ArrayList<Integer> a, ArrayList<Integer> b){
        int len=a.size();
        for(int i=0; i<len; i++) {
            int ai=a.get(i), bi=b.get(i);
            if (ai==bi) continue;
            if(ai<bi) return a;
            return b;
        }
        return a;
    }
}

class DM {
    public ArrayList<ArrayList<Integer>> array;

    DM(ArrayList<Integer> array1, ArrayList<Integer> array2){
        array = new ArrayList<>();
        array.add(array1);
        array.add(array2);
    }
}

class Graph {
    public static Vertex[] g;
    private int n;
    public static int component;

    public class Vertex {
        public int mark=-1;
        public int num;
        private Vertex next = null;
        Vertex(int num) { this.num = num; }
    }

    Graph(int n) {
        this.n = n;
        g = new Vertex[n];
        for (int i = 0; i < n; i++) g[i] = new Vertex(i);
        for(int i=0; i<n; i++)
            for(int j=0; j<n; j++)
                if(Mars.in.next().equals("+") && i<j) addEdge(i,j);
        BFS();
    }

    private void addEdge(int first, int second){
        Vertex v = g[first];
        while (v.next != null) v = v.next;
        v.next = new Vertex(second);
        v = g[second];
        if (first != second) {
            while (v.next != null) v = v.next;
            v.next = new Vertex(first);
        }
    }

    private void BFS(){
        Queue<Integer> q = new LinkedList<>();
        for(int v=0, c=1; v<n; v++ )
            if(g[v].mark==-1 && g[v].next!=null) {
                g[v].mark=c;
                q.offer(v);
                while (!q.isEmpty()) {
                    int j = q.remove();
                    for (Vertex e = g[j].next; e != null; e = e.next)
                        if (g[e.num].mark == -1) {
                            if(g[j].mark%2==0) g[e.num].mark=g[j].mark+1;
                            else g[e.num].mark=g[j].mark-1;
                            q.offer(e.num);
                        }
                        else if(g[e.num].mark==g[j].mark){
                            System.out.println("No solution");
                            System.exit(0);
                        }
                }
                component=c;
                c+=2;
            }
    }
}