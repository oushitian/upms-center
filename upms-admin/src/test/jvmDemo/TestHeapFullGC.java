package jvmDemo;

import java.util.Vector;

/**
 * 描述:
 * -Xmx11M -Xms4M -verbose:gc
 *
 * 第二次参数-Xmx11M -Xms11M -verbose:gc
 *
 * 可以发现JVM会试图将内存尽可能限制在-Xms中，当实际大小达到-Xms时，会触发FullGC，所以把-Xms和-Xmx设置一样可以减少GC次数
 *
 * -Xmn设置新生代大小，一般设置在整个堆空间的1/4-1/3之间
 * @author fd
 * @create 2018-07-04 15:17
 **/
public class TestHeapFullGC {

    public static void main(String[] args) {
        Vector vector = new Vector();
        for (int i = 1 ; i <= 10 ; i ++) {
            byte[] b = new byte[1024*1024];
            vector.add(b);
            if (vector.size() == 3){
                vector.clear();
            }
        }
    }
}
