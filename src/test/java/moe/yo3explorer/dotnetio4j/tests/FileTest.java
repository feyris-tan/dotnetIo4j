package moe.yo3explorer.dotnetio4j.tests;

import moe.yo3explorer.dotnetio4j.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * Created by ft on 28.03.17.
 */
public class FileTest
{
    private final String filename = "test.bin";
    private final String dirname = "testdir";

    @Test
    public void createAndDeleteFile() throws Exception {
        FileStream fileStream = File.create(filename);
        fileStream.close();
        File.delete(filename);
    }

    @Test
    public void deleteFileThatDoesNotExistsShouldFail()
    {
        try
        {
            File.delete("inexistant.file");
            Assert.fail();
        }
        catch(FileNotFoundException fnfe)
        {
            return;
        }
    }

    @Test
    public void fileClassShouldNotDeleteDirectories()
    {
        Directory.createDirectory(dirname);
        try
        {
            File.delete(dirname);
            Assert.fail();
        }
        catch(IOException ioe)
        {
            if (ioe instanceof FileNotFoundException) Assert.fail("Got FileNotFound instead of IO!");
            Directory.deleteDirectory(dirname);
            return;
        }
    }

    @Test
    public void fileExists()
    {
        File.create(filename).close();
        if (!File.exists(filename)) Assert.fail("File.exists reported false even though it was created.");

        File.delete(filename);
        if (File.exists(filename)) Assert.fail("The file exists even though it was deleted.");
    }

    @Test
    public void fileDoesNotExistIfItIsADirectory()
    {
        Directory.createDirectory(dirname);

        boolean fileExists = File.exists(dirname);

        Directory.deleteDirectory(dirname);

        Assert.assertFalse(fileExists);
    }

    @Test
    public void openRead()
    {
        byte[] buffer = new byte[1024];
        new Random().nextBytes(buffer);

        File.writeAllBytes(filename,buffer);

        FileStream fs = File.openRead(filename);
        byte[] cmpBuffer = new byte[1024];
        assert fs.read(cmpBuffer,0,1024) == 1024;
        fs.close();

        File.delete(filename);

        Assert.assertArrayEquals(buffer,cmpBuffer);
    }

    @Test
    public void readAllBytes()
    {
        byte[] buffer = new byte[1024];
        new Random().nextBytes(buffer);

        File.writeAllBytes(filename,buffer);

        byte[] cmpBuffer = File.readAllBytes(filename);

        File.delete(filename);

        Assert.assertArrayEquals(buffer,cmpBuffer);
    }

    @Test
    public void open()
    {
        File.openWrite(filename).close();
        File.open(filename,FileMode.OpenOrCreate,FileAccess.Read).close();

        File.delete(filename);
    }
}
