package com.volavis.dbanonym.backend.anonymization.options.constoption.components;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.MysqlEnumConstData;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.attributes.rdbmsspecific.MysqlEnumAttribute;

import java.util.List;

/**
 * Builds the (const) GUI for the GuiID Mysql_Enum.
 */
public class MysqlEnumConstGui extends ConstOptionComponent {

    private final MysqlEnumConstData optionData;
    private final List<String> enumList;

    private final Select<String> select = new Select<>();

    /**
     * Constructor.
     * @param optionData Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public MysqlEnumConstGui(ConstOptionData optionData, Entity entity, Attribute attribute) {
        super(entity, attribute);
        this.optionData = (MysqlEnumConstData) optionData;

        MysqlEnumAttribute enumAttribute = (MysqlEnumAttribute) attribute;
        enumList = enumAttribute.getEnumValues();

        buildComponentLayout();
        binderSetup();
    }

    @Override
    protected void buildComponentLayout() {
        Span title = new Span(StringConstants.CONST_COMP_MYSQL_ENUM_TITLE);

        select.setWidth("500px");
        select.setItems(enumList);

        add(title, select);
    }

    @Override
    protected void binderSetup() {
        Binder<MysqlEnumConstData> binder = new Binder<>();

        bindFields(binder);
        saveValuesSetup(binder);
        loadValuesSetup(binder);
    }

    /**
     * Binds input fields.
     * @param binder Binder the fields will be bound to.
     */
    private void bindFields(Binder<MysqlEnumConstData> binder) {
        binder.bind(select, MysqlEnumConstData::getEnumValue, MysqlEnumConstData::setEnumValue);
    }

    /**
     * Defines how and when the user input will be saved.
     * @param binder Binder that manages the input fields.
     */
    private void saveValuesSetup(Binder<MysqlEnumConstData> binder) {
        select.addValueChangeListener(event -> {
            if (!binder.writeBeanIfValid(optionData)) {
                optionData.setEnumValue(null);
            }
        });
    }

    /**
     * Loads previous user input.
     * @param binder Binder that manages the input fields.
     */
    private void loadValuesSetup(Binder<MysqlEnumConstData> binder) {
        if (optionData.getEnumValue() != null) {
            binder.readBean(optionData);

        } else {
            if(!enumList.isEmpty()) {
                String firstValue = enumList.get(0);
                select.setValue(firstValue);
                optionData.setEnumValue(firstValue);
            }
        }
    }
}