package com.volavis.dbanonym;

/**
 * Contains the String constants for the project.
 */
public final class StringConstants {

    /**
     * Constructor.
     */
    private StringConstants() {
        throw new UnsupportedOperationException();
    }

    // General
    public static final String YES = "Ja";
    public static final String NO = "Nein";

    public static final String CLOSE = "Schließen";
    public static final String OPEN = "Öffnen";
    public static final String SAVE = "Speichern";
    public static final String CANCEL = "Abbrechen";
    public static final String IGNORE = "Ignorieren";
    public static final String OKAY = "Okay";

    public static final String TABLE = "Tabelle";
    public static final String TABLES = "Tabellen";
    public static final String ATTRIBUTE = "Attribut";
    public static final String ATTRIBUTES = "Attribute";
    public static final String OPTION = "Option";
    public static final String OPTIONS = "Optionen";
    public static final String ANONYMIZATION_OPTION = "Anonymisierungsoption";
    public static final String CHECK_CONSTRAINTS = "Check Constraints";
    public static final String DEFAULT_VALUE = "Defualtwert";

    public static final String KEY = "Key";
    public static final String NAME = "Name";
    public static final String DB_DATA_TYPE = "DB-Typ";
    public static final String JAVA_DATA_TYPE = "Java-Typ";
    public static final String NULLABLE = "Nullable";
    public static final String UNIQUE = "Unique";
    public static final String DEFAULT = "Default";
    public static final String PK = "PK";
    public static final String FK = "FK";

    public static final String WARNING_NO_DB_CONNECTION = "Sie müssen zuerst eine DB-Verbindung herstellen!";
    public static final String CHECK_CONSTRAINTS_GRID_TITLE = "Folgende Check Constraints könnten für Sie wichtig sein:";

    // MainView
    public static final String MAIN_VIEW_APPLICATION_NAME = "DB-Anonym";
    public static final String MAIN_VIEW_LOGO_VOLAVIS = "Volavis GmbH";
    public static final String MAIN_VIEW_TAB_CONNECT_VIEW = "DB-Verbindung";
    public static final String MAIN_VIEW_TAB_TABLES_VIEW = "Tabellen";

    // ConnectView
    public static final String CONNECT_VIEW_ROUTE = "connect";
    public static final String CONNECT_VIEW_PAGE_TITLE = "Datenbankverbindung";
    public static final String CONNECT_VIEW_DESC_TITLE = "Beschreibung";
    public static final String CONNECT_VIEW_DESC = "DB-Anonym ist eine Anonymisierungssoftware für relationale Datenbanken zum Schutz von sensiblen Kundendaten." +
            "<br><br>In dieser View werden die Verbindungsdaten angegeben. Der Datenbankbenutzer benötigt die Rechte \"SELECT\" und \"UPDATE\"." +
            "<br><br>Per JDBC wird eine Verbindung zur angegebenen Datenbank aufgebaut und es werden Metadaten extrahiert." +
            " In der folgenden View werden die Tabellen und Attribute übersichtlich dargestellt." +
            " Es können verschiedene Anonymisierungoptionen für ein Attribut ausgewählt werden.";
    public static final String CONNECT_VIEW_FORM_RDBMS = "RDBMS";
    public static final String CONNECT_VIEW_FORM_VERSION = "Version";
    public static final String CONNECT_VIEW_FORM_USER = "Benutzer";
    public static final String CONNECT_VIEW_FORM_PASSWORD = "Kennwort";
    public static final String CONNECT_VIEW_FORM_URL = "JDBC URL";
    public static final String CONNECT_VIEW_FORM_CONNECT_BUTTON = "Verbindung herstellen";
    public static final String CONNECT_VIEW_FORM_ERROR_USER = "Benutzername wird benötigt!";
    public static final String CONNECT_VIEW_FORM_ERROR_PASSWORD = "Kennwort wird benötigt!";
    public static final String CONNECT_VIEW_FORM_ERROR_URL = "JDBC URL der Datenbank wird benötigt!";

