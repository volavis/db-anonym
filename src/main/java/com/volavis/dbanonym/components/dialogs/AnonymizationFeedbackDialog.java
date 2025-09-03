package com.volavis.dbanonym.components.dialogs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.database.SimpleAttribute;

import java.util.List;

/**
 * Opened after the anonymization. Tells the user that the anonymization has been successful or shows a list of failed attributes.
 */
@CssImport("./styles/components/dialogs/anonymization-feedback-dialog.css")
public class AnonymizationFeedbackDialog extends Composite<Dialog> {

    private final Paragraph title = new Paragraph();
    private final Span text = new Span();
    private final Grid<SimpleAttribute> grid = new Grid<>();
    private final SimpleInformationDialog simpleInformationDialog = new SimpleInformationDialog();
    private final Button closeButton = new Button();

    /**
     * Constructor.
     */
    public AnonymizationFeedbackDialog() {
        getContent().setWidth("800px");

        title.setId("anonymization-dialog-title");

        createGrid();

        simpleInformationDialog.getCloseButton().addClickListener(event -> simpleInformationDialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        closeButton.setText(StringConstants.OKAY);
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(closeButton);

        getContent().add(title, text, grid, buttonLayout);
    }

    /**
     * Creates a grid showing the attributes whose anonymization failed.
     */
    private void createGrid() {

        grid.setVisible(false);
        grid.setId("anonymization-dialog-grid");
        grid.setHeight("250px");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        String htmlHeader = "<div style='font-weight: bold;'>%s</div>";
        grid.addColumn(SimpleAttribute::getEntityName)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.TABLE)))
                .setSortable(true);
        grid.addColumn(SimpleAttribute::getAttributeName)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.ATTRIBUTE)))
                .setSortable(true);
        grid.addColumn(SimpleAttribute::getOption)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.ANONYMIZATION_OPTION)))
                .setSortable(true);
        grid.addComponentColumn(this::buildExceptionDialog)
                .setFlexGrow(0).setWidth("45px");
    }

    /**
     * Builds the button that opens the exception dialog.
     * @param simpleAttribute Attribute of current row.
     */
    private HorizontalLayout buildExceptionDialog(SimpleAttribute simpleAttribute) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        Button button = new Button(new Icon(VaadinIcon.WARNING));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> {
            simpleInformationDialog.setTitleAndText(StringConstants.ANONYMIZATION_FEEDBACK_DIALOG_EXCEPTION_TITLE,
                    simpleAttribute.getCause().toString());
            simpleInformationDialog.open();
        });
        layout.add(button);
        return layout;
    }

    /**
     * Displays a different title and text depending on the list of failed attribute anonymizations.
     * <P>Success message if the list is empty. Otherwise shows the grid.
     * @param list Attributes whose anonymization failed.
     */
    public void setState(List<SimpleAttribute> list) {
        if(list == null || list.isEmpty()) {
            grid.setVisible(false);
            title.setText(StringConstants.ANONYMIZATION_FEEDBACK_DIALOG_SUCCESS_TITLE);
            text.setText(StringConstants.ANONYMIZATION_FEEDBACK_DIALOG_SUCCESS_TEXT);
        } else {
            grid.setVisible(true);
            grid.setItems(list);
            title.setText(StringConstants.ANONYMIZATION_FEEDBACK_DIALOG_ERROR_TITLE);
            text.setText(StringConstants.ANONYMIZATION_FEEDBACK_DIALOG_ERROR_TEXT);
        }
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