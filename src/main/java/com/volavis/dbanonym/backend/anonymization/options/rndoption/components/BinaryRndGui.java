package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.BinaryRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

/**
 * Builds the (rnd) GUI for the GuiID Binary.
 */
public class BinaryRndGui extends RndOptionComponent {

    private final BinaryRndData optionData;
    private final long recordCount;

    private final Select<BinaryRndData.BinaryDropdownElement> select = new Select<>();
    private final Span infoSpan = new Span();

    private final IntegerField minBytesField = new IntegerField();
    private final IntegerField maxBytesField = new IntegerField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public BinaryRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (BinaryRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        Span title = new Span(StringConstants.RND_COMP_BINARY_TITLE);

        select.setWidth("500px");
        select.setItems(BinaryRndData.BinaryDropdownElement.values());

        minBytesField.setLabel(StringConstants.RND_COMP_BINARY_MIN_FIELD_NAME);
        minBytesField.setWidth("500px");
        minBytesField.setValueChangeMode(ValueChangeMode.EAGER);

        maxBytesField.setLabel(StringConstants.RND_COMP_BINARY_MAX_FIELD_NAME);
        maxBytesField.setWidth("500px");
        maxBytesField.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout inputFieldLayout = new HorizontalLayout();
        inputFieldLayout.add(minBytesField, maxBytesField);

        add(title, select, infoSpan, inputFieldLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<BinaryRndData> binderSelect = new Binder<>();
        Binder<BinaryRndData> binderInputFields = new Binder<>();

        bindFieldsSelect(binderSelect);
        loadValuesSetupSelect(binderSelect);

        loadValuesSetupInputFields();
        bindFieldsInputFields(binderInputFields);

        saveValuesSetupSelect(binderSelect);
        saveValuesSetupInputFields(binderInputFields);
    }

    /**
     * Select: Loads previous user input.
     */
    private void loadValuesSetupSelect(Binder<BinaryRndData> binder) {
        binder.readBean(optionData);
    }

    /**
     * Input fields: Loads previous user input.
     */
    private void loadValuesSetupInputFields() {
        if(optionData.showInitialBounds()) {

            // Initial bounds (Shown only once before user has entered anything!)
            int minV = 1;
            int maxV = attribute.getPrecision();
            optionData.setMinBytes(minV);
            minBytesField.setValue(minV);
            optionData.setMaxBytes(maxV);
            maxBytesField.setValue(maxV);

            optionData.setShowInitialBounds(false);

        } else {

            // binder.readBean() gets not used because it triggers validation!
            if(optionData.getMinBytes() != null) {
                minBytesField.setValue(optionData.getMinBytes());
            }
            if(optionData.getMaxBytes() != null) {
                maxBytesField.setValue(optionData.getMaxBytes());
            }
        }
    }

    /**
     * Select: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsSelect(Binder<BinaryRndData> binder) {
        binder.bind(select,
                rndData -> {
                    BinaryRndData.BinaryDropdownElement selection = rndData.getBinaryDropdownElement();
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    return selection;
                },
                (rndData, selection) -> {
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    rndData.setBinaryDropdownElement(selection);
                });
    }

    /**
     * Sets the visibility of the components depending on the selected dropdown element.
     */
    private void updateComponentsVisibility(BinaryRndData.BinaryDropdownElement selection) {
        switch (selection) {
            case SAME_BYTE_COUNT:
                minBytesField.setVisible(false);
                maxBytesField.setVisible(false);
                break;
            case USER_DEFINED:
                minBytesField.setVisible(true);
                maxBytesField.setVisible(true);
                break;
        }
    }

    /**
     * Input fields: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsInputFields(Binder<BinaryRndData> binder) {
        binder.forField(minBytesField)
                .asRequired(StringConstants.RND_COMP_BINARY_REQUIRED_MIN)
                .withValidator(new IntegerRangeValidator(String.format(StringConstants.RND_COMP_BINARY_VALIDATOR_RANGE, attribute.getPrecision()),
                        1, attribute.getPrecision()))
                .bind(BinaryRndData::getMinBytes, BinaryRndData::setMinBytes);

        binder.forField(maxBytesField)
                .asRequired(StringConstants.RND_COMP_BINARY_REQUIRED_MAX)
                .withValidator(new IntegerRangeValidator(String.format(StringConstants.RND_COMP_BINARY_VALIDATOR_RANGE, attribute.getPrecision()),
                        1, attribute.getPrecision()))
                .withValidator(maxInteger -> {
                    Integer minInteger = minBytesField.getValue();
                    if(minInteger != null) {
                        return maxInteger.compareTo(minInteger) >= 0;
                    }
                    return false;
                }, StringConstants.RND_COMP_BINARY_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxInteger -> {
                    if(attribute.isUnique()) {
                        Integer minInteger = minBytesField.getValue();
                        if(minInteger != null) {
                            // TODO: Missing Validation
                        }
                        return true;    // Change --> return false;
                    }
                    return true;
                }, StringConstants.RND_COMP_BINARY_VALIDATOR_UNIQUE)
                .bind(BinaryRndData::getMaxBytes, BinaryRndData::setMaxBytes);
    }

    /**
     * Select: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupSelect(Binder<BinaryRndData> binder) {
        select.addValueChangeListener(event -> binder.writeBeanIfValid(optionData));
    }

    /**
     * Input fields: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupInputFields(Binder<BinaryRndData> binder) {
        minBytesField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinBytes(null);
            }
        });

        maxBytesField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxBytes(null);
            }
        });
    }
}