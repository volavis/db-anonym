package com.volavis.dbanonym.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;

/**
 * Simple horizontal line component.
 */
@Tag("vaadin-line")
public class Line extends Div {

    /**
     * Constructor.
     */
    public Line() {
        getStyle().set("width","100%").set("border-top","2px solid");
    }
}
