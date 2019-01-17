package com.twinkle.common.util;

/**
 * description: 对象序列化接口
 *
 * @author ：King
 * @date ：2019/1/13 10:26
 */
public interface Serializer {

    /**
     * 序列化程序的名称。
     * 
     * @return
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    String name();

    /**
     * 序列化给定的对象。
     * 
     * @param obj
     *            给定的对象
     * @return
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化。
     * 
     * @param bytes
     * @return
     * 
     * @since 1.0.0
     * @version 1.0.0
     */
     Object deserialize(byte[] bytes);

}
