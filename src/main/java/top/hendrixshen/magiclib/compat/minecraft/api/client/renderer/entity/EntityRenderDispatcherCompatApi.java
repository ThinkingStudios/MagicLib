package top.hendrixshen.magiclib.compat.minecraft.api.client.renderer.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@OnlyIn(Dist.CLIENT)
public interface EntityRenderDispatcherCompatApi {
    default double distanceToSqrCompat(Entity entity) {
        throw new UnImplCompatApiException();
    }

    default Quaternionf cameraOrientationCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default double distanceToSqr(Entity entity) {
    //$$     return this.distanceToSqrCompat(entity);
    //$$ }
    //$$
    //$$ default Quaternion cameraOrientation() {
    //$$     return this.cameraOrientationCompat();
    //$$ }
    //#endif
}