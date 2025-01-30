package util.json.deserializer;

import com.google.gson.*;
import task.SubTask;
import task.TaskStatus;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

import static util.JsonUtil.DATE_TIME_FORMAT;

public class SubTaskDeserializer implements JsonDeserializer<SubTask> {

    @Override
    public SubTask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        TaskStatus status = TaskStatus.valueOf(jsonObject.get("status").getAsString().toUpperCase());
        int epicId = jsonObject.get("epicId").getAsInt();

        if (jsonObject.has("duration") && jsonObject.has("startTime")) {
            Duration duration = Duration.ofMinutes(jsonObject.get("duration").getAsLong());
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString(), DATE_TIME_FORMAT);
            return new SubTask(name, description, status, startTime, duration, epicId);
        }

        return new SubTask(name, description, status, epicId);

    }
}