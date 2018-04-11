import java.util.Scanner;

public class MinDist {
    int min=2000000, i, l, n;
    char c;

    public MinDist(String s, char x, char y){
        l=s.length();
        for(i=0;(s.charAt(i)!=x)&&(s.charAt(i)!=y);i++);
        c=s.charAt(i);
        for(i++;i<l;i++,n++){
            if((s.charAt(i)==x)||(s.charAt(i)==y)){
                if(!(s.charAt(i)==c)){
                    if (n<min){min=n;}
                    c=s.charAt(i);
                }
                n=-1;
            }
        }
    }

    public String toString(){
        return min + "";
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        char x = in.next().charAt(0), y = in.next().charAt(0);
        MinDist m = new MinDist(s,x,y);
        System.out.println(m);
    }
}