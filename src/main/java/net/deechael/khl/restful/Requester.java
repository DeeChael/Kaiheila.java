package net.deechael.khl.restful;

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.restful.ratelimit.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Requester extends KaiheilaObject {
    protected static final Logger Log = LoggerFactory.getLogger(Requester.class);

    private final RateLimiter rateLimiter = new RateLimiter();
    private final ScheduledExecutorService threadPool;

    public Requester(Gateway gateway) {
        this(gateway, 4);
    }

    public Requester(Gateway gateway, int workerThread) {
        super(gateway);
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

        private final KaiheilaBot rabbit;
        private final HttpCall httpCall;

        public Worker(KaiheilaBot rabbit, HttpCall httpCall) {
            this.rabbit = rabbit;
            this.httpCall = httpCall;
        }

        @Override
        public HttpCall.Response call() throws Exception {
            return rabbit.getHttpClient().execute(httpCall);
        }

    }
}
