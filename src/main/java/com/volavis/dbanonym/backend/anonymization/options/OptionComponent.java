package com.volavis.dbanonym.backend.anonymization.options;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.database.Attribute;

import java.util.Set;

/**
 * Superclass for all the AnonymizationOptions GUI classes.
 */
public abstract class OptionComponent extends VerticalLayout {

    /**
     * Constructor.
     */
    public OptionComponent() {
        setWidthFull();
        setHeightFull();
    }

    /**
     * Builds the general information (at the top) about the AnonymizationOption.
     * @param information Information that will be displayed.
     */
    protected HorizontalLayout buildOptionInfoLayout(String information) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(new Icon(VaadinIcon.INFO_CIRCLE_O), new Span(information));
        return layout;
    }

    /**
     * Builds a table with possible check constraints.
     * @param attribute Attribute which holds the check constraints.
     */
    protected VerticalLayout buildCheckConstraintsLayout(Attribute attribute) {
        Set<String> constraintsSet = attribute.getPossibleCheckConstraints();

        if(constraintsSet.isEmpty()) {
            return null;
        } else {
            VerticalLayout layout = new VerticalLayout();
            layout.setPadding(false);

            Grid<String> grid = new Grid<>();
            grid.setItems(constraintsSet);
            grid.setHeightByRows(true);
            grid.setSelectionMode(Grid.SelectionMode.NONE);
            grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);
            String htmlHeader = "<div style='font-weight: bold;'>%s</div>";
            grid.addColumn(item -> item).setHeader(new Html(String.format(htmlHeader, StringConstants.CHECK_CONSTRAINTS)));

            layout.add(new Span(StringConstants.CHECK_CONSTRAINTS_GRID_TITLE), grid);
            return layout;
        }
    }
}