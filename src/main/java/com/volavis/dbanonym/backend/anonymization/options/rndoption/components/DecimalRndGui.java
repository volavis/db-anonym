package com.volavis.dbanonym.backend.anonymization.options.rndoption.components;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.DecimalRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.JavaDataType;
import com.volavis.dbanonym.backend.database.jdbc.types.JdbcDecimalType;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * Builds the (rnd) GUI for the GuiID Decimal.
 */
public class DecimalRndGui extends RndOptionComponent {

    protected final DecimalRndData optionData;
    private final long recordCount;

    private final Select<DecimalRndData.DecimalDropdownElement> select = new Select<>();
    private final Span infoSpan = new Span();

    protected final Checkbox signCheckbox = new Checkbox();

    private final int minDecimalPlace = 0;
    private final int maxDecimalPlace = Integer.MAX_VALUE;
    protected final IntegerField decimalPlaceField = new IntegerField();

    protected final BigDecimalField minDecimalField = new BigDecimalField();
    protected final BigDecimalField maxDecimalField = new BigDecimalField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public DecimalRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (DecimalRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        Span title = new Span(StringConstants.RND_COMP_DECIMAL_TITLE);

        select.setWidth("500px");
        select.setItems(DecimalRndData.DecimalDropdownElement.values());

        signCheckbox.setLabel(StringConstants.RND_COMP_DECIMAL_CHECKBOX_NAME);

        decimalPlaceField.setLabel(StringConstants.RND_COMP_DECIMAL_DECIMAL_PLACE_FIELD_NAME);
        decimalPlaceField.setWidth("500px");
        decimalPlaceField.setHasControls(true);
        decimalPlaceField.setValueChangeMode(ValueChangeMode.EAGER);
        decimalPlaceField.setMin(minDecimalPlace);
        decimalPlaceField.setMax(maxDecimalPlace);

        minDecimalField.setLabel(StringConstants.RND_COMP_DECIMAL_MIN_FIELD_NAME);
        minDecimalField.setWidth("500px");
        minDecimalField.setLocale(Locale.GERMANY);
        minDecimalField.setValueChangeMode(ValueChangeMode.EAGER);

        maxDecimalField.setLabel(StringConstants.RND_COMP_DECIMAL_MAX_FIELD_NAME);
        maxDecimalField.setWidth("500px");
        maxDecimalField.setLocale(Locale.GERMANY);
        maxDecimalField.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout inputFieldLayout = new HorizontalLayout();
        inputFieldLayout.add(minDecimalField, maxDecimalField);

        add(title, select, infoSpan, signCheckbox, decimalPlaceField, inputFieldLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<DecimalRndData> binderSelect = new Binder<>();
        Binder<DecimalRndData> binderInputFields = new Binder<>();

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
    private void loadValuesSetupSelect(Binder<DecimalRndData> binder) {
        binder.readBean(optionData);
    }

    /**
     * Input fields: Loads previous user input.
     */
    protected void loadValuesSetupInputFields() {

        signCheckbox.setValue(optionData.useSign());

        if(optionData.showInitialBounds()) {

            // Initial bounds (Shown only once before user has entered anything!)
            switch(attribute.getJavaDataType()) {
                case FLOAT:
                case DOUBLE:
                    Integer decimalPlacesV = JdbcDecimalType.getDecimalPlaces(attribute.getJdbcDataTypeValidation());
                    if(decimalPlacesV != null) {
                        optionData.setDecimalPlace(decimalPlacesV);
                        decimalPlaceField.setValue(decimalPlacesV);
                    }
                    break;
                case BIGDECIMAL:
                    int scaleV = attribute.getScale();
                    optionData.setDecimalPlace(scaleV);
                    decimalPlaceField.setValue(scaleV);
                    BigDecimal minV = JdbcDecimalType.generateMinOrMax(true, attribute);
                    optionData.setMinDecimal(minV);
                    minDecimalField.setValue(minV);
                    BigDecimal maxV = JdbcDecimalType.generateMinOrMax(false, attribute);
                    optionData.setMaxDecimal(maxV);
                    maxDecimalField.setValue(maxV);
                    break;
            }

            optionData.setShowInitialBounds(false);

        } else {

            // binder.readBean() gets not used because it triggers validation!
            if(optionData.getDecimalPlace() != null) {
                decimalPlaceField.setValue(optionData.getDecimalPlace());
            }
            if(optionData.getMinDecimal() != null) {
                minDecimalField.setValue(optionData.getMinDecimal());
            }
            if(optionData.getMaxDecimal() != null) {
                maxDecimalField.setValue(optionData.getMaxDecimal());
            }
        }
    }

    /**
     * Select: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsSelect(Binder<DecimalRndData> binder) {
        binder.bind(select,
                rndData -> {
                    DecimalRndData.DecimalDropdownElement selection = rndData.getDecimalDropdownElement();
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    return selection;
                },
                (rndData, selection) -> {
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    rndData.setDecimalDropdownElement(selection);
                });
    }

    /**
     * Sets the visibility of the components depending on the selected dropdown element.
     */
    private void updateComponentsVisibility(DecimalRndData.DecimalDropdownElement selection) {
        switch (selection) {
            case SAME_PRE_POST_DECIMAL_PLACES:
                signCheckbox.setVisible(true);
                decimalPlaceField.setVisible(false);
                minDecimalField.setVisible(false);
                maxDecimalField.setVisible(false);
                break;
            case USER_DEFINED:
                signCheckbox.setVisible(false);
                decimalPlaceField.setVisible(true);
                minDecimalField.setVisible(true);
                maxDecimalField.setVisible(true);
                break;
        }
    }

    /**
     * Input fields: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsInputFields(Binder<DecimalRndData> binder) {
        bindingSetupDecimalPlaceField(binder);
        bindingSetupMin(binder);
        bindingSetupMax(binder);
    }

    /**
     * Field binding setup for decimalPlaceField.
     * @param binder Binder the fields will be bound to.
     */
    private void bindingSetupDecimalPlaceField(Binder<DecimalRndData> binder) {
        binder.forField(decimalPlaceField)
                .asRequired(StringConstants.RND_COMP_DECIMAL_DECIMAL_PLACE_REQUIRED)
                .withValidator(new IntegerRangeValidator(StringConstants.RND_COMP_DECIMAL_DECIMAL_PLACE_VALIDATOR_RANGE
                        ,minDecimalPlace, maxDecimalPlace))
                .withValidator(integerValue -> {
                    if(attribute.getJavaDataType() == JavaDataType.BIGDECIMAL) {
                        return integerValue <= attribute.getScale();
                    } else {
                        return true;
                    }
                }, String.format(StringConstants.RND_COMP_DECIMAL_DECIMAL_PLACE_VALIDATOR_SCALE, attribute.getScale()))
                .bind(DecimalRndData::getDecimalPlace, DecimalRndData::setDecimalPlace);
    }

    /**
     * Field binding setup for minDecimalField.
     * @param binder Binder the fields will be bound to.
     */
    private void bindingSetupMin(Binder<DecimalRndData> binder) {
        binder.forField(minDecimalField)
                .asRequired(StringConstants.RND_COMP_DECIMAL_REQUIRED_MIN)
                .withValidator(getCheckPostDecimalPlacesValidator())
                .withValidator(getCheckInfinityOrPrePostDecimalPlacesValidator())
                .bind(DecimalRndData::getMinDecimal, DecimalRndData::setMinDecimal);
    }

    /**
     * Field binding setup for maxDecimalField.
     * @param binder Binder the fields will be bound to.
     */
    private void bindingSetupMax(Binder<DecimalRndData> binder) {
        binder.forField(maxDecimalField)
                .asRequired(StringConstants.RND_COMP_DECIMAL_REQUIRED_MAX)
                .withValidator(getCheckPostDecimalPlacesValidator())
                .withValidator(getCheckInfinityOrPrePostDecimalPlacesValidator())
                .withValidator(maxBigDecimal -> {
                    try {
                        return maxBigDecimal.compareTo(new BigDecimal(minDecimalField.getValue().toPlainString().trim())) > 0;
                    } catch (Exception ignored) {
                        // Problem cross field validation: minDecimalField not yet validated!
                        return false;
                    }
                }, StringConstants.RND_COMP_DECIMAL_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxBigDecimal -> {
                    // If attribute is unique: Check if user defined interval has enough values
                    if(attribute.isUnique()) {
                        try {
                            int decimalPlaces = decimalPlaceField.getValue();
                            BigDecimal minBigDecimal = new BigDecimal(minDecimalField.getValue().toPlainString().trim());
                            BigDecimal diff = maxBigDecimal.subtract(minBigDecimal);
                            BigInteger valueCount = (diff.movePointRight(decimalPlaces)).toBigInteger();
                            valueCount = valueCount.add(BigInteger.ONE);
                            return valueCount.compareTo(BigInteger.valueOf(recordCount)) >= 0;
                        } catch (Exception ignored) {
                            // Problem cross field validation: minDecimalField not yet validated!
                            return false;
                        }
                    }
                    return true;
                }, String.format(StringConstants.RND_COMP_DECIMAL_VALIDATOR_UNIQUE, recordCount))
                .bind(DecimalRndData::getMaxDecimal, DecimalRndData::setMaxDecimal);
    }

    /**
     * Returns a validator that compares the current decimal places to the input in the decimalPlaceField.
     * @return Okay = true; Problem = false.
     */
    private Validator<BigDecimal> getCheckPostDecimalPlacesValidator() {
        return (bigDecimal, valueContext) -> {
            try {
                Integer decimalPlace = decimalPlaceField.getValue();
                if(decimalPlace != null) {
                    if (JdbcDecimalType.checkPostDecimalPlaces(decimalPlace, bigDecimal)) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error(StringConstants.RND_COMP_DECIMAL_VALIDATOR_POST_DECIMAL_PLACES);
                    }
                }
                return ValidationResult.error(StringConstants.RND_COMP_DECIMAL_VALIDATOR_POST_DECIMAL_PLACES);
            } catch (Exception ignored) {
                // Problem cross field validation: decimalPlaceField not yet validated!
                return ValidationResult.error(StringConstants.RND_COMP_DECIMAL_VALIDATOR_POST_DECIMAL_PLACES);
            }
        };
    }

    /**
     * Returns a validator that checks a) If the float/double value is between NEGATIVE_INFINITY and POSITIVE_INFINITY.
     * And b) The pre and post decimal places of the BigDecimal value.
     * @return Okay = true; Problem = false.
     */
    private Validator<BigDecimal> getCheckInfinityOrPrePostDecimalPlacesValidator() {
        return (bigDecimal, valueContext) -> {
            switch (attribute.getJavaDataType()) {
                case FLOAT:
                    float fValue = bigDecimal.floatValue();
                    if (fValue != Float.NEGATIVE_INFINITY && fValue != Float.POSITIVE_INFINITY) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error(StringConstants.RND_COMP_DECIMAL_VALIDATOR_INFINITY);
                    }
                case DOUBLE:
                    double dValue = bigDecimal.doubleValue();
                    if (dValue != Double.NEGATIVE_INFINITY && dValue != Double.POSITIVE_INFINITY) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error(StringConstants.RND_COMP_DECIMAL_VALIDATOR_INFINITY);
                    }
                case BIGDECIMAL:
                    if (JdbcDecimalType.checkPreAndPostDecimalPlaces(attribute, bigDecimal)) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error(String.format(StringConstants.RND_COMP_DECIMAL_VALIDATOR_PRE_POST_DECIMAL_PLACES,
                                attribute.getPrecision(), attribute.getScale()));
                    }
            }
            return ValidationResult.ok();
        };
    }

    /**
     * Select: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupSelect(Binder<DecimalRndData> binder) {
        select.addValueChangeListener(event -> binder.writeBeanIfValid(optionData));
    }

    /**
     * Input fields: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupInputFields(Binder<DecimalRndData> binder) {

        signCheckbox.addValueChangeListener(event -> optionData.setUseSign(signCheckbox.getValue()));

        decimalPlaceField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setDecimalPlace(null);
            }
        });

        minDecimalField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinDecimal(null);
            }
        });

        maxDecimalField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxDecimal(null);
            }
        });
    }
}