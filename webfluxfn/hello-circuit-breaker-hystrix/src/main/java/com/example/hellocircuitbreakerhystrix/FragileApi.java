package com.example.hellocircuitbreakerhystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.support.SimpleHttpServerFactoryBean;
import org.springframework.util.SocketUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@SuppressWarnings("deprecation")
public class FragileApi extends SimpleHttpServerFactoryBean {

    private final Logger log = LoggerFactory.getLogger(FragileApi.class);

    private int port;

    private AtomicInteger counter = new AtomicInteger(0);

    public int getPort() {
        return port;
    }

    boolean isFailure() {
        final int n = this.counter.incrementAndGet() % 120;
        if (n == 0) {
            this.counter.set(1);
        }
        return n <= 40;
    }

    @Override
    public void afterPropertiesSet() throws IOException {
        this.port = SocketUtils.findAvailableTcpPort();
        super.setPort(port);
        super.setExecutor(Executors.newSingleThreadExecutor(r -> {
            final Thread thread = new Thread(r);
            thread.setName("fragile");
            return thread;
        }));
        super.setContexts(Collections.singletonMap("/", exchange -> {
            final boolean failure = this.isFailure();
            final byte[] body = (failure ? "Out of Service" : "Hello!").getBytes();
            if (failure) {
                try {
                    Thread.sleep(100);
                    log.warn("count={} Failure!", this.counter.get() % 120);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                log.info("count={}", this.counter.get() % 120);
            }
            exchange.getResponseHeaders().set(CONTENT_TYPE, TEXT_PLAIN_VALUE);
            exchange.sendResponseHeaders(failure ? 503 : 200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        }));
        super.afterPropertiesSet();
    }
}
