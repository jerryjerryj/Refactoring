package creating.creators;

import java.io.BufferedInputStream;
import creating.Puzzle;
import creating.PuzzleAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Rin
 * Date: 05.01.17
 * Time: 17:16
 * To change this template use File | Settings | File Templates.
 */
public class CreatorFromSequence extends PuzzleCreator
{
    public CreatorFromSequence(BufferedInputStream stream)
    {
        super(stream);
    }
    public Puzzle CreatePuzzle() throws IOException
    {
        ArrayList<Character> numbers = new ArrayList<Character>();
        int c;
        boolean insideNumber = false;

        while ( (c = stream.read()) >= 0) {
            if (Character.isDigit(c)) {
                numbers.add(new Character( (char) c ));
                insideNumber = true;
            }
            else {
                if (insideNumber) {
                    numbers.add(PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR);
                    insideNumber = false;
                }

                if (c == PuzzleAttributes.PUZZLE_STRING_ROW_SYMBOL_CHAR ||
                        c == PuzzleAttributes.PUZZLE_STRING_COLUMN_SYMBOL_CHAR) {

                    numbers.add(new Character( (char) c ));
                    numbers.add(PuzzleAttributes.PUZZLE_STRING_SEPARATOR_CHAR); // add a separator
                }
                else if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                }
                else throw new RuntimeException(
                            "ERROR: sequence contains an illegal character: (" + (char) c + ")");
            }
        }

        char[] characters = new char[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            characters[i] = numbers.get(i);
        }
        String sequences = new String(characters);

        return createPuzzleFromSequences(sequences);

    }
}