    // TablesView
    public static final String TABLES_VIEW_ROUTE = "tables";
    public static final String TABLES_VIEW_PAGE_TITLE = "Tabellen";
    public static final String TABLES_VIEW_EXECUTE_BUTTON = "Anonymisierung ausführen";
    public static final String TABLES_VIEW_ENTITY_COMPONENTS_LAYOUT_TITLE = "Alle Tabellen:";
    public static final String TABLES_VIEW_ENTITY_COMPONENTS_SEARCHBAR = "Tabellenname...";
    public static final String TABLES_VIEW_NO_ANONYMIZATION_OPTIONS = "Sie haben keine Anonymisierungsoptionen!";

    // AnonymizationView
    public static final String ANONYMIZATION_VIEW_ROUTE = "anonymization";
    public static final String ANONYMIZATION_VIEW_PAGE_TITLE = "Anonymisierung";
    public static final String ANONYMIZATION_VIEW_OPTIONS = "Optionen";
    public static final String ANONYMIZATION_VIEW_INVALID = "Eingaben fehlen oder sind fehlerhaft!";

    // EntityComponent
    public static final String ENTITY_COMPONENT_TYPE_INFO = "Typ Info";
    public static final String ENTITY_COMPONENT_ANONYMIZATION = "Anonymisierung";
    public static final String ENTITY_COMPONENT_INFO = "Info";
    public static final String ENTITY_COMPONENT_JAVA_FALLBACK_WARNING = "Der Datenbank-Datentyp dieses Attributes ist dem Programm unbekannt." +
            " Stattdessen wird der Java-Datentyp verwendet, den der JDBC Treiber vorschlägt.";

    // ConnectDialog
    public static final String CONNECT_DIALOG_TITLE = "Verbindung herstellen";
    public static final String CONNECT_DIALOG_TEXT = "Wollen Sie die Datenbankverbindung ändern? " +
            "Schon vorgenommene Einstellungen werden dann gelöscht.";

    // ValidationErrorDialog
    public static final String VALIDATION_ERROR_DIALOG_TITLE = "Fehler bei der Validierung";
    public static final String VALIDATION_ERROR_DIALOG_TEXT = "In der folgenden Tabelle werden die Attribute aufgelistet" +
            ", bei deren Anonymisierungsoptionen ein Problem festgestellt wurde." +
            " Sie können die Fehler ignorieren, dann werden die betroffenen Attribute nicht anonymisiert.";

    // AnonymizationDialog
    public static final String ANONYMIZATION_DIALOG_EVERYTHING_BUTTON = "Alles anonymisieren";
    public static final String ANONYMIZATION_DIALOG_SELECTION_BUTTON = "Auswahl anonymisieren";
    public static final String ANONYMIZATION_DIALOG_OVERVIEW_TITLE = "Anonymisierung starten";
    public static final String ANONYMIZATION_DIALOG_OVERVIEW_TEXT = "Wählen Sie aus, ob Sie alle Attribute, bei denen Sie eine " +
            "Anonymiserungsoption eingestellt haben, anonymisieren wollen, oder ob Sie nur eine " +
            "Auswahl an Attributen anonymisieren wollen.";
    public static final String ANONYMIZATION_DIALOG_SELECTION_TITLE = "Attribute auswählen";
    public static final String ANONYMIZATION_DIALOG_SELECTION_TEXT = "Wählen Sie die Attribute aus, die Sie anonymisiert haben wollen.";

    // ProgressBarDialog
    public static final String PROGRESSBAR_DIALOG_TITLE = "Anonymisierung";
    public static final String PROGRESSBAR_DIALOG_TEXT = "Die Anonymisierung wird ausgeführt. Bitte haben Sie Geduld.";

