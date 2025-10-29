///*
// * Copyright 2021-2025 Odboy
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package cn.odboy.framework.doc;
//
//import cn.odboy.framework.properties.AppProperties;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * api页面 /doc.html
// */
//@Configuration
//public class SwaggerConfig {
//    @Autowired
//    private AppProperties properties;
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        // 添加开关控制
//        if (!properties.getSwagger().getEnabled()) {
//            return new OpenAPI();
//        }
//        return new OpenAPI().components(new Components().addSecuritySchemes("Authorization",
//                new SecurityScheme().name("Authorization").type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)))
//            .info(new Info().title("CuteJava 接口文档").version("1.4.1").description("一个简单且易上手的自动化运维平台"))
//            .addSecurityItem(new SecurityRequirement().addList("Authorization"));
//    }
//}
