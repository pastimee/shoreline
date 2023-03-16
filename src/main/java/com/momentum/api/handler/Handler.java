package com.momentum.api.handler;

import com.momentum.api.registry.ILabel;
import com.momentum.api.util.Wrapper;

/**
 * Similar to always enabled modules, run in the background,
 * provide important client functions
 *
 * @author linus
 * @since 03/01/2023
 */
@SuppressWarnings("rawtypes")
public class Handler implements Wrapper, ILabel {

    /**
     * Gets the label
     *
     * @return The label
     */
    @Override
    public String getLabel() {

        // class name with identifier
        String clazz = getClass().getSimpleName().toLowerCase();

        // class name
        clazz = clazz.substring(0, clazz.length() - 7);

        // create label
        return clazz + "_handler";
    }
}
