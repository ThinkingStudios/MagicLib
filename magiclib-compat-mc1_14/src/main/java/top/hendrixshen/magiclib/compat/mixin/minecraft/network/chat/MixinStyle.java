package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import javax.annotation.Nullable;

@SuppressWarnings("ConstantConditions, unused")
@Mixin(Style.class)
public class MixinStyle {

    @Public
    @Remap("field_24360")
    private static final Style EMPTY = new Style();

    @Remap("method_30938")
    public Style withUnderlined(@Nullable Boolean boolean_) {
        return ((Style) (Object) this).copy().setUnderlined(boolean_);
    }

    @Remap("method_36140")
    public Style withStrikethrough(@Nullable Boolean boolean_) {
        return ((Style) (Object) this).copy().setStrikethrough(boolean_);
    }

    @Remap("method_36141")
    public Style withObfuscated(@Nullable Boolean boolean_) {
        return ((Style) (Object) this).copy().setObfuscated(boolean_);
    }

//    public Style withHoverEvent(@Nullable HoverEvent hoverEvent) {
//        return ((Style) (Object) this).copy().setHoverEvent(hoverEvent);
//    }
}