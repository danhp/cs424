package student_player.mytools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        return MCTS.getBestMove(gameState, player_id);
//        return AlphaBeta.getBestMoveAlphaBeta(gameState, player_id);
    }

    public static int countSeeds(HusBoardState gameState, int player_id) {
        int[] my_pits = gameState.getPits()[player_id];
        int sum = 0;
        for (int i : my_pits) {
            sum += my_pits[i];
        }

        return sum;
    }

    // Sorted with largest element first.
    public static List<HusMove> sortMoves(List<HusMove> moveList, final HusBoardState gameState, final int player_id) {
        Collections.sort(moveList, new Comparator<HusMove>() {
            @Override
            public int compare(HusMove m1, HusMove m2) {
                HusBoardState s1 = (HusBoardState) gameState.clone();
                s1.move(m1);
                int score1 = countSeeds(s1, player_id);

                HusBoardState s2 = (HusBoardState) gameState.clone();
                s2.move(m2);
                int score2 = countSeeds(s2, player_id);

                return (score1 < score2) ? 1 : (score1 == score2 ) ? 0 : -1;
            }
        });

        return moveList;
    }
}
