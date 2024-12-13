import java.util.ArrayList;

public class Computer {
    private Tile[] board;

    public Computer() {
        this.board = new Tile[]{Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty};
    }

    public Computer(Tile[] board) {
        this.board = board;
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
}
