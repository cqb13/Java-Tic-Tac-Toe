package game.simulation;

import game.Difficulty;
import game.Player;
import game.Tile;

import java.util.ArrayList;

public class SavedComputer {
    private final Difficulty difficulty;
    private final Player player;
    private Tile[] board;
    private final ArrayList<GameStateNode> gameMap;
    private GameStateNode currentNode;

    public SavedComputer(Difficulty difficulty, Player player, ArrayList<GameStateNode> gameMap) {
        this.difficulty = difficulty;
        this.player = player;
        this.gameMap = gameMap;
        this.board = new Tile[]{Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty};
    }
    
    public void otherPlayerMove(int move, boolean isFirst) {
        if (this.difficulty == Difficulty.Easy) return;

        if(isFirst) {
            currentNode = gameMap.get(move);
        } else {
            currentNode = currentNode.findMove(move);
        }
    }

    public void updateBoard(Tile[] board) {
        this.board = board;
    }

    public int makeMove(int moves) {
        boolean isFirst = moves == 0;
        int move = switch (this.difficulty) {
            case Hard -> this.hardMove(isFirst);
            case Medium -> this.mediumMove(moves);
            case Easy -> randomMove();
        };

        if (this.difficulty == Difficulty.Easy) {
            return move;
        }

        if(isFirst) {
            this.currentNode = this.gameMap.get(move);
        } else {
            this.currentNode = this.currentNode.findMove(move);
        }
        return move;
    }

    private int hardMove(boolean isFirst) {
        if(isFirst) {
            return 0;
        } else {
            return this.currentNode.getNextBestMove().getMove();
        }
    }

    private int mediumMove(int moves) {
        if(moves == 0) {
            int sideChoice = (int) Math.floor(Math.random() * 4);
            return sideChoice * 2 + 1;
        }

        if(moves == 1) {
            if(this.currentNode.getMove() == 0) {
                return 2;
            } else {
                return 0;
            }
        }

        if(moves == 2) {
            if (this.board[1] == this.player.tile && this.board[7] == Tile.Empty) {
                return 7;
            }

            if (this.board[7] == this.player.tile && this.board[1] == Tile.Empty) {
                return 1;
            }

            if (this.board[3] == this.player.tile && this.board[5] == Tile.Empty) {
                return 5;
            }

            if (this.board[5] == this.player.tile && this.board[3] == Tile.Empty) {
                return 3;
            }

            if (this.board[1] == Tile.Empty) {
                return 1;
            }

            if (this.board[3] == Tile.Empty) {
                return 3;
            }

            if (this.board[5] == Tile.Empty) {
                return 5;
            }
        }

        return hardMove(false);
    }

    private ArrayList<Integer> getEmptyTiles() {
        ArrayList<Integer> emptyTiles = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if (this.board[i] == Tile.Empty) {
                emptyTiles.add(i);
            }
        }

        return emptyTiles;
    }

    private int randomMove() {
        ArrayList<Integer> emptyTiles = getEmptyTiles();

        return emptyTiles.get((int)(Math.random() * emptyTiles.size()));
    }
}