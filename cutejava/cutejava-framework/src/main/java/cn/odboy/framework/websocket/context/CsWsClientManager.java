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

package cn.odboy.framework.websocket.context;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket客户端管理
 *
 * @author odboy
 * @date 2025-07-24
 */
@Slf4j
public class CsWsClientManager {
    /**
     * concurrent包的线程安全Map, 用来存放每个客户端对应的MyWebSocket对象。（分布式必出问题）
     */
    private static final Map<String, CsWsServer> clientMap = new ConcurrentHashMap<>();

    public static void addClient(String sid, CsWsServer csWsServer) {
        // 如果存在就先删除一个, 防止重复推送消息
        removeClient(sid);
        clientMap.put(sid, csWsServer);
    }

    public static void removeClient(String sid) {
        CsWsServer wsServer = clientMap.get(sid);
        if (wsServer != null) {
            try {
                //                log.info("关闭session, sid={}", sid);
                wsServer.getSession().close();
            } catch (Exception e) {
                // ignore
            }
            //            log.info("停止任务, sid={}", sid);
            wsServer.stopTask(sid);
            //            log.info("移除客户端, sid={}", sid);
            clientMap.remove(sid);
        }
    }

    public static Collection<CsWsServer> getAllClient() {
        return clientMap.values();
    }

    public static CsWsServer getClientBySid(String sid) {
        return clientMap.get(sid);
    }
}
