/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2017 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package de.cau.cs.kieler.klighd.labels.management;

import org.eclipse.elk.graph.ElkLabel;

/**
 * Label manager that sets the text of unfocussed labels to the empty string, effectively hiding
 * them.
 * 
 * @author cds
 */
public class HidingLabelManager extends AbstractKlighdLabelManager {

    @Override
    protected Result doResizeLabel(final ElkLabel label, final double targetWidth) {
        return Result.modified("");
    }

}
