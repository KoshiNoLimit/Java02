import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.TreeSet;

public class Sync {
    private static TreeSet<String> del = new TreeSet<>();
    private static TreeSet<String> cop = new TreeSet<>();

    public static void main(String args[]) throws IOException {
        comp(new File(args[0]), new File(args[1]));
        if(del.isEmpty() && cop.isEmpty()) System.out.println("IDENTICAL");
        else {
            for (String s : del) System.out.println("DELETE " + s.substring(args[1].length()+1));
            for (String s : cop) System.out.println("COPY " + s.substring(args[0].length()+1));
        }
    }

    private static void comp(File S, File D) throws IOException {
        if (S.exists())
            for (String p : S.list()) {
                File s = new File(S, p);
                File d = new File(D, p);
                if (!d.exists()) goFile(s, cop);
                else if (s.isDirectory()) comp(s, d);
                    else if (!Arrays.equals(Files.readAllBytes(s.toPath()), Files.readAllBytes(d.toPath()))) {
                        del.add(d.getAbsolutePath());
                        cop.add(s.getAbsolutePath());
                    }
            }
        if (D.exists())
            for (String p : D.list()) {
                File s = new File(S, p);
                File d = new File(D, p);
                if(!s.exists()) goFile(d, del);
            }
    }

    private static void goFile(File F, TreeSet<String> ar){
        if(F.isDirectory())
            for(File f : F.listFiles()) goFile(f, ar);
        else ar.add(F.getAbsolutePath());
    }
}
