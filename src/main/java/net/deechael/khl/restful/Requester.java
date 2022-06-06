package net.deechael.khl.restful;

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.core.RabbitObject;
import net.deechael.khl.restful.ratelimit.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Requester extends RabbitObject {
    protected static final Logger Log = LoggerFactory.getLogger(Requester.class);

    private final RateLimiter rateLimiter = new RateLimiter();
    private final ScheduledExecutorService threadPool;

    public Requester(RabbitImpl rabbit) {
        this(rabbit, 4);
    }

    public Requester(RabbitImpl rabbit, int workerThread) {
        super(rabbit);
        this.threadPool = Executors.newScheduledThreadPool(workerThread, Requester::requesterThreadFactory);
    }

    private static Thread requesterThreadFactory(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName("RequestWorkerThread");
        thread.setDaemon(true);
        return thread;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public static class Worker implements Callable<HttpCall.Response> {

        private final RabbitImpl rabbit;
        private final HttpCall httpCall;

        public Worker(RabbitImpl rabbit, HttpCall httpCall) {
            this.rabbit = rabbit;
            this.httpCall = httpCall;
        }

        @Override
        public HttpCall.Response call() throws Exception {
            return rabbit.getHttpClient().execute(httpCall);
        }

    }
}
