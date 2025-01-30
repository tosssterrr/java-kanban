package util;

import com.google.gson.*;
import task.Epic;
import task.SubTask;
import task.Task;
import util.json.deserializer.EpicDeserializer;
import util.json.deserializer.SubTaskDeserializer;
import util.json.deserializer.TaskDeserializer;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonUtil {
    private static final Gson gson;

    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Epic.class, new EpicDeserializer())
                .registerTypeAdapter(SubTask.class, new SubTaskDeserializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .registerTypeAdapter(Duration.class, new DurationSerializer())
                .registerTypeAdapter(Duration.class, new DurationDeserializer())
                .setPrettyPrinting()
                .create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> className) {
        return gson.fromJson(json, className);
    }

    private static class LocalDateTimeSerializer implements JsonSerializer<LocalDateTime> {

        @Override
        public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(DATE_TIME_FORMAT.format(src));
        }
    }

    private static class LocalDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {

        @Override
        public LocalDateTime deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
            return LocalDateTime.parse(json.getAsString(), DATE_TIME_FORMAT);
        }
    }

    private static class DurationSerializer implements JsonSerializer<Duration> {

        @Override
        public JsonElement serialize(Duration duration, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(duration.toMinutes());
        }
    }

    private static class DurationDeserializer implements JsonDeserializer<Duration> {

        @Override
        public Duration deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
            long minutes = json.getAsLong();
            return Duration.ofMinutes(minutes);
        }
    }

}
