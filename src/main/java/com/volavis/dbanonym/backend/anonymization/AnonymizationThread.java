package com.volavis.dbanonym.backend.anonymization;

import com.vaadin.flow.component.UI;
import com.volavis.dbanonym.backend.database.ConnectedDatabase;
import com.volavis.dbanonym.backend.database.SimpleAttribute;
import com.volavis.dbanonym.components.dialogs.AnonymizationFeedbackDialog;
import com.volavis.dbanonym.components.dialogs.ProgressBarDialog;

import java.util.List;

/**
 * Thread that performs the anonymization. It opens and closes the progressbar dialog.
 */
public class AnonymizationThread extends Thread {

    private final UI ui;
    private final ConnectedDatabase database;
    private final List<SimpleAttribute> attributesToBeProcessed;
    private final ProgressBarDialog progressBarDialog;
    private final AnonymizationFeedbackDialog anonymizationFeedbackDialog;

    /**
     * Constructor.
     * @param ui Topmost component.
     * @param database Database the user connected to.
     * @param attributesToBeProcessed List of (simple)attributes that will be anonymized.
     * @param progressBarDialog Dialog with progressbar.
     * @param anonymizationFeedbackDialog Tells the user that the anonymization has been un/successful.
     */
    public AnonymizationThread(UI ui, ConnectedDatabase database, List<SimpleAttribute> attributesToBeProcessed,
                               ProgressBarDialog progressBarDialog, AnonymizationFeedbackDialog anonymizationFeedbackDialog) {
        this.ui = ui;
        this.database = database;
        this.attributesToBeProcessed = attributesToBeProcessed;
        this.progressBarDialog = progressBarDialog;
        this.anonymizationFeedbackDialog = anonymizationFeedbackDialog;
    }

    @Override
    public void run() {
        ui.access(progressBarDialog::open);
        Anonymization anonymization = new Anonymization(ui, database, progressBarDialog);
        List<SimpleAttribute> failedAttributesList = anonymization.execute(attributesToBeProcessed);
        ui.access(() -> {
            progressBarDialog.close();
            anonymizationFeedbackDialog.setState(failedAttributesList);
            anonymizationFeedbackDialog.open();
        });
    }
}