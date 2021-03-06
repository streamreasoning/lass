package it.polimi.streamreasoning.xbench.writers.utils;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import it.polimi.streamreasoning.xbench.state.GlobalState;

/**
 * An output stream that buffers the contents in memory until the file is closed
 * at which time it submits the write to the background writer service
 * 
 * @author rvesse
 *
 */
public class MemoryBufferedOutputStream extends FilterOutputStream {

    private final GlobalState state;

    public MemoryBufferedOutputStream(GlobalState state) {
        super(new ByteArrayOutputStream(BufferSizes.MEMORY_BUFFER_SIZE));
        this.state = state;
    }

    @Override
    public void close() throws IOException {
        super.close();

        // Submit the write to the background writer service
        OutputStream output = this.state.getWriterPool().getOutputStream();
        output.write(((ByteArrayOutputStream) this.out).toByteArray());
        output.flush();
        this.out = null;
    }
}
