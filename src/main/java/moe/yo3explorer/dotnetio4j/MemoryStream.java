package moe.yo3explorer.dotnetio4j;

import java.io.IOException;

/**
 * Created by ft on 28.03.17.
 */
public class MemoryStream extends Stream
{
    private byte[] backBuffer;
    private long currentPosition;
    private boolean canWrite;
    private boolean closed;

    public MemoryStream()
    {
        backBuffer = new byte[0];
    }

    public MemoryStream(int capacity)
    {
        backBuffer = new byte[capacity];
    }

    public MemoryStream(byte[] buffer)
    {
        this.backBuffer = buffer;
        canWrite = true;
    }

    public MemoryStream(byte[] buffer,boolean canWrite)
    {
        this.backBuffer = buffer;
        this.canWrite = canWrite;
    }

    @Override
    public boolean canRead() {
        if (closed) return false;
        return true;
    }

    @Override
    public boolean canSeek() {
        if (closed) return false;
        return true;
    }

    @Override
    public boolean canWrite() {
        if (closed) return false;
        return canWrite;
    }

    @Override
    public long getLength() {
        return backBuffer.length;
    }

    @Override
    public long getPosition() {
        return currentPosition;
    }

    @Override
    public void setPosition(long value) {
        if (currentPosition < 0) throw new RuntimeException("position must not be negative.");
        currentPosition = value;
    }

    @Override
    public void close() throws IOException {
        backBuffer = null;
        canWrite = false;
        closed = true;
    }

    @Override
    public void flush() {

    }

    @Override
    public long seek(long offset, SeekOrigin origin) {
        switch(origin)
        {
            case Begin:
                this.currentPosition = offset;
                break;
            case Current:
                this.currentPosition += offset;
                break;
            case End:
                this.currentPosition = backBuffer.length + offset;
                break;
        }
        return this.currentPosition;
    }

    @Override
    public void setLength(long value) {
        byte[] newBuffer = new byte[(int)value];
        int copyLength = Math.min(newBuffer.length,backBuffer.length);

        System.arraycopy(backBuffer,0,newBuffer,0,copyLength);

        backBuffer = newBuffer;
    }

    @Override
    public int read(byte[] buffer, int offset, int length) {
        int readLength = (int)Math.min(backBuffer.length - currentPosition,length);

        System.arraycopy(backBuffer,(int)currentPosition,buffer,offset,readLength);

        return readLength;
    }

    @Override
    public void write(byte[] buffer, int offset, int count)
    {
        int goal = (int) (currentPosition + count);
        if (goal > backBuffer.length) setPosition(goal);

        System.arraycopy(buffer,offset,backBuffer,(int)currentPosition,count);
    }
}
