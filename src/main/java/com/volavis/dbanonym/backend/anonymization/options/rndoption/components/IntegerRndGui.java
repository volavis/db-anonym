package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToBigIntegerConverter;
import com.vaadin.flow.data.validator.BigIntegerRangeValidator;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.IntegerRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcIntegerType;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Builds the (rnd) GUI for the GuiID Integer.
 */
public class IntegerRndGui extends RndOptionComponent {

    private final IntegerRndData optionData;
    private final long recordCount;

    private final Select<IntegerRndData.IntegerDropdownElement> select = new Select<>();
    private final Span infoSpan = new Span();

    private final Checkbox signCheckbox = new Checkbox();

    private final TextField minIntegerField = new TextField();
    private final TextField maxIntegerField = new TextField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public IntegerRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (IntegerRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        Span title = new Span(StringConstants.RND_COMP_INTEGER_TITLE);

        select.setWidth("500px");
        select.setItems(IntegerRndData.IntegerDropdownElement.values());

        signCheckbox.setLabel(StringConstants.RND_COMP_INTEGER_CHECKBOX_NAME);
        signCheckbox.setVisible(attribute.isSigned());

        minIntegerField.setLabel(StringConstants.RND_COMP_INTEGER_MIN_FIELD_NAME);
        minIntegerField.setWidth("500px");
        minIntegerField.setValueChangeMode(ValueChangeMode.EAGER);

        maxIntegerField.setLabel(StringConstants.RND_COMP_INTEGER_MAX_FIELD_NAME);
        maxIntegerField.setWidth("500px");
        maxIntegerField.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout inputFieldLayout = new HorizontalLayout();
        inputFieldLayout.add(minIntegerField, maxIntegerField);

        add(title, select, infoSpan, signCheckbox, inputFieldLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<IntegerRndData> binderSelect = new Binder<>();
        Binder<IntegerRndData> binderInputFields = new Binder<>();

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
    private void loadValuesSetupSelect(Binder<IntegerRndData> binder) {
        binder.readBean(optionData);
    }

    /**
     * Input fields: Loads previous user input.
     */
    private void loadValuesSetupInputFields() {

        signCheckbox.setValue(optionData.useSign());

        if(optionData.showInitialBounds()) {

            // Initial bounds (Shown only once before user has entered anything!)
            BigInteger minV = JdbcIntegerType.getMinOrMax(true, attribute.getJdbcDataTypeValidation(), attribute.isSigned());
            BigInteger maxV = JdbcIntegerType.getMinOrMax(false, attribute.getJdbcDataTypeValidation(), attribute.isSigned());
            if( minV != null && maxV != null) {
                optionData.setMinInteger(minV);
                minIntegerField.setValue(minV.toString());
                optionData.setMaxInteger(maxV);
                maxIntegerField.setValue(maxV.toString());
            }

            optionData.setShowInitialBounds(false);

        } else {

            // binder.readBean() gets not used because it triggers validation!
            if(optionData.getMinInteger() != null) {
                minIntegerField.setValue(optionData.getMinInteger().toString());
            }
            if(optionData.getMaxInteger() != null) {
                maxIntegerField.setValue(optionData.getMaxInteger().toString());
            }
        }
    }

    /**
     * Select: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsSelect(Binder<IntegerRndData> binder) {
        binder.bind(select,
                rndData -> {
                    IntegerRndData.IntegerDropdownElement selection = rndData.getIntegerDropdownElement();
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    return selection;
                },
                (rndData, selection) -> {
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    rndData.setIntegerDropdownElement(selection);
                });
    }

    /**
     * Sets the visibility of the components depending on the selected dropdown element.
     */
    private void updateComponentsVisibility(IntegerRndData.IntegerDropdownElement selection) {
        switch (selection) {
            case SAME_DIGIT_COUNT:
                if(attribute.isSigned()) {
                    signCheckbox.setVisible(true);
                }
                minIntegerField.setVisible(false);
                maxIntegerField.setVisible(false);
                break;
            case VALUE_RANGE:
                signCheckbox.setVisible(false);
                minIntegerField.setVisible(false);
                maxIntegerField.setVisible(false);
                break;
            case USER_DEFINED:
                signCheckbox.setVisible(false);
                minIntegerField.setVisible(true);
                maxIntegerField.setVisible(true);
                break;
        }
    }

    /**
     * Input fields: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsInputFields(Binder<IntegerRndData> binder) {
        bindingSetupMin(binder);
        bindingSetupMax(binder);
    }

    /**
     * Field binding setup for minIntegerField.
     * @param binder Binder the fields will be bound to.
     */
    private void bindingSetupMin(Binder<IntegerRndData> binder) {
        binder.forField(minIntegerField)
                .asRequired(StringConstants.RND_COMP_INTEGER_REQUIRED_MIN)
                .withNullRepresentation("")
                .withValidator(new RegexpValidator(StringConstants.RND_COMP_INTEGER_VALIDATOR_NAN,
                        "^\\s*[-]?\\d+\\s*"))
                .withConverter(new StringToBigIntegerConverter(StringConstants.RND_COMP_INTEGER_VALIDATOR_NAN) {
                    @Override
                    protected NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(Locale.GERMANY);  // Locale
                        format.setGroupingUsed(false);                          // Separator disabled!
                        return format;
                    }
                })
                .withValidator(new BigIntegerRangeValidator(StringConstants.RND_COMP_INTEGER_VALIDATOR_VALUE_RANGE,
                        JdbcIntegerType.getMinOrMax(true, attribute.getJdbcDataTypeValidation(), attribute.isSigned()),
                        JdbcIntegerType.getMinOrMax(false, attribute.getJdbcDataTypeValidation(), attribute.isSigned())))
                .bind(IntegerRndData::getMinInteger, IntegerRndData::setMinInteger);
    }

    /**
     * Field binding setup for maxIntegerField.
     * @param binder Binder the fields will be bound to.
     */
    private void bindingSetupMax(Binder<IntegerRndData> binder) {
        binder.forField(maxIntegerField)
                .asRequired(StringConstants.RND_COMP_INTEGER_REQUIRED_MAX)
                .withNullRepresentation("")
                .withValidator(new RegexpValidator(StringConstants.RND_COMP_INTEGER_VALIDATOR_NAN,
                        "^\\s*[-]?\\d+\\s*"))
                .withConverter(new StringToBigIntegerConverter(StringConstants.RND_COMP_INTEGER_VALIDATOR_NAN) {
                    @Override
                    protected NumberFormat getFormat(Locale locale) {
                        NumberFormat format = super.getFormat(Locale.GERMANY);  // Locale US
                        format.setGroupingUsed(false);                          // Separator disabled!
                        return format;
                    }
                })
                .withValidator(new BigIntegerRangeValidator(StringConstants.RND_COMP_INTEGER_VALIDATOR_VALUE_RANGE,
                        JdbcIntegerType.getMinOrMax(true, attribute.getJdbcDataTypeValidation(), attribute.isSigned()),
                        JdbcIntegerType.getMinOrMax(false, attribute.getJdbcDataTypeValidation(), attribute.isSigned())))
                .withValidator(maxBigInteger -> {
                    try {
                        return maxBigInteger.compareTo(new BigInteger(minIntegerField.getValue().trim())) > 0;
                    } catch (Exception ignored) {
                        // Problem cross field validation: minIntegerField not yet validated!
                        return false;
                    }
                }, StringConstants.RND_COMP_INTEGER_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxBigInteger -> {
                    if(attribute.isUnique()) {
                        try {
                            BigInteger minBigInteger = new BigInteger(minIntegerField.getValue().trim());
                            BigInteger valueCount = maxBigInteger.subtract(minBigInteger).add(BigInteger.ONE);
                            return valueCount.compareTo(BigInteger.valueOf(recordCount)) >= 0;
                        } catch (Exception ignored) {
                            // Problem cross field validation: minIntegerField not yet validated!
                            return false;
                        }
                    }
                    return true;
                }, String.format(StringConstants.RND_COMP_INTEGER_VALIDATOR_UNIQUE, recordCount))
                .bind(IntegerRndData::getMaxInteger, IntegerRndData::setMaxInteger);
    }

    /**
     * Select: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupSelect(Binder<IntegerRndData> binder) {
        select.addValueChangeListener(event -> binder.writeBeanIfValid(optionData));
    }

    /**
     * Input fields: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupInputFields(Binder<IntegerRndData> binder) {

        signCheckbox.addValueChangeListener(event -> optionData.setUseSign(signCheckbox.getValue()));

        minIntegerField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinInteger(null);
            }
        });

        maxIntegerField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxInteger(null);
            }
        });
    }
}