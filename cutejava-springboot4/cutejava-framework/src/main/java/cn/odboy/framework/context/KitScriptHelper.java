/*
 * Copyright 2021-2026 Odboy
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

import com.ezylang.evalex.EvaluationException;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.data.EvaluationValue;
import com.ezylang.evalex.parser.ParseException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 脚本工具
 *
 * @author odboy
 * @date 2025-09-23
 */
@Slf4j
@Component
public class KitScriptHelper {

    public Object evalObj(String script, Map<String, Object> args) throws EvaluationException, ParseException {
        Expression expression = new Expression(script);
        expression.withValues(args);
        EvaluationValue result = expression.evaluate();
        return result.getValue();
    }
}
