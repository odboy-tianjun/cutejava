package cn.odboy.framework.websocket.context;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WsClientManager {
    /**
     * concurrent包的线程安全Map，用来存放每个客户端对应的MyWebSocket对象。（分布式必出问题）
     */
    private final Map<String, CsWsServer> client = new ConcurrentHashMap<>();

    public void addClient(String sid, CsWsServer csWsServer) {
        // 如果存在就先删除一个，防止重复推送消息
        client.remove(sid);
        client.put(sid, csWsServer);
    }

    public void removeClient(String sid) {
        client.remove(sid);
    }

    public Collection<CsWsServer> getAllClient() {
        return client.values();
    }

    public CsWsServer getClientBySid(String sid) {
        return client.get(sid);
    }
}
