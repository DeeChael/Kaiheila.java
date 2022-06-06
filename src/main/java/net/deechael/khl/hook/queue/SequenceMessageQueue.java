/*
 *    Copyright 2020-2021 Rabbit author and contributors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.deechael.khl.hook.queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SequenceMessageQueue<T> {
    private final ConcurrentHashMap<Integer, T> buffed = new ConcurrentHashMap<>(4096);
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition;
    private final AtomicInteger latestSn;
    private final AtomicInteger takeSn;

    public SequenceMessageQueue(int initialSn) {
        condition = this.lock.newCondition();
        latestSn = new AtomicInteger(initialSn);
        takeSn = new AtomicInteger(initialSn);
    }

    private int getNextSn(int sn) {
        if (sn == Integer.MAX_VALUE) {
            return 0;
        }
        return ++sn;
    }

    public void push(int sn, T data) {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            buffed.put(sn, data);
            int nextSn = getNextSn(latestSn.intValue());
            if (nextSn == sn) {
                int latest;
                do {
                    latest = nextSn;
                    nextSn = getNextSn(nextSn);
                } while (buffed.containsKey(nextSn));
                latestSn.set(latest);
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        T res;
        ReentrantLock lock = this.lock;
        lock.lockInterruptibly();
        try {
            int nextPos = getNextSn(takeSn.intValue());
            while (!buffed.containsKey(nextPos)) {
                this.condition.await();
            }
            takeSn.set(nextPos);
            res = buffed.get(nextPos);
            buffed.remove(nextPos);
        } finally {
            lock.unlock();
        }
        return res;
    }

    public void clear() {
        ReentrantLock lock = this.lock;
        lock.lock();
        try {
            this.takeSn.set(0);
            this.latestSn.set(0);
            buffed.clear();
        } finally {
            lock.unlock();
        }
    }

    public int getLatestSn() {
        return this.latestSn.intValue();
    }
}