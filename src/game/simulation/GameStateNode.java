package game.simulation;

import java.util.ArrayList;

public class GameStateNode {
    private int move;
    private boolean isBest;
    private ArrayList<GameStateNode> nextMoves;

    public GameStateNode(int move, boolean isBest) {
        this.move = move;
        this.isBest = isBest;
        this.nextMoves = new ArrayList<>();
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

    public boolean isBest() {
        return this.isBest;
    }

    public void makeBest() {
        this.isBest = true;
    }

    public GameStateNode getNextBestMove() {
        for (GameStateNode move : this.nextMoves) {
            if (move.isBest()) {
                return move;
            }
        }
        return null;
    }
}
