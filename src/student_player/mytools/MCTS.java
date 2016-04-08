package student_player.mytools;

import hus.HusBoardState;
import hus.HusMove;

import java.util.List;
import java.util.Random;

public class MCTS {
    private static Random rand = new Random();
    private static int maximizer;
    private static Node root;

    public static HusMove getBestMove(HusBoardState gameState, int player_id) {
        maximizer = player_id;

        long delta = 20;
        double timeRemaining = 1.85e9;
        long startTime = System.nanoTime();

        if (gameState.getTurnNumber() == 0) {
            timeRemaining = 28e9;
            root = new Node(gameState, null, null);
        } else {
            // Attempt to save the root.
            root = moveRoot(root, gameState);

            delta = System.nanoTime() - startTime;
            timeRemaining -= delta;
        }

        while (delta * 4 < timeRemaining) {
            startTime = System.nanoTime();

            Node toSimulateFrom = treePolicy(root);
            int score = defaultPolicy(toSimulateFrom.gameState);
            backup(toSimulateFrom, score);

            delta = System.nanoTime() - startTime;
            timeRemaining -= delta;
        }

        Node bestChild = bestChild(root, 0);
        HusMove bestMove = bestChild.parentMove;
        root = bestChild;
        root.parent = null;
        root.parentMove = null;
        return bestMove;
    }

    private static Node treePolicy(Node node) {
        while (!node.gameState.gameOver()) {
            if (node.untriedMoves.peek() != null) {
                return expand(node);
            } else {
                // Exploration constant ~= 1 / root(2)
                node = bestChild(node, 0.707);
            }
        }

        return node;
    }

    private static Node expand(Node node) {
        // Get an untried move.
        HusMove move = node.untriedMoves.poll();

        // Create the node and add it to the tree
        Node newNode = new Node(MyTools.doMove(node.gameState, move), node, move);
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
//        final int DEPTH = 30;
//        int initSeed = MyTools.countSeeds(gameState, maximizer);
//        for (int i = 0; !state.gameOver() && i < DEPTH; i++) {
//            List<HusMove> moves = state.getLegalMoves();
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

    private static Node moveRoot(Node root, HusBoardState gameState) {
        for (Node n : root.children)  {
            if (MyTools.stateEquals(n.gameState, gameState)) {
                return n;
            }
        }

        return new Node(gameState, null, null);
    }
}
