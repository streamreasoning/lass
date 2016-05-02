package it.polimi.streamreasoning.xbench.generators;

import it.polimi.streamreasoning.xbench.generation.InstanceCount;
import it.polimi.streamreasoning.xbench.model.Ontology;
import it.polimi.streamreasoning.xbench.state.SiteState;
import it.polimi.streamreasoning.xbench.writers.ConsolidationMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

class SiteGenerator implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteGenerator.class);

    private final SiteState univState;

    private PersonGenerator pGenerator;
    private ActionGenerator aGenerator;

    public SiteGenerator(SiteState univState) {
        this.univState = univState;
        aGenerator = new ActionGenerator(univState);
        pGenerator = new PersonGenerator(univState, aGenerator);
    }

    public void run() {
        if (!this.univState.getGlobalState().shouldContinue()) {
            LOGGER.error("Some data generators have failed, skipping further data generation (University {})", this.univState.getUniversityIndex());
            return;
        }

        try {
            _generateSite(this.univState);

            InstanceCount[] instances = univState.getInstances();
            for (int i = 0; i < Ontology.CLASS_NUM; i++) {
                LOGGER.info("CLASS [" + Ontology.CLASS_TOKEN[i] + "] INSTANCES: Expected [" + instances[i].num + "] " +
                        "Count [" + instances[i].count + "]");
            }


            this.univState.setComplete();
        } catch (Throwable e) {
            this.univState.setError(e);
            LOGGER.error("Error in generating University {} - {}", this.univState.getUniversityIndex(), e);
            StringWriter strWriter = new StringWriter();
            PrintWriter writer = new PrintWriter(strWriter);
            e.printStackTrace(writer);
            LOGGER.error(strWriter.toString());
        }
    }

    /**
     * Creates a university.
     */
    private void _generateSite(SiteState univState) {
        univState.resetInstanceInfo();

        for (int i = 0; i < univState.getInstances()[Ontology.CS_C_DISCUSSION].num; i++) {
            String filename = univState.getFilename(i);
            if (i == 0 || univState.getGlobalState().consolidationMode() == ConsolidationMode.None) {
                univState.getWriter().startFile(filename, univState.getGlobalState());
                univState.getStreamWriter().startStream(filename);
                _generateAUniv(univState, univState.getUniversityIndex());
            }

            _generateDiscussion(univState, i, filename);
            _generateContent(univState);
            _genPeople(univState);
            // _genActions(univState);
            close(univState, i, filename);
        }


    }

    /**
     * Creates a department.
     *
     * @param index Index of the department. NOTE: Use univIndex instead of
     *              instances[CS_C_SITE].count till generateASection(CS_C_SITE, )
     *              is invoked.
     */
    private void _generateDiscussion(SiteState univState, int index, String filename) {
        // Start a new file if we're not consolidating or this is the first
        // department for the university
        univState.updateCount(Ontology.CS_C_DISCUSSION);

        univState.getWriter().startSection(Ontology.CS_C_DISCUSSION, univState.getId(Ontology.CS_C_DISCUSSION, index));
        univState.getWriter().addProperty(Ontology.CS_DP_NAME, univState.getRelativeName(Ontology.CS_C_DISCUSSION, index),
                false);
        univState.getWriter().addProperty(Ontology.CS_P_SUBCOMMUNITY_OF, Ontology.CS_C_SITE,
                univState.getId(Ontology.CS_C_SITE, univState.getUniversityIndex()));
        univState.getWriter().endSection(Ontology.CS_C_DISCUSSION);


    }

    private void _genActions(SiteState univState) {
        aGenerator.genActionInstances(univState);
    }

    private void _genPeople(SiteState univState) {
        for (int classType = Ontology.CS_C_DISCUSSION + 1; classType < Ontology.CLASS_NUM; classType++) {
            univState.getInstances()[classType].count = 0;
            for (int instanceIndex = 0; instanceIndex < univState.getInstances()[classType].num; instanceIndex++) {
                _generateASection(univState, classType, instanceIndex);
            }
        }
        pGenerator.attendeeInstanceGen(univState);
        pGenerator.expertInstanceGen(univState);
    }

    /**
     * Generates an instance of the specified class
     *
     * @param classType Type of the instance.
     * @param index     Index of the instance.
     */
    private void _generateASection(SiteState state, int classType, int index) {
        univState.updateCount(classType);

        switch (classType) {
            case Ontology.CS_C_PARTICIPANT: //used only in recursion
                pGenerator.participantInstanceGen(state, classType, index);
                break;
            case Ontology.CS_C_INFLUENCER: //used only in recursion
                pGenerator.influencerInstanceGen(state, classType, index);
                break;
            case Ontology.CS_C_NEWS:
                pGenerator.newsInstanceGen(state, index);
                break;
            case Ontology.CS_C_COMMERCIAL:
                pGenerator.comInstanceGen(state, index);
                break;
            case Ontology.CS_C_VIP:
                pGenerator.vipInstanceGen(state, index);
                break;
            case Ontology.CS_C_INVOLVED:
                pGenerator.involvedInstanceGen(state, index);
                break;
            case Ontology.CS_C_TOPIC_FOLLOWER:
                pGenerator.topicFollowerInstanceGen(state, index);
                break;
            case Ontology.CS_C_TRENDING_TOPIC_FOLLOWER:
                pGenerator.trendingTopicFollowerInstanceGen(state, index);
                break;
            default:
                break;
        }
    }

    /**
     * Generates a university instance.
     *
     * @param index Index of the instance.
     */
    private void _generateAUniv(SiteState univState, int index) {
        univState.getWriter().startSection(Ontology.CS_C_SITE, univState.getId(Ontology.CS_C_SITE, index));
        univState.getWriter().addProperty(Ontology.CS_DP_NAME, univState.getRelativeName(Ontology.CS_C_SITE, index),
                false);
        univState.getWriter().endSection(Ontology.CS_C_SITE);
    }

    /**
     * Generates topic/trending topic  instances. These topics are assigned to
     */
    private void _generateContent(SiteState univState) {

        List<Integer> topics = univState.getRandomList(univState.getTopicNum(), 0, univState.getTopicNum() - 1);

        for (Integer t : topics) {
            _contentInstanceGen(univState, Ontology.CS_C_TOPIC, t.intValue());
        }

        List<Integer> ttopics = univState.getRandomList(univState.getTrendingTopicNum(), 0, univState.getTrendingTopicNum() - 1);

        for (Integer t : ttopics) {
            _contentInstanceGen(univState, Ontology.CS_C_TRENDING_TOPIC, t.intValue());
        }

        List<Integer> tags = univState.getRandomList(univState.getTagNum(), 0, univState.getTagNum() - 1);
        for (Integer t : tags) {
            _contentInstanceGen(univState, Ontology.CS_C_TAG, t.intValue());
        }

        List<Integer> events = univState.getRandomList(univState.getEventNum(), 0, univState.getEventNum() - 1);

        for (Integer e : events) {
            _eventInstanceGen(univState, e.intValue());
        }
    }

    private void _contentInstanceGen(SiteState univState, int type, int index) {
        univState.updateCount(type);
        univState.getWriter().startSection(type, univState.getId(type, index));
        univState.getWriter().endSection(type);
    }

    /**
     * Generates a research group instance.
     *
     * @param index Index of the research group.
     */
    private void _eventInstanceGen(SiteState univState, int index) {
        univState.updateCount(Ontology.CS_C_EVENT);
        String id = univState.getId(Ontology.CS_C_EVENT, index);
        univState.getWriter().startSection(Ontology.CS_C_EVENT, id);

        univState.getWriter().addProperty(Ontology.CS_P_SUBCOMMUNITY_OF,
                univState.getId(Ontology.CS_C_DISCUSSION, univState.getInstances()[Ontology.CS_C_DISCUSSION].count - 1), true);

        int eventTag = univState.getRandom(univState.getTagNum() - 1);

        univState.getWriter().addProperty(Ontology.CS_P_HAS_EVENT_TAG, univState.getId(Ontology.CS_C_TAG, eventTag), true);

        univState.getWriter().endSection(Ontology.CS_C_EVENT);

    }


    private void close(SiteState univState, int index, String filename) {
        if (univState.getGlobalState().consolidationMode() != ConsolidationMode.None) {
            // Consolidating output so file is not yet complete
            if (!univState.getGlobalState().isQuietMode())
                System.out.println(filename + " in progress...");
        }
        String bar = "";
        for (int i = 0; i < filename.length(); i++)
            bar += '-';
        Generator.LOGGER.info(bar);
        Generator.LOGGER.info(filename);
        Generator.LOGGER.info(bar);
        _generateComments(univState);

        // End the file if we aren't consolidating or this is the last file for
        // the university
        if (univState.getGlobalState().consolidationMode() == ConsolidationMode.None
                || index == univState.getInstances()[Ontology.CS_C_DISCUSSION].num - 1) {
            if (univState.getGlobalState().consolidationMode() != ConsolidationMode.Full) {
                System.out.println(filename + " generated");
            } else {
                // Consolidating output so file is not yet complete
                System.out.println(filename + " (Site " + univState.getUniversityIndex() + ") in progress...");
            }
            univState.getWriter().endFile(univState.getGlobalState());
        }

        univState.getStreamWriter().closeStream();
    }


    ///////////////////////////////////////////////////////////////////////////

    /**
     * Increases by 1 the instance count of the specified class. This also
     * includes the increase of the instance count of all its super class.
     *
     * @param classType Type of the instance.
     */


    /**
     * Outputs log information to both the log file and the screen after a
     * department is generated.
     */
    private void _generateComments(SiteState univState) {
        int classInstNum = 0; // total class instance num in this department
        long totalClassInstNum = 0l; // total class instance num so far
        int propInstNum = 0; // total property instance num in this
        // department
        long totalPropInstNum = 0l; // total property instance num so far

        Generator.LOGGER.debug("External Seed={} Interal Seed={}", univState.getGlobalState().getBaseSeed(),
                univState.getSeed());

        Generator.LOGGER.info("CLASS INSTANCE# (TOTAL-SO-FAR)");
        Generator.LOGGER.info("----------------------------");
        for (int i = 0; i < Ontology.CLASS_NUM; i++) {
            Generator.LOGGER.debug("{} {} ({})", Ontology.CLASS_TOKEN[i], univState.getInstances()[i].logNum,
                    univState.getGlobalState().getTotalInstances(i));
            classInstNum += univState.getInstances()[i].logNum;
            totalClassInstNum += univState.getGlobalState().getTotalInstances(i);
        }

        Generator.LOGGER.info("TOTAL: {}", classInstNum);
        Generator.LOGGER.info("TOTAL SO FAR: {}", totalClassInstNum);

        Generator.LOGGER.info("PROPERTY INSTANCE# (TOTAL-SO-FAR)");
        Generator.LOGGER.info("-------------------------------");
        for (int i = 0; i < Ontology.PROP_NUM; i++) {
            Generator.LOGGER.debug("{} {} ({})", Ontology.PROP_TOKEN[i], univState.getProperties()[i].logNum,
                    univState.getGlobalState().getTotalProperties(i));
            propInstNum += univState.getProperties()[i].logNum;
            totalPropInstNum += univState.getGlobalState().getTotalProperties(i);
        }
        Generator.LOGGER.info("TOTAL: {}", propInstNum);
        Generator.LOGGER.info("TOTAL SO FAR: {}", totalPropInstNum);

        if (!univState.getGlobalState().isQuietMode()) {
            System.out.println("CLASS INSTANCE #: " + classInstNum + ", TOTAL SO FAR: " + totalClassInstNum);
            System.out.println("PROPERTY INSTANCE #: " + propInstNum + ", TOTAL SO FAR: " + totalPropInstNum);
            System.out.println();
        }

    }

}