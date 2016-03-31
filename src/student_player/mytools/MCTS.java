package student_player.mytools;

import hus.HusBoard;
import hus.HusBoardState;
import hus.HusMove;

import java.util.ArrayList;
import java.util.Random;

public class MCTS {
    private static Random rand = new Random();
    private static int maximizer;
    private static Node root;

    public static HusMove getBestMoveMCTS(HusBoardState gameState, int player_id) {
        maximizer = player_id;

        root = new Node(gameState, Integer.MIN_VALUE, null, null);

        long timeRemaining = 1800000000;
        long delta = 20;

        while (delta * 4 < timeRemaining) {
            long startTime = System.nanoTime();

            // Do the work
            Node toSimulateFrom = treePolicy(root);
            int score = defaultPolicy(toSimulateFrom.gameState);
            backup(toSimulateFrom, score);

            delta = System.nanoTime() - startTime;
            timeRemaining -= delta;
        }

        return bestChild(root, 0).parentMove;
    }

    private static Node treePolicy(Node node) {
        while (!node.gameState.gameOver()) {
            if (node.children.size() < node.gameState.getLegalMoves().size()) {
                return expand(node);
            } else {
                node = bestChild(node, 1 / Math.sqrt(2));
            }
        }

        return node;
    }

    private static Node expand(Node node) {
        // Get the best untried move
        int r = rand.nextInt(node.untriedMoves.size());
        HusMove move = node.untriedMoves.get(r);
        node.untriedMoves.remove(r);

        // Create the new child state
        HusBoardState newState = (HusBoardState) node.gameState.clone();
        newState.move(move);

        // Create the node and add it to the tree
        Node newNode = new Node(newState, Integer.MIN_VALUE, node, move);
        node.children.add(newNode);

        return newNode;
    }

    private static Node bestChild(Node node, double explorationConstant) {
        Node bestChild = node.children.get(0);
        double bestValueSoFar = Double.MIN_VALUE;

        for (Node child : node.children) {
            double uct = child.score / child.numsOfVisits;
            uct += explorationConstant * Math.sqrt(2 * Math.log(node.numsOfVisits) / child.numsOfVisits);

            if (uct > bestValueSoFar) {
                bestChild = child;
                bestValueSoFar = uct;
            }
        }

        return bestChild;
    }

    private static int defaultPolicy(HusBoardState gameState) {
        HusBoardState state = (HusBoardState) gameState.clone();

        // TODO: Run with a depth 2 Alpha-beta.
        // Continuously get a random move until gameOver.
        while (!state.gameOver()) {
            ArrayList<HusMove> moves = state.getLegalMoves();
            HusMove move = moves.get(rand.nextInt(moves.size()));
            state.move(move);
        }

        return (state.getWinner() == maximizer) ? 1 : 0;
    }

    private static void backup(Node node, int delta) {
        while (node != null) {
            node.numsOfVisits++;
            node.score += delta;
            delta = -delta;
            node = node.parent;
        }
    }
}
