package Breakthrough;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**********************************************************************************************
 * Title: MinMax
 * Description: The MinMax class implements the Minimax algorithm with
 * Alpha-Beta pruning for selecting the best move in the Breakthrough game.
 *
 * @author Gilles Foster Djoko Kamgaing , Hassan Khanafer, Andy Mahautiere
 ***********************************************************************************************/
public class MinMax {

    private static final int depthMaximum = 5;

    /**
     * Applique l'algorithme Minimax avec Ã©lagage Alpha-Beta pour Ã©valuer et
     * sÃ©lectionner le meilleur coup.
     *
     * @param tableau            L'Ã©tat actuel du plateau de jeu.
     * @param depth              La profondeur actuelle de la recherche Minimax.
     * @param alpha              La valeur alpha pour l'Ã©lagage Alpha-BÃªta.
     * @param beta               La valeur bÃªta pour l'Ã©lagage Alpha-BÃªta.
     * @param isMaximizingPlayer Indique si le joueur actuel est le joueur qui maximise ou non.
     * @param tempsFin           L'horodatage de l'Ã©chÃ©ance pour arrÃªter la recherche.
     * @return                   Le score d'Ã©valuation du mouvement.
     */
    public static double AlphaBeta(Tableau tableau, int depth, double alpha, double beta, boolean isMaximizingPlayer,
                                   long tempsFin) {

        if (System.currentTimeMillis() >= tempsFin) {
            return 0;
        }

        Evaluation fonctionEvaluation = new Evaluation(tableau);

        int joueurActuel;
        if (Client.joueur == 1) {
            if (isMaximizingPlayer)
                joueurActuel = 1;
            else
                joueurActuel = 2;
        } else {
            if (isMaximizingPlayer)
                joueurActuel = 2;
            else
                joueurActuel = 1;
        }

        if (depth == depthMaximum || fonctionEvaluation.inTerminalState()) {
            return fonctionEvaluation.evaluate(joueurActuel);
        }

        List<Mouvement> mouvementLegal = GenerateurMouvement.generateMouvementLegal(tableau.getTableau(), joueurActuel == 1);

        if (isMaximizingPlayer) {

            double meilleurScore = Double.NEGATIVE_INFINITY;

            for (Mouvement mouvement : mouvementLegal) {

                Tableau newTableau = tableau.applyMove(mouvement);

                double scoreActuel = AlphaBeta(newTableau, depth + 1, alpha, beta, false, tempsFin);

                meilleurScore = Math.max(meilleurScore, scoreActuel);

                alpha = Math.max(alpha, scoreActuel);

                if (beta <= alpha) {
                    break;
                }
            }

            return meilleurScore;

        } else {

            double meilleurScore = Double.POSITIVE_INFINITY;

            for (Mouvement mouvement : mouvementLegal) {

                Tableau newTableau = tableau.applyMove(mouvement);

                double scoreActuel = AlphaBeta(newTableau, depth + 1, alpha, beta, true, tempsFin);

                meilleurScore = Math.min(meilleurScore, scoreActuel);

                beta = Math.min(beta, scoreActuel);

                if (beta <= alpha) {
                    break;
                }
            }

            return meilleurScore;
        }
    }

    /**
     * SÃ©lectionne le meilleur coup pour un Ã©tat de plateau de jeu et un joueur
     * donnÃ©s en utilisant l'algorithme Minimax avec Ã©lagage Alpha-Beta et
     * approfondissement itÃ©ratif.
     *
     * @param tableau     L'Ã©tat actuel du plateau de jeu.
     * @param isRedPlayer Indique si le joueur actuel est le joueur rouge ou non.
     * @return            Le meilleur coup Ã  jouer.
     */
    public static Mouvement getBestMove(Tableau tableau, boolean isRedPlayer) {

        int depthInitiale = 1;
        long tempsLimite = 5000;

        long tempsRestant = tempsLimite - 500;
        int depthMaximum = depthInitiale;
        while (System.currentTimeMillis() + (depthMaximum - depthInitiale) * 100 < tempsRestant) {
            depthMaximum++;
        }

        Mouvement bestMove = null;
        for (int depth = depthInitiale; depth <= depthMaximum; depth++) {
            long tempsDebut = System.currentTimeMillis();
            long tempsFin = tempsDebut + tempsRestant;

            List<Mouvement> mouvementLegal = GenerateurMouvement.generateMouvementLegal(tableau.getTableau(),
                    isRedPlayer);
            List<Mouvement> meilleurMouvement = new ArrayList<>();
            double meilleurScore = Double.NEGATIVE_INFINITY;

            for (Mouvement mouvement : mouvementLegal) {
                Tableau newTableau = tableau.applyMove(mouvement);
                double scoreActuel = AlphaBeta(newTableau, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false, tempsFin);

                if (scoreActuel > meilleurScore) {
                    meilleurMouvement.clear();
                    meilleurMouvement.add(mouvement);
                    meilleurScore = scoreActuel;
                } else if (scoreActuel == meilleurScore) {
                    meilleurMouvement.add(mouvement);
                }

                if (System.currentTimeMillis() >= tempsFin) {
                    break;
                }
            }

            if (!meilleurMouvement.isEmpty()) {
                bestMove = meilleurMouvement.get(new Random().nextInt(meilleurMouvement.size()));
            } else {
                bestMove = mouvementLegal.get(new Random().nextInt(mouvementLegal.size()));
            }

            if (depth == depthMaximum) {
                break;
            }
        }

        return bestMove;
    }
}