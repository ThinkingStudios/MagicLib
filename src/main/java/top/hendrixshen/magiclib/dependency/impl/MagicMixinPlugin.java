package top.hendrixshen.magiclib.dependency.impl;

import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.compat.impl.MagicExtension;
import top.hendrixshen.magiclib.dependency.api.DepCheckFailureCallback;
import top.hendrixshen.magiclib.dependency.api.EmptyMixinPlugin;
import top.hendrixshen.magiclib.util.MagicStreamHandler;
import top.hendrixshen.magiclib.util.MixinUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Optional;

public class MagicMixinPlugin extends EmptyMixinPlugin {
    private static boolean compatVersionChecked = false;

    private DepCheckFailureCallback depCheckFailureCallback =
            (targetClassName, mixinClassName, reason) -> {
                if (MixinEnvironment.getCurrentEnvironment().getOption(MixinEnvironment.Option.DEBUG_EXPORT)) {
                    MagicLibReference.getLogger().warn("{}: \nMixin {} can't apply to {} because: \n{}",
                            Optional.ofNullable(reason.getCause()).orElse(reason).getClass().getSimpleName(),
                            mixinClassName, targetClassName, reason.getMessage());
                }
            };

    public void setDepCheckFailureCallback(DepCheckFailureCallback depCheckFailureCallback) {
        this.depCheckFailureCallback = depCheckFailureCallback;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Even if you remove the mixin from preApply in extension, the log will still have a lot of warnings.
        return Dependencies.checkDependency(targetClassName, mixinClassName, this.depCheckFailureCallback);
    }

    @Override
    public void onLoad(String mixinPackage) {
        if (!compatVersionChecked) {
            compatVersionChecked = true;
            //ForgeUtil.compatVersionCheck();

            try {
                Object transformer = MixinEnvironment.getCurrentEnvironment().getActiveTransformer();

                if (transformer == null) {
                    throw new IllegalStateException("Not running with a transformer?");
                }

                Field extensionsField = transformer.getClass().getDeclaredField("extensions");
                extensionsField.setAccessible(true);
                Extensions extensions = (Extensions) extensionsField.get(transformer);
                extensions.add(new MagicExtension());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            Object urlLoader = Thread.currentThread().getContextClassLoader();
            Class<?> knotClassLoader = null;
            String[] knotClassLoaderNames = {
                    "org.quiltmc.loader.impl.launch.knot.KnotClassLoader",
                    "net.fabricmc.loader.impl.launch.knot.KnotClassLoader",
                    "net.fabricmc.loader.launch.knot.KnotClassLoader"};
            for (String knotClassLoaderName : knotClassLoaderNames) {
                try {
                    knotClassLoader = Class.forName(knotClassLoaderName);
                } catch (ClassNotFoundException e) {
                    continue;
                }

                break;
            }

            if (knotClassLoader == null) {
                throw new RuntimeException("Can't found KnotClassLoader");
            }

            try {
                Method method;

                try {
                    method = knotClassLoader.getDeclaredMethod("addUrlFwd", URL.class);
                } catch (NoSuchMethodException e) {
                    method = knotClassLoader.getDeclaredMethod("addURL", URL.class);
                }

                method.setAccessible(true);
                method.invoke(urlLoader, MagicStreamHandler.getMemoryClassLoaderUrl());
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        MixinUtil.commitMagicClass();
    }
}
