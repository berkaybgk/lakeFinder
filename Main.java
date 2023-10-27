import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This projects aim is to create the Radegast's Game
 * It reads the initial terrain from input.txt
 * Terrain is simply a 2d array with height elements in each coordinate
 * The game lets you place 10 pieces of dirt before the flood of wealth comes in.
 * Then prints out the score which is proportional to the square root of the lakes' volumes.
 * @author : Berkay Bugra Gok
 * Id : 2021400258
 * @since : 28.04.2023
 */

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        // Input file has been started to be read, first the row and column count information
        File inputName = new File("input.txt");
        Scanner inputHandle = new Scanner(inputName);
        int[] rowAndColumn = new int[2];
        String firstLine = inputHandle.nextLine().trim();
        rowAndColumn[0] = Integer.parseInt(firstLine.split(" ")[0]); // 0th index is column
        rowAndColumn[1] = Integer.parseInt(firstLine.split(" ")[1]); // 1st index is row

        //Necessary primitive array will be created
        int[][] boardList2D = new int[rowAndColumn[1]][rowAndColumn[0]];
        for (int i = 0; i<rowAndColumn[1]; i++) { // for rows
            String currentLine = inputHandle.nextLine().trim();
            String[] currentList = currentLine.split(" ");
            for (int j = 0; j < currentList.length; j++) // for columns
                boardList2D[i][j] = Integer.parseInt(currentList[j]);
        }

        terrainPrinter(int2DToString2D(boardList2D),0); //initial state of the board is printed

        String[] alphabet = new String[26*27]; // Alphabet which will be used in the input and while printing the board
        for (char c = 'a'; c <= 'z'; c++) { // here the alphabet is created using char's and converting them to strings
            alphabet[c - 'a'] = String.valueOf(c);
        }
        int alphabetIndex = 26;
        for (char c1 = 'a'; c1 <= 'z'; c1++) { // 2-letter letter codes are added to the alphabet list
            for (char c2 = 'a'; c2 <= 'z'; c2++ ) {
                alphabet[alphabetIndex] = c1 + String.valueOf(c2);
                alphabetIndex++;
            }
        }
        
        //Inputs of the game is going to be taken from the user, 10 pieces of dirt is going to be placed
        for (int i = 0; i < 10; i++) { // 10 rights to place the dirt
            boolean isInputCorrect = false;
            String letterCode = "";
            String digitCode = "";

            do { //
                System.out.print("Add stone " + (i+1) + " / 10 to coordinate: ");
                Scanner userInput = new Scanner(System.in);
                String inputString = userInput.nextLine();
                boolean willBreak = false;

                if (!(inputString.equals(inputString.trim()))){ // if there are excessive spaces afterward
                    willBreak = true;
                }
                else if ((inputString.length() < 2) | (inputString.length() > 5)) {
                    willBreak = true;
                }
                else { // if our input is short enough to be proper, 5 or fewer chars
                    String isLetterCode = "";
                    String isDigitCode = "";

                    for (int character = 0; character < inputString.length(); character++) {
                        char currentCharacter = inputString.charAt(character);
                        // if the character is at the 0th index
                        if (character == 0) {
                            if ((currentCharacter >= 'a') & (currentCharacter <= 'z')) { // if the character is a lowercase letter
                                isLetterCode = isLetterCode + currentCharacter;
                            } else { // if the first character is not a lowercase letter
                                willBreak = true;
                            }
                        }
                        // if the character is at the 1st index
                        else if (character == 1) {
                            if ((currentCharacter >= 'a') & (currentCharacter <= 'z')) { // if the second char is a lowercase letter
                                isLetterCode = isLetterCode + currentCharacter;
                            } else if ((currentCharacter >= '0') & (currentCharacter <= '9')) { // if the second char is a number 1-9
                                isDigitCode = isDigitCode + currentCharacter;
                            } else {
                                willBreak = true;
                            }
                        } else if (character == 2) { // if the character is at the 2nd index, meaning that 3,4,5 chars long input
                            if (isDigitCode.length() > 0) { // if the second char was digit
                                if (!((currentCharacter >= '0') & (currentCharacter <= '9'))) { // if the number is not a digit 0-9
                                    willBreak = true;
                                } else { // if a single letter code and 2 digits code
                                    isDigitCode = isDigitCode + currentCharacter;
                                }
                            } else { // if it is a two-letter code input
                                if ((currentCharacter >= '0') & (currentCharacter <= '9')) { // if the third char is a number 1-9
                                    isDigitCode = isDigitCode + currentCharacter;
                                } else { // if it is a two-letter code input but 2nd index is not a digit
                                    willBreak = true;
                                }
                            }
                        } else if (character == 3) { // if the character is at the 3rd index, meaning that 4,5 chars long input
                            // one or two letters, at least one digit is taken as input
                            if (!((currentCharacter >= '0') & (currentCharacter <= '9'))) { // if the number is not a digit 0-9
                                willBreak = true;
                            } else {
                                isDigitCode = isDigitCode + currentCharacter;
                            }
                        } else { // if the character is at the 4th index, meaning that 5 chars long input
                            if (!((currentCharacter >= '0') & (currentCharacter <= '9'))) { // if the number is not a digit 0-9
                                willBreak = true;
                            } else {
                                isDigitCode = isDigitCode + currentCharacter;
                            }
                        }

                        if (willBreak) {
                            break;
                        }

                        else {
                            if (character == inputString.length()-1) { // if the last character has been checked and everything is proper
                                digitCode = isDigitCode;
                                letterCode = isLetterCode;
                                isInputCorrect = true;
                            }
                        }
                    }
                }

                try {
                    //input for the current dirt (i+1/10) is taken properly, now add it to the 2d list
                    int indexLetter = 0; // These are the indexes that the dirt is going to be placed on,
                    int indexDigit = Integer.parseInt(digitCode);

                    int searchIndex = 0;//
                    for (String letter :alphabet) { // indexLetter can't be found using binarySearch since the alphabet list is not sorted.
                        if (letter.equals(letterCode)) {
                            indexLetter = searchIndex;
                            break;
                        }
                        searchIndex++;
                    }
                    boardList2D[indexDigit][indexLetter] = boardList2D[indexDigit][indexLetter] + 1; //Dirt is placed,height +=1
                    terrainPrinter(int2DToString2D(boardList2D),1);
                    System.out.println("---------------");

                } catch (ArrayIndexOutOfBoundsException | NumberFormatException exceptions) {
                    isInputCorrect = false;
                }

                if (! isInputCorrect) { // if not in the correct format, print "Not a valid step!", and "Add stone " + i+1 + " / 10 to coordinate:"
                    System.out.println("Not a valid step!");
                }
            } while (! isInputCorrect);
        }

        // Dirt are placed, now find the lakes and volumes
        String[][] terrainStringList = int2DToString2D(boardList2D); // Our final terrain list converted to String elements

        int[][] neighboursList = {
                {-1,-1}, {-1,0}, {-1,+1}, {0,-1}, {0,+1}, {+1,-1}, {+1,0}, {+1,+1}
        };

        int[][] waterList = new int[terrainStringList.length][terrainStringList[0].length]; // Create a water list with the same size of our terrain
        int maxHeight = Integer.MIN_VALUE; // Maximum and minimum heights of the terrain will be found
        int minHeight = Integer.MAX_VALUE;
        for (int i = 0; i < boardList2D.length; i++) {
            for (int j = 0; j < boardList2D[0].length; j++) {
                if (boardList2D[i][j] > maxHeight)
                    maxHeight = boardList2D[i][j];
                if (boardList2D[i][j] < minHeight)
                    minHeight = boardList2D[i][j];
            }
        }
        for (int i = 1; i < waterList.length-1; i++) { // Our water list is filled up to the max height, except the borders
            for (int j = 1; j < waterList[0].length-1; j++) {
                waterList[i][j] = maxHeight-boardList2D[i][j];
            }
        }

        // Excessive water will be deleted layer by layer, for loop iterates maxHeight-minHeight times
        for (int waterDeletion = 0; waterDeletion < maxHeight-minHeight; waterDeletion++) { // for the layers of water
            for (int row = 1; row < waterList.length-1; row++) { // for rows of the water
                for (int col = 1; col < waterList[0].length -1; col++) { // for columns of the water
                    if (waterList[row][col] > 0) {
                        int currentUnitHeight = boardList2D[row][col] + waterList[row][col];
                        for (int[] relativeNeighbour : neighboursList) { // for neighbours relative to this coordinate
                            int currentNeighbourX = row + relativeNeighbour[0]; // current neighbours x and y coordinates are found
                            int currentNeighbourY = col + relativeNeighbour[1];
                            int currentNeighbourHeight = boardList2D[currentNeighbourX][currentNeighbourY] + waterList[currentNeighbourX][currentNeighbourY];
                            // if the neighbours total height is shorter than our unit's height,
                            // we want to remove a piece of water and break
                            if (currentNeighbourHeight < currentUnitHeight) {
                                waterList[row][col] -= 1;
                                break;
                            }
                        }
                    }
                }
            }
        }

        // Water lakes has been found with their height at each coordinate, waterList
        // Now we determine the lakes, and name them
        String[] capitalAlphabet = new String[26*27]; // Alphabet which will be used while printing the board
        for (char c = 'A'; c <= 'Z'; c++) { // here the alphabet is created using char's and converting them to strings
            capitalAlphabet[c - 'A'] = String.valueOf(c);
        }
        int capitalAlphabetIndex = 26;
        for (char c1 = 'A'; c1 <= 'Z'; c1++) { // 2-letter letter codes are added to the alphabet list
            for (char c2 = 'A'; c2 <= 'Z'; c2++ ) {
                capitalAlphabet[capitalAlphabetIndex] = c1 + String.valueOf(c2);
                capitalAlphabetIndex++;
            }
        }

        String[][] stringOutputTerrain = stringOutputTerrain(boardList2D,waterList,int2DToString2D(boardList2D),capitalAlphabet,0);
        terrainPrinter(stringOutputTerrain,1);

        ArrayList<Integer> volumesList = volumesFinder(waterList,neighboursList);
        double finalVolume = 0.0;
        for (int vol : volumesList) { // Find the score
            finalVolume += Math.sqrt(vol);
        }
        System.out.print((String.format("Final score: %.2f",finalVolume)).replace(",","."));
    }

    /**
     * @param array2D : it takes a 2d String list and prints the values
     * @param nonsenseConstant : it is created to fit the output into example outputs, to arrange the empty spaces after some lines.
     */
    public static void terrainPrinter(String[][] array2D, int nonsenseConstant) { // accepts a 2d terrain list, prints it according to the format
        String[] alphabet = new String[26*27];
        for (char c = 'a'; c <= 'z'; c++) { // here the alphabet is created using char's and converting them to strings
            alphabet[c - 'a'] = c + "";
        }
        int alphabetIndex = 26;
        for (char c1 = 'a'; c1 <= 'z'; c1++) { // 2-letter letter codes are added to the alphabet list
            for (char c2 = 'a'; c2 <= 'z'; c2++ ) {
                alphabet[alphabetIndex] = c1 + String.valueOf(c2);
                alphabetIndex++;
            }
        }
        // Printing operations differ from 1-10-100
        for (int i = 0; i< array2D.length; i++) { //iterates over rows
            // Indentation changes
            if (array2D.length < 11) { // for single digit indentation
                System.out.print("  " + i);
            }
            else if (array2D.length < 101) { // for two digit indentation
                if (i < 10) {
                    System.out.print("  " + i);
                }
                else {
                    System.out.print(" " + i);
                }
            }
            else if (array2D.length < 1000) { // for three digit indentation
                if (i < 10) {
                    System.out.print("  " + i);
                }
                else if (i<100){
                    System.out.print(" " + i);
                }
                else {
                    System.out.print(i);
                }
            }
            for (int j = 0; j< array2D[i].length; j++) {
                if (array2D[i][j].length() == 1)
                    System.out.print("  " + array2D[i][j]);
                else if (array2D[i][j].length() == 2)
                    System.out.print(" " + array2D[i][j]);
                if (j == array2D[0].length-1) { // if the end of the line
                    if (nonsenseConstant == 1)
                        System.out.print(" ");
                    else { // nonsenseConstant = 0; terrain will be painted for the first time, no extra space at the end of the 0th row
                        if (i != 0)
                            System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }

        System.out.print("     ");
        for (int i = 0; i < array2D[0].length; i++) { // alphabetic letter codes
            System.out.print(alphabet[i]);
            if (!(i == array2D[0].length-1)) {
                if (i <= 24)
                    System.out.print("  ");
                else if (i <=100) {
                    System.out.print(" ");
                }
            }
            if (i == array2D[0].length - 1) {
                System.out.print(" ");
                System.out.println();
            }
        }
    }

    /**
     * @param integer2DList : 2D integer list like our terrain list
     * @return : String version of the 2d integer list
     */
    public static String[][] int2DToString2D(int[][] integer2DList) {
        String[][] returnList = new String[integer2DList.length][integer2DList[0].length];
        for (int row = 0; row < integer2DList.length; row++) {
            for (int col = 0; col < integer2DList[0].length; col++) {
                returnList[row][col] = String.valueOf(integer2DList[row][col]);
            }
        }
        return returnList;
    }

    /**
     * Prints the board with capital letter coded lakes
     * @param board2dList : 2d integer list of the terrain
     * @param water2dList : 2d integer list of the water at each coordinate
     * @param currentOutputList : Output list
     * @param alphabetList : list that contains letter codes
     * @param letterCodeIndex : int variable to name the lakes
     * @return : output list
     */
    public static String[][] stringOutputTerrain(int[][] board2dList, int[][] water2dList, String[][] currentOutputList, String[] alphabetList, int letterCodeIndex) {
        int[][] neighboursList = {
                {-1,-1}, {-1,0}, {-1,+1}, {0,-1}, {0,+1}, {+1,-1}, {+1,0}, {+1,+1}
        };

        for (int i = 1; i < board2dList.length-1; i++) { // for all coordinates except the ones on the border
            for (int j = 1; j < board2dList[0].length-1; j++) {
                if ((water2dList[i][j] > 0) && (isNumeric(currentOutputList[i][j]))) { // if there is water in the current coordinate, and it hasn't been added to a lake before
                    currentOutputList[i][j] = alphabetList[letterCodeIndex]; // Uppercase letter code is given to the unit lake
                    lakeFinder(i,j,water2dList,currentOutputList,alphabetList,letterCodeIndex);
                    letterCodeIndex++;
                }
            }
        }
        return currentOutputList;
    }

    /**
     * Recursive function to find the volumes of the lakes
     * @param currentX : row number
     * @param currentY : column number
     * @param waterList : 2d water list
     * @param currentOutputList : Terrain list with named lakes
     * @param alphabetList : letter codes list
     * @param letterCodeIndex : int variable to name the lakes
     */
    public static void lakeFinder(int currentX, int currentY, int[][] waterList, String[][] currentOutputList, String[] alphabetList, int letterCodeIndex){
        int[][] neighboursList = {
                {-1,-1}, {-1,0}, {-1,+1}, {0,-1}, {0,+1}, {+1,-1}, {+1,0}, {+1,+1}
        };

        for (int[] relativeNeighbourCoordinates : neighboursList) { // original coordinate is named, look for all the neighbours
            // if the neighbour is also a part of the lake, name it the same letter.
            // and call the recursive naming function from that neighbour again
            int currentNeighbourX = currentX + relativeNeighbourCoordinates[0];
            int currentNeighbourY = currentY + relativeNeighbourCoordinates[1];
            if ((waterList[currentNeighbourX][currentNeighbourY] > 0) && !(currentOutputList[currentNeighbourX][currentNeighbourY].equals(alphabetList[letterCodeIndex]))) { // if the neighbour has water and is not named before
                currentOutputList[currentNeighbourX][currentNeighbourY] = alphabetList[letterCodeIndex];
                lakeFinder(currentNeighbourX,currentNeighbourY,waterList,currentOutputList,alphabetList,letterCodeIndex);
            }
        }
    }

    /**
     * @param strNum : checks if a string is made up of positive integers
     * @return : boolean value
     */
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
            if (d < 0) {
                return false;
            }
        } catch (NumberFormatException exception) {
            return false;
        }

        return true;
    }

    /**
     * @param waterList : our 2d list showing water volumes at each coordinate
     * @param neighboursList : adjacent and diagonal neighbours
     * @return : ArrayList of volumes
     */
    public static ArrayList<Integer> volumesFinder(int[][] waterList, int[][] neighboursList) {
        ArrayList<Integer> groups = new ArrayList<>();
        boolean[][] visited = new boolean[waterList.length][waterList[0].length];
        for (int i = 1; i < waterList.length - 1; i++) {
            for (int j = 1; j < waterList[0].length - 1; j++) {
                if (waterList[i][j] != 0 && !visited[i][j]) {
                    int sum = searchNeighbours(i, j, visited, waterList, neighboursList);
                    groups.add(sum);
                }
            }
        }
        return groups;
    }

    /**
     * @param x : x coordinate
     * @param y : y coordinate
     * @param visited : boolean 2d list to keep track of the added lakes
     * @param waterList : 2d water list with coordinates and volume values
     * @param neighboursList : 2d adjacent and diagonal neighbours
     * @return :
     */
    public static int searchNeighbours(int x, int y, boolean[][] visited, int[][] waterList, int[][] neighboursList) {
        visited[x][y] = true;
        int sum = waterList[x][y];
        for (int[] relativeNeighbour : neighboursList) {
            int neighbourX = x + relativeNeighbour[0];
            int neighbourY = y + relativeNeighbour[1];
            if (waterList[neighbourX][neighbourY] != 0 && !visited[neighbourX][neighbourY]) {
                sum += searchNeighbours(neighbourX, neighbourY, visited, waterList, neighboursList);
            }
        }
        return sum;
    }
}