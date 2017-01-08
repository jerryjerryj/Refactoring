package solving.deductors;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 08.01.17
 * Time: 5:01
 * To change this template use File | Settings | File Templates.
 */
public abstract class Deductor
{
    protected int[][] sequences;
    protected int length;
    protected int[] itemTrueCounts;
    protected int[][] matrix;
    protected int configurationCount;

    protected abstract int FindRowIndex(int index, int iterator);
    protected abstract int FindColumnIndex(int index, int iterator);

    protected void Deduct(int index, boolean[] checkNeeded)
    {
        int maxNumberSequences = sequences[index].length;

        int[] itemValues = new int[length];

        itemTrueCounts = new int[length];
        configurationCount = 0;

        int[] sequencesEndIndices = new int[maxNumberSequences];
        for (int j = 0; j < maxNumberSequences; j++)
            sequencesEndIndices[j] = DeductionAttributes.ILLEGAL_END_INDEX;

        DeductRecursive(index, 0, itemValues, sequencesEndIndices);

        if (configurationCount == 0)
            throw new RuntimeException("INTERNAL ERROR: configurations == 0");

        for (int i = 0; i < length; i++)
        {
            int row = FindRowIndex(index, i);
            int column = FindColumnIndex(index, i);
            if (itemTrueCounts[i] == 0 &&matrix[row][column] != DeductionAttributes.PUZZLE_FALSE)
            {
                checkNeeded[column] = true;
                matrix[row][column] = DeductionAttributes.PUZZLE_FALSE;
            }
            else if (itemTrueCounts[i] == configurationCount && matrix[row][column] != DeductionAttributes.PUZZLE_TRUE)
            {
                checkNeeded[column] = true;
                matrix[row][column] = DeductionAttributes.PUZZLE_TRUE;
            }
        }
    }
    private void DeductRecursive(int index, int sequenceIndex, int[] itemValues, int[] sequenceEndIndices)
    {
        int maxNumberSequences = sequences[index].length;
        if (sequenceIndex >= maxNumberSequences || sequences[index][sequenceIndex] == DeductionAttributes.PUZZLE_NO_SEQUENCE)
        {
            int falseStart = 0;
            if(sequenceIndex!=0)
                falseStart = sequenceEndIndices[sequenceIndex - 1] + 2;

            for (int i = falseStart; i < length; i++)
            {
                int row = FindRowIndex(index, i);
                int column = FindColumnIndex(index, i);
                if (matrix[row][column] == DeductionAttributes.PUZZLE_TRUE)
                    return;
                else itemValues[i] = DeductionAttributes.PUZZLE_TEMP_FALSE;
            }

            configurationCount++;
            for (int i = 0; i < length; i++)
            {
                if (itemValues[i] == DeductionAttributes.PUZZLE_TEMP_TRUE)
                    itemTrueCounts[i]++;
                else if (itemValues[i] != DeductionAttributes.PUZZLE_TEMP_FALSE)
                    throw new RuntimeException("INTERNAL ERROR: illegal item value");
            }
            return;
        }

        int sequenceLength = sequences[index][sequenceIndex];

        int sequenceLengthSum = 0;
        for (int i = sequenceIndex + 1; i < maxNumberSequences; i++)
        {
            int row = FindRowIndex(index, i);
            int column = FindColumnIndex(index, i);
            if (sequences[row][column] == DeductionAttributes.PUZZLE_NO_SEQUENCE)
                break;
            sequenceLengthSum += sequences[index][i] + 1;
        }
        int maxEndPosition = length - sequenceLengthSum - 1;

        int minStartPosition =0;
        if (sequenceIndex != 0)
            minStartPosition = sequenceEndIndices[sequenceIndex - 1] + 2;

        int minEndPosition = minStartPosition + sequenceLength - 1;
        if (minEndPosition > maxEndPosition)
            throw new RuntimeException("ERROR: No space for all sequences!");
        outer:
        for (int endPosition = minEndPosition; endPosition <= maxEndPosition; endPosition++) {
            int startPosition = endPosition - sequenceLength + 1;
            if (endPosition != minEndPosition) {
                int undoIndex = startPosition - 1;
                int row = FindRowIndex(index, undoIndex);
                int column = FindColumnIndex(index, undoIndex);
                if (matrix[row][column] == DeductionAttributes.PUZZLE_TRUE) break outer;
                else itemValues[undoIndex] = DeductionAttributes.PUZZLE_TEMP_FALSE;
            }
            for (int i = startPosition; i <= endPosition ; i++)
            {
                int row = FindRowIndex(index, i);
                int column = FindColumnIndex(index, i);
                if (matrix[row][column] == DeductionAttributes.PUZZLE_FALSE)
                {
                    int nextPossibleEndPosition = i + sequenceLength;
                    for (int j = startPosition; j <= i - 1; j++){
                        int r = FindRowIndex(index, j);
                        int c = FindColumnIndex(index, j);
                        if (matrix[r][c] == DeductionAttributes.PUZZLE_TRUE)
                            break outer;
                        else itemValues[j] = DeductionAttributes.PUZZLE_TEMP_FALSE;
                    }
                    endPosition = nextPossibleEndPosition - 1;
                    continue outer;
                }
                else itemValues[i] = DeductionAttributes.PUZZLE_TEMP_TRUE;
            }
            if (endPosition < length - 1) {
                int row = FindRowIndex(index, endPosition+1);
                int column = FindColumnIndex(index, endPosition+1);
                if (matrix[row][column] == DeductionAttributes.PUZZLE_TRUE) continue outer;
                else itemValues[endPosition + 1] = DeductionAttributes.PUZZLE_TEMP_FALSE;
            }
            sequenceEndIndices[sequenceIndex] = endPosition;
            DeductRecursive(index, sequenceIndex + 1, itemValues, sequenceEndIndices);
        }
    }
}
