package cn.odboy;

import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.util.KitClassUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mybatis批量操作生成器
 *
 * @author odboy
 */
public class MybatisMapperBatchOpRender {

  /**
   * 渲染MapperXml
   *
   * @param entityClass    持久类
   * @param primaryKeyName 主键名称
   */
  public <T, K> void render(Class<?> entityClass, String primaryKeyName) {
    if (entityClass == null) {
      throw new BadRequestException("参数 entityClass 必填");
    }
    TableName tableNameAnnotation = entityClass.getAnnotation(TableName.class);
    if (tableNameAnnotation == null) {
      throw new BadRequestException("参数 entityClass 不是一个有效的Mybatis表对象");
    }

    List<Field> fields = new ArrayList<>();
    KitClassUtil.getAllFields(entityClass, fields);
    fields = fields.stream().filter(f -> !"serialVersionUID".equals(f.getName())).collect(Collectors.toList());

    Field primaryKeyField = fields.stream().filter(f -> f.getName().equals(primaryKeyName)).findFirst().orElse(null);
    if (primaryKeyField == null) {
      throw new BadRequestException("参数 primaryKeyName 设置有误");
    }

    fields = fields.stream().filter(f -> !f.getName().equals(primaryKeyName)).collect(Collectors.toList());

    // 持久类主键类型名
    String entityPkTypeName = primaryKeyField.getType().getSimpleName();
    // 持久类名
    String entityClassName = entityClass.getSimpleName();
    // 数据表名
    String tableName = tableNameAnnotation.value();

    System.err.println("============== 渲染Mapper ==================");
    String mapperBatchInsert = "void batchInsert(@Param(\"list\") List<$EntityClassName> list);";
    System.err.println(mapperBatchInsert.replace("$EntityClassName", entityClassName));
    String mapperBatchUpdate = "void batchUpdateById(@Param(\"list\") List<$EntityClassName> list);";
    System.err.println(mapperBatchUpdate.replace("$EntityClassName", entityClassName));
    String mapperBatchDelete = "void batchDeleteByIds(@Param(\"list\") List<$EntityPkTypeName> list);";
    System.err.println(mapperBatchDelete.replace("$EntityPkTypeName", entityPkTypeName));
    System.err.println("\n============== 渲染MapperXml ==================");
    List<String> fieldNameList = new ArrayList<>();
    List<String> fieldNameUnderlineList = new ArrayList<>();
    List<String> fieldNameItemList = new ArrayList<>();

    for (Field field : fields) {
      fieldNameList.add(field.getName());
    }
    for (String fieldName : fieldNameList) {
      fieldNameUnderlineList.add(StrUtil.toUnderlineCase(fieldName));
      fieldNameItemList.add("#{item." + fieldName + "}");
    }

    String batchInsertXml = "<insert id=\"batchInsert\">\n"
        + "INSERT INTO " + tableName + " ("
        + String.join(",", fieldNameUnderlineList)
        + ") VALUES\n"
        + "<foreach collection=\"list\" item=\"item\" separator=\",\">\n"
        + "(\n"
        + String.join(",\n", fieldNameItemList)
        + ")\n"
        + "</foreach>\n"
        + "</insert>\n";
    System.err.println(batchInsertXml);

    StringBuilder ifStringBuilder = new StringBuilder();
    for (int i = 0; i < fieldNameList.size(); i++) {
      ifStringBuilder.append("<if test=\"item.")
          .append(fieldNameList.get(i))
          .append(" != null\">")
          .append(fieldNameUnderlineList.get(i))
          .append(" = #{item.")
          .append(fieldNameList.get(i))
          .append("},</if>\n");
    }
    String batchUpdateXml = "<update id=\"batchUpdateById\">\n"
        + "<foreach collection=\"list\" item=\"item\" separator=\";\">\n"
        + "UPDATE " + tableName + "\n"
        + "<set>\n"
        + ifStringBuilder
        + "</set>\n"
        + "WHERE " + StrUtil.toUnderlineCase(primaryKeyName) + " = #{item." + primaryKeyName + "}\n"
        + "</foreach>\n"
        + "</update>\n";
    System.err.println(batchUpdateXml);

    String batchDeleteXml = "<delete id=\"batchDeleteByIds\">\n"
        + "delete from " + tableName + " where " + StrUtil.toUnderlineCase(primaryKeyName) + " in\n"
        + "<foreach collection=\"list\" item=\"id\" open=\"(\" close=\")\" separator=\",\">\n"
        + "#{id}\n"
        + "</foreach>\n"
        + "</delete>";
    System.err.println(batchDeleteXml);
  }

  public static void main(String[] args) {
    new MybatisMapperBatchOpRender().render(SystemDeptTb.class, "id");
  }
}
