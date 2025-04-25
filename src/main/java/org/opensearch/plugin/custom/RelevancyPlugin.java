/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.plugin.custom;

import org.opensearch.plugins.Plugin;


import org.opensearch.plugins.SearchPlugin;

import java.util.List;
import java.util.Collections;

public class RelevancyPlugin extends Plugin implements SearchPlugin {


    @Override
    public List<QuerySpec<?>> getQueries() {
        return Collections.singletonList(
                new QuerySpec<>(
                        "custom_match",
                        CustomQueryBuilder::new,
                        CustomQueryBuilder::fromXContent
                )
        );
    }


}
