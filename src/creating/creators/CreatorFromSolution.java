package creating.creators;

import creating.Puzzle;
import creating.PuzzleAttributes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class CreatorFromSolution extends PuzzleCreator
{
    public CreatorFromSolution(BufferedInputStream stream)
    {
        super(stream);
    }
    public Puzzle CreatePuzzle() throws IOException
    {
        ArrayList<ArrayList<Boolean>> matrix = new ArrayList< ArrayList<Boolean> >();
        boolean newRowStarts = true;
        int c;
        while ( (c = stream.read()) >= 0) {
            if (newRowStarts) {
                if (c == PuzzleAttributes.PUZZLE_MATRIX_TRUE_CHAR ||
                        c == PuzzleAttributes.PUZZLE_MATRIX_FALSE_CHAR) {
                    matrix.add(new ArrayList<Boolean>());
                    newRowStarts = false;
                }
            }

            if (c == PuzzleAttributes.PUZZLE_MATRIX_TRUE_CHAR) {
                matrix.get(matrix.size() - 1).add(true);
            }
            else if (c == PuzzleAttributes.PUZZLE_MATRIX_FALSE_CHAR) {
                matrix.get(matrix.size() - 1).add(false);
            }
            else if (c == '\n') {
                newRowStarts = true;
            }
            else if (c == ' ') {
				/* nothing */
            }
            else if (c == '\t') {
				/* nothing */
            }
            else if (c == '\r') { // carriage return
				/* nothing */
            }
            else throw new RuntimeException("ERROR: illegal input character: (" + ((char) c) + ")");
        }

        int rowCount = matrix.size();
        int colCount = -1;
        for (int row = 0; row < rowCount; row++) {
            if (colCount < 0) colCount = matrix.get(row).size();
            else if (matrix.get(row).size() != colCount) throw new RuntimeException("ERROR: uneven input matrix rows");
        }
        if (colCount < 0) throw new RuntimeException("ERROR: Empty input matrix");

        boolean[][] puzzleMatrix = new boolean[rowCount][colCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < colCount; col++) {
                puzzleMatrix[row][col] = matrix.get(row).get(col);
            }
        }

        return createPuzzleFromSolution(puzzleMatrix);
    }


    public static Puzzle createPuzzleFromSolution(boolean[][] solution) {
        if (solution == null) throw new RuntimeException("ERROR: solution is null");
        if (solution.length == 0)  throw new RuntimeException("ERROR: solution has no rows (empty)!");

        ArrayList<Byte> sequences = new ArrayList<Byte>();

        int numRows = solution.length;
        int numCols = solution[0].length;

        // add row sequences
        for (int row = 0; row < numRows; row++) {
            sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_ROW_SYMBOL_CHAR); // row symbol
            sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);  // separator

            int sequenceLengthCounter = 0;
            boolean insideSequence = false;
            for (int col = 0; col < numCols; col++) {
                if (solution[row][col]) {
                    if (!insideSequence) {
                        insideSequence = true;
                    }
                    sequenceLengthCounter++;
                }
                else { // solution[row][col] == false
                    if (insideSequence) {
                        // end of one sequence
                        byte[] numBytes = new Integer(sequenceLengthCounter).toString().getBytes();

                        for (int b = 0; b < numBytes.length; b++) {
                            sequences.add(numBytes[b]);
                        }
                        sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);
                        sequenceLengthCounter = 0;
                        insideSequence = false;
                    }
                }
            }
            if (insideSequence) {
                // end of last sequence on this row
                byte[] numBytes = new Integer(sequenceLengthCounter).toString().getBytes();

                for (int b = 0; b < numBytes.length; b++) {
                    sequences.add(numBytes[b]);
                }
                sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);
                sequenceLengthCounter = 0;
                insideSequence = false;
            }
        }

        // add column sequences
        for (int col = 0; col < numCols; col++) {
            sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_COLUMN_SYMBOL_CHAR); // row symbol
            sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);  // separator

            int sequenceLengthCounter = 0;
            boolean insideSequence = false;
            for (int row = 0; row < numRows; row++) {
                if (solution[row][col]) {
                    if (!insideSequence) {
                        insideSequence = true;
                    }
                    sequenceLengthCounter++;
                }
                else { // solution[row][col] == false
                    if (insideSequence) {
                        // end of one sequence
                        byte[] numBytes = new Integer(sequenceLengthCounter).toString().getBytes();

                        for (int b = 0; b < numBytes.length; b++) {
                            sequences.add(numBytes[b]);
                        }
                        sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);
                        sequenceLengthCounter = 0;
                        insideSequence = false;
                    }
                }
            }
            if (insideSequence) {
                byte[] numBytes = new Integer(sequenceLengthCounter).toString().getBytes();

                for (int b = 0; b < numBytes.length; b++) {
                    sequences.add(numBytes[b]);
                }
                sequences.add((byte) PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);
                sequenceLengthCounter = 0;
                insideSequence = false;
            }
        }

        byte[] sequenceBytes = new byte[sequences.size()];
        for (int i = 0; i < sequences.size(); i++) {
            sequenceBytes[i] = sequences.get(i);
        }
        String sequenceString = new String(sequenceBytes);

        return createPuzzleFromSequences(sequenceString);
    }




}
