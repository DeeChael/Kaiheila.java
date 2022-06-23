package org.bukkit.scheduler;

class KaiheilaAsyncDebugger {
    private KaiheilaAsyncDebugger next = null;
    private final int expiry;
    private final Class<?> clazz;

    KaiheilaAsyncDebugger(final int expiry, final Class<?> clazz) {
        this.expiry = expiry;
        this.clazz = clazz;
    }

    final KaiheilaAsyncDebugger getNextHead(final int time) {
        KaiheilaAsyncDebugger next, current = this;
        while (time > current.expiry && (next = current.next) != null) {
            current = next;
        }
        return current;
    }

    final KaiheilaAsyncDebugger setNext(final KaiheilaAsyncDebugger next) {
        return this.next = next;
    }

    StringBuilder debugTo(final StringBuilder string) {
        for (KaiheilaAsyncDebugger next = this; next != null; next = next.next) {
            string.append(next.clazz.getName()).append('@').append(next.expiry).append(',');
        }
        return string;
    }
}
