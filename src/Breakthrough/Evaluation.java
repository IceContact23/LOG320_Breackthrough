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

    Tableau tableau;
    private int nbPionRouge;
    private int nbPionNoir;
    private int safetyPionRouge;
    private int safetyPionNoir;
    private int pionRougeProteger;
    private int pionNoirProteger;
    private int pionRougeBlock;
    private int pionNoirBlock;
    private int gagneProxiRouge;
    private int gagneProxiNoir;
    private int controleCentreRouge;
    private int controleCentreNoir;

    /**
     * Construit un objet Evaluation avec le tableau donné.
     * 
     * @param tableau le board de jeu
     */
    public Evaluation(Tableau tableau) {
        this.tableau = tableau;
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
        evaluatePusherSafety();
        evaluateBlock();
        evaluateGagneProxi();
        evaluateControleCentre();
        evaluateProtection();

        // Facteurs de poids d'évaluation
        int poidProtection = 20;
        int poidPionPremiereLigneProteger = 35;
        int poidBlock = 35;
        int poidPionPremiereLigneVivant = 500;
        int poidPionDeuxiemeLigneVivant = 500;

        // Calculer le score d'évaluation
        if (joueur == 1) {
            evaluation = poidPionPremiereLigneVivant * (nbPionRouge - nbPionNoir)
                    + poidPionDeuxiemeLigneVivant * (nbPionRouge - nbPionNoir)
                    + poidProtection * pionRougeProteger + poidPionPremiereLigneProteger * safetyPionRouge
                    + poidBlock * pionRougeBlock - poidBlock * pionNoirBlock
                    + gagneProxiRouge - gagneProxiNoir + controleCentreRouge;
        } else {
            evaluation = poidPionPremiereLigneVivant * (nbPionNoir - nbPionRouge)
                    + poidPionDeuxiemeLigneVivant * (nbPionNoir - nbPionRouge)
                    + poidProtection * pionNoirProteger + poidPionPremiereLigneProteger * safetyPionNoir
                    + poidBlock * pionNoirBlock - poidProtection * pionRougeBlock
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
        for (int row = 0; row < tableau.getTableau().length; row++) {
            for (int col = 0; col < tableau.getTableau()[row].length; col++) {

                if (tableau.getTableau()[row][col] == 4)
                    pionRougePremiereLigne++;

                if (tableau.getTableau()[row][col] == 2)
                    pionNoirPremiereLigne++;

                // Vérifiez si les pièces du joueur ont atteint la première rangée de
                // l'adversaire
                if (joueur == 1) {
                    if (tableau.getTableau()[row][col] == 4 || tableau.getTableau()[row][col] == 4) {
                        if (col == 0) {
                            return true;
                        }
                    }
                } else {
                    if (tableau.getTableau()[row][col] == 2 || tableau.getTableau()[row][col] == 2) {
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

        for (int i = 0; i < tableau.getTableau().length; i++) {
            for (int j = 0; j < tableau.getTableau()[i].length; j++) {
                if (tableau.getTableau()[i][j] == 4)
                    nbPionRouge++;

                if (tableau.getTableau()[i][j] == 2)
                    nbPionNoir++;
            }
        }
    }

    /**
     * Calcule le nombre de pièces rouges et noires protégées sur le plateau de jeu.
     */
    private void evaluateProtection() {
        pionRougeProteger = 0;
        pionNoirProteger = 0;

        for (int i = 0; i < tableau.getTableau().length; i++) {
            for (int j = 0; j < tableau.getTableau()[i].length; j++) {
                if (tableau.getTableau()[i][j] == 4 && (i == 0 || tableau.getTableau()[i - 1][j] == 4))
                    pionRougeProteger++;

                if (tableau.getTableau()[i][j] == 2
                        && (i == tableau.getTableau().length - 1 || tableau.getTableau()[i + 1][j] == 2))
                    pionNoirProteger++;
            }
        }
    }

    /**
     * Calcule la sécurité des poussoirs rouges et noirs sur le plateau de jeu.
     */
    private void evaluatePusherSafety() {
        safetyPionRouge = 0;
        safetyPionNoir = 0;

        for (int i = 0; i < tableau.getTableau().length; i++) {
            for (int j = 0; j < tableau.getTableau()[i].length; j++) {
                if (tableau.getTableau()[i][j] == 3 && (i == 0 || tableau.getTableau()[i - 1][j] != 4))
                    safetyPionRouge++;

                if (tableau.getTableau()[i][j] == 1
                        && (i == tableau.getTableau().length - 1 || tableau.getTableau()[i + 1][j] != 2))
                    safetyPionNoir++;
            }
        }
    }

    /**
     * Calcule le nombre de pièces rouges et noires bloquées sur le plateau de jeu.
     */
    private void evaluateBlock() {
        pionRougeBlock = 0;
        pionNoirBlock = 0;

        for (int i = 0; i < tableau.getTableau().length; i++) {
            for (int j = 0; j < tableau.getTableau()[i].length; j++) {
                if (tableau.getTableau()[i][j] == 4) {
                    if (i != 0 && tableau.getTableau()[i - 1][j] != 0 && tableau.getTableau()[i - 1][j] != 4)
                        pionRougeBlock++;
                }

                if (tableau.getTableau()[i][j] == 2) {
                    if (i != tableau.getTableau().length - 1 && tableau.getTableau()[i + 1][j] != 0
                            && tableau.getTableau()[i + 1][j] != 2)
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

        for (int i = 0; i < tableau.getTableau().length; i++) {
            for (int j = 0; j < tableau.getTableau()[i].length; j++) {
                if (tableau.getTableau()[i][j] == 4 && (i == 0 || i == 1))
                    gagneProxiRouge++;

                if (tableau.getTableau()[i][j] == 2
                        && (i == tableau.getTableau().length - 1 || i == tableau.getTableau().length - 2))
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
            for (int j = 0; j < tableau.getTableau()[i].length; j++) {
                if (tableau.getTableau()[i][j] == 4)
                    controleCentreRouge++;

                if (tableau.getTableau()[i][j] == 2)
                    controleCentreNoir++;
            }
        }
    }
}