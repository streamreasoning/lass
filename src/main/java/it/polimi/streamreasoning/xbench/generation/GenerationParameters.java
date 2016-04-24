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
     * size of the pool of TOPICS in one discussion
     */
    public static final int TOPIC_NUMBER = 100; //must >= max faculty # * FACULTY_COURSE_MAX
    /**
     * size of the pool of TRENDING TOPICS in one discussion
     */
    public static final int TRENDING_TOPIC_NUMBER = 100; //must >= max faculty # * FACULTY_GRADCOURSE_MAX

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
     * minimum number of departments in a university
     */
    public static final int DISCUSSION_MIN = 15;
    /**
     * maximum number of departments in a university
     */
    public static final int DISCUSSION_MAX = 25;
    //must: DISCUSSION_MAX - DISCUSSION_MIN + 1 <> 2 ^ n


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
    /**
     * minimum number of publications of a full professor
     */
    public static final int FULLPROF_PUB_MIN = 15;
    /**
     * maximum number of publications of a full professor
     */
    public static final int FULLPROF_PUB_MAX = 20;
    /**
     * minimum number of publications of an associate professor
     */
    public static final int ASSOPROF_PUB_MIN = 10;
    /**
     * maximum number of publications of an associate professor
     */
    public static final int ASSOPROF_PUB_MAX = 18;
    /**
     * minimum number of publications of an assistant professor
     */
    public static final int ASSTPROF_PUB_MIN = 5;
    /**
     * maximum number of publications of an assistant professor
     */
    public static final int ASSTPROF_PUB_MAX = 10;
    /**
     * minimum number of publications of a graduate student
     */
    public static final int GRADSTUD_PUB_MIN = 0;
    /**
     * maximum number of publications of a graduate student
     */
    public static final int GRADSTUD_PUB_MAX = 5;
    /**
     * minimum number of publications of a lecturer
     */
    public static final int LEC_PUB_MIN = 0;
    /**
     * maximum number of publications of a lecturer
     */
    public static final int LEC_PUB_MAX = 5;
    /**
     * minimum number of  research topics related to a publication
     */
    public static final int PUB_RES_MIN = 1;
    /**
     * maximum number of research topics related to a publication
     */
    public static final int PUB_RES_MAX = 10;
    /**
     * minimum number of courses taught by a faculty
     */
    public static final int FACULTY_COURSE_MIN = 1;
    /**
     * maximum number of courses taught by a faculty
     */
    public static final int FACULTY_COURSE_MAX = 2;
    /**
     * minimum number of graduate courses taught by a faculty
     */
    public static final int FACULTY_GRADCOURSE_MIN = 1;
    /**
     * maximum number of graduate courses taught by a faculty
     */
    public static final int FACULTY_GRADCOURSE_MAX = 2;
    /**
     * minimum number of research by a professor
     */
    public static final int PROFESSOR_RESEARCH_MIN = 1;
    /**
     * maximum number of research by a professor
     */
    public static final int PROFESSOR_RESEARCH_MAX = 5;
    /**
     * minimum number of courses taken by a undergraduate student
     */
    public static final int UNDERSTUD_COURSE_MIN = 2;
    /**
     * maximum number of courses taken by a undergraduate student
     */
    public static final int UNDERSTUD_COURSE_MAX = 4;
    /**
     * minimum number of courses taken by a graduate student
     */
    public static final int GRADSTUD_COURSE_MIN = 1;
    /**
     * maximum number of courses taken by a graduate student
     */
    public static final int GRADSTUD_COURSE_MAX = 3;

    public static final int NEWS_MIN = 7;
    /**
     * maximum number of full professors in a department
     */
    public static final int NEWS_MAX = 10;
    /**
     * minimum number of associate professors in a department
     */
    public static final int COMM_MIN = 10;
    /**
     * maximum number of associate professors in a department
     */
    public static final int COMM_MAX = 14;
    /**
     * minimum number of assistant professors in a department
     */
    public static final int VIP_MIN = 8;
    /**
     * maximum number of assistant professors in a department
     */
    public static final int VIP_MAX = 11;
    /**
     * minimum number of lecturers in a department
     */
    public static final int LEC_MIN = 5;
    /**
     * maximum number of lecturers in a department
     */
    public static final int LEC_MAX = 7;

    /**
     * minimum number of lecturers in a department
     */
    public static final int PUB_SOFTWARE_MIN = 1;
    /**
     * maximum number of lecturers in a department
     */
    public static final int PUB_SOFTWARE_MAX = 1;

    /**
     * minimum number of lecturers in a department
     */
    public static final int PUB_SOFTWARE_VERSION_MIN = 1;
    /**
     * maximum number of lecturers in a department
     */
    public static final int PUB_SOFTWARE_VERSION_MAX = 5;


    /**
     * minimum ratio of undergraduate students to faculties in a department
     */
    public static final int R_UNDERSTUD_FACULTY_MIN = 1;
    /**
     * maximum ratio of undergraduate students to faculties in a department
     */
    public static final int R_UNDERSTUD_FACULTY_MAX = 1;
    /**
     * minimum ratio of graduate students to faculties in a department
     */
    public static final int R_GRADSTUD_FACULTY_MIN = 1;
    /**
     * maximum ratio of graduate students to faculties in a department
     */
    public static final int R_GRADSTUD_FACULTY_MAX = 1;


    //MUST: FACULTY_COURSE_MIN >= R_GRADSTUD_FACULTY_MAX / R_STKH_EXPT_MIN;
    /**
     * minimum ratio of graduate students to TA in a department
     */
    public static final int R_STKH_EXPT_MIN = 3;
    /**
     * maximum ratio of graduate students to TA in a department
     */
    public static final int R_STKH_EXPT_MAX = 5;
    /**
     * minimum ratio of graduate students to RA in a department
     */
    public static final int R_STKH_ATT_MIN = 3;
    /**
     * maximum ratio of graduate students to RA in a department
     */
    public static final int R_STKH_ATT_MAX = 5;
    /**
     * average ratio of undergraduate students to undergraduate student advising professors
     */
    public static final int R_UNDERSTUD_ADVISOR = 5;
    /**
     * average ratio of graduate students to graduate student advising professors
     */
    public static final int R_GRADSTUD_ADVISOR = 1;


}
