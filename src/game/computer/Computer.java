package game.computer;

import game.Difficulty;
import game.Player;
import game.Tile;

import java.util.ArrayList;

public class Computer {
    private final Difficulty difficulty;
    private Tile[] board;
    private final Player player;

    public Computer(Difficulty difficulty, Player player) {
        this.difficulty = difficulty;
        this.player = player;
        this.board = new Tile[]{Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty};
    }

    public Player getPlayer() {
        return this.player;
    }

    public void updateBoard(Tile[] board) {
        this.board = board;
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

    public int makeMove() {
        return switch (this.difficulty) {
            case Hard -> miniMax(this.player, true, 0).getLocation();
            case Medium -> mediumDifficultyComputer();
            case Easy -> randomMove();
        };
    }

    private int randomMove() {
        ArrayList<Integer> emptyTiles = getEmptyTiles();

        return emptyTiles.get((int)(Math.random() * emptyTiles.size()));
    }

    private int mediumDifficultyComputer() {
        int openTiles = getEmptyTiles().size();

        // Choose side on first move
        if (openTiles == 9) {
            int sideChoice = (int) Math.floor(Math.random() * 4);
            return sideChoice * 2 + 1;
        }

        // Dont take the center on the first move
        if (openTiles == 8) {
            return miniMax(this.player, false, 0).getLocation();
        }

        if(openTiles == 7) {
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

        return miniMax(Player.O, true, 0).getLocation();
    }

    private MiniMaxResult miniMax(Player currentPlayer, boolean checkCenter, int turn) {
        Player computerPlayer = this.player;
        Player otherPlayer = currentPlayer == Player.X ? Player.O : Player.X;

        ArrayList<Integer> openSquares = getEmptyTiles();
        MiniMaxResult result = new MiniMaxResult(-1, 0);

        // The player won on the last turn
        if (playerWon()) {
            if (currentPlayer == computerPlayer) {
                result.setScore(-15 + turn);
            } else {
                result.setScore(15 - turn);
            }

            return result;
        }

        if (openSquares.isEmpty()) {
            result.setScore(0);
            return result;
        }

        MiniMaxResult bestMove = new MiniMaxResult(-1, currentPlayer == computerPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);

        int wins = 0;
        for (int location : openSquares) {
            if (location == 4 && !checkCenter) {
                continue;
            }

            this.board[location] = currentPlayer.tile;
            MiniMaxResult miniResult = miniMax(otherPlayer, true, turn + 1);

            if (miniResult.getScore() < 0 ) {
                wins += 1;
            }

            this.board[location] = Tile.Empty;

            if (currentPlayer == computerPlayer) {
                if (miniResult.getScore() > bestMove.getScore()) {
                    bestMove.setScore(miniResult.getScore());
                    bestMove.setLocation(location);
                    bestMove.setWinningMoves(miniResult.getWinningMoves());
                }
            } else {
                if (miniResult.getScore() < bestMove.getScore()) {
                    bestMove.setScore(miniResult.getScore());
                    bestMove.setLocation(location);
                    bestMove.setWinningMoves(miniResult.getWinningMoves());
                }
            }

            if (miniResult.getScore() == bestMove.getScore() && miniResult.getWinningMoves() < bestMove.getWinningMoves()) {
                bestMove.setWinningMoves(miniResult.getWinningMoves());
                bestMove.setLocation(miniResult.getLocation());
            }
        }
        bestMove.setWinningMoves(wins);
        return bestMove;
    }

    public boolean playerWon() {
        // Check verticals
        for (int i = 0; i < 3; i++) {
            if (this.board[i] == this.board[i + 3] && this.board[i] == this.board[i + 6] && this.board[i] != Tile.Empty) {
                return true;
            }
        }

        // Check horizontal
        for (int i = 0; i < 9; i += 3) {
            if (this.board[i] == this.board[i + 1] && this.board[i] == this.board[i + 2] && this.board[i] != Tile.Empty) {
                return true;
            }
        }

        if (this.board[4] == Tile.Empty) {
            return false;
        }

        // Check top left to bottom right and top right to bottom left
        return (this.board[0] == this.board[4] && this.board[4] == this.board[8]) || (this.board[2] == this.board[4] && this.board[4] == this.board[6]);
    }
}
