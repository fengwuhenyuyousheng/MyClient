package application;

import android.app.Application;
import org.xutils.x;

/**这是自己的Application类
 * Created by Administrator on 2016/6/13.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        x.Ext.init(this);//xUtils初始化
    }
}
