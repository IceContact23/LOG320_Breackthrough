package Breakthrough;

/**********************************************************************************************
 * Title: Evaluation
 * Description: Classe chargée d'évaluer l'état du jeu et de lui attribuer un
 * score
 * 
 *
 * @author Gilles Foster Djoko Kamgaing , Hassan Khanafer, Andy Mahautiere
 ***********************************************************************************************/

public class Evaluation {

    int[][] tableau;
    int rowLenght;
    int colLenght;
    private int nbPionRouge;
    private int nbPionNoir;
    private int pionRougeOpen;
    private int pionNoirOpen;
    private int pionRougeSafe;
    private int pionNoirSafe;
    private int pionRougeInDanger;
    private int pionNoirInDanger;
    private int pionRougeBlock;
    private int pionNoirBlock;
    private int gagneProxiRouge;
    private int gagneProxiNoir;
    private int controleCentreRouge;
    private int controleCentreNoir;
    private int pionNoir = 2;
    private int pionRouge = 4;
    /**
     * TODO: INVERSER X et Y du tableau
     * */

    /**
     * Construit un objet Evaluation avec le tableau donné.
     * 
     * @param tableau le board de jeu
     */
    public Evaluation(Tableau tableau) {
        this.tableau = tableau.getTableau();
        this.rowLenght = tableau.getTableau().length;
        this.colLenght = tableau.getTableau()[0].length;
    }

    /**
     * Évalue l'état du jeu et renvoie le score d'évaluation pour le joueur spécifié
     * 
     * @param joueur Le joueur pour qui évaluer l'état du jeu.
     * @return Le score d'évaluation pour le joueur spécifié.
     */
    public double evaluate(int joueur) {

        double evaluation;

        evaluateAvantageMaterial();
        evaluatePionOpen();
        //evaluateBlock();
        evaluateGagneProxi();
        evaluateControleCentre();
        evaluatePionSafe();

        // Facteurs de poids d'évaluation
        int poidProtection = 50;
        int poidDanger = 50;
        int poidPionOpen = 50;
        int poidPionPremiereLigneProteger = 35;
        //int poidBlock = 35;
        int poidPionPremiereLigneVivant = 50;
        int poidPionDeuxiemeLigneVivant = 50;

        // Calculer le score d'évaluation
        if (joueur == 1) { //Pion rouge
            evaluation = poidPionPremiereLigneVivant * (nbPionRouge - nbPionNoir)
                    + poidPionDeuxiemeLigneVivant * (nbPionRouge - nbPionNoir)
                    + poidProtection * pionRougeSafe /*+ poidDanger * (pionNoirInDanger - pionRougeInDanger)*//*poidPionPremiereLigneProteger * safetyPionRouge*/
                    //+ poidPionOpen * (pionRougeOpen - pionNoirOpen)
                    + gagneProxiRouge - gagneProxiNoir + controleCentreRouge;
        } else { //Pion noir
            evaluation = poidPionPremiereLigneVivant * (nbPionNoir - nbPionRouge)
                    + poidPionDeuxiemeLigneVivant * (nbPionNoir - nbPionRouge)
                    + poidProtection * pionNoirSafe /*+ poidDanger * (pionRougeInDanger - pionNoirInDanger)*//*poidPionPremiereLigneProteger * safetyPionNoir*/
                    //+ poidPionOpen * (pionNoirOpen - pionRougeOpen)
                    + gagneProxiNoir - gagneProxiRouge + controleCentreNoir;
        }

        return (joueur == Client.joueur) ? evaluation : -(evaluation);
    }

    /**
     * Vérifie si le jeu est dans un état gagnant pour le joueur spécifié
     * 
     * @param joueur Le joueur à vérifier pour un état gagnant.
     * @return Vrai si le joueur a gagné, Faux sinon.
     */
    public boolean inWinState(int joueur) {

        int pionRougePremiereLigne = 0;
        int pionNoirPremiereLigne = 0;

        // Compte le nombre de pièces de la première rangée pour chaque joueur
        for (int row = 0; row < tableau.length; row++) {
            for (int col = 0; col < tableau[row].length; col++) {

                if (tableau[row][col] == 4)
                    pionRougePremiereLigne++;

                if (tableau[row][col] == 2)
                    pionNoirPremiereLigne++;

                // Vérifiez si les pièces du joueur ont atteint la première rangée de
                // l'adversaire
                if (joueur == 1) {
                    if (tableau[row][col] == 4 || tableau[row][col] == 4) {
                        if (col == 0) {
                            return true;
                        }
                    }
                } else {
                    if (tableau[row][col] == 2 || tableau[row][col] == 2) {
                        if (col == 7) {
                            return true;
                        }
                    }
                }
            }
        }

        // Vérifier si toutes les pièces de l'adversaire ont été éliminées
        return (joueur == 1 && pionNoirPremiereLigne == 0) || (joueur == 2 && pionRougePremiereLigne == 0);
    }

