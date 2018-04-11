import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Calc {
    private static ArrayList<Token> tokens;
    private static int pos;
    static Scanner in = new Scanner(System.in);

    public static void main(String args[]){
        try{ System.out.println( parse(new Lexer(in.nextLine()).getTokens()));}
        catch(Exception e){ System.out.println(e.getMessage()); }
    }

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

    enum Type{
        PLUS,
        MINUS,
        STAR,
        SLASH,
        NUMBER,
        LBR,
        RBR,
        EOF
    }

    static class Lexer {
        private String str;
        private ArrayList<Token> ls;
        private int pos;
        private int length;
        private static Type[] mass ={Type.MINUS, Type.PLUS, Type.STAR, Type.SLASH, Type.LBR, Type.RBR};
        private static String chars = "-+*/()";
        private static HashMap<String, Integer> values;

        Lexer(String str) {
            this.str = str;
            ls = new ArrayList<>();
            length = str.length();
            values = new HashMap<>();
        }

        ArrayList<Token> getTokens(){
            while(pos<length){
                char ch = peek();
                if(Character.isDigit(ch)) goNumber();
                else if (chars.indexOf(ch)>=0) goOperator();
                else if (Character.isLetter(ch)) goVal();
                else next();
            }
            return ls;
        }

        private void goVal(){
            StringBuilder s = new StringBuilder();
            char ch = peek();
            while(Character.isLetterOrDigit(ch) || ch=='_' || ch=='$'){
                s.append(ch);
                ch=next();
            }
            if(values.containsKey(s.toString())) addToken(Type.NUMBER, values.get(s.toString()).toString());
            else {
                values.put(s.toString(), in.nextInt());
                addToken(Type.NUMBER, values.get(s.toString()).toString());
            }
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

    private static int parse(ArrayList<Token> tokens) throws Exception{
        Calc.tokens = tokens;
        pos = 0;
        int result = parseE();
        if(get(0).getType()!=Type.EOF) throw new Exception("error");
        return result;
    }

    private static int parseF() throws Exception{
        if(match(Type.NUMBER)) return Integer.parseInt(get(-1).getText());
        else if(match(Type.MINUS)) return - parseF();
            else if(match(Type.LBR)){
                int r = parseE();
                if(match(Type.RBR)) return r;
                else throw new Exception("error");
            }
        else throw new Exception("error");
    }

    private static int parseE() throws Exception{
        return (additive(parseT()));
    }

    private static int parseT() throws Exception{
        return (multiplicate(parseF()));
    }

    private static int additive(int res) throws Exception {
        if (match(Type.PLUS)) res = additive(res + parseT());
        else if (match(Type.MINUS)) res = additive(res - parseT());
        return res;
    }

    private static int multiplicate(int res) throws Exception {
        if(match(Type.STAR)) res = multiplicate(res * parseF());
        else if(match(Type.SLASH)) res = multiplicate(res / parseF());
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

