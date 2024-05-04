package top.hendrixshen.magiclib.compat.minecraft.api.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@OnlyIn(Dist.CLIENT)
public interface BufferBuilderCompatApi {
    default BufferBuilder vertexCompat(Matrix4f matrix4f, float x, float y, float z) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default BufferBuilder vertex(Matrix4f matrix4f, float x, float y, float z) {
    //$$     return this.vertexCompat(matrix4f, x, y, z);
    //$$ }
    //#endif
}
