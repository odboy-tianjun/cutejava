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

package cn.odboy.framework.context;

import lombok.extern.slf4j.Slf4j;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 脚本工具
 *
 * @author odboy
 * @date 2025-09-23
 */
@Slf4j
@Component
public class CsScriptHelper {
    public Object evalObj(String script, Map<String, Object> args) {
        try (Context rhino = Context.enter()) {
            Scriptable scope = rhino.initStandardObjects();
            bindArgumentsToScope(scope, args);
            Object result = rhino.evaluateString(scope, script, "JavaScript", 1, null);
            log.info("执行结果: {}", Context.toString(result));
            return result;
        }
    }

    private void bindArgumentsToScope(Scriptable scope, Map<?, ?> args) {
        if (args == null) {
            return;
        }
        for (Map.Entry<?, ?> entry : args.entrySet()) {
            Object jsValue = Context.javaToJS(entry.getValue(), scope);
            scope.put(entry.getKey().toString(), scope, jsValue);
        }
    }

    private void bindArgumentsToScope(Scriptable scope, Object args) {
        if (args == null) {
            return;
        }
        // 单个对象参数
        Object jsValue = Context.javaToJS(args, scope);
        scope.put("args", scope, jsValue);
    }
}
