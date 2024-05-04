package top.hendrixshen.magiclib.mixin.compat.minecraft.math;

import com.mojang.math.Quaternion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.math.QuaternionCompatApi;

//#if MC < 11500
//$$ import net.minecraft.util.Mth;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import top.hendrixshen.magiclib.util.MiscUtil;
//#endif

/**
 * The implementation for mc [1.14.4, ~)
 */
@OnlyIn(Dist.CLIENT)
@Mixin(Quaternion.class)
public abstract class MixinQuaternion implements QuaternionCompatApi {
    //#if MC > 11404
    @Shadow
    public abstract void mul(float f);

    @Shadow
    public abstract void normalize();

    @Shadow
    public abstract Quaternion copy();
    //#else
    //$$ @Final
    //$$ @Shadow
    //$$ private float[] values;
    //$$
    //$$ @Shadow
    //$$ public abstract float i();
    //$$
    //$$ @Shadow
    //$$ public abstract float j();
    //$$
    //$$ @Shadow
    //$$ public abstract float k();
    //$$
    //$$ @Shadow
    //$$ public abstract float r();
    //$$
    //#endif

    @Override
    public void mulCompat(float f) {
        //#if MC > 11404
        this.mul(f);
        //#else
        //$$ this.values[0] *= f;
        //$$ this.values[1] *= f;
        //$$ this.values[2] *= f;
        //$$ this.values[3] *= f;
        //#endif
    }

    @Override
    public void normalizeCompat() {
        //#if MC > 11404
        this.normalize();
        //#else
        //$$ float f = this.i() * this.i() + this.j() * this.j() + this.k() * this.k() + this.r() * this.r();
        //$$
        //$$ if (f > 1.0E-6F) {
        //$$     this.mulCompat((float) Mth.fastInvSqrt(f));
        //$$ } else {
        //$$     this.mulCompat(0);
        //$$ }
        //#endif
    }

    @Override
    public Quaternion copyCompat() {
        //#if MC > 11404
        return this.copy();
        //#else
        //$$ return new Quaternion((Quaternion) MiscUtil.cast(this));
        //#endif
    }
}