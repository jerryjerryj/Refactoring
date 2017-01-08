package creating;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class InputFile
{
    public static final String SOLUTION_EXTENTION = "jpsol";
    public static final String SEQUENCE_EXTENTION = "jpseq";

    private String extention;
    private ArrayList<String> sourceLines;

    public InputFile(String path) throws IOException
    {
        extention = path.substring(path.lastIndexOf('.') + 1);
        if(!extention.equals(SOLUTION_EXTENTION) && !extention.equals(SEQUENCE_EXTENTION))
            throw new FileNotFoundException("Illegal file format");
        sourceLines = (ArrayList<String>) Files.readAllLines(Paths.get(path));
    }

    public String getExtention()
    {
        return extention;
    }
    public ArrayList<String> getSourceLines()
    {
        return sourceLines;
    }
}
