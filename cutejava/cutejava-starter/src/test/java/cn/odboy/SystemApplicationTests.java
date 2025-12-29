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
package cn.odboy;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemUserVo;
import cn.odboy.system.service.SystemUserService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemApplicationTests {

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private SystemUserService systemUserService;

  public static void main(String[] args) {
  }

  @Test
  public void contextLoads() {
    SystemDeptTb systemDept = new SystemDeptTb();
    systemDept.setId(2L);
    SystemJobTb systemJob = new SystemJobTb();
    systemJob.setId(11L);
    Set<SystemJobTb> systemJobs = CollUtil.newHashSet(systemJob);
    SystemRoleTb systemRole = new SystemRoleTb();
    systemRole.setId(2L);
    Set<SystemRoleTb> systemRoles = CollUtil.newHashSet(systemRole);
    String pwd = passwordEncoder.encode("123456");
    for (int i = 100; i < 200; i++) {
      SystemUserVo systemUser = new SystemUserVo();
      systemUser.setDept(systemDept);
      systemUser.setJobs(systemJobs);
      systemUser.setRoles(systemRoles);
      systemUser.setUsername("odboy" + i);
      systemUser.setEmail("1943815" + i + "@qq.com");
      systemUser.setPhone("18797874" + i);
      systemUser.setPassword(pwd);
      systemUser.setEnabled(true);
      systemUserService.saveUser(systemUser);
    }
  }
}
