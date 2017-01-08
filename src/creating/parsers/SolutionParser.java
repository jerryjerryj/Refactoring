package creating.parsers;

import creating.Puzzle;
import creating.PuzzleAttributes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class SolutionParser extends PuzzleParser
{
    public SolutionParser(ArrayList<String> sourceLines)
    {
        super(sourceLines);
    }
    public Puzzle ParseSourceToPuzzle() throws IOException
    {
        return new Puzzle(ParseLines(sourceLines), ParseLines(ReverseLinesRowsToLinesColumns(sourceLines)));
    }

    private int[][] ParseLines(ArrayList<String>  lines)
    {
        int[][] result = new int[lines.size()][];
        for (int i=0;i<lines.size();++i)
            result[i] = ParseLine(lines.get(i));
        return result;
    }
    private int[] ParseLine(String line)
    {
        ArrayList<Integer> result = new ArrayList<>();
        int counter = 0;
        for(char symbol : line.toCharArray())
            if(symbol==PuzzleAttributes.MATRIX_FALSE && counter!=0)
            {
                result.add(counter);
                counter=0;
            }
            else if(symbol == PuzzleAttributes.MATRIX_TRUE)
                ++counter;
        if(counter!=0)
            result.add(counter);
        return result.stream().mapToInt(i->i).toArray();
    }
    private ArrayList<String> ReverseLinesRowsToLinesColumns(ArrayList<String> lines)
    {
        ArrayList<String> result = new ArrayList<>();

        for(int i=0;i<lines.get(0).length();++i)
        {
            StringBuilder builder = new StringBuilder();
            for(String line : lines)
                builder.append(line.charAt(i));
            result.add(builder.toString());
        }
        return result;
    }
}
