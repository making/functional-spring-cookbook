package com.example.hellocircuitbreakerhystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.cloud.circuitbreaker.commons.Customizer;
import org.springframework.cloud.circuitbreaker.hystrix.HystrixCircuitBreakerFactory;

import static com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;

public class CircuitBreakerFactoryCustomizer implements Customizer<HystrixCircuitBreakerFactory> {

    @Override
    public void customize(HystrixCircuitBreakerFactory factory) {
        factory.configureDefault(id -> HystrixCommand.Setter
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
