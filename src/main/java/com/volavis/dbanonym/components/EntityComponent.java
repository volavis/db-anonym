package com.volavis.dbanonym.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.options.OptionData;
import com.volavis.dbanonym.backend.commonviewdata.EntityComponentData;
import com.volavis.dbanonym.backend.commonviewdata.EntityComponentUtil;
import com.volavis.dbanonym.backend.commonviewdata.SelectedAttribute;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Component that represents a table/entity. It displays the metadata of the table and the table's attributes.
 * Each attribute has a button that allows the user to choose an anonymization option.
 */
@CssImport("./styles/components/entity-component.css")
@CssImport(value = "./styles/components/entity-component-shadowdom.css", themeFor = "vaadin-details")
public class EntityComponent extends Composite<Details> {

    private final Entity entity;
    private final SelectedAttribute selectedAttribute;
    private final EntityComponentData entityComponentData;

    private Grid<Attribute> grid;

    /** Id of the HTML element. Used inorder to scroll to the EntityComponent. Id set in TablesView. */
    private String htmlID;

    /**
     * Constructor.
     * @param entity Entity the EntityComponent will represent.
     * @param selectedAttribute Attribute the user wants to anonymize.
     * @param entityComponentData Keeps track of opened and current entity components.
     */
    public EntityComponent(Entity entity, SelectedAttribute selectedAttribute,
                           EntityComponentData entityComponentData) {

        this.entity = entity;
        this.selectedAttribute = selectedAttribute;
        this.entityComponentData = entityComponentData;

        // Details component
        HorizontalLayout summaryLayout = createDetailsSummary(entity);
        getContent().setSummary(summaryLayout);
        getContent().addThemeVariants(DetailsVariant.FILLED, DetailsVariant.REVERSE);
        getContent().addOpenedChangeListener(event -> {
            if (grid == null) {
                createGrid();
                HorizontalLayout infoLayout = createEntityInformation(entity);
                getContent().addContent(grid, infoLayout);
            }
        });
    }

    /**
     * Creates the Details component's summary layout.
     */
    private HorizontalLayout createDetailsSummary(Entity entity) {

        HorizontalLayout summaryLayout = new HorizontalLayout();
        summaryLayout.setWidthFull();
        summaryLayout.setSpacing(false);
        summaryLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        summaryLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Span entityNameSpan = new Span(entity.getName());
        entityNameSpan.addClassName("entity-name-span");
        summaryLayout.add(entityNameSpan);

        int optionCount = countAnonymizationOptions();
        if (optionCount == 1) {
            summaryLayout.add(new Span(optionCount + " " + StringConstants.OPTION));
        } else if (optionCount > 1) {
            summaryLayout.add(new Span(optionCount + " " + StringConstants.OPTIONS));
        }

        return summaryLayout;
    }

    /**
     * Creates the grid showing the entity's attributes.
     */
    private void createGrid() {

        grid = new Grid<>();
        grid.setHeightByRows(true);
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_COLUMN_BORDERS, GridVariant.LUMO_COMPACT);

        // Sort attributes
        Collection<Attribute> attributeCollection = entity.getAttributes().values();
        ArrayList<Attribute> attributeList = attributeCollection.stream()
                .sorted(Comparator.comparing(Attribute::getName))
                .collect(Collectors.toCollection(ArrayList::new));
        grid.setItems(attributeList);

