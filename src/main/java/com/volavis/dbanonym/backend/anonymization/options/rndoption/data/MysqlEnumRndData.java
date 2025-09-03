package com.volavis.dbanonym.backend.anonymization.options.rndoption.data;

import com.volavis.dbanonym.backend.anonymization.options.rndoption.RndOptionData;

/**
 * Saves the (rnd) GUI user input for the GuiID MYSQL_ENUM.
 */
public class MysqlEnumRndData extends RndOptionData {

    @Override
    public boolean isValid() {
        return true;
    }
}