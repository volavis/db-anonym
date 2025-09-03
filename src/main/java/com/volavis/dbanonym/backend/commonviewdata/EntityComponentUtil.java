package com.volavis.dbanonym.backend.commonviewdata;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.PendingJavaScriptResult;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.components.EntityComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Includes useful methods for working with an EntityComponent list.
 */
public final class EntityComponentUtil {

    public static final String PREFIX = "entity-component-";

    public static final String JS_PAGE_FINISHED_LOADING =
              "function delay() {\n"
            + "  return new Promise(function(resolve) {\n"
            + "    setTimeout(resolve, 50)\n"
            + "  });\n"
            + "}\n"
            + "async function waitForLoadingToFinish() {\n"
            + "  while(true) {\n"
            + "    let progressElement = document.getElementsByClassName('v-loading-indicator');\n"
            + "    if (progressElement[0].style.display == 'none') {\n"
            + "      return true;\n"
            + "    } else {\n"
            + "      await delay();\n"
            + "    }\n"
            + "  }\n"
            + "}\n"
            + "return waitForLoadingToFinish();";

    /**
     * Constructor.
     */
    private EntityComponentUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Builds the EntityComponent list. Called when the user navigates to the TablesView.
     * Additionally it sorts the list and generates the ids.
     * @param entityComponentList EntityComponent list that will be filled.
     * @param selectedAttribute Attribute the user wants to anonymize.
     * @param entityComponentData Keeps track of opened and current entity components.
     * @param entityMap Entities used to build the EntityComponent list.
     */
    public static void buildEntityComponentList(List<EntityComponent> entityComponentList, SelectedAttribute selectedAttribute,
                                                EntityComponentData entityComponentData, Map<String, Entity> entityMap) {
        if (entityComponentList != null && selectedAttribute != null && entityComponentData != null && entityMap != null) {

            entityComponentList.clear();

            for (Entity entity : entityMap.values()) {
                entityComponentList.add(new EntityComponent(entity, selectedAttribute, entityComponentData));
            }

            entityComponentList.sort(Comparator.comparing(entityComponent -> entityComponent.getEntity().getName()));

            AtomicLong idCounter = new AtomicLong(0);
            for (EntityComponent entityComponent : entityComponentList) {
                entityComponent.setHtmlID(String.valueOf(idCounter.getAndIncrement()));
            }
        }
    }

    /**
     * Returns an EntityComponent depending on the given id.
     * @param entityComponentList EntityComponent list that will be searched.
     * @param id Id that is being searched for.
     * @return Found EntityComponent or null.
     */
    public static EntityComponent getEntityComponentFromID(List<EntityComponent> entityComponentList, String id) {
        if (entityComponentList != null && isIdValid(id)) {
            for (EntityComponent entityComponent : entityComponentList) {
                if (entityComponent.getHtmlID().equals(id)) {
                    return entityComponent;
                }
            }
        }
        return null;
    }

    /**
     * Opens the EntityComponents that were opened last time (openedEntityComponentsList in entityComponentData).
     * @param entityComponentList EntityComponent list in which EntityComponents are to be opened.
     * @param entityComponentData Keeps track of opened and current entity components.
     */
    public static void openEntityComponentsAgain(List<EntityComponent> entityComponentList, EntityComponentData entityComponentData) {
        if (entityComponentList != null && entityComponentData != null) {
            for (EntityComponent entityComponent : entityComponentList) {
                List<String> idList = entityComponentData.getOpenedEntityComponentsList();
                if (idList.contains(entityComponent.getHtmlID())) {
                    entityComponent.open();
                }
            }
        }
    }

    /**
     * Saves which EntityComponents are currently open.
     * @param entityComponentList EntityComponent list whose state will be saved.
     * @param entityComponentData Keeps track of opened and current entity components.
     */
    public static void saveOpenedEntityComponents(List<EntityComponent> entityComponentList, EntityComponentData entityComponentData) {
        if (entityComponentList != null && entityComponentData != null) {
            List<String> idList = new ArrayList<>();
            for (EntityComponent entityComponent : entityComponentList) {
                if (entityComponent.isOpen()) {
                   idList.add(entityComponent.getHtmlID());
                }
            }
            entityComponentData.setOpenedEntityComponentsList(idList);
        }
    }

    /**
     * Scrolls (and opens) an EntityComponent depending on an ID.
     * @param entityComponentList EntityComponent list that will be searched.
     * @param id Id that is being searched for.
     * @param scrollIfClosed Scroll to the EntityComponent if it is closed?
     * @param openIfClosed Open the EntityComponent if it is closed?
     */
    public static void scrollToEntityComponentById(List<EntityComponent> entityComponentList, String id,
                                                   boolean scrollIfClosed, boolean openIfClosed) {
        if (entityComponentList != null) {
            EntityComponent entityComponent = getEntityComponentFromID(entityComponentList, id);
            if (entityComponent != null) {
                scrollLogic(entityComponent, scrollIfClosed, openIfClosed);
            }
        }
    }

    /**
     * Scrolls (and opens) an EntityComponent depending on its name.
     * @param entityComponentList EntityComponent list that will be searched.
     * @param entityName Name that is being searched for.
     * @param scrollIfClosed Scroll to the EntityComponent if it is closed?
     * @param openIfClosed Open the EntityComponent if it is closed?
     */
    public static void scrollToEntityComponentByName(List<EntityComponent> entityComponentList, String entityName,
                                                     boolean scrollIfClosed, boolean openIfClosed) {
        if (entityComponentList != null && entityName != null && !entityName.isEmpty()) {
            for (EntityComponent entityComponent : entityComponentList) {
                String tmpName = entityComponent.getEntity().getName().toLowerCase().replaceAll("\\s", "");
                if (tmpName.equals(entityName.toLowerCase().replaceAll("\\s", ""))) {
                    scrollLogic(entityComponent, scrollIfClosed, openIfClosed);
                }
            }
        }
    }

    /**
     * Decided if the page scrolls down to the EntityComponent and if the EntityComponent gets opened.
     * @param entityComponent The EntityComponent to be scrolled to.
     * @param scrollIfClosed Scroll to the EntityComponent if it is closed?
     * @param openIfClosed Open the EntityComponent if it is closed?
     */
    private static void scrollLogic(EntityComponent entityComponent, boolean scrollIfClosed, boolean openIfClosed) {
        if (entityComponent.isOpen()) {
            entityComponent.getElement().callJsFunction("scrollIntoView");
        } else {
            if (openIfClosed) {
                entityComponent.open();
            }
            if (scrollIfClosed) {
                PendingJavaScriptResult loadingFinished = UI.getCurrent().getPage().executeJs(EntityComponentUtil.JS_PAGE_FINISHED_LOADING);
                loadingFinished.then(Boolean.class, (res) -> entityComponent.getElement().callJsFunction("scrollIntoView"));
            }
        }
    }

    /**
     * Checks if the given id is valid (has the right prefix).
     * @param id Id that needs to be validated.
     * @return valid = true, invalid = false.
     */
    private static boolean isIdValid(String id) {
        if (id != null && !id.equals("")) {
            int prefixLength = PREFIX.length();
            if (id.length() > prefixLength) {
                return id.substring(0, prefixLength).equals(PREFIX);
            }
        }
        return false;
    }
}