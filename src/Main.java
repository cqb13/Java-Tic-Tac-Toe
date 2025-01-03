import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static Player currentPlayer = Player.X;

    public static void main(String[] args) {
        Board board = new Board();
        Computer.Difficulty difficulty = Computer.Difficulty.Hard;
        boolean otherPlayerIsHuman;
        boolean computerFirst = false;
        scanner = new Scanner(System.in);
        currentPlayer = Player.X;

        System.out.print("Would you like to play against a human (h) or a computer (c)? [h,C]: ");
        String otherPlayer = scanner.next();

        otherPlayerIsHuman = otherPlayer.equalsIgnoreCase("h") || otherPlayer.equalsIgnoreCase("human");

        if (!otherPlayerIsHuman) {
            System.out.print("Would you like to player the computer on easy (e), medium (m), or hard(h)? [e,m,H]: ");

            String maybeValidDifficulty = scanner.next();

            if (maybeValidDifficulty.equalsIgnoreCase("e") || maybeValidDifficulty.equalsIgnoreCase("easy")){
                difficulty = Computer.Difficulty.Easy;
            } else if (maybeValidDifficulty.equalsIgnoreCase("m") || maybeValidDifficulty.equalsIgnoreCase("medium")){
                difficulty = Computer.Difficulty.Medium;
            }

            System.out.print("Would you like the computer to go first? [y,N]: ");
            String maybeComputerFirst = scanner.next();
            computerFirst = maybeComputerFirst.equalsIgnoreCase("y") || maybeComputerFirst.equalsIgnoreCase("yes");
        }

        Computer computer = new Computer(difficulty);

        if (computerFirst) {
            computer.updateBoard(board.getBoard());
            int location = computer.makeMove() + 1;
            computer.updateBoard(board.getBoard());

            board.placePiece(location, Tile.O);
            computer.updateBoard(board.getBoard());
            board.display();
        }

        board.display();
        while (true) {
            while (true) {
                int number = getInputNumber();

                if (board.placePiece(number, currentPlayer == Player.X ? Tile.X : Tile.O)) {
                    break;
                }
            }

            if (board.playerWon()) {
                board.display();
                if (currentPlayer == Player.X) {
                    System.out.println("Player X won!");
                } else {
                    System.out.println("Player O won!");
                }
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

                while (true) {
                    int number = getInputNumber();

                    if (board.placePiece(number, currentPlayer == Player.X ? Tile.X : Tile.O)) {
                        break;
                    }
                }

                if (board.playerWon()) {
                    board.display();
                    if (currentPlayer == Player.X) {
                        System.out.println("Player X won!");
                    } else {
                        System.out.println("Player O won!");
                    }
                    break;
                }

                if (board.isFull()) {
                    board.display();
                    System.out.println("Its a draw!");
                    break;
                }
                board.display();
                continue;
            }

            computer.updateBoard(board.getBoard());
            int location = computer.makeMove() + 1;
            computer.updateBoard(board.getBoard());

            board.placePiece(location, Tile.O);
            computer.updateBoard(board.getBoard());
            board.display();
            if (board.playerWon()) {
                if (currentPlayer == Player.X) {
                    System.out.println("Player X won!");
                } else {
                    System.out.println("Player O won!");
                }
                break;
            }

            if (board.isFull()) {
                System.out.println("Its a draw!");
                break;
            }
        }
    }

    public static int getInputNumber() {
        System.out.println("Enter the number seen in a square: ");
        int value = scanner.nextInt();
        if (value > 0 && value < 10) {
            return value;
        } else {
            return getInputNumber();
        }
    }
}