package org.opensearch.plugin.custom;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.*;
import org.apache.lucene.search.DoubleValuesSource;

import java.io.IOException;
import java.util.Objects;

public class CustomQuery extends Query {
    private final Query innerQuery;
    private final String priceField;

    private static final Logger logger = LogManager.getLogger(CustomQuery.class);


    public CustomQuery(Query innerQuery, String priceField) {
        this.innerQuery = innerQuery;
        this.priceField = priceField;
    }

        @Override
        public Weight createWeight(IndexSearcher searcher, ScoreMode scoreMode, float boost) throws IOException {
            Weight innerWeight = searcher.createWeight(innerQuery, scoreMode, boost);

            DoubleValuesSource priceSource = DoubleValuesSource.fromDoubleField("price");

            logger.info("Found price for doc {}: {}", priceField, priceSource);

            return new CustomWeight(innerWeight, priceField);
        }

    @Override
    public void visit(QueryVisitor visitor) {

    }

    @Override
    public String toString(String field) {
        return "CustomQuery(score based on field `" + priceField + "`)";
    }

    @Override
    public boolean equals(Object obj) {
        if (sameClassAs(obj) == false) return false;
        CustomQuery other = (CustomQuery) obj;
        return innerQuery.equals(other.innerQuery) && priceField.equals(other.priceField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(innerQuery, priceField);
    }
}
