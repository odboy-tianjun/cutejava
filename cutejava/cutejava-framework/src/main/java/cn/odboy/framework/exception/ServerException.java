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
package cn.odboy.framework.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public class ServerException extends RuntimeException {
    private Integer status = INTERNAL_SERVER_ERROR.value();

    public ServerException(String msg) {
        super(msg);
    }

    public ServerException(HttpStatus status, String msg) {
        super(msg);
        this.status = status.value();
    }

    public ServerException(Throwable cause) {
        super(cause.getMessage());
        this.status = INTERNAL_SERVER_ERROR.value();
    }
}
