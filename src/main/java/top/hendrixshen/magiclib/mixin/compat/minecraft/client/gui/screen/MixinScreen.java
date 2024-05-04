package top.hendrixshen.magiclib.mixin.compat.minecraft.client.gui.screen;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.client.gui.screens.ScreenCompatApi;

//#if MC < 11700
//$$ import net.minecraft.client.gui.components.AbstractWidget;
//$$ import org.spongepowered.asm.mixin.Final;
//$$
//$$ import java.util.List;
//#endif

@OnlyIn(Dist.CLIENT)
@Mixin(Screen.class)
public abstract class MixinScreen implements ScreenCompatApi {
    //#if MC > 11605
    @SuppressWarnings("target")
    @Shadow
    protected abstract GuiEventListener addRenderableWidget(GuiEventListener guiEventListener);

    @Shadow
    protected abstract Renderable addRenderableOnly(Renderable widget);
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ protected List<AbstractWidget> buttons;
    //$$
    //$$
    //$$ @Shadow
    //$$ protected abstract AbstractWidget addButton(AbstractWidget abstractWidget);
    //#endif

    @Override
    public GuiEventListener addRenderableWidgetCompat(GuiEventListener guiEventListener) {
        //#if MC > 11605
        return this.addRenderableWidget(guiEventListener);
        //#else
        //$$ this.addButton((AbstractWidget) guiEventListener);
        //$$ return guiEventListener;
        //#endif
    }

    @Override
    public Renderable addRenderableOnlyCompat(Renderable widget) {
        //#if MC > 11605
        return this.addRenderableOnly(widget);
        //#else
        //$$ this.buttons.add((AbstractWidget) widget);
        //$$ return widget;
        //#endif
    }

    //#if MC > 11502
    @SuppressWarnings("target")
    @Shadow
    protected abstract GuiEventListener addWidget(GuiEventListener guiEventListener);
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ protected List<GuiEventListener> children;
    //#endif

    @Override
    public GuiEventListener addWidgetCompat(GuiEventListener guiEventListener) {
        //#if MC > 11502
        return this.addWidget(guiEventListener);
        //#else
        //$$ this.children.add(guiEventListener);
        //$$ return guiEventListener;
        //#endif
    }
}