    // AnonymizationFeedbackDialog
    public static final String ANONYMIZATION_FEEDBACK_DIALOG_EXCEPTION_TITLE = "Aufgetretene Exception";
    public static final String ANONYMIZATION_FEEDBACK_DIALOG_SUCCESS_TITLE = "Anonymisierung erfolgreich";
    public static final String ANONYMIZATION_FEEDBACK_DIALOG_SUCCESS_TEXT = "Alle Attribute konnten anonymisiert werden.";
    public static final String ANONYMIZATION_FEEDBACK_DIALOG_ERROR_TITLE = "Fehler bei der Anonymisierung";
    public static final String ANONYMIZATION_FEEDBACK_DIALOG_ERROR_TEXT = "Es konnten nicht alle Attribute anonymisiert werden." +
            " In der folgenden Tabelle werden die Attribute aufgelistet, bei denen eine Exception" +
            " aufgetreten ist.";

    // Attribute subclasses
    public static final String ATTRIBUTE_INFO_CHARS = "Zeichen";
    public static final String ATTRIBUTE_INFO_DIGITS = "Ziffern";
    public static final String ATTRIBUTE_INFO_BIGDECIMAL = "%s P, %s S";
    public static final String ATTRIBUTE_INFO_BIGDECIMAL_ANY_SCALE = "%s P, * S";
    public static final String ATTRIBUTE_INFO_BYTES = "Bytes";

    // AnonymizationOption subclasses
    public static final String NONE_COMP_OPTION_INFO = "Das Attribut wird nicht anonymisiert!";
    public static final String NULL_COMP_OPTION_INFO = "Alle Werte der Spalte werden durch Null ersetzt.";
    public static final String DEF_COMP_OPTION_INFO = "Alle Werte der Spalte werden durch den Defaultwert ersetzt.";
    public static final String CONST_COMP_OPTION_INFO = "Alle Werte der Spalte werden durch einen konstanten Wert ersetzt.";
    public static final String RND_COMP_OPTION_INFO = "Alle Werte der Spalte werden durch einen zufälligen Wert ersetzt.";

    //================================
    // ConstOptionComponent subclasses

    // Characters
    public static final String CONST_COMP_CHARACTERS_TITLE = "Bitte schreiben Sie einen String in das Eingabefeld:";
    public static final String CONST_COMP_CHARACTERS_REQUIRED = "Der String fehlt!";
    public static final String CONST_COMP_CHARACTERS_VALIDATOR_LENGTH = "Ihr String darf maximal %d Zeichen haben!";

    // Integer
    public static final String CONST_COMP_INTEGER_TITLE = "Bitte schreiben Sie eine Ganzzahl in das Eingabefeld:";
    public static final String CONST_COMP_INTEGER_REQUIRED = "Die Ganzzahl fehlt!";
    public static final String CONST_COMP_INTEGER_VALIDATOR_NAN = "Es handelt sich nicht um eine Ganzzahl!";
    public static final String CONST_COMP_INTEGER_VALIDATOR_RANGE = "Ihre Ganzzahl liegt nicht im Wertebereich";

    // Decimal
    public static final String CONST_COMP_DECIMAL_TITLE = "Bitte schreiben Sie eine Dezimalzahl in das Eingabefeld:";
    public static final String CONST_COMP_DECIMAL_WARNING_DIV_TITLE = "Die Dezimalzahl könnte gerundet werden!";
    public static final String CONST_COMP_DECIMAL_WARNING_DIV_FLOAT = "Float-Wert: ";
    public static final String CONST_COMP_DECIMAL_WARNING_DIV_DOUBLE = "Double-Wert: ";
    public static final String CONST_COMP_DECIMAL_REQUIRED = "Die Dezimalzahl fehlt oder ist fehlerhaft!";
    public static final String CONST_COMP_DECIMAL_VALIDATOR_INFINITY = "Die Dezimalzahl muss zwischen -INFINITY und +INFINITY liegen!";
    public static final String CONST_COMP_DECIMAL_VALIDATOR_DECIMAL_PLACES = "Die Dezimalzahl darf maximal %d Ziffer(n) haben, mit %d Dezimalstelle(n)!";

