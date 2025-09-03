package com.volavis.dbanonym.components.dialogs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.volavis.dbanonym.StringConstants;

/**
 * Dialog that displays the progress of the anonymization. Shows a progressbar.
 */
@CssImport("./styles/components/dialogs/progress-bar-dialog.css")
public class ProgressBarDialog extends Composite<Dialog> {

    private final Span attributeProgress = new Span();
    private final ProgressBar progressBar = new ProgressBar();

    /**
     * Constructor.
     */
    public ProgressBarDialog() {
        getContent().setCloseOnEsc(false);
        getContent().setCloseOnOutsideClick(false);
        getContent().setWidth("800px");

        Paragraph title = new Paragraph(StringConstants.PROGRESSBAR_DIALOG_TITLE);
        title.setId("dialog-title");

        HorizontalLayout textLayout = new HorizontalLayout();
        Span text = new Span(StringConstants.PROGRESSBAR_DIALOG_TEXT);
        textLayout.add(text, attributeProgress);

        getContent().add(title, textLayout, progressBar);
    }

    /**
     * Sets the minimum and maximum bounds of the progress bar.
     * @param min Minimum bound. Probably 0!
     * @param attributeCount Maximum bound. Equals the number of attributes that will be anonymized.
     */
    public void setProgressBarBounds(double min, double attributeCount) {
        progressBar.setMin(min);
        progressBar.setMax(attributeCount);
        progressBar.setValue(min);

        setAttributeProgress(min, attributeCount);
    }

    /**
     * Updates the progress bar (+1). Called after an attribute has been anonymized.
     */
    public void updateProgressBar() {
        double currentValue = progressBar.getValue();
        double nextValue = currentValue + 1;

        if(nextValue <= progressBar.getMax()) {
            progressBar.setValue(nextValue);
            setAttributeProgress(nextValue, progressBar.getMax());
        }
    }

    /**
     * Updates the already anonymized attributes count.
     * @param current Count of already anonymized attributes.
     * @param max Max attribute count.
     */
    private void setAttributeProgress(double current, double max) {
        String progressString = "[" + StringConstants.ATTRIBUTES + " %d/%d]";
        attributeProgress.setText(String.format(progressString, (int) current, (int) max));
    }

    /**
     * Opens the dialog.
     */
    public void open() {
        getContent().open();
    }

    /**
     * Closes the dialog.
     */
    public void close() {
        getContent().close();
    }
}