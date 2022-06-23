package org.bukkit.scheduler;

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;
import org.jetbrains.annotations.NotNull;

/**
 * This class is provided as an easy way to handle scheduling tasks.
 */
public abstract class KaiheilaRunnable extends KaiheilaObject implements Runnable {
    private Task task;

    public KaiheilaRunnable(KaiheilaBot kaiheilaBot) {
        super(kaiheilaBot);
    }


    public synchronized boolean isCancelled() throws IllegalStateException {
        checkScheduled();
        return task.isCancelled();
    }

    public synchronized void cancel() throws IllegalStateException {
        getKaiheilaBot().getScheduler().cancelTask(getTaskId());
    }

    @NotNull
    public synchronized Task runTask() throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(getKaiheilaBot().getScheduler().runTask((Runnable) this));
    }

    @NotNull
    public synchronized Task runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(getKaiheilaBot().getScheduler().runTaskAsynchronously((Runnable) this));
    }

    @NotNull
    public synchronized Task runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(getKaiheilaBot().getScheduler().runTaskLater((Runnable) this, delay));
    }

    @NotNull
    public synchronized Task runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(getKaiheilaBot().getScheduler().runTaskLaterAsynchronously((Runnable) this, delay));
    }

    @NotNull
    public synchronized Task runTaskTimer(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(getKaiheilaBot().getScheduler().runTaskTimer((Runnable) this, delay, period));
    }

    @NotNull
    public synchronized Task runTaskTimerAsynchronously(long delay, long period) throws IllegalArgumentException, IllegalStateException {
        checkNotYetScheduled();
        return setupTask(getKaiheilaBot().getScheduler().runTaskTimerAsynchronously((Runnable) this, delay, period));
    }

    public synchronized int getTaskId() throws IllegalStateException {
        checkScheduled();
        return task.getTaskId();
    }

    private void checkScheduled() {
        if (task == null) {
            throw new IllegalStateException("Not scheduled yet");
        }
    }

    private void checkNotYetScheduled() {
        if (task != null) {
            throw new IllegalStateException("Already scheduled as " + task.getTaskId());
        }
    }

    @NotNull
    private Task setupTask(@NotNull final Task task) {
        this.task = task;
        return task;
    }
}
