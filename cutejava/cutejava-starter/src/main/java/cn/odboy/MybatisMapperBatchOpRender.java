package cn.odboy;

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.util.KitClassUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mybatis批量操作生成器
 *
 * @author odboy
 */
public class MybatisMapperBatchOpRender {
    /**
     * 持久类名
     */
    private String entityClassName;
    /**
     * 持久类主键类型名
     */
    private String entityPkTypeName;

    /**
     * 渲染MapperXml
     *
     * @param entityClass 持久类
     * @param keyMapper   哪个是主键
     */
    public <T, K> void render(Class<?> entityClass, Function<? super T, ? extends K> keyMapper) {
        String mapperBatchInsert = "void batchInsert(@Param(\"list\") List<$EntityClassName> list);";
        System.err.println(mapperBatchInsert.replace("$EntityClassName", this.entityClassName));
        String mapperBatchUpdate = "void batchUpdateById(@Param(\"list\") List<$EntityClassName> list);";
        System.err.println(mapperBatchUpdate.replace("$EntityClassName", this.entityClassName));
        String mapperBatchDelete = "void batchDeleteByIds(@Param(\"list\") List<$EntityPkTypeName> list);";
        System.err.println(mapperBatchDelete.replace("$EntityPkTypeName", this.entityPkTypeName));

        List<Field> fields = new ArrayList<>();
        KitClassUtil.getAllFields(entityClass, fields);

        fields = fields.stream().filter(f -> !"serialVersionUID".equals(f.getName())).collect(Collectors.toList());

        System.err.println(fields);
    }

    public static void main(String[] args) {
        new MybatisMapperBatchOpRender().render(SystemDeptTb.class, SystemDeptTb::getId);
    }
}
