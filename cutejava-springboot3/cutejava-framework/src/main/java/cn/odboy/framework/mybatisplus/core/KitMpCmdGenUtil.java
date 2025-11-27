/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.framework.mybatisplus.core;

import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitBaseUserTimeTb;
import cn.odboy.framework.exception.BadRequestException;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * 代码生成器入口
 *
 * @author odboy
 * @date 2025-01-11
 */
@Data
public class KitMpCmdGenUtil {
    // 项目根路径。生成结果 C:\Users\Administrator\Documents\GitHub\cutejava\cutejava
    private static final String projectRootPath = System.getProperty("user.dir") + "/";
    /**
     * 数据库类型 MySQL
     */
    private static final IDbQuery DB_QUERY = new MySqlQuery();
    /**
     * 类型转换
     */
    private static final String TYPE_TINY_INT = "tinyint";
    private static final String TYPE_DATETIME = "datetime";
    private static final String TYPE_DATE = "date";
    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;
    private String driverClassName;
    private String parentPackageName = "cn.odboy";
    private String parentModuleName;

    private static Consumer<StrategyConfig.Builder> getEntityConfigConsumer() {
        return strategyConfigBuilder -> strategyConfigBuilder.entityBuilder().enableLombok()
            // 如果不需要生成注解, 去掉.enableTableFieldAnnotation()
            .enableTableFieldAnnotation().enableFileOverride().naming(NamingStrategy.underline_to_camel).columnNaming(NamingStrategy.underline_to_camel)
            .idType(IdType.AUTO);
    }

    private static Consumer<StrategyConfig.Builder> getStrategyConfigConsumer(List<String> tableNames) {
        return strategyConfigBuilder -> strategyConfigBuilder.enableCapitalMode().enableSkipView().disableSqlFilter().addInclude(tableNames)
            // 实体类生成策略
            .entityBuilder().superClass(KitBaseUserTimeTb.class).addIgnoreColumns("create_time", "update_time", "create_by", "update_by")
            // 开启生成实体时生成字段注解。
            // 会在实体类的属性前，添加[@TableField("nickname")]
            .enableTableFieldAnnotation().disableSerialVersionUID()
            // 阶段2：Mapper策略配置
            .mapperBuilder()
            // 开启 @Mapper 注解。
            // 会在mapper接口上添加注解[@Mapper]
            //                .enableMapperAnnotation()
            // 启用 BaseResultMap 生成。
            // 会在mapper.xml文件生成[通用查询映射结果]配置。
            //                .enableBaseResultMap()
            // 启用 BaseColumnList。
            // 会在mapper.xml文件生成[通用查询结果列 ]配置
            //                .enableBaseColumnList()
            // 阶段4：Controller策略配置
            .controllerBuilder()
            // 会在控制类中加[@RestController]注解。
            .enableRestStyle()
            // 开启驼峰转连字符
            .enableHyphenStyle().build();
    }

    private static Consumer<GlobalConfig.Builder> getGlobalConfigConsumer() {
        String outputDir = projectRootPath + "CodeGen";
        return globalConfigBuilder -> globalConfigBuilder.enableSwagger().outputDir(outputDir).author("codegen").commentDate("yyyy-MM-dd")
            .dateType(DateType.TIME_PACK);
    }

    private static Consumer<StrategyConfig.Builder> getStrategyConfigConsumer() {
        return strategyConfigBuilder -> strategyConfigBuilder.mapperBuilder().mapperAnnotation(Mapper.class).formatMapperFileName("%sMapper")
            .formatXmlFileName("%sMapper").entityBuilder().formatFileName("%sTb");
    }

