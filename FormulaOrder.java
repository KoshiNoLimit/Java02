import java.util.*;

public class FormulaOrder {
    private static Scanner in = new Scanner(System.in);
    private static HashSet<String> vals = new HashSet<>();
    private static ArrayList<Graph.Vertex> vertexes = new ArrayList<>();

    public static void  main(String[] args)  {
        try {
            for (int i = 0; in.hasNextLine(); i++) {
                String string = in.nextLine();
                if (string.equals("")) break;
                String[] subs = string.split("=");
                if (subs.length != 2) throw new Exception("syntax error");
                for(String s : subs) if(s.endsWith(",") || s.startsWith(",")) throw new Exception("syntax error");
                String[] values = subs[0].split(",");
                String[] exceptions = subs[1].split(",");
                if (values.length != exceptions.length) throw new Exception("syntax error");
                for(int g=values.length-1; g>=0; g--){
                    values[g] = values[g].trim();
                    exceptions[g] = exceptions[g].trim();
                    if(values[g].contains("_") || exceptions[g].contains("_")) throw new Exception("syntax error");
                    for(char ch : values[g].toCharArray())
                        if (!Character.isLetterOrDigit(ch)) throw new Exception("syntax error");
                }
                for (String v : exceptions) if (v.trim().equals("")) throw new Exception("syntax error");
                for (String v : values) {
                    if (v.trim().equals("") || vals.contains(v.trim())) throw new Exception("syntax error");
                    vals.add(v.trim());
                }
                ArrayList<String> need = new ArrayList<>();
                try {
                    for (String ex : exceptions) {
                        Calc.Lexer L = new Calc.Lexer(ex);
                        Calc.parse(L.getTokens());
                        need.addAll(L.getValues());
                    }
                } catch (Exception e) { System.out.println(e.getMessage()); }
                vertexes.add(new Graph.Vertex(i, values, exceptions, need, string));
            }
            try {
                Graph G = new Graph(vertexes);
                G.DFS();
            } catch (Exception e) { System.out.println(e.getMessage()); }
        }catch (Exception e) { System.out.println(e.getMessage()); }
    }

    static class Graph {
        private static Vertex[] ls;
        private int n;

        Graph(ArrayList<Vertex> mass) throws Exception {
            n = mass.size();
            ls = new Vertex[n];
            System.arraycopy(mass.toArray(), 0, ls, 0, ls.length);
            for(Vertex v : ls) addEdge(v.num);
        }

        private static class Vertex {
            int num;
            char color = 'w';
            String[] exceptions;
            String[] values;
            ArrayList<String> need;
            Vertex next = null;
            String string;

            Vertex(int num, String[] values, String[] exceptions, ArrayList<String> need, String string) {
                this.num = num;
                this.values = values;
                this.exceptions = exceptions;
                this.need = need;
                this.string = string;
            }
        }

        private void addEdge(int num) throws Exception {
            Vertex v = ls[num];
            for(String val : ls[num].need)
                for(Vertex u : ls) {
                    if (Arrays.stream(u.values).anyMatch(s -> s.equals(val))) {
                        v.next = new Vertex(u.num, u.values, u.exceptions, u.need, u.string);
                    }
                    if (v.next != null) {
                        v = v.next;
                        break;
                    }
                    if(u.num == n-1) throw new Exception("syntax error");
                }
        }

        void DFS () throws Exception {
            ArrayList<String> write = new ArrayList<>();
            Stack<Vertex> s = new Stack<>();
            for(Vertex v : ls){
                if(v.color == 'w'){
                    s.push(ls[v.num]);
                    while (!s.empty()){
                        Vertex u = s.pop();
                        if(u.color == 'w'){
                            u.color = 'g';
                            s.push(u);
                            for(Vertex e = u.next; e!=null; e = e.next )
                                if(ls[e.num].color == 'w') s.push(ls[e.num]);
                                else if(ls[e.num].color == 'g') throw new Exception("cycle");
                        }
                        else if(u.color == 'g'){
                            u.color = 'b';
                            write.add(ls[u.num].string);
                        }
                    }
                }
            }
            write.forEach(System.out::println);
        }
    }

