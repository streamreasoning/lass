package it.polimi.streamreasoning.xbench.model;

/**
 * informaiton of a publication instance
 */
public class ActionModel {
    /**
     * id
     */
    public String id;
    /**
     * name
     */
    public String name;
    /**
     * list of authors
     */
    public String author;
    /**
     * list of other authors
     */
    public String[] mentions;

    public int type;

    public Integer[] tags;
    public Integer[] topics;
    public Integer[] ttopics;

    public Integer[] likes;
    public Integer[] shares;
    public Integer[] comments;

    public ActionModel reactionTo;

    public boolean isReaction() {
        return type == Ontology.CS_C_SHARED_POST || type == Ontology.CS_C_LIKE || type == Ontology.CS_C_COMMENT;
    }

}