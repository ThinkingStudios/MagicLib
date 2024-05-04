package top.hendrixshen.magiclib.mixin.compat.minecraft.client;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.client.MinecraftCompatApi;

//#if MC < 11500
//$$ import org.spongepowered.asm.mixin.Final;
//#endif

@OnlyIn(Dist.CLIENT)
@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements MinecraftCompatApi {
    //#if MC > 11404
    @Shadow
    public abstract Window getWindow();
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ public Window window;
    //#endif

    @Override
    public Window getWindowCompat() {
        //#if MC > 11404
        return this.getWindow();
        //#else
        //$$ return this.window;
        //#endif
    }
}
