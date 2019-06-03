package com.netflix.hystrix.examples.zhaojn;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;

/**
 * @author zhaojn
 * @desc
 * @date 2018/6/25
 */
public abstract class UserHystrixCommand extends HystrixCommand<String> {



    protected UserHystrixCommand(com.netflix.hystrix.HystrixCommandProperties.Setter commandProperties,
                                 com.netflix.hystrix.HystrixThreadPoolProperties.Setter threadPoolProperties
    ) {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("user-group"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("user-cmd"))
                        .andCommandPropertiesDefaults(commandProperties)
                        .andThreadPoolPropertiesDefaults(threadPoolProperties)

        );
    }

 /*   @Override
    protected String run() throws Exception {
//        log.info(Thread.currentThread().getName() + "==============run==================");


        return null;
    }

    @Override
    protected String getFallback() {
//
//        log.error(Thread.currentThread().getName() +"isCircuitBreakerOpen={}", isCircuitBreakerOpen());
//        log.error(Thread.currentThread().getName() +"isExecutionComplete={}", isExecutionComplete());
//        log.error(Thread.currentThread().getName() +"isExecutedInThread={}", isExecutedInThread());
//        log.error(Thread.currentThread().getName() +"isSuccessfulExecution={}", isSuccessfulExecution());
//        log.error(Thread.currentThread().getName() +"isFailedExecution={}", isFailedExecution());
//        log.error(Thread.currentThread().getName() +"isResponseFromFallback={}", isResponseFromFallback());
//        log.error(Thread.currentThread().getName() +"isResponseTimedOut={}", isResponseTimedOut());
//        log.error(Thread.currentThread().getName() +"isResponseShortCircuited={}", isResponseShortCircuited());
//        log.error(Thread.currentThread().getName() +"isResponseFromCache={}", isResponseFromCache());
//        log.error(Thread.currentThread().getName() +"isResponseRejected={}", isResponseRejected());
        log.error(Thread.currentThread().getName() +"getExecutionEvents={}", getExecutionEvents());

        String message = Optional.ofNullable(this.getFailedExecutionException()).map(Throwable::getMessage).orElse("");

        log.error(Thread.currentThread().getName() + "==============getFallback==================" + message);

        return null;
    }
    */


}
