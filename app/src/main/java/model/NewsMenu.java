package model;

import java.util.ArrayList;

/**这是新闻数据模型
 * Created by Administrator on 2016/6/13.
 */
public class NewsMenu {

    public int retcode;
    public ArrayList<Integer> extend;
    public ArrayList<NewsData> data;

    public ArrayList<NewsData> getData() {
        return data;
    }

    public void setData(ArrayList<NewsData> data) {
        this.data = data;
    }



    //这是侧边栏
    public class NewsData{
        public int id;
        public String title;
        public int type;
        public ArrayList<Children> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ArrayList<Children> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<Children> children) {
            this.children = children;
        }
    }

    //这是侧边栏内容详情
    public class Children{
        public int id;
        public String title;
        public int type;
        public String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
