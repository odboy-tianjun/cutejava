package cn.odboy.constool;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.framework.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 数据对比同步工具
 * <p>
 * 适用场景：对比两个相同类型的对象集合，以对照组（compareRecords）为基准：
 * <p>
 * - 本地不存在的数据 → 新增
 * <p>
 * - 本地存在的数据 → 更新
 * <p>
 * - 对照组不存在的数据 → 删除
 *
 * @param <T> 数据类型
 * @author odboy
 */
public class KitDiffSyncConstTool<T> {

    /**
     * 数据同步回调接口
     *
     * @param <T> 数据类型
     */
    public interface SyncCallback<T> {
        /**
         * 全量存储对照组数据
         *
         * @param records 需要保存的数据列表
         */
        void doSaveBatch(List<T> records);

        /**
         * 批量更新数据
         *
         * @param localList   本地已存在的数据列表
         * @param compareList 对照组对应的数据列表
         */
        void doUpdateBatch(List<T> localList, List<T> compareList);

        /**
         * 批量删除数据
         *
         * @param records 需要删除的数据列表
         */
        void doDeleteBatch(List<T> records);
    }

    /**
     * 本地数据源
     */
    private final List<T> localRecords;

    /**
     * 对照数据源（以对照数据源为准）
     */
    private final List<T> compareRecords;

    /**
     * 构造函数
     *
     * @param localRecords   本地数据源
     * @param compareRecords 对照数据源（基准数据）
     */
    public KitDiffSyncConstTool(List<T> localRecords, List<T> compareRecords) {
        this.localRecords = localRecords;
        this.compareRecords = compareRecords;
    }

    /**
     * 执行数据对比同步
     *
     * @param keyMapper 唯一键映射函数，用于比较数据唯一性（如：Entity::getId）
     * @param callback  同步回调接口，处理具体的增删改操作
     * @param <K>       唯一键类型
     */
    public <K> void sync(Function<? super T, ? extends K> keyMapper, SyncCallback<T> callback) {
        if (keyMapper == null) {
            throw new BadRequestException("参数 keyMapper 必填");
        }
        if (callback == null) {
            throw new BadRequestException("参数 callback 必填");
        }

        // 如果本地数据为空，直接全量保存对照数据
        if (this.localRecords.isEmpty()) {
            callback.doSaveBatch(this.compareRecords);
            return;
        }

        // 构建本地数据的键值映射
        Map<K, T> localMap = localRecords.stream().collect(Collectors.toMap(keyMapper, i -> i, (oldest, newest) -> oldest));

        List<T> insertList = new ArrayList<>();
        List<T> updateCompareList = new ArrayList<>();
        List<T> updateLocalList = new ArrayList<>();
        List<T> deleteList = new ArrayList<>();

        // 遍历对照数据，识别新增和更新
        for (T compareRecord : compareRecords) {
            K key = keyMapper.apply(compareRecord);
            T local = localMap.get(key);

            if (local == null) {
                // 本地不存在，需要新增
                insertList.add(compareRecord);
            } else {
                // 本地已存在，需要更新
                updateLocalList.add(local);
                updateCompareList.add(compareRecord);
                // 标记为已处理
                localMap.remove(key);
            }
        }

        // 剩余的就是本地存在但对照组中不存在的，需要删除
        for (Map.Entry<K, T> entry : localMap.entrySet()) {
            deleteList.add(entry.getValue());
        }

        // 执行批量操作
        if (CollUtil.isNotEmpty(insertList)) {
            callback.doSaveBatch(insertList);
        }

        if (CollUtil.isNotEmpty(updateLocalList)) {
            callback.doUpdateBatch(updateLocalList, updateCompareList);
        }

        if (CollUtil.isNotEmpty(deleteList)) {
            callback.doDeleteBatch(deleteList);
        }
    }

//    /**
//     * 使用示例
//     */
//    public static void main(String[] args) {
//        List<KitBaseUserTimeTb> localList = new ArrayList<>();
//        List<KitBaseUserTimeTb> onlineList = new ArrayList<>();
//
//        KitDiffSyncConstTool<KitBaseUserTimeTb> diffSyncTool = new KitDiffSyncConstTool<>(localList, onlineList);
//        diffSyncTool.sync(KitBaseUserTimeTb::getCreateBy, new SyncCallback<>() {
//            @Override
//            public void doSaveBatch(List<KitBaseUserTimeTb> records) {
//                // 全量保存
//            }
//
//            @Override
//            public void doUpdateBatch(List<KitBaseUserTimeTb> localList, List<KitBaseUserTimeTb> compareList) {
//                // 将 compareList 中的数据，填充到对应的 localList 数据中，更新 localList 数据
//            }
//
//            @Override
//            public void doDeleteBatch(List<KitBaseUserTimeTb> records) {
//                // 条件删除
//            }
//        });
//    }
}
