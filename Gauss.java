import java.util.Scanner;

public class Gauss {
    public static void main(String[] args){

        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        Element[][] A = new Element[n][n+1];

        for(int i=0; i<n; i++)
            for(int j=0; j<n+1; j++) A[i][j] = new Element(in.nextInt(), 1);
        for(int i=0; i<n; i++){
            if (A[i][i].isNull()) {
                if(i==n-1){
                    System.out.println("No solution");
                    System.exit(0);
                }
                for(int j=i+1; j<n; j++){
                    if (!A[j][i].isNull()) {
                        Element[] jack = A[i];
                        A[i]=A[j];
                        A[j]=jack;
                        break;
                    }
                    if(j==n-1){
                        System.out.println("No solution");
                        System.exit(0);
                    }
                }
            }
            for(int j=n; j>=i; j--) A[i][j]=Element.devide(A[i][j], A[i][i]);
            for(int j=i+1; j<n; j++) {
                Element e = Element.devide(A[j][i], A[i][i]);
                for (int k = n; k >=i; k--) A[j][k]=Element.subtract(A[j][k], Element.multiply(e, A[i][k]));
            }
        }
        Element[] answers = new Element[n];
        for(int i=n-1; i>=0; i--){
            for(int j=i+1; j<n; j++)
                A[i][n]=Element.subtract(A[i][n], Element.multiply(A[i][j], answers[j]));
            answers[i]=A[i][n];
        }
        for(Element e : answers) System.out.println(e);
    }

    static class Element{
        private int top;
        private int not;

        private Element(int top, int not){
            this.top=top;
            this.not=not;
            beauty();
        }

        private static Element subtract(Element o1, Element o2){
            return new Element(o1.top*o2.not - o2.top*o1.not, o1.not*o2.not);
        }
        private static Element multiply(Element o1, Element o2){
            return new Element(o1.top*o2.top, o1.not*o2.not);
        }
        private static Element devide(Element o1, Element o2){
            return new Element(o1.top*o2.not, o1.not*o2.top);
        }

       private void beauty(){
           if(top==0)not=1;
           if(not<0){not=-not; top=-top;}
           if(top!=1 && not!=1) {
               int x = Math.abs(not), y = Math.abs(top);
               while (x != y) {
                   if (x > y) {
                       x = x - y;
                   } else y = y - x;
               }
               not /= x;
               top /= y;
           }
       }

       private boolean isNull(){ return top==0;}

       @Override
       public String toString(){ return top+"/"+not;}
    }
}
