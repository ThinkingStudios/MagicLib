package top.hendrixshen.magiclib.util;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ForgeUtil {

    /**
     * Verify that the ForgeModLoader/FancyModLoader (FML) has loaded the qualified mods.
     *
     * @param version          Version provided by the FML.
     * @param versionPredicate Semantic versioning expression.
     * @return True if the FML finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(ArtifactVersion version, String versionPredicate) {
        try {
            return VersionRange.createFromVersion(versionPredicate).containsVersion(version);

//            if (fabricLegacyVersionPredicateParser != null) {
//                try {
//                    return (boolean) fabricLegacyVersionPredicateParser.invoke(null, version, versionPredicate);
//                } catch (InvocationTargetException | IllegalAccessException e) {
//                    MagicLibReference.getLogger().error("Failed to invoke fabricLegacyVersionPredicateParser#matches", e);
//                    throw new RuntimeException(e);
//                }
//            } else {
//                return VersionRange.createFromVersion(versionPredicate).containsVersion(version);
//            }
        } catch (Throwable e) {
            MagicLibReference.getLogger().error("Failed to parse version or version predicate {} {}: {}", version.getQualifier(), versionPredicate, e);
            return false;
        }
    }

    /**
     * Verify that the ForgeModLoader/FancyModLoader (FML) has loaded the qualified mods.
     *
     * @param modId Version provided by the FML.
     * @return True if the FML finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    /**
     * Verify that the ForgeModLoader/FancyModLoader (FML) has loaded the qualified mod.
     *
     * @param modId            Version provided by the FML.
     * @param versionPredicate Semantic versioning expression.
     * @return True if the FML finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionPredicate) {
        Optional<? extends ModContainer> modContainerOptional = ModList.get().getModContainerById(modId);

        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return isModLoaded(modContainer.getModInfo().getVersion(), versionPredicate);
        }

        return false;
    }

    public static @NotNull Set<URL> getResources(String name) throws IOException {
        ClassLoader urlLoader = Thread.currentThread().getContextClassLoader();
        HashSet<URL> hashSet = new HashSet<>();
        Enumeration<URL> urlEnumeration = urlLoader.getResources(name);

        while (urlEnumeration.hasMoreElements()) {
            hashSet.add(urlEnumeration.nextElement());
        }

        return hashSet;
    }

    /**
     * @return True if started with ArchitecturyLoom / ForgeGradle / NeoGradle.
     */
    public static boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }
}
