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