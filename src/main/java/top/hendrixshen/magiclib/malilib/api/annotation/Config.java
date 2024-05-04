package top.hendrixshen.magiclib.malilib.api.annotation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.hendrixshen.magiclib.dependency.api.ConfigDependencyPredicate;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.impl.ConfigDependencyPredicates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Configuration annotations.
 */
@OnlyIn(Dist.CLIENT)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    /**
     * The category to which the configuration belongs, if using the GUI provided by MagicLib,
     * will be used as the basis for tab assignment.
     *
     * @return Configuration category.
     */
    String category();

    /**
     * The dependency check used for this configuration will be used as a prerequisite for whether
     * the configuration is displayed when using the GUI provided by MagicLib.
     *
     * @return Configuration dependencies.
     */
    Dependencies dependencies() default @Dependencies;

    /**
     * The custom predicate used for this configuration will be used as a prerequisite for whether
     * the configuration is displayed when using the GUI provided by MagicLib.
     *
     * @return OptionDependencyPredicate for this configuration.
     */
    Class<? extends ConfigDependencyPredicate> predicate() default ConfigDependencyPredicates.TrueConfigPredicate.class;
}
