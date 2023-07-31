package Breakthrough;

import java.io.*;
import java.net.*;



public class Client {

    private static Tableau tableau;

    public static int joueur;

    public static void main(String[] args) {

        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[8][8];
        boolean open = true;
        try {
            MyClient = new Socket(InetAddress.getLocalHost(), 8888);
            //1MyClient = new Socket(args[0], Integer.parseInt(args[1]));

            input    = new BufferedInputStream(MyClient.getInputStream());
            output   = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while(open){
                char cmd = 0;

                cmd = (char)input.read();
                // Debut de la partie en joueur rouge
                if(cmd == '1'){

                    joueur = 1;

                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[y][x] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 8){
                            x = 0;
                            y++;
                        }
                    }

                    tableau = new Tableau(board);

                    System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");

                    
                    Mouvement bestMove = MinMax.getBestMove(tableau, true);

                    System.out.println("***** " + bestMove + " *****");

                    tableau.getTableau()[bestMove.getFrom().getY()][bestMove.getFrom().getX()] = 0;
                    if (bestMove.getTypePion() == 4) {
                        tableau.getTableau()[bestMove.getTo().getY()][bestMove.getTo().getX()] = 4;
                    }
                    String move = bestMove.toString();

                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                }
                // Debut de la partie en joueur Noir
                if(cmd == '2'){
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des rouges");

                    joueur = 2;

                    byte[] aBuffer = new byte[1024];

                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer).trim();
                    String[] boardValues;
                    boardValues = s.split(" ");
                    int x=0,y=0;
                    for(int i=0; i<boardValues.length;i++){
                        board[y][x] = Integer.parseInt(boardValues[i]);
                        x++;
                        if(x == 8){
                            x = 0;
                            y++;
                        }
                    }

                    tableau = new Tableau(board);
                }


                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if(cmd == '3'){
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    input.read(aBuffer,0,size);

                    String s = new String(aBuffer);
                    System.out.println("Dernier coup :"+ s);

                    tableau.trackOpponentMove(s);

                    System.out.println("Entrez votre coup : ");
                    String move;

                    if (joueur == 1) {
                        Mouvement bestMove = MinMax.getBestMove(tableau, true);
                        tableau.getTableau()[bestMove.getFrom().getY()][bestMove.getFrom().getX()] = 0;
                        if (bestMove.getTypePion() == 4) {
                            tableau.getTableau()[bestMove.getTo().getY()][bestMove.getTo().getX()] = 4;
                        }
                        move = bestMove.toString();
                    }
                    else {
                        Mouvement bestMove = MinMax.getBestMove(tableau, false);
                        tableau.getTableau()[bestMove.getFrom().getY()][bestMove.getFrom().getX()] = 0;
                        if (bestMove.getTypePion() == 2) {
                            tableau.getTableau()[bestMove.getTo().getY()][bestMove.getTo().getX()] = 2;
                        }
                        move = bestMove.toString();

                    }

                    System.out.println("***** " + move + " *****");

                    

                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                    
                }
                // Le dernier coup est invalide
                if(cmd == '4'){
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    System.out.println("Current tableau : ");
                    for(int i=0; i<board.length;i++){
                        for (int j=0; j< board.length; j++) {
                            System.out.print(board[i][j]);
                        }
                        System.out.println();
                    }
                    System.out.println("End current tableau : ");
                    String move;

                    if (joueur == 1){
                        Mouvement bestMove = MinMax.getBestMove(tableau, true);
                        tableau.getTableau()[bestMove.getFrom().getY()][bestMove.getFrom().getX()] = 0;
                        if (bestMove.getTypePion() == 4) {
                            tableau.getTableau()[bestMove.getTo().getY()][bestMove.getTo().getX()] = 4;
                        }
                        move = bestMove.toString();
                    }
                    else{
                        Mouvement bestMove = MinMax.getBestMove(tableau, false);
                        tableau.getTableau()[bestMove.getFrom().getY()][bestMove.getFrom().getX()] = 0;
                        if (bestMove.getTypePion() == 2) {
                            tableau.getTableau()[bestMove.getTo().getY()][bestMove.getTo().getX()] = 2;
                        }
                        move = bestMove.toString();
                    }

                    output.write(move.getBytes(),0,move.length());
                    output.flush();

                }
                // La partie est terminée
                if(cmd == '5'){
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer,0,size);
                    String s = new String(aBuffer);
                    System.out.println("Partie Terminé. Le dernier coup joué est: "+s);
                    String move = null;
                    move = console.readLine();
                    output.write(move.getBytes(),0,move.length());
                    output.flush();
                    output.close();
                    input.close();
                    open = false;
                    System.out.close();
                    System.in.close();
                }
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }

    }

}
