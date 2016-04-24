package it.polimi.streamreasoning.xbench.generators;

import it.polimi.streamreasoning.xbench.state.SiteState;
import it.polimi.streamreasoning.xbench.model.ActionModel;
import it.polimi.streamreasoning.xbench.generation.GenerationParameters;
import it.polimi.streamreasoning.xbench.model.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ActionGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionGenerator.class);

    private final SiteState univState;


    public ActionGenerator(SiteState univState) {
        this.univState = univState;
    }

    /**
     * Generates publication instances. These publications are assigned to some
     * faculties and graduate students before.
     */
    public void genActionInstances(SiteState univState) {
        for (ActionModel ai : univState.getPublications()) {
            genActionInstance(univState, ai);
        }
    }

    /**
     * Generates a action instance.
     *
     * @param action Information of the action.
     */
    public void genActionInstance(SiteState univState, ActionModel action) {
        univState.updateCount(action.type);

        String graphName = Ontology.CLASS_TOKEN[action.type] + "-" + action.id;

        univState.getWriter().startSection(action.type, action.id);
        univState.getWriter().addProperty(Ontology.CS_DP_NAME, action.name, false);
        univState.getWriter().addProperty(Ontology.CS_P_PERFORMED_BY, action.author, true);


        if (action.isReaction() && action.reactionTo != null) {
            if (action.type == Ontology.CS_C_SHARED_POST) {
                univState.getWriter().addProperty(Ontology.CS_P_IS_SHARING, action.reactionTo.id, true);
            } else {
                univState.getWriter().addProperty(Ontology.CS_P_IS_REACTION_TO, action.reactionTo.id, true);
            }
        }

        if (action.tags != null) {
            for (int i : action.tags) {
                univState.getWriter().addProperty(Ontology.CS_P_IS_TAGGED_WITH, univState.getId(Ontology.CS_C_TAG, i),
                        true);
            }
        }

        if (action.topics != null) {
            for (int i : action.topics) {
                univState.getWriter().addProperty(Ontology.CS_P_CONTAINS, univState.getId(Ontology.CS_C_TOPIC, i),
                        true);
            }
        }

        if (action.ttopics != null) {
            for (int i : action.ttopics) {
                univState.getWriter().addProperty(Ontology.CS_P_CONTAINS, univState.getId(Ontology.CS_C_TRENDING_TOPIC, i),
                        true);
            }
        }

        if (action.mentions != null) {
            for (String mention : action.mentions) {
                univState.getWriter().addProperty(Ontology.CS_P_CONTAINS, mention, true);
            }
        }

        univState.getWriter().endSection(action.type);
        univState.getStreamWriter().append(graphName);

    }

    //ASSIGNMENTS

    public void assignActions(SiteState univState, int authorId, int authorClass, int min, int max) {

        int num = univState.getRandomFromRange(min, max);
        for (int i = 0; i < num; i++) {
            int actionType = univState.getRandomFromRange(Ontology.CS_C_MICROPOST, Ontology.CS_C_BLOG_POST);
            univState.getPublications().add(genAction(univState, actionType, authorClass, authorId,
                    univState.getRandomFromRange(
                            GenerationParameters.REACTION_DEEPNESS_MIN, GenerationParameters.REACTION_DEEPNESS_MAX)));
        }
    }

    private ActionModel genAction(SiteState univState, int type, int authorClass, int authorId, int deepness) {
        String author = univState.getId(authorClass, authorId);
        ActionModel action = new ActionModel();
        action.id = univState.getId(type, authorId, author);
        action.name = univState.getRelativeName(type, authorId);
        action.author = author;
        action.type = type;
        if ((action.type != Ontology.CS_C_LIKE || action.type != Ontology.CS_C_SHARED_POST) && deepness > 0) {
            action.mentions = assingMentions(univState, type);
            action.tags = assingTags(univState, type);
            action.topics = assingTopics(univState, type);
            action.ttopics = assingTrendingTopics(univState, type);
            genReactions(univState, action, deepness--);
        }
        return action;
    }

    public String[] assingMentions(SiteState univState, int actionType) {

        int mentionNum = 0;
        String[] tags;
        switch (actionType) {
            case Ontology.CS_C_MICROPOST:
                mentionNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_MENTIONS_MIN, GenerationParameters.MICROPOST_MENTIONS_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                mentionNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_MENTIONS_MIN, GenerationParameters.BLOGPOST_MENTIONS_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                mentionNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_MENTIONS_MIN, GenerationParameters.MEDIAPOST_MENTIONS_MAX);
                break;
            default:
                break;
        }

        tags = new String[mentionNum];

        for (int i = 0; i < mentionNum; i++) {
            int personType = univState.getRandomFromRange(Ontology.CS_C_NEWS, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER);
            int randomId = univState.getRandom(univState.getInstances()[personType].total - 1);
            tags[i] = univState.getId(personType, randomId);
        }

        return tags;
    }

    public Integer[] assingTags(SiteState univState, int actionType) {

        int tagNum = 0;
        Integer[] tags;
        switch (actionType) {
            case Ontology.CS_C_MICROPOST:
                tagNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_TAG_MIN, GenerationParameters.MICROPOST_TAG_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                tagNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_TAG_MIN, GenerationParameters.BLOGPOST_TAG_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                tagNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_TAG_MIN, GenerationParameters.MEDIAPOST_TAG_MAX);
                break;
            default:
                break;
        }

        tags = new Integer[tagNum];
        univState.getRandomList(tagNum, 0, univState.getInstances()[Ontology.CS_C_TAG].num - 1).toArray(tags);

        return tags;
    }

    public Integer[] assingTopics(SiteState univState, int actionType) {

        int topicNum = 0;
        Integer[] topics;
        switch (actionType) {
            case Ontology.CS_C_MICROPOST:
                topicNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_TOPIC_MIN, GenerationParameters.MICROPOST_TOPIC_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                topicNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_TOPIC_MIN, GenerationParameters.BLOGPOST_TOPIC_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                topicNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_TOPIC_MIN, GenerationParameters.MEDIAPOST_TOPIC_MAX);
                break;
            default:
                break;
        }

        topics = new Integer[topicNum];
        univState.getRandomList(topicNum, 0, univState.getInstances()[Ontology.CS_C_TOPIC].num - 1).toArray(topics);

        return topics;
    }

    public Integer[] assingTrendingTopics(SiteState univState, int actionType) {

        int topicNum = 0;
        Integer[] topics;
        switch (actionType) {
            case Ontology.CS_C_MICROPOST:
                topicNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_TRENDING_TOPIC_MIN, GenerationParameters.MICROPOST_TRENDING_TOPIC_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                topicNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_TRENDING_TOPIC_MIN, GenerationParameters.BLOGPOST_TRENDING_TOPIC_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                topicNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_TRENDING_TOPIC_MIN, GenerationParameters.MEDIAPOST_TRENDING_TOPIC_MAX);
                break;
            default:
                break;
        }

        topics = new Integer[topicNum];
        univState.getRandomList(topicNum, 0, univState.getInstances()[Ontology.CS_C_TRENDING_TOPIC].num - 1).toArray(topics);

        return topics;
    }

    private void genReactions(SiteState univState, ActionModel source, int deepness) {
        genLikes(univState, source, deepness);
        genComments(univState, source, deepness);
        genShares(univState, source, deepness);
    }

    private void genLikes(SiteState univState, ActionModel source, int deepness) {

        int likesNum = 0;

        switch (source.type) {
            case Ontology.CS_C_MICROPOST:
                likesNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_LIKE_MIN, GenerationParameters.MICROPOST_LIKE_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                likesNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_LIKE_MIN, GenerationParameters.BLOGPOST_LIKE_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                likesNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_LIKE_MIN, GenerationParameters.MEDIAPOST_LIKE_MAX);
                break;
            default:
                break;
        }

        for (int i = 0; i < likesNum; i++) {

            int personType = univState.getRandomFromRange(Ontology.CS_C_NEWS, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER);
            int randomId = univState.getRandom(univState.getInstances()[personType].total - 1);
            //generate as it was a normal action
            ActionModel reaction = genAction(univState, Ontology.CS_C_LIKE, personType, randomId, deepness--);
            reaction.reactionTo = source;
            univState.getPublications().add(reaction);

        }
    }

    private void genComments(SiteState univState, ActionModel source, int deepness) {
        int commentsNum = 0;

        switch (source.type) {
            case Ontology.CS_C_MICROPOST:
                commentsNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_COMMENT_MIN, GenerationParameters.MICROPOST_COMMENT_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                commentsNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_COMMENT_MIN, GenerationParameters.BLOGPOST_COMMENT_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                commentsNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_COMMENT_MIN, GenerationParameters.MEDIAPOST_COMMENT_MAX);
                break;
            default:
                break;
        }

        for (int i = 0; i < commentsNum; i++) {
            int personType = univState.getRandomFromRange(Ontology.CS_C_NEWS, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER);
            int randomId = univState.getRandom(univState.getInstances()[personType].total - 1);
            //generate as it was a normal action
            ActionModel reaction = genAction(univState, Ontology.CS_C_COMMENT, personType, randomId, deepness--);
            reaction.reactionTo = source;
            univState.getPublications().add(reaction);
        }
    }

    private void genShares(SiteState univState, ActionModel source, int deepness) {
        int sharesNum = 0;

        switch (source.type) {
            case Ontology.CS_C_MICROPOST:
                sharesNum = univState.getRandomFromRange(GenerationParameters.MICROPOST_SHARE_MIN, GenerationParameters.MICROPOST_SHARE_MAX);
                break;
            case Ontology.CS_C_BLOG_POST:
                sharesNum = univState.getRandomFromRange(GenerationParameters.BLOGPOST_SHARE_MIN, GenerationParameters.BLOGPOST_SHARE_MAX);
                break;
            case Ontology.CS_C_MEDIAPOST:
                sharesNum = univState.getRandomFromRange(GenerationParameters.MEDIAPOST_SHARE_MIN, GenerationParameters.MEDIAPOST_SHARE_MAX);
                break;
            default:
                break;
        }

        for (int i = 0; i < sharesNum; i++) {
            int personType = univState.getRandomFromRange(Ontology.CS_C_NEWS, Ontology.CS_C_TRENDING_TOPIC_FOLLOWER);
            int randomId = univState.getRandom(univState.getInstances()[personType].total - 1);
            //generate as it was a normal action
            ActionModel reaction = genAction(univState, Ontology.CS_C_SHARED_POST, personType, randomId, deepness--);
            reaction.reactionTo = source;
            univState.getPublications().add(reaction);
        }
    }

}