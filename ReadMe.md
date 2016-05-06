# LASS Instance Generator v1 (L1G)
 
Instance Generator for the Lass Ontology (http://streamreasoning.org/ontologies/lass.owl) based on the code of University Benchmark Articifial Data Generator (UBA) for LUBM benchmark.

## Usage

    > ./generate.sh options
   
Run the following to see the usage summary:

    > ./generate.sh --help

### Performance Tuning

There are a number of parameters that can be used to tune the performance of the generator.  The best combination will depend on the hardware on which you are generating the data.

#### Multi-threading

We strongly suggest using `--threads` to set the number of threads, typically you should set this to twice the number of processor cores (assuming hyper-threading enabled).  Using this option will give you substantially better performance than not using it.

#### Consolidation

Using consolidation will reduce the number of files generated though total IO will be roughly the same. With `--consolidate Partial` you get a file per university (which can still be a lot of files at scale) while `--consolidate Full` will produce a single file per-thread which provides the least number of files while still giving good parallel throughput.

#### Compression

The `--compress` option trades processing power for substantially reduced IO. The reduced IO is invaluable at larger scales, for example with 1000 universities and `--consolidate Full` the compressed N-Triples output file is 706 MB while the uncompressed output is 23 GB i.e. an approximately 32x compression ratio.

#### Output Format

The value given for `--format` controls the output data format and can have an effect on the amount of IO done and the performance.

`TURTLE` is the most compact format but is most expensive to produce because the reduction to prefixed name form takes extra time.  `NTRIPLES` and `OWL` are typically the fastest formats to produce.

## Copyright

### Original UBA Code

The Semantic Web and Agent Technologies (SWAT) Lab, CSE Department, Lehigh University

### Modified UBA Code

Rob Vesse
##L1G code
Riccardo Tommasini Politecnico of Milan

## Contact

riccardo dot tommasini at polimit dot it

### Original Author

Yuanbo Guo	[yug2@lehigh.edu](mailto:yug2@lehigh.edu)
