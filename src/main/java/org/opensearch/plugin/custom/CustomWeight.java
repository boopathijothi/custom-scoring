package org.opensearch.plugin.custom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.SortedNumericDocValues;
import org.apache.lucene.search.*;
import org.opensearch.index.fielddata.SortedNumericDoubleValues;


import java.io.IOException;

public class CustomWeight extends Weight {
    private final Weight innerWeight;
    private final String priceField;
    private static final Logger logger = LogManager.getLogger(CustomWeight.class);

    public CustomWeight(Weight innerWeight, String priceField) {
        super(innerWeight.getQuery());
        this.innerWeight = innerWeight;
        this.priceField = priceField;
        logger.info("inner query {}" ,innerWeight.getQuery() );

    }

    @Override
    public Scorer scorer(LeafReaderContext context) throws IOException {
        Scorer innerScorer = innerWeight.scorer(context);

        logger.info("inner scorer {}",innerScorer );
        if (innerScorer == null) return null;

        logger.info("LeafReaderContext :{} ",context );

        SortedNumericDocValues priceValues = context.reader().getSortedNumericDocValues(priceField);

        logger.info("price in weight {}", priceValues);

        return new FilterScorer(innerScorer) {
            @Override
            public float getMaxScore(int upTo) throws IOException {
                return 0;
            }

            final DocIdSetIterator it = innerScorer.iterator();

            @Override
            public float score() throws IOException {
                int docId = docID();
                logger.info("docID {}", docId);

                // Log the document ID being processed
                assert priceValues != null;
                double price = 1.0;
                if (priceValues.advanceExact(docId)) {
                    price = priceValues.nextValue();
                    float boost = (float)(1.0 / (1.0 + price));
                    logger.info("docID {} modified score is {}", docId, (innerScorer.score() * boost));

                    return (innerScorer.score() * boost);
                } else {
                    return innerScorer.score();
                }
            }
        };
    }

    @Override
    public Explanation explain(LeafReaderContext context, int doc) throws IOException {
        Explanation inner = innerWeight.explain(context, doc);
        SortedNumericDocValues priceValues = context.reader().getSortedNumericDocValues(priceField);

        if (priceValues.advanceExact(doc)) {
            double price = priceValues.nextValue();
            float boost = (float)(1.0 / (1.0 + price));
            return Explanation.match(
                    inner.getValue().floatValue() * boost,
                    "score * dynamic price boost (1 / (1 + price=" + price + "))",
                    inner
            );
        } else {
            return inner;
        }
    }


    @Override
    public boolean isCacheable(LeafReaderContext ctx) {
        return false;
    }
}
