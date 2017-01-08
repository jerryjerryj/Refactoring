import creating.PuzzleAttributes;
import solving.deductors.DeductionAttributes;

public class Printer
{
    public static void PrintSolution(int[][] result)
    {
        if (result == null) {
            System.out.println("No solutions!");
            return;
        }
        System.out.println("Solution :"+"\n");
        PrintArray(result);
    }
    private static void PrintArray(int[][] array)
    {
        for(int[] row : array)
        {
            for(int col : row)
            {
                if(col== DeductionAttributes.PUZZLE_TRUE)
                    System.out.print(PuzzleAttributes.MATRIX_TRUE);
                else System.out.print(PuzzleAttributes.MATRIX_FALSE);
            }
            System.out.println();
        }
    }
}