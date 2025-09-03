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
 * Opened if the user wants to change the database connection (user is already connected!).
 * <P>Warns the user that he will loose the settings he has made.
 */
@CssImport("./styles/components/dialogs/connect-dialog.css")
public class ConnectDialog extends Composite<Dialog> {

    private final Button cancelButton = new Button();
    private final Button confirmButton = new Button();

    /**
     * Constructor.
     */
    public ConnectDialog() {
        getContent().setWidth("800px");

        Paragraph title = new Paragraph(StringConstants.CONNECT_DIALOG_TITLE);
        title.setId("dialog-title");
        Span text = new Span(StringConstants.CONNECT_DIALOG_TEXT);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setId("button-layout");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        cancelButton.setText(StringConstants.CANCEL);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        confirmButton.setText(StringConstants.OKAY);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);
        buttonLayout.add(cancelButton, confirmButton);

        getContent().add(title, text, buttonLayout);
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

    public Button getCancelButton() {
        return cancelButton;
    }

    public Button getConfirmButton() {
        return confirmButton;
    }
}