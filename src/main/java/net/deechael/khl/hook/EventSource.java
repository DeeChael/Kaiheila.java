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

package net.deechael.khl.hook;

public abstract class EventSource {

    protected final EventManagerReceiver manager;
    protected boolean paused;

    public EventSource(EventManagerReceiver manager) {
        this.paused = false;
        this.manager = manager;
    }

    public abstract void start();

    public void enableEventPipe() {
        this.paused = false;
    }

    public void disableEventPipe() {
        this.paused = true;
    }

    public abstract void shutdown();

}
