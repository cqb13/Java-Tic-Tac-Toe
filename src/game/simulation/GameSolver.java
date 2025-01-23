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
            move.makeLastMove();
            return move;
        }
        board.placeTile(move.getMoveLocation() + 1, currentPlayer.tile);
        if (board.playerWon() || board.isFull()) {
            board.placeTile(move.getMoveLocation() + 1, Tile.Empty);
            move.makeLastMove();
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
            GameStateNode next = calculateMove(board, new GameStateNode(i, false),
                    currentPlayer == Player.O ? Player.X : Player.O);
            if (i == bestMoveLocation) {
                next.makeBest();
            }
            move.addMove(next);
            board.placeTile(i + 1, Tile.Empty);
        }
        board.placeTile(move.getMoveLocation() + 1, Tile.Empty);

        return move;
    }

    private static void recursiveWriteTxt(GameStateNode gameStateNode, int depth, Writer writer) throws IOException {
        writer.write("\t".repeat(depth)
                + (gameStateNode.isBest() ? gameStateNode.getMoveLocation() + "*" : gameStateNode.getMoveLocation())
                + (gameStateNode.isLastMove() ? "!" : "") + "\n");
        if (gameStateNode.getNextMoves().isEmpty()) {
            return;
        }

        for (GameStateNode node : gameStateNode.getNextMoves()) {
            recursiveWriteTxt(node, depth + 1, writer);
        }
    }

    public static void saveToFileTxt(ArrayList<GameStateNode> gameMap, String path) {
        try (Writer writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            for (GameStateNode parentNode : gameMap) {
                int depth = 0;
                recursiveWriteTxt(parentNode, depth, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialization Format:
     *
     * Each node is stored in the following format:
     *
     * <pre>
     * [Move Location (1 byte)] [Flags (1 byte)] [Number of Children (1 byte)] [Child Data...]
     * </pre>
     *
     * Breakdown of the Binary Format:
     * - Move Location (1 byte): A 1-byte integer representing the location of the
     * move on the board.
     * board.
     * - Flags (1 byte): A byte where each bit represents a specific attribute of
     * the node:
     * - Bit 0 (LSB) represents the "isBest" flag: 1 for true, 0 for false.
     * - Bit 1 represents the "isLast" flag: 1 for true, 0 for false.
     * - Number of Children (1 byte): A 1-byte integer representing the number of
     * child nodes this node has.
     * - Child Data: Recursively written child nodes in the same format.
     *
     * Example for a node with:
     * - moveLocation = 2 (valid position on the board)
     * - isBest = true (this is the best move)
     * - isLast = false (this is not the last move)
     * - 3 children
     *
     * The binary data would look like this (hex):
     *
     * 02 03 03 [Child1 data] [Child2 data] [Child3 data]
     *
     * Breakdown:
     * - 02: The move location is 2 (valid value between 0 and 8).
     * - 03: Flags byte (0b00000011), where:
     * - isBest = true (1)
     * - isLast = false (0)
     * - 03: The node has 3 children.
     * - [Child1 data] [Child2 data] [Child3 data]: Recursively written child nodes.
     *
     * Child nodes are also serialized in the same format. Ex:
     * - A child node with moveLocation = 1, isBest = false, isLast = true, and no
     * children:
     *
     * 01 02 00
     *
     * Breakdown:
     * - 01: The move location is 1.
     * - 02: Flags byte (0b00000010), where:
     * - isBest = false (0)
     * - isLast = true (1)
     * - 00: The node has no children.
     */
    private static void recursiveWriteBinary(GameStateNode gameStateNode, DataOutputStream dos) throws IOException {
        dos.writeByte(gameStateNode.getMoveLocation()); // Store the move location as an integer
        dos.writeByte((gameStateNode.isBest() ? 1 : 0) | (gameStateNode.isLastMove() ? 2 : 0)); // Encode flags in a single
        dos.writeByte(gameStateNode.getNextMoves().size()); // Number of children

        for (GameStateNode node : gameStateNode.getNextMoves()) {
            recursiveWriteBinary(node, dos);
        }
    }

    public static void saveToFile(ArrayList<GameStateNode> gameMap, String path) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path))) {
            for (GameStateNode parentNode : gameMap) {
                recursiveWriteBinary(parentNode, dos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write game map to file", e);
        }
    }

    private static GameStateNode recursiveReadBinary(DataInputStream dis) throws IOException {
        int moveLocation = dis.readByte();
        byte flags = dis.readByte();
        boolean isBest = (flags & 1) != 0;
        boolean isLast = (flags & 2) != 0;

        GameStateNode node = new GameStateNode(moveLocation, isBest);
        if (isLast) {
            node.makeLastMove();
        }

        int childCount = dis.readByte();
        for (int i = 0; i < childCount; i++) {
            node.addMove(recursiveReadBinary(dis));
        }

        return node;
    }

    public static ArrayList<GameStateNode> loadFromFile(String path) {
        ArrayList<GameStateNode> gameMap = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
            while (dis.available() > 0) {
                gameMap.add(recursiveReadBinary(dis));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read game map from file", e);
        }

        return gameMap;
    }
}
