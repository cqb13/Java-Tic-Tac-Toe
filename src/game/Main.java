package game;

import game.computer.*;
import game.simulation.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner scanner;
    public static Player currentPlayer;

    public static void main(String[] args) {
        Board board = new Board();
        Difficulty difficulty = Difficulty.Hard;
        boolean computerFirst = false;
        boolean otherPlayerIsHuman;
        scanner = new Scanner(System.in);
        currentPlayer = Player.X;

        System.out.print("Would you like to run a simulation (s), generate AI file (g), play against a human (h), or play against a computer (c)? [s,g,h,C]: ");
        String gameModeSelection = scanner.nextLine();

        if (gameModeSelection.equalsIgnoreCase("s") || gameModeSelection.equalsIgnoreCase("simulation")) {
            Difficulty computer1Difficulty = Difficulty.Hard;
            Difficulty computer2Difficulty = Difficulty.Hard;
            boolean outputSimulations = false;

            System.out.print("Would you like the difficulty of computer 1 to be easy (e), medium (m), or hard(h)? [e,m,H]: ");
            String computer1DifficultyString = scanner.nextLine();
            if(computer1DifficultyString.equalsIgnoreCase("e") || computer1DifficultyString.equalsIgnoreCase("easy")){
                computer1Difficulty = Difficulty.Easy;
            }
            if(computer1DifficultyString.equalsIgnoreCase("m") || computer1DifficultyString.equalsIgnoreCase("medium")){
                computer1Difficulty = Difficulty.Medium;
            }

            System.out.print("Would you like the difficulty of computer 2 to be easy (e), medium (m), or hard(h)? [e,m,H]: ");
            String computer2DifficultyString = scanner.nextLine();
            if(computer2DifficultyString.equalsIgnoreCase("e") || computer2DifficultyString.equalsIgnoreCase("easy")){
                computer2Difficulty = Difficulty.Easy;
            }
            if(computer2DifficultyString.equalsIgnoreCase("m") || computer2DifficultyString.equalsIgnoreCase("medium")){
                computer2Difficulty = Difficulty.Medium;
            }

            int simulations = getSimulationNumber();

            System.out.print("Would you like to watch the games? [y,N]: ");
            String outputSimulationsString = scanner.nextLine();
            if(outputSimulationsString.equalsIgnoreCase("y") || outputSimulationsString.equalsIgnoreCase("yes")){
                outputSimulations = true;
            }

            Simulation simulation = new Simulation(computer1Difficulty, computer2Difficulty, simulations, outputSimulations);
            ArrayList<SimulationResult> simulationData = simulation.run();

            int playerOneWins = 0;
            int playerTwoWins = 0;

            for (SimulationResult simulationResult : simulationData) {
                if (simulationResult.winner() == Winner.ComputerOne) {
                    playerOneWins += 1;
                }

                if (simulationResult.winner() == Winner.ComputerTwo) {
                    playerTwoWins += 1;
                }
            }

            System.out.println("Computer One won " + playerOneWins + " games");
            System.out.println("Computer Two won " + playerTwoWins + " games");
            System.out.print(simulations - (playerOneWins + playerTwoWins) + " Draws");
            return;
        }

        if (gameModeSelection.equalsIgnoreCase("g") || gameModeSelection.equalsIgnoreCase("generate")) {
            ArrayList<GameStateNode> gameMap = GameSolver.calculateGame();
            GameSolver.saveToFile(gameMap, "./gamemap.txt");
            return;
        }

        otherPlayerIsHuman = gameModeSelection.equalsIgnoreCase("h") || gameModeSelection.equalsIgnoreCase("human");

        if (!otherPlayerIsHuman) {
            System.out.print("Would you like to player the computer on easy (e), medium (m), or hard(h)? [e,m,H]: ");

            String maybeValidDifficulty = scanner.nextLine();

            if (maybeValidDifficulty.equalsIgnoreCase("e") || maybeValidDifficulty.equalsIgnoreCase("easy")){
                difficulty = Difficulty.Easy;
            } else if (maybeValidDifficulty.equalsIgnoreCase("m") || maybeValidDifficulty.equalsIgnoreCase("medium")){
                difficulty = Difficulty.Medium;
            }

            System.out.print("Would you like the computer to go first? [y,N]: ");
            String maybeComputerFirst = scanner.nextLine();
            computerFirst = maybeComputerFirst.equalsIgnoreCase("y") || maybeComputerFirst.equalsIgnoreCase("yes");
        }

        Computer computer = new Computer(difficulty, computerFirst ? Player.X : Player.O);

        if (computerFirst) {
            currentPlayer = Player.O;
            computer.updateBoard(board.getBoard());
            int location = computer.makeMove() + 1;
            computer.updateBoard(board.getBoard());

            board.placeTile(location, computer.getPlayer().tile);
            computer.updateBoard(board.getBoard());
        }

        board.display();
        while (true) {
            int number = getInputNumber();
            while (!board.placeTile(number, currentPlayer.tile)) {
                number = getInputNumber();
            }

            if (board.playerWon()) {
                board.display();
                System.out.println("Player " + currentPlayer.string + " won!");
                break;
            }

            if (board.isFull()) {
                board.display();
                System.out.println("Its a draw!");
                break;
            }

            if (otherPlayerIsHuman) {
                board.display();
                currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;

                continue;
            }

            computer.updateBoard(board.getBoard());
            int location = computer.makeMove() + 1;
            computer.updateBoard(board.getBoard());

            board.placeTile(location, computer.getPlayer().tile);
            board.display();

            if (board.playerWon()) {
                System.out.println("Player " + computer.getPlayer().string + " won!");
                break;
            }

            if (board.isFull()) {
                System.out.println("Its a draw!");
                break;
            }
        }
    }

    public static int getInputNumber() {
        System.out.print("Enter the number seen in a square: ");
        String input = scanner.nextLine();
        int value;

        try {
            value = Integer.parseInt(input);
        } catch(Exception e){
            value = getInputNumber();
        }

        if (value > 0 && value < 10) {
            return value;
        } else {
            return getInputNumber();
        }
    }

    public static int getSimulationNumber() {
        System.out.print("How many simulations would you like to run? ");
        String input = scanner.nextLine();
        int value;

        try {
            value = Integer.parseInt(input);
        } catch(Exception e){
            value = getInputNumber();
        }

        return value;
    }
}