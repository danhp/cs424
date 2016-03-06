package student_player;

import hus.HusBoardState;
import hus.HusPlayer;
import hus.HusMove;

import student_player.mytools.MyTools;

/** A Hus player submitted by a student. */
public class StudentPlayer extends HusPlayer {

    /** You must modify this constructor to return your student number.
     * This is important, because this is what the code that runs the
     * competition uses to associate you with your agent.
     * The constructor should do nothing else. */
    public StudentPlayer() { super("260526252"); }

    /** This is the primary method that you need to implement.
     * The ``board_state`` object contains the current state of the game,
     * which your agent can use to make decisions. See the class hus.RandomHusPlayer
     * for another example agent. */
    public HusMove chooseMove(HusBoardState gameState) {
        HusMove move;

        /* Opening move behaviour */
        if (gameState.getTurnNumber() == 0) {
            if (player_id == gameState.firstPlayer()) {
                // Randomized opening from a set of moves.
                move = MyTools.getOpener(gameState, player_id, opponent_id);

            } else {
                // Play the best counter.
                move = MyTools.getCounter(gameState, player_id, opponent_id);
            }

        } else {
            // Normal in-game behaviour
            move = MyTools.getBestMove(gameState, player_id);
        }

        return move;
    }
}
