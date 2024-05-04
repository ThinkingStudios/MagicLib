package top.hendrixshen.magiclib.malilib.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.util.MiscUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class ConfigHandler implements IConfigHandler {
    public final Path configPath;
    public final ConfigManager configManager;
    public final int configVersion;
    public final String modId;

    @Nullable
    public Consumer<ConfigHandler> preDeserializeCallback;
    @Nullable
    public Consumer<ConfigHandler> postDeserializeCallback;
    @Nullable
    public Consumer<ConfigHandler> preSerializeCallback;
    @Nullable
    public Consumer<ConfigHandler> postSerializeCallback;

    public JsonObject jsonObject;

    public ConfigHandler(String modId, ConfigManager configManager, int configVersion) {
        this(modId, Paths.get(String.format("%s.json", modId)), configManager, configVersion);
    }

    @Deprecated
    public ConfigHandler(String modId, ConfigManager configManager, int configVersion,
                         @Nullable Consumer<ConfigHandler> preDeserializeCallback,
                         @Nullable Consumer<ConfigHandler> postSerializeCallback) {
        this(modId, Paths.get(String.format("%s.json", modId)), configManager, configVersion,
                preDeserializeCallback, postSerializeCallback);
    }

    @Deprecated
    public ConfigHandler(String modId, Path configPath, ConfigManager configManager, int configVersion,
                         @Nullable Consumer<ConfigHandler> preDeserializeCallback,
                         @Nullable Consumer<ConfigHandler> postSerializeCallback) {
        this.configPath = FileUtils.getConfigDirectory().toPath().resolve(configPath);
        this.configManager = configManager;
        this.preDeserializeCallback = preDeserializeCallback;
        this.postSerializeCallback = postSerializeCallback;
        this.configVersion = configVersion;
        this.jsonObject = new JsonObject();
        this.modId = modId;
    }

    public ConfigHandler(String modId, Path configPath, ConfigManager configManager, int configVersion) {
        this.configPath = FileUtils.getConfigDirectory().toPath().resolve(configPath);
        this.configManager = configManager;
        this.configVersion = configVersion;
        this.jsonObject = new JsonObject();
        this.modId = modId;
    }

    public static void register(ConfigHandler configHandler) {
        fi.dy.masa.malilib.config.ConfigManager.getInstance().registerConfigHandler(configHandler.modId, configHandler);
    }

    // From Malilib for MC 1.18.x.
    public static boolean writeJsonToFile(JsonObject root, @NotNull File file) {
        File fileTmp = new File(file.getParentFile(), file.getName() + ".tmp");

        if (fileTmp.exists()) {
            fileTmp = new File(file.getParentFile(), UUID.randomUUID() + ".tmp");
        }

        try {
            OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(fileTmp.toPath()), StandardCharsets.UTF_8);
            writer.write(JsonUtils.GSON.toJson(root));
            writer.close();

            if (file.exists() && file.isFile() && !file.delete()) {
                MagicLibReference.getLogger().warn("Failed to delete file '{}'", file.getAbsolutePath());
            }

            return fileTmp.renameTo(file);
        } catch (IOException e) {
            MagicLibReference.getLogger().warn("Failed to write JSON data to file '{}'", fileTmp.getAbsolutePath(), e);
        }

        return false;
    }

    // Modified from Malilib.
    @Nullable
    public static JsonElement parseJsonFile(File file) {
        if (file != null && file.exists() && file.isFile() && file.canRead()) {
            String fileName = file.getAbsolutePath();

            try {
                InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8);
                JsonElement element = MiscUtil.GSON.fromJson(inputStreamReader, JsonElement.class);
                inputStreamReader.close();
                return element;
            } catch (Exception e) {
                MagicLibReference.getLogger().error("Failed to parse the JSON file '{}'", fileName, e);
            }
        }

        return null;
    }

    public void loadFromFile() {
        JsonElement jsonElement = parseJsonFile(this.configPath.toFile());

        if (jsonElement != null && jsonElement.isJsonObject()) {
            this.jsonObject = jsonElement.getAsJsonObject();

            if (this.preDeserializeCallback != null) {
                this.preDeserializeCallback.accept(this);
            }

            for (String category : this.configManager.getCategories()) {
                List<ConfigBase<?>> configs = this.configManager.getOptionsByCategory(category).stream()
                        .map(ConfigOption::getConfig).collect(Collectors.toList());
                ConfigUtils.readConfigBase(this.jsonObject, category, configs);
            }
        }

        if (this.postDeserializeCallback != null) {
            this.postDeserializeCallback.accept(this);
        }
    }

    public void saveToFile() {
        if (this.preSerializeCallback != null) {
            this.preSerializeCallback.accept(this);
        }

        for (String category : this.configManager.getCategories()) {
            List<ConfigBase<?>> configs = this.configManager.getOptionsByCategory(category).stream()
                    .map(ConfigOption::getConfig).collect(Collectors.toList());
            ConfigUtils.writeConfigBase(this.jsonObject, category, configs);
        }

        if (this.postSerializeCallback != null) {
            this.postSerializeCallback.accept(this);
        }

        this.jsonObject.add("configVersion", new JsonPrimitive(this.configVersion));
        writeJsonToFile(this.jsonObject, this.configPath.toFile());
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
