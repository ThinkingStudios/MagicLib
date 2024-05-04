package top.hendrixshen.magiclib.mixin.language;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.language.impl.MagicLanguageManager;

@OnlyIn(Dist.CLIENT)
@Mixin(value = Minecraft.class, priority = 1001)
public class MixinMinecraft {
    @Inject(
            //#if MC > 11404
            method = "<init>",
            //#else
            //$$ method = "init",
            //#endif
            at = @At(
                    value = "RETURN"
            )
    )
    //#if MC > 11404
    private void afterInit(GameConfig gameConfig, CallbackInfo ci) {
    //#else
    //$$ private void afterInit(CallbackInfo ci) {
    //#endif
        MagicLanguageManager.INSTANCE.initClient();
        Minecraft.getInstance().getLanguageManager().onResourceManagerReload(Minecraft.getInstance().getResourceManager());
    }
}
