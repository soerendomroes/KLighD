/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2015 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd.ui.printing;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import org.eclipse.jface.preference.IPreferenceStore;

import de.cau.cs.kieler.klighd.ui.KlighdUIPlugin;

/**
 * Specialization of {@link PrintOptions} dedicated to diagram printouts.
 * See {@link PrintOptions} for details on this separation.
 * 
 * @author chsch
 */
public class DiagramPrintOptions extends PrintOptions {

    private static final IPreferenceStore PREF_STORE = KlighdUIPlugin.getDefault().getPreferenceStore();

    /** Id of the preference SHOW_PREVIEW. */
    private static final String PREFERENCE_INITIALLY_SHOW_PREVIEW =
            "klighd.printing.initiallyShowPreview";

    /**
     * Preference initializer making sure the required data contain valid values.
     *
     * @author chsch
     */
    public static class Initializer extends PrintOptions.Initializer {

        /**
         * {@inheritDoc}
         */
        @Override
        protected IPreferenceStore getPreferenceStore() {
            return PREF_STORE;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void initializeDefaultPreferences() {
            super.initializeDefaultPreferences();
            PREF_STORE.setDefault(PREFERENCE_INITIALLY_SHOW_PREVIEW, false);
        }
    }

    /**
     * Convenience getter.
     *
     * @return {@code true} the print preview is to be shown while opening the print dialog,
     *         {@code false} otherwise.
     */
    public static boolean getInitiallyShowPreview() {
        return PREF_STORE.getBoolean(PREFERENCE_INITIALLY_SHOW_PREVIEW);
    }

    /**
     * Convenience setter.
     *
     * @param initiallyShow
     *            {@code true} the print preview is to be shown while opening the print dialog,
     *            {@code false} otherwise.
     */
    public static void setInitiallyShowPreview(final boolean initiallyShow) {
        PREF_STORE.setValue(PREFERENCE_INITIALLY_SHOW_PREVIEW, initiallyShow);
    }
    
    private PrintExporter exporter;
    private Dimension2D diagramBounds = null;

    /**
     * Constructor.
     *
     * @param printExporter
     *            the {@link PrintExporter} being employed shall be hooked for easy access while,
     *            e.g., computing the "Fit to pages" scale
     */
    public DiagramPrintOptions(final PrintExporter printExporter) {
        super(PREF_STORE);
        this.exporter = printExporter;
    }

    /**
     * Gets the exporter to use when printing or showing the preview.
     *
     * @return the exporter
     */
    public PrintExporter getExporter() {
        return exporter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void resetTrimData() {
        // this method is already called while invoking the super(...) constructor,
        //  and while the exporter field is not yet initialized; so ...
        if (exporter != null) {
            exporter.resetTrimInformation();
        } 
    }

    @Override
    protected Point2D updateCenteringOffset() {
        if (!getHorizontallyCentered() && !getVerticallyCentered()) {
            return new Point2D.Double();
        }

        final Dimension2D pBounds = exporter.getTrimmedTileBounds(this);

        if (pBounds == null) {
            return null;
        }

        if (diagramBounds == null) {
            if (exporter == null) {
                // in this case we cannot compute the centering offset, should not happen
                return null;
            }
            diagramBounds = exporter.getDiagramBoundsIncludingTrim();
        }

        return new Point2D.Double(
            (pBounds.getWidth() * getPagesWide() - diagramBounds.getWidth() * getScaleFactor()) / 2,
            (pBounds.getHeight() * getPagesTall() - diagramBounds.getHeight() * getScaleFactor()) / 2);
    }
}