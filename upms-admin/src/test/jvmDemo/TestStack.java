package jvmDemo;

/**
 * 描述:测试当前虚拟机栈的深度
 * 利用-Xss来设置当前栈的大小
 *
 * 通过测试可知当局部变量越多时，栈帧会膨胀，固定的栈也就能放越少的栈帧
 * 报错java.lang.StackOverflowError
 *
 * @author fd
 * @create 2018-07-04 14:06
 **/
public class TestStack {

    private int i = 0;

    public void insum(long a,long b,long c){
        long d = 0,e = 0,g = 0;
        i++;
        insum(a,b,c);//递归调用，直到jvm的栈溢出
    }

    public void add() {
        try {
            insum(1,2,3);
        }catch (Throwable throwable){
            System.out.println("当前栈的深度是:"+i);
            throwable.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TestStack().add();
    }

}
