package com.twinkle.common.util;

/**
 * description: 序列化工具类
 *
 * @author ：King
 * @date ：2019/1/13 10:26
 */
public final class SerializationUtil {

    private SerializationUtil() {
        throw new Error("Do not allow instantiation!");
    }

    private static Serializer serializer;

    static {
        serializer = new FSTSerializer();
    }

    /**
     * 序列化。
     * 
     * @param obj
     * @return
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    public static byte[] serialize(Object obj) {
        return serializer.serialize(obj);
    }

    /**
     * 反序列化。
     * 
     * @param bytes
     * @return
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    public static Object deserialize(byte[] bytes) {
        return serializer.deserialize(bytes);
    }

}
