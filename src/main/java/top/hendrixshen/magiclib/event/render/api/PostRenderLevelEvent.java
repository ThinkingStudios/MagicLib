package top.hendrixshen.magiclib.event.render.api;

import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface PostRenderLevelEvent extends IRenderEvent<Level> {
    @Override
    void render(Level level, RenderContext context, float tickDelta);
}
