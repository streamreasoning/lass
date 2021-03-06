package it.polimi.streamreasoning.xbench.writers;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.zip.GZIPOutputStream;

import it.polimi.streamreasoning.xbench.generators.GeneratorCallbackTarget;
import it.polimi.streamreasoning.xbench.state.GlobalState;
import it.polimi.streamreasoning.xbench.writers.utils.BufferSizes;
import it.polimi.streamreasoning.xbench.writers.utils.MemoryBufferedOutputStream;

public class AbstractWriter {

    /** white space string */
    protected static final String T_SPACE = " ";
    /** output stream */
    protected PrintStream out = null;
    /** the generator */
    protected GeneratorCallbackTarget callbackTarget;

    public AbstractWriter(GeneratorCallbackTarget callbackTarget) {
        this.callbackTarget = callbackTarget;
    }

    /**
     * Prepares the output stream
     * 
     * @param fileName
     *            File name
     * @param state
     *            State
     */
    protected  void prepareOutputStream(String fileName, GlobalState state) {
        if (state.consolidationMode() != ConsolidationMode.Full) {
            try {
                // Prepare the output stream
                OutputStream stream = new FileOutputStream(fileName);
                if (fileName.endsWith(".gz")) {
                    stream = new GZIPOutputStream(stream, BufferSizes.GZIP_BUFFER_SIZE);
                } else {
                    stream = new BufferedOutputStream(stream, BufferSizes.OUTPUT_BUFFER_SIZE);
                }
                out = new PrintStream(stream);
            } catch (IOException e) {
                throw new RuntimeException("Create file failure!", e);
            }
        } else {
            out = new PrintStream(new MemoryBufferedOutputStream(state));
        }
    }

    /**
     * Cleans up the output stream
     */
    protected final void cleanupOutputStream() {
        if (out.checkError()) {
            // Make sure to null out the output stream when we're done because
            // the nature of how we do multi-threading means we have lots of
            // references to our writers each of which may be holding a
            // reference to a buffer
            out = null;
            throw new RuntimeException("Error writing file");
        }

        out.flush();
        out.close();

        if (out.checkError()) {
            // Make sure to null out the output stream when we're done because
            // the nature of how we do multi-threading means we have lots of
            // references to our writers each of which may be holding a
            // reference to a buffer
            out = null;
            throw new RuntimeException("Error writing file");
        }
    }

}