package edu.lehigh.swat.bench.uba.model;

import java.util.ArrayList;

/** informaiton of a publication instance */
public class PublicationInfo {
    /** id */
    public String id;
    /** name */
    public String name;
    /** list of authors */
    public String author;
    /** list of other authors */
    public ArrayList<String> mentions;

    public int type;

    public int[] researches;
}