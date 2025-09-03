# DB-Anonym

Dieses Projekt ist eine Anwendung zur Anonymisierung von Datenbanken.
Es ermöglicht, sensible Daten wie Namen, Adressen oder Kundendaten sicher zu schützen, ohne dass dabei die Struktur der Datenbank verloren geht.

Mit der Software können Daten so verfremdet werden, dass sie für Tests, Analysen oder Entwicklung genutzt werden können.
Die Bedienung erfolgt über eine Benutzeroberfläche, in der passende Anonymisierungsregeln ausgewählt und konfiguriert werden können.

**Unterstützte Datenbanken**

* Microsoft SQL Server
* Oracle
* MySQL

---

Version 1.0, April 2025

## Anleitung: Erweiterung der Software

**Inhalt**

[[_TOC_]]

### 1. Neues RDBMS

- Das Enum **RdbmsID** erweitern und einen lesbaren Namen angeben.
  
- Dem neuen RDBMS werden im nächsten Schritt Datenbankversionen zugeordnet.

### 2. Neue Datenbankversion

- Jede Datenbankversion hat ihre eigene Klasse (Siehe Klasse Mysql56).
  
- Die älteste unterstützte Datenbankversionsklasse muss von der abstrakten Klasse **DatabaseVersion** erben und ihre abstrakten Methoden implementieren. In den Methoden stehen die datenbankspezifischen SQL-Befehle, die an die dazugehörigen Methoden der Klasse JdbcDao übergeben werden. Die Klasse **JdbcDao** führt die SELECT- und UPDATE-Queries aus. Gegebenenfalls müssen der Klasse JdbcDao neue Methoden hinzugefügt werden.
  
- Jede neuere Datenbankversion erbt von der Klasse ihrer Vorgängerversion und überschreibt nur Methoden, wenn sich etwas in der neueren Version geändert hat (Die Methode getVersionID() muss überschrieben werden).
  
- Das Enum **VersionID** erweitern:
  - Im Konstruktor einen lesbaren Versionsnamen + RdbmsID + JDBC-Treiber-Klassenname übergeben.
  - Gegebenenfalls in der pom.xml einen neuen JDBC-Treiber zu den Dependencies hinzufügen.
  - Das Enum-Objekt muss zwei Methoden implementieren:
    - Die Methode **createDatabaseVersion()** gibt die Datenbankversionsklasse zurück.
    - Die Methode **createMetadataExtractor()** gibt den Metadaten-Extraktor zurück (Siehe nächstes Unterkapitel 2.1.).

#### 2.1. Neuer Metadaten-Extraktor

- Der Metadaten-Extraktor gibt die Reihenfolge vor, wie die Metadaten extrahiert werden sollen und welche Methoden dafür eingesetzt werden sollen.
  
- Es gibt schon zwei Extraktoren: Den Default-Metadaten-Extraktor (RDBMS: MS SQL Server + Oracle) und den MySQL-Metadaten-Extraktor (RDBMS: MySQL).
  
- Gegebenenfalls muss ein neuer Metadaten-Extraktor implementiert werden:
  - Die neue Klasse muss von der abstrakten Klasse **MetadataExtractor** erben und die Methode **extractMetadata()** implementieren.
  - In der Methode wird das übergebene **Metadata-Object** mit den benötigten Metadaten gefüllt. Hierfür werden die Methoden der Datenbankversionsklasse eingesetzt.
  - Als letzter Schritt in der Methode wird das Mapping zwischen den Datenbank-Datentypen und den Attribute-Unterklassen ausgeführt. Hier findet sich auch die Logik für den Fallback-Java-Klassennamen, der durch den JDBC-Treiber zurückgegeben wird und nur bei Problemen verwendet wird (Siehe Methode performAttributeMapping() der Klasse DefaultMetadataExtractor).

#### 2.2. Mapping der Datenbank-Datentypen zu den Java-Datentypen

- Die große Switch-Case-Anweisung für das Mapping der Datentypen wurde für jedes RDBMS in eine eigene Klasse ausgelagert (Siehe Klasse MysqlMapping).
  
