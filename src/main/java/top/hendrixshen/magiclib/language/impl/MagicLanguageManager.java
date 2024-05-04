package top.hendrixshen.magiclib.language.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.impl.config.ConfigEntrypoint;
import top.hendrixshen.magiclib.util.MiscUtil;

//#if MC <= 11802
//$$ import java.io.FileNotFoundException;
//#endif
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MagicLanguageManager implements ResourceManagerReloadListener {
    public static final String DEFAULT_CODE = "en_us";
    public static final MagicLanguageManager INSTANCE = new MagicLanguageManager();
    public ConcurrentHashMap<String, String> defaultLanguage = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ConcurrentHashMap<String, String>> language = new ConcurrentHashMap<>();

    ArrayList<String> fallbackLanguageList = Lists.newArrayList(DEFAULT_CODE);
    @Getter
    private String currentCode = DEFAULT_CODE;
    @NotNull
    private ResourceManager resourceManager;

    private MagicLanguageManager() {
        this.resourceManager = new MagicLanguageResourceManager();
        this.reload();
    }

    public void setCurrentCode(String currentCode) {
        if (!Objects.equals(this.currentCode, currentCode)) {
            this.currentCode = currentCode;
            this.reload();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void initClient() {
        this.resourceManager = Minecraft.getInstance().getResourceManager();
        this.fallbackLanguageList = Lists.newArrayList(ConfigEntrypoint.getFallbackLanguageListFromConfig());
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(INSTANCE);
        this.reload();
    }

    private void initLanguage(String code, ConcurrentHashMap<String, String> language) {
        String languagePath = String.format("lang/%s.json", code);
        Set<String> nameSpaces;

        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            // 1.15 的 server 缺少 ResourceManager.getNamespaces
            nameSpaces = ((MagicLanguageResourceManager) resourceManager).getNamespaces();
        } else {
            nameSpaces = resourceManager.getNamespaces();
        }

        for (String namespace : nameSpaces) {
            ResourceLocation resourceLocation = new ResourceLocation(namespace, languagePath);

            try {
                for (Resource resource : resourceManager.getResourceStack(resourceLocation)) {
                    try {
                        InputStream inputStream = resource.open();
                        try {
                            MiscUtil.loadStringMapFromJson(inputStream, language::put);
                        } catch (Throwable e) {
                            if (inputStream != null) {
                                inputStream.close();
                            }

                            MagicLibReference.getLogger().warn("Failed to parse translations from {} ({})", resource, e);
                            continue;
                        }

                        // from minecraft code
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        MagicLibReference.getLogger().warn("Failed to load translations from {} ({})", resource, e);
                    }
                }
            //#if MC <= 11802
            //$$ } catch (FileNotFoundException ignored) {
            //$$ // ignore..
            //#endif
            } catch (Exception e) {
                MagicLibReference.getLogger().warn("Failed to load translations from {}:{} ({})", namespace, languagePath, e);
            }
        }
    }

    public void reload() {
        this.defaultLanguage.clear();
        this.language.clear();
        ArrayList<String> codes = new ArrayList<>(fallbackLanguageList);
        codes.remove(currentCode);
        codes.add(0, currentCode);

        if (!codes.contains(DEFAULT_CODE)) {
            codes.add(DEFAULT_CODE);
        }

        for (int i = codes.size() - 1; i >= 0; --i) {
            String code = codes.get(i);
            ConcurrentHashMap<String, String> currentLanguage = new ConcurrentHashMap<>();
            initLanguage(code, currentLanguage);
            this.language.put(code, currentLanguage);
            this.defaultLanguage.putAll(currentLanguage);
        }
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        reload();
    }

    public String get(String key, Object... objects) {
        String translateValue = this.defaultLanguage.getOrDefault(key, key);

        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public String get(String key) {
        return this.defaultLanguage.getOrDefault(key, key);
    }

    public String getByCode(String code, String key) {
        ConcurrentHashMap<String, String> currentLanguage = this.language.getOrDefault(code, null);

        if (currentLanguage == null) {
            currentLanguage = new ConcurrentHashMap<>();
            this.initLanguage(code, currentLanguage);
            this.language.put(code, currentLanguage);
        }

        return currentLanguage.getOrDefault(key, key);
    }

    public String getByCode(String code, String key, Object... objects) {
        ConcurrentHashMap<String, String> currentLanguage = this.language.getOrDefault(code, null);

        if (currentLanguage == null) {
            currentLanguage = new ConcurrentHashMap<>();
            this.initLanguage(code, currentLanguage);
            this.language.put(code, currentLanguage);
        }

        String translateValue = currentLanguage.getOrDefault(key, key);

        try {
            return String.format(translateValue, objects);
        } catch (IllegalFormatException var4) {
            return "Format error: " + translateValue;
        }
    }

    public boolean exists(String key) {
        return this.defaultLanguage.containsKey(key);
    }

    public boolean exists(String code, String key) {
        ConcurrentHashMap<String, String> currentLanguage = this.language.getOrDefault(code, null);

        if (currentLanguage == null) {
            currentLanguage = new ConcurrentHashMap<>();
            this.initLanguage(code, currentLanguage);
            this.language.put(code, currentLanguage);
        }

        return currentLanguage.containsKey(key);
    }
}