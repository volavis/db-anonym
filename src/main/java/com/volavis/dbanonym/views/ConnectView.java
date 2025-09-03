package com.volavis.dbanonym.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.commonviewdata.EntityComponentData;
import com.volavis.dbanonym.backend.commonviewdata.MenuDatabaseTab;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.rdbms.RdbmsID;
import com.volavis.dbanonym.backend.database.rdbms.VersionID;
import com.volavis.dbanonym.backend.database.jdbc.ConnectionSettings;
import com.volavis.dbanonym.components.dialogs.ConnectDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows the user to connect to the specified database.
 */
@Route(value = StringConstants.CONNECT_VIEW_ROUTE, layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle(StringConstants.CONNECT_VIEW_PAGE_TITLE)
@CssImport("./styles/views/connect-view.css")
public class ConnectView extends HorizontalLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectView.class);

    private final MainView mainView;
    private final MenuDatabaseTab menuDatabaseTab;
    private final EntityComponentData entityComponentData;
    private final ConnectedDatabase database;

    private final Select<RdbmsID> dbmsSelect = new Select<>();
    private final Select<VersionID> versionSelect = new Select<>();
    private final TextField user = new TextField();
    private final PasswordField password = new PasswordField();
    private final TextField url = new TextField();
    private final Button connectButton = new Button();
    private final TextArea exceptionOutput = new TextArea();

    private final ConnectDialog connectDialog = new ConnectDialog();

    private final Binder<ConnectionSettings> binder = new Binder<>(ConnectionSettings.class);
    private final ConnectionSettings settings = new ConnectionSettings();

    /**
     * Constructor.
     * @param mainView MainView consisting of header and drawer. (autowired)
     * @param menuDatabaseTab Information about the menu tab that navigates to TablesView. (autowired)
     * @param database Database the user connected to. (autowired)
     * @param entityComponentData Keeps track of opened and current entity components. (autowired)
     */
    @Autowired
    public ConnectView(MainView mainView, MenuDatabaseTab menuDatabaseTab, ConnectedDatabase database,
                       EntityComponentData entityComponentData) {

        this.mainView = mainView;
        this.menuDatabaseTab = menuDatabaseTab;
        this.database = database;
        this.entityComponentData = entityComponentData;

        VerticalLayout layoutLeft = createDescription();
        FormLayout layoutRight = createForm();
        add(layoutLeft, layoutRight);

        validationSetup();
        eventListenerSetup();
    }

    /**
     * Creates the description on the left.
     */
    private VerticalLayout createDescription() {

        VerticalLayout layoutLeft = new VerticalLayout();
        layoutLeft.setWidth("60%");
        H4 titleDescription = new H4(StringConstants.CONNECT_VIEW_DESC_TITLE);
        titleDescription.setId("title-description");
        Span description = new Span();
        description.getElement().setProperty("innerHTML", StringConstants.CONNECT_VIEW_DESC);
        layoutLeft.add(titleDescription, description);
        return layoutLeft;
    }

    /**
     * Creates the form on the right. The form allows the user to input the connecting settings.
     */
    private FormLayout createForm() {

        FormLayout columnLayout = new FormLayout();
        columnLayout.setId("column-layout");
        columnLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("25em", 1),
                new FormLayout.ResponsiveStep("32em", 2));

        dbmsSelect.setLabel(StringConstants.CONNECT_VIEW_FORM_RDBMS);
        dbmsSelect.setItems(RdbmsID.values());

        versionSelect.setLabel(StringConstants.CONNECT_VIEW_FORM_VERSION);

        user.setLabel(StringConstants.CONNECT_VIEW_FORM_USER);

        password.setLabel(StringConstants.CONNECT_VIEW_FORM_PASSWORD);

        url.setLabel(StringConstants.CONNECT_VIEW_FORM_URL);

        connectButton.setText(StringConstants.CONNECT_VIEW_FORM_CONNECT_BUTTON);
        connectButton.setIcon(new Icon(VaadinIcon.PLUG));
        connectButton.setId("connect-button");
        connectButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        exceptionOutput.setId("exception-output");
        exceptionOutput.setReadOnly(true);

        columnLayout.add(dbmsSelect, versionSelect, user, password);
        columnLayout.add(url, 2);
        columnLayout.add(connectButton, 2);
        columnLayout.add(exceptionOutput, 2);

        return columnLayout;
    }

    /**
     * Binder setup: Form field input validation and filling fields with already existing connection settings.
     */
    private void validationSetup() {

        binder.bind(dbmsSelect, ConnectionSettings::getRdbms, ConnectionSettings::setRdbms);

        // Change database versions when rdbms changes
        dbmsSelect.addValueChangeListener(event -> {
            List<VersionID> versions = getRdbmsVersions(dbmsSelect.getValue());
            if(!versions.isEmpty()) {
                versionSelect.setEnabled(true);
                versionSelect.setItems(versions);
                versionSelect.setValue(versions.get(0));
            } else {
                versionSelect.clear();
                versionSelect.setEnabled(false);
            }
        });

        binder.bind(versionSelect, ConnectionSettings::getVersion, ConnectionSettings::setVersion);

        binder.forField(user)
                .asRequired(StringConstants.CONNECT_VIEW_FORM_ERROR_USER)
                .bind(ConnectionSettings::getUser, ConnectionSettings::setUser);

        binder.forField(password)
                .asRequired(StringConstants.CONNECT_VIEW_FORM_ERROR_PASSWORD)
                .bind(ConnectionSettings::getPassword, ConnectionSettings::setPassword);

        binder.forField(url)
                .asRequired(StringConstants.CONNECT_VIEW_FORM_ERROR_URL)
                .bind(ConnectionSettings::getUrl, ConnectionSettings::setUrl);

        // Read connection settings
        if (database.getConnectionSettings() != null) {
            binder.readBean(database.getConnectionSettings());
        } else {
            // Initial values
            ConnectionSettings cs = new ConnectionSettings();

            /*cs.setRdbms(RdbmsID.MYSQL);
            cs.setVersion(VersionID.MYSQL_80);
            cs.setUser("root");
            cs.setPassword("Igel22");
            cs.setUrl("jdbc:mysql://localhost:3306/typetest?serverTimezone=UTC");
*/
            /*cs.setRdbms(RdbmsID.ORACLE);
            cs.setVersion(VersionID.ORACLE_19C);
            cs.setUser("typetest");
            cs.setPassword("Darwin98");
            cs.setUrl("jdbc:oracle:thin:@LAPTOP-OPVBIQPT:1521:orcl");
*/
            /*cs.setRdbms(RdbmsID.MSSQL);
            cs.setVersion(VersionID.MSSQL_2019);
            cs.setUser("sa");
            cs.setPassword("Poseidon42");
            cs.setUrl("jdbc:sqlserver://192.168.10.71;databaseName=typetest");*/

            binder.readBean(cs);
        }
    }

    /**
     * Setup: Event handling for buttons and dialogs.
     */
    private void eventListenerSetup() {

        // Connect button
        connectButton.addClickListener(e -> {
            if (binder.writeBeanIfValid(settings)) {
                if (database == null || database.getMetadata() == null) {
                    establishConnection();
                } else {
                    // ConnectDialog opened if there is already an existing connection
                    connectDialog.open();
                }
            }
        });

        // ConnectDialog: confirm
        connectDialog.getConfirmButton().addClickListener(e -> establishConnection());

        // ConnectDialog: cancel
        connectDialog.getCancelButton().addClickListener(e -> connectDialog.close());
    }

    /**
     * Connects to the specified database via the settings, extracts the metadata and navigates to the TablesView.
     */
    private void establishConnection() {
        try {
            connectDialog.close();
            database.changeConnection(settings);
            exceptionOutput.clear();

            // Change database tab in MainView
            menuDatabaseTab.setConnected(true);
            menuDatabaseTab.setDbName(database.getMetadata().getDatabaseName());
            mainView.showDatabaseTab();

            // Reset entityComponentData
            entityComponentData.setCurrentEntityComponentId("");                    // No scrolling
            entityComponentData.setOpenedEntityComponentsList(new ArrayList<>());   // Nothing opened

            getUI().ifPresent(ui -> ui.navigate("tables"));

        } catch (Exception e) {
            exceptionOutput.setValue(e.toString());
            LOGGER.error("Metadata retrieval exception", e);
        }
    }

    /**
     * Returns a list of only those VersionIDs whose RdbmsID equals the parameter rdbmsID.
     * @param rdbmsID RdbmsID that is being searched for.
     * @return Selection of VersionIDs.
     */
    private List<VersionID> getRdbmsVersions(RdbmsID rdbmsID) {
        List<VersionID> resultList = new ArrayList<>();
        for(VersionID vID : VersionID.values()) {
            if(vID.getRdbms() == rdbmsID) {
                resultList.add(vID);
            }
        }
        return resultList;
    }
}