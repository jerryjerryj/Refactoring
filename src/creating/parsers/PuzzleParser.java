package creating.parsers;


import java.io.IOException;
import java.util.ArrayList;

import creating.Puzzle;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class PuzzleParser
{
    protected ArrayList<String> sourceLines;

    public PuzzleParser(ArrayList<String> sourceLines)
    {
        this.sourceLines = sourceLines;
    }
    protected abstract Puzzle ParseSourceToPuzzle() throws IOException;

    public Puzzle Parse() throws IOException
    {
        try{
            return ParseSourceToPuzzle();
        } catch (IOException e){
            throw new IOException("Incorrect input : "+e.getMessage());
        }
    }
}
