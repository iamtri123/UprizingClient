package net.minecraft.client.audio;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import org.apache.commons.lang3.Validate;

public class SoundListSerializer implements JsonDeserializer {

    public SoundList deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext p_deserialize_3_) {
        final JsonObject jsonObject = JsonUtils.getJsonElementAsJsonObject(jsonElement, "entry");

        final SoundList soundList = new SoundList();
        soundList.setReplaceExisting(JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonObject, "replace", false));

        final SoundCategory soundCategory = SoundCategory.func_147154_a(JsonUtils.getJsonObjectStringFieldValueOrDefault(jsonObject, "category", SoundCategory.MASTER.getCategoryName()));
        soundList.setSoundCategory(soundCategory);

        Validate.notNull(soundCategory, "Invalid category");

        if (jsonObject.has("sounds")) {
            JsonArray var7 = JsonUtils.getJsonObjectJsonArrayField(jsonObject, "sounds");

            for (int var8 = 0; var8 < var7.size(); ++var8) {
                JsonElement var9 = var7.get(var8);
                SoundList.SoundEntry soundEntry = new SoundList.SoundEntry();

                if (JsonUtils.jsonElementTypeIsString(var9)) {
                    soundEntry.setName(JsonUtils.getJsonElementStringValue(var9, "sound"));
                } else {
                    JsonObject var11 = JsonUtils.getJsonElementAsJsonObject(var9, "sound");
                    soundEntry.setName(JsonUtils.getJsonObjectStringFieldValue(var11, "name"));

                    if (var11.has("type")) {
                        SoundList.SoundEntry.Type var12 = SoundList.SoundEntry.Type.getType(JsonUtils.getJsonObjectStringFieldValue(var11, "type"));
                        Validate.notNull(var12, "Invalid type");
                        soundEntry.setType(var12);
                    }

                    float var13;

                    if (var11.has("volume")) {
                        var13 = JsonUtils.getJsonObjectFloatFieldValue(var11, "volume");
                        Validate.isTrue(var13 > 0.0F, "Invalid volume");
                        soundEntry.setVolume(var13);
                    }

                    if (var11.has("pitch")) {
                        var13 = JsonUtils.getJsonObjectFloatFieldValue(var11, "pitch");
                        Validate.isTrue(var13 > 0.0F, "Invalid pitch");
                        soundEntry.setPitch(var13);
                    }

                    if (var11.has("weight")) {
                        int var14 = JsonUtils.getJsonObjectIntegerFieldValue(var11, "weight");
                        Validate.isTrue(var14 > 0, "Invalid weight");
                        soundEntry.setWeight(var14);
                    }

                    if (var11.has("stream")) {
                        soundEntry.setStreaming(JsonUtils.getJsonObjectBooleanFieldValue(var11, "stream"));
                    }
                }

                soundList.getSoundList().add(soundEntry);
            }
        }

        return soundList;
    }
}