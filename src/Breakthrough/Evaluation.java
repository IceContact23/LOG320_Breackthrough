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
    private int pionRougeVivant;
    private int pionNoirVivant;
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
    private int scoreAvancementRouge;
    private int scoreAvancementNoir;
    private int scoreGroupePionRouge;
    private int scoreGroupePionNoir;
    private int trouBaseRouge;
    private int trouBaseNoir;
    private int pionNoir = 2;
    private int pionRouge = 4;

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
        evaluateGagneProxi();
        evaluatePionSafe();
        evaluateAvancementPion();
        evaluateGroupementPion();
        evaluateTrouBase();

        // Facteurs de poids d'évaluation
        int poidAvancement = 1;
        int poidTrouBase = -5000;
        int poidPionSafe = 50;
        int poidPionInDanger = -1000;
        int poidPionOpen = 0;
        int poidPionVivant = 5;
        int poidRatioPionVivant = 35;
        int poidControleCentre = 0;
        int poidGagneProxi = 1000000;

        // Calculer le score d'évaluation
        if (joueur == 1) { //Pion rouge
            evaluation = 0;
        } else { //Pion noir
            evaluation = 0;
        }
        return (joueur == Client.joueur) ? evaluation : -(evaluation);
    }

    private void evaluateTrouBase() {
        for (int x = 0; x < tableau[0].length; x++) {
            if(tableau[rowLenght-1][x] == 0){// Base rouge
                int nbCaseVide = 0;
                for (int y = rowLenght-1; y > 4 ; y--) {
                    if (tableau[y][x] == 0){
                        nbCaseVide++;
                    }
                }
                trouBaseRouge += nbCaseVide == 3 ? 1 : 0;
            }

            if(tableau[0][x] == 0){// Base noir
                int nbCaseVide = 0;
                for (int y = 0; y < 3 ; y++) {
                    if (tableau[y][x] == 0){
                        nbCaseVide++;
                    }
                }
                trouBaseNoir += nbCaseVide == 3 ? 1 : 0;
            }
        }
    }

    private void evaluateGroupementPion() {
        scoreGroupePionRouge = 0;
        scoreGroupePionNoir = 0;

        int poidScoreGroupe = 5;
        int[][] directions = {
                {1, -1},
                {1, 0},
                {1, 1},
                {0, -1},
                {0, 1},
                {-1, -1},
                {-1, 0},
                {-1, 1},
        };

        for (int i = 0; i < tableau.length; i++) {
            int nbPionNoir = 0;
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == pionRouge){
                    int nbPionRouge = 0;
                    for(int[] direction : directions){
                        int x = direction[1];
                        int y = direction[0];
                        if(y >= 0 && y <= rowLenght-1 && x >= 0 && x <= colLenght-1 && tableau[y][x] == pionRouge){
                            nbPionRouge++;
                        }
                    }
                    scoreGroupePionRouge += poidScoreGroupe * nbPionRouge;
                }

                if (tableau[i][j] == pionNoir){
                    for(int[] direction : directions){
                        int x = direction[1];
                        int y = direction[0];
                        if(y >= 0 && y <= rowLenght-1 && x >= 0 && x <= colLenght-1 && tableau[y][x] == pionNoir){
                            nbPionNoir++;
                        }
                    }
                    scoreGroupePionNoir += poidScoreGroupe * nbPionNoir;
                }
            }
        }
    }

    private void evaluateAvancementPion() {
        int[] poidAvancement = new int[] {100, 70, 50, 40, 30, 20, 10, 0};
        //int[] poidAvancement = new int[] {0, 0, 0, 400, 400, 0, 0, 0};
        scoreAvancementRouge = 0;
        scoreAvancementNoir = 0;

        for (int i = 0; i < tableau.length; i++) {
            int nbPionRouge = 0;
            int nbPionNoir = 0;
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == pionRouge){
                    nbPionRouge++;
                }
                if (tableau[i][j] == pionNoir){
                    nbPionNoir++;
                }
            }
            scoreAvancementRouge += poidAvancement[7-i] * nbPionRouge;
            scoreAvancementNoir += poidAvancement[i] * nbPionNoir;
        }
    }

    /**
     * Vérifie si le jeu est terminer
     * 
     * @return Vrai si le jeu est terminé, Faux sinon.
     */
    public boolean inTerminalState() {

        //Vérifie si un joueur à attein la base adverse
        for(int col = 0; col < tableau[0].length; col++){
            if (tableau[0][col] == pionRouge || tableau[7][col] == pionNoir) {
                return true;
            }
        }

        int nbPionRouge = 0;
        int nbPionNoir = 0;
        for (int row = 0; row < tableau.length; row++) {
            for (int col = 0; col < tableau[row].length; col++) {
                if (tableau[row][col] == pionRouge) nbPionRouge++;
                if (tableau[row][col] == pionNoir) nbPionNoir++;
            }
        }

        // Vérifier si un joueur n'a plus de pion
        return nbPionRouge == 0 || nbPionNoir == 0;
    }

    /**
     * Calcule le nombre de pièces rouges et noires sur le plateau de jeu.
     */
    private void evaluateAvantageMaterial() {
        pionRougeVivant = 0;
        pionNoirVivant = 0;

        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == 4)
                    pionRougeVivant++;

                if (tableau[i][j] == 2)
                    pionNoirVivant++;
            }
        }
    }

    /**
     * Calcule le nombre de pièces rouges et noires protégées et ceux en danger sur le plateau de jeu.
     */
    private void evaluatePionSafe() {
        pionRougeSafe = pionRougeVivant;
        pionNoirSafe = pionNoirVivant;
        pionRougeInDanger = 0;
        pionNoirInDanger = 0;
        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == pionNoir && i+1 <= rowLenght-1 && ((j+1 <= colLenght-1  && tableau[i+1][j+1] == pionRouge) || (j-1 >= 0 && tableau[i+1][j-1] == pionRouge))){
                    if(i-1 >= 0 && ((j+1 <= colLenght-1 && tableau[i-1][j+1] == pionNoir)  || (j-1 >= 0 && tableau[i-1][j-1] == pionNoir))){
                        pionRougeInDanger++;
                    }else{
                        pionNoirSafe--;
                    }
                }

                if (tableau[i][j] == pionRouge && i-1 >= 0 && ((j+1 <= colLenght-1 && tableau[i-1][j+1] == pionNoir)  || (j-1 >= 0 && tableau[i-1][j-1] == pionNoir))){
                    if(i+1 <= rowLenght-1 && ((j+1 <= colLenght-1  && tableau[i+1][j+1] == pionRouge) || (j-1 >= 0 && tableau[i+1][j-1] == pionRouge))){
                        pionNoirInDanger++;
                    }else{
                        pionRougeSafe--;
                    }
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

        int[] array = new int[] {0,1,6,7};
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if (tableau[i][j] == 4 && (i == 0 || i == 1)) gagneProxiRouge++;
                if (tableau[i][j] == 2 && (i == 6 || i == 7)) gagneProxiNoir++;
            }
        }
    }
}