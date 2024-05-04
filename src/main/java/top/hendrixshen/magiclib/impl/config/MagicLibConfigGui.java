package top.hendrixshen.magiclib.impl.config;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.gui.ConfigGui;
import top.hendrixshen.magiclib.util.StringUtil;

@ApiStatus.Internal
@OnlyIn(Dist.CLIENT)
public class MagicLibConfigGui extends ConfigGui {
    private static MagicLibConfigGui INSTANCE;

    private MagicLibConfigGui(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager, () -> StringUtil.tr("gui.title", MagicLibReference.getModVersion(),
                StringUtil.trVersionType(MagicLibReference.getModVersion())));
    }

    protected static MagicLibConfigGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MagicLibConfigGui(MagicLibReference.getModIdentifier(),
                    MagicLibConfigs.ConfigCategory.GENERIC, ConfigManager.get(MagicLibReference.getModIdentifier()));
        }
        return INSTANCE;
    }
}