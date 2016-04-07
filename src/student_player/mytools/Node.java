package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Node {
    public int numsOfVisits;
    public HusBoardState gameState;
    public double score;

    public Node parent;
    public HusMove parentMove;

    public List<Node> children;
    public PriorityQueue<HusMove> untriedMoves;

    public Node(final HusBoardState state, Node parent, HusMove parentMove) {
        this.numsOfVisits = 0;
        this.score = 0;

        this.gameState = state;
        this.parent = parent;
        this.parentMove = parentMove;

        this.children = new ArrayList<Node>();

        // Sort, so that we only expand the best subset.
        untriedMoves = new PriorityQueue<HusMove>(13, new Comparator<HusMove>() {
            @Override
            public int compare(HusMove m1, HusMove m2) {
                int score1 = MyTools.countSeeds(MyTools.doMove(gameState, m1), state.getTurnPlayer());
                int score2 = MyTools.countSeeds(MyTools.doMove(gameState, m2), state.getTurnPlayer());

                return score2 - score1;
            }
        });

        for (HusMove m : state.getLegalMoves()) {
            untriedMoves.add(m);
        }
    }
}
