public class MiniMaxResult {
    private int location;
    private int score;

    public MiniMaxResult(int location, int score) {
        this.location = location;
        this.score = score;
    }

    public int getLocation() {
        return this.location;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
