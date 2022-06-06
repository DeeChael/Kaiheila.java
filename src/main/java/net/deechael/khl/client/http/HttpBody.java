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

package net.deechael.khl.client.http;

import java.nio.ByteBuffer;

public abstract class HttpBody {

    private final int contentLength;
    private final HttpMediaType contentType;
    private final ByteBuffer buffer;

    public HttpBody(int contentLength, HttpMediaType contentType, ByteBuffer buffer) {
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.buffer = buffer;
    }

    public int getContentLength() {
        return contentLength;
    }

    public HttpMediaType getContentType() {
        return contentType;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    @Override
    public String toString() {
        return "HttpBody{" +
                "contentLength=" + contentLength +
                ", contentType=" + contentType +
                '}';
    }
}
