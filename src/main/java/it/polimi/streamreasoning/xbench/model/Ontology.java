package it.polimi.streamreasoning.xbench.model;

public class Ontology {

    ///////////////////////////////////////////////////////////////////////////
    // ontology class information
    //
    ///////////////////////////////////////////////////////////////////////////
    /**
     * n/a
     */
    public static final int CS_C_NULL = -1;
    /**
     * University
     */
    public static final int CS_C_SITE = 0;
    /**
     * Department
     */
    public static final int CS_C_DISCUSSION = CS_C_SITE + 1;

    public static final int CS_C_PERSON = CS_C_DISCUSSION + 1;

    public static final int CS_C_CONTRIBUTOR = CS_C_PERSON + 1;
    /**
     * Faculty
     */
    public static final int CS_C_PARTICIPANT = CS_C_CONTRIBUTOR + 1;
    /**
     * Professor
     */
    public static final int CS_C_INFLUENCER = CS_C_PARTICIPANT + 1;
    /**
     * FullProfessor
     */
    public static final int CS_C_NEWS = CS_C_INFLUENCER + 1;
    /**
     * AssociateProfessor
     */
    public static final int CS_C_COMMERCIAL = CS_C_NEWS + 1;
    /**
     * AssistantProfessor
     */
    public static final int CS_C_VIP = CS_C_COMMERCIAL + 1;
    /**
     * Lecturer
     */
    public static final int CS_C_INVOLVED = CS_C_VIP + 1;
    /**
     * Student
     */
    public static final int CS_C_STAKEHOLDER = CS_C_INVOLVED + 1;
    /**
     * UndergraduateStudent
     */
    public static final int CS_C_TOPIC_FOLLOWER = CS_C_STAKEHOLDER + 1;
    /**
     * GraduateStudent
     */
    public static final int CS_C_TRENDING_TOPIC_FOLLOWER = CS_C_TOPIC_FOLLOWER + 1;
    /**
     * TeachingAssistant
     */
    public static final int CS_C_EXPERT = CS_C_TRENDING_TOPIC_FOLLOWER + 1;
    /**
     * ResearchAssistant
     */
    public static final int CS_C_ATTENDEE = CS_C_EXPERT + 1;
    /**
     * DiscussionLeader
     */
    public static final int CS_C_DISCUSSION_LEADER = CS_C_ATTENDEE + 1;
    /**
     * Course
     */
    public static final int CS_C_TOPIC = CS_C_DISCUSSION_LEADER + 1;
    /**
     * GraduateCourse
     */
    public static final int CS_C_TRENDING_TOPIC = CS_C_TOPIC + 1;
    /**
     * Research
     */
    public static final int CS_C_TAG = CS_C_TRENDING_TOPIC + 1;
    /**
     * ResearchGroup
     */
    public static final int CS_C_EVENT = CS_C_TAG + 1;
    /**
     * Publication
     */
    public static final int CS_C_ACTION = CS_C_EVENT + 1;
    /**
     * Article
     */
    public static final int CS_C_POST = CS_C_ACTION + 1;
    /**
     * ConferencePaper
     **/
    public static final int CS_C_MEDIAPOST = CS_C_POST + 1;
    /**
     * JournalArticle
     **/
    public static final int CS_C_MICROPOST = CS_C_MEDIAPOST + 1;
    /**
     * TechnicalReport
     **/
    public static final int CS_C_BLOG_POST = CS_C_MICROPOST + 1;
    /**
     * Book
     **/
    public static final int CS_C_COMMENT = CS_C_BLOG_POST + 1;
    /**
     * Manual
     **/
    public static final int CS_C_LIKE = CS_C_COMMENT + 1;
    /**
     * PublishedSpecification
     **/
    public static final int CS_C_SHARED_POST = CS_C_LIKE + 1;
    /**
     * SoftwareProgram
     **/
    public static final int CS_C_REACTION = CS_C_SHARED_POST + 1;
    /**
     * UnofficialPublication
     **/
    public static final int CS_C_POPULAR_POST = CS_C_REACTION + 1;


    /**
     * class information
     */


