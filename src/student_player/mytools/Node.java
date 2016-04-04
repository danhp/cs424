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

    public Node(HusBoardState state, Node parent, HusMove parentMove) {
        this.numsOfVisits = 0;
        this.score = 0;

        this.gameState = state;
        this.parent = parent;
        this.parentMove = parentMove;

        this.children = new ArrayList<Node>();
        this.untriedMoves = state.getLegalMoves();
    }
}
