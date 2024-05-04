package top.hendrixshen.magiclib;

import lombok.Getter;
import net.minecraftforge.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.hendrixshen.magiclib.util.StringUtil;

public class MagicLibReference {
    @Getter
    private static final String modIdentifier = "@MOD_IDENTIFIER@";
    @Getter
    private static final String modIdentifierCurrent = "@MOD_IDENTIFIER@-@MINECRAFT_VERSION_IDENTIFY@";
    @Getter
    private static final String modName = "@MOD_NAME@";
    @Getter
    private static final String modNameCurrent = ModList.get().getModContainerById(modIdentifierCurrent).orElseThrow(RuntimeException::new).getModInfo().getDisplayName();
    @Getter
    private static final String modVersion = ModList.get().getModContainerById(modIdentifierCurrent).orElseThrow(RuntimeException::new).getModInfo().getVersion().getQualifier();
    @Getter
    private static final String modVersionType = StringUtil.getVersionType(modVersion);
    @Getter
    private static final Logger logger = LogManager.getLogger(modIdentifier);
}
