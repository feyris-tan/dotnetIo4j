package moe.yo3explorer.dotnetio4j;

/**
 * Created by ft on 28.03.17.
 */
public class FileNotFoundException extends IOException
{
    public FileNotFoundException() {
        super("A file could not be found.");
    }

    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(Throwable cause) {
        super(cause);
    }
}
