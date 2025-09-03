package com.volavis.dbanonym.backend.anonymization.options.nulloption;

import com.vaadin.flow.component.dependency.CssImport;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;

/**
 * GUI of the anonymization option "null".
 */
@CssImport("./styles/components/anonymizationoptions/null-option-component.css")
public class NullOptionComponent extends OptionComponent {

    /**
     * Constructor.
     */
    public NullOptionComponent() {
        super();
        add(buildOptionInfoLayout(StringConstants.NULL_COMP_OPTION_INFO));
    }
}