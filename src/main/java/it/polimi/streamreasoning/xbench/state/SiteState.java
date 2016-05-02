package it.polimi.streamreasoning.xbench.state;

import it.polimi.streamreasoning.xbench.generation.GenerationParameters;
import it.polimi.streamreasoning.xbench.generation.InstanceCount;
import it.polimi.streamreasoning.xbench.generation.PropertyCount;
import it.polimi.streamreasoning.xbench.generators.Generator;
import it.polimi.streamreasoning.xbench.generators.GeneratorCallbackTarget;
import it.polimi.streamreasoning.xbench.model.ActionModel;
import it.polimi.streamreasoning.xbench.model.DiscussionLeaderModel;
import it.polimi.streamreasoning.xbench.model.Ontology;
import it.polimi.streamreasoning.xbench.writers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

public class SiteState implements GeneratorCallbackTarget {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteState.class);

    private final GlobalState state;

    /**
     * random number generator
     */
    private final Random random;

    /**
     * seed of the random number generator for the current university
     */
    private final long seed;

    /**
     * index of this university
     */
    private final int index;

    /**
     * (class) instance information
     */
    private final InstanceCount[] instances;

    /**
     * property instance information
     */
    private final PropertyCount[] properties;

    /**
     * list of publication instances generated so far (in the current
     * department)
     */
    private final ArrayList<ActionModel> publications;
    /**
     * index of the full professor who has been chosen as the department chair
     */
    private DiscussionLeaderModel chair;

    private Writer writer;
    private RDFStreamWriter streamWriter;

    private boolean completed = false;
    private Throwable error = null;

    public SiteState(GlobalState state, int index) {
        this.state = state;
        this.index = index;
        this.seed = state.getBaseSeed() * (Integer.MAX_VALUE + 1) + index;
        this.random = new Random(seed);

        this.chair = new DiscussionLeaderModel();

        this.instances = new InstanceCount[Ontology.CLASS_NUM];
        for (int i = 0; i < Ontology.CLASS_NUM; i++) {
            this.instances[i] = new InstanceCount();
        }
        this.properties = new PropertyCount[Ontology.PROP_NUM];
        for (int i = 0; i < Ontology.PROP_NUM; i++) {
            this.properties[i] = new PropertyCount();
        }

        publications = new ArrayList<>();

        switch (state.getWriterType()) {
            case OWL:
                writer = new OwlWriter(this, state.getOntologyUrl());
                this.streamWriter = new RDFStreamWriter(this, state.getOntologyUrl());
                break;
            case DAML:
                writer = new DamlWriter(this, state.getOntologyUrl());
                break;
            case NTRIPLES:
                writer = new NTriplesWriter(this, state.getOntologyUrl());
                break;
            case TURTLE:
                writer = new TurtleWriter(this, state.getOntologyUrl());
                break;
            default:
                throw new RuntimeException("Invalid writer type specified");
        }


    }

    public boolean hasCompleted() {
        return this.completed;
    }

    public void setComplete() {
        this.completed = true;

        // Throw away our reference to writer because it could be holding
        // various buffers which if we no longer need and as such should be GC'd
        this.writer = null;
    }

    public void setError(Throwable e) {
        this.error = e;
        this.state.incrementErrorCount();

        // Throw away our reference to writer because it could be holding
        // various buffers which if we've failed we no longer need and as such
        // should be GC'd
        this.writer = null;
    }

    public boolean hasError() {
        return this.error != null;
    }

    public Throwable getError() {
        return this.error;
    }

    public GlobalState getGlobalState() {
        return state;
    }

    public long getSeed() {
        return this.seed;
    }

    public int getUniversityIndex() {
        return this.index;
    }

    public DiscussionLeaderModel getChair() {
        return this.chair;
    }

    public Writer getWriter() {
        return writer;
    }

    public RDFStreamWriter getStreamWriter() {
        return this.streamWriter;
    }

    public String getFilename(int deptIndex) {
        StringBuilder fileName = new StringBuilder();

        // Base in output directory
        fileName.append(this.state.getOutputDirectory().getAbsolutePath());
        if (fileName.charAt(fileName.length() - 1) != File.separatorChar)
            fileName.append(File.separatorChar);

        if (this.state.consolidationMode() != ConsolidationMode.Full) {
            fileName.append(getName(Ontology.CS_C_SITE, this.index));
            if (this.state.consolidationMode() == ConsolidationMode.None) {
                // Department Index
                fileName.append(Generator.INDEX_DELIMITER);
                fileName.append(deptIndex);
            }
        } else {
            fileName.append("Sites-");
            fileName.append(Integer.toString(this.state.getWriterPool().getWriterId()));
        }

        // Extension
        fileName.append(this.state.getFileExtension());

        // Compression?
        if (this.state.compressFiles()) {
            fileName.append(".gz");
        }

        return fileName.toString();
    }

    /**
     * Gets the globally unique name of the specified instance.
     *
     * @param classType Type of the instance.
     * @param index     Index of the instance within its type.
     * @return Name of the instance.
     */
    public String getName(int classType, int index) {
        String name;

        switch (classType) {
            case Ontology.CS_C_SITE:
                name = getRelativeName(classType, index);
                break;
            case Ontology.CS_C_DISCUSSION:
                name = getRelativeName(classType, index) + Generator.INDEX_DELIMITER + (this.getUniversityIndex());
                break;
            // NOTE: Assume departments with the same index share the same pool of
            // courses and tags
            case Ontology.CS_C_TOPIC:
            case Ontology.CS_C_TRENDING_TOPIC:
            case Ontology.CS_C_TAG:
                name = getRelativeName(classType, index) + Generator.INDEX_DELIMITER
                        + (this.instances[Ontology.CS_C_DISCUSSION].count - 1);
                break;
            default:
                name = getRelativeName(classType, index) + Generator.INDEX_DELIMITER
                        + (this.instances[Ontology.CS_C_DISCUSSION].count - 1) + Generator.INDEX_DELIMITER
                        + (this.getUniversityIndex());
                break;
        }

        return name;
    }

    /**
     * Gets the name of the specified instance that is unique within a
     * department.
     *
     * @param classType Type of the instance.
     * @param index     Index of the instance within its type.
     * @return Name of the instance.
     */
    public String getRelativeName(int classType, int index) {
        String name;

        switch (classType) {
            case Ontology.CS_C_SITE:
                // should be unique too!
                name = Ontology.CLASS_TOKEN[classType] + index;
                break;
            case Ontology.CS_C_DISCUSSION:
                name = Ontology.CLASS_TOKEN[classType] + index;
                break;
            default:
                name = Ontology.CLASS_TOKEN[classType] + index;
                break;
        }

        return name;
    }

    /**
     * Gets the email address of the specified instance.
     *
     * @param classType Type of the instance.
     * @param index     Index of the instance within its type.
     * @return The email address of the instance.
     */
    public String getEmail(int classType, int index) {
        String email = "";

        switch (classType) {
            case Ontology.CS_C_SITE:
                email += getRelativeName(classType, index) + "@" + getRelativeName(classType, index) + ".edu";
                break;
            case Ontology.CS_C_DISCUSSION:
                email += getRelativeName(classType, index) + "@" + getRelativeName(classType, index) + "."
                        + getRelativeName(Ontology.CS_C_SITE, this.getUniversityIndex()) + ".edu";
                break;
            default:
                email += getRelativeName(classType, index) + "@"
                        + getRelativeName(Ontology.CS_C_DISCUSSION, this.instances[Ontology.CS_C_DISCUSSION].count - 1) + "."
                        + getRelativeName(Ontology.CS_C_SITE, this.getUniversityIndex()) + ".edu";
                break;
        }

        return email.toLowerCase();
    }

    /**
     * Gets the id of the specified instance.
     *
     * @param classType Type of the instance.
     * @param index     Index of the instance within its type.
     * @return Id of the instance.
     */
    public String getId(int classType, int index) {
        String id;

        switch (classType) {
            case Ontology.CS_C_SITE:
                id = "http://www." + getRelativeName(classType, index) + ".edu";
                break;
            case Ontology.CS_C_DISCUSSION:
                id = "http://www." + getRelativeName(classType, index) + "."
                        + getRelativeName(Ontology.CS_C_SITE, this.getUniversityIndex()) + ".edu";
                break;
            default:
                id = getId(Ontology.CS_C_DISCUSSION, this.instances[Ontology.CS_C_DISCUSSION].count - 1) + Generator.ID_DELIMITER
                        + getRelativeName(classType, index);
                break;
        }

        return id;
    }

    /**
     * Gets the id of the specified instance.
     *
     * @param classType Type of the instance.
     * @param index     Index of the instance within its type.
     * @param param     Auxiliary parameter.
     * @return Id of the instance.
     */
    public String getId(int classType, int index, String param) {
        String id;

        switch (classType) {
            case Ontology.CS_C_ACTION:
                // NOTE: param is author id
                id = param + Generator.ID_DELIMITER + Ontology.CLASS_TOKEN[classType] + index;
                break;
            default:
                id = getId(classType, index);
                break;
        }

        return id;
    }

    @Override
    public void startSectionCB(int classType) {
        this.instances[classType].logNum++;
        this.state.incrementTotalInstances(classType);
    }

    @Override
    public void startAboutSectionCB(int classType) {
        startSectionCB(classType);
    }

    @Override
    public void addPropertyCB(int property) {
        this.properties[property].logNum++;
        this.state.incrementTotalProperties(property);
    }

    @Override
    public void addValueClassCB(int classType) {
        this.instances[classType].logNum++;
        this.state.incrementTotalInstances(classType);
    }

    /**
     * Creates a list of the specified number of integers without duplication
     * which are randomly selected from the specified range.
     *
     * @param num Number of the integers.
     * @param min Minimum value of selectable integer.
     * @param max Maximum value of selectable integer.
     * @return So generated list of integers.
     */
    public ArrayList<Integer> getRandomList(int num, int min, int max) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        for (int i = min; i <= max; i++) {
            tmp.add(new Integer(i));
        }

        for (int i = 0; i < num; i++) {
            int pos = getRandomFromRange(0, tmp.size() - 1);
            list.add(tmp.get(pos));
            tmp.remove(pos);
        }

        return list;
    }

    /**
     * Randomly selects a integer from the specified range.
     *
     * @param min Minimum value of the selectable integer.
     * @param max Maximum value of the selectable integer.
     * @return The selected integer.
     */
    public int getRandomFromRange(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    public int[] getRandomRange(int size, int min, int max) {

        Set<Integer> ret = new HashSet<Integer>();
        while (ret.size() != size) {
            ret.add(new Integer(random.nextInt(max - min + 1) + min));
        }

        Integer[] integers = ret.toArray(new Integer[size]);
        int[] arr = new int[ret.size()];

        for (int i = 0; i < size; i++) {
            arr[i] = integers[i].intValue();
        }

        return arr;
    }

    public int getRandom(int max) {
        return random.nextInt(max);
    }

    public boolean getBoolean() {
        return random.nextBoolean();
    }

    public void reset() {
        this.resetInstanceInfo();

        publications.clear();

        for (int i = 0; i < Ontology.CLASS_NUM; i++) {
            this.instances[i].logNum = 0;
        }
        for (int i = 0; i < Ontology.PROP_NUM; i++) {
            this.properties[i].logNum = 0;
        }

        // decide the chair
        chair.type = this.getRandomFromRange(Ontology.CS_C_NEWS, Ontology.CS_C_ATTENDEE);
        chair.id = this.random.nextInt(this.instances[chair.type].total);
    }

    /**
     * Sets instance specification.
     */
    public void resetInstanceInfo() {
        int subClass, superClass;

        for (int i = 0; i < Ontology.CLASS_NUM; i++) {
            switch (i) {
                case Ontology.CS_C_SITE:
                    this.instances[Ontology.CS_C_SITE].num = this.state.getNumberUniversities();
                    break;
                case Ontology.CS_C_DISCUSSION:
                    this.instances[Ontology.CS_C_DISCUSSION].num = getRandomFromRange(GenerationParameters.DISCUSSION_MIN,
                            GenerationParameters.DISCUSSION_MAX);
                    break;
                case Ontology.CS_C_PERSON:
                    this.instances[Ontology.CS_C_PERSON].num = getRandomFromRange(GenerationParameters.PERSON_MIN,
                            GenerationParameters.PERSON_MAX);
                    break;
                case Ontology.CS_C_PARTICIPANT:
                    break;
                case Ontology.CS_C_INFLUENCER:
                    break;
                case Ontology.CS_C_NEWS:
                    this.instances[i].num = new Double(GenerationParameters.R_NEWS_PERSON * this.instances[Ontology.CS_C_PERSON].num).intValue();
                    break;
                case Ontology.CS_C_COMMERCIAL:
                    this.instances[i].num = new Double(GenerationParameters.R_COMM_PERSON * this.instances[Ontology.CS_C_PERSON].num).intValue();
                    break;
                case Ontology.CS_C_VIP:
                    this.instances[i].num = new Double(GenerationParameters.R_VIP_PERSON * this.instances[Ontology.CS_C_PERSON].num).intValue();
                    break;
                case Ontology.CS_C_INVOLVED:
                    this.instances[i].num = new Double(GenerationParameters.R_INVOLVED_PERSON * this.instances[Ontology.CS_C_PERSON].num).intValue();
                    break;
                case Ontology.CS_C_STAKEHOLDER:
                    break;
                case Ontology.CS_C_TOPIC_FOLLOWER:
                    this.instances[i].num = new Double(GenerationParameters.R_TOPIC_FOLLOWER_PERSON * this.instances[Ontology.CS_C_PERSON].num).intValue();
                    break;
                case Ontology.CS_C_TRENDING_TOPIC_FOLLOWER:
                    this.instances[i].num = new Double(GenerationParameters.R_TRENDING_TOPIC_FOLLOWER_PERSON * this.instances[Ontology.CS_C_PERSON].num).intValue();
                    break;
                case Ontology.CS_C_EXPERT:
                    this.instances[i].num =
                            new Double(GenerationParameters.R_EXPERT_PERSON * (this.instances[Ontology.CS_C_TRENDING_TOPIC_FOLLOWER].total + this.instances[Ontology.CS_C_TOPIC_FOLLOWER].total)).intValue();
                    break;
                case Ontology.CS_C_ATTENDEE:
                    this.instances[i].num = new Double(GenerationParameters.R_ATTENDEE_PERSON * (this.instances[Ontology.CS_C_TRENDING_TOPIC_FOLLOWER].total + this.instances[Ontology.CS_C_TOPIC_FOLLOWER].total)).intValue();
                    break;
                case Ontology.CS_C_DISCUSSION_LEADER:
                    break;
                case Ontology.CS_C_EVENT:
                    this.instances[i].num = getRandomFromRange(GenerationParameters.EVENT_MIN,
                            GenerationParameters.EVENT_MAX);
                    break;
                case Ontology.CS_C_TAG:
                    this.instances[i].num = getRandomFromRange(GenerationParameters.TAGS_NUM_MIN,
                            GenerationParameters.TAGS_NUM_MAX);
                    break;
                case Ontology.CS_C_TOPIC:
                    this.instances[i].num = getRandomFromRange(GenerationParameters.TOPIC_NUM_MIN,
                            GenerationParameters.TOPIC_NUM_MAX);
                    break;
                case Ontology.CS_C_TRENDING_TOPIC:
                    this.instances[i].num = getRandomFromRange(GenerationParameters.TRENDING_TOPIC_NUM_MIN,
                            GenerationParameters.TRENDING_TOPIC_NUM_MAX);
                    break;
                case Ontology.CS_C_ACTION:
                    break;
                case Ontology.CS_C_POST:
                    break;
                case Ontology.CS_C_MEDIAPOST:
                    break;
                case Ontology.CS_C_MICROPOST:
                    break;
                case Ontology.CS_C_BLOG_POST:
                    break;
                case Ontology.CS_C_COMMENT:
                    break;
                case Ontology.CS_C_LIKE:
                    break;
                case Ontology.CS_C_SHARED_POST:
                    break;
                case Ontology.CS_C_REACTION:
                    break;
                case Ontology.CS_C_POPULAR_POST:
                    break;
                default:
                    this.instances[i].num = Ontology.CLASS_INFO[i][Ontology.INDEX_NUM];
                    break;
            }
            this.instances[i].total = this.instances[i].num;
            LOGGER.info("CLASS [" + Ontology.CLASS_TOKEN[i] + "] INSTANCES: Expected [" + instances[i].num + "] ");
            subClass = i;
            while ((superClass = Ontology.CLASS_INFO[subClass][Ontology.INDEX_SUPER]) != Ontology.CS_C_NULL) {
                this.instances[superClass].total += this.instances[i].num;
                subClass = superClass;
            }
        }
    }

    public InstanceCount[] getInstances() {
        return this.instances;
    }

    public PropertyCount[] getProperties() {
        return this.properties;
    }

    public List<ActionModel> getPublications() {
        return this.publications;
    }

    public int getTopicNum() {
        return this.instances[Ontology.CS_C_TOPIC].num;
    }

    public int getTrendingTopicNum() {
        return this.instances[Ontology.CS_C_TRENDING_TOPIC].num;
    }

    public int getTagNum() {
        return this.instances[Ontology.CS_C_TAG].num;
    }

    public int getEventNum() {
        return this.instances[Ontology.CS_C_EVENT].num;
    }

    public void updateCount(int classType) {
        int subClass, superClass;

        this.getInstances()[classType].count++;
        subClass = classType;
        while ((superClass = Ontology.CLASS_INFO[subClass][Ontology.INDEX_SUPER]) != Ontology.CS_C_NULL) {
            this.getInstances()[superClass].count++;
            subClass = superClass;
        }
    }
}
