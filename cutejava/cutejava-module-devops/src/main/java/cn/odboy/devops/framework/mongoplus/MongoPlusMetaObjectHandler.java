/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.odboy.devops.framework.mongoplus;

import com.anwen.mongo.handlers.MetaObjectHandler;
import com.anwen.mongo.model.AutoFillMetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MongoPlusMetaObjectHandler implements MetaObjectHandler {
//    @Override
//    public void insertFill(Map<String, Object> insertFillMap, Document document) {
//        insertFillMap.keySet().forEach(key -> {
//            if (key.equals("createTime")){
//                document.put(key, LocalDateTime.now());
//            }
//        });
//    }
//
//    @Override
//    public void updateFill(Map<String, Object> insertFillMap, Document document) {
//        insertFillMap.keySet().forEach(key -> {
//            if (key.equals("createTime")){
//                document.put(key, LocalDateTime.now());
//            }
//        });
//    }

    @Override
    public void insertFill(AutoFillMetaObject insertAutoFillMetaObject) {
        Date nowTime = new Date();
        insertAutoFillMetaObject.fillValue("create_time", nowTime);
        insertAutoFillMetaObject.fillValue("update_time", nowTime);
    }

    @Override
    public void updateFill(AutoFillMetaObject updateAutoFillMetaObject) {
        updateAutoFillMetaObject.fillValue("update_time", new Date());
    }
}