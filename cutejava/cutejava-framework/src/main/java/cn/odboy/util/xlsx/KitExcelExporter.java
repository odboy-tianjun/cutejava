package cn.odboy.util.xlsx;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.idev.excel.EasyExcel;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * EasyExcel 导出增强工具类 (支持普通/分页模式)
 *
 * @author https://mp.weixin.qq.com/s/gnrKCUzHg24sBSDp4IyAvg
 */
public class KitExcelExporter {

  private static int calculateOptimalPageSize(Class<?> clazz) {
    // 1. 估算单条数据大小 (字节) - 根据业务模型调整逻辑
    // 保守估计500字节/行
    long approxBytesPerRow = 500;
    // 2. 获取当前JVM可用内存 (转成字节)
    long freeMemoryBytes = Runtime.getRuntime().freeMemory();
    // 3. 【安全策略】仅使用一部分可用内存 (例如 40%)
    long safeMemoryToUse = (long) (freeMemoryBytes * 0.4);
    // 4. 计算建议分页条数
    int suggestedPageSize = (int) (safeMemoryToUse / approxBytesPerRow);
    // 5. 设置合理范围 (防止太大或太小)
    // 限制在1000~10000条/页
    return Math.max(1000, Math.min(suggestedPageSize, 10000));
  }

  /**
   * 分页写入 (大数据量首选)
   *
   * @param response     /
   * @param fileName     下载文件名
   * @param dataModel    数据类 (User.class)
   * @param totalCount   总条数
   * @param pageSupplier 分页查询逻辑
   */
  public static <T> void exportByPage(HttpServletResponse response, String fileName, Class<T> dataModel, long totalCount, PageQuerySupplier<T> pageSupplier) {
    int pageSize = calculateOptimalPageSize(dataModel);
    String sheetName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
    setupResponse(response, fileName, sheetName);
    try (OutputStream out = response.getOutputStream()) {
      KitExcelPageWriteHelper.writeByPage(out, dataModel, sheetName, pageSize, totalCount, pageSupplier);
    } catch (Exception e) {
      throw new RuntimeException("导出失败: " + e.getMessage(), e);
    }
  }

  /**
   * 普通导出 (小数据量)
   *
   * @param response  /
   * @param fileName  下载文件名
   * @param dataModel 数据类 (User.class)
   * @param dataList  全量数据List
   */
  public static <T> void exportSimple(HttpServletResponse response, String fileName, Class<T> dataModel, List<T> dataList) {
    String sheetName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
    setupResponse(response, fileName, sheetName);
    try (OutputStream out = response.getOutputStream()) {
      EasyExcel.write(out, dataModel)
          .sheet(sheetName)
          // 全量写入
          .doWrite(dataList);
    } catch (Exception e) {
      throw new RuntimeException("导出失败: " + e.getMessage(), e);
    }
  }

  /**
   * 设置响应头
   */
  private static void setupResponse(HttpServletResponse response, String title, String sheetName) {
    try {
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
      response.setCharacterEncoding("utf-8");
      String fileName = URLEncoder.encode(sheetName + "_" + title, StandardCharsets.UTF_8);
      response.setHeader("Content-disposition",
          "attachment;filename*=utf-8''" + fileName + ".xlsx");
    } catch (Exception e) {
      throw new RuntimeException("设置响应头失败", e);
    }
  }

  /**
   * 分页查询供应商
   *
   * @param <T>
   */
  @FunctionalInterface
  public interface PageQuerySupplier<T> {

    List<T> getPage(long pageNum, long pageSize); // 函数式接口
  }
}
