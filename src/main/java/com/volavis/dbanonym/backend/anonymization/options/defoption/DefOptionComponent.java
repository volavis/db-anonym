package com.volavis.dbanonym.backend.anonymization.options.defoption;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.OptionComponent;
import com.volavis.dbanonym.backend.database.Attribute;

/**
 * GUI of the anonymization option "default".
 */
@CssImport("./styles/components/anonymizationoptions/def-option-component.css")
public class DefOptionComponent extends OptionComponent {

    /**
     * Constructor.
     * @param attribute The GUI will be build based on the attribute's properties.
     */
    public DefOptionComponent(Attribute attribute) {
        super();

        add(buildOptionInfoLayout(StringConstants.DEF_COMP_OPTION_INFO));

        HorizontalLayout layout = new HorizontalLayout();
        Div defaultValue = new Div();
        defaultValue.setId("default-value");
        defaultValue.add(attribute.getDefaultValue());
        layout.add(new Span(StringConstants.DEFAULT_VALUE + ": "), defaultValue);
        add(layout);
    }
}