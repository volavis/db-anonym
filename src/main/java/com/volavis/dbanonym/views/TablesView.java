package com.volavis.dbanonym.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.anonymization.AnonymizationThread;
import com.volavis.dbanonym.backend.commonviewdata.EntityComponentData;
import com.volavis.dbanonym.backend.commonviewdata.EntityComponentUtil;
import com.volavis.dbanonym.backend.commonviewdata.SelectedAttribute;
import com.volavis.dbanonym.backend.database.*;
import com.volavis.dbanonym.components.EntityComponent;
import com.volavis.dbanonym.components.Line;
import com.volavis.dbanonym.components.dialogs.AnonymizationDialog;
import com.volavis.dbanonym.components.dialogs.AnonymizationFeedbackDialog;
import com.volavis.dbanonym.components.dialogs.ProgressBarDialog;
import com.volavis.dbanonym.components.dialogs.ValidationErrorDialog;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Displays the database's metadata (tables, attributes, data types, etc.).
 * <P>The user can decide which attribute to anonymize.
 * <P>The button at the top allows the user to start the anonymization.
 */
@Route(value = StringConstants.TABLES_VIEW_ROUTE, layout = MainView.class)
@PageTitle(StringConstants.TABLES_VIEW_PAGE_TITLE)
@CssImport("./styles/views/tables-view.css")
public class TablesView extends VerticalLayout implements BeforeEnterObserver, AfterNavigationObserver,
        BeforeLeaveObserver {

    private final ConnectedDatabase database;
    private final SelectedAttribute selectedAttribute;
    private final EntityComponentData entityComponentData;

    private final Button executeButton = new Button();
    private final Span databaseNameSpan = new Span();
    private final Span entityCountSpan = new Span();
    private final Span attributeCountSpan = new Span();
    private final Button closeAllEntityComponentsButton = new Button();
    private final Button openAllEntityComponentsButton = new Button();
    private final Div entityComponentContainer = new Div();

    private final ValidationErrorDialog validationErrorDialog;
    private final AnonymizationDialog anonymizationDialog = new AnonymizationDialog();
    private final ProgressBarDialog progressBarDialog = new ProgressBarDialog();
    private final AnonymizationFeedbackDialog anonymizationFeedbackDialog = new AnonymizationFeedbackDialog();

    private final List<EntityComponent> entityComponentList = new ArrayList<>();

    /** List of (simple)attributes that will be anonymized. Attributes get removed if invalid or unselected by user. */
    private List<SimpleAttribute> attributesToBeProcessed = new ArrayList<>();


    /**
     * Constructor.
     * @param database Database the user connected to. (autowired)
     * @param entityComponentData Keeps track of opened and current entity components. (autowired)
     * @param selectedAttribute Attribute the user wants to anonymize. (autowired)
     */
    @Autowired
    public TablesView(ConnectedDatabase database, EntityComponentData entityComponentData,
                      SelectedAttribute selectedAttribute) {

        this.database = database;
        this.entityComponentData = entityComponentData;
        this.selectedAttribute = selectedAttribute;

        setId("tables-view");

        HorizontalLayout topLayout = createTopLayout();
        HorizontalLayout actionsLayout = createActionsLayout();

        entityComponentContainer.setWidthFull();
        entityComponentContainer.setHeightFull();

        Button scrollToTopButton = new Button(new Icon(VaadinIcon.CHEVRON_UP));
        scrollToTopButton.setId("scroll-to-top-button");
        scrollToTopButton.addClickListener(event -> this.getElement().callJsFunction("scrollIntoView"));

        add(topLayout, new Line(), actionsLayout, entityComponentContainer, scrollToTopButton);

        validationErrorDialog = new ValidationErrorDialog(database, selectedAttribute, entityComponentData);

        eventListenerSetup();
    }

    /**
     * Creates the top layout which consists out of the "start anonymization" button and some database information.
     */
    private HorizontalLayout createTopLayout() {

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthFull();
        topLayout.setSpacing(false);
        topLayout.setAlignItems(Alignment.CENTER);
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        executeButton.setText(StringConstants.TABLES_VIEW_EXECUTE_BUTTON);
        executeButton.setIcon(new Icon(VaadinIcon.COGS));
        executeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(executeButton);

        HorizontalLayout infoLayout = new HorizontalLayout();
        databaseNameSpan.setId("schema-name");
        infoLayout.add(databaseNameSpan, entityCountSpan, attributeCountSpan);

        topLayout.add(buttonLayout, infoLayout);

        return topLayout;
    }

    /**
     * Creates the actions layout. The actions affect the entity components.
     * <P>E.g. Close, open all components; Search for a component.
     */
    private HorizontalLayout createActionsLayout() {

        HorizontalLayout actionsLayout = new HorizontalLayout();
        actionsLayout.setId("entity-actions");
        actionsLayout.setWidthFull();
        actionsLayout.setSpacing(false);
        actionsLayout.setAlignItems(Alignment.CENTER);
        actionsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        HorizontalLayout actionsLayoutLeft = createOpenCloseButtonsLayout();
        TextField entitySearchbar = createSearchbar();
        actionsLayout.add(actionsLayoutLeft, entitySearchbar);

        return actionsLayout;
    }

    /**
     * Creates the layout containing the close/open all entity components buttons.
     */
    private HorizontalLayout createOpenCloseButtonsLayout() {

        HorizontalLayout actionsLayoutLeft = new HorizontalLayout();
        actionsLayoutLeft.setAlignItems(Alignment.CENTER);
        Span buttonsTitleSpan = new Span(StringConstants.TABLES_VIEW_ENTITY_COMPONENTS_LAYOUT_TITLE);
        closeAllEntityComponentsButton.setText(StringConstants.CLOSE);
        closeAllEntityComponentsButton.setWidth("100px");
        closeAllEntityComponentsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        openAllEntityComponentsButton.setText(StringConstants.OPEN);
        openAllEntityComponentsButton.setWidth("100px");
        openAllEntityComponentsButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
        actionsLayoutLeft.add(buttonsTitleSpan, closeAllEntityComponentsButton, openAllEntityComponentsButton);

        return actionsLayoutLeft;
    }

    /**
     * Creates the entity component searchbar.
     */
    private TextField createSearchbar() {

        Button searchButton = new Button(new Icon(VaadinIcon.SEARCH));
        searchButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        TextField entitySearchField = new TextField();
        entitySearchField.setWidth("500px");
        entitySearchField.setPlaceholder(StringConstants.TABLES_VIEW_ENTITY_COMPONENTS_SEARCHBAR);
        entitySearchField.setPrefixComponent(searchButton);
        entitySearchField.setClearButtonVisible(true);

        entitySearchField.addKeyDownListener(Key.ENTER, event -> EntityComponentUtil.
                scrollToEntityComponentByName(entityComponentList, entitySearchField.getValue(), true, true));
        searchButton.addClickListener(event -> EntityComponentUtil.
                scrollToEntityComponentByName(entityComponentList, entitySearchField.getValue(), true, true));

        return entitySearchField;
    }

    /**
     * Setup: Event handling for buttons and dialogs.
     */
    private void eventListenerSetup() {

        setupOpenCloseAllButtons();

        // Execute anonymization button
        executeButton.addClickListener( event -> validate());

        // ValidationErrorDialog: ignore
        validationErrorDialog.getIgnoreButton().addClickListener( event -> {
            anonymizationDialog.overviewMode();
            anonymizationDialog.buildAccordion(attributesToBeProcessed);
            anonymizationDialog.open();
            validationErrorDialog.close();
        });

        // AnonymizationDialog: anonymize everything
        anonymizationDialog.getAnonymizeEverythingButton().addClickListener( event -> {
            anonymizationDialog.close();
            anonymize();
        });

        // AnonymizationDialog: anonymize selection
        anonymizationDialog.getAnonymizeSelectionButton().addClickListener( event -> anonymizationDialog.selectionMode());

        // AnonymizationDialog: confirm
        anonymizationDialog.getConfirmSelectionButton().addClickListener( event -> {
            attributesToBeProcessed = anonymizationDialog.getSelectedAttributesList();
            anonymizationDialog.close();
            anonymize();
        });

        // Close buttons
        validationErrorDialog.getCancelButton().addClickListener( event -> validationErrorDialog.close());
        anonymizationDialog.getCancelButton().addClickListener( event -> anonymizationDialog.close());
        anonymizationFeedbackDialog.getCloseButton().addClickListener(event -> anonymizationFeedbackDialog.close());
    }

    /**
     * Click listener setup for the open/close all entity components buttons.
     */
    private void setupOpenCloseAllButtons() {

        // Close all EntityComponents
        closeAllEntityComponentsButton.addClickListener( event -> {
            for (EntityComponent entityComponent : entityComponentList) {
                entityComponent.close();
            }
        });

        // Open all EntityComponents
        openAllEntityComponentsButton.addClickListener( event -> {
            for (EntityComponent entityComponent : entityComponentList) {
                entityComponent.open();
            }
        });
    }

    /**
     * Checks if the user is connected to a database (if metadata exists). If not, navigates to ConnectView.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (database == null || database.getMetadata() == null || database.getMetadata().getEntities().isEmpty()) {
            event.forwardTo(ConnectView.class);
            Notification notification = new Notification(StringConstants.WARNING_NO_DB_CONNECTION, 5000);
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }

    /**
     * Uses the metadata to insert information texts and to build the EntityComponents (representing a table with attributes).
     * Opens EntityComponents that were opened last time and scrolls to the current EntityComponent.
     */
    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        insertDatabaseInformation();
        entityComponentsOperations();
    }

    /**
     * Inserts database information into the top layout Spans.
     */
    private void insertDatabaseInformation() {

        databaseNameSpan.setText(database.getMetadata().getDatabaseName());

        int entityCount = database.getMetadata().getEntities().size();
        entityCountSpan.setText(entityCount + " " + (entityCount == 1 ? StringConstants.TABLE : StringConstants.TABLES));

        int attributeCount = countAttributes();
        attributeCountSpan.setText(attributeCount + " " + (attributeCount == 1 ? StringConstants.ATTRIBUTE : StringConstants.ATTRIBUTES));
    }

    /**
     * Operations on entity components (build, insert, open and scroll operations).
     */
    private void entityComponentsOperations() {
        if(entityComponentData != null) {

            EntityComponentUtil.buildEntityComponentList(entityComponentList, selectedAttribute, entityComponentData, database.getMetadata().getEntities());

            entityComponentContainer.removeAll();
            for (EntityComponent entityComponent : entityComponentList) {
                entityComponentContainer.add(entityComponent);
            }

            EntityComponentUtil.openEntityComponentsAgain(entityComponentList, entityComponentData);

            getUI().ifPresent(ui -> {
                PendingJavaScriptResult loadingFinished = ui.getPage().executeJs(EntityComponentUtil.JS_PAGE_FINISHED_LOADING);
                loadingFinished.then(Boolean.class, (res) -> {
                    String id = entityComponentData.getCurrentEntityComponentId();
                    EntityComponentUtil.scrollToEntityComponentById(entityComponentList, id, false, false);
                });
            });
        }
    }

    /**
     * Saves which EntityComponents are currently open.
     */
    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        if(entityComponentData != null) {
            EntityComponentUtil.saveOpenedEntityComponents(entityComponentList, entityComponentData);
        }
    }

    /**
     * Validation before the anonymization. Checks if fields are empty or invalid (if their values are null).
     */
    private void validate() {

        attributesToBeProcessed.clear();

        // Fill attributesToBeProcessed
        attributesToBeProcessed = getAttributesWithAnonymizationOption();

        if(attributesToBeProcessed.isEmpty()) {
            Notification notification = new Notification(StringConstants.TABLES_VIEW_NO_ANONYMIZATION_OPTIONS, 5000);
            notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_ERROR);
            notification.open();
        } else {
            List<SimpleAttribute> invalidAttributesList = new OptionValidator().validate(database.getMetadata().getEntities());
            attributesToBeProcessed.removeAll(invalidAttributesList);
            processInvalidAttributesList(invalidAttributesList);
        }
    }

    /**
     * If there are invalid attributes the ValidationErrorDialog gets opened otherwise the AnonymizationDialog.
     * @param invalidAttributesList List of invalid (simple)attributes.
     */
    private void processInvalidAttributesList(List<SimpleAttribute> invalidAttributesList) {
        if (invalidAttributesList.isEmpty()) {
            anonymizationDialog.overviewMode();
            anonymizationDialog.buildAccordion(attributesToBeProcessed);
            anonymizationDialog.open();
        } else {
            validationErrorDialog.setGridItems(invalidAttributesList);
            if(attributesToBeProcessed.isEmpty()) {
                validationErrorDialog.disableIgnoreButton();
            }
            validationErrorDialog.open();
        }
    }

    /**
     * Starts the anonymization and afterwards opens the feedback dialog.
     */
    private void anonymize() {
        AnonymizationThread thread = new AnonymizationThread(UI.getCurrent(), database, attributesToBeProcessed, progressBarDialog, anonymizationFeedbackDialog);
        thread.start();
    }

    /**
     * Returns a list of (simple)attributes that have an anonymization option.
     */
    private List<SimpleAttribute> getAttributesWithAnonymizationOption() {
        List<SimpleAttribute> simpleAttributeList = new ArrayList<>();
        for(Entity entity : database.getMetadata().getEntities().values()) {
            for(Attribute attribute : entity.getAttributes().values()) {
                if(attribute.getAnonymizationOptionData() != null) {
                    simpleAttributeList.add(new SimpleAttribute(entity.getName(), attribute.getName(), attribute.getAnonymizationOptionData().getOptionID(), null));
                }
            }
        }
        return simpleAttributeList;
    }

    /**
     * Returns the number of all attributes.
     */
    private int countAttributes() {
        int count = 0;
        for (Entity entity : database.getMetadata().getEntities().values()) {
            count += entity.getAttributes().size();
        }
        return count;
    }
}