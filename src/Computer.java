import java.util.ArrayList;

public class Computer {
    private Difficulty difficulty;
    private Tile[] board;

    public enum Difficulty {
        Hard,
        Medium,
        Easy,
    }

    public Computer(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.board = new Tile[]{Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty};
    }

    public void updateBoard(Tile[] board) {
        this.board = board;
    }

    private ArrayList<Number> getEmptyTiles() {
        ArrayList<Number> emptyTiles = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            if (this.board[i] == Tile.Empty) {
                emptyTiles.add(i);
            }
        }

        return emptyTiles;
    }

    public int makeMove() {
        return switch (this.difficulty) {
            case Hard -> miniMax(Player.O, 0).getLocation();
            case Medium -> randomMove();
            case Easy -> randomMove();
        };
    }

    private int randomMove() {
        ArrayList<Number> emptyTiles = getEmptyTiles();

        return (int)(Math.random() * emptyTiles.size());
    }

    private MiniMaxResult miniMax(Player currentPlayer, int turns) {
        Player computerPlayer = Player.O;
        Player otherPlayer = currentPlayer == Player.X ? Player.O : Player.X;

        ArrayList<Number> openSquares = getEmptyTiles();
        MiniMaxResult result = new MiniMaxResult(-1, 0);

        // The player won on the last turn
        if (playerWon()) {
            if (currentPlayer == computerPlayer) {
                result.setScore(-10);
            } else {
                result.setScore(10);
            }

            return result;
        }

        if (openSquares.isEmpty()) {
            result.setScore(0);
            return result;
        }

        MiniMaxResult bestMove = new MiniMaxResult(-1, currentPlayer == computerPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE);

        for (Number location : openSquares) {
            this.board[location.intValue()] = currentPlayer == Player.X ? Tile.X : Tile.O;
            MiniMaxResult miniResult = miniMax(otherPlayer, turns + 1);

            this.board[location.intValue()] = Tile.Empty;

            if (currentPlayer == computerPlayer) {
                if (miniResult.getScore() > bestMove.getScore()) {
                    bestMove.setScore(miniResult.getScore());
                    bestMove.setLocation(location.intValue());
                }
            } else {
                if (miniResult.getScore() < bestMove.getScore()) {
                    bestMove.setScore(miniResult.getScore());
                    bestMove.setLocation(location.intValue());
                }
            }
        }

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
