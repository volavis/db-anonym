package com.volavis.dbanonym.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.*;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.commonviewdata.SelectedAttribute;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Gets opened if user wants to choose the anonymization option of an attribute.
 * <P>The user can customize the option (e.g. enter min and max values).
 */
@Route(value = StringConstants.ANONYMIZATION_VIEW_ROUTE, layout = MainView.class)
@PageTitle(StringConstants.ANONYMIZATION_VIEW_PAGE_TITLE)
@CssImport("./styles/views/anonymization-view.css")
public class AnonymizationView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver {

    private final SelectedAttribute selectedAttr;

    private Attribute attribute;
    private Entity entity;

    private final Button saveButton = new Button();
    private final Button cancelButton = new Button();
    private final Span entityNameSpan = new Span();
    private final Span attributeNameSpan = new Span();
    private final Span infoAttributeDBTypeSpan = new Span();
    private final Span infoAttributeJavaTypeSpan = new Span();
    private final Span infoAttributeNullSpan = new Span();
    private final Span infoAttributeUniqueSpan = new Span();
    private final RadioButtonGroup<OptionID> radioButtons = new RadioButtonGroup<>();
    private final Div optionContainer = new Div();

    /** Contains a data object for each anonymization option.
     * That way input does not disappear if user switches between options! */
    private final Map<OptionID, OptionData> optionsMap = new HashMap<>();

    /**
     * Constructor.
     * @param selectedAttr Attribute the user wants to anonymize. (autowired)
     */
    @Autowired
    public AnonymizationView(SelectedAttribute selectedAttr) {

        this.selectedAttr = selectedAttr;

        setId("anonymization-view");

        HorizontalLayout topLayout = createTopLayout();
        HorizontalLayout bottomLayout = createBottomLayout();
        add(topLayout, bottomLayout);

        eventListenerSetup();
    }

    /**
     * Creates the top layout that consists out of the save/cancel buttons and the database information.
     */
    private HorizontalLayout createTopLayout() {

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setId("top-layout");
        topLayout.setWidthFull();
        topLayout.setSpacing(false);
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        saveButton.setText(StringConstants.SAVE);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cancelButton.setText(StringConstants.CANCEL);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        buttonLayout.add(saveButton, cancelButton);

        HorizontalLayout titleLayout = new HorizontalLayout();
        attributeNameSpan.setId("attribute-name-span");
        titleLayout.add(entityNameSpan, attributeNameSpan);

        HorizontalLayout infoLayout = new HorizontalLayout();
        infoLayout.add(infoAttributeJavaTypeSpan, infoAttributeDBTypeSpan, infoAttributeNullSpan, infoAttributeUniqueSpan);

        topLayout.add(buttonLayout, titleLayout, infoLayout);

        return topLayout;
    }

    /**
     * Creates the bottom layout that consists out of the option radio buttons and the option component container.
     */
    private HorizontalLayout createBottomLayout() {

        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.setSpacing(false);
        bottomLayout.setWidthFull();

        radioButtons.setId("radio-buttons");
        radioButtons.setLabel(StringConstants.ANONYMIZATION_VIEW_OPTIONS);
        radioButtons.setItems(OptionID.values());
        radioButtons.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioButtons.setValue(OptionID.NONE);

        optionContainer.setId("option-container");
        optionContainer.setWidthFull();

        bottomLayout.add(radioButtons, optionContainer);

        return bottomLayout;
    }

    /**
     * Setup: Event handling for buttons and dialogs.
     */
    private void eventListenerSetup() {

        // Save button
        saveButton.addClickListener(event -> save());

        // Cancel button
        cancelButton.addClickListener(event -> cancelButton.getUI().ifPresent(ui -> ui.navigate("tables")));

        // Radio buttons
        radioButtons.addValueChangeListener(event -> updateView(event.getValue()));
    }

    /**
     * Saves the attribute's anonymization option.
     */
    private void save() {
        OptionID tmpID = radioButtons.getValue();
        if(tmpID == OptionID.NONE) {
            attribute.setAnonymizationOptionData(null);
            saveButton.getUI().ifPresent(ui -> ui.navigate("tables"));
        } else {
            OptionData optionData = optionsMap.get(tmpID);
            if(optionData != null && optionData.isValid()) {
                attribute.setAnonymizationOptionData(optionData);
                saveButton.getUI().ifPresent(ui -> ui.navigate("tables"));
            } else {
                Notification notification = new Notification(StringConstants.ANONYMIZATION_VIEW_INVALID, 3000);
                notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
                notification.open();
            }
        }
    }

    /**
     * Checks if the user is connected to a database (if metadata exists). If not navigates to ConnectView.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(selectedAttr == null || selectedAttr.getAttribute() == null || selectedAttr.getEntity() == null) {
            event.forwardTo(ConnectView.class);
            Notification notification = new Notification(StringConstants.WARNING_NO_DB_CONNECTION, 5000);
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }

    /**
     * Uses the selectedAttribute bean to retrieve the current attribute and entity.
     * Disables unnecessary radio buttons, builds the option's gui and inserts information texts.
     */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {

        attribute = selectedAttr.getAttribute();
        entity = selectedAttr.getEntity();

        // Fill optionsMap
        for(OptionID optionID : OptionID.values()) {
            optionsMap.put(optionID, optionID.createAnonymizationOption().getOptionData(attribute));
        }

        disableRadioButtons();

        // Update optionsMap and view
        if(attribute.getAnonymizationOptionData() == null) {
            updateView(OptionID.NONE);
        } else {
            // Replace empty option with already built option
            optionsMap.put(attribute.getAnonymizationOptionData().getOptionID(), attribute.getAnonymizationOptionData());
            updateView(attribute.getAnonymizationOptionData().getOptionID());
        }

        insertDatabaseInformation();
    }

    /**
     * Disables radio buttons whose anonymization option cannot be used for this attribute.
     */
    private void disableRadioButtons() {
        radioButtons.setItemEnabledProvider(optionID -> optionID.createAnonymizationOption().enableRadioButton(attribute));
    }

    /**
     * Inserts database information into the top layout Spans.
     */
    private void insertDatabaseInformation() {

        entityNameSpan.setText(entity.getName());
        attributeNameSpan.setText(attribute.getName());

        infoAttributeJavaTypeSpan.setText(StringConstants.JAVA_DATA_TYPE + ": " + attribute.getJavaDataType());

        String typeInfo = attribute.getDataTypeInformation();
        if(typeInfo == null || typeInfo.isEmpty()) {
            infoAttributeDBTypeSpan.setText(StringConstants.DB_DATA_TYPE + ": " + attribute.getDatabaseDataType().toUpperCase());
        } else {
            infoAttributeDBTypeSpan.setText(StringConstants.DB_DATA_TYPE + ": " + attribute.getDatabaseDataType().toUpperCase() + " (" + typeInfo + ")");
        }

        infoAttributeNullSpan.setText(StringConstants.NULLABLE + ": " + (attribute.isNullable() ? StringConstants.YES : StringConstants.NO));
        infoAttributeUniqueSpan.setText(StringConstants.UNIQUE + ": " + (attribute.isUnique() ? StringConstants.YES : StringConstants.NO));
    }

    /**
     * Selects the radio button and builds the gui for the given anonymization option.
     * @param optionID The attribute's optionID || radio button selection || OptionID NONE.
     */
    private void updateView(OptionID optionID) {

        // Update selected radio button
        radioButtons.setValue(optionID);

        // Update optionContainer
        optionContainer.removeAll();
        optionContainer.add(optionID.createAnonymizationOption().getOptionComponent(optionsMap.get(optionID), entity, attribute));
    }
}