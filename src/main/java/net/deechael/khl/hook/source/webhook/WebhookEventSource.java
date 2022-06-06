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

package net.deechael.khl.hook.source.webhook;

import net.deechael.khl.hook.EventManagerReceiver;
import net.deechael.khl.hook.EventSource;
import net.deechael.khl.hook.source.EventSourceByteBufferListener;
import net.deechael.khl.util.compression.Compression;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;

public class WebhookEventSource extends EventSource implements EventSourceByteBufferListener {
    protected static final Logger Log = LoggerFactory.getLogger(WebhookEventSource.class);

    private final Compression compression;
    private final ObjectMapper mapper;
    private final String verifyToken;
    private final String encryptKey;

    public WebhookEventSource(EventManagerReceiver manager, Compression compression, ObjectMapper mapper, String verifyToken, String encryptKey) {
        super(manager);
        this.compression = compression;
        this.mapper = mapper;
        this.verifyToken = verifyToken;
        this.encryptKey = encryptKey;
    }

    private boolean verifyData(JsonNode node) {
        String token = node.has("verify_token") ? node.get("verify_token").asText() : null;
        if (token != null) {
            if (verifyToken == null) {
                return false;
            }
            return verifyToken.equals(token);
        }
        return true;
    }

    private String returnChallenge(JsonNode node) {
        if (node.get("type").asInt() == 255 && node.get("channel_type").asText().equals("WEBHOOK_CHALLENGE")) {
            String challenge = node.get("challenge").asText();
            return "{\"challenge\":\"" + challenge + "\"}";
        }
        return null;
    }

    /**
     * 数据解压，压缩文件自适应 (Zlib 魔术字 0x78)
     *
     * @param buffer 原始数据
     * @return 目标数据
     */
    private ByteBuffer decompress(ByteBuffer buffer) {
        ByteBuffer byteBuffer = buffer;
        if (buffer.get(0) == 0x78) {
            try {
                byteBuffer = compression.decompress(buffer);
            } catch (IOException ignored) {
            }
        }
        return byteBuffer;
    }

    private byte[] decrypt(JsonNode root) {
        if (root.has("encrypt")) {
            String encrypt = root.get("encrypt").asText();
            try {
                return WebhookCipher.decrypt(encryptKey, encrypt.getBytes(StandardCharsets.UTF_8));
            } catch (KeyException e) {
                Log.error("WebhookCipher EncryptKey 密钥格式错误");
            } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                Log.error("WebhookCipher 当前 Java 发行版本（或版本）不支持 AES-128-CBC AES/CBC/PKCS5Padding 解密算法");
            } catch (InvalidAlgorithmParameterException e) {
                Log.error("WebhookCipher 当前算法参数有误");
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                Log.error("WebhookCipher AES-CBC 错误数据填充");
            }
        }
        return null;
    }

    private JsonNode readJsonTree(byte[] bytes) {
        try {
            return mapper.readTree(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String transfer(ByteBuffer buffer) {
        ByteBuffer byteBuffer = decompress(buffer);
        // JSON 预加载
        JsonNode root = readJsonTree(byteBuffer.array());
        if (root == null) {
            return null;
        }
        byte[] decrypt = decrypt(root);
        root = readJsonTree(decrypt);
        if (root == null) {
            return null;
        }

        JsonNode data = root.get("d");
        // verifyToken 匹配
        if (!verifyData(data)) {
            Log.warn("Webhook verifyToken 与当前配置不匹配");
            return null;
        }

        // 返回 challenge
        String challenge = returnChallenge(data);
        if (challenge != null) {
            return challenge;
        }

        if (!super.paused) {
            super.manager.process(root.get("sn").asInt(), data.toString());
        }
        return null;
    }

    @Override
    public void start() {
    }

    @Override
    public void shutdown() {
    }
}
