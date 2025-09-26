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

package cn.odboy.framework.redis;

import cn.hutool.core.util.StrUtil;
import cn.odboy.util.CsSystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.config.EqualJitterDelay;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedissonConfiguration {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.database}")
    private int redisDatabase;

    @Value("${spring.redis.password:}")
    private String redisPassword;

    @Value("${spring.redis.timeout:5000}")
    private int timeout;

    @Value("${spring.redis.lettuce.pool.max-active:64}")
    private int connectionPoolSize;

    @Value("${spring.redis.lettuce.pool.min-idle:16}")
    private int connectionMinimumIdleSize;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setThreads(CsSystemUtil.getCpuCount());
        config.setNettyThreads(0);
        config.setCodec(new StringCodec());
        config.setTransportMode(TransportMode.NIO);
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + redisHost + ":" + redisPort);
        singleServerConfig.setDatabase(redisDatabase);
        singleServerConfig.setTimeout(timeout);
        singleServerConfig.setConnectionPoolSize(connectionPoolSize);
        singleServerConfig.setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        if (StrUtil.isNotBlank(redisPassword)) {
            singleServerConfig.setPassword(redisPassword);
        }
        singleServerConfig.setIdleConnectionTimeout(10000);
        singleServerConfig.setConnectTimeout(10000);
        singleServerConfig.setTimeout(3000);
        singleServerConfig.setRetryAttempts(3);
        // singleServerConfig.setRetryInterval(1500);
        singleServerConfig.setRetryDelay(new EqualJitterDelay(Duration.ofSeconds(3), Duration.ofSeconds(5)));
        singleServerConfig.setSubscriptionsPerConnection(5);
        singleServerConfig.setSubscriptionConnectionMinimumIdleSize(1);
        singleServerConfig.setSubscriptionConnectionPoolSize(50);
        singleServerConfig.setConnectionMinimumIdleSize(32);
        singleServerConfig.setConnectionPoolSize(64);
        singleServerConfig.setDnsMonitoringInterval(5000);
        RedissonClient redissonClient = Redisson.create(config);
        log.info("Redisson客户端 初始化完毕");
        return redissonClient;
    }
}


