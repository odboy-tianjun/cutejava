package cn.odboy.util.xlsx;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.idev.excel.FastExcel;
import cn.odboy.framework.exception.BadRequestException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.experimental.UtilityClass;

/**
 * xlsx文件导出工具
 *
 * @author odboy
 * @date 2025-12-29
 */
@UtilityClass
public class KitXlsxExportUtil {

  /**
   * @param response       HttpServletResponse
   * @param title          标题
   * @param dataObjectList 数据源，一般为从数据库直接查询的数据
   * @param targetClazz    目标对象Class
   * @param rowHandler     数据对象 -> 目标对象 映射逻辑
   * @param <O>            数据源对象类型
   * @param <T>            目标对象类型
   * @throws IOException
   */
  public static <O, T> void exportFile(HttpServletResponse response, String title, List<O> dataObjectList, Class<T> targetClazz, KitXlsxExportDataTransferRowHandler<O, T> rowHandler) throws IOException {
    if (response == null) {
      throw new BadRequestException("参数 HttpServletResponse(response) 必填");
    }
    if (CollUtil.isEmpty(dataObjectList)) {
      throw new BadRequestException("参数 数据源(dataObjectList) 必填");
    }
    if (StrUtil.isBlank(title)) {
      throw new BadRequestException("参数 标题(title) 必填");
    }
    String sheetName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
    List<T> resultList = new ArrayList<>();
    for (O o : dataObjectList) {
      List<T> rows = rowHandler.doHandler(o);
      resultList.addAll(rows);
    }
    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    response.setCharacterEncoding("utf-8");
    String fileName = URLEncoder.encode(sheetName + "_" + title, StandardCharsets.UTF_8);
    response.setHeader("Content-disposition",
        "attachment;filename*=utf-8''" + fileName + ".xlsx");
    FastExcel.write(response.getOutputStream(), targetClazz)
        .sheet(sheetName)
        .doWrite(resultList);
  }
}
