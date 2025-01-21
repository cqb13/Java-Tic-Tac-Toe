package game.simulation;

public record SimulationResult(Winner winner, int movesInGame) {
    public String winnerName() {
        if (this.winner == Winner.ComputerOne) {
            return "Computer One";
        } else {
            return "Computer Two";
        }
    }
}
