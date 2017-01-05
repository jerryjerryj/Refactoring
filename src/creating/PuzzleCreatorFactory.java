package creating;

import creating.creators.CreatorFromSequence;
import creating.creators.CreatorFromSolution;
import creating.creators.PuzzleCreator;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class PuzzleCreatorFactory
{
    public static PuzzleCreator Create(String path) throws IOException
    {
        InputFile file = new InputFile(path);
        if(file.getExtention().equals(InputFile.SEQUENCE_EXTENTION))
            return new CreatorFromSequence(file.getStream());
        else return new CreatorFromSolution(file.getStream());
    }
}
