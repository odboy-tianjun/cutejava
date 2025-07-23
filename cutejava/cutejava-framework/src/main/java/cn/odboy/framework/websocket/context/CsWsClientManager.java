package cn.odboy.framework.websocket.context;

import javax.websocket.CloseReason;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CsWsClientManager {
    /**
     * concurrent包的线程安全Map, 用来存放每个客户端对应的MyWebSocket对象。（分布式必出问题）
     */
    private static final Map<String, CsWsServer> client = new ConcurrentHashMap<>();

    public static void addClient(String sid, CsWsServer csWsServer) {
        // 如果存在就先删除一个, 防止重复推送消息
        removeClient(sid);
        client.put(sid, csWsServer);
    }

    public static void removeClient(String sid) {
        try {
            CsWsServer wsServer = client.get(sid);
            if (wsServer != null) {
                wsServer.getSession().close();
            }
        } catch (Exception e) {
            // ignore
        }
        client.remove(sid);
    }

    public static Collection<CsWsServer> getAllClient() {
        return client.values();
    }

    public static CsWsServer getClientBySid(String sid) {
        return client.get(sid);
    }
}
