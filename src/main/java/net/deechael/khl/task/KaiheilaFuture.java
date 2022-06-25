package net.deechael.khl.task;

import net.deechael.khl.bot.KaiheilaBot;

import java.util.concurrent.*;

class KaiheilaFuture<T> extends KaiheilaTask implements Future<T> {

    private final Callable<T> callable;
    private T value;
    private Exception exception = null;

    KaiheilaFuture(final Callable<T> callable, final KaiheilaBot kaiheilaBot, final int id) {
        super(kaiheilaBot, null, id, KaiheilaTask.NO_REPEATING);
        this.callable = callable;
    }

    @Override
    public synchronized boolean cancel(final boolean mayInterruptIfRunning) {
        if (getPeriod() != KaiheilaTask.NO_REPEATING) {
            return false;
        }
        setPeriod(KaiheilaTask.CANCEL);
        return true;
    }

    @Override
    public boolean isDone() {
        final long period = this.getPeriod();
        return period != KaiheilaTask.NO_REPEATING && period != KaiheilaTask.PROCESS_FOR_FUTURE;
    }

    @Override
    public T get() throws CancellationException, InterruptedException, ExecutionException {
        try {
            return get(0, TimeUnit.MILLISECONDS);
        } catch (final TimeoutException e) {
            throw new Error(e);
        }
    }

    @Override
    public synchronized T get(long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        timeout = unit.toMillis(timeout);
        long period = this.getPeriod();
        long timestamp = timeout > 0 ? System.currentTimeMillis() : 0L;
        while (true) {
            if (period == KaiheilaTask.NO_REPEATING || period == KaiheilaTask.PROCESS_FOR_FUTURE) {
                this.wait(timeout);
                period = this.getPeriod();
                if (period == KaiheilaTask.NO_REPEATING || period == KaiheilaTask.PROCESS_FOR_FUTURE) {
                    if (timeout == 0L) {
                        continue;
                    }
                    timeout += timestamp - (timestamp = System.currentTimeMillis());
                    if (timeout > 0) {
                        continue;
                    }
                    throw new TimeoutException();
                }
            }
            if (period == KaiheilaTask.CANCEL) {
                throw new CancellationException();
            }
            if (period == KaiheilaTask.DONE_FOR_FUTURE) {
                if (exception == null) {
                    return value;
                }
                throw new ExecutionException(exception);
            }
            throw new IllegalStateException("Expected " + KaiheilaTask.NO_REPEATING + " to " + KaiheilaTask.DONE_FOR_FUTURE + ", got " + period);
        }
    }

    @Override
    public void run() {
        synchronized (this) {
            if (getPeriod() == KaiheilaTask.CANCEL) {
                return;
            }
            setPeriod(KaiheilaTask.PROCESS_FOR_FUTURE);
        }
        try {
            value = callable.call();
        } catch (final Exception e) {
            exception = e;
        } finally {
            synchronized (this) {
                setPeriod(KaiheilaTask.DONE_FOR_FUTURE);
                this.notifyAll();
            }
        }
    }

    @Override
    synchronized boolean cancel0() {
        if (getPeriod() != KaiheilaTask.NO_REPEATING) {
            return false;
        }
        setPeriod(KaiheilaTask.CANCEL);
        notifyAll();
        return true;
    }
}
