package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**这是MD5加密算法工具类
 * Created by Administrator on 2016/6/13.
 */
public class MD5Tool {
    public  static String getUrlMD5(String url){
        StringBuilder sb=new StringBuilder();
        try {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            byte[] digest=messageDigest.digest(url.getBytes());
            for(int i=0;i<digest.length;i++){
                int result=digest[i] & 0xff;
                String hexString=Integer.toHexString(result);
                if(hexString.length()<2){
                    sb.append("0");
                }
                sb.append(hexString);
            }
            return  sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
