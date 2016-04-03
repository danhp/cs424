package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.List;
import java.util.Random;

public class MCTS {
    private static Random rand = new Random();
    private static int maximizer;
    private static Node root;

    public static HusMove getBestMoveMCTS(HusBoardState gameState, int player_id) {
        maximizer = player_id;

        root = new Node(gameState, Integer.MIN_VALUE, null, null);

        long timeRemaining = 1850000000;
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
        // Start the root
        if (node.children.size() == 0) {
            return expand(node);
        }

        while (node.children.size() > 0) {
            if (node.children.size() < node.gameState.getLegalMoves().size()) {
                return expand(node);
            } else {
                node = bestChild(node, 1 / Math.sqrt(2));
            }
        }

        return node;
    }

    private static Node expand(Node node) {
        // Get a random untried move
        HusMove move = node.untriedMoves.get(0);
        node.untriedMoves.remove(0);

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

        // Random until GameOver
        while (!state.gameOver()) {
            List<HusMove> moves = state.getLegalMoves();
            HusMove move = moves.get(rand.nextInt(moves.size()));
            state.move(move);
        }
        return (state.getWinner() == maximizer) ? 1 : 0;

        // Random with a depth limit
//        final int DEPTH = 20;
//        int initSeed = MyTools.countSeeds(gameState, maximizer);
//        for (int i = 0; !state.gameOver() && i < DEPTH; i++) {
//            ArrayList<HusMove> moves = state.getLegalMoves();
//            HusMove move = moves.get(rand.nextInt(moves.size()));
//            state.move(move);
//        }
//
//        int finalSeed = MyTools.countSeeds(state, maximizer);
//        return (finalSeed >= initSeed) ? 1 : 0;


        // 2-ply alpha beta
//        int initSeed = MyTools.countSeeds(gameState, maximizer);
//        int finalSeed = AlphaBeta.alphaBeta(gameState, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, gameState.getTurnPlayer() == maximizer);
//        return (finalSeed > initSeed) ? 1 : 0;
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
