package top.hendrixshen.magiclib.mixin.compat.minecraft.math;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;

@OnlyIn(Dist.CLIENT)
@Mixin(DummyClass.class)
public class MixinQuaternion {
}