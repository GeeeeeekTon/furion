//package org.furion.core.utils;
//
//import com.dyuproject.protostuff.LinkedBuffer;
//import com.dyuproject.protostuff.ProtostuffIOUtil;
//import com.dyuproject.protostuff.Schema;
//import com.dyuproject.protostuff.runtime.RuntimeSchema;
//import org.springframework.objenesis.Objenesis;
//import org.springframework.objenesis.ObjenesisStd;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author 仓颉
// *用户可以根据需要自己定制化序列化方式
// */
//public class SerializationUtil {
////对schema做缓存，加快编解码速度
//	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();
//
//    private static Objenesis objenesis = new ObjenesisStd(true);
//
//    private SerializationUtil() {
//    }
//
//    @SuppressWarnings("unchecked")
//    private static <T> Schema<T> getSchema(Class<T> cls) {
//        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
//        if (schema == null) {
//            schema = RuntimeSchema.createFrom(cls);
//            if (schema != null) {
//                cachedSchema.put(cls, schema);
//            }
//        }
//        return schema;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> byte[] serialize(T obj) {
//        Class<T> cls = (Class<T>) obj.getClass();
//        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
//        try {
//            Schema<T> schema = getSchema(cls);
//            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
//        } catch (Exception e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        } finally {
//            buffer.clear();
//        }
//    }
//
//    public static <T> T deserialize(byte[] data, Class<T> cls) {
//        try {
//            T message = (T) objenesis.newInstance(cls);
//            Schema<T> schema = getSchema(cls);
//            ProtostuffIOUtil.mergeFrom(data, message, schema);
//            return message;
//        } catch (Exception e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        }
//    }
//}
