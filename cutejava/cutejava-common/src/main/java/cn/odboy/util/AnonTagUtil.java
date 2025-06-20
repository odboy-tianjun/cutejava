package cn.odboy.util;

import cn.odboy.annotation.AnonymousAccess;
import cn.odboy.constant.RequestMethodEnum;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * 匿名标记工具
 */
public final class AnonTagUtil {

    /**
     * 获取匿名标记的URL
     *
     * @param applicationContext /
     * @return /
     */
    public static Map<String, Set<String>> getAnonymousUrl(final ApplicationContext applicationContext) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping) applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        Map<String, java.util.Set<String>> anonymousUrls = new HashMap<>(8);
        // 获取匿名标记
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.isEmpty() ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                if (infoEntry.getKey().getPatternsCondition() != null) {
                    switch (Objects.requireNonNull(request)) {
                        case GET:
                            get.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                            break;
                        case POST:
                            post.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                            break;
                        case PUT:
                            put.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                            break;
                        case PATCH:
                            patch.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                            break;
                        case DELETE:
                            delete.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                            break;
                        default:
                            all.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                            break;
                    }
                }
            }
        }
        anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(), all);
        return anonymousUrls;
    }

    /**
     * 获取所有匿名标记的URL
     *
     * @param applicationContext /
     * @return /
     */
    public static Set<String> getAllAnonymousUrl(final ApplicationContext applicationContext) {
        Set<String> allUrl = new HashSet<>();
        Map<String, Set<String>> anonymousUrls = getAnonymousUrl(applicationContext);
        for (String key : anonymousUrls.keySet()) {
            allUrl.addAll(anonymousUrls.get(key));
        }
        return allUrl;
    }
}
