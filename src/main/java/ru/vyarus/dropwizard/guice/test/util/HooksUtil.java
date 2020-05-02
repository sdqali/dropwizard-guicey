package ru.vyarus.dropwizard.guice.test.util;

import ru.vyarus.dropwizard.guice.hook.GuiceyConfigurationHook;

import java.util.ArrayList;
import java.util.List;

/**
 * Guice hooks test utilities.
 *
 * @author Vyacheslav Rusakov
 * @since 02.05.2020
 */
public final class HooksUtil {

    private HooksUtil() {
    }

    /**
     * Instantiates provided hooks.
     *
     * @param hooks hooks to instantiate
     * @return hooks instances
     */
    @SafeVarargs
    public static List<GuiceyConfigurationHook> create(final Class<? extends GuiceyConfigurationHook>... hooks) {
        final List<GuiceyConfigurationHook> res = new ArrayList<>();
        for (Class<? extends GuiceyConfigurationHook> hook : hooks) {
            try {
                res.add(hook.newInstance());
            } catch (Exception e) {
                throw new IllegalStateException("Failed to instantiate guicey hook: " + hook.getSimpleName(), e);
            }
        }
        return res;
    }

    /**
     * Register configuration hooks.
     *
     * @param hooks hooks to register
     */
    public static void register(final List<GuiceyConfigurationHook> hooks) {
        if (hooks != null) {
            for (GuiceyConfigurationHook hook : hooks) {
                hook.register();
            }
        }
    }
}