    // Binary
    public static final String CONST_COMP_BINARY_TITLE = "Bitte schreiben Sie einen String in das Eingabefeld. Dieser wird später in einen Binärstring (UTF-8) umgewandelt:";
    public static final String CONST_COMP_BINARY_REQUIRED = "Der String fehlt!";
    public static final String CONST_COMP_BINARY_VALIDATOR_LENGTH = "Der Binärstring darf maximal %d Bytes haben!";

    // Boolean
    public static final String CONST_COMP_BOOLEAN_TITLE = "Bitte wählen Sie einen Wahrheitswert aus:";

    // Date
    public static final String CONST_COMP_DATE_TITLE = "Bitte wählen Sie ein Datum aus:";
    public static final String CONST_COMP_DATE_REQUIRED = "Das Datum fehlt oder ist fehlerhaft!";

    // Time
    public static final String CONST_COMP_TIME_TITLE = "Bitte schreiben Sie eine Uhrzeit [hh:mm:ss.fff] in das Eingabefeld:";
    public static final String CONST_COMP_TIME_REQUIRED = "Die Uhrzeit fehlt oder ist fehlerhaft!";

    // Timestamp
    public static final String CONST_COMP_TIMESTAMP_TITLE = "Bitte schreiben Sie einen Zeitstempel in die Eingabefelder:";
    public static final String CONST_COMP_TIMESTAMP_REQUIRED = "Das Datum/ Die Uhrzeit fehlt oder ist fehlerhaft!";

    // Unknown
    public static final String CONST_COMP_UNKNOWN_TITLE = "Dieser Datentyp kann nicht mit der Option \"Konstante\" anonymisiert werden!";

    // Mysql_Enum
    public static final String CONST_COMP_MYSQL_ENUM_TITLE = "Bitte wählen Sie einen Wert aus:";

    // Oracle_Characters
    public static final String CONST_COMP_ORACLE_CHARACTERS_VALIDATOR_LENGTH = "Ihr String darf maximal %d Bytes haben!";

    // Oracle_Decimal
    public static final String CONST_COMP_ORACLE_DECIMAL_VALIDATOR_DECIMAL_PLACES = "Es gibt ein Problem mit Ihrer Dezimalzahl: Precision = %d, Scale = %d! [Beachten Sie Oracles Besonderheit, wenn der Scale größer ist als die Precision!]";

    //==============================
    // RndOptionComponent subclasses

    // Characters
    public static final String RND_COMP_CHARACTERS_TITLE = "Bitte wählen Sie eine Option aus:";
    public static final String RND_COMP_CHARACTERS_MIN_FIELD_NAME = "Minimale Länge";
    public static final String RND_COMP_CHARACTERS_MAX_FIELD_NAME = "Maximale Länge";
    public static final String RND_COMP_CHARACTERS_REQUIRED_MIN = "Die minimale Länge fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_CHARACTERS_REQUIRED_MAX = "Die maximale Länge fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_CHARACTERS_VALIDATOR_RANGE = "Die Länge darf nur im Intervall [1, %d] liegen!";
    public static final String RND_COMP_CHARACTERS_VALIDATOR_MAX_AFTER_MIN = "Die maximale Länge muss >= sein als die minimale Länge!";
    public static final String RND_COMP_CHARACTERS_VALIDATOR_UNIQUE = "Unique-Constraint: Es gibt nicht genügend Werte im Intervall!";

    // Integer
    public static final String RND_COMP_INTEGER_TITLE = "Bitte wählen Sie eine Option aus:";
    public static final String RND_COMP_INTEGER_MIN_FIELD_NAME = "Minimaler Wert";
    public static final String RND_COMP_INTEGER_MAX_FIELD_NAME = "Maximaler Wert";
    public static final String RND_COMP_INTEGER_CHECKBOX_NAME = "Vorzeichen beibehalten";
    public static final String RND_COMP_INTEGER_REQUIRED_MIN = "Der minimale Wert fehlt!";
    public static final String RND_COMP_INTEGER_REQUIRED_MAX = "Der maximale Wert fehlt!";
    public static final String RND_COMP_INTEGER_VALIDATOR_NAN = "Es handelt sich nicht um eine Ganzzahl!";
    public static final String RND_COMP_INTEGER_VALIDATOR_VALUE_RANGE = "Ihr Wert liegt nicht im Wertebereich!";
    public static final String RND_COMP_INTEGER_VALIDATOR_MAX_AFTER_MIN = "Ihr maximaler Wert muss größer sein als Ihr minimaler Wert!";
    public static final String RND_COMP_INTEGER_VALIDATOR_UNIQUE = "Unique-Constraint: Es muss mindestens %d Werte im Intervall geben!";

