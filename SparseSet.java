import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;

public class SparseSet<T extends Hintable> extends AbstractSet<T> {
    private int n;
    private ArrayList<T> dense = new ArrayList<>();

    @Override
    public Iterator<T> iterator() { return new SSIterator(); }
    private class SSIterator implements Iterator<T>{
        private int pos;
        @Override
        public boolean hasNext() { return pos<n; }
        @Override
        public T next() { return dense.get(pos++);}
        @Override
        public void remove(){
            dense.get(--n).setHint(dense.get(pos-1).hint());
            dense.set(dense.get(pos-1).hint(), dense.get(n));
        }
    }

    @Override
    public int size() { return n; }

    @Override
    public void clear() { n = 0; }

    @Override
    public boolean contains(Object o){
        return ((T)o).hint()<n && dense.get(((T)o).hint())==o;
    }

    @Override
    public boolean add(T o) {
        if(contains(o)) return false;
        dense.add(o);
        o.setHint(n);
        ++n;
        return true;
    }

    @Override
    public boolean remove(Object o){
        if(!contains(o)) return false;
        dense.get(--n).setHint(((T)o).hint());
        dense.set(((T)o).hint(), dense.get(n));
        return true;
    }
}
