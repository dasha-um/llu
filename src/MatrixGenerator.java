import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class MatrixGenerator
{
    public static final int SEED1 = 1;
    public static final int SEED2 = 2;
    public static final int EMPTY_ROW_FRACTION = 10;

    public static final String MATRIX1_NAME = "mg1.txt";
    public static final String MATRIX2_NAME = "mg2.txt";
    public static final int SIZE = 2000;

    private final int emptyRowFraction;
    private final int size;
    private final String emptyRow;
    private final Random rnd;
    private final String file;

    public MatrixGenerator(int seed, int emptyRowFraction, String file, int size)
    {
        this.emptyRowFraction = emptyRowFraction;
        this.size = size;
        this.file = file;
        rnd = new Random(seed);
        emptyRow = Collections.nCopies(size, "0").stream().collect(Collectors.joining(" "));
    }

    public void run()
    {
        try
        {
            new MatrixGenerator(SEED1, EMPTY_ROW_FRACTION, MATRIX1_NAME, SIZE).generate();
            new MatrixGenerator(SEED2, EMPTY_ROW_FRACTION, MATRIX2_NAME, SIZE).generate();
        }
        catch (IOException e)
        {
            System.out.println("Fail to generate matrix file: " + e);
        }
    }


    public void generate() throws IOException
    {
        PrintWriter out = new PrintWriter(new FileWriter(file));
        for (int i = 0; i < size; i++)
        {
            // only 1/emptyRowFraction will have non 0 values
            if (rnd.nextInt(emptyRowFraction) == 0)
                out.println(generateRow());
            else
                out.println(emptyRow);
        }
        out.close();
    }

    private String generateRow()
    {
        return rnd.ints(0, emptyRowFraction).limit(size).mapToObj(r -> (r == 0) ? "" + rnd.nextInt(10000) : "0")
                .collect(Collectors.joining(" "));
    }

}
