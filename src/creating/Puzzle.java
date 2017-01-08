package creating;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
public class Puzzle
{
    private int[][] rowSequences;
    private int[][] columnSequences;

    public Puzzle(int[][] rowSequences, int[][] columnSequences)
    {
        this.rowSequences = rowSequences;
        this.columnSequences = columnSequences;
    }

    public int[][] getRowSequences()
    {
        return rowSequences;
    }
    public int[][] getColumnSequences()
    {
        return columnSequences;
    }

}
