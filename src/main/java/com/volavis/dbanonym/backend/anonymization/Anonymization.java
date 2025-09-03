package com.volavis.dbanonym.backend.anonymization;

import com.vaadin.flow.component.UI;
import com.volavis.dbanonym.backend.anonymization.options.AnonymizationOption;
import com.volavis.dbanonym.backend.anonymization.options.OptionID;
import com.volavis.dbanonym.backend.database.Attribute;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.Entity;
import com.volavis.dbanonym.backend.database.SimpleAttribute;
import com.volavis.dbanonym.components.dialogs.ProgressBarDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Executes the anonymization depending on the anonymization option.
 * <P>Responsible for retrieving PKs from database.
 */
public class Anonymization {

    private static final Logger LOGGER = LoggerFactory.getLogger(Anonymization.class);

    private final UI ui;
    private final ConnectedDatabase database;
    private final ProgressBarDialog progressBarDialog;

    private String currentEntity = "";
    private final List<Attribute> pkList = new ArrayList<>();
    private final List<ValueWithPKs> valueWithPKsList = new ArrayList<>();

    /**
     * Constructor.
     * @param ui Topmost component.
     * @param database Database the user connected to.
     * @param progressBarDialog Dialog containing progressbar.
     */
    public Anonymization(UI ui, ConnectedDatabase database, ProgressBarDialog progressBarDialog) {
        this.ui = ui;
        this.database = database;
        this.progressBarDialog = progressBarDialog;
    }

    /**
     * Executes the anonymization.
     * @param simpleAttributeList List of (simple)attributes that need to be anonymized.
     * @return List of (simple)attributes whose anonymization failed.
     */
    public List<SimpleAttribute> execute(List<SimpleAttribute> simpleAttributeList) {
        List<SimpleAttribute> failedAttributes = new ArrayList<>();

        // Initialize progressbar
        long operationCount = simpleAttributeList.size();
        ui.access(() -> progressBarDialog.setProgressBarBounds(0.0, (double) operationCount));

        // Sort simpleAttributeList by entity
        simpleAttributeList.sort(Comparator.comparing(SimpleAttribute::getEntityName));

        for(SimpleAttribute simpleAttribute : simpleAttributeList) {
            Entity entity = database.getMetadata().getEntities().get(simpleAttribute.getEntityName());
            Attribute attribute = entity.getAttributes().get(simpleAttribute.getAttributeName());

            try {
                OptionID optionID = attribute.getAnonymizationOptionData().getOptionID();
                AnonymizationOption anonymizationOption = optionID.createAnonymizationOption();

                if(anonymizationOption.usesPKValues()) {
                    queryPKs(entity);
                }

                anonymizationOption.update(database, entity, attribute, pkList, valueWithPKsList);
                ui.access(progressBarDialog::updateProgressBar);

            } catch(Exception exception) {

                failedAttributes.add(new SimpleAttribute(entity.getName(), attribute.getName(),
                        attribute.getAnonymizationOptionData().getOptionID(), exception));

                LOGGER.error("Anonymization exception [Entity: " + entity.getName() +
                        ", Attribute: " + attribute.getName() + "]", exception);
            }
        }
        return failedAttributes;
    }

    /**
     * Queries the primary keys.
     * @param entity Entity whose pk will be retrieved.
     */
    private void queryPKs(Entity entity) {

        // Get pks only once per entity (requirement: list is sorted by entity!)
        if(!entity.getName().equals(currentEntity)) {
            currentEntity = entity.getName();

            pkList.clear();
            valueWithPKsList.clear();

            // Insert all primary key attributes into list (composite primary keys!)
            for(Attribute a : entity.getAttributes().values()) {
                if(a.isPK()) {
                    pkList.add(a);
                }
            }

            if(pkList.isEmpty()) {
                throw new EntityHasNoPkException(entity.getName());
            }

            String dbName = database.getMetadata().getDatabaseName();
            database.getDatabaseVersion().queryEntityPKs(database.getDao(), dbName, entity, pkList, valueWithPKsList);
        }
    }
}