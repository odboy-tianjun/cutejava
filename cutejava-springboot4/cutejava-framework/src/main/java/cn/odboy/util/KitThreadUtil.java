package cn.odboy.util;

import cn.hutool.core.thread.ThreadUtil;

public class KitThreadUtil {

  /**
   * 可中断的睡眠
   */
  public static void interruptSleep(int i) throws InterruptedException {
    Thread.sleep(i);
  }

  public static void safeSleep(int i) {
    ThreadUtil.safeSleep(i);
  }
}
