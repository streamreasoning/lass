package it.polimi.streamreasoning.xbench.writers;

import com.opencsv.CSVWriter;
import it.polimi.streamreasoning.xbench.generators.GeneratorCallbackTarget;
import it.polimi.streamreasoning.xbench.state.GlobalState;
import it.polimi.streamreasoning.xbench.writers.utils.BufferSizes;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Riccardo on 24/04/16.
 */
public class RDFStreamWriter extends OwlWriter {
    private CSVWriter stream;
    private String streamName;

    public RDFStreamWriter(GeneratorCallbackTarget callbackTarget, String ontologyUrl) {
        super(callbackTarget, ontologyUrl);
    }

    @Override
    protected void prepareOutputStream(String fileName, GlobalState state) {
        try {
            // Prepare the output stream
            File f = new File(fileName);
            f.createNewFile();
            OutputStream stream = new FileOutputStream(f);

            if (fileName.endsWith(".gz")) {
                stream = new GZIPOutputStream(stream, BufferSizes.GZIP_BUFFER_SIZE);
            } else {
                stream = new BufferedOutputStream(stream, BufferSizes.OUTPUT_BUFFER_SIZE);
            }
            out = new PrintStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Create file failure!", e);
        }
    }


    public void startStream(String streamName) {
        this.streamName = streamName;
        try {
            this.stream = new CSVWriter(new FileWriter(streamName + ".stream"), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeStream() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void append(String graphName) {
        try {
            stream.writeNext(new String[]{graphName + ".owl", System.currentTimeMillis() + ""});
            stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CSVWriter getStreamWriter() {
        return stream;
    }


}
