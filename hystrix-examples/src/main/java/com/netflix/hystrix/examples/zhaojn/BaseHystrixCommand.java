package com.netflix.hystrix.examples.zhaojn;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * @author zhaojn
 * @desc
 * @date 2018/6/25
 */
public abstract class BaseHystrixCommand extends HystrixCommand<String> {



    protected BaseHystrixCommand(com.netflix.hystrix.HystrixCommandProperties.Setter commandProperties,
                                 com.netflix.hystrix.HystrixThreadPoolProperties.Setter threadPoolProperties
    ) {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("user-group"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("user-cmd"))
                        .andCommandPropertiesDefaults(commandProperties)
                        .andThreadPoolPropertiesDefaults(threadPoolProperties)

        );
    }

}
