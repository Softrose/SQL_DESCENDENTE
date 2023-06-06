import java.util.List;

public class Parser {

    private final List<Token> tokens;

    private final Token IDENTIFICADOR = new Token(TipoToken.IDENTIFICADOR, "");
    private final Token SELECT = new Token(TipoToken.SELECT, "select");
    private final Token FROM = new Token(TipoToken.FROM, "from");
    private final Token DISTINCT = new Token(TipoToken.DISTINCT, "distinct");
    private final Token COMMA = new Token(TipoToken.COMMA, ",");
    private final Token DOT = new Token(TipoToken.DOT, ".");
    private final Token STAR = new Token(TipoToken.STAR, "*");
    private final Token EOF = new Token(TipoToken.EOF, "");

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        Q();

        if(!hayErrores && !preanalisis.equals(EOF)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(EOF)){
            System.out.println("Consulta válida");
        }
    }

    void Q(){
        if(preanalisis.equals(SELECT)){
            coincidir(SELECT);
            D();
            coincidir(FROM);
            T();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba la palabra reservada SELECT.");
        }
    }

    void D(){
        if(hayErrores) return;

        if(preanalisis.equals(DISTINCT)){
            coincidir(DISTINCT);
            P();
        }
        else if(preanalisis.equals(STAR) || preanalisis.equals(IDENTIFICADOR)){
            P();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba DISTINCT, * o un identificador.");
        }
    }

    void P(){
        if(hayErrores) return;

        if(preanalisis.equals(STAR)){
            coincidir(STAR);
        }
        else if(preanalisis.equals(IDENTIFICADOR)){
            A();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición \" + preanalisis.posicion + \". Se esperaba * o un identificador.");
        }
    }

    void A(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            A2();
            A1();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void A1(){
        if(hayErrores) return;

        if(preanalisis.equals(COMMA)){
            coincidir(COMMA);
            A();
        }
    }

    void A2(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            coincidir(IDENTIFICADOR);
            A3();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void A3(){
        if(hayErrores) return;

        if(preanalisis.equals(DOT)){
            coincidir(DOT);
            coincidir(IDENTIFICADOR);
        }
    }

    void T(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            T2();
            T1();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void T1(){
        if(hayErrores) return;

        if(preanalisis.equals(COMMA)){
            coincidir(COMMA);
            T();
        }
    }

    void T2(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            coincidir(IDENTIFICADOR);
            T3();
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un identificador.");
        }
    }

    void T3(){
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR)){
            coincidir(IDENTIFICADOR);
        }
    }


    void coincidir(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un  " + t.tipo);

        }
    }

}