    // Decimal
    public static final String RND_COMP_DECIMAL_TITLE = "Bitte wählen Sie eine Option aus:";
    public static final String RND_COMP_DECIMAL_MIN_FIELD_NAME = "Minimaler Wert";
    public static final String RND_COMP_DECIMAL_MAX_FIELD_NAME = "Maximaler Wert";
    public static final String RND_COMP_DECIMAL_CHECKBOX_NAME = "Vorzeichen beibehalten";
    public static final String RND_COMP_DECIMAL_DECIMAL_PLACE_FIELD_NAME = "Maximale Anzahl an Nachkommastellen";
    public static final String RND_COMP_DECIMAL_DECIMAL_PLACE_REQUIRED = "Die Nachkommastellenanzahl fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_DECIMAL_DECIMAL_PLACE_VALIDATOR_RANGE = "Die Nachkommastellenanzahl liegt nicht im Wertebereich!";
    public static final String RND_COMP_DECIMAL_DECIMAL_PLACE_VALIDATOR_SCALE = "Die Nachkommastellenanzahl darf maximal %d sein!";
    public static final String RND_COMP_DECIMAL_REQUIRED_MIN = "Der minimale Wert fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_DECIMAL_REQUIRED_MAX = "Der maximale Wert fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_DECIMAL_VALIDATOR_POST_DECIMAL_PLACES = "Ihre Dezimalzahl darf nur so viele Dezimalstellen maximal haben, wie in dem Nachkommastellen Eingabefeld angegeben wurde!";
    public static final String RND_COMP_DECIMAL_VALIDATOR_INFINITY = "Die Dezimalzahl muss zwischen -INFINITY und +INFINITY liegen!";
    public static final String RND_COMP_DECIMAL_VALIDATOR_PRE_POST_DECIMAL_PLACES = "Die Dezimalzahl darf maximal %d Ziffer(n) haben, mit %d Dezimalstelle(n)!";
    public static final String RND_COMP_DECIMAL_VALIDATOR_MAX_AFTER_MIN = "Ihr maximaler Wert muss größer sein als Ihr minimaler Wert!";
    public static final String RND_COMP_DECIMAL_VALIDATOR_UNIQUE = "Unique-Constraint: Es muss mindestens %d Werte im Intervall geben!";

    // Binary
    public static final String RND_COMP_BINARY_TITLE = "Bitte wählen Sie eine Option aus:";
    public static final String RND_COMP_BINARY_MIN_FIELD_NAME = "Minimale Byteanzahl";
    public static final String RND_COMP_BINARY_MAX_FIELD_NAME = "Maximale Byteanzahl";
    public static final String RND_COMP_BINARY_REQUIRED_MIN = "Die minimale Byteanzahl fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_BINARY_REQUIRED_MAX = "Die maximale Byteanzahl fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_BINARY_VALIDATOR_RANGE = "Die Byteanzahl darf nur im Intervall [1, %d] liegen!";
    public static final String RND_COMP_BINARY_VALIDATOR_MAX_AFTER_MIN = "Die maximale Byteanzahl muss >= sein als die minimale Byteanzahl!";
    public static final String RND_COMP_BINARY_VALIDATOR_UNIQUE = "Unique-Constraint: Es gibt nicht genügend Werte im Intervall!";

