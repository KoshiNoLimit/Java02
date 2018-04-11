public class Element<T> {
    private T x;
    private int depth=0;
    private Element<T> parent = this;

    public Element(T x){ this.x = x; }

    public T x(){ return x; }

    public boolean equivalent(Element<T> x){ return this.parent==find(x).parent; }

    public void union(Element<T> y){
        Element<T> rooty=find(y);
        Element<T> rootx=find(this);
        if(rootx.depth<rooty.depth) rootx.parent=rooty;
        else{
            rooty.parent=rootx;
            if(rootx.depth==rooty.depth && rootx!=rooty) ++rootx.depth;
        }
    }

    private Element<T> find (Element<T> x){ return x.parent==x ? x : find(x.parent); }
}
