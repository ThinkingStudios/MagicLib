package top.hendrixshen.magiclib.compat.minecraft.api.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@OnlyIn(Dist.CLIENT)
public interface CameraCompatApi {
    default Quaternionf rotationCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default Quaternion rotation() {
    //$$     return this.rotationCompat();
    //$$ }
    //#endif
}
