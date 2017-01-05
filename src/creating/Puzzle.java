package creating;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 21:32
 * To change this template use File | Settings | File Templates.
 */
public class Puzzle {
    private int[][] rowSequences;
    private int[][] colSequences;
    private int[][] puzzleMatrix;

    public Puzzle(int[][] rowSequences, int[][] colSequences) {
        this.rowSequences = rowSequences;
        this.colSequences = colSequences;

        this.puzzleMatrix = new int[rowSequences.length][colSequences.length];
    }

    public int[][] getRowSequences(){
        return rowSequences;
    }
    public int[][] getColSequences(){
        return colSequences;
    }
    public int[][] getPuzzleMatrix(){
        return puzzleMatrix;
    }
    public void setPuzzleMatrix(int[][] matrix){
        puzzleMatrix = matrix;
    }

}
