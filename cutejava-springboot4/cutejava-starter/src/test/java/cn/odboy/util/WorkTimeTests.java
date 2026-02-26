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
package cn.odboy.util;

/**
 * 钉钉 Teambition 实际工时数计算
 */
public class WorkTimeTests {

  public static void main(String[] args) {
    // 总工作时长（时）
    double totalHour = 11;
    // 实际工作时长（天），一天8小时工作制
    double actualHour = totalHour / 8.0;
    System.err.println("actualHour=" + actualHour + " 天");
  }
}
