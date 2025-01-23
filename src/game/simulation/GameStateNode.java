package game.simulation;

import java.util.ArrayList;

public class GameStateNode {
    private final int move;
    private boolean isBest;
    private boolean isLastMove;
    private final ArrayList<GameStateNode> nextMoves;

    public GameStateNode(int move, boolean isBest) {
        this.move = move;
        this.isBest = isBest;
        this.isLastMove = false;
        this.nextMoves = new ArrayList<>();
    }

    public int getMove() {
        return this.move;
    }

    public void addMove(GameStateNode move) {
        this.nextMoves.add(move);
    }

    public int getMoveLocation() {
        return this.move;
    }

    public ArrayList<GameStateNode> getNextMoves() {
        return this.nextMoves;
    }

    public GameStateNode findMove(int index) {
        if (this.isLastMove) {
            return null;
        }

        for (GameStateNode node : this.nextMoves) {
            if (node.getMove() == index) {
                return node;
            }
        }

        return null;
    }

    public boolean isLastMove() {
        return this.isLastMove;
    }

    public void makeLastMove() {
        this.isLastMove = true;
    }

    public boolean isBest() {
        return this.isBest;
    }

    public void makeBest() {
        this.isBest = true;
    }

    public GameStateNode getNextBestMove() {
        if (this.isLastMove) {
            return null;
        }

        for (GameStateNode move : this.nextMoves) {
            if (move.isBest()) {
                return move;
            }
        }
        return null;
    }
}
