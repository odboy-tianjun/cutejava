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
package cn.odboy.system.dal.model.response;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class SystemUserJwtVo implements UserDetails {

  @ApiModelProperty(value = "用户")
  private final SystemUserVo user;
  @ApiModelProperty(value = "数据权限")
  private final List<Long> dataScopes;
  @ApiModelProperty(value = "角色")
  private final List<SystemRoleCodeVo> authorities;

  public Set<String> getRoles() {
    return authorities.stream().map(SystemRoleCodeVo::getAuthority).collect(Collectors.toSet());
  }

  @Override
  @JSONField(serialize = false)
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  @JSONField(serialize = false)
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  @JSONField(serialize = false)
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JSONField(serialize = false)
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JSONField(serialize = false)
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JSONField(serialize = false)
  public boolean isEnabled() {
    return user.getEnabled();
  }
}
