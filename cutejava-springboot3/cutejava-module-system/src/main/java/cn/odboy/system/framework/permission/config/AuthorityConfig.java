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

package cn.odboy.system.framework.permission.config;

import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service(value = "el")
public class AuthorityConfig {

    /**
     * 判断接口是否有权限
     *
     * @param permissions 权限
     * @return /
     */
    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> roleList = CsSecurityHelper.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return roleList.contains("admin") || Arrays.stream(permissions).anyMatch(roleList::contains);
    }
}
