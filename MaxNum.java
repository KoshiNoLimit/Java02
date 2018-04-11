import java.util.Scanner;
import java.util.Arrays;

public class MaxNum{
    private String ar[];
    public MaxNum(String[] arr){
        ar=arr;
        Arrays.sort(ar, (s1, s2) -> Integer.compare(-Integer.parseInt(s1+s2), -Integer.parseInt(s2+s1)));
    }

    public String toString(){
        return String.join("",ar);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String ar[] = new String[n];
        for(int i=0;i<n;i++){
            ar[i]=Integer.toString(in.nextInt());
        }
        MaxNum f = new MaxNum(ar);
        System.out.println(f);
    }
}