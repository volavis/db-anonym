package com.volavis.dbanonym.components.dialogs;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.database.SimpleAttribute;

import java.util.*;

/**
 * The user can decide if he wants to anonymize all attributes or if he only wants to anonymize a selection of attributes.
 * <P>Shows an accordion of all attributes with an anonymization option.
 * <P>If the user wants to make a selection, checkboxes appear next to the attribute's name.
 */
@CssImport("./styles/components/dialogs/anonymization-dialog.css")
public class AnonymizationDialog extends Composite<Dialog> {

    private final Paragraph title = new Paragraph();
    private final Span text = new Span();

    private final Div accordionDiv = new Div();
    private final List<Checkbox> checkboxList = new ArrayList<>();
    private final List<SimpleAttribute> selectedAttributesList = new ArrayList<>();

    private final HorizontalLayout buttonLayout = new HorizontalLayout();
    private final Button cancelButton = new Button();
    private final HorizontalLayout leftLayout = new HorizontalLayout();
    private final Button anonymizeEverythingButton = new Button();
    private final Button anonymizeSelectionButton = new Button();
    private final Button confirmSelectionButton = new Button();

    /**
     * Constructor.
     */
    public AnonymizationDialog() {
        getContent().setWidth("800px");

        title.setId("dialog-title");

        // Scroller contains Div which contains Accordion
        Scroller scroller = new Scroller(accordionDiv);
        scroller.setId("scroller-accordion");
        scroller.setHeight("300px");

        createButtons();

        getContent().add(title, text, scroller, buttonLayout);
    }

    private void createButtons() {
        buttonLayout.setId("button-layout");
        buttonLayout.setSpacing(false);
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        anonymizeEverythingButton.setText(StringConstants.ANONYMIZATION_DIALOG_EVERYTHING_BUTTON);
        anonymizeEverythingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        anonymizeSelectionButton.setText(StringConstants.ANONYMIZATION_DIALOG_SELECTION_BUTTON);
        anonymizeSelectionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        confirmSelectionButton.setText(StringConstants.OKAY);
        confirmSelectionButton.setWidth("250px");
        confirmSelectionButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        cancelButton.setText(StringConstants.CANCEL);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
    }

    /**
     * Displays the accordion without checkboxes. User can decide if he wants to anonymize everything or a selection.
     */
    public void overviewMode() {
        title.setText(StringConstants.ANONYMIZATION_DIALOG_OVERVIEW_TITLE);
        text.setText(StringConstants.ANONYMIZATION_DIALOG_OVERVIEW_TEXT);

        buttonLayout.removeAll();
        leftLayout.add(anonymizeEverythingButton, anonymizeSelectionButton);
        buttonLayout.add(leftLayout, cancelButton);
    }

    /**
     * Displays the accordion with checkboxes. User can make a selection of attributes to anonymize.
     */
    public void selectionMode() {
        title.setText(StringConstants.ANONYMIZATION_DIALOG_SELECTION_TITLE);
        text.setText(StringConstants.ANONYMIZATION_DIALOG_SELECTION_TEXT);

        selectedAttributesList.clear();
        for(Checkbox cb : checkboxList) {
            cb.setValue(false);
            cb.setVisible(true);
        }

        buttonLayout.removeAll();
        confirmSelectionButton.setEnabled(false);
        buttonLayout.add(confirmSelectionButton, cancelButton);
    }

    /**
     * Fills the accordion with data.
     * @param simpleAttributeList Attributes to be displayed in the accordion.
     */
    public void buildAccordion(List<SimpleAttribute> simpleAttributeList) {

        selectedAttributesList.clear();
        checkboxList.clear();

        Accordion accordion = new Accordion();
        Map<String, VerticalLayout> verticalLayoutMap = new HashMap<>();

        for(SimpleAttribute simpleAttribute : simpleAttributeList) {
            String key = simpleAttribute.getEntityName();
            VerticalLayout tmpLayout = getVerticalLayout(verticalLayoutMap, key);
            HorizontalLayout horizontalLayout = createHorizontalLayout(simpleAttribute);
            tmpLayout.add(horizontalLayout);
        }

        List<String> keyList =  new ArrayList<>(verticalLayoutMap.keySet());
        Collections.sort(keyList);
        for(String key : keyList) {
            accordion.add(key, verticalLayoutMap.get(key));
        }

        accordionDiv.removeAll();
        accordionDiv.add(accordion);
    }

    /**
     * Returns an existing Layout or a new one. The VerticalLayout displays all the attributes of an entity.
     * @param verticalLayoutMap Map containing all VerticalLayouts of the Accordion.
     * @param key Current entity's name.
     */
    private VerticalLayout getVerticalLayout(Map<String, VerticalLayout> verticalLayoutMap, String key) {
        VerticalLayout tmpLayout;
        if(verticalLayoutMap.containsKey(key)) {
            tmpLayout = verticalLayoutMap.get(key);
        } else {
            tmpLayout = new VerticalLayout();
            verticalLayoutMap.put(key, tmpLayout);
        }
        return tmpLayout;
    }

    /**
     * Creates the HorizontalLayout containing the components for one attribute (checkbox, name, option).
     * @param simpleAttribute Attribute that will be displayed.
     */
    private HorizontalLayout createHorizontalLayout(SimpleAttribute simpleAttribute) {

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        Checkbox attributeCheckbox = new Checkbox();
        checkboxList.add(attributeCheckbox);
        attributeCheckbox.setValue(false);
        attributeCheckbox.setVisible(false);
        attributeCheckbox.addValueChangeListener(event -> {
            if(attributeCheckbox.getValue()) {
                selectedAttributesList.add(simpleAttribute);
            } else {
                selectedAttributesList.remove(simpleAttribute);
            }
            confirmSelectionButton.setEnabled(!selectedAttributesList.isEmpty());
        });
        Span attributeName = new Span(simpleAttribute.getAttributeName());
        Span attributeOption = new Span("[" + simpleAttribute.getOption().toString() + "]");
        horizontalLayout.add(attributeCheckbox, attributeName, attributeOption);

        return horizontalLayout;
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

    public Button getAnonymizeEverythingButton() {
        return anonymizeEverythingButton;
    }

    public Button getAnonymizeSelectionButton() {
        return anonymizeSelectionButton;
    }

    public Button getConfirmSelectionButton() {
        return confirmSelectionButton;
    }

    public List<SimpleAttribute> getSelectedAttributesList() {
        return selectedAttributesList;
    }
}