package cn.geminius.rabbitmq.utils;

/**
 * @author geminius
 * @date 2021/7/5 20:59
 */
public class SleepUtils {

    public static void sleep (int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
