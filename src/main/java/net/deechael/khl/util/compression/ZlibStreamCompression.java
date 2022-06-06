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

package net.deechael.khl.util.compression;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.InflaterInputStream;

public class ZlibStreamCompression implements Compression {

    /**
     * 解压Zlib格式压缩字节数组
     *
     * @param data 压缩数据字节数组
     * @return 解压后字节数组
     * @throws IOException IO流问题
     */
    @Override
    public ByteBuffer decompress(ByteBuffer data) throws IOException {
        InflaterInputStream inputStream = new InflaterInputStream(new ByteArrayInputStream(data.array()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        inputStream.transferTo(outputStream);
        return ByteBuffer.wrap(outputStream.toByteArray());
    }
}
