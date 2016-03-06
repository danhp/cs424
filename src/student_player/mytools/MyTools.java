package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;

public class MyTools {
    private static final int DEPTH = 6;

    public static HusMove getOpener(HusBoardState gameState, int player_id, int oppnonent_id) {
        // Get the legal moves for the current board state.
        ArrayList<HusMove> moves = gameState.getLegalMoves();
        HusMove move = moves.get(0);

        return move;
    }

    // Try to determine the opener with the given state.
    public static HusMove getCounter(HusBoardState gameState, int player_id, int opponent_id) {
        // Get the legal moves for the current board state.
        ArrayList<HusMove> moves = gameState.getLegalMoves();
        HusMove move = moves.get(0);

        return move;
    }

    public static HusMove getBestMove(HusBoardState gameState, int player_id) {
        return getBestMoveAlphaBeta(gameState, player_id);
    }

    // Assumes will only be called when the game is not over
    public static HusMove getBestMoveAlphaBeta(HusBoardState gameState, int player_id) {
        // Get the legal moves for the current game state
        ArrayList<HusMove> moveList = gameState.getLegalMoves();

        HusMove bestMove = moveList.get(0);
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        // Find the best move
        for (HusMove m : moveList) {
            // See it played out
            HusBoardState cloned_gameState = (HusBoardState) gameState.clone();
            cloned_gameState.move(m);

            int outcome = alphaBeta(cloned_gameState, DEPTH, alpha, beta, false, player_id);

            if (outcome > alpha) {
                alpha = outcome;
                bestMove = m;
            }
        }

        return bestMove;
    }

    private static int alphaBeta(HusBoardState gameState, int depth, int alpha, int beta, boolean isMaximize, int player_id) {
        // Termination check
        if (depth == 0 || gameState.gameOver()) {
            return score(gameState, player_id);
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

                value = Math.max(value, alphaBeta(cloned_gameState, depth - 1, alpha, beta, !isMaximize, player_id));
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

                value = Math.min(value, alphaBeta(cloned_gameState, depth - 1, alpha, beta, !isMaximize, player_id));
                beta = Math.min(beta, value);

                // Prune
                if (value <= alpha) { break; }
            }
        }

        return value;
    }

    private static int score(HusBoardState gameState, int player_id) {

        // Draws happen so rarely that we group them with losses.
        if (gameState.gameOver()) {
            int winner = gameState.getWinner();

            if (winner == player_id) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }

        int[] my_pits = gameState.getPits()[player_id];

        int sum = 0;
        for (int i : my_pits) {
            sum += my_pits[i];
        }

        return sum;
    }
}
