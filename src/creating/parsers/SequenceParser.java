package creating.parsers;

import creating.Puzzle;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class SequenceParser extends PuzzleParser
{
    public static final String SEQUENCES_NUMBERS_SEPARATOR = " ";
    public static final String ROW_SYMBOL = "_";
    public static final String COLUMN_SYMBOL = "|";

    public SequenceParser(ArrayList<String> sourceLines)
    {
        super(sourceLines);
    }

    public Puzzle ParseSourceToPuzzle() throws IOException
    {
        ArrayList<int[]> rows =  new ArrayList<>();
        ArrayList<int[]> columns =  new ArrayList<>();
        for(String line : sourceLines)
        {
            String[] members = line.split(SEQUENCES_NUMBERS_SEPARATOR);
            String marker = members[0];
            int[] numbers = new int[members.length-1];
            for(int i=1; i<members.length; ++i)
                numbers[i-1] = Integer.parseInt(members[i]);
            if(marker.equals(ROW_SYMBOL))
                rows.add(numbers);
            else if(marker.equals(COLUMN_SYMBOL))
                columns.add(numbers);
            else throw new IOException("Unexpected character "+marker);
        }
        return new Puzzle(ArrayListToPrimitive(rows), ArrayListToPrimitive(columns));
    }
    public int[][] ArrayListToPrimitive (ArrayList<int[]> list)
    {
        int[][] primitive = new int[list.size()][];
        for(int i=0, len = list.size(); i < len; i++)
            primitive[i] = list.get(i);
        return primitive;
    }
}


