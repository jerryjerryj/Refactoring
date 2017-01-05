package solving;

import creating.Puzzle;
import creating.PuzzleAttributes;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */
public class Solver {

    private int numRows;
    private int numCols;
    private int deductedItems;
    private int configurationCount = 0;
    int[] rowItemTrueCounts;
    int[] colItemTrueCounts;
    private Puzzle puzzle;
    public Solver(Puzzle puzzle)
    {
        this.puzzle = puzzle;
        numRows = puzzle.getRowSequences().length;
        numCols = puzzle.getColSequences().length;
    }

    public ArrayList< int[][] > solveDeduction()
    {
        int[][] matrix = new int[numRows][numCols];
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                matrix[r][c] = PuzzleAttributes.PUZZLE_INITIAL_VALUE;
            }
        }

        boolean[] checkNeedRows = new boolean[numRows];
        boolean[] checkNeedCols = new boolean[numCols];
        for (int r = 0; r < numRows; r++) checkNeedRows[r] = true;
        for (int c = 0; c < numCols; c++) checkNeedCols[c] = true;

        deductedItems = 0;
        int prevDeductedItems = -1;
        boolean deductionReady = false;
        while ( !deductionReady ) {
            deductionReady = true;

            if (deductedItems > prevDeductedItems) {
                prevDeductedItems = deductedItems;
                System.err.println("Deducted: " + deductedItems + "/" + (numRows * numCols));
            }
            for (int r = 0; r < numRows; r++) {
                System.err.print("_");
                if (checkNeedRows[r]) {
                    deductionReady = false;
                    deductRow(r, matrix, checkNeedCols);
                    checkNeedRows[r] = false;
                }
            }

            // all columns:
            for (int c = 0; c < numCols; c++) {
                System.err.print("|");
                if (checkNeedCols[c]) {
                    deductionReady = false;
                    deductColumn(c, matrix, checkNeedRows);
                    checkNeedCols[c] = false;
                }
            }
        }

        // check whether all values are legal:
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (matrix[r][c] != PuzzleAttributes.PUZZLE_TRUE &&
                        matrix[r][c] != PuzzleAttributes.PUZZLE_FALSE) {
                    return null;
                }
            }
        }

        ArrayList< int[][] > solution = new ArrayList< int[][] >();
        solution.add(matrix);

        return solution;
    }
    private void deductRow(int rowIndex, int[][] matrix, boolean[] checkNeedCols) {
        int maxNumRowSeqs = puzzle.getRowSequences()[rowIndex].length;

        int[] rowItemValues = new int[numCols];
        for (int c = 0; c < numCols; c++) {
            rowItemValues[c] = PuzzleAttributes.PUZZLE_INITIAL_VALUE;
        }
        rowItemTrueCounts = new int[numCols]; // init: zeros
        configurationCount = 0;

        int[] rowSeqEndIndices = new int[maxNumRowSeqs];
        for (int j = 0; j < maxNumRowSeqs; j++) {
            rowSeqEndIndices[j] = PuzzleAttributes.ILLEGAL_END_INDEX;
        }

        deductRowRecursive(rowIndex, 0, rowItemValues, matrix, rowSeqEndIndices);

        if (configurationCount == 0) throw new RuntimeException("INTERNAL ERROR: configurations == 0");

        for (int c = 0; c < numCols; c++) {
            if (rowItemTrueCounts[c] == 0) {
                if (matrix[rowIndex][c] != PuzzleAttributes.PUZZLE_FALSE) {
                    checkNeedCols[c] = true;
                    matrix[rowIndex][c] = PuzzleAttributes.PUZZLE_FALSE;
                    deductedItems++;
                }
            }
            else if (rowItemTrueCounts[c] == configurationCount) {
                if (matrix[rowIndex][c] != PuzzleAttributes.PUZZLE_TRUE) {
                    checkNeedCols[c] = true;
                    matrix[rowIndex][c] = PuzzleAttributes.PUZZLE_TRUE;
                    deductedItems++;
                }
            }
        }
    }


    private void deductColumn(int colIndex, int[][] matrix, boolean[] checkNeedRows)
    {
        int maxNumColSeqs = puzzle.getColSequences()[colIndex].length;

        int[] colItemValues = new int[numRows];
        for (int r = 0; r < numRows; r++) {
            colItemValues[r] = PuzzleAttributes.PUZZLE_INITIAL_VALUE;
        }
        colItemTrueCounts = new int[numRows];
        configurationCount = 0;

        int[] colSeqEndIndices = new int[maxNumColSeqs];
        for (int j = 0; j < maxNumColSeqs; j++) {
            colSeqEndIndices[j] = PuzzleAttributes.ILLEGAL_END_INDEX;
        }

        deductColRecursive(colIndex, 0, colItemValues, matrix, colSeqEndIndices);

        if (configurationCount == 0) throw new RuntimeException("INTERNAL ERROR: configurations == 0");

        for (int r = 0; r < numRows; r++) {
            if (colItemTrueCounts[r] == 0) {
                if (matrix[r][colIndex] != PuzzleAttributes.PUZZLE_FALSE) {
                    checkNeedRows[r] = true;
                    matrix[r][colIndex] = PuzzleAttributes.PUZZLE_FALSE;
                    deductedItems++;
                }
            }
            else if (colItemTrueCounts[r] == configurationCount) {
                if (matrix[r][colIndex] != PuzzleAttributes.PUZZLE_TRUE) {
                    checkNeedRows[r] = true;
                    matrix[r][colIndex] = PuzzleAttributes.PUZZLE_TRUE;
                    deductedItems++;
                }
            }
        }
    }

    private void deductRowRecursive(int rowIndex, int rowSeqIndex, int[] rowItemValues, int[][] matrix, int[] rowSeqEndIndices)
    {
         int maxNumRowSeqs = puzzle.getRowSequences()[rowIndex].length;
        if (rowSeqIndex >= maxNumRowSeqs || puzzle.getRowSequences()[rowIndex][rowSeqIndex] == PuzzleAttributes.PUZZLE_NO_SEQUENCE) {
            int falseStart;
            if (rowSeqIndex == 0) falseStart = 0;
            else falseStart = rowSeqEndIndices[rowSeqIndex - 1] + 2;

            for (int i = falseStart; i < numCols; i++) {
                if (matrix[rowIndex][i] == PuzzleAttributes.PUZZLE_TRUE) return;
                else rowItemValues[i] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
            }
            this.configurationCount++;
            for (int c = 0; c < numCols; c++) {
                if (rowItemValues[c] == PuzzleAttributes.PUZZLE_TEMP_TRUE) {
                    rowItemTrueCounts[c]++;
                }
                else if (rowItemValues[c] != PuzzleAttributes.PUZZLE_TEMP_FALSE)
                    throw new RuntimeException("INTERNAL ERROR: illegal rowItem value");
            }
            return;
        }

        int sequenceLength = puzzle.getRowSequences()[rowIndex][rowSeqIndex];

        int maxEndPosition;
        int seqLengthSum = 0;
        for (int s = rowSeqIndex + 1; s < maxNumRowSeqs; s++) {
            if (puzzle.getRowSequences()[rowIndex][s] == PuzzleAttributes.PUZZLE_NO_SEQUENCE) break;
            seqLengthSum += puzzle.getRowSequences()[rowIndex][s] + 1;
        }
        maxEndPosition = numCols - seqLengthSum - 1;

        int minStartPosition;
        if (rowSeqIndex == 0) minStartPosition = 0;
        else minStartPosition = rowSeqEndIndices[rowSeqIndex - 1] + 2;
        int minEndPosition = minStartPosition + sequenceLength - 1;
        if (minEndPosition > maxEndPosition) {
            throw new RuntimeException("ERROR: No space for all sequences!");
        }
        outer:
        for (int endPos = minEndPosition; endPos <= maxEndPosition; endPos++) {
            int startPos = endPos - sequenceLength + 1;
            if (endPos != minEndPosition) {
                int undoIndex = startPos - 1;
                if (matrix[rowIndex][undoIndex] == PuzzleAttributes.PUZZLE_TRUE) break outer;
                else rowItemValues[undoIndex] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
            }
            for (int i = startPos; i <= endPos ; i++) {
                if (matrix[rowIndex][i] == PuzzleAttributes.PUZZLE_FALSE) {
                    int nextPossibleEndPos = i + sequenceLength;
                    for (int j = startPos; j <= i - 1; j++) {
                        if (matrix[rowIndex][j] == PuzzleAttributes.PUZZLE_TRUE) {
                            break outer;
                        }
                        else rowItemValues[j] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
                    }
                    endPos = nextPossibleEndPos - 1;
                    continue outer;
                }
                else rowItemValues[i] = PuzzleAttributes.PUZZLE_TEMP_TRUE;
            }
            if (endPos < numCols - 1) {
                if (matrix[rowIndex][endPos + 1] == PuzzleAttributes.PUZZLE_TRUE) continue outer;
                else rowItemValues[endPos + 1] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
            }
            rowSeqEndIndices[rowSeqIndex] = endPos;
            this.deductRowRecursive(rowIndex, rowSeqIndex + 1, rowItemValues, matrix, rowSeqEndIndices);
        }
    }

    private void deductColRecursive(int colIndex, int colSeqIndex, int[] colItemValues, int[][] matrix, int[] colSeqEndIndices) {
        int maxNumColSeqs = puzzle.getColSequences()[colIndex].length;
        if (colSeqIndex >= maxNumColSeqs || puzzle.getColSequences()[colIndex][colSeqIndex] == PuzzleAttributes.PUZZLE_NO_SEQUENCE) {
            // add 'false's to the end of the column
            int falseStart; // first index to add 'false's to
            if (colSeqIndex == 0) falseStart = 0;
            else falseStart = colSeqEndIndices[colSeqIndex - 1] + 2;

            for (int i = falseStart; i < numRows; i++) {
                if (matrix[i][colIndex] == PuzzleAttributes.PUZZLE_TRUE) return;
                else colItemValues[i] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
            }

            // current column ready
            this.configurationCount++;
            for (int r = 0; r < numRows; r++) {
                if (colItemValues[r] == PuzzleAttributes.PUZZLE_TEMP_TRUE) {
                    colItemTrueCounts[r]++;
                }
                else if (colItemValues[r] == PuzzleAttributes.PUZZLE_TEMP_FALSE) {
                    // do nothing
                }
                else throw new RuntimeException("INTERNAL ERROR: illegal colItem value");
            }
            return;
        }

        int sequenceLength = puzzle.getColSequences()[colIndex][colSeqIndex];

        int maxEndPosition; // last index where this sequence may end
        int seqLengthSum = 0;
        for (int s = colSeqIndex + 1; s < maxNumColSeqs; s++) {
            if (puzzle.getColSequences()[colIndex][s] == PuzzleAttributes.PUZZLE_NO_SEQUENCE) break;

            // sequence length plus mandatory preceding 'false'
            seqLengthSum += puzzle.getColSequences()[colIndex][s] + 1;
        }
        maxEndPosition = numRows - seqLengthSum - 1;

        int minStartPosition; // from what index current sequence is starting from?
        if (colSeqIndex == 0) minStartPosition = 0; // first sequence on the column
        else minStartPosition = colSeqEndIndices[colSeqIndex - 1] + 2;

        int minEndPosition = minStartPosition + sequenceLength - 1;

        // is there room for this sequence (and the others)?
        if (minEndPosition > maxEndPosition) {
            // no room!
            throw new RuntimeException("ERROR: No space for all sequences!");
        }

        // browse through all possible placements
        outer: // label for 'for' loop
        for (int endPos = minEndPosition; endPos <= maxEndPosition; endPos++) {
            int startPos = endPos - sequenceLength + 1;

            // undo previous placement of this sequence
            if (endPos != minEndPosition) { // not first placement?
                // Undo the first 'true' of PRECEDING sequence position.
                // It is the item just before first 'true' of current sequence position.
                int undoIndex = startPos - 1;

                if (matrix[undoIndex][colIndex] == PuzzleAttributes.PUZZLE_TRUE) break outer;
                else colItemValues[undoIndex] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
            }

            // add sequence (new placement):
            for (int i = startPos; i <= endPos ; i++) {
                if (matrix[i][colIndex] == PuzzleAttributes.PUZZLE_FALSE) {
                    // Optimization starts -->
                    // It is impossible to have 'true' now in (i, colIndex).
                    // Skip a few impossible sequence positions.
                    int nextPossibleEndPos = i + sequenceLength;
                    for (int j = startPos; j <= i - 1; j++) {
                        if (matrix[j][colIndex] == PuzzleAttributes.PUZZLE_TRUE) {
                            // It is impossible to have 'false' now in (j, colIndex).
                            // Further position of this sequence won't help.
                            break outer;
                        }
                        else colItemValues[j] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
                    }
                    endPos = nextPossibleEndPos - 1; // minus one to counter subsequent "++" in 'for' loop
                    // <-- Optimization ends
                    continue outer;
                }
                else colItemValues[i] = PuzzleAttributes.PUZZLE_TEMP_TRUE;
            }
            // add a succeeding 'false', if not yet at the end of the column.
            if (endPos < numRows - 1) {
                if (matrix[endPos + 1][colIndex] == PuzzleAttributes.PUZZLE_TRUE) continue outer;
                else colItemValues[endPos + 1] = PuzzleAttributes.PUZZLE_TEMP_FALSE;
            }

            // update ending index for this sequence (new ending index)
            colSeqEndIndices[colSeqIndex] = endPos;

            // recursive call:
            this.deductColRecursive(colIndex, colSeqIndex + 1, colItemValues, matrix, colSeqEndIndices);
        }
    }


}
