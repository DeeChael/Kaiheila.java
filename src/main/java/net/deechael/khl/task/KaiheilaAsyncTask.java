package net.deechael.khl.task;

import net.deechael.khl.bot.KaiheilaBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

class KaiheilaAsyncTask extends KaiheilaTask {

    protected static final Logger Log = LoggerFactory.getLogger(KaiheilaAsyncTask.class);

    private final LinkedList<Worker> workers = new LinkedList<Worker>();
    private final Map<Integer, KaiheilaTask> runners;

    KaiheilaAsyncTask(final Map<Integer, KaiheilaTask> runners, final KaiheilaBot kaiheilaBot, final Object task, final int id, final long delay) {
        super(kaiheilaBot, task, id, delay);
        this.runners = runners;
    }

    @Override
    public boolean isSync() {
        return false;
    }

    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        synchronized (workers) {
            if (getPeriod() == KaiheilaTask.CANCEL) {
                // Never continue running after cancelled.
                // Checking this with the lock is important!
                return;
            }
            workers.add(
                    new Worker() {
                        @Override
                        public Thread getThread() {
                            return thread;
                        }

                        @Override
                        public int getTaskId() {
                            return KaiheilaAsyncTask.this.getTaskId();
                        }
                    });
        }
        Throwable thrown = null;
        try {
            super.run();
        } catch (final Throwable t) {
            thrown = t;
            Log.warn(
                    String.format(
                            "Generated an exception while executing task %s",
                            getTaskId()),
                    thrown);
        } finally {
            // Cleanup is important for any async task, otherwise ghost tasks are everywhere
            synchronized (workers) {
                try {
                    final Iterator<Worker> workers = this.workers.iterator();
                    boolean removed = false;
                    while (workers.hasNext()) {
                        if (workers.next().getThread() == thread) {
                            workers.remove();
                            removed = true; // Don't throw exception
                            break;
                        }
                    }
                    if (!removed) {
                        throw new IllegalStateException(
                                String.format(
                                        "Unable to remove worker %s on task %s",
                                        thread.getName(),
                                        getTaskId()),
                                thrown); // We don't want to lose the original exception, if any
                    }
                } finally {
                    if (getPeriod() < 0 && workers.isEmpty()) {
                        // At this spot, we know we are the final async task being executed!
                        // Because we have the lock, nothing else is running or will run because delay < 0
                        runners.remove(getTaskId());
                    }
                }
            }
        }
    }

    LinkedList<Worker> getWorkers() {
        return workers;
    }

    @Override
    boolean cancel0() {
        synchronized (workers) {
            // Synchronizing here prevents race condition for a completing task
            setPeriod(KaiheilaTask.CANCEL);
            if (workers.isEmpty()) {
                runners.remove(getTaskId());
            }
        }
        return true;
    }
}
