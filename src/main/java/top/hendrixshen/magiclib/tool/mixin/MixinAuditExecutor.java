package top.hendrixshen.magiclib.tool.mixin;

import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.util.ForgeUtil;
import top.hendrixshen.magiclib.util.MixinUtil;

public class MixinAuditExecutor {
    private static final String KEYWORD_PROPERTY = String.format("%s.mixin_audit", MagicLibReference.getModIdentifier());

    public static void execute() {
        if (ForgeUtil.isDevelopmentEnvironment() &&
                "true".equals(System.getProperty(MixinAuditExecutor.KEYWORD_PROPERTY))) {
            MagicLibReference.getLogger().info("Triggered mixin audit.");
            MixinUtil.audit();
            System.exit(0);
        }
    }
}
