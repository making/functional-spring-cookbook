package com.example.hellocircuitbreakerhystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.cloud.circuitbreaker.commons.Customizer;
import org.springframework.cloud.circuitbreaker.hystrix.ReactiveHystrixCircuitBreakerFactory;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;

public class CircuitBreakerFactoryCustomizer implements Customizer<ReactiveHystrixCircuitBreakerFactory> {

    @Override
    public void customize(ReactiveHystrixCircuitBreakerFactory factory) {
        factory.configureDefault(id -> HystrixObservableCommand.Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
            .andCommandKey(HystrixCommandKey.Factory.asKey(id))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                // https://github.com/Netflix/Hystrix/wiki/How-it-Works#circuit-breaker
                .withExecutionIsolationStrategy(SEMAPHORE)
                .withCircuitBreakerErrorThresholdPercentage(50)
                .withCircuitBreakerRequestVolumeThreshold(24)
                .withCircuitBreakerSleepWindowInMilliseconds(1000 /* for demo */)
                .withMetricsRollingStatisticalWindowInMilliseconds(3000 /* for demo */)
                .withExecutionTimeoutInMilliseconds(3000)
            ));
    }
}
