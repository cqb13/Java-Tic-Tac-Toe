import java.util.ArrayList;

public class Computer {
    boolean hard;
    private Tile[] board;

    public Computer(boolean hard) {
        this.hard = hard;
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
        if (this.hard) {
            return randomMove();
        } else {
            return MiniMax();
        }
    }

    private int randomMove() {
        ArrayList<Number> emptyTiles = getEmptyTiles();

        return (int)(Math.random() * emptyTiles.size());
    }

    private int MiniMax() {
        return 1;
    }
}
