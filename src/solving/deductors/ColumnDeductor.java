package solving.deductors;

import creating.Puzzle;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 08.01.17
 * Time: 3:17
 * To change this template use File | Settings | File Templates.
 */
public class ColumnDeductor extends Deductor
{
    public ColumnDeductor(Puzzle puzzle, int[][] matrix)
    {
        sequences = puzzle.getColumnSequences();
        length = puzzle.getRowSequences().length;
        this.matrix = matrix;
    }
    public void Deduct(int columnIndex, boolean[] checkNeededRows, int configurationCount)
    {
        this.configurationCount = configurationCount;
        Deduct(columnIndex, checkNeededRows);
    }
    public int FindRowIndex(int index, int iterator)
    {
        return iterator;
    }
    public int FindColumnIndex(int index, int iterator)
    {
        return  index;
    }

}
