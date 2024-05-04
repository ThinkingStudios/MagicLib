package top.hendrixshen.magiclib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;
import top.hendrixshen.magiclib.impl.config.ConfigEntrypoint;
import top.hendrixshen.magiclib.tool.mixin.MixinAuditExecutor;

//#if MC > 11903 && MC < 12000
//$$ import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
//$$ import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
//#endif

@Mod("magiclib")
public class MagicLib {

    public MagicLib() {
        this.onInitialize();
        if (FMLLoader.getDist().isClient()) {
            this.onInitializeClient();
        }
    }

    //#if MC > 11903 && MC < 12000
    //$$ @Dependencies(and = @Dependency(value = "malilib", versionPredicate = ">=0.15.4", optional = true))
    //#endif
    public void onInitializeClient() {
        ConfigEntrypoint.init();
        MagicLibReference.getLogger().info("[{}|Client]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }

    public void onInitialize() {
        MixinAuditExecutor.execute();
        CarpetEntrypoint.init();
        MagicLibReference.getLogger().info("[{}|Common]: Mod initialized - Version: {}", MagicLibReference.getModName(), MagicLibReference.getModVersion());
    }
}
