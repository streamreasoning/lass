package it.polimi.streamreasoning.xbench.writers.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

import it.polimi.streamreasoning.xbench.state.GlobalState;
import it.polimi.streamreasoning.xbench.writers.ConsolidationMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a pool of thread-scoped writers so each worker thread can write to a
 * single separate file without blocking
 * <p>
 * Used to implement the {@link ConsolidationMode#Full} consolidation mode.
 * </p>
 * 
 * @author rvesse
 *
 */
public class WriterPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriterPool.class);

    private final GlobalState state;
    private final AtomicInteger writerIds = new AtomicInteger(0);
    private final List<OutputStream> writers = new ArrayList<>();
    private final List<String> filenames = new ArrayList<>();
    private final ThreadLocal<OutputStream> threadWriters = new ThreadLocal<>();
    private final ThreadLocal<Integer> threadIds = new ThreadLocal<>();
    private boolean closed = false;

    /**
     * Creates a new pool
     * 
     * @param state
     *            Global state
     */
    public WriterPool(GlobalState state) {
        this.state = state;
    }

    /**
     * Gets a writer from the pool for the current thread
     * 
     * @return Output stream
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException {
        if (this.closed)
            throw new IllegalStateException("Writer pool has been closed");

        OutputStream output = this.threadWriters.get();
        if (output == null) {
            // Build filename
            StringBuilder filename = new StringBuilder();
            filename.append("Sites-");
            int id = this.writerIds.incrementAndGet();
            filename.append(id);
            filename.append(this.state.getFileExtension());
            if (this.state.compressFiles()) {
                filename.append(".gz");
            }

            LOGGER.info("Issued new writer from pool (ID {} - File {})", id, filename);

            // Prepare an output stream
            output = new FileOutputStream(new File(this.state.getOutputDirectory(), filename.toString()));
            output = new BufferedOutputStream(output, BufferSizes.OUTPUT_BUFFER_SIZE);
            if (this.state.compressFiles()) {
                output = new GZIPOutputStream(output, BufferSizes.GZIP_BUFFER_SIZE);
            }

            // Record this writer so we can re-use it on this thread later and
            // so we can clean it up from a different thread
            this.threadWriters.set(output);
            synchronized (this.writers) {
                this.writers.add(output);
                this.filenames.add(filename.toString());
                this.threadIds.set(id);
            }
        }
        return output;
    }

    /**
     * Gets the ID of the writer for this thread
     * 
     * @return Writer ID
     */
    public int getWriterId() {
        Integer i = this.threadIds.get();
        if (i == null) {
            // Get the writer for this thread to cause the ID to be allocated
            try {
                getOutputStream();
            } catch (IOException e) {
                throw new RuntimeException("Unable to allocate a writer ID for this thread");
            }
        }
        i = this.threadIds.get();
        return i;
    }

    /**
     * Closes all the writers in the pool
     */
    public void close() {
        if (this.closed)
            return;
        this.closed = true;

        // Try to close each output stream
        List<Throwable> errors = new ArrayList<>();
        synchronized (this.writers) {
            for (int i = 0; i < this.writers.size(); i++) {
                OutputStream output = this.writers.get(i);
                String filename = this.filenames.get(i);
                try {
                    output.flush();
                    output.close();

                    System.out.println(String.format("%s generated", filename));
                } catch (IOException e) {
                    // Log and track errors
                    errors.add(e);
                    LOGGER.error("Error closing pool writer - {}", e);
                    System.out.println(String.format("Failed to generate %s - %s", filename, e.getMessage()));
                }
            }
        }

        // Throw if any errors
        if (!errors.isEmpty())
            throw new IllegalStateException(
                    String.format("%d writers from the writer pool could not be closed", errors.size()));
    }
}