    // Boolean
    public static final String RND_COMP_BOOLEAN_TITLE = "Die Attributwerte werden durch zufällige Wahrheitswerte ersetzt.";
    public static final String RND_COMP_BOOLEAN_UNIQUE = "Das Attribut kann, wegen dem Unique-Constraint, nicht mit der Option \"Zufall\" anonymisiert werden!";

    // Date
    public static final String RND_COMP_DATE_TITLE = "Für jeden Wert des Attributes wird ein zufälliges Datum generiert, welches zwischen dem unten festgelegten Start- und Enddatum liegt (Grenzen inklusive).";
    public static final String RND_COMP_DATE_MIN_PICKER_NAME = "Startdatum";
    public static final String RND_COMP_DATE_MAX_PICKER_NAME = "Enddatum";
    public static final String RND_COMP_DATE_REQUIRED_MIN = "Das Startdatum fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_DATE_REQUIRED_MAX = "Das Enddatum fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_DATE_VALIDATOR_MAX_AFTER_MIN = "Das Enddatum muss nach dem Startdatum liegen!";
    public static final String RND_COMP_DATE_VALIDATOR_UNIQUE = "Unique-Constraint: Es muss mindestens %d mögliche Daten geben!";

    // Time
    public static final String RND_COMP_TIME_TITLE = "Für jeden Wert des Attributes wird eine zufällig Uhrzeit generiert, welche zwischen zwei festgelegten Uhrzeiten liegt (Grenzen inklusive).";
    public static final String RND_COMP_TIME_MIN_PICKER_NAME = "Minimale Uhrzeit";
    public static final String RND_COMP_TIME_MAX_PICKER_NAME = "Maximale Uhrzeit";
    public static final String RND_COMP_TIME_REQUIRED_MIN = "Die minimale Uhrzeit fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_TIME_REQUIRED_MAX = "Die maximale Uhrzeit fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_TIME_VALIDATOR_MAX_AFTER_MIN = "Die maximale Uhrzeit muss zeitlich hinter der minimalen Uhrzeit liegen!";
    public static final String RND_COMP_TIME_VALIDATOR_UNIQUE = "Unique-Constraint: Es muss mindestens %d mögliche Uhrzeiten geben!";

    // Timestamp
    public static final String RND_COMP_TIMESTAMP_TITLE = "Für jeden Wert des Attributes wird ein zufälliger Zeitstempel generiert, welcher zwischen zwei festgelegten Zeitstempeln liegt (Grenzen inklusive).";
    public static final String RND_COMP_TIMESTAMP_MIN_PICKER_NAME = "Minimaler Zeitstempel";
    public static final String RND_COMP_TIMESTAMP_MAX_PICKER_NAME = "Maximaler Zeitstempel";
    public static final String RND_COMP_TIMESTAMP_REQUIRED_MIN = "Der minimale Zeitstempel fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_TIMESTAMP_REQUIRED_MAX = "Der maximale Zeitstempel fehlt oder ist fehlerhaft!";
    public static final String RND_COMP_TIMESTAMP_VALIDATOR_MAX_AFTER_MIN = "Der maximale Zeitstempel muss zeitlich hinter dem minimalen Zeitstempel liegen!";
    public static final String RND_COMP_TIMESTAMP_VALIDATOR_UNIQUE = "Unique-Constraint: Es muss mindestens %d mögliche Zeitstempel geben!";

    // Unknown
    public static final String RND_COMP_UNKNOWN_TITLE = "Dieser Datentyp kann nicht mit der Option \"Zufall\" anonymisiert werden!";

    // Mysql_Enum
    public static final String RND_COMP_MYSQL_ENUM_TITLE = "Die Attributwerte werden durch zufällige Enum-Werte ersetzt.";

    // Oracle_Decimal
    public static final String RND_COMP_ORACLE_DECIMAL = "Dieser Datentyp kann noch nicht mit der Option \"Zufall\" anonymisiert werden!";

