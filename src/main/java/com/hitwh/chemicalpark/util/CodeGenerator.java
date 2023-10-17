package com.hitwh.chemicalpark.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

/**
  * MP代码生成器
 **/
public class CodeGenerator {
    public static void main(String[] args) {
        generate();
    }

    public static void generate(){
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/chemicalpark?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("PJK") // 设置作者
                            .outputDir("C:\\Users\\ICES\\Desktop\\chemicalpark\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.hitwh.chemicalpark") // 设置父包名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "C:\\Users\\ICES\\Desktop\\chemicalpark\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude("storage") // 设置需要生成的表名
                            .addTablePrefix("sys_", "c_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
