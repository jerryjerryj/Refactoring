/*
Japanese Puzzle solving.Solver

Different solver techniques:
- simple exhaustive search [DONE]
- quite complex exhaustive search [DONE]
- very complex exhaustive search [-]
- 'swap random' randomized search [-]
- deduction search [DONE] <-- this is good enough!

Esa Junttila, 30.9.2007
*/

import creating.Puzzle;
import creating.PuzzleCreatorFactory;
import creating.creators.PuzzleCreator;
import solving.Solver;

import java.util.ArrayList;
import java.io.IOException;

public class JapanesePuzzle {

    private static final String USAGE_MESSAGE = "usage: java JapanesePuzzle <puzzleFile>       (direct file)\n" +
            "   OR: java JapanesePuzzle  <puzzleInput>    (redirect file as input)\n";

    public static void main(String[] args){
        if(args.length != 1)
        {
            System.err.println(USAGE_MESSAGE);
            return;
        }
        try
        {
            PuzzleCreator c =  PuzzleCreatorFactory.Create(args[0]);
            Puzzle puzzle = c.CreatePuzzle();
            Solver solver =  new Solver(puzzle);

            System.err.print("COMPUTING...");
            long startTime = System.currentTimeMillis();
            ArrayList<int[][]> result = solver.solveDeduction();

            long endTime = System.currentTimeMillis();
            System.err.print("...OK (" + (endTime - startTime) + " ms)\n");

            Printer.PrintSolution(result);
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

	}
}