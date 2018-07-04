package jvmDemo;

import org.junit.Test;

/**
 * 描述:
 *
 * 使用jvm参数
 * -XX:+PrintGCDetails -XX:SurvivorRatio=8(8表示8:1:1  2表示2:1:1新生代的比例系数) -XX:MaxTenuringThreshold=15 -Xms40M(最小堆) -Xmx40M(最大堆) -Xmn20M(新生代大小)
 *
 *
 * 设置老年代和新生代的比例(-XX:NewRadio=2表示老年代是新生代的2倍)
 *
 * @author fd
 * @create 2018-07-04 14:30
 **/
public class TestHeapGC {

    public static void main(String[] args) {
        byte[] b1 = new byte[1024*1024/2];
        byte[] b2 = new byte[1024*1024*8];

        b2 = null;
        b2 = new byte[1024*1024*8];

        //System.gc();
    }

    //测试方法区
    //-XX:PermSize=2M(方法区的初始大小) -XX:MaxPermSize=4M(方法区的最大容量，一般设置64M或者128M，如果都不能满足条件就要考虑优化代码了) -XX:+PrintGCDetails
    @Test
    public void permGenGC(){
        for (int i = 0; i < Integer.MAX_VALUE ; i++) {
            String t = String.valueOf(i).intern();
        }
    }

}
