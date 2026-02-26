package cn.odboy.util.xlsx;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.metadata.WriteSheet;
import java.io.OutputStream;
import java.util.List;

/**
 * 分页写入Excel工具 - 专治各种不服(OOM)
 */
public class KitExcelPageWriteHelper {

  /**
   * 执行分页写入
   *
   * @param outputStream 输出流 (响应OutputStream)
   * @param head         数据模型Class (如 User.class)
   * @param sheetName    表格名称
   * @param pageSize     【重要】每批次处理条数 (建议 1000~5000)
   * @param totalCount   总数据量 (用于计算总页数)
   * @param supplier     分页数据提供器 (你的业务查询逻辑)
   */
  public static <T> void writeByPage(OutputStream outputStream, Class<T> head, String sheetName, long pageSize, long totalCount, KitExcelExporter.PageQuerySupplier<T> supplier) {
    try (ExcelWriter excelWriter = EasyExcel.write(outputStream, head).build()) {
      // 默认Sheet
      WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
      int totalPage = totalCount > 0 ? (int) Math.ceil((double) totalCount / pageSize) : 1;
      for (int pageNum = 1; pageNum <= totalPage; pageNum++) {
        List<T> pageData = supplier.getPage(pageNum, pageSize);
        excelWriter.write(pageData, writeSheet);
        pageData.clear();
      }
    }
  }
}