    //=========================
    // RndOptionData subclasses
    public static final String RND_CHARACTERS_DROPDOWN_ELEMENT_SAME_LENGTH_TITLE = "Gleiche Anzahl an Zeichen";
    public static final String RND_CHARACTERS_DROPDOWN_ELEMENT_SAME_LENGTH_INFO = "Jeder Wert dieses Attributes wird durch einen zufälligen String ersetzt, der die gleiche Zeichenlänge hat wie der vorherige nicht anonymisierte Wert.";
    public static final String RND_CHARACTERS_DROPDOWN_ELEMENT_USER_DEFINED_TITLE = "Benutzerdefinierte Länge";
    public static final String RND_CHARACTERS_DROPDOWN_ELEMENT_USER_DEFINED_INFO = "Jeder Wert dieses Attributes wird durch einen zufälligen String ersetzt, dessen Länge in den Eingabefeldern festgelegt wird (NULL-Werte bleiben!).";

    public static final String RND_DECIMAL_DROPDOWN_ELEMENT_SAME_LENGTH_TITLE = "Gleichbleibende Anzahl an Vor- und Nachkommastellen";
    public static final String RND_DECIMAL_DROPDOWN_ELEMENT_SAME_LENGTH_INFO = "Es wird für jeden Wert eine zufällige Dezimalzahl generiert, die die gleiche Anzahl an Vor- und Nachkommastellen hat, wie die vorherige nicht anonymisierte Dezimalzahl (NULL-Werte bleiben! + Die generierte Zahl könnte gerundet werden!).";
    public static final String RND_DECIMAL_DROPDOWN_ELEMENT_USER_DEFINED_TITLE = "Benutzerdefiniertes Intervall";
    public static final String RND_DECIMAL_DROPDOWN_ELEMENT_USER_DEFINED_INFO = "Jeder Wert dieses Attributes wird durch eine zufällige Dezimalzahl ersetzt, die innerhalb des benutzerdefinierten Intervalls liegt (Grenzen inklusive).";

    public static final String RND_BINARY_DROPDOWN_ELEMENT_SAME_BYTE_COUNT_TITLE = "Gleiche Anzahl an Bytes";
    public static final String RND_BINARY_DROPDOWN_ELEMENT_SAME_BYTE_COUNT_INFO = "Es wird ein zufälliger Binärstring erzeugt, der die gleiche Anzahl an Bytes hat wie der vorherige Wert (NULL-Werte bleiben!).";
    public static final String RND_BINARY_DROPDOWN_ELEMENT_USER_DEFINED_TITLE = "Benutzerdefiniertes Intervall";
    public static final String RND_BINARY_DROPDOWN_ELEMENT_USER_DEFINED_INFO = "Es wird ein zufälliger Binärstring erzeugt, der innerhalb des benutzerdefinierten Intervalls liegt.";

    public static final String RND_INTEGER_DROPDOWN_ELEMENT_SAME_DIGIT_COUNT_TITLE = "Gleiche Anzahl an Ziffern";
    public static final String RND_INTEGER_DROPDOWN_ELEMENT_SAME_DIGIT_COUNT_INFO = "Es wird für jeden Wert eine zufällige Ganzzahl generiert, die die gleiche Anzahl an Ziffern haben wird (NULL-Werte bleiben!).";
    public static final String RND_INTEGER_DROPDOWN_ELEMENT_VALUE_RANGE_TITLE = "Zahl aus dem Wertebereich";
    public static final String RND_INTEGER_DROPDOWN_ELEMENT_VALUE_RANGE_INFO = "Jeder Wert dieses Attributes wird durch eine zufällige Zahl aus dem Wertebereich des JDBC-Typen ersetzt.";
    public static final String RND_INTEGER_DROPDOWN_ELEMENT_USER_DEFINED_TITLE = "Benutzerdefiniertes Intervall";
    public static final String RND_INTEGER_DROPDOWN_ELEMENT_USER_DEFINED_INFO = "Jeder Wert dieses Attributes wird durch eine zufällige Zahl aus dem benutzerdefinierten Intervall ersetzt.";
}