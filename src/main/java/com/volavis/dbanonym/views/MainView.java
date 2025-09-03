package com.volavis.dbanonym.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.volavis.dbanonym.StringConstants;
import com.volavis.dbanonym.backend.commonviewdata.MenuDatabaseTab;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Is a top-level placeholder for other views. It consists out of a header and a menu.
 */
@Push
@SpringComponent
@UIScope
@JsModule("./styles/shared-styles.js")
@CssImport("./styles/views/main-view.css")
@PWA(name = StringConstants.MAIN_VIEW_APPLICATION_NAME, shortName = StringConstants.MAIN_VIEW_APPLICATION_NAME, enableInstallPrompt = false)
public class MainView extends AppLayout {

    private final MenuDatabaseTab menuDatabaseTab;

    private final Tabs menu;
    private Tab databaseTab;
    private final H1 viewTitle = new H1();

    /**
     * Constructor.
     * @param menuDatabaseTab Information about the menu tab that navigates to TablesView. (autowired)
     */
    @Autowired
    public MainView(MenuDatabaseTab menuDatabaseTab) {

        this.menuDatabaseTab = menuDatabaseTab;

        setPrimarySection(Section.DRAWER);
        addToNavbar(createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    /**
     * Builds the header containing the menu button and logos.
     */
    private Component createHeaderContent() {

        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.setWidthFull();
        layout.getThemeList().set("dark", true);
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        HorizontalLayout layoutLeft = new HorizontalLayout();
        layoutLeft.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutLeft.setWidth("400px");
        layoutLeft.add(new DrawerToggle(), viewTitle);

        Image logoDBAnonym = new Image("images/text_dbanonym.png", StringConstants.MAIN_VIEW_APPLICATION_NAME);

        HorizontalLayout layoutRight = new HorizontalLayout();
        layoutRight.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        layoutRight.setWidth("400px");
        Image logoVolavis = new Image("images/logo_volavis.png", StringConstants.MAIN_VIEW_LOGO_VOLAVIS);
        logoVolavis.setId("logo-volavis");
        layoutRight.add(logoVolavis);

        layout.add(layoutLeft, logoDBAnonym, layoutRight);
        return layout;
    }

    /**
     * Builds the menu's tabs.
     */
    private Tabs createMenu() {

        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");

        // Tab for ConnectView
        tabs.add(createTab(StringConstants.MAIN_VIEW_TAB_CONNECT_VIEW, ConnectView.class));

        // Tab for TablesView
        databaseTab = createTab(StringConstants.MAIN_VIEW_TAB_TABLES_VIEW, TablesView.class);
        showDatabaseTab();
        tabs.add(databaseTab);

        return tabs;
    }

    /**
     * Creates a menu tab.
     * @param text Name of the tab.
     * @param navigationTarget Class of the view to navigate to.
     */
    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    /**
     * Builds the drawer containing an image, title and the menu tabs.
     * @param menu Tabs to be added to the drawer.
     */
    private Component createDrawerContent(Tabs menu) {

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo_dbanonym.png", ""));
        logoLayout.add(new H1(StringConstants.MAIN_VIEW_APPLICATION_NAME));

        layout.add(logoLayout, menu);
        return layout;
    }

    /**
     * Inserts the page's title and sets the visibility of the TablesView tab.
     */
    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());

        showDatabaseTab();
    }

    /**
     * Retrieves the selected tab.
     */
    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren()
                .filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    /**
     * Retrieves the page's title.
     */
    private String getCurrentPageTitle() {
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }

    /**
     * If user connects to a database, the tab navigating to the TablesView is set visible and gets the name of the db.
     */
    public void showDatabaseTab() {
        if(menuDatabaseTab.isConnected()) {
            databaseTab.setVisible(true);

            // Change RouterLink name in databaseTab
            Optional<Component> routerLinkOptional = databaseTab.getChildren()
                    .filter(child -> child instanceof RouterLink)
                    .findFirst();
            routerLinkOptional.ifPresent(routerLinkComponent -> {
                RouterLink routerLink = (RouterLink) routerLinkComponent;
                routerLink.getElement().setText(menuDatabaseTab.getDbName());
            });

        } else {
            databaseTab.setVisible(false);
        }
    }
}