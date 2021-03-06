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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.deechael.khl.hook.EventManagerReceiver;
import net.deechael.khl.hook.EventSource;
import net.deechael.khl.hook.source.EventSourceByteBufferListener;
import net.deechael.khl.util.compression.Compression;
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
     * ???????????????????????????????????? (Zlib ????????? 0x78)
     *
     * @param buffer ????????????
     * @return ????????????
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
                Log.error("WebhookCipher EncryptKey ??????????????????");
            } catch (NoSuchPaddingException | NoSuchAlgorithmException e) {
                Log.error("WebhookCipher ?????? Java ???????????????????????????????????? AES-128-CBC AES/CBC/PKCS5Padding ????????????");
            } catch (InvalidAlgorithmParameterException e) {
                Log.error("WebhookCipher ????????????????????????");
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                Log.error("WebhookCipher AES-CBC ??????????????????");
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
        // JSON ?????????
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
        // verifyToken ??????
        if (!verifyData(data)) {
            Log.warn("Webhook verifyToken ????????????????????????");
            return null;
        }

        // ?????? challenge
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
