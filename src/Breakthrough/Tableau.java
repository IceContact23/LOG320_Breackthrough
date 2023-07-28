package Breakthrough;

/**********************************************************************************************
 * Title: Tableau
 * Description: La classe Tableau représente le tableau de jeu dans le jeu
 * Breakthrough.
 * Breakthrough.
 *
 * @author Gilles Foster Djoko Kamgaing , Hassan Khanafer, Andy Mahautiere
 ***********************************************************************************************/

public class Tableau {
    private int[][] tableau;

    /**
     * Construit un objet `Tableau` avec le tableau de jeu spécifié.
     *
     * @param tableau Le tableau de jeu.
     */
    public Tableau(int[][] tableau) {
        this.tableau = tableau;
    }

    /**
     * Enregistre le mouvement de l'adversaire sur le tableau de jeu.
     *
     * @param move Le mouvement de l'adversaire au format "from-to" (ex: "a2-a3").
     */
    public void trackOpponentMove(String move) {
        Coordonne from = new Coordonne(move.charAt(1), move.charAt(2));
        Coordonne to = new Coordonne(move.charAt(6), move.charAt(7));

        int pion = tableau[from.getY()][from.getX()];
        tableau[from.getY()][from.getX()] = 0;
        tableau[to.getY()][to.getX()] = pion;
    }

    /**
     * Retourne le tableau de jeu.
     *
     * @return Le tableau de jeu.
     */
    public int[][] getTableau() {
        return tableau;
    }

    /**
     * Définit le tableau de jeu.
     *
     * @param tableau Le tableau de jeu à définir.
     */
    public void setTableau(int[][] tableau) {
        this.tableau = tableau;
    }

    /**
     * Applique le mouvement spécifié sur le tableau de jeu et retourne un nouveau
     * tableau avec le mouvement appliqué.
     *
     * @param move Le mouvement à appliquer.
     * @return Un nouveau tableau avec le mouvement appliqué.
     */
    public Tableau applyMove(Mouvement move) {
        int[][] newTableau = copyTableau();

        Coordonne from = move.getFrom();
        Coordonne to = move.getTo();
        int pion = newTableau[from.getY()][from.getX()];
        newTableau[from.getY()][from.getX()] = 0;
        newTableau[to.getY()][to.getX()] = pion;

        return new Tableau(newTableau);
    }

    private int[][] copyTableau() {
        int[][] newTableau = new int[tableau.length][];

        // Crée une copie profonde du tableau de jeu actuel
        for (int i = 0; i < tableau.length; i++) {
            newTableau[i] = tableau[i].clone();
        }

        return newTableau;
    }

    /**
     * Vérifie si l'objet spécifié est égal à ce tableau de jeu.
     *
     * @param obj L'objet à comparer.
     * @return `true` si l'objet est égal à ce tableau de jeu, sinon `false`.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tableau other = (Tableau) obj;
        return java.util.Arrays.deepEquals(tableau, other.tableau);
    }

    /**
     * Retourne le code de hachage pour ce tableau de jeu.
     *
     * @return Le code de hachage pour ce tableau de jeu.
     */
    @Override
    public int hashCode() {
        return java.util.Arrays.deepHashCode(tableau);
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du tableau de
     * jeu.
     *
     * @return La représentation du tableau de jeu.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : tableau) {
            for (int cell : row) {
                sb.append(cell).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
