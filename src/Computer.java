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
            case Hard -> miniMax(Player.O, true, 0).getLocation();
            case Medium -> mediumDifficultyComputer();
            case Easy -> randomMove();
        };
    }

    private int randomMove() {
        ArrayList<Number> emptyTiles = getEmptyTiles();

        return (int)(Math.random() * emptyTiles.size());
    }

    private int mediumDifficultyComputer() {
        int openTiles = getEmptyTiles().size();

        // Choose side on first move
        if (openTiles == 9) {
            System.out.println("side");
            int sideChoice = (int) Math.floor(Math.random() * 4);
            return sideChoice * 2 + 1;
        }

        // Dont take the center on the first move
        if (openTiles == 8) {
            System.out.println("not center");
            return miniMax(Player.O, false, 0).getLocation();
        }

        return miniMax(Player.O, true, 0).getLocation();
    }

    private MiniMaxResult miniMax(Player currentPlayer, boolean checkCenter, int turn) {
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

        int wins = 0;
        for (int location : openSquares) {
            if (location == 4 && !checkCenter) {
                continue;
            }

            this.board[location] = currentPlayer == Player.X ? Tile.X : Tile.O;
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
