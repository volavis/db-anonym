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
import com.volavis.dbanonym.backend.commonviewdata.EntityComponentData;
import com.volavis.dbanonym.backend.commonviewdata.SelectedAttribute;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.SimpleAttribute;

import java.util.List;

/**
 * Displays all attributes whose anonymization option is invalid (reason: empty field, invalid data, etc.).
 * <P>Opened right after the user starts the anonymization.
 */
@CssImport("./styles/components/dialogs/validation-error-dialog.css")
public class ValidationErrorDialog extends Composite<Dialog> {

    ConnectedDatabase database;
    SelectedAttribute selectedAttribute;
    EntityComponentData entityComponentData;

    private final Grid<SimpleAttribute> grid = new Grid<>();
    private final Button ignoreButton = new Button();
    private final Button cancelButton = new Button();

    /**
     * Constructor.
     * @param database Database the user connected to.
     * @param selectedAttribute Attribute the user wants to anonymize.
     * @param entityComponentData Keeps track of opened and current entity components.
     */
    public ValidationErrorDialog(ConnectedDatabase database, SelectedAttribute selectedAttribute,
                                 EntityComponentData entityComponentData) {
        this.database = database;
        this.selectedAttribute = selectedAttribute;
        this.entityComponentData = entityComponentData;

        getContent().setWidth("800px");

        Paragraph title = new Paragraph(StringConstants.VALIDATION_ERROR_DIALOG_TITLE);
        title.setId("validation-dialog-title");
        Span text = new Span(StringConstants.VALIDATION_ERROR_DIALOG_TEXT);

        createGrid();
        HorizontalLayout buttonLayout = createButtonLayout();

        getContent().add(title, text, grid, buttonLayout);
    }

    /**
     * Creates the grid that shows the invalid attributes.
     */
    private void createGrid() {

        grid.setId("validation-dialog-grid");
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
        grid.addComponentColumn(this::buildLinkButton)
                .setFlexGrow(0).setWidth("45px");
    }

    /**
     * Creates the button layout containing the cancel and ignore button.
     */
    private HorizontalLayout createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        cancelButton.setText(StringConstants.CANCEL);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        ignoreButton.setText(StringConstants.IGNORE);
        ignoreButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SUCCESS);
        buttonLayout.add(cancelButton, ignoreButton);
        return buttonLayout;
    }

    /**
     * Creates the "link button" that navigates to the AnonymizationView of the invalid attribute.
     * @param simpleAttribute Attribute whose anonymization option is invalid.
     */
    private HorizontalLayout buildLinkButton(SimpleAttribute simpleAttribute) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        Button button = new Button(new Icon(VaadinIcon.ARROW_FORWARD));
        button.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_PRIMARY);
        button.addClickListener(event -> {
            Entity entity = database.getMetadata().getEntities().get(simpleAttribute.getEntityName());
            Attribute attribute = entity.getAttributes().get(simpleAttribute.getAttributeName());
            selectedAttribute.update(attribute, entity);
            entityComponentData.setCurrentEntityComponentId("");    // No scrolling
            button.getUI().ifPresent(ui -> ui.navigate("anonymization"));
            close();
        });
        layout.add(button);
        return layout;
    }

    /**
     * Sets the grid's items.
     * @param list List of invalid (simple)attributes.
     */
    public void setGridItems(List<SimpleAttribute> list) {
        grid.setItems(list);
    }

    /**
     * Disables the ignore button.
     */
    public void disableIgnoreButton() {
        ignoreButton.setVisible(false);
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

    public Button getIgnoreButton() {
        return ignoreButton;
    }
}