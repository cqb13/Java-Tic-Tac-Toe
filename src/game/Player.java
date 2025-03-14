package game;

public enum Player {
    X (Tile.X, "X"),
    O (Tile.O, "O");

    public final Tile tile;
    public final String string;

    Player(Tile tile, String string){
        this.tile = tile;
        this.string = string;
    }
}
