package model;

import java.util.ArrayList;

/**这是新闻详情标签页的数据模型
 * Created by Administrator on 2016/6/16.
 */
public class TabNewDetails {

    public int retcode;
    public  TabNewDetailsData data;

    public class TabNewDetailsData
    {
        public String							    countcommenturl;
        public String							     more;
        public ArrayList<TabNewDetailsDataNews>     news;
        public String							     title;
        public ArrayList<TabNewDetailsDataTopic>    topic;
        public ArrayList<TabNewDetailsDataTopNews>	topnews;

        public class TabNewDetailsDataNews
        {
            public String  comment;
            public String	commentlist;
            public String	commenturl;
            public String	id;
            public String	listimage;
            public String	pubdate;
            public String	title;
            public String	type;
            public String	url;
        }

        public class TabNewDetailsDataTopic
        {

            public String	description;
            public String	id;
            public String	listimage;
            public String	sort;
            public String	title;
            public String	url;
        }

        
        public class TabNewDetailsDataTopNews
        {
            public String	comment;
            public String	commentlist;
            public String	commenturl;
            public String	id;
            public String	pubdate;
            public String	title;
            public String	topimage;
            public String	type;
            public String	url;

        }

    }

}
