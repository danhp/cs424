package student_player.mytools;

import java.util.*;

import hus.HusBoardState;
import hus.HusMove;

public class MyTools {
    public static HusMove getBestMove(HusBoardState gameState, int player_id) {
        return MCTS.getBestMove(gameState, player_id);
//        return AlphaBeta.getBestMoveAlphaBeta(gameState, player_id);
    }

    public static HusBoardState doMove(HusBoardState gameState, HusMove move) {
        HusBoardState cloned = (HusBoardState) gameState.clone();
        cloned.move(move);
        return cloned;
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
                int score1 = countSeeds(doMove(gameState, m1), player_id);
                int score2 = countSeeds(doMove(gameState, m2), player_id);

                return score2 - score1;
            }
        });

        return moveList;
    }

    public static boolean stateEquals(HusBoardState state1, HusBoardState state2) {
        return Arrays.deepEquals(state1.getPits(), state2.getPits());
    }
}
