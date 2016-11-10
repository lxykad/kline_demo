package test.lxy.com.demo.bean;

/**
 * Created by lxy on 16/11/9.
 * 分时数据
 */

public class FsBean2 {

    public String time;
    public float open;
    public float close;
    public int volume;
    public int balance;

    @Override
    public String toString() {
        return "FsBean2{" +
                "time='" + time + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", volume=" + volume +
                ", balance=" + balance +
                '}';
    }
}

