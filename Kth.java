import java.util.Scanner;

public class BridgeNum { 
    private static Graph g;
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        g = new Graph(n);
        
        int edge = in.nextInt();
        for (int i = 0; i < edge; i++) {
            int left = in.nextInt();
            int right = in.nextInt();
            g.add(left, right);
        }
        
        g.DFS1();
        g.DFS2();
    }
}

class Graph {
    class Element {
        private Element next, parent;
        private int comp, mark;
        private final int value;    
        public Element(int i) { value = i; next = null;}
    }
    private static Element[] graph;
    private static Queue q;
    private final int vertex;  
    private int count;
    
    public Graph(int vert) {
        graph = new Element[vertex = vert];
        for (int i = 0; i < vertex; i++) graph[i] = new Element(i);
        q = new Queue(vertex);
    }
    
    public void add(int left, int right) {
        Element temp = graph[left];
        Element a = new Element(right);
        while (temp.next != null) temp = temp.next;
        temp.next = a;
                
        temp = graph[right];
        a = new Element(left);
        while (temp.next != null) temp = temp.next;
        temp.next = a;
    }
    
    public void DFS1() {
        int n = 0;
        for (int i = 0; i < vertex; i++) graph[i].mark = 0;
        
        for (int i = 0; i < vertex; i++) {
            if (graph[i].mark == 0) {
                n++;
                graph[i].parent = null;
                VisitVertex1(graph[i]);
            }
        }
        count = n;
    }
    
    private void VisitVertex1(Element v) {
        v.mark = 1;
        q.enq(v.value);
        for (Element u = v.next; u != null; u = u.next) {
            if (graph[u.value].mark == 0) {
                graph[u.value].parent = v;
                VisitVertex1(graph[u.value]);
            }
        }
        v.mark = 2;
    }
    
    public void DFS2() {
        for (int i = 0; i < vertex; i++) graph[i].comp = -1;
        int component = 0;
        
        while (!q.isEmpty()) {
            int v = q.deq();
            if (graph[v].comp == -1) {
                VisitVertex2(graph[v], component);
                component++;
            }
        }
        System.out.println(component);
        System.out.println(component);
    }
    
    private void VisitVertex2(Element v, int component) {
        v = graph[v.value];
        v.comp = component;
        
        for (Element u = v.next; u != null; u = u.next) {
            if ((graph[u.value].comp == -1) && (graph[u.value].parent != v))
                VisitVertex2(graph[u.value], component);
        }
    }
}

class Queue {
    int head;
    int tail;
    int[] data;

    Queue(int size) {
        data = new int[size];
        tail = 0;
        head = 0;
    }

    void enq(int value) {
        data[tail] = value; 
        tail++;
    }

    int deq() {
        return data[head++];
    }

    boolean isEmpty() {
        return head == tail;
    }
}