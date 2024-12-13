import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static Player currentPlayer = Player.X;

    public static void main(String[] args) {
        Board board = new Board();
        Computer computer = new Computer(board.getBoard());

        board.display();
        while (true) {
            while (true) {
                int number = getInputNumber();

                if (board.placePiece(number, currentPlayer == Player.X ? Tile.X : Tile.O)) {
                    break;
                }
            }

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

            currentPlayer = currentPlayer == Player.X ? Player.O : Player.X;
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