package top.hendrixshen.magiclib.malilib.api.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus;

@Deprecated()
@ApiStatus.ScheduledForRemoval(inVersion = "0.8")
@OnlyIn(Dist.CLIENT)
public interface TranslatableConfig extends IMagicConfigBase {
}
