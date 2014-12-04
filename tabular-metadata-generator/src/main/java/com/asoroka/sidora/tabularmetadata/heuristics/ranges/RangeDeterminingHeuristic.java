
package com.asoroka.sidora.tabularmetadata.heuristics.ranges;

import java.util.Map;

import com.asoroka.sidora.tabularmetadata.datatype.DataType;
import com.asoroka.sidora.tabularmetadata.heuristics.Heuristic;
import com.google.common.collect.Range;

/**
 * Determines ranges in different {@link DataTypes} for the supplied values.
 * 
 * @author ajs6f
 */
public interface RangeDeterminingHeuristic<SelfType extends RangeDeterminingHeuristic<SelfType>> extends
        Heuristic<SelfType, Map<DataType, Range<?>>> {
    // EMPTY
}
