package tictactoe;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static char[][] board = new char[3][3];
    public static int moves;

    public static State getGameState() {

        // check rows and cols for three in a row
        for (int row = 0; row < 3; row++) {
            StringBuilder reeks1 = new StringBuilder();
            StringBuilder reeks2 = new StringBuilder();
            for (int col = 0; col < 3; col++) {
                reeks1.append(board[row][col]);
                reeks2.append(board[col][row]);
            }
            if (reeks1.toString().equals("XXX") || reeks2.toString().equals("XXX")) {
                return State.X_WIN;
            }
            if (reeks1.toString().equals("OOO") || reeks2.toString().equals("OOO")) {
                return State.O_WIN;
            }
        }

        // check diagonals for three in a row
        String diagonal1 = "" + board[0][0] + board[1][1] + board[2][2];
        String diagonal2 = "" + board[0][2] + board[1][1] + board[2][0];
        if (diagonal1.equals("XXX") || diagonal2.equals("XXX")) return State.X_WIN;
        if (diagonal1.equals("OOO") || diagonal2.equals("OOO")) return State.O_WIN;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return State.UNDECIDED;
                }
            }
        }
        return State.DRAW;
    }

    public static void createEmptyBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public static void printBoard() {
        System.out.println("---------");
        for (int row = 0; row < 3; row++) {
            System.out.print("| ");
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println("| ");
        }
        System.out.println("---------");
    }

    public static void main(String[] args) {
        do {
            System.out.println("Input command: ");
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.nextLine();
            Scanner scanCommand = new Scanner(cmd);
            if (!scanCommand.hasNext()) { // no tokens!
                System.out.println("Bad parameters!");
            } else {
                String command = scanCommand.next();
                if (command.equals("exit")) { // exit the program
                    return;
                } else {
                    if (!command.equals("start")) {
                        System.out.println("Bad parameters!");
                    } else { // check the start parameters
                        if (scanCommand.hasNext()) {
                            String firstPlayer = scanCommand.next();
                            if (!(firstPlayer.equals("user") || firstPlayer.equals("easy") || firstPlayer.equals("medium") || firstPlayer.equals("hard"))) {
                                System.out.println("Bad parameters!");
                                continue;
                            }
                            if (scanCommand.hasNext()) {
                                String secondPlayer = scanCommand.next();
                                if (!(secondPlayer.equals("user") || secondPlayer.equals("easy") || secondPlayer.equals("medium") || secondPlayer.equals("hard"))) {
                                    System.out.println("Bad parameters!");
                                    continue;
                                }
                                playGame(firstPlayer, secondPlayer);
                            } else {
                                System.out.println("Bad parameters!");
                            }
                        } else {
                            System.out.println("Bad parameters!");
                        }
                    }
                }
            }
        } while (true);
    }

    public static void playGame(String firstPlayer, String secondPlayer) {
        createEmptyBoard();
        moves = 0;
        printBoard();

        do {
            switch ((moves % 2 == 0) ? firstPlayer : secondPlayer) {
                case "user" -> makeUserMove();
                case "easy" -> makeEasyMove();
                case "medium" -> makeMediumMove();
                case "hard" -> makeHardMove();
            }
            moves++;
            printBoard();
        } while (getGameState().equals(State.UNDECIDED));
        switch (getGameState()) {
            case X_WIN -> System.out.println("X wins");
            case O_WIN -> System.out.println("O wins");
            case DRAW -> System.out.println("Draw");
        }
    }

    public static void makeEasyMove() {
        System.out.println("Making move level \"easy\"");
        easyMove();
    }

    public static void easyMove() {
        Random random = new Random();
        int row, col;
        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != ' ');
        board[row][col] = (moves % 2 == 0) ? 'X' : 'O';
    }

    public static void makeMediumMove() {
        System.out.println("Making move level \"medium\"");
        char fillChar = moves % 2 == 0 ? 'X' : 'O';
        char otherChar = fillChar == 'X' ? 'O' : 'X';
        if (isNotTwoInARow(fillChar, fillChar)) {
            if (isNotTwoInARow(otherChar, fillChar)) {
                easyMove();
            }
        }
    }

    public static void makeHardMove() {
        makeAITurn();
    }

    public static boolean isNotTwoInARow(char doubleChar, char fillChar) {
        int emptyRow = -1;
        int emptyCol = -1;

        for (int i = 0; i < 3; i++) {
            int empty = 0;
            int found = 0;
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    empty++;
                    emptyRow = i;
                    emptyCol = j;
                } else if (board[i][j] == doubleChar) {
                    found++;
                }
            }
            if (found == 2 && empty == 1) {
                board[emptyRow][emptyCol] = fillChar;
                return false;
            }
        }

        for (int j = 0; j < 3; j++) {
            int empty = 0;
            int found = 0;
            for (int i = 0; i < 3; i++) {
                if (board[i][j] == ' ') {
                    empty++;
                    emptyRow = i;
                    emptyCol = j;
                } else if (board[i][j] == doubleChar) {
                    found++;
                }
            }
            if (found == 2 && empty == 1) {
                board[emptyRow][emptyCol] = fillChar;
                return false;
            }
        }

        int empty = 0;
        int found = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][i] == ' ') {
                empty++;
                emptyRow = i;
            } else if (board[i][i] == doubleChar) {
                found++;
            }
        }
        if (found == 2 && empty == 1) {
            board[emptyRow][emptyRow] = fillChar;
            return false;
        }

        empty = 0;
        found = 0;
        for (int i = 0; i < 3; i++) {
            if (board[i][2 - i] == ' ') {
                empty++;
                emptyRow = i;
            } else if (board[i][2 - i] == doubleChar) {
                found++;
            }
        }
        if (found == 2 && empty == 1) {
            board[emptyRow][2 - emptyRow] = fillChar;
            return false;
        }

        return true;
    }

    public static void makeUserMove() {
        boolean legalMove = false;
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the coordinates: ");
            String coordinateString = scanner.nextLine();
            Scanner stringScanner = new Scanner(coordinateString);
            String coordinate2 = "";
            String coordinate1 = stringScanner.next();
            if (stringScanner.hasNext()) {
                coordinate2 = stringScanner.next();
            }
            if (!(Utility.isInteger(coordinate1) && Utility.isInteger(coordinate2))) {
                System.out.println("You should enter numbers!");
                continue;
            }
            int c1 = Integer.parseInt(coordinate1);
            int c2 = Integer.parseInt(coordinate2);
            if (!(c1 > 0 && c1 < 4 && c2 > 0 && c2 < 4)) {
                System.out.println("Coordinates should be from 1 to 3!");
                continue;
            }
            if (board[c1 - 1][c2 - 1] != ' ') {
                System.out.println("This cell is occupied! Choose another one!");
            } else {
                board[c1 - 1][c2 - 1] = (moves % 2 == 0) ? 'X' : 'O';
                legalMove = true;
            }
        } while (!legalMove);
    }

