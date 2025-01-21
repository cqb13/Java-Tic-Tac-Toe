package game.simulation;

import game.*;
import game.computer.Computer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class GameSolver {
    private static Computer computerX = new Computer(Difficulty.Hard, Player.X);
    private static Computer computerO = new Computer(Difficulty.Hard, Player.O);

    public static ArrayList<GameStateNode> calculateGame() {
        ArrayList<GameStateNode> gameMap = new ArrayList<>();

        computerX = new Computer(Difficulty.Hard, Player.X);
        computerO = new Computer(Difficulty.Hard, Player.O);

        for (int i = 0; i < 9; i++) {
            Board board = new Board();

            GameStateNode move = calculateMove(board, new GameStateNode(i, false), Player.X);
            if (i == 0) {
                move.makeBest();
            }
            gameMap.add(move);
        }

        return gameMap;
    }

    private static GameStateNode calculateMove(Board board, GameStateNode move, Player currentPlayer) {
        if (board.playerWon() || board.isFull()) {
            board.placeTile(move.getMoveLocation() + 1, Tile.Empty);

            return move;
        }
        board.placeTile(move.getMoveLocation() + 1, currentPlayer.tile);
        if (board.playerWon() || board.isFull()) {
            board.placeTile(move.getMoveLocation() + 1, Tile.Empty);

            return move;
        }

        computerX.updateBoard(board.getBoard());
        computerO.updateBoard(board.getBoard());
        int bestMoveLocation = switch (currentPlayer) {
            case Player.O -> computerX.makeMove();
            case Player.X -> computerO.makeMove();
        };
        computerX.updateBoard(board.getBoard());
        computerO.updateBoard(board.getBoard());
        for (int i = 0; i < 9; i++) {
            if (board.getTile(i + 1) != Tile.Empty) {
                continue;
            }

            computerX.updateBoard(board.getBoard());
            computerO.updateBoard(board.getBoard());
            GameStateNode next = calculateMove(board, new GameStateNode(i, false), currentPlayer == Player.O ? Player.X : Player.O);
            if (i == bestMoveLocation) {
                next.makeBest();
            }
            move.addMove(next);
            board.placeTile(i + 1, Tile.Empty);
        }
        board.placeTile(move.getMoveLocation() + 1, Tile.Empty);

        return move;
    }

    public static void saveToFile(ArrayList<GameStateNode> gameMap, String path) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (GameStateNode parentNode : gameMap) {
                int depth = 0;
                recursiveWrite(parentNode, depth, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void recursiveWrite(GameStateNode gameStateNode, int depth, Writer writer) throws IOException {
        writer.write("  ".repeat(depth) + (gameStateNode.isBest() ? gameStateNode.getMoveLocation() + "*" : gameStateNode.getMoveLocation()) + "\n");
        if (gameStateNode.getNextMoves().isEmpty()) {
            return;
        }

        for (GameStateNode node : gameStateNode.getNextMoves()) {
            recursiveWrite(node, depth + 1, writer);
        }
    }

    public static ArrayList<GameStateNode> loadFromFile(String path) {
        ArrayList<GameStateNode> gameMap = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(path))) {
            GameStateNode root = parseGameMapFileLine(br, 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return gameMap;
    }

    public static GameStateNode parseGameMapFileLine(BufferedReader br, int depth) throws IOException {
        GameStateNode parentNode = new GameStateNode(0, true);

        br.mark(0);
        String line = br.readLine();
        while(line.startsWith("\t".repeat(depth))){
            br.reset();
            parentNode.addMove(parseGameMapFileLine(br, depth + 1));
            br.mark(0);
            line = br.readLine();
        }
        br.reset();

        return parentNode;
    }
}
