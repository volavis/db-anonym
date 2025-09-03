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
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.CharactersRndData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

import java.math.BigInteger;

/**
 * Builds the (rnd) GUI for the GuiID Characters.
 */
public class CharactersRndGui extends RndOptionComponent {

    private final CharactersRndData optionData;
    private final long recordCount;

    private final Select<CharactersRndData.CharactersDropdownElement> select = new Select<>();
    private final Span infoSpan = new Span();

    private final IntegerField minLengthField = new IntegerField();
    private final IntegerField maxLengthField = new IntegerField();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public CharactersRndGui(RndOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (CharactersRndData) optionData;
        this.recordCount = entity.getRecordCount();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {

        Span title = new Span(StringConstants.RND_COMP_CHARACTERS_TITLE);

        select.setWidth("500px");
        select.setItems(CharactersRndData.CharactersDropdownElement.values());

        minLengthField.setLabel(StringConstants.RND_COMP_CHARACTERS_MIN_FIELD_NAME);
        minLengthField.setWidth("500px");
        minLengthField.setValueChangeMode(ValueChangeMode.EAGER);

        maxLengthField.setLabel(StringConstants.RND_COMP_CHARACTERS_MAX_FIELD_NAME);
        maxLengthField.setWidth("500px");
        maxLengthField.setValueChangeMode(ValueChangeMode.EAGER);

        HorizontalLayout inputFieldLayout = new HorizontalLayout();
        inputFieldLayout.add(minLengthField, maxLengthField);

        add(title, select, infoSpan, inputFieldLayout);
    }

    @Override
    protected void binderSetup() {
        Binder<CharactersRndData> binderSelect = new Binder<>();
        Binder<CharactersRndData> binderInputFields = new Binder<>();

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
    private void loadValuesSetupSelect(Binder<CharactersRndData> binder) {
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
            optionData.setMinLength(minV);
            minLengthField.setValue(minV);
            optionData.setMaxLength(maxV);
            maxLengthField.setValue(maxV);

            optionData.setShowInitialBounds(false);

        } else {

            // binder.readBean() gets not used because it triggers validation!
            if(optionData.getMinLength() != null) {
                minLengthField.setValue(optionData.getMinLength());
            }
            if(optionData.getMaxLength() != null) {
                maxLengthField.setValue(optionData.getMaxLength());
            }
        }
    }

    /**
     * Select: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsSelect(Binder<CharactersRndData> binder) {
        binder.bind(select,
                rndData -> {
                    CharactersRndData.CharactersDropdownElement selection = rndData.getCharactersDropdownElement();
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    return selection;
                },
                (rndData, selection) -> {
                    infoSpan.setText(selection.getInfoText());
                    updateComponentsVisibility(selection);
                    rndData.setCharactersDropdownElement(selection);
                });
    }

    /**
     * Sets the visibility of the components depending on the selected dropdown element.
     */
    private void updateComponentsVisibility(CharactersRndData.CharactersDropdownElement selection) {
        switch (selection) {
            case SAME_LENGTH:
                minLengthField.setVisible(false);
                maxLengthField.setVisible(false);
                break;
            case USER_DEFINED:
                minLengthField.setVisible(true);
                maxLengthField.setVisible(true);
                break;
        }
    }

    /**
     * Input fields: Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFieldsInputFields(Binder<CharactersRndData> binder) {
        binder.forField(minLengthField)
                .asRequired(StringConstants.RND_COMP_CHARACTERS_REQUIRED_MIN)
                .withValidator(new IntegerRangeValidator(String.format(StringConstants.RND_COMP_CHARACTERS_VALIDATOR_RANGE, attribute.getPrecision()),
                        1, attribute.getPrecision()))
                .bind(CharactersRndData::getMinLength, CharactersRndData::setMinLength);

        binder.forField(maxLengthField)
                .asRequired(StringConstants.RND_COMP_CHARACTERS_REQUIRED_MAX)
                .withValidator(new IntegerRangeValidator(String.format(StringConstants.RND_COMP_CHARACTERS_VALIDATOR_RANGE, attribute.getPrecision()),
                        1, attribute.getPrecision()))
                .withValidator(maxInteger -> {
                    Integer minInteger = minLengthField.getValue();
                    if(minInteger != null) {
                        return maxInteger.compareTo(minInteger) >= 0;
                    }
                    return false;
                }, StringConstants.RND_COMP_CHARACTERS_VALIDATOR_MAX_AFTER_MIN)
                .withValidator(maxInteger -> {
                    if(attribute.isUnique()) {
                        Integer minInteger = minLengthField.getValue();
                        if(minInteger != null) {
                            int possibleCharacters = 36;
                            if(maxInteger.equals(minInteger)) {
                                // Kombinatorik: n hoch k
                                return Math.pow(possibleCharacters, minInteger) >= recordCount;
                            } else {
                                BigInteger possibleValues = BigInteger.ZERO;
                                for(int i = minInteger; i <= maxInteger; i++) {
                                    possibleValues = possibleValues.add(BigInteger.valueOf((int)Math.pow(possibleCharacters, i)));
                                }
                                return possibleValues.compareTo(BigInteger.valueOf(recordCount)) >= 0;
                            }
                        }
                        return false;
                    }
                    return true;
                }, StringConstants.RND_COMP_CHARACTERS_VALIDATOR_UNIQUE)
                .bind(CharactersRndData::getMaxLength, CharactersRndData::setMaxLength);
    }

    /**
     * Select: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupSelect(Binder<CharactersRndData> binder) {
        select.addValueChangeListener(event -> binder.writeBeanIfValid(optionData));
    }

    /**
     * Input fields: Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetupInputFields(Binder<CharactersRndData> binder) {
        minLengthField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMinLength(null);
            }
        });

        maxLengthField.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setMaxLength(null);
            }
        });
    }
}