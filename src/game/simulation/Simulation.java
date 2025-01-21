package game.simulation;

import java.util.ArrayList;
import game.*;
import game.computer.Computer;

public class Simulation {
    private final Difficulty computerOneDifficulty;
    private final Difficulty computerTwoDifficulty;
    private final int simulations;
    private final boolean displaySimulations;

    public Simulation(Difficulty computerOneDifficulty, Difficulty computerTwoDifficulty, int simulations, boolean displaySimulations) {
        this.computerOneDifficulty = computerOneDifficulty;
        this.computerTwoDifficulty = computerTwoDifficulty;
        this.simulations = simulations;
        this.displaySimulations = displaySimulations;
    }

    public ArrayList<SimulationResult> run() {
        ArrayList<SimulationResult> simulationData = new ArrayList<>();

        int barDone = 0;
        int percentDone = 0;
        int increaseEach = this.simulations / 100;
        for (int i = 0; i < this.simulations; i++) {
            if(i % increaseEach == 0){
                System.out.print("\r[" + "#".repeat(barDone) + " ".repeat(20 - barDone) + "] " + percentDone + "%");
                percentDone++;
                if(percentDone % 5 == 0) {
                    barDone++;
                }
            }
            Board board = new Board();
            Computer computerOne = new Computer(this.computerOneDifficulty, Player.X);
            Computer computerTwo = new Computer(this.computerTwoDifficulty, Player.O);

            int moves = 0;
            while (true) {
                computerOne.updateBoard(board.getBoard());
                int location = computerOne.makeMove() + 1;
                board.placeTile(location, Tile.X);
                moves++;
                if (this.displaySimulations) {
                    board.display();
                }
                if (board.playerWon()) {
                    simulationData.add(new SimulationResult(Winner.ComputerOne, moves));
                    break;
                }

                if (board.isFull()) {
                    simulationData.add(new SimulationResult(Winner.None, moves));
                    break;
                }

                computerTwo.updateBoard(board.getBoard());
                location = computerTwo.makeMove() + 1;
                board.placeTile(location, Tile.O);
                moves++;
                if (board.playerWon()) {
                    simulationData.add(new SimulationResult(Winner.ComputerTwo, moves));
                    break;
                }

                if (board.isFull()) {
                    simulationData.add(new SimulationResult(Winner.None, moves));
                    break;
                }
                if (this.displaySimulations) {
                    board.display();
                }
            }
        }
        System.out.println("\rDone!");

        return simulationData;
    }
}
