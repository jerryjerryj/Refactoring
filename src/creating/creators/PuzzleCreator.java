package creating.creators;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

import creating.Puzzle;
import creating.PuzzleAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class PuzzleCreator
{
    protected BufferedInputStream stream;

    public PuzzleCreator(BufferedInputStream stream)
    {
        this.stream = stream;
    }
    public abstract Puzzle CreatePuzzle() throws IOException;
    protected static Puzzle createPuzzleFromSequences(String sequences) {
        if (sequences == null) throw new NullPointerException("INTERNAL ERROR: Puzzle string is null");

        String[] tokens = sequences.split(PuzzleAttributes.PUZZLE_STRING_SEPARATOR);
        if (tokens.length < 2) {
            throw new RuntimeException(
                    "ERROR: Invalid puzzle string: too few parameters");
        }

        ArrayList<ArrayList<Integer>> rowSequences = new ArrayList< ArrayList<Integer> >();
        ArrayList< ArrayList<Integer> > colSequences = new ArrayList< ArrayList<Integer> >();
        int tokenIndex = -1;
        boolean endOfRows = false;
        while (true) {
            tokenIndex++;
            if (tokenIndex >= tokens.length) break;

            String str = tokens[tokenIndex];
            int inputType;
            int inputValue = -1;
            if (str.equals(PuzzleAttributes.PUZZLE_STRING_ROW_SYMBOL_STRING)) {
                inputType = PuzzleAttributes.INPUT_TYPE_ROW_SYMBOL;
            }
            else if (str.equals(PuzzleAttributes.PUZZLE_STRING_COLUMN_SYMBOL_STRING)) {
                inputType = PuzzleAttributes.INPUT_TYPE_COLUMN_SYMBOL;
            }
            else {
                inputType = PuzzleAttributes.INPUT_TYPE_NUMBER;
                try { inputValue = Integer.parseInt(str); }
                catch(NumberFormatException nfe) { throw new RuntimeException(nfe); }
            }


            if (inputType == PuzzleAttributes.INPUT_TYPE_ROW_SYMBOL) {
                if (endOfRows) {
                    throw new RuntimeException("ERROR: a row symbol among columns");
                }
                rowSequences.add(new ArrayList<Integer>());
            }
            else if (inputType == PuzzleAttributes.INPUT_TYPE_COLUMN_SYMBOL) {
                endOfRows = true;
                colSequences.add(new ArrayList<Integer>());
            }
            else if (inputType == PuzzleAttributes.INPUT_TYPE_NUMBER) {
                if (inputValue == 0) throw new RuntimeException(
                        "ERROR: zero sequence length!");
                else if (inputValue < 0) throw new RuntimeException(
                        "INTERNAL ERROR: negative sequence length!");

                if (endOfRows) {
                    int lastColIndex = colSequences.size() - 1;
                    if (lastColIndex < 0) {
                        throw new RuntimeException("ERROR: Missing column symbol before sequences!");
                    }
                    colSequences.get(lastColIndex).add(inputValue);
                }
                else {
                    int lastRowIndex = rowSequences.size() - 1;
                    if (lastRowIndex < 0) {
                        throw new RuntimeException("ERROR: Missing row symbol before sequences!");
                    }
                    rowSequences.get(lastRowIndex).add(inputValue);
                }
            }
            else {
                throw new RuntimeException("INTERNAL ERROR: illegal puzzle string token type.");
            }
        }
        int numRows = rowSequences.size();
        int numCols = colSequences.size();
        int maxRowSequenceCount = -1;
        for (int r = 0; r < numRows; r++) {
            int s = rowSequences.get(r).size();
            if (s > maxRowSequenceCount) maxRowSequenceCount = s;
        }
        int maxColSequenceCount = -1;
        for (int c = 0; c < numCols; c++) {
            int s = colSequences.get(c).size();
            if (s > maxColSequenceCount) maxColSequenceCount = s;
        }
        int[][] puzzleRowSequences = new int[numRows][maxRowSequenceCount];
        for (int r = 0; r < numRows; r++) {
            for (int s = 0; s < maxRowSequenceCount; s++) {
                int seqValue;
                if (s < rowSequences.get(r).size()) seqValue = rowSequences.get(r).get(s);
                else seqValue = PuzzleAttributes.PUZZLE_NO_SEQUENCE;
                puzzleRowSequences[r][s] = seqValue;
            }
        }
        int[][] puzzleColSequences = new int[numCols][maxColSequenceCount];
        for (int c = 0; c < numCols; c++) {
            for (int s = 0; s < maxColSequenceCount; s++) {
                int seqValue;
                if (s < colSequences.get(c).size()) seqValue = colSequences.get(c).get(s);
                else seqValue = PuzzleAttributes.PUZZLE_NO_SEQUENCE;
                puzzleColSequences[c][s] = seqValue;
            }
        }
        int rowSums = sequenceSum(puzzleRowSequences);
        int colSums = sequenceSum(puzzleColSequences);
        if (rowSums != colSums) {
            throw new IllegalArgumentException(
                    "Mismatch in row sums and column sums: " + rowSums + " vs " + colSums);
        }

        return new Puzzle(puzzleRowSequences, puzzleColSequences);
    }

    private static int sequenceSum(int[][] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                int val = array[i][j];
                sum += (val > 0 ? val : 0);
            }
        }
        return sum;
    }
}
