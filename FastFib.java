import java.util.Scanner;
import java.math.BigInteger;
import static java.math.BigInteger.*;

public class FastFib {
    private static BigInteger calcFib(Integer n) {
        BigInteger[][] b = {{ONE, ONE}, {ONE, ZERO}};
        BigInteger[][] c = new BigInteger[2][2];
        BigInteger[][] f = {{ONE, ONE}, {ONE, ZERO}};
        for (int i = n-1; i > 0; i/=2) {
            if (i % 2 != 0) {
                for (int j = 0; j < 2; j++) { System.arraycopy(f[j], 0, c[j], 0, 2); }
                for (int j = 0; j < 2; j++) {
                    for (int k = 0; k < 2; k++) { f[j][k] = (c[0][k].multiply(b[j][0])).add(c[1][k].multiply(b[j][1])); }
                }
            }
            for (int j = 0; j < 2; j++) { System.arraycopy(b[j], 0, c[j], 0, 2); }
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    b[j][k] = (c[0][k].multiply(c[j][0])).add(c[1][k].multiply(c[j][1]));
                }
            }
        }
        return f[0][0].subtract(f[1][1]);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println(calcFib(in.nextInt()));
    }
}

