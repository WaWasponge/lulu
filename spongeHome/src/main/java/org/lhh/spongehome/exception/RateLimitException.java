package org.lhh.spongehome.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimitException  extends RuntimeException{
    private final int retryAfter;

    //    可添加一个时间 告知客户端重试等待时间
    public RateLimitException (String  message,int retryAfter){
        super(message);
        this.retryAfter = retryAfter;// 告知客户端重试等待时间
    }
}
