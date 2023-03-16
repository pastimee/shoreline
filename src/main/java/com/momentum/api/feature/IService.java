package com.momentum.api.feature;

import java.util.List;

/**
 * IService providing feature
 * @author linus
 * @since 02/06/2023
 */
public interface IService<T> {

    /**
     * Provides the service
     *
     * @param in The input
     */
    void provide(T in);
}
