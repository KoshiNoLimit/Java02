import java.util.AbstractSet;
import java.util.Iterator;

public class IntSparseSet extends AbstractSet<Integer> {
    private int low;
    private int high;
    private int n=0;
    private int sparse[];
    private int dense[];

    IntSparseSet(int low, int high){
        this.low=low;
        this.high=high;
        dense = new int[high-low];
        sparse = new int[high-low];
    }

    @Override
    public Iterator<Integer> iterator() { return new ISSIterator();}
    private class ISSIterator implements Iterator<Integer>{
        private int pos;
        ISSIterator(){ pos=0; }
        public boolean hasNext(){ return pos<n; }
        public Integer next(){ return dense[pos++]; }

        @Override
        public void remove() {
            dense[pos-1]=dense[--n];
            sparse[dense[n]]=pos-1;
        }
    }

    @Override
    public boolean contains(Object o){
        return low<=(int)o && (int)o<high && 0<=sparse[(int)o-low] && sparse[(int)o-low]<n && dense[sparse[(int)o-low]]==(int)o;
    }

    @Override
    public boolean add(Integer o) {
        if (low<=o && o<high && (sparse[o-low]>=n || dense[sparse[o-low]]!=o))  {
            dense[n]=o;
            sparse[o-low]=n++;
            return true;
        }
        return false;

    }

    @Override
    public boolean remove(Object o) {
        if(contains(o)){
            dense[sparse[(int)o-low]]=dense[--n];
            sparse[dense[n]-low]=sparse[(int)o-low];
            return true;
        }
        return false;
    }

    @Override
    public void clear() { n=0; }

    @Override
    public int size() { return n; }
}
