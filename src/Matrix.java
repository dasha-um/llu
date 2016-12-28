import java.io.IOException;

public interface Matrix {
    Matrix mul(Matrix b) throws
            IOException, InterruptedException;
    Matrix pmul(Matrix b) throws
            IOException, InterruptedException;
    void save(String fileName) throws IOException;
}

