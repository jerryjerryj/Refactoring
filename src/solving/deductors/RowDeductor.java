package solving.deductors;

import creating.Puzzle;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 08.01.17
 * Time: 3:17
 * To change this template use File | Settings | File Templates.
 */
public class RowDeductor  extends Deductor
{
    public RowDeductor(Puzzle puzzle, int[][] matrix)
    {
        sequences = puzzle.getRowSequences();
        length = puzzle.getColumnSequences().length;
        this.matrix = matrix;
    }
    public void Deduct(int rowIndex, boolean[] checkNeededColumns, int configurationCount) {
        this.configurationCount = configurationCount;
        Deduct(rowIndex, checkNeededColumns);
    }

    public int FindRowIndex(int index, int iterator)
    {
        return index;
    }
    public int FindColumnIndex(int index, int iterator)
    {
        return  iterator;
    }

}
