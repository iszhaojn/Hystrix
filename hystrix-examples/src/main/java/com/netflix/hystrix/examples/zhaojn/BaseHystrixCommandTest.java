package com.netflix.hystrix.examples.zhaojn;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import junit.framework.TestCase;
import org.apache.commons.lang.math.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaojn
 * @desc
 * @date 2018/6/25
 */
public class BaseHystrixCommandTest extends TestCase {

	ExecutorService executorService = Executors.newFixedThreadPool(100);

	public void testHystrix01() {
		com.netflix.hystrix.HystrixCommandProperties.Setter commandPropertiesDefaults =
				HystrixCommandProperties.Setter()
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(90)
						.withRequestLogEnabled(true)
						////默认是true，本例中为了展现该参数,设置断路器是否起作用
						.withCircuitBreakerEnabled(false)
						//默认是false，本例中为了展现该参数,强制断路器进入打开状态，将会拒绝所有的请求,优先级比circuitBreaker.forceClosed高
						.withCircuitBreakerForceOpen(false)
						//默认是false，本例中为了展现该参数,强制断路器进入关闭状态，将会允许所有的请求，无视错误率
						.withCircuitBreakerForceClosed(false)
						//超时熔断时间
						.withExecutionTimeoutInMilliseconds(1000)
						//(1)错误百分比超过5%
						.withCircuitBreakerErrorThresholdPercentage(10)
						//(2)10s以内调用次数10次，同时满足(1)(2)熔断器打开
						.withCircuitBreakerRequestVolumeThreshold(100)
						//隔5s之后，熔断器会尝试半开(关闭)，重新放进来请求
						.withCircuitBreakerSleepWindowInMilliseconds(5000);

		com.netflix.hystrix.HystrixThreadPoolProperties.Setter threadPoolPropertiesDefaults =
				HystrixThreadPoolProperties.Setter()
						//用来配置线程池大小。Hystrix 对线程池的配置有一些限制，这里只能配置线程数的 Core Size，不能配置 Max Size。不配置的话使用的默认值是 10
						.withCoreSize(20)
						//用来配置核心线程数空闲时 keep alive 的时长，默认为 1 mins。这项配置一般不需要修改
						.withKeepAliveTimeMinutes(1)
						//用来配置线程池任务队列的大小，默认值为 -1。当使用 -1
						.withMaxQueueSize(-1)
//                        .withAllowMaximumSizeToDivergeFromCoreSize()
//              .withMetricsRollingStatisticalWindowBuckets(2)
//              .withMetricsRollingStatisticalWindowInMilliseconds(10)
//              .withQueueSizeRejectionThreshold(2)
				;

		final AtomicInteger pass = new AtomicInteger(0);
		final AtomicInteger block = new AtomicInteger(0);

		CountDownLatch countDownLatch = new CountDownLatch(100);
		List<Callable<Object>> callables = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Callable<Object> runnable = () -> {
				BaseHystrixCommand baseHystrixCommand =
						new BaseHystrixCommand(commandPropertiesDefaults, threadPoolPropertiesDefaults) {
							@Override
							protected String run() {

								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								System.out.println(Thread.currentThread().getName() + "------------>pass by" + getExecutionEvents());
								countDownLatch.countDown();
								pass.incrementAndGet();
								return null;
							}

							@Override
							protected String getFallback() {

								System.out.println(Thread.currentThread().getName() + "------------>block by" + getExecutionEvents());
								try {
									Thread.sleep(RandomUtils.nextInt(1500));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								countDownLatch.countDown();
								block.incrementAndGet();
								return null;
							}
						};
				baseHystrixCommand.execute();
				return null;
			};
			callables.add(runnable);
		}

		try {
			executorService.invokeAll(callables);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			countDownLatch.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("pass = " + pass.get());
		System.out.println("block = " + block.get());

	}

}
