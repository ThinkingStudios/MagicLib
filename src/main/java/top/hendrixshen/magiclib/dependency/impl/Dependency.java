package top.hendrixshen.magiclib.dependency.impl;

import lombok.ToString;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.AnnotationNode;
import org.spongepowered.asm.util.Annotations;
import top.hendrixshen.magiclib.util.ForgeUtil;

@ToString
public class Dependency {
    public static final String SATISFIED = "Satisfied!";
    public final String modId;
    public final String versionPredicate;
    public final boolean optional;
    public final boolean satisfied;

    private Dependency(String modId, String versionPredicate, boolean optional) {
        this.modId = modId;
        this.versionPredicate = versionPredicate;
        this.optional = optional;
        this.satisfied = optional ?
                !ForgeUtil.isModLoaded(modId) || ForgeUtil.isModLoaded(modId, versionPredicate) :
                ForgeUtil.isModLoaded(modId, versionPredicate);
    }

    @Contract("_ -> new")
    public static @NotNull Dependency of(top.hendrixshen.magiclib.dependency.api.annotation.@NotNull Dependency dependency) {
        return new Dependency(dependency.value(), dependency.versionPredicate(), dependency.optional());
    }

    @Contract("_ -> new")
    public static @NotNull Dependency of(AnnotationNode dependencyNode) {
        return new Dependency(Annotations.getValue(dependencyNode, "value", top.hendrixshen.magiclib.dependency.api.annotation.Dependency.class),
                Annotations.getValue(dependencyNode, "versionPredicate", top.hendrixshen.magiclib.dependency.api.annotation.Dependency.class),
                Annotations.getValue(dependencyNode, "optional", top.hendrixshen.magiclib.dependency.api.annotation.Dependency.class)
        );
    }

    public String getCheckResult() {
        if (!this.satisfied) {
            if (ForgeUtil.isModLoaded(this.modId)) {
                return ModList.get().getModContainerById(this.modId).map(modContainer -> {
                    IModInfo metadata = modContainer.getModInfo();
                    String modName = metadata.getDisplayName();
                    String modVersion = metadata.getVersion().getQualifier();
                    return String.format("Mod %s (%s) detected. Requires [%s], but found %s!", modName, this.modId, this.versionPredicate, modVersion);
                }).orElse(String.format("Get %s data failed!", this.modId));
            } else {
                return String.format("Mod %s not found. Requires [%s]!", this.modId, this.versionPredicate);
            }
        } else {
            return SATISFIED;
        }
    }
}
