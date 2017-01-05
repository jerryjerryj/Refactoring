import creating.PuzzleAttributes;
import java.util.ArrayList;

public class Printer
{
    public static void PrintSolution(ArrayList<int[][]> result)
    {
        if (result == null) {
            System.out.println("No solutions!");
            return;
        }

        for (int i = 0; i < result.size(); i++) {
            System.out.println("Solution " + (i+1) +":"+"\n");
            PrintArray(result.get(i));
        }
    }
    private static void PrintArray(int[][] array)
    {
        for(int[] row : array)
        {
            for(int col : row)
            {
                if(col== PuzzleAttributes.PUZZLE_TRUE)
                    System.out.print(PuzzleAttributes.PUZZLE_MATRIX_TRUE_CHAR);
                else System.out.print(PuzzleAttributes.PUZZLE_MATRIX_FALSE_CHAR);
            }
            System.out.println();
        }
    }
}