
package com.asoroka.sidora.statistics;

import static com.google.common.collect.Iterators.advance;
import static com.google.common.collect.Iterators.cycle;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;

import com.asoroka.sidora.statistics.heuristics.TypeDeterminationHeuristic;
import com.google.common.collect.AbstractIterator;

/**
 * The heart of workflow. Handed a {@link CSVParser}, this class will scan through it and supply the values of fields
 * to a "row" of {@link TypeDeterminationHeuristic} strategies cloned from the configured choice.
 * 
 * @author ajs6f
 */
public class CsvScanner extends AbstractIterator<CSVRecord> {

    private final Iterator<CSVRecord> internalScanner;

    private final Iterator<TypeDeterminationHeuristic<?>> strategies;

    private final List<TypeDeterminationHeuristic<?>> rowOfStrategies;

    private static final Logger log = getLogger(CsvScanner.class);

    @Inject
    public CsvScanner(final CSVParser parser, final TypeDeterminationHeuristic<?> strategy) {
        super();
        this.internalScanner = parser.iterator();
        final int numColumns = parser.getHeaderMap().size();
        log.debug("Found {} columns in our CSV.", numColumns);
        // a "row" of strategy instances of the same length as the rows in our CSV
        this.rowOfStrategies = newArrayListWithCapacity(numColumns);
        for (int i = 0; i < numColumns; i++) {
            rowOfStrategies.add(strategy.clone());
        }
        // this.strategies will cycle endlessly around our row
        this.strategies = cycle(rowOfStrategies);
    }

    @Override
    protected CSVRecord computeNext() {
        if (internalScanner.hasNext()) {
            final CSVRecord nextRecord = internalScanner.next();
            for (final String value : nextRecord) {
                strategies.next().addValue(value);
            }
            return nextRecord;
        }
        return endOfData();
    }

    /**
     * Scan rows in our CSV up to a limit.
     * 
     * @param limit The number of rows to scan
     */
    public void scan(final int... limit) {
        if (limit.length == 0) {
            while (hasNext()) {
                next();
            }
        } else {
            advance(this, limit[0]);
        }
    }

    /**
     * Use this to recover and interrogate the strategy instances used in scanning.
     * 
     * @return the row of strategies used to determine the types of fields
     */
    public List<TypeDeterminationHeuristic<?>> getStrategies() {
        return rowOfStrategies;
    }

}