        setGridColumns();
    }

    /**
     * Sets the data and header for each column.
     */
    private void setGridColumns() {
        String htmlHeader = "<div style='font-weight: bold;'>%s</div>";

        grid.addColumn(this::createKeyInfoStr)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.KEY)))
                .setFlexGrow(0).setWidth("65px");
        grid.addColumn(Attribute::getName)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.NAME)))
                .setSortable(true);
        grid.addColumn(attribute -> attribute.getDatabaseDataType().toUpperCase())
                .setHeader(new Html(String.format(htmlHeader, StringConstants.DB_DATA_TYPE)))
                .setSortable(true);
        /*grid.addColumn(attribute -> {
            try {
                return JDBCType.valueOf(attribute.getJdbcDataType());
            } catch(IllegalArgumentException exception) {
                return attribute.getJdbcDataType();
            }
        })
                .setHeader(new Html(String.format(htmlHeader, "JDBC-Typ")))
                .setSortable(true);*/
        grid.addComponentColumn(this::createJavaType)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.JAVA_DATA_TYPE)))
                .setSortable(true)
                .setComparator(attribute -> attribute.getJavaDataType().toString());
        grid.addColumn(Attribute::getDataTypeInformation)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.ENTITY_COMPONENT_TYPE_INFO)));
        grid.addColumn(attribute -> attribute.isNullable() ? StringConstants.YES : StringConstants.NO)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.NULLABLE)))
                .setFlexGrow(0).setWidth("70px");
        grid.addColumn(attribute -> attribute.isUnique() ? StringConstants.YES : StringConstants.NO)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.UNIQUE)))
                .setFlexGrow(0).setWidth("65px");
        grid.addColumn(attribute -> attribute.getDefaultValue() != null ? StringConstants.YES : StringConstants.NO)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.DEFAULT)))
                .setFlexGrow(0).setWidth("70px");
        grid.addComponentColumn(this::createAnonymizationButton)
                .setHeader(new Html(String.format(htmlHeader, StringConstants.ENTITY_COMPONENT_ANONYMIZATION)));
    }

    /**
     * Creates the entity information at the bottom of the Details component.
     */
    private HorizontalLayout createEntityInformation(Entity entity) {
        HorizontalLayout infoLayout = new HorizontalLayout();
        Span infoTitle = new Span(StringConstants.ENTITY_COMPONENT_INFO + ": ");
        Span attributeCount = new Span(entity.getAttributes().size() + " " + StringConstants.ATTRIBUTES);
        Span pkCount = new Span(countPK() + " " + StringConstants.PK);
        Span fkCount = new Span(countFK() + " " + StringConstants.FK);
        infoLayout.add(infoTitle, attributeCount, pkCount, fkCount);
        return infoLayout;
    }

    /**
     * Returns information about an attribute's key.
     * @param attribute Attribute of current row.
     * @return Information if attribute is a pk, fk or both.
     */
    private String createKeyInfoStr(Attribute attribute) {
        String keyInfo = "";
        if (attribute.isPK() && attribute.getForeignKey() != null) {
            keyInfo = StringConstants.PK + "/" + StringConstants.FK;
        } else {
            if (attribute.isPK()) {
                keyInfo = StringConstants.PK;
            } else if (attribute.getForeignKey() != null) {
                keyInfo = StringConstants.FK;
            }
        }
        return keyInfo;
    }

    /**
     * Returns the name of an attribute's Java data type.
     * @param attribute Attribute of current row.
     * @return The Java data type name. If the database type is unknown the fallback JDBC Java data type gets used and a warning sign appears.
     */
    private HorizontalLayout createJavaType(Attribute attribute) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidthFull();
        horizontalLayout.setSpacing(false);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.add(new Span(attribute.getJavaDataType().toString()));

        if (attribute.useJavaDataTypeFallback()) {
            Button warningButton = new Button(new Icon(VaadinIcon.WARNING));
            warningButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
            warningButton.addClickListener(event -> {
                Notification notification = new Notification(StringConstants.ENTITY_COMPONENT_JAVA_FALLBACK_WARNING, 9000);
                notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
                notification.open();
            });

            horizontalLayout.add(warningButton);
        }
        return horizontalLayout;
    }

    /**
     * Creates the anonymization button.
     * @param attribute Attribute of current row.
     * @return The anonymization button. If the user chose an option, the option's name is displayed.
     */
    private HorizontalLayout createAnonymizationButton(Attribute attribute) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        if (!attribute.isPK() && attribute.getForeignKey() == null) {
            Button button = new Button(new Icon(VaadinIcon.WRENCH));
            button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button.addClickListener(e -> {
                selectedAttribute.update(attribute, entity);
                entityComponentData.setCurrentEntityComponentId(htmlID);
                button.getUI().ifPresent(ui -> ui.navigate("anonymization"));
            });
            layout.add(button);

            Div optionDiv;
            OptionData option = attribute.getAnonymizationOptionData();
            if (option != null) {
                optionDiv = new Div();
                optionDiv.setText(option.getOptionID().toString());
                layout.add(optionDiv);
            }
        } else {
            // Useless hidden button - row is supposed to have the same height with or without button!
            Button emptyButton = new Button(new Icon(VaadinIcon.WRENCH));
            emptyButton.getStyle().set("visibility", "hidden");
            layout.add(emptyButton);
        }

        return layout;
    }

    /**
     * Number of anonymization options of the table's attributes.
     */
    private int countAnonymizationOptions() {
        int count = 0;
        for (Attribute attribute : entity.getAttributes().values()) {
            if (attribute.getAnonymizationOptionData() != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Number of pk in the table.
     */
    private int countPK() {
        int count = 0;
        for (Attribute attribute : entity.getAttributes().values()) {
            if (attribute.isPK()) {
                count++;
            }
        }
        return count;
    }

    /**
     * Number of fk in the table.
     */
    private int countFK() {
        int count = 0;
        for (Attribute attribute : entity.getAttributes().values()) {
            if (attribute.getForeignKey() != null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Checks if the details component is opened.
     */
    public boolean isOpen() {
        return getContent().isOpened();
    }

    /**
     * Opens the details component.
     */
    public void open() {
        getContent().setOpened(true);
    }

    /**
     * Closes the details component.
     */
    public void close() {
        getContent().setOpened(false);
    }

    /**
     * Sets the HTML id of the details component. A prefix will be added to the id.
     */
    public void setHtmlID(String htmlID) {
        String completeID = EntityComponentUtil.PREFIX + htmlID.trim();
        this.htmlID = completeID;
        getContent().setId(completeID);
    }

    public Entity getEntity() {
        return entity;
    }

    public String getHtmlID() {
        return htmlID;
    }
}