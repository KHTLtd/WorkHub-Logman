package json;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.workhub.logman.data.deserialization.LocalDateTimeDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * JSON Mapping utils
 *
 * @author alexkirillov
 * @since 1.0.0 | 24.11.2021
 */
@SuppressWarnings("deprecation")
public abstract class UtilJson {
    private static final Logger log = LoggerFactory.getLogger(UtilJson.class);
    private static final ObjectMapper OBJECT_MAPPER_BY_GETTER = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER_BY_FIELD = new ObjectMapper();
    private static final TypeReference<HashMap<String, String>> TYPE_MAP = new TypeReference<>() { };

    static {
        SimpleModule module = new SimpleModule();

        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());

        OBJECT_MAPPER_BY_FIELD.registerModule(module);
        OBJECT_MAPPER_BY_GETTER.registerModule(module);

        OBJECT_MAPPER_BY_FIELD.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        OBJECT_MAPPER_BY_GETTER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        OBJECT_MAPPER_BY_FIELD.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        OBJECT_MAPPER_BY_FIELD.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER_BY_FIELD.setTimeZone(Calendar.getInstance().getTimeZone());

        OBJECT_MAPPER_BY_GETTER.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        OBJECT_MAPPER_BY_GETTER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER_BY_GETTER.setTimeZone(Calendar.getInstance().getTimeZone());

        OBJECT_MAPPER_BY_FIELD.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER_BY_GETTER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getDefaultObjectMapper() {
        return OBJECT_MAPPER_BY_GETTER;
    }

    public static <T> T readFromJson(String jsonStrinng, Class<T> tClass) {
        try {
            return OBJECT_MAPPER_BY_GETTER.readerFor(tClass).readValue(jsonStrinng);
        } catch (Exception e) {
            log.info("Failed to parse JSON string : {}", e.getMessage());
            return null;
        }
    }

    public static Map<String, String> readMapFromJson(String jsonString) {
        try {
            return OBJECT_MAPPER_BY_GETTER.readValue(jsonString, TYPE_MAP);
        } catch (Exception e) {
            log.info("Failed to parse JSON string : {}", e.getMessage());
            return null;
        }
    }
    
    public static String generateJsonObjectStringForClass(Object object) {
        List<Method> methods = accumulateGetters(object.getClass().getMethods());
        StringBuilder jsonString = new StringBuilder("{");
        int len = methods.size();
        for (int i = 0; i < len; i++) {
            String methodName = methods.get(i).getName();
            jsonString.append("\"").append(getFieldNameFromGetter(methodName)).append("\":\"");
            if (i < len - 1) {
                try {
                    if (methodName.contains("Stamp")) {
                        jsonString.append(((LocalDateTime)methods.get(i).invoke(object))
                                .toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now())).toEpochMilli());
                    } else {
                        jsonString.append(methods.get(i).invoke(object));
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error("Failed to invoke getter of the object [{}]. Value will be set to NULL", object);
                }
                jsonString.append("\",");
            } else {
                try {
                    if (methodName.contains("Stamp")) {
                        jsonString.append(((LocalDateTime)methods.get(i).invoke(object))
                                .toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))
                                .toEpochMilli()).append("\"");
                    } else {
                        jsonString.append(methods.get(i).invoke(object)).append("\"");
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    log.error("Failed to invoke getter of the object [{}]. Value will be set to NULL", object);
                }
                    jsonString.append("}");
            }
        }
        return jsonString.toString();
    }

    private static String getFieldNameFromGetter(String methodName) {
        methodName = methodName.substring(3);
        char firstLetter = methodName.substring(0, 1).toLowerCase(Locale.getDefault()).toCharArray()[0];
        return methodName.replace(methodName.charAt(0), firstLetter) ;
    }

    private static List<Method> accumulateGetters(Method[] methods) {
        List<Method> getters = new ArrayList<>();
        for (Method method : methods) {
            String name = method.getName();
            if (name.contains("get") && !name.equals("getClass")) {
                getters.add(method);
            }
        }
        return getters;
    }

}
