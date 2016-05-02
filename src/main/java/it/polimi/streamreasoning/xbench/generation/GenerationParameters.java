package it.polimi.streamreasoning.xbench.generation;

public class GenerationParameters {

    ///////////////////////////////////////////////////////////////////////////
    //restrictions for data generation
    ///////////////////////////////////////////////////////////////////////////
    /**
     * size of the pool of universities
     */
    public static final int SITES = 1000;


    /**
     * minimum number of departments in a university
     */
    public static final int DISCUSSION_MIN = 10;
    /**
     * maximum number of departments in a university
     */
    public static final int DISCUSSION_MAX = 15;
    //must: DISCUSSION_MAX - DISCUSSION_MIN + 1 <> 2 ^ n

    /**
     * MIN size of the pool of TAGS in one dicussion
     */
    public static final int TAGS_NUM_MIN = 30;
    /**
     * MAX size of the pool of rTAGS in one dicussion
     */
    public static final int TAGS_NUM_MAX = 90;


    /**
     * MIN size of the pool of TOPIC in one dicussion
     */
    public static final int TOPIC_NUM_MIN = 30;
    /**
     * MAX size of the pool of TOPIC in one dicussion
     */
    public static final int TOPIC_NUM_MAX = 90;


    /**
     * MIN size of the pool of TRENDING_TOPIC in one dicussion
     */
    public static final int TRENDING_TOPIC_NUM_MIN = 30;
    /**
     * MAX size of the pool of TRENDING_TOPIC in one dicussion
     */
    public static final int TRENDING_TOPIC_NUM_MAX = 90;


    /**
     * minimum number of research groups in a department
     */
    public static final int EVENT_MIN = 10;
    /**
     * maximum number of research groups in a department
     */
    public static final int EVENT_MAX = 20;
    /**
     * minimum number of full professors in a department
     */

    /**
     * minimum number of research groups in a department
     */
    public static final int EVENT_ATTENDEE_MIN = 0;
    /**
     * maximum number of research groups in a department
     */
    public static final int EVENT_ATTENDEE_MAX = 20;
    /**
     * minimum number of full professors in a department
     */

    public static final int ACTION_MIN = 0;
    public static final int ACTION_MAX = 30;

    /**
     * maximum number of research groups in a department
     */
    public static final int REACTION_DEEPNESS_MIN = 0;
    public static final int REACTION_DEEPNESS_MAX = 5;

    public static final int MICROPOST_MENTIONS_MIN = 0;
    public static final int MICROPOST_MENTIONS_MAX = 3;

    public static final int MICROPOST_TAG_MIN = 0;
    public static final int MICROPOST_TAG_MAX = 5;

    public static final int MICROPOST_TOPIC_MIN = 0;
    public static final int MICROPOST_TOPIC_MAX = 5;

    public static final int MICROPOST_TRENDING_TOPIC_MIN = 0;
    public static final int MICROPOST_TRENDING_TOPIC_MAX = 2;

    public static final int MICROPOST_LIKE_MIN = 0;
    public static final int MICROPOST_LIKE_MAX = 2;

    public static final int MICROPOST_COMMENT_MIN = 0;
    public static final int MICROPOST_COMMENT_MAX = 2;

    public static final int MICROPOST_SHARE_MIN = 0;
    public static final int MICROPOST_SHARE_MAX = 2;

    public static final int MEDIAPOST_MENTIONS_MIN = 0;
    public static final int MEDIAPOST_MENTIONS_MAX = 3;

    public static final int MEDIAPOST_TAG_MIN = 0;
    public static final int MEDIAPOST_TAG_MAX = 5;

    public static final int MEDIAPOST_TOPIC_MIN = 0;
    public static final int MEDIAPOST_TOPIC_MAX = 5;

    public static final int MEDIAPOST_TRENDING_TOPIC_MIN = 0;
    public static final int MEDIAPOST_TRENDING_TOPIC_MAX = 2;

    public static final int MEDIAPOST_LIKE_MIN = 0;
    public static final int MEDIAPOST_LIKE_MAX = 2;

    public static final int MEDIAPOST_COMMENT_MIN = 0;
    public static final int MEDIAPOST_COMMENT_MAX = 2;

    public static final int MEDIAPOST_SHARE_MIN = 0;
    public static final int MEDIAPOST_SHARE_MAX = 2;

    public static final int BLOGPOST_MENTIONS_MIN = 0;
    public static final int BLOGPOST_MENTIONS_MAX = 3;

    public static final int BLOGPOST_TAG_MIN = 0;
    public static final int BLOGPOST_TAG_MAX = 5;

    public static final int BLOGPOST_TOPIC_MIN = 0;
    public static final int BLOGPOST_TOPIC_MAX = 12;

    public static final int BLOGPOST_TRENDING_TOPIC_MIN = 0;
    public static final int BLOGPOST_TRENDING_TOPIC_MAX = 3;

    public static final int BLOGPOST_LIKE_MIN = 0;
    public static final int BLOGPOST_LIKE_MAX = 2;

    public static final int BLOGPOST_COMMENT_MIN = 0;
    public static final int BLOGPOST_COMMENT_MAX = 2;

    public static final int BLOGPOST_SHARE_MIN = 0;
    public static final int BLOGPOST_SHARE_MAX = 2;

    /**FOLLOWERS**/

    /**
     * minimum number of lecturers in a department
     */
    public static final int COM_FOLLOWER_NUM_MIN = 5000;
    /**
     * maximum number of lecturers in a department
     */
    public static final int COM_FOLLOWER_NUM_MAX = 1000000;

    /**
     * minimum number of lecturers in a department
     */
    public static final int VIP_FOLLOWER_NUM_MIN = 500000;
    /**
     * maximum number of lecturers in a department
     */
    public static final int VIP_FOLLOWER_NUM_MAX = 100000000;

    /**
     * minimum number of lecturers in a department
     */
    public static final int NEWS_FOLLOWER_NUM_MIN = 10000;
    /**
     * maximum number of lecturers in a department
     */
    public static final int NEWS_FOLLOWER_NUM_MAX = 500000;

    /**
     * minimum number of lecturers in a department
     */
    public static final int PEEP_FOLLOWER_NUM_MIN = 0;
    /**
     * maximum number of lecturers in a department
     */
    public static final int PEEP_FOLLOWER_NUM_MAX = 500;


    public static final int PERSON_MIN = 500;
    public static final int PERSON_MAX = 2000;


    public static final double R_TOPIC_FOLLOWER_PERSON = 0.3;
    public static final double R_TRENDING_TOPIC_FOLLOWER_PERSON = 0.1;
    public static final double R_EXPERT_PERSON = 0.05;
    public static final double R_ATTENDEE_PERSON = 0.05;
    public static final double R_COMM_PERSON = 0.05;
    public static final double R_VIP_PERSON = 0.05;
    public static final double R_NEWS_PERSON = 0.05;
    public static final double R_INVOLVED_PERSON = 0.25;


    public static final int FOLLOWED_TOPIC_MIN = 2;
    /**
     * maximum number of courses taken by a undergraduate student
     */
    public static final int FOLLOWED_TOPIC_MAX = 4;
    /**
     * minimum number of courses taken by a graduate student
     */
    public static final int FOLLOWED_TT_MIN = 1;
    /**
     * maximum number of courses taken by a graduate student
     */
    public static final int FOLLOWED_TT_MAX = 3;


}
