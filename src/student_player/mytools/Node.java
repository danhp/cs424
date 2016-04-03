package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    public int numsOfVisits;
    public HusBoardState gameState;
    public double score;

    public Node parent;
    public HusMove parentMove;

    public List<Node> children;
    public List<HusMove> untriedMoves;

    public Node() {
        this.gameState = new HusBoardState();
        this.score = 0.0;
        this.parentMove = new HusMove(0, 0);
        this.children = new ArrayList<Node>();
    }

    public Node(HusBoardState state, int score, Node parent, HusMove parentMove) {
        this.numsOfVisits = 0;
        this.gameState = state;
        this.score = score;

        this.parent = parent;
        this.parentMove = parentMove;

        this.children = new ArrayList<Node>();
        this.untriedMoves = state.getLegalMoves();
    }
}
