import java.util.Scanner;
import java.util.HashSet;
public class Econom {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        HashSet<String> hs = new HashSet<>();
        String s = in.nextLine();
        int len = s.length();
        for(int i=0;i<len;){
            while(i<len && s.charAt(i)!='(') ++i;
            if(i==len) break;
            int j=++i;
            for(int f=1; f!=0; j++)
                if(s.charAt(j)=='(') ++f;
                else if(s.charAt(j)==')') --f;
            hs.add(s.substring(i, j-1));
        }
        System.out.println(hs.size());
    }
}