- In der Methode **getAttribute()** gibt es eine Switch-Case-Anweisung, welche die Datenbank-Datentypen (Strings) den Java-Datentypen bzw. den Attribute-Unterklassen zuordnet. Dem Copy-Konstruktor einer Attribute-Unterklasse wird ein UnknownAttribute übergeben, welches die Attribut-Metadaten hält.
  
- Die eben beschriebene getAttribute()-Methode wird in der gleichnamigen Methode der jeweiligen Datenbankversionsklasse aufgerufen.
  
- Alle nicht aufgelisteten Datenbank-Datentypen bleiben zuerst ein UnknownAttribute. Im nächsten Schritt wird ihnen ggf. über den Fallback-Java-Klassennamen eine Attribute-Unterklasse zugeordnet. Funktioniert dieses nicht, bleiben sie ein UnknownAttribute und können nicht anonymisiert werden!
  
- Die Mapping-Methode wird auch dazu genutzt, Änderungen an den Metadaten durchzuführen.
  - Bsp. MySQL: JDBC kennt keine unsigned Attribute! Man muss bei diesen Attributen den JDBC-Datentypen VARCHAR verwenden.
  - Bsp. Oracle Datentyp NUMBER: Eine Precision von 0 steht für den Defaultwert 38.

### 3. Neue Attribute-Unterklasse

- Zu jedem Datenbank-Datentyp gehört ein Java-Datentyp bzw. eine Attribute-Unterklasse.

- Die neue Klasse muss von der abstrakten Klasse **Attribute** erben. Sie muss im eigenen Konstruktor den Copy-Konstruktor von Attribute aufrufen und ein Attribute-Objekt, einen JavaDataType und eine GuiID übergeben.
  - Jede Attribute-Unterklasse braucht ein Enum-Objekt im Enum **JavaDataType**. Hier findet sich auch das Mapping für die Fallback-Java-Klassennamen (Siehe Methode getAttributeFromClassName()).
  - Die **GuiID** entscheidet, welchen Konfigurationsbereich und welches Objekt zum Speichern der Eingaben die Klasse verwendet (Siehe Kapitel 5).
    
- Ein Beispiel ist die Klasse StringAttribute.

- Es können mehrere Methoden überschrieben werden:
  - Methode **getDataTypeInformation()** gibt Informationen zum Datentypen zurück, die in der TablesView angezeigt werden.
  - Methode **getConstValue()** gibt einen konstanten Wert zurück, der für die Anonymisierung verwendet wird. Alle Eingabewerte aus den Konfigurationsbereichen finden sich in der Instanzvariablen **anonymizationOptionData**, die entsprechend gecastet werden muss, um an die Getter-Methoden zu kommen.
  - Methode **getRndValue()** gibt einen zufälligen Wert zurück, der für die Anonymisierung verwendet wird. Aus dem **anonymizationOptionData**-Objekt können die Nutzereingaben entnommen werden, die entscheiden, wie die zufälligen Werte generiert werden sollen. Die Methoden zum Generieren von zufälligen Werten finden sich in der Klasse **RndValueGenerator**.
    - Es können zusätzliche Metadaten für die Anonymisierung verwendet werden. Siehe beispielhaft die **additionalDataMap** der Klasse StringAttribute, die im Java-Package **additionaldata** enthaltenen Klassen und die **getRndAnonymizationDataIDList()**-Methode der GuiID-Klasse.
    
- In einer Attribute-Unterklasse können weitere Instanzvariablen und Methoden zum Extrahieren von Metadaten hinzugefügt werden (Siehe Klasse MysqlEnumAttribute).
  - Um eine SELECT-Query auszuführen, muss der Datenbankversionsklasse (Bsp. Klasse Mysql56) eine weitere Methode hinzugefügt werden. Hierfür muss ein Interface verwendet werden wie z.B. **MysqlAdditionalMetadata**, welches zum RDBMS MySQL gehört. Das Objekt vom Typen DatabaseVersion muss in ein Objekt vom Typen MysqlAdditionalMetadata gecastet werden, um an die Query-Methode zu gelangen.

