package solving;

import creating.Puzzle;
import creating.PuzzleAttributes;
import solving.deductors.ColumnDeductor;
import solving.deductors.DeductionAttributes;
import solving.deductors.RowDeductor;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */
public class Solver
{

    private int numRows;
    private int numCols;
    private int deductedItems = 0;
    private int configurationCount = 0;
    int[] colItemTrueCounts;
    private Puzzle puzzle;
    public Solver(Puzzle puzzle)
    {
        this.puzzle = puzzle;
        numRows = puzzle.getRowSequences().length;
        numCols = puzzle.getColumnSequences().length;
    }

    public int[][] solveDeduction()
    {
        int[][] matrix = new int[numRows][numCols];
        RowDeductor rowDeductor = new RowDeductor(puzzle,matrix);
        ColumnDeductor columnDeductor = new ColumnDeductor(puzzle,matrix);

        boolean[] checkNeedRows = new boolean[numRows];
        boolean[] checkNeedColumns = new boolean[numCols];
        Arrays.fill(checkNeedRows,true);
        Arrays.fill(checkNeedColumns,true);

        int prevDeductedItems = -1;
        boolean deductionReady = false;
        while ( !deductionReady ) {
            deductionReady = true;

            if (deductedItems > prevDeductedItems)
                prevDeductedItems = deductedItems;
            for (int r = 0; r < numRows; r++)
            {
                if (checkNeedRows[r])
                {
                    deductionReady = false;
                    rowDeductor.Deduct(r,checkNeedColumns,configurationCount);
                    checkNeedRows[r] = false;
                }
            }

            for (int c = 0; c < numCols; c++)
            {
                if (checkNeedColumns[c]) {
                    deductionReady = false;
                    //columnDeductor.Deduct(c,checkNeedRows,configurationCount);
                    deductColumn(c, matrix, checkNeedRows);
                    //rowDeductor1.Deduct(c,checkNeedRows,configurationCount);
                    checkNeedColumns[c] = false;
                }
            }
        }
        if(isValuesCorrect(matrix))
            return matrix;
        else return null;
    }
    private boolean isValuesCorrect(int[][] matrix)
    {
        for (int r = 0; r < numRows; r++)
            for (int c = 0; c < numCols; c++)
                if (matrix[r][c] != DeductionAttributes.PUZZLE_TRUE && matrix[r][c] != DeductionAttributes.PUZZLE_FALSE)
                    return false;
        return true;
    }

    private void deductColumn(int colIndex, int[][] matrix, boolean[] checkNeedRows)
    {
        int maxNumColSeqs = puzzle.getColumnSequences()[colIndex].length;

        int[] colItemValues = new int[numRows];
        for (int r = 0; r < numRows; r++) {
            colItemValues[r] = DeductionAttributes.PUZZLE_INITIAL_VALUE;
        }
        colItemTrueCounts = new int[numRows];
        configurationCount = 0;

        int[] colSeqEndIndices = new int[maxNumColSeqs];
        for (int j = 0; j < maxNumColSeqs; j++) {
            colSeqEndIndices[j] = DeductionAttributes.ILLEGAL_END_INDEX;
        }

        deductColRecursive(colIndex, 0, colItemValues, matrix, colSeqEndIndices);
                //col
        if (configurationCount == 0) throw new RuntimeException("INTERNAL ERROR: configurations == 0");

        for (int r = 0; r < numRows; r++) {
            if (colItemTrueCounts[r] == 0 && matrix[r][colIndex] != DeductionAttributes.PUZZLE_FALSE) {
                    checkNeedRows[r] = true;
                    matrix[r][colIndex] = DeductionAttributes.PUZZLE_FALSE;
                    deductedItems++;
            }
            else if (colItemTrueCounts[r] == configurationCount && matrix[r][colIndex] != DeductionAttributes.PUZZLE_TRUE)
            {
                checkNeedRows[r] = true;
                matrix[r][colIndex] = DeductionAttributes.PUZZLE_TRUE;
                deductedItems++;
            }
        }
    }


