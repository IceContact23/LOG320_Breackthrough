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
    private int pionRougeSafe;
    private int pionNoirSafe;
    private int pionRougeInDanger;
    private int pionNoirInDanger;
    private int scoreAvancementRouge;
    private int scoreAvancementNoir;
    private int scoreGroupePionRouge = 0;
    private int scoreGroupePionNoir = 0;
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
        evaluatePionSafe();
        evaluateAvancementPion();
        evaluateGroupementPion(4); //Pion rouge
        evaluateGroupementPion(2); //Pion noir

        // Facteurs de poids d'évaluation
        double poidAvancement = 1;
        int poidGroupement = 1;
        int poidPionSafe = 100;
        int poidPionInDanger = 150;
        int poidPionVivant = 200;

        // Calculer le score d'évaluation
        if (joueur == 1) { //Pion rouge
            evaluation =
                    poidPionVivant * (pionRougeVivant - pionNoirVivant)
                        + poidAvancement * (scoreAvancementRouge - scoreAvancementNoir)
                        + poidGroupement * (scoreGroupePionRouge - scoreGroupePionNoir)
                        + poidPionSafe * pionRougeSafe
                        + poidPionInDanger * (pionNoirInDanger - pionRougeInDanger);
        } else { //Pion noir
            evaluation =
                    poidPionVivant * (pionNoirVivant - pionRougeVivant)
                            + poidAvancement * (scoreAvancementNoir - scoreAvancementRouge)
                            + poidGroupement * (scoreGroupePionNoir - scoreGroupePionRouge)
                            + poidPionSafe * pionNoirSafe
                            + poidPionInDanger * (pionRougeInDanger - pionNoirInDanger);;
        }
        return (joueur == Client.joueur) ? evaluation : -(evaluation);
    }

    private void evaluateGroupementPion(int couleur) {
        int[] poidScoreGroupe = {0, 0, 25, 25, 35, 35, 50, 50};

         boolean[][] caseToSkip = new boolean[rowLenght][colLenght];
        int[][] directions = {
                {0, 1},
                {1, 0},
                {1, 1}
        };

        for (int i = 0; i < tableau.length; i++) {
            for (int j = 0; j < tableau[i].length; j++) {
                if(caseToSkip[i][j]) continue;
                else if (tableau[i][j] == couleur){
                    int nbPion = 0;
                    for(int[] direction : directions){
                        int x = direction[1] + j;
                        int y = direction[0] + i;
                        if(y >= 0 && y <= rowLenght-1 && x >= 0 && x <= colLenght-1){
                            caseToSkip[y][x] = true;
                            nbPion += tableau[y][x] == couleur ? 1 : 0;
                        }
                    }
                    if(nbPion > 1){
                        scoreGroupePionRouge += couleur == pionRouge ? poidScoreGroupe[7-i] * nbPion : 0;
                        scoreGroupePionNoir += couleur == pionNoir ? poidScoreGroupe[i] * nbPion : 0;
                    }
                }
            }
        }
    }

    private void evaluateAvancementPion() {
        int[] poidAvancement = new int[] {50, 0, 0, 50, 100, 500, 77777, 7777777};
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
}