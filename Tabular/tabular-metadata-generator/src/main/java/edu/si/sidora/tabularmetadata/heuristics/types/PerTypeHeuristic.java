/*
 * Copyright 2015-2016 Smithsonian Institution.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.You may obtain a copy of
 * the License at: http://www.apache.org/licenses/
 *
 * This software and accompanying documentation is supplied without
 * warranty of any kind. The copyright holder and the Smithsonian Institution:
 * (1) expressly disclaim any warranties, express or implied, including but not
 * limited to any implied warranties of merchantability, fitness for a
 * particular purpose, title or non-infringement; (2) do not assume any legal
 * liability or responsibility for the accuracy, completeness, or usefulness of
 * the software; (3) do not represent that use of the software would not
 * infringe privately owned rights; (4) do not warrant that the software
 * is error-free or will be maintained, supported, updated or enhanced;
 * (5) will not be liable for any indirect, incidental, consequential special
 * or punitive damages of any kind or nature, including but not limited to lost
 * profits or loss of data, on any basis arising from contract, tort or
 * otherwise, even if any of the parties has been warned of the possibility of
 * such loss or damage.
 *
 * This distribution includes several third-party libraries, each with their own
 * license terms. For a complete copy of all copyright and license terms, including
 * those of third-party libraries, please see the product release notes.
 */

package edu.si.sidora.tabularmetadata.heuristics.types;

import static edu.si.sidora.tabularmetadata.datatype.DataType.String;
import static edu.si.sidora.tabularmetadata.datatype.DataType.datatypes;
import static edu.si.sidora.tabularmetadata.datatype.DataType.orderingByHierarchy;

import edu.si.sidora.tabularmetadata.datatype.DataType;

/**
 * A {@link TypeDeterminingHeuristic} that uses some test that maps types to boolean acceptance values. The test is
 * created by overriding {@link #candidacy(DataType)}. Types are passed or rejected without any particular order other
 * than specificity of type within the hierarchy.
 * 
 * @author A. Soroka
 * @param <SelfType>
 */
public abstract class PerTypeHeuristic<SelfType extends PerTypeHeuristic<SelfType>>
		extends TypeCountAggregatingHeuristic<SelfType> {

	@Override
	public DataType results() {
		return datatypes().stream().filter(this::candidacy).min(orderingByHierarchy).orElse(String);
	}

	/**
	 * Subclasses must override this method with an algorithm that uses the gathered statistics (and possibly other
	 * information) to make a determination about the most likely type of the proffered values.
	 * 
	 * @return Whether this type should be considered as a candidate for selection.
	 */
	protected abstract boolean candidacy(final DataType type);
}
