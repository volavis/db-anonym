package com.volavis.dbanonym.backend.anonymization.options;

import com.volavis.dbanonym.backend.anonymization.additionaldata.AnonymizationDataID;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.constoption.ConstOptionData;
import com.volavis.dbanonym.backend.anonymization.options.constoption.components.*;
import com.volavis.dbanonym.backend.anonymization.options.constoption.data.*;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionComponent;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.components.*;
import com.volavis.dbanonym.backend.anonymization.options.rndoption.data.*;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Enum for the supported GUI IDs/groups.
 * <P>GuiIDs group JavaDataTypes together, e.g. short, int, long, can have the same GUI (= INTEGER).
 */
public enum GuiID {

    CHARACTERS {
        @Override
        public ConstOptionData getConstOptionData() {
            return new CharactersConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new CharactersConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new CharactersRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new CharactersRndGui(option, entity, attribute);
        }

        @Override
        public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
            CharactersRndData optionData = (CharactersRndData) attribute.getAnonymizationOptionData();
            if(optionData.getCharactersDropdownElement() == CharactersRndData.CharactersDropdownElement.SAME_LENGTH) {
                return Collections.singletonList(AnonymizationDataID.LENGTH);
            }
            return null;
        }
    },
    INTEGER {
        @Override
        public ConstOptionData getConstOptionData() {
            return new IntegerConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new IntegerConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new IntegerRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new IntegerRndGui(option, entity, attribute);
        }

        @Override
        public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
            List<AnonymizationDataID> dataIDList = new ArrayList<>();
            IntegerRndData optionData = (IntegerRndData) attribute.getAnonymizationOptionData();
            if(optionData.getIntegerDropdownElement() == IntegerRndData.IntegerDropdownElement.SAME_DIGIT_COUNT) {
                dataIDList.add(AnonymizationDataID.LENGTH);
                if(optionData.useSign()) {
                    dataIDList.add(AnonymizationDataID.SIGN);
                }
            }
            return dataIDList;
        }
    },
    DECIMAL {
        @Override
        public ConstOptionData getConstOptionData() {
            return new DecimalConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new DecimalConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new DecimalRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new DecimalRndGui(option, entity, attribute);
        }

        @Override
        public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
            DecimalRndData optionData = (DecimalRndData) attribute.getAnonymizationOptionData();
            if(optionData.getDecimalDropdownElement() == DecimalRndData.DecimalDropdownElement.SAME_PRE_POST_DECIMAL_PLACES) {
                return Collections.singletonList(AnonymizationDataID.DECIMAL);
            }
            return null;
        }
    },
    BINARY {
        @Override
        public ConstOptionData getConstOptionData() {
            return new BinaryConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new BinaryConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new BinaryRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new BinaryRndGui(option, entity, attribute);
        }

        @Override
        public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
            BinaryRndData optionData = (BinaryRndData) attribute.getAnonymizationOptionData();
            if(optionData.getBinaryDropdownElement() == BinaryRndData.BinaryDropdownElement.SAME_BYTE_COUNT) {
                return Collections.singletonList(AnonymizationDataID.LENGTH);
            }
            return null;
        }
    },
    BOOLEAN {
        @Override
        public ConstOptionData getConstOptionData() {
            return new BooleanConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new BooleanConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new BooleanRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new BooleanRndGui(option, entity, attribute);
        }
    },
    DATE {
        @Override
        public ConstOptionData getConstOptionData() {
            return new DateConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new DateConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new DateRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new DateRndGui(option, entity, attribute);
        }
    },
    TIME {
        @Override
        public ConstOptionData getConstOptionData() {
            return new TimeConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new TimeConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new TimeRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new TimeRndGui(option, entity, attribute);
        }
    },
    TIMESTAMP {
        @Override
        public ConstOptionData getConstOptionData() {
            return new TimestampConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new TimestampConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new TimestampRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new TimestampRndGui(option, entity, attribute);
        }
    },
    UNKNOWN,
    MYSQL_ENUM {
        @Override
        public ConstOptionData getConstOptionData() {
            return new MysqlEnumConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new MysqlEnumConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new MysqlEnumRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new MysqlEnumRndGui(entity, attribute);
        }
    },
    ORACLE_CHARACTERS {
        @Override
        public ConstOptionData getConstOptionData() {
            return new CharactersConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new OracleCharactersConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return new CharactersRndData();
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new CharactersRndGui(option, entity, attribute);
        }

        @Override
        public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
            CharactersRndData optionData = (CharactersRndData) attribute.getAnonymizationOptionData();
            if(optionData.getCharactersDropdownElement() == CharactersRndData.CharactersDropdownElement.SAME_LENGTH) {
                return Collections.singletonList(AnonymizationDataID.LENGTH);
            }
            return null;
        }
    },
    ORACLE_DECIMAL {
        @Override
        public ConstOptionData getConstOptionData() {
            return new DecimalConstData();
        }

        @Override
        public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
            return new OracleDecimalConstGui(option, entity, attribute);
        }

        @Override
        public RndOptionData getRndOptionData() {
            return null;
        }

        @Override
        public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
            return new OracleDecimalRndGui(option, entity, attribute);
        }

        @Override
        public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
            DecimalRndData optionData = (DecimalRndData) attribute.getAnonymizationOptionData();
            if(optionData.getDecimalDropdownElement() == DecimalRndData.DecimalDropdownElement.SAME_PRE_POST_DECIMAL_PLACES) {
                return Collections.singletonList(AnonymizationDataID.DECIMAL);
            }
            return null;
        }
    };

    /**
     * Returns a subclass of ConstOptionData that fits the GuiID.
     * @return Object that saves the user input.
     */
    public ConstOptionData getConstOptionData() {
        return null;
    }

    /**
     * Returns a subclass of ConstOptionComponent that fits the GuiID.
     * @param option Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     * @return Object that builds the GUI.
     */
    public ConstOptionComponent getConstOptionComponent(ConstOptionData option, Entity entity, Attribute attribute) {
        return new UnknownConstGui(entity, attribute);
    }

    /**
     * Returns a subclass of RndOptionData that fits the GuiID.
     * @return Object that saves the user input.
     */
    public RndOptionData getRndOptionData() {
        return null;
    }

    /**
     * Returns a subclass of RndOptionComponent that fits the GuiID.
     * @param option Data class that saves the user input.
     * @param entity Entity the attribute belongs to.
     * @param attribute The GUI will be build based on the attribute's properties.
     * @return Object that builds the GUI.
     */
    public RndOptionComponent getRndOptionComponent(RndOptionData option, Entity entity, Attribute attribute) {
        return new UnknownRndGui(entity, attribute);
    }

    /**
     * List containing which additional metadata must be retrieved from the database for the anonymization.
     * @param attribute Used to refine the selection.
     * @return List of AnonymizationDataIDs.
     */
    public List<AnonymizationDataID> getRndAnonymizationDataIDList(Attribute attribute) {
        return null;
    }
}