package com.volavis.dbanonym.components.dialogs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.volavis.dbanonym.StringConstants;

/**
 * Dialog that displays some text and has a close button.
 */
@CssImport("./styles/components/dialogs/simple-information-dialog.css")
public class SimpleInformationDialog extends Composite<Dialog> {

    private final Paragraph titleParagraph = new Paragraph();
    private final Span textSpan = new Span();
    private final Button closeButton = new Button();

    /**
     * Constructor.
     */
    public SimpleInformationDialog() {
        getContent().setWidth("800px");

        titleParagraph.setId("dialog-title");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        closeButton.setText(StringConstants.CLOSE);
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(closeButton);

        getContent().add(titleParagraph, textSpan, buttonLayout);
    }

    /**
     * Updates the dialog's title and text.
     * @param titleStr Dialog title.
     * @param textStr Dialog text content.
     */
    public void setTitleAndText(String titleStr, String textStr) {
        titleParagraph.setText(titleStr);
        textSpan.setText(textStr);
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

    public Button getCloseButton() {
        return closeButton;
    }
}