//    Mike

    public static void makeAITurn() {
        char aiStatus = (moves % 2 == 0) ? 'X' : 'O'; //Get the symbol the AI is playing. (1=x, 2=o)
        double bestScore = Double.NEGATIVE_INFINITY;
        GridPos bestMove = new GridPos();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                // Is the spot available?
                if (board[x][y] == ' ') {
                    board[x][y] = aiStatus; //Mark this position
                    double score = minimax(false); //Check the score we end up after marking y and x as our move.
                    board[x][y] = ' '; //Unmarked move.
                    if (score > bestScore) { //If this move had a better score then our previous best score then:
                        bestScore = score; //Mark our current score as our best score.
                        bestMove = new GridPos(x, y); //Save the mark as our best move.
                    }
                }
            }
        }
        board[bestMove.row][bestMove.col] = aiStatus; //Apply our best move.
//        endTurn();
    }

    public static double minimax(boolean isMaximizing) {
        State result = getGameState();
        if (result != State.UNDECIDED) {
            switch (result) {
                case X_WIN:
                    return ((moves % 2 == 0) ? 1 : -1); //Return 1 if we were playing as player 1 and player 1 won. Else return -1 if we lost.
                case O_WIN:
                    return ((moves % 2 != 0) ? 1 : -1); //Return 1 if we were playing as player 2 and player 2 won. Else return -1 if we lost.
                case DRAW:
                    return 0; //Return 0 if the last move ended up with a tie.
            }
        }
        //Check all remaining possibilities if we have the advantage in the end.
        double bestScore;
        if (isMaximizing) { //Look for the best move
            bestScore = Double.NEGATIVE_INFINITY;
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    // Is the spot available?
                    if (board[x][y] == ' ') {
                        board[x][y] = (moves % 2 == 0) ? 'X' : 'O';
                        double score = minimax(false);
                        board[x][y] = ' ';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
        } else { //Look for the worst move
            bestScore = Double.POSITIVE_INFINITY;
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    // Is the spot available?
                    if (board[x][y] == ' ') {
                        board[x][y] = (moves % 2 != 0) ? 'X' : 'O';
                        double score = minimax(true);
                        board[x][y] = ' ';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
        }
        return bestScore;
    }
}