    private static Consumer<DataSourceConfig.Builder> getDataSourceConfigConsumer() {
        return builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
            if (typeCode == Types.SMALLINT) {
                return DbColumnType.INTEGER;
            }
            if (typeCode == Types.DATE) {
                return DbColumnType.DATE;
            }
            if (typeCode == Types.TIME) {
                return DbColumnType.DATE;
            }
            if (typeCode == Types.TIMESTAMP) {
                return DbColumnType.DATE;
            }
            return typeRegistry.getColumnType(metaInfo);
        });
    }

    /**
     * 代码生成函数
     *
     * @param moduleName  模块名称
     * @param tablePrefix 表前缀(可空)
     * @param tableNames  表名称, 多个用逗号分隔
     */
    public void gen(String moduleName, String tablePrefix, List<String> tableNames) {
        this.parentModuleName = moduleName;
        // 1.数据库配置
        DataSourceConfig.Builder dataSourceConfigBuilder =
            new DataSourceConfig.Builder(this.databaseUrl, this.databaseUsername, this.databasePassword).driverClassName(this.driverClassName);
        dataSourceConfigBuilder.dbQuery(DB_QUERY);
        dataSourceConfigBuilder.keyWordsHandler(new MySqlKeyWordsHandler());
        // 1.1.快速生成器
        FastAutoGenerator fastAutoGenerator = FastAutoGenerator.create(dataSourceConfigBuilder);
        // 2.全局配置
        // 自定义类型转换
        fastAutoGenerator.dataSourceConfig(getDataSourceConfigConsumer());
        // 覆盖已生成文件
        // 添加swagger注解
        // 设置注释的作者
        // 设置注释的日期格式
        // 使用java8新的时间类型
        fastAutoGenerator.globalConfig(getGlobalConfigConsumer());
        // 3.包配置
        // 设置父包名
        // 设置父包模块名
        // 设置MVC下各个模块的包名
        // 设置XML资源文件的目录
        fastAutoGenerator.packageConfig(getPackageConfigConsumer());
        // 4.模板配置
        // 使用Freemarker引擎模板, 默认的是Velocity引擎模板
        AbstractTemplateEngine templateEngine = new FreemarkerTemplateEngine();
        fastAutoGenerator.templateEngine(templateEngine);
        // 5.策略配置
        // 设置需要生成的表名
        // 设置过滤表前缀
        fastAutoGenerator.strategyConfig(getStrategyConfigConsumer(tableNames));
        if (tablePrefix != null) {
            // 表前缀, 可多个
            fastAutoGenerator.strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.addTablePrefix(tablePrefix));
        }
        // 5.1.Entity策略配置
        // 生成实体时生成字段的注解, 包括@TableId注解等
        // 数据库表和字段映射到实体的命名策略, 为下划线转驼峰
        // 全局主键类型为None
        // 实体名称格式化为XXXEntity
        fastAutoGenerator.strategyConfig(getEntityConfigConsumer());
        // 5.2.Controller策略配置
        // 开启生成@RestController控制器
        fastAutoGenerator.strategyConfig(strategyConfigBuilder -> strategyConfigBuilder.controllerBuilder().enableRestStyle());
        // 如果不需要生成Controller, fastAutoGenerator.templateConfig(templateConfig -> templateConfig.controller(""))。5.3.Service策略配置：格式化service接口和实现类的文件名称, 去掉默认的ServiceName前面的I
        fastAutoGenerator.strategyConfig(
            strategyConfigBuilder -> strategyConfigBuilder.serviceBuilder().formatServiceFileName("%sService").formatServiceImplFileName("%sServiceImpl"));
        // 5.4.Mapper策略配置
        // 格式化 mapper文件名,格式化xml实现类文件名称
        fastAutoGenerator.strategyConfig(getStrategyConfigConsumer());
        // 6.生成代码
        fastAutoGenerator.execute();
    }

    private Consumer<PackageConfig.Builder> getPackageConfigConsumer() {
        String mapperPath = projectRootPath + "CodeGen/resources/mapper";
        if (StrUtil.isBlank(this.parentModuleName)) {
            throw new BadRequestException("模块名称必填");
        }
        return packageConfigBuilder -> packageConfigBuilder.parent(this.parentPackageName).moduleName(this.parentModuleName).entity("dal.dataobject")
            .mapper("dal.mysql").service("service").serviceImpl("service.impl").controller("controller")
            .pathInfo(Collections.singletonMap(OutputFile.xml, mapperPath));
    }
}