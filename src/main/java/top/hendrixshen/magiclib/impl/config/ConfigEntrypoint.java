package top.hendrixshen.magiclib.impl.config;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.language.impl.MagicLanguageManager;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.util.ForgeUtil;
import top.hendrixshen.magiclib.util.ReflectUtil;

import java.util.List;

public class ConfigEntrypoint {
    private static final boolean isMalilibLoaded = ForgeUtil.isModLoaded("malilib");
    private static final int configVersion = 1;

    public static @Nullable Screen getGui(Screen parent) {
        if (!isMalilibLoaded) {
            return null;
        }

        Object screen = ReflectUtil.invokeDeclared("top.hendrixshen.magiclib.impl.config.MagicLibConfigGui", "getInstance", null).orElseThrow(RuntimeException::new);
        //#if MC > 11903
        ReflectUtil.invoke("top.hendrixshen.magiclib.impl.config.MagicLibConfigGui", "setParent", screen, new Class[] {Screen.class}, parent);
        //#else
        //$$ ReflectUtil.invoke("top.hendrixshen.magiclib.impl.config.MagicLibConfigGui", "setParentGui", screen, new Class[] {Screen.class}, parent);
        //#endif
        return (Screen) screen;
    }

    public static void init() {
        if (!isMalilibLoaded) {
            return;
        }

        ConfigManager cm = ConfigManager.get(MagicLibReference.getModIdentifier());
        cm.parseConfigClass(MagicLibConfigs.class);
        ConfigHandler configHandler = new ConfigHandler(MagicLibReference.getModIdentifier(), cm, configVersion);
        configHandler.postDeserializeCallback = MagicLibConfigs::postDeserialize;
        ConfigHandler.register(configHandler);
        MagicLibConfigs.init(cm);
    }

    public static @NotNull List<String> getFallbackLanguageListFromConfig() {
        return isMalilibLoaded ? MagicLibConfigs.fallbackLanguageList : Lists.newArrayList(MagicLanguageManager.INSTANCE.getCurrentCode());
    }
}
