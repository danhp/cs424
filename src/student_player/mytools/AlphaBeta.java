package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;
import java.util.List;

public class AlphaBeta {
    private static int maximizer;

    // Assumes will only be called when the game is not over
    public static HusMove getBestMoveAlphaBeta(final HusBoardState gameState, int player_id, int depth) {
        // Set the player we want the move fore
        maximizer = player_id;

        // Get the legal moves for the current game state
        List<HusMove> moveList = gameState.getLegalMoves();
        moveList = MyTools.sortMoves(moveList, gameState, maximizer);

        HusMove bestMove = moveList.get(0);
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Find the best move
        for (HusMove m : moveList) {
            // See it played out
            HusBoardState cloned_gameState = (HusBoardState) gameState.clone();
            cloned_gameState.move(m);

            int outcome = alphaBeta(cloned_gameState, depth, alpha, beta, false);

            if (outcome > alpha) {
                alpha = outcome;
                bestMove = m;
            }
        }

        return bestMove;
    }

    public static int alphaBeta(HusBoardState gameState, int depth, int alpha, int beta, boolean isMaximize) {
        // Termination check
        if (depth == 0 || gameState.gameOver()) {
            return score(gameState);
        }

        // Get the legal moves for the current board state.
        ArrayList<HusMove> moveList = gameState.getLegalMoves();
        int value;

        if (isMaximize) {
            value = Integer.MIN_VALUE;

            // Search the children
            for (HusMove m : moveList) {
                // See it played out
                HusBoardState cloned_gameState = (HusBoardState) gameState.clone();
                cloned_gameState.move(m);

                value = Math.max(value, alphaBeta(cloned_gameState, depth - 1, alpha, beta, !isMaximize));
                alpha = Math.max(alpha, value);

                // Prune
                if (value >= beta) { break; }
            }

        } else {
            value = Integer.MAX_VALUE;

            // Search the children
            for (HusMove m : moveList) {
                // See it played out
                HusBoardState cloned_gameState = (HusBoardState) gameState.clone();
                cloned_gameState.move(m);

                value = Math.min(value, alphaBeta(cloned_gameState, depth - 1, alpha, beta, !isMaximize));
                beta = Math.min(beta, value);

                // Prune
                if (value <= alpha) { break; }
            }
        }

        return value;
    }


    private static int score(HusBoardState gameState) {
        // Draws happen so rarely that we group them with losses.
        if (gameState.gameOver()) {
            int winner = gameState.getWinner();

            if (winner == maximizer) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }

        return MyTools.countSeeds(gameState, maximizer);
    }
}
