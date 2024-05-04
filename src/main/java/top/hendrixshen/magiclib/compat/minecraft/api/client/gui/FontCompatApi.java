package top.hendrixshen.magiclib.compat.minecraft.api.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@OnlyIn(Dist.CLIENT)
public interface FontCompatApi {
    default int widthCompat(Component component) {
        throw new UnImplCompatApiException();
    }

    default int drawInBatch(String text, float x, float y, int color, boolean shadow, Matrix4f matrix4f, boolean seeThrough, int backgroundColor, int light) {
        throw new UnImplCompatApiException();
    }

    default int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f, boolean seeThrough, int backgroundColor, int light) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11502
    //$$ default int width(Component component) {
    //$$     return this.widthCompat(component);
    //$$ }
    //#endif
}