    public static final int[][] CLASS_INFO = {
            /* {instance number if not specified, direct super class} */
            // NOTE: the super classes specifed here do not necessarily reflect
            // the entailment of the ontology
            {2, CS_C_NULL}, // CS_C_SITE
            {1, CS_C_NULL}, // CS_C_DISCUSSION
            {1, CS_C_NULL}, //CS_C_PERSON
            {0, CS_C_PERSON}, // CS_C_CONTRIBUTOR
            {0, CS_C_CONTRIBUTOR}, // CS_C_PARTICIPANT
            {0, CS_C_PARTICIPANT}, // CS_C_INFLUENCER
            {0, CS_C_INFLUENCER}, // CS_C_NEWS
            {0, CS_C_INFLUENCER}, // CS_C_COMMERCIAL
            {0, CS_C_INFLUENCER}, // CS_C_VIP
            {0, CS_C_PARTICIPANT}, // CS_C_INVOLVED
            {0, CS_C_PERSON}, // CS_C_STAKEHOLDER
            {0, CS_C_STAKEHOLDER}, // CS_C_TOPIC_FOLLOWER
            {0, CS_C_STAKEHOLDER}, // CS_C_TRENDING_TOPIC_FOLLOWER
            {0, CS_C_PERSON}, // CS_C_EXPERT
            {0, CS_C_CONTRIBUTOR}, // CS_C_ATTENDEE
            {0, CS_C_INFLUENCER}, // CS_C_DISCUSSION_LEADER
            {0, CS_C_NULL}, // CS_C_TOPIC
            {0, CS_C_NULL}, // CS_C_TRENDING_TOPIC
            {0, CS_C_NULL}, // CS_C_TAG
            {0, CS_C_NULL}, // CS_C_EVENT
            {0, CS_C_NULL}, // CS_C_ACTION
            {0, CS_C_ACTION}, // CS_C_POST
            {0, CS_C_POST}, // CS_C_MEDIAPOST
            {0, CS_C_POST}, // CS_C_MICROPOST
            {0, CS_C_POST}, // CS_C_BLOG_POST
            {0, CS_C_REACTION, CS_C_POST}, // CS_C_COMMENT
            {0, CS_C_REACTION}, // CS_C_LIKE
            {0, CS_C_REACTION, CS_C_POST}, // CS_C_SHARED_POST
            {0, CS_C_ACTION}, // CS_C_REACTION
            {0, CS_C_POST} // CS_C_POPULAR_POST
    };
    /**
     * class name strings
     */
    public static final String[] CLASS_TOKEN = {
            "Site", // CS_C_SITE
            "Discussion", // CS_C_DISCUSSION
            "User   ", //CS_C_PERSON
            "Contributor",//CS_C_CONTRIBUTOR
            "Participant", // CS_C_PARTICIPANT
            "Influencer", // CS_C_INFLUENCER
            "News", // CS_C_NEWS
            "Commercial", // CS_C_COMMERCIAL
            "VIP", // CS_C_VIP
            "Involved", // CS_C_INVOLVED
            "Stakeholder", // CS_C_STAKEHOLDER
            "TopicFollower", // CS_C_TOPIC_FOLLOWER
            "TrendingTopicFollower", // CS_C_TRENDING_TOPIC_FOLLOWER
            "Expert", // CS_C_EXPERT
            "Attendee", // CS_C_ATTENDEE
            "DiscussionLeader", // CS_C_DISCUSSION_LEADER
            "Topic", // CS_C_TOPIC
            "TrendingTopic", // CS_C_TRENDING_TOPIC
            "Tag", // CS_C_TAG
            "Event", // CS_C_EVENT
            "Action", // CS_C_ACTION
            "Post", //CS_C_POST
            "MediaPost", //CS_C_MEDIAPOST
            "MicroPost", //CS_C_MICROPOST
            "BlogPost", //CS_C_BLOG_POST
            "Comment", //CS_C_COMMENT
            "Like",//CS_C_LIKE
            "SharedPost", //CS_C_SHARED_POST
            "Reaction", //CS_C_REACTION
            "PopularPost" //CS_C_POPULAR_POST

    };
    /**
     * number of classes
     */
    public static final int CLASS_NUM = CLASS_INFO.length;
    /**
     * index of instance-number in the elements of array CLASS_INFO
     */
    public static final int INDEX_NUM = 0;
    /**
     * index of super-class in the elements of array CLASS_INFO
     */
    public static final int INDEX_SUPER = 1;
    ///////////////////////////////////////////////////////////////////////////
    // ontology property information
    ///////////////////////////////////////////////////////////////////////////

