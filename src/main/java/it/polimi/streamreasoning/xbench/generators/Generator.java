/**
 * by Yuanbo Guo
 * Semantic Web and Agent Technology Lab, CSE Department, Lehigh University, USA
 * Copyright (C) 2004
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package it.polimi.streamreasoning.xbench.generators;

import java.util.*;
import java.util.concurrent.TimeUnit;

import it.polimi.streamreasoning.xbench.state.GlobalState;
import it.polimi.streamreasoning.xbench.state.SiteState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.polimi.streamreasoning.xbench.writers.ConsolidationMode;
import it.polimi.streamreasoning.xbench.writers.WriterType;

import java.io.*;

public class Generator {

    static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);

    /** delimiter between different parts in an id string */
    public static final char ID_DELIMITER = '/';
    /** delimiter between name and index in a name string of an instance */
    public static final char INDEX_DELIMITER = '_';

    /**
     * Begins the data generation.
     * 
     * @param univNum
     *            Number of universities to generate.
     * @param startIndex
     *            Starting index of the universities.
     * @param seed
     *            Seed for data generation.
     * @param daml
     *            Generates DAML+OIL data if true, OWL data otherwise.
     * @param ontology
     *            Ontology url.
     * @param workDir
     *            The working directory where generated files should be written
     * @param compress
     *            Whether to compress output
     * @param consolidate
     *            Whether and how to consolidate output
     * @param threads
     *            The number of threads to use for data generation
     * @param executionTimeout
     *            The maximum amount of time to wait for data generation to
     *            complete before aborting it
     * @param executionTimeoutUnit
     *            The time unit in which {@code executionTimeout} is expressed
     * @param quiet
     *            Whether to enable quiet mode
     */
    public void start(int univNum, int startIndex, int seed, WriterType writerType, String ontology, String workDir,
            ConsolidationMode consolidate, boolean compress, int threads, long executionTimeout,
            TimeUnit executionTimeoutUnit, boolean quiet) {
        File outputDir = workDir != null ? new File(workDir) : new File(".");
        outputDir = outputDir.getAbsoluteFile();
        if (!outputDir.exists() || !outputDir.isDirectory()) {
            if (!outputDir.mkdirs()) {
                throw new IllegalArgumentException(
                        String.format("Unable to create requested output directory %s", outputDir));
            }
        }
        GlobalState state = new GlobalState(univNum, seed, startIndex, ontology, writerType, outputDir, consolidate,
                compress, threads, executionTimeout, executionTimeoutUnit, quiet);

        try {
            state.start();
            System.out.println("Started...");

            // Submit a university generator for each university
            List<SiteState> states = new ArrayList<>();
            for (int i = 0; i < state.getNumberUniversities(); i++) {
                SiteState siteState = new SiteState(state, i + state.getStartIndex());
                SiteGenerator siteGen = new SiteGenerator(siteState);
                state.getExecutor().submit(siteGen);
                states.add(siteState);
            }
            try {
                state.getExecutor().shutdown();
                if (!state.getExecutor().awaitTermination(state.getExecutionTimeout(),
                        state.getExecutionTimeoutUnit())) {
                    // Force remaining threads to shut down
                    state.getExecutor().shutdownNow();
                    throw new RuntimeException("Timeout was exceeded");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("A generator thread was interrupted", e);
            }

            // Check everything completed
            if (!state.shouldContinue()) {
                notAllGeneratorsFinished(state);
            }
            for (SiteState siteState : states) {
                if (!siteState.hasCompleted()) {
                    notAllGeneratorsFinished(state);
                }
            }

            // Tell state we're finished
            state.finish();

            System.out.println("Completed!");
        } finally {
            // Tell state to finish regardless of whether we encountered an
            // error
            // If we completed successfully then this will already have been
            // called and calling it again is a no-op
            state.finish();
        }
    }

    private void notAllGeneratorsFinished(GlobalState state) {
        throw new RuntimeException("Not all sites generators finished successfully, see log for details");
    }
}