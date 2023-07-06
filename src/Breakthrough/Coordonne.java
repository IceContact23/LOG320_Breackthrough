package Breakthrough;

/**********************************************************************************************
 * Title: Coordonne
 * Description: La classe Coordonne représente une coordonnée sur le plateau de
 * jeu. Il peut être créé avec des indices de ligne et de colonne ou des
 * coordonnées basées sur des caractères.
 *
 * @author Gilles Foster Djoko Kamgaing , Hassan Khanafer, Andy Mahautiere
 ***********************************************************************************************/

public class Coordonne {
    private final int x;
    private final int y;

    /**
     * Construit un nouvel objet `Coordonne` avec les indices de ligne et de colonne
     * donnés.
     *
     * @param x L'index de ligne.
     * @param y L'indice de colonne.
     */
    public Coordonne(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Construit un nouvel objet `Coordonne` avec des coordonnées basées sur des
     * caractères.
     *
     * @param x Le caractère de ligne (A à H).
     * @param y Le caractère de la colonne (1 à 8).
     */
    public Coordonne(char x, char y) {
        this.x = translateCharToX(x);
        this.y = 8 - Character.getNumericValue(y);
    }

    /**
     * Convertit la coordonnée en une représentation sous forme de chaîne lisible
     * par l'homme.
     *
     * @return La coordonnée sous forme de chaîne (par exemple, "A1", "B2", etc.).
     */
    @Override
    public String toString() {
        return translateXToChar() + invertY();
    }

    /**
     * Traduit l'index de colonne dans le caractère correspondant (A à H).
     *
     * @return La représentation en caractères de l'index de colonne.
     */
    public String translateXToChar() {
        return switch (this.x) {
            case 0 -> "A";
            case 1 -> "B";
            case 2 -> "C";
            case 3 -> "D";
            case 4 -> "E";
            case 5 -> "F";
            case 6 -> "G";
            case 7 -> "H";
            default -> "";
        };
    }

    /**
     * Traduit la coordonnée de ligne basée sur des caractères en la valeur entière
     * correspondante.
     *
     * @param x Le caractère de ligne (A à H).
     * @return L'index de ligne correspondant.
     */
    public int translateCharToX(char x) {
        return switch (x) {
            case 'A' -> 0;
            case 'B' -> 1;
            case 'C' -> 2;
            case 'D' -> 3;
            case 'E' -> 4;
            case 'F' -> 5;
            case 'G' -> 6;
            case 'H' -> 7;
            default -> -1;
        };
    }

    /**
     * Dans un tableau int 2D, la coordonnée (0,0) est dans le coin supérieur
     * gauche, mais dans le jeu, la coordonnée (0,0) est dans le coin inférieur
     * gauche. Cette fonction inverse la valeur Y pour correspondre au système de
     * coordonnées du jeu.
     *
     * @return La valeur Y inversée.
     */
    public int invertY() {
        return 8 - this.y;
    }

    /**
     * Renvoie l'index de ligne de la coordonnée.
     *
     * @return L'index de ligne.
     */
    public int getX() {
        return x;
    }

    /**
     * Renvoie l'index de colonne de la coordonnée.
     *
     * @return L'indice de colonne.
     */
    public int getY() {
        return y;
    }
}
