/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.plugin.custom;

import org.apache.lucene.search.Query;
import org.opensearch.core.common.io.stream.StreamInput;
import org.opensearch.core.common.io.stream.StreamOutput;
import org.opensearch.core.xcontent.XContentBuilder;
import org.opensearch.core.xcontent.XContentParser;
import org.opensearch.index.query.AbstractQueryBuilder;
import org.opensearch.index.query.QueryBuilder;
import org.opensearch.index.query.QueryShardContext;

import java.io.IOException;
import java.util.Objects;

import static java.lang.System.out;

public class CustomQueryBuilder extends AbstractQueryBuilder<CustomQueryBuilder> {
    private static final String NAME = "custom_match";

    private final QueryBuilder innerQuery;
    private static String priceField;

    public CustomQueryBuilder(QueryBuilder innerQuery, String priceField) {
        this.innerQuery = innerQuery;
        this.priceField = priceField;
    }

    public CustomQueryBuilder(StreamInput in) throws IOException {
        super(in);
        this.innerQuery = in.readNamedWriteable(QueryBuilder.class);
        this.priceField = in.readString();
    }

    @Override
    protected void doWriteTo(StreamOutput streamOutput) throws IOException {
        //out.writeNamedWriteable(innerQuery);

    }

    @Override
    protected Query doToQuery(QueryShardContext context) throws IOException {
        Query luceneQuery = innerQuery.toQuery(context);
        return new CustomQuery(luceneQuery, priceField);
    }

    @Override
    protected boolean doEquals(CustomQueryBuilder other) {
        return Objects.equals(innerQuery, other.innerQuery) && priceField == other.priceField;
    }

    @Override
    protected int doHashCode() {
        return Objects.hash(innerQuery, priceField);
    }

    @Override
    protected void doXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject(NAME);
        builder.field("price", priceField);
        builder.field("query");
        innerQuery.toXContent(builder, params);
        builder.endObject();
    }

    public static CustomQueryBuilder fromXContent(XContentParser parser) throws IOException {
        QueryBuilder innerQuery = null;

        while (parser.nextToken() != XContentParser.Token.END_OBJECT) {
            String fieldName = parser.currentName();
            parser.nextToken();

            if ("query".equals(fieldName)) {
                innerQuery = parseInnerQueryBuilder(parser);
            } else if ("price_field".equals(fieldName)) {
                priceField = parser.text();
            }
        }

        return new CustomQueryBuilder(innerQuery, priceField);
    }

    @Override
    public String getWriteableName() {
        return NAME;
    }
}