    private void deductColRecursive(int colIndex, int colSeqIndex, int[] colItemValues, int[][] matrix, int[] colSeqEndIndices) {
        int maxNumColSeqs = puzzle.getColumnSequences()[colIndex].length;
        if (colSeqIndex >= maxNumColSeqs || puzzle.getColumnSequences()[colIndex][colSeqIndex] == DeductionAttributes.PUZZLE_NO_SEQUENCE) {
            // add 'false's to the end of the column
            int falseStart; // first index to add 'false's to
            if (colSeqIndex == 0) falseStart = 0;
            else falseStart = colSeqEndIndices[colSeqIndex - 1] + 2;

            for (int i = falseStart; i < numRows; i++) {
                if (matrix[i][colIndex] == DeductionAttributes.PUZZLE_TRUE) return;
                else colItemValues[i] = DeductionAttributes.PUZZLE_TEMP_FALSE;
            }

            // current column ready
            configurationCount++;
            for (int r = 0; r < numRows; r++) {
                if (colItemValues[r] == DeductionAttributes.PUZZLE_TEMP_TRUE) {
                    colItemTrueCounts[r]++;
                }
                else if (colItemValues[r] != DeductionAttributes.PUZZLE_TEMP_FALSE) {
                    throw new RuntimeException("INTERNAL ERROR: illegal colItem value");
                }
            }
            return;
        }

        int sequenceLength = puzzle.getColumnSequences()[colIndex][colSeqIndex];

        int maxEndPosition; // last index where this sequence may end
        int seqLengthSum = 0;
        for (int s = colSeqIndex + 1; s < maxNumColSeqs; s++) {
            if (puzzle.getColumnSequences()[colIndex][s] == DeductionAttributes.PUZZLE_NO_SEQUENCE) break;

            // sequence length plus mandatory preceding 'false'
            seqLengthSum += puzzle.getColumnSequences()[colIndex][s] + 1;
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

                if (matrix[undoIndex][colIndex] == DeductionAttributes.PUZZLE_TRUE) break outer;
                else colItemValues[undoIndex] = DeductionAttributes.PUZZLE_TEMP_FALSE;
            }

            // add sequence (new placement):
            for (int i = startPos; i <= endPos ; i++) {
                if (matrix[i][colIndex] == DeductionAttributes.PUZZLE_FALSE) {
                    // Optimization starts -->
                    // It is impossible to have 'true' now in (i, colIndex).
                    // Skip a few impossible sequence positions.
                    int nextPossibleEndPos = i + sequenceLength;
                    for (int j = startPos; j <= i - 1; j++) {
                        if (matrix[j][colIndex] == DeductionAttributes.PUZZLE_TRUE) {
                            // It is impossible to have 'false' now in (j, colIndex).
                            // Further position of this sequence won't help.
                            break outer;
                        }
                        else colItemValues[j] = DeductionAttributes.PUZZLE_TEMP_FALSE;
                    }
                    endPos = nextPossibleEndPos - 1; // minus one to counter subsequent "++" in 'for' loop
                    // <-- Optimization ends
                    continue outer;
                }
                else colItemValues[i] = DeductionAttributes.PUZZLE_TEMP_TRUE;
            }
            // add a succeeding 'false', if not yet at the end of the column.
            if (endPos < numRows - 1) {
                if (matrix[endPos + 1][colIndex] == DeductionAttributes.PUZZLE_TRUE) continue outer;
                else colItemValues[endPos + 1] = DeductionAttributes.PUZZLE_TEMP_FALSE;
            }

            // update ending index for this sequence (new ending index)
            colSeqEndIndices[colSeqIndex] = endPos;

            // recursive call:
            this.deductColRecursive(colIndex, colSeqIndex + 1, colItemValues, matrix, colSeqEndIndices);
        }
    }


}
