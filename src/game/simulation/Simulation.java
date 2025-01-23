package game.simulation;

import java.util.ArrayList;
import game.*;
import game.computer.Computer;

public class Simulation {
    private final Difficulty computerOneDifficulty;
    private final Difficulty computerTwoDifficulty;
    private final int simulations;
    private final boolean displaySimulations;
    private final ArrayList<GameStateNode> gameMap;

    public Simulation(Difficulty computerOneDifficulty, Difficulty computerTwoDifficulty, int simulations, boolean displaySimulations) {
        this.computerOneDifficulty = computerOneDifficulty;
        this.computerTwoDifficulty = computerTwoDifficulty;
        this.simulations = simulations;
        this.displaySimulations = displaySimulations;
        // TODO: if a file does not exist generate it, removes the generate option from main
        this.gameMap = GameSolver.loadFromFile("gamemap.txt");
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
            SavedComputer computerOne = new SavedComputer(this.computerOneDifficulty, Player.X, gameMap);
            SavedComputer computerTwo = new SavedComputer(this.computerTwoDifficulty, Player.O, gameMap);

            int moves = 0;
            while (true) {
                computerOne.updateBoard(board.getBoard());
                int location = computerOne.makeMove(moves) + 1;
                computerTwo.otherPlayerMove(location - 1, moves == 0);
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
                location = computerTwo.makeMove(moves) + 1;
                computerOne.otherPlayerMove(location - 1, false);
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
