package net.deechael.khl.hook.source.websocket.session.storage;

import net.deechael.khl.hook.source.websocket.WebSocketEventSourceSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class WebSocketSessionFileBaseStorage implements WebSocketSessionStorage {
    protected final static Logger Log = LoggerFactory.getLogger(WebSocketSessionFileBaseStorage.class);

    private final static String dataSplit = ";";
    private final File file;

    public WebSocketSessionFileBaseStorage(File file) {
        this.file = file;
    }

    private boolean sessionFolderWriteable() {
        File parentFile = new File(file.getAbsolutePath()).getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirResult = parentFile.mkdirs();
            if (mkdirResult) {
                Log.warn("WebSocket Session 文件夹创建成功 [{}]", parentFile.getAbsolutePath());
            } else {
                Log.warn("WebSocket Session 文件夹创建失败 [{}]", parentFile.getAbsolutePath());
            }
        }
        if (!parentFile.isDirectory()) {
            Log.warn("无法作为 WebSocket  Session 文件夹使用，当前路径不是文件夹 [{}]", parentFile.getAbsolutePath());
            return false;
        }
        if (!(parentFile.canRead() && parentFile.canWrite())) {
            Log.warn("WebSocket Session 文件夹没有(读/写)权限 [{}]", parentFile.getAbsolutePath());
            return false;
        }
        return true;
    }

    private boolean checkSessionFileWriteable() {
        boolean writeable = sessionFolderWriteable();
        if (!writeable) {
            return false;
        }
        if (!file.exists()) {
            boolean createStatus = false;
            try {
                createStatus = file.createNewFile();
            } catch (IOException ignored) {
            }
            if (createStatus) {
                Log.warn("WebSocket Session 文件创建成功 [{}]", file.getAbsolutePath());
            } else {
                Log.warn("WebSocket Session 文件创建失败 [{}]", file.getAbsolutePath());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveSession(WebSocketEventSourceSession session) {
        if (!checkSessionFileWriteable()) {
            Log.warn("WebSocket Session 文件不可写");
            return false;
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(session.getSessionId() + dataSplit + session.getGateway() + dataSplit + session.getSn());
            writer.flush();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public WebSocketEventSourceSession getSession() {
        String dataLine;
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            return null;
        }
        BufferedReader reader = new BufferedReader(fileReader);
        try {
            dataLine = reader.readLine();
            String[] data = dataLine.split(dataSplit);
            WebSocketEventSourceSession session = new WebSocketEventSourceSession();
            session.setSessionId(data[0]);
            session.setGateway(data[1]);
            session.setSn(Integer.parseInt(data[2]));
            reader.close();
            fileReader.close();
            return session;
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public void clearSession() {
        if (file.exists() && file.delete()) {
            Log.info("WebSocket Session 文件已删除");
        } else {
            Log.warn("WebSocket Session 文件删除失败");
        }
    }
}
