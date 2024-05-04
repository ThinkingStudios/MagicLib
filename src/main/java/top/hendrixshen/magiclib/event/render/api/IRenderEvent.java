package top.hendrixshen.magiclib.event.render.api;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
interface IRenderEvent<T> {
    default Supplier<String> getProfilerSectionSupplier() {
        return () -> this.getClass().getName();
    }

    void render(T obj, RenderContext context, float tickDelta);
}
