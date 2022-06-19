package net.deechael.khl.api;

/**
 * 服务器表情，用户自定义上传的表情
 */
public interface Emoji {

    /**
     * 开黑啦唯一标识符 Emoji Id
     *
     * @return Emoji Id
     */
    String getId();

    /**
     * 服务器内 Emoji 名称
     *
     * @return Emoji 名称
     */
    String getName();

    /**
     * 获取表情上传用户
     *
     * @return 上传用户
     */
    User getUploader();

}
