package creating;

import creating.parsers.PuzzleParser;
import creating.parsers.SequenceParser;
import creating.parsers.SolutionParser;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class ParserFactory
{
    public static PuzzleParser Create(String path) throws IOException
    {
        InputFile file = new InputFile(path);
        if(file.getExtention().equals(InputFile.SEQUENCE_EXTENTION))
            return new SequenceParser(file.getSourceLines());
        else return new SolutionParser(file.getSourceLines());
    }
}
