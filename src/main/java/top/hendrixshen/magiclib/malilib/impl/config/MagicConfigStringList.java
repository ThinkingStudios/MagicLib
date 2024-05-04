package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.malilib.api.config.IMagicConfigBase;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class MagicConfigStringList extends ConfigStringList implements IMagicConfigBase {
    private final String prefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public MagicConfigStringList(String prefix, String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
        this.prefix = prefix;
    }

    @Override
    public String getConfigGuiDisplayName() {
        return IMagicConfigBase.super.getConfigGuiDisplayName();
    }

    @Override
    public void setValueFromJsonElement(JsonElement jsonElement) {
        super.setValueFromJsonElement(jsonElement);

        if (this.valueChangedFromJsonCallback != null) {
            this.valueChangedFromJsonCallback.accept(this);
        }
    }

    @Override
    @Nullable
    public Consumer<ConfigBase<?>> getValueChangedFromJsonCallback() {
        return this.valueChangedFromJsonCallback;
    }

    @Override
    public void setValueChangedFromJsonCallback(@Nullable Consumer<ConfigBase<?>> valueChangedFromJsonCallback) {
        this.valueChangedFromJsonCallback = valueChangedFromJsonCallback;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }
}
