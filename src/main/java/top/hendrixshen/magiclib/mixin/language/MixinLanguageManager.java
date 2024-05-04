package top.hendrixshen.magiclib.mixin.language;

import com.google.common.collect.Lists;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.config.ConfigEntrypoint;
import top.hendrixshen.magiclib.language.impl.MagicLanguageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

//#if MC > 11201
import org.spongepowered.asm.mixin.Unique;
//#endif

@OnlyIn(Dist.CLIENT)
@Mixin(LanguageManager.class)
public class MixinLanguageManager {
    @Shadow
    private String currentCode;

    //#if MC > 11502
    @Shadow
    private Map<String, LanguageInfo> languages;

    //#if MC > 12001
    @Unique
    private static final String magiclib$DEFAULT_LANGUAGE = "en_us";
    //#else
    //$$ @Shadow
    //$$ @Final
    //#if MC > 11903
    //$$ public static String DEFAULT_LANGUAGE_CODE;
    //#else
    //$$ private static LanguageInfo DEFAULT_LANGUAGE;
    //#endif
    //#endif
    //#endif

    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postInit(String string, CallbackInfo ci) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    @Inject(
            method = "setSelected",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postSetSelected(
            //#if MC > 11903
            String languageCode,
            //#else
            //$$ LanguageInfo languageInfo,
            //#endif
            CallbackInfo ci
    ) {
        MagicLanguageManager.INSTANCE.setCurrentCode(this.currentCode);
    }

    //#if MC > 11502
    @Contract("_ -> param1")
    @ModifyVariable(
            method = "onResourceManagerReload",
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11903
                    target = "Lnet/minecraft/client/resources/language/ClientLanguage;loadFrom(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resources/language/ClientLanguage;",
                    //#else
                    //$$ target = "Lnet/minecraft/client/resources/language/ClientLanguage;loadFrom(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;)Lnet/minecraft/client/resources/language/ClientLanguage;",
                    //#endif
                    ordinal = 0
            )
    )
    //#if MC > 11903
    private @NotNull List<String> addFallbackLanguage(List<String> languageInfoList) {
    //#else
    //$$ private @NotNull List<LanguageInfo> addFallbackLanguage(List<LanguageInfo> languageInfoList) {
    //#endif
        ArrayList<String> codes = Lists.newArrayList(ConfigEntrypoint.getFallbackLanguageListFromConfig());

        if (!codes.isEmpty()) {
            codes.remove(currentCode);
        }

        codes.add(0, currentCode);

        if (!codes.contains(MagicLanguageManager.DEFAULT_CODE)) {
            codes.add(MagicLanguageManager.DEFAULT_CODE);
        }

        Collections.reverse(codes);
        languageInfoList.clear();

        for (String code : codes) {
            LanguageInfo languageInfo = this.languages.getOrDefault(code, null);

            if (languageInfo != null) {
                //#if MC > 11903
                languageInfoList.add(code);
                //#else
                //$$ languageInfoList.add(languageInfo);
                //#endif
            }
        }

        if (languageInfoList.isEmpty()) {
            languageInfoList.add(
                    //#if MC > 12001
                    magiclib$DEFAULT_LANGUAGE
                    //#elseif MC > 11903
                    //$$ DEFAULT_LANGUAGE_CODE
                    //#else
                    //$$ DEFAULT_LANGUAGE
                    //#endif
            );
        }

        return languageInfoList;
    }
    //#else
    //$$ @ModifyVariable(
    //$$         method = "onResourceManagerReload",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lnet/minecraft/client/resources/language/Locale;loadFrom(Lnet/minecraft/server/packs/resources/ResourceManager;Ljava/util/List;)V",
    //$$                 ordinal = 0
    //$$         )
    //$$ )
    //$$ private List<String> addFallbackLanguage(List<String> languageCodeList) {
    //$$     ArrayList<String> codes = Lists.newArrayList();
    //$$
    //$$     if (!codes.isEmpty()) {
    //$$         codes.remove(currentCode);
    //$$     }
    //$$
    //$$     codes.add(0, currentCode);
    //$$
    //$$     if (!codes.contains(MagicLanguageManager.DEFAULT_CODE)) {
    //$$         codes.add(MagicLanguageManager.DEFAULT_CODE);
    //$$     }
    //$$
    //$$     return codes;
    //$$ }
    //#endif
}
