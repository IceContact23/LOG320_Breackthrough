package Breakthrough;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************
 * Title: GenerateurMouvement
 * Description: La classe GénérateurMouvement génère des mouvements légaux pour
 * le jeu.
 *
 * 
 *
 * @author Gilles Foster Djoko Kamgaing , Hassan Khanafer, Andy Mahautiere
 ***********************************************************************************************/


public class GenerateurMouvement {

    public GenerateurMouvement() {
    }

    /**
     * Génère une liste des mouvements légaux pour un état de plateau de jeu et un
     * joueur donnés.
     *
     * @param tableau        Le plateau de jeu représenté sous la forme d'un tableau d'entiers 2D.
     * @param estJoueurRouge Indique si le joueur en cours est rouge ou non.
     * @return La liste des mouvements légaux.
     */
    public static List<Mouvement> generateMouvementLegal(int[][] tableau, boolean estJoueurRouge) {
        int pion = estJoueurRouge ? 4 : 2;
        List<Mouvement> mouvementsLegal = new ArrayList<>();
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if(tableau[i][j] == pion){
                    generateMovement(tableau, j, i, mouvementsLegal, estJoueurRouge);
                }
            }
        }
        return mouvementsLegal;
    }
    /**
     * Génère des mouvements légaux pour un pion à la position donnée
     *
     * @param tableau         Le plateau de jeu représenté sous la forme d'un tableau d'entiers 2D.
     * @param posX               L'index de colonne du pion.
     * @param posY               L'index de ligne du pion.
     * @param mouvementsLegal La liste pour stocker les mouvements légaux générés.
     * @param estJoueurRouge  Indique si le joueur en cours est rouge ou non.
     */
    private static void generateMovement(int[][] tableau, int posX, int posY, List<Mouvement> mouvementsLegal, boolean estJoueurRouge) {
        int pion = estJoueurRouge ? 4 : 2;
        int pionAdversaire = estJoueurRouge ? 2 : 4;
        int rowDirection = estJoueurRouge ? -1 : 1;

        int[][] directions = {
                {rowDirection, -1},
                {rowDirection, 0},
                {rowDirection, 1}
        };

        for (int[] direction : directions) {
            int newY = posY + direction[0];
            int newX = posX + direction[1];

            if (isValidMouvement(tableau, posX, posY, newX, newY, estJoueurRouge)) {
                mouvementsLegal.add(new Mouvement(pion, new Coordonne(posX, posY), new Coordonne(newX, newY)));
            }
        }
    }

    /**
     * Vérifie si une position de ligne et de colonne donnée est valide sur le
     * plateau de jeu.
     * @return `true` si la position est valide, `false` sinon.
     */
    private static boolean isValidMouvement(int[][] tableau, int oldX, int oldY, int newX, int newY, boolean estJoueurRouge) {
        int pion = estJoueurRouge ? 4 : 2;
        int pionAdversaire = estJoueurRouge ? 2 : 4;
        int rowDirection = estJoueurRouge ? -1 : 1;

        if(newX >= 0 && newX < tableau[0].length && newY >= 0 && newY < tableau.length){
            if(newX == oldX && newY == oldY+rowDirection && tableau[newY][newX] == 0){ //avance d'une case en avant
                return true;
            }else if((newX == oldX-1 || newX == oldX+1) && newY == oldY+rowDirection && (tableau[newY][newX] == 0 || tableau[newY][newX] == pionAdversaire)){//avance d'une case en diagonale
                return true;
            }
            return false;
        }else{
            return false;
        }
    }
}
