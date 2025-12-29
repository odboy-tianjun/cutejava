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

import cn.odboy.framework.monitor.service.KitHealthCheckPointService;
import cn.odboy.system.dal.mysql.SystemDictMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HealthCheckPointer implements KitHealthCheckPointService {

  @Autowired
  private SystemDictMapper systemDictMapper;

  /**
   * 系统准备就绪
   */
  @Override
  public ResponseEntity<?> doReadiness() {
    return ResponseEntity.ok(null);
  }

  /**
   * 系统是否正常
   */
  @Override
  public ResponseEntity<?> doLiveness() {
    return ResponseEntity.ok(systemDictMapper.selectById(1));
  }
}
