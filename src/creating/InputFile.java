package creating;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InputFile
{
    private static final int MAX_INPUT_FILE_SIZE = 10000000;
    public static final String SOLUTION_EXTENTION = "jpsol";
    public static final String SEQUENCE_EXTENTION = "jpseq";

    private String extention;
    private BufferedInputStream stream;

    public InputFile(String path) throws FileNotFoundException
    {
        extention = path.substring(path.lastIndexOf('.') + 1);
        if(!extention.equals(SOLUTION_EXTENTION) && !extention.equals(SEQUENCE_EXTENTION))
            throw new FileNotFoundException("Illegal file format");
        //stream.mark(MAX_INPUT_FILE_SIZE);
        stream = new BufferedInputStream(new FileInputStream(path));
    }

    public String getExtention()
    {
        return extention;
    }

    public BufferedInputStream getStream()
    {
        return stream;
    }
}