### 4. Neue Anonymisierungsoption

- Das Enum **OptionID** erweitern und einen lesbaren Namen angeben. Das Enum-Objekt muss die Methode **createAnonymizationOption()** implementieren und eine **AnonymizationOption**-Unterklasse zurückgeben.
  
- Die neue Anonymisierungsoptionen-Klasse muss von der abstrakten Klasse **AnonymizationOption** erben. Im Konstruktor muss die OptionID übergeben werden und es muss angegeben werden, ob die Option Primärschlüssel benötigt.
  
- Es müssen mehrere Methoden implementiert werden (Als Beispiel siehe NullOption und RndOption):
  - Die Methode **getOptionData()** gibt das Objekt zurück, welches die Nutzereingaben in den Konfigurationsbereichen speichert. Es kann für alle Attribute-Unterklassen dasselbe Objekt zurückgegeben werden oder es kann für jede Attribute-Unterklasse, mithilfe des Enums GuiID, ein eigenes Objekt zurückgegeben werden (Siehe Kapitel 5).
  - Die Methode **getOptionComponent()** gibt den Konfigurationsbereich zurück (eine Vaadin Komponente), in welchem der Anwender die Anonymisierungsoption konfigurieren kann. Wie bei getOptionData() kann die GuiID verwendet werden, damit jede Attribute-Unterklasse einen eigenen Konfigurationsbereich bekommt.
  - Die Methode **update()** führt das eigentliche Datenbankupdate aus. Ihr wird das Attribut übergeben, welches anonymisiert werden soll. Hier findet sich die Logik, um weitere Metadaten zu extrahieren, um Listen mit zufälligen Werten zu füllen und um Batch-Updates auszuführen.
  - Die Methode **enableRadioButton()** entscheidet, ob die Anonymisierungsoption durch den Nutzer in der Radio-Button-Gruppe (AnonymizationView) ausgewählt werden darf.
    - Bsp.: Darf das Attribut nicht NULL werden, so kann auch nicht die NullOption verwendet werden.

### 5. GuiID und Konfigurationsbereiche

- Die Anwender werden in den Konfigurationsbereichen über die Anonymisierungsoptionen informiert und sie können diese konfigurieren. Die Eingaben werden in einem separaten Objekt gespeichert.
  
- Welche Attribute-Unterklasse welchen Konfigurationsbereich verwendet, wird in der **GuiID** festgelegt. Mit der GuiID können Attribute-Unterklassen gruppiert werden, die den gleichen Konfigurationsbereich haben (Bsp. Alle Dezimalzahlen wie Float, Double und BigDecimal haben die GuiID DECIMAL).
  
- Die einzelnen Enum-Objekte können vier Methoden implementieren:
  - Die Methoden **getConstOptionData()** und **getConstOptionComponent()** beziehen sich auf die ConstOption.
  - Die Methoden **getRndOptionData()** und **getRndOptionComponent()** beziehen sich auf die RndOption.
  - Eine neue Anonymisierungsoption muss eventuell weitere Methoden hinzufügen, falls sie verschiedene Konfigurationsbereiche unterstützt.
    
- Die Klassen, die von den Methoden zurückgegeben werden, erben entweder von der abstrakten Klasse **OptionData** oder **OptionComponent**.
  - OptionData gibt die Methode **isValid()** vor, die überprüft, ob Eingabewerte fehlen oder falsch sind (Bzw. ob die Werte NULL sind!). Siehe z.B. Klasse TimeConstData.
  - OptionComponent kommt mit zwei Methoden zum Bauen der Anonymisierungsoptionen-Info und der Check-Constraints-Tabelle. Siehe die Beispielklasse TimeConstGui und TimeRndData.
    - Es wurde versucht alle GUI-Strings der Anwendung in die Klasse **StringConstants** auszulagern.
  - Die Klassen finden sich in den Packages der Anonymisierungsoptionen (Bsp. nulloption, rndoption).
