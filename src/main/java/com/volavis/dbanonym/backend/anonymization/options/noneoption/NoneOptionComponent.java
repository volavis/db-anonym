package com.volavis.dbanonym.backend.anonymization.options.noneoption;

import com.vaadin.flow.component.dependency.CssImport;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;

/**
 * GUI of the anonymization option "none".
 */
@CssImport("./styles/components/anonymizationoptions/none-option-component.css")
public class NoneOptionComponent extends OptionComponent {

    /**
     * Constructor.
     */
    public NoneOptionComponent() {
        super();
        add(buildOptionInfoLayout(StringConstants.NONE_COMP_OPTION_INFO));
    }
}