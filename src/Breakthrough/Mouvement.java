package Breakthrough;

/**********************************************************************************************
 * Title: Mouvement
 * Description: La classe Mouvement représente un mouvement possible dans le jeu
 * Breakthrough.
 *
 * @author Gilles Foster Djoko Kamgaing , Hassan Khanafer, Andy Mahautiere
 ***********************************************************************************************/

public class Mouvement {

    private int typePion;
    private double evaluation;
    private Coordonne to;
    private Coordonne from;

    /**
     * Construit un objet `Mouvement` avec les coordonnées de départ et d'arrivée.
     *
     * @param typePion Le type de pion associé au mouvement.
     * @param from     Les coordonnées de départ du mouvement.
     * @param to       Les coordonnées d'arrivée du mouvement.
     */
    public Mouvement(int typePion, Coordonne from, Coordonne to) {
        this.typePion = typePion;
        this.from = from;
        this.to = to;
    }

    /**
     * Construit un objet `Mouvement` avec les coordonnées de départ, d'arrivée et
     * une évaluation du plateau.
     *
     * @param typePion      Le type de pion associé au mouvement.
     * @param from          Les coordonnées de départ du mouvement.
     * @param to            Les coordonnées d'arrivée du mouvement.
     * @param evaluateBoard L'évaluation du plateau associée au mouvement.
     */
    public Mouvement(int typePion, Coordonne from, Coordonne to, double evaluateBoard) {
        this.typePion = typePion;
        this.from = from;
        this.to = to;
        this.evaluation = evaluateBoard;
    }

    /**
     * Retourne les coordonnées d'arrivée du mouvement.
     *
     * @return Les coordonnées d'arrivée du mouvement.
     */
    public Coordonne getTo() {
        return to;
    }

    /**
     * Retourne les coordonnées de départ du mouvement.
     *
     * @return Les coordonnées de départ du mouvement.
     */
    public Coordonne getFrom() {
        return from;
    }

    /**
     * Retourne l'évaluation du plateau associée au mouvement.
     *
     * @return L'évaluation du plateau associée au mouvement.
     */
    public double getEvaluation() {
        return evaluation;
    }

    /**
     * Définit l'évaluation du plateau associée au mouvement.
     *
     * @param score L'évaluation du plateau.
     */
    public void setEvaluation(double score) {
        evaluation = score;
    }

    /**
     * Retourne le type de pion associé au mouvement.
     *
     * @return Le type de pion associé au mouvement.
     */
    public int getTypePion() {
        return typePion;
    }

    /**
     * Retourne une représentation sous forme de chaîne de caractères du mouvement.
     *
     * @return La représentation du mouvement.
     */
    @Override
    public String toString() {
        return from.toString() + "-" + to.toString();
    }
}
