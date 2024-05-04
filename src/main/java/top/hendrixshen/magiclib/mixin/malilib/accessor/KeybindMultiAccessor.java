package top.hendrixshen.magiclib.mixin.malilib.accessor;

import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@OnlyIn(Dist.CLIENT)
@Dependencies(and = @Dependency("mafglib"))
@Mixin(value = KeybindMulti.class, remap = false)
public interface KeybindMultiAccessor {
    @Accessor()
    IHotkeyCallback getCallback();
}
