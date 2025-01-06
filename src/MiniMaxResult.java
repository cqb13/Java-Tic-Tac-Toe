public class MiniMaxResult {
    private int location;
    private int score;
    private int winningMoves;

    public MiniMaxResult(int location, int score) {
        this.location = location;
        this.score = score;
        this.winningMoves = 0;
    }

    public int getLocation() {
        return this.location;
    }

    public int getScore() {
        return this.score;
    }

    public int getWinningMoves() {
        return this.winningMoves;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public void setWinningMoves(int winningMoves) {
        this.winningMoves = winningMoves;
    }
}
