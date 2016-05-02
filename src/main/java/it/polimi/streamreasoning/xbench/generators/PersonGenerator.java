package it.polimi.streamreasoning.xbench.generators;

import it.polimi.streamreasoning.xbench.generation.GenerationParameters;
import it.polimi.streamreasoning.xbench.model.Ontology;
import it.polimi.streamreasoning.xbench.state.SiteState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

class PersonGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonGenerator.class);

    private final SiteState univState;
    private final ActionGenerator aGen;

    public PersonGenerator(SiteState univState, ActionGenerator gen) {
        this.univState = univState;
        this.aGen = gen;

    }


    private void personInstanceGen(SiteState univState, int type, int index) {
        // person properties
        univState.getWriter().addProperty(Ontology.CS_P_IS_PROMINENT_OF, Ontology.CS_C_SITE,
                univState.getId(Ontology.CS_C_SITE, univState.getRandom(GenerationParameters.SITES)));
        univState.getWriter().addProperty(Ontology.CS_P_IS_TECHNICAL_MEMBER_OF, Ontology.CS_C_SITE,
                univState.getId(Ontology.CS_C_SITE, univState.getRandom(GenerationParameters.SITES)));
        univState.getWriter().addProperty(Ontology.CS_P_IS_ACTIVE_MEMBER_OF, Ontology.CS_C_SITE,
                univState.getId(Ontology.CS_C_SITE, univState.getRandom(GenerationParameters.SITES)));

        univState.getWriter().addProperty(Ontology.CS_DP_EMAIL, univState.getEmail(type, index), false);

        int followersNum = 0;
        switch (type) {
            case Ontology.CS_C_NEWS:
                followersNum = univState.getRandomFromRange(GenerationParameters.NEWS_FOLLOWER_NUM_MIN, GenerationParameters.NEWS_FOLLOWER_NUM_MAX);
                break;
            case Ontology.CS_C_VIP:
                followersNum = univState.getRandomFromRange(GenerationParameters.VIP_FOLLOWER_NUM_MIN, GenerationParameters.VIP_FOLLOWER_NUM_MAX);
                break;
            case Ontology.CS_C_COMMERCIAL:
                followersNum = univState.getRandomFromRange(GenerationParameters.COM_FOLLOWER_NUM_MIN, GenerationParameters.COM_FOLLOWER_NUM_MAX);
                break;
            default:
                followersNum = univState.getRandomFromRange(GenerationParameters.PEEP_FOLLOWER_NUM_MIN, GenerationParameters.PEEP_FOLLOWER_NUM_MAX);
                break;
        }

        univState.getWriter().addProperty(Ontology.CS_DP_FOLLOWERS_NUM, followersNum + "", false);

        univState.getWriter().addProperty(Ontology.CS_DP_NAME, univState.getRelativeName(type, index), false);

        if (univState.getBoolean()) {
            univState.getWriter().addProperty(Ontology.CS_P_IS_INFLUENCED_BY, _assignInfluencer(univState), true);
        }
    }

    //SUB CLASSES OF PERSON

    /**
     * Generates a TA instance according to the specified information.
     */
    public void expertInstanceGen(SiteState univState) {
        for (int i = 0; i < univState.getInstances()[Ontology.CS_C_EXPERT].total; i++) {
            int expertClass = univState.getRandomFromRange(Ontology.CS_C_TOPIC_FOLLOWER, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER);
            int expert = univState.getRandom(univState.getInstances()[expertClass].total - 1);
            int expertiseClass = univState.getRandomFromRange(Ontology.CS_C_TOPIC, Ontology.CS_C_TRENDING_TOPIC);
            int expertise = univState.getRandom(univState.getInstances()[expertiseClass].total - 1);

            univState.getWriter().startAboutSection(Ontology.CS_C_EXPERT, univState.getId(expertClass, expert));
            univState.getWriter().addProperty(Ontology.CS_P_EXPERT_IN, univState.getId(expertiseClass, expertise), true);
            univState.getWriter().endSection(Ontology.CS_C_EXPERT);
        }
    }

    private void contributorInstanceGen(SiteState univState, int type, int index) {
        personInstanceGen(univState, type, index);
    }

    /**
     * Generates properties for the specified student instance.
     *
     * @param type  Type of the student.
     * @param index Index of the instance within its type.
     */
    public void stakeholderInstanceGen(SiteState univState, int type, int index) {
        personInstanceGen(univState, type, index);
        univState.getWriter().addProperty(Ontology.CS_P_IS_INVOLVED_IN,
                univState.getId(Ontology.CS_C_DISCUSSION, univState.getInstances()[Ontology.CS_C_DISCUSSION].count - 1), true);
    }

    //SUB CLASSES OF CONTRIBUTOR

    public void moderatorInstanceGen(SiteState univState, int type, int index) {
        //TODO
    }

    public void techInstanceGen(SiteState univState, int type, int index) {
        //TODO
    }

    /**
     * Generates an attendee instances chosing between stakeholder
     */
    public void attendeeInstanceGen(SiteState univState) {

        int size = univState.getInstances()[Ontology.CS_C_ATTENDEE].total;
        int startingIndex = 0;

        int eventNum = univState.getEventNum();

        for (int e = 0; e < eventNum; e++) {

            int tf_att = univState.getRandomFromRange(GenerationParameters.EVENT_ATTENDEE_MIN, GenerationParameters.EVENT_ATTENDEE_MAX);

            for (int tf = 0; tf < tf_att; tf++) {
                univState.getWriter().startAboutSection(Ontology.CS_C_ATTENDEE, univState.getId(Ontology.CS_C_TOPIC_FOLLOWER, tf));
                univState.getWriter().addProperty(Ontology.CS_P_PARTICIPATES, univState.getId(Ontology.CS_C_EVENT, e), true);
                univState.getWriter().endSection(Ontology.CS_C_ATTENDEE);
            }

            int ttatt = univState.getRandomFromRange(GenerationParameters.EVENT_ATTENDEE_MIN, GenerationParameters.EVENT_ATTENDEE_MAX);

            for (int ttf = 0; ttf < ttatt; ttf++) {
                univState.getWriter().startAboutSection(Ontology.CS_C_ATTENDEE, univState.getId(Ontology.CS_C_TRENDING_TOPIC_FOLLOWER, ttf));
                univState.getWriter().addProperty(Ontology.CS_P_PARTICIPATES, univState.getId(Ontology.CS_C_EVENT, e), true);
                univState.getWriter().endSection(Ontology.CS_C_ATTENDEE);
            }

        }

        //NB: information about person have been already generated as topic follower or trending topic follower as well as actions
    }

    /**
     * Generates properties for the specified faculty instance.
     *
     * @param type  Type of the faculty.
     * @param index Index of the instance within its type.
     */
    public void participantInstanceGen(SiteState univState, int type, int index) {
        univState.getWriter().startSection(type, univState.getId(type, index));

        contributorInstanceGen(univState, type, index);

        //Discussion Lead
        if (index == univState.getChair().id && type == univState.getChair().type) {
            univState.getWriter().addProperty(Ontology.CS_P_LEADS,
                    univState.getId(Ontology.CS_C_DISCUSSION, univState.getInstances()[Ontology.CS_C_DISCUSSION].count - 1), true);
        }

        //Discussion Participation
        univState.getWriter().addProperty(Ontology.CS_P_PARTICIPATES,
                univState.getId(Ontology.CS_C_DISCUSSION, univState.getInstances()[Ontology.CS_C_DISCUSSION].count - 1), true);


        univState.getWriter().endSection(type);
    }

    //SUB CLASSES OF PARTICIPANT

    /**
     * Generates a lecturer instance.
     *
     * @param index Index of the lecturer.
     */
    public void involvedInstanceGen(SiteState univState, int index) {
        participantInstanceGen(univState, Ontology.CS_C_INVOLVED, index);
        aGen.assignActions(univState, index, Ontology.CS_C_INVOLVED, GenerationParameters.ACTION_MIN, GenerationParameters.ACTION_MAX);
    }

    /**
     * Generates properties for a professor instance.
     *
     * @param type  Type of the professor.
     * @param index Index of the intance within its type.
     */
    public void influencerInstanceGen(SiteState univState, int type, int index) {
        participantInstanceGen(univState, type, index);
    }

    //SUB CLASSES OF INFLUENCER

    /**
     * Generates a full professor instances.
     *
     * @param index Index of the full professor.
     */
    public void newsInstanceGen(SiteState univState, int index) {
        influencerInstanceGen(univState, Ontology.CS_C_NEWS, index);
        aGen.assignActions(univState, index, Ontology.CS_C_NEWS, GenerationParameters.ACTION_MIN,
                GenerationParameters.ACTION_MAX);
    }

    /**
     * Generates an associate professor instance.
     *
     * @param index Index of the associate professor.
     */
    public void comInstanceGen(SiteState univState, int index) {
        influencerInstanceGen(univState, Ontology.CS_C_COMMENT, index);
        aGen.assignActions(univState, index, Ontology.CS_C_COMMENT, GenerationParameters.ACTION_MIN,
                GenerationParameters.ACTION_MAX);
    }

    /**
     * Generates an assistant professor instance.
     *
     * @param index Index of the assistant professor.
     */
    public void vipInstanceGen(SiteState univState, int index) {
        influencerInstanceGen(univState, Ontology.CS_C_VIP, index);
        aGen.assignActions(univState, index, Ontology.CS_C_VIP, GenerationParameters.ACTION_MIN,
                GenerationParameters.ACTION_MAX);
    }

    //SUB CLASSES OF STAKEHOLDER

    /**
     * Generates an undergraduate student instance.
     *
     * @param index Index of the undergraduate student.
     */
    public void topicFollowerInstanceGen(SiteState univState, int index) {

        univState.getWriter().startSection(Ontology.CS_C_TOPIC_FOLLOWER, univState.getId(Ontology.CS_C_TOPIC_FOLLOWER, index));
        this.stakeholderInstanceGen(univState, Ontology.CS_C_TOPIC_FOLLOWER, index);

        int n = univState.getRandomFromRange(GenerationParameters.FOLLOWED_TOPIC_MIN, GenerationParameters.FOLLOWED_TOPIC_MAX);
        ArrayList<Integer> topics = univState.getRandomList(n, 0, univState.getTopicNum() - 1);

        for (Integer i : topics) {
            univState.getWriter().addProperty(Ontology.CS_P_IS_INTERESTED_IN,
                    univState.getId(Ontology.CS_C_TOPIC, i), true);
        }

        univState.getWriter().endSection(Ontology.CS_C_TOPIC_FOLLOWER);


        aGen.assignActions(univState, index, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER, GenerationParameters.ACTION_MIN,
                GenerationParameters.ACTION_MAX);
    }

    /**
     * Generates a graduate student instance.
     *
     * @param index Index of the graduate student.
     */
    public void trendingTopicFollowerInstanceGen(SiteState univState, int index) {
        univState.getWriter().startSection(Ontology.CS_C_TRENDING_TOPIC_FOLLOWER, univState.getId(Ontology.CS_C_TOPIC_FOLLOWER, index));
        stakeholderInstanceGen(univState, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER, index);
        int n = univState.getRandomFromRange(GenerationParameters.FOLLOWED_TT_MIN,
                GenerationParameters.FOLLOWED_TT_MAX);
        ArrayList<Integer> tt = univState.getRandomList(n, 0, univState.getTrendingTopicNum() - 1);

        for (Integer i : tt) {
            univState.getWriter().addProperty(Ontology.CS_P_IS_INTERESTED_IN,
                    univState.getId(Ontology.CS_C_TRENDING_TOPIC, i), true);
        }

        univState.getWriter().endSection(Ontology.CS_C_TRENDING_TOPIC_FOLLOWER);

        aGen.assignActions(univState, index, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER, GenerationParameters.ACTION_MIN,
                GenerationParameters.ACTION_MAX);
    }


    /**
     * Select an advisor from the professors.
     *
     * @return Id of the selected professor.
     */
    public String _assignInfluencer(SiteState univState) {
        int profType;
        int index;

        profType = univState.getRandomFromRange(Ontology.CS_C_NEWS, Ontology.CS_C_VIP);
        index = univState.getRandom(univState.getInstances()[profType].total);
        return univState.getId(profType, index);
    }

}