package game;

public class Board {
    private final Tile[] board;
    private static final String[] xTile = {
            " \\  /  ",
            "   \\/   ",
            "   /\\   ",
            "  /  \\  ",
    };
    private static final String[] oTile = {
            "+----+ ",
            " |    | ",
            " |    | ",
            " +----+ ",
    };
    private static final String[] emptyTile = {
            "       ",
            "        ",
            "        ",
            "        ",
    };

    public Board() {
        this.board = new Tile[]{Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty, Tile.Empty};
    }

    public Tile[] getBoard() {
        return this.board;
    }

    public void display() {
        for (int i = 0; i < 9; i += 3) {
            System.out.println("+--------+--------+--------+");
            String[] firstTile = getPieceTile(this.board[i]);
            String[] secondTile = getPieceTile(this.board[i + 1]);
            String[] thirdTile = getPieceTile(this.board[i + 2]);
            System.out.println("|" +
                    (this.board[i] == Tile.Empty ? i + 1 : " ") + firstTile[0] + "|" +
                    (this.board[i + 1] == Tile.Empty ? i + 2 : " ") + secondTile[0] + "|" +
                    (this.board[i + 2] == Tile.Empty ? i + 3 : " ") + thirdTile[0] + "|");
            System.out.println("|" + firstTile[1] + "|" + secondTile[1] + "|" + thirdTile[1] + "|");
            System.out.println("|" + firstTile[2] + "|" + secondTile[2] + "|" + thirdTile[2] + "|");
            System.out.println("|" + firstTile[3] + "|" + secondTile[3] + "|" + thirdTile[3] + "|");
        }
        System.out.println("+--------+--------+--------+");
    }

    private static String[] getPieceTile(Tile tile) {
        switch (tile) {
            case X -> {
                return xTile;
            }
            case O -> {
                return oTile;
            }
            case Empty -> {
                return emptyTile;
            }
        }
        return emptyTile;
    }

    public boolean placeTile(int location, Tile tile) {
        int index = location - 1;
        if (this.board[index] != Tile.Empty && tile != Tile.Empty) {
            return false;
        }

        this.board[index] = tile;
        return true;
    }

    public Tile getTile(int location) {
        return this.board[location - 1];
    }

    public boolean isFull() {
        for (Tile tile : this.board) {
            if (tile == Tile.Empty) {
                return false;
            }
        }

        return true;
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
