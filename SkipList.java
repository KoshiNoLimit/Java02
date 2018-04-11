import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class SkipList<K extends Comparable<K>,V> extends AbstractMap<K,V> {
    private int lvl;
    private Element l;
    private int size;

    public SkipList(int lvl){
        this.lvl=lvl;
        l = new Element(null, null);
        size=0;
    }

    @Override
    public Set<Entry<K,V>> entrySet() { return new Tree(); }

     class Tree extends TreeSet<Entry<K,V>>{
        @Override
        public Iterator<Entry<K, V>> iterator() {return new AMIterator();}

        private class AMIterator implements Iterator<Entry<K, V>> {
            private Element pos=l;

            @Override
            public boolean hasNext() { return pos.next[0] != null; }

            @Override
            public Element next() { return pos=pos.next[0]; }

            @Override
            public void remove(){ SkipList.this.remove(pos.key); }
        }
    }

    private class Element implements Entry<K, V> {
        private V val;
        private K key;
        private Element[] next = new SkipList.Element[lvl];
        private Element(K key, V val){
            this.key=key;
            this.val=val;
        }

        @Override
        public K getKey() { return key; }

        @Override
        public V getValue() { return val; }

        @Override
        public V setValue(V val) { return this.val=val; }

        public String toString(){return key+" "+val;}
    }

    @Override
    public boolean isEmpty() { return size==0; }

    private Element[] Skip (K k){
        Element x=l;
        Element [] p = new SkipList.Element[lvl];
        for(int i=lvl-1; i>=0; i--) {
            while (x.next[i] != null && x.next[i].key.compareTo(k) < 0) x = x.next[i];
            p[i] = x;
        }
        return p;
    }

    @Override
    public V put(K k, V v){
        Element[] p = Skip(k);
        if(p[0].next[0]!=null && p[0].next[0].key.equals(k)) {
            V old = p[0].next[0].val;
            p[0].next[0].val=v;
            return old;
        }
        Element x = new Element(k,v);
        int r = new Random().nextInt()*2;
        int i=0;
        while(i<lvl && r%2==0){
            x.next[i]=p[i].next[i];
            p[i].next[i]=x;
            ++i; r>>=1;
        }
        ++size;
        for(;i<lvl;i++) x.next[i]=null;
        return null;
    }
    @Override
    public V remove(Object k) {
        Element[] p = Skip((K)k);
        Element x = p[0].next[0];
        if (x==null || !x.key.equals(k)) return null;
        V v = x.val;
        for(int i=0; i<lvl && p[i].next[i]==x; i++) p[i].next[i]=x.next[i];
        --size;
        return v;

    }

    @Override
    public void clear(){l=new Element(null, null); size=0;}
    public int size() { return size; }
}
