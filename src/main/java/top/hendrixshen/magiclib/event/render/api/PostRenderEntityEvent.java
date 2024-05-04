package top.hendrixshen.magiclib.event.render.api;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface PostRenderEntityEvent extends IRenderEvent<Entity> {
    @Override
    void render(Entity entity, RenderContext context, float tickDelta);
}