    static class Calc {
        private static ArrayList<Token> tokens;
        private static int pos;

        static class Token{
            private Type type;
            private String text;

            Token(Type type, String text) {
                this.type=type;
                this.text=text;
            }

            String getText() { return text; }

            Type getType() { return type; }

            @Override
            public String toString(){ return type + " " + text; }
        }

        enum Type{ PLUS, MINUS, STAR, SLASH, NUMBER, LBR, RBR, EOF }

        static class Lexer {
            private String str;
            private ArrayList<Token> ls;
            private int pos;
            private int length;
            private static Type[] mass ={Type.MINUS, Type.PLUS, Type.STAR, Type.SLASH, Type.LBR, Type.RBR};
            private static String chars = "-+*/()";
            static ArrayList<String> values;

            Lexer(String str) {
                this.str = str;
                ls = new ArrayList<>();
                length = str.length();
                values = new ArrayList<>();
            }

            ArrayList<Token> getTokens() {
                while(pos<length){
                    char ch = peek();
                    if(Character.isDigit(ch)) goNumber();
                    else if (chars.indexOf(ch)>=0) goOperator();
                    else if (Character.isLetter(ch)) goVal();
                    else next();
                }
                return ls;
            }
            ArrayList<String> getValues(){ return values; }

            private void goVal() {
                StringBuilder s = new StringBuilder();
                char ch = peek();
                while(Character.isLetterOrDigit(ch) || ch=='_' || ch=='$'){
                    s.append(ch);
                    ch=next();
                }
                values.add(s.toString());
                addToken(Type.NUMBER, "1");
            }

            private void goNumber(){
                StringBuilder s = new StringBuilder();
                char ch = peek();
                while(Character.isDigit(ch)){
                    s.append(ch);
                    ch = next();
                }
                addToken(Type.NUMBER, s.toString());
            }

            private void goOperator(){
                addToken(mass[chars.indexOf(peek())]);
                next();
            }

            private void addToken(Type type) {
                ls.add(new Token(type, ""));
            }
            private void addToken(Type type, String s) {
                ls.add(new Token(type, s));
            }

            private char peek() {
                return pos>=length  ? '\0' : str.charAt(pos);
            }

            private char next(){
                ++pos;
                return peek();
            }
        }

        private static void parse(ArrayList<Token> tokens) throws Exception{
            Calc.tokens = tokens;
            pos = 0;
            parseE();
            if(get(0).getType()!=Type.EOF) throw new Exception("syntax error");
        }

        private static int parseF() throws Exception{
            if(match(Type.NUMBER)) return Integer.parseInt(get(-1).getText());
            else if(match(Type.MINUS)) return - parseF();
            else if(match(Type.LBR)){
                int r = parseE();
                if(match(Type.RBR)) return r;
                else throw new Exception("syntax error");
            }
            else throw new Exception("syntax error");
        }

        private static int parseE() throws Exception{
            return (additive(parseT()));
        }

        private static int parseT() throws Exception{
            return (multiplicative(parseF()));
        }

        private static int additive(int res) throws Exception {
            if (match(Type.PLUS)) res = additive(res + parseT());
            else if (match(Type.MINUS)) res = additive(res - parseT());
            return res;
        }

        private static int multiplicative(int res) throws Exception {
            if(match(Type.STAR)) res = multiplicative(res * parseF());
            else if(match(Type.SLASH)) res = multiplicative(res / parseF());
            return res;
        }

        private static boolean match(Type type){
            if(type != get(0).getType()) return false;
            ++pos;
            return true;
        }

        private static Token get(int i){
            return pos+i>=tokens.size() ? new Token(Type.EOF, "") : tokens.get(pos+i);
        }
    }
}
