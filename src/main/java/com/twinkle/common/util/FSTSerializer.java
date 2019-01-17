package com.twinkle.common.util;


import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * description: 使用FST实现的序列化
 *
 * @author ：King
 * @date ：2019/1/13 10:26
 */
public class FSTSerializer implements Serializer {

    private static Logger logger = LoggerFactory.getLogger(FSTSerializer.class);

    public static final String FST_SERIALIZER = "fst";

    @Override
    public String name() {
        return FST_SERIALIZER;
    }

    @Override
    public byte[] serialize(Object obj) {
        if (null == obj) {
            return new byte[0];
        }
        else if (!(obj instanceof Serializable)) {
            throw new IllegalArgumentException("[" + obj.getClass().getName() + "] does not implement java.io.Serializable interface.");
        }

        byte[] result = null;
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream(128);
        FSTObjectOutput fstOutputStream = null;

        try {
            fstOutputStream = new FSTObjectOutput(bytesOutputStream);
            fstOutputStream.writeObject(obj);
            result = bytesOutputStream.toByteArray();
        }
        catch (IOException e) {
            logger.error("Failed to serialize.", e);
        }
        catch (Exception e) {
            logger.error("Failed to serialize.", e);
        }
        finally {
            if (null != fstOutputStream) {
                try {
                    fstOutputStream.close();
                }
                catch (IOException e) {
                }
            }
        }

        return result;
    }

    @Override
    public Object deserialize(byte[] bytes) {
        if (null == bytes || 0 == bytes.length) {
            return null;
        }

        Object result = null;
        FSTObjectInput fstObjectInput = null;

        try {
            fstObjectInput = new FSTObjectInput(new ByteArrayInputStream(bytes));
            result = fstObjectInput.readObject();
        }
        catch (ClassNotFoundException e) {
            logger.error("Class of a serialized object cannot be found.", e);
        }
        catch (IOException e) {
            logger.error("Failed to deserialize.", e);
        }
        catch (Exception e) {
            logger.error("Failed to deserialize.", e);
        }
        finally {
            if (null != fstObjectInput) {
                try {
                    fstObjectInput.close();
                }
                catch (IOException e) {
                }
            }
        }

        return result;
    }

}
