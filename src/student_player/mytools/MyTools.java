package student_player.mytools;

import java.util.ArrayList;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {
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
        return MCTS.getBestMoveMCTS(gameState, player_id);
//        return AlphaBeta.getBestMoveAlphaBeta(gameState, player_id);
    }
}