    /**
     * Calcule le nombre de pièces rouges et noires sur le plateau de jeu.
     */
    private void evaluateAvantageMaterial() {
        nbPionRouge = 0;
        nbPionNoir = 0;

        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == 4)
                    nbPionRouge++;

                if (tableau[i][j] == 2)
                    nbPionNoir++;
            }
        }
    }

    /**
     * Calcule le nombre de pièces rouges et noires protégées et ceux en danger sur le plateau de jeu.
     */
    private void evaluatePionSafe() {
        pionRougeSafe = nbPionRouge;
        pionNoirSafe = nbPionNoir;
        pionRougeInDanger = 0;
        pionNoirInDanger = 0;
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == pionNoir && i+1 <= rowLenght-1 && ((j+1 <= colLenght-1  && tableau[i+1][j+1] == pionRouge) || (j-1 >= 0 && tableau[i+1][j-1] == pionRouge))){
                    pionNoirSafe--;
                    pionNoirInDanger++;
                }

                if (tableau[i][j] == pionRouge && i-1 >= 0 && ((j+1 <= colLenght-1 && tableau[i-1][j+1] == pionNoir)  || (j-1 >= 0 && tableau[i-1][j-1] == pionNoir))){
                    pionRougeSafe--;
                    pionRougeInDanger++;
                }
            }
        }
    }

    /**
     * Calcule la sécurité des poussoirs rouges et noirs sur le plateau de jeu.
     */
    private void evaluatePionOpen() {
        pionRougeOpen = 0;
        pionNoirOpen = 0;

        //Pion rouge
        for (int i = 2; i <= 4; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if(tableau[i][j] == pionRouge){
                    pionRougeOpen++;
                    boolean safe = true;

                    if (i-1 >= 0 && ((j+1 <= colLenght-1 && tableau[i-1][j+1] == pionNoir)  || (j-1 >= 0 && tableau[i-1][j-1] == pionNoir))){
                        pionRougeOpen--;
                        safe = false;
                    }
                    for(int saut = i-2; saut >= 0 && safe; saut= saut-2){
                        if(saut-1 >= 0 && (tableau[saut-1][j] == pionNoir || (j+1 <= colLenght-1 && tableau[saut-1][j+1] == pionNoir)  || (j-1 >= 0 && tableau[saut-1][j-1] == pionNoir))){
                            pionRougeOpen--;
                            safe = false;
                        }
                    }
                }
            }
        }

        //Pion noir
        for (int i = 3; i <= 5; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if(tableau[i][j] == pionNoir){
                    pionNoirOpen++;
                    boolean safe = true;

                    if (i+1 <= rowLenght-1 && ((j+1 <= colLenght-1 && tableau[i+1][j+1] == pionRouge)  || (j-1 >= 0 && tableau[i+1][j-1] == pionRouge))){
                        pionNoirOpen--;
                        safe = false;
                    }
                    for(int saut = i+2; saut <= rowLenght-1 && safe; saut= saut+2){
                        if(saut+1 <= rowLenght-1 && (tableau[saut+1][j] == pionRouge || (j+1 <= colLenght-1 && tableau[saut+1][j+1] == pionRouge)  || (j-1 >= 0 && tableau[saut+1][j-1] == pionRouge))){
                            pionNoirOpen--;
                            safe = false;
                        }
                    }
                }
            }
        }
    }

    /**
     * Calcule le nombre de pièces rouges et noires bloquées sur le plateau de jeu.
     */
    private void evaluateBlock() {
        pionRougeBlock = 0;
        pionNoirBlock = 0;

        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == 4) {
                    if (i != 0 && tableau[i - 1][j] != 0 && tableau[i - 1][j] != 4)
                        pionRougeBlock++;
                }

                if (tableau[i][j] == 2) {
                    if (i != tableau.length - 1 && tableau[i + 1][j] != 0
                            && tableau[i + 1][j] != 2)
                        pionNoirBlock++;
                }
            }
        }
    }

    /**
     * Calcule le nombre de pièces rouges et noires qui sont proches de gagner.
     */
    private void evaluateGagneProxi() {
        gagneProxiRouge = 0;
        gagneProxiNoir = 0;

        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == 4 && (i == 0 || i == 1))
                    gagneProxiRouge++;

                if (tableau[i][j] == 2
                        && (i == tableau.length - 1 || i == tableau.length - 2))
                    gagneProxiNoir++;
            }
        }
    }

    /**
     * Calcule le contrôle du centre du plateau de jeu par les pièces rouges et
     * noires.
     */
    private void evaluateControleCentre() {
        controleCentreRouge = 0;
        controleCentreNoir = 0;

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == 4)
                    controleCentreRouge++;

                if (tableau[i][j] == 2)
                    controleCentreNoir++;
            }
        }
    }
}