    /**
     * InterestedIn
     */
    public static final int CS_P_IS_INTERESTED_IN =0;
    /**
     * postsAbout
     */
    public static final int CS_P_POSTS_ABOUT = CS_P_IS_INTERESTED_IN + 1;
    /**
     * isProminentOf
     */
    public static final int CS_P_IS_PROMINENT_OF = CS_P_POSTS_ABOUT + 1;
    /**
     * isTechnicalMemberOf
     */
    public static final int CS_P_IS_TECHNICAL_MEMBER_OF = CS_P_IS_PROMINENT_OF + 1;
    /**
     * isActiveMemberOf
     */
    public static final int CS_P_IS_ACTIVE_MEMBER_OF = CS_P_IS_TECHNICAL_MEMBER_OF + 1;
    /**
     * isInfluencedBy
     */
    public static final int CS_P_IS_INFLUENCED_BY = CS_P_IS_ACTIVE_MEMBER_OF + 1;
    /**
     * involvedIn
     */
    public static final int CS_P_IS_INVOLVED_IN = CS_P_IS_INFLUENCED_BY + 1;
    /**
     * performedBy
     */
    public static final int CS_P_PERFORMED_BY = CS_P_IS_INVOLVED_IN + 1;
    /**
     * contains
     */
    public static final int CS_P_CONTAINS = CS_P_PERFORMED_BY + 1;

    /**
     * leads
     */
    public static final int CS_P_LEADS = CS_P_CONTAINS + 1;
    /**
     * expertIn
     */
    public static final int CS_P_EXPERT_IN = CS_P_LEADS + 1;
    /**
     * subCommunityOf
     */
    public static final int CS_P_SUBCOMMUNITY_OF = CS_P_EXPERT_IN + 1;
    /**
     * participates
     */
    public static final int CS_P_PARTICIPATES = CS_P_SUBCOMMUNITY_OF + 1;

    /**
    * isTaggedWith
    **/
    public static final int CS_P_IS_TAGGED_WITH = CS_P_PARTICIPATES + 1;
    /**
     * isReactionTo
     **/
    public static final int CS_P_IS_REACTION_TO = CS_P_IS_TAGGED_WITH + 1;

    /**
     *  isSharing
     **/
    public static final int CS_P_IS_SHARING = CS_P_IS_REACTION_TO + 1;

    /**
     * hasEventTag
     **/
    public static final int CS_P_HAS_EVENT_TAG = CS_P_IS_SHARING + 1;


    /**
     * isFollowedBy
     **/
    public static final int CS_P_IS_FOLLOWED_BY = CS_P_HAS_EVENT_TAG + 1;

    /**
     * listedTopic
     **/
    public static final int CS_P_LISTED_TOPIC = CS_P_IS_FOLLOWED_BY + 1;


    /**
     * emailAddress
     */
    public static final int CS_DP_EMAIL = CS_P_LISTED_TOPIC + 1;
    /**
     * telephone
     */
    public static final int CS_DP_FOLLOWERS_NUM = CS_DP_EMAIL + 1;

    /**
     * telephone
     */
    public static final int CS_DP_FOLLOWING_NUM = CS_DP_FOLLOWERS_NUM + 1;

    /**
     * name
     */
    public static final int CS_DP_NAME = CS_DP_FOLLOWING_NUM +1;


    /**
     * property name strings
     */
    public static final String[] PROP_TOKEN = {
            "interestedIn",  // CS_P_IS_INTERESTED_IN
            "postsAbout",//CS_P_POSTS_ABOUT
            "isProminentOf",//CS_P_IS_PROMINENT_OF
            "isTechnicalMemberOf",//CS_P_IS_TECHNICAL_MEMBER_OF
            "isActiveMemberOf",//CS_P_IS_ACTIVE_MEMBER_OF
            "isInfluencedBy",//CS_P_IS_INFLUENCED_BY
            "involvedIn",//CS_P_IS_INVOLVED_IN
            "performedBy",//CS_P_PERFORMED_BY
            "contains",//CS_P_CONTAINS
            "leads",//CS_P_LEADS
            "expertIn",//CS_P_EXPERT_IN
            "subCommunityOf",//CS_P_SUBCOMMUNITY_OF
            "participates",//CS_P_PARTICIPATES
            "isTaggedWith",//CS_P_IS_TAGGED_WITH
            "isReactionTo",//CS_P_IS_REACTION_TO
            "isSharing",//CS_P_IS_SHARING
            "hasEventTag", //CS_P_HAS_EVENT_TAG
            "isFollowedBy", //CS_P_IS_FOLLOWED_BY
            "listedTopic",//CS_P_LISTED_TOPIC
            "emailAddress", //CS_DP_EMAIL
            "followersNumber",//CS_DP_FOLLOWERS_NUM
            "followingNumber", //CS_DP_RESEARCH_INTEREST
            "name" //CS_DP_NAME
    };
    /**
     * number of properties
     */
    public static final int PROP_NUM = PROP_TOKEN.length;

}
