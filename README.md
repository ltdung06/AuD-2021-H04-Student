# Community Tests für die vierte Hausübung der AuD 2021

Zum Ausführen der Tests sollte eine [eigene JUnit Run Configuration](https://git.rwth-aachen.de/groups/aud-tests/-/wikis/JUnit-Run-Configuration) angelegt werden, da durch den gradle task nicht alle Meldungen angezeigt werden (z.B. warum Tests ignoriert werden).

Da einige Methoden intern sehr komplex aufgebaut sind und man mit Black-box testing (worunter diese Tests fallen) dabei nur bedingt Aussagen über die Korrektheit treffen kann, wird bei den folgenden Tests nur geprüft, ob die jeweiligen Definitionen korrekt sind:
- [`SortingExperiment`](src/test/java/h04/SortingExperimentTest.java)
- ([`LinearInterpolation`](src/test/java/h04/function/LinearInterpolationTest.java), [siehe Beschreibung weiter unten](#linearinterpolationtest))

Fehler der Form `"XYZ has no field matching the criteria specified by the assignment"` deuten darauf hin, dass eine Klasse nicht das gesuchte Attribut besitzt, entweder weil es nicht existiert oder weil es nicht alle vorausgesetzten Modifier hat (z.B. wird eine Konstante verlangt, aber das Attribut wurde nicht als `final` deklariert).

Mit * markierte Methoden testen, ob die jeweilige Klasse bzw. Interface korrekt definiert ist. Sie sind an sich keine Testmethoden und nur in Verbindung mit "richtigen" Tests, also mit `@Test` oder vergleichbar annotierten Methoden, brauchbar.

Die Tests kommen mit einem kleinen Tool, das vor jeder Ausführung nach Updates sucht. Da das einige Sekunden in Anspruch nehmen kann und vielleicht auch anderweitig nicht gewünscht ist, kann diese Funktionalität ausgeschaltet werden, indem die Konstante `CHECK_FOR_UPDATES` in [`Utils.java`](src/test/java/h04/Utils.java) auf `false` gesetzt wird. Sollte das Repository nicht geklont oder als remote eingebunden werden, muss die Datei [`.test_version`](.test_version) im Stammverzeichnis des Repositorys ebenfalls kopiert und im Projektverzeichnis abgelegt werden, ansonsten wird die Update-Suche immer fehlschlagen.

DISCLAIMER: Das Durchlaufen der Tests ist keine Garantie dafür, dass die Aufgaben vollständig korrekt implementiert sind.

<br>

## Package `h04`

-----

## SortingExperimentTest

### .checkClass()* / .classDefinitionCorrect()
Überprüft, ob die Definition der Klasse `SortingExperiment` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse nicht generisch ist
- die Klasse nicht abstrakt ist
- die im Übungsblatt vorausgesetzten statischen Methoden `main(String[])` und `computeOptimalThresholds(int, int, int, double)` vorhanden und richtig definiert sind

<br>

## Package `h04.function`

-----

## DoubleToIntFunctionTest

### .checkInterface()* / .interfaceDefinitionCorrect()
Überprüft, ob die Definition des Interfaces `DoubleToIntFunction` korrekt ist. Testet, dass …
- das Interface public ist
- das Interface nicht generisch ist
- die im Übungsblatt vorausgesetzte Methode `apply(double)` vorhanden und richtig definiert ist

<br>

## ArrayDoubleToIntFunctionTest

Setzt voraus, dass `DoubleToIntFunction` richtig definiert ist.

### .checkClass()*
Überprüft, ob die Definition der Klasse `ArrayDoubleToIntFunction` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse nicht generisch ist
- die Klasse das Interface `DoubleToIntFunction` implementiert und nicht abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `int[]` als ersten Parameter hat

### ApplyTests

Testet die Methode `apply(double)`.

##### .testIllegalArguments()
Überprüft, ob die Methode korrekterweise eine `IllegalArgumentException` wirft.

##### .testValidArguments()
Überprüft, ob die Methode die richtigen Werte für gegebene Werte zurückgibt.

<br>

## LinearDoubleToIntFunctionTest

Setzt voraus, dass `DoubleToIntFunction` richtig definiert ist.

### .checkClass()*
Überprüft, ob die Definition der Klasse `LinearDoubleToIntFunction` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse nicht generisch ist
- die Klasse das Interface `DoubleToIntFunction` implementiert und nicht abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `double` als ersten und zweiten Parameter hat
- die Klasse zwei Objektkonstanten `a` und `b` vom Typ `double` hat

### .testFields()
Überprüft, ob der Konstruktor die Konstanten richtig initialisiert.

### ApplyTests

Testet die Methode `apply(double)`.

##### .testIllegalArguments()
Überprüft, ob die Methode korrekterweise eine `IllegalArgumentException` wirft.

##### .testValidArguments()
Überprüft, ob die Methode die richtigen Werte für gegebene Werte zurückgibt.

<br>

## ListToIntFunctionTest

### .checkInterface()* / .interfaceDefinitionCorrect()
Überprüft, ob die Definition des Interfaces `ListToIntFunction` korrekt ist. Testet, dass …
- das Interface public ist
- das Interface generisch ist und einen Typparameter `T` hat
- die im Übungsblatt vorausgesetzte Methode `apply(List)` vorhanden und richtig definiert ist

<br>

## ConstantListToIntFunctionTest

Setzt voraus, dass `ListToIntFunction` richtig definiert ist.

### .checkClass()*
Überprüft, ob die Definition der Klasse `ConstantListToIntFunction` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse generisch ist und einen Typparameter `T` hat
- die Klasse das Interface `ListToIntFunction` implementiert und nicht abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `int` als ersten Parameter hat
- die Klasse eine private Objektkonstante vom Typ `int` hat

### .testIntField()
Überprüft, ob der Konstruktor die Konstante richtig initialisiert.

### ApplyTests

Testet die Methode `apply(List)`.

##### .testIllegalArguments()
Überprüft, ob die Methode korrekterweise eine `NullPointerException` wirft.

##### .testValidArguments()
Überprüft, ob die Methode den richtigen Wert für eine gegebene Liste zurückgibt.

<br>

## FunctionOnDegreeOfDisorderTest

Setzt voraus, dass `ListToIntFunction` richtig definiert ist.

### .checkClass()* / .classDefinitionCorrect()
Überprüft, ob die Definition der Klasse `FunctionOnDegreeOfDisorder` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse generisch ist und einen Typparameter `T` hat
- die Klasse das Interface `ListToIntFunction` implementiert aber abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `Comparator<? super T>` als ersten Parameter hat
- die Klasse eine protected Objektkonstante vom Typ `Comparator<? super T>` hat

<br>

## FunctionOnRatioOfRunsTest

Setzt voraus, dass `FunctionOnDegreeOfDisorder` und `DoubleToIntFunction` richtig definiert sind.

### .checkClass()*
Überprüft, ob die Definition der Klasse `FunctionOnRatioOfRuns` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse generisch ist und einen Typparameter `T` hat
- die Klasse von `FunctionOnDegreeOfDisorder` erbt und nicht abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `DoubleToIntFunction` als ersten und `Comparator<? super T>` als zweiten Parameter hat
- die Klasse eine private Objektkonstante vom Typ `DoubleToIntFunction` hat

### .testDoubleToIntFunctionField()
Überprüft, ob der Konstruktor die Konstante richtig initialisiert.

### ApplyTests

Testet die Methode `apply(List)`.

##### .testIllegalArguments()
Überprüft, ob die Methode korrekterweise eine `NullPointerException` wirft.

##### .testValidArguments()
Überprüft, ob die Methode den richtigen Wert für eine gegebene Liste zurückgibt.

<br>

## FunctionOnRatioOfInversionsTest

Setzt voraus, dass `FunctionOnDegreeOfDisorder` und `DoubleToIntFunction` richtig definiert sind.

### .checkClass()*
Überprüft, ob die Definition der Klasse `FunctionOnRatioOfInversions` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse generisch ist und einen Typparameter `T` hat
- die Klasse von `FunctionOnDegreeOfDisorder` erbt und nicht abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `DoubleToIntFunction` als ersten und `Comparator<? super T>` als zweiten Parameter hat
- die Klasse eine private Objektkonstante vom Typ `DoubleToIntFunction` hat

### .testDoubleToIntFunctionField()
Überprüft, ob der Konstruktor die Konstante richtig initialisiert.

### ApplyTests

Testet die Methode `apply(List)`.

##### .testIllegalArguments()
Überprüft, ob die Methode korrekterweise eine `NullPointerException` wirft.

##### .testValidArguments()
Überprüft, ob die Methode den richtigen Wert für eine gegebene Liste zurückgibt.

<br>

## DoubleToIntFunctionFitterTest

Setzt voraus, dass `DoubleToIntFunction` richtig implementiert ist.

### .checkInterface()* / .interfaceDefinitionCorrect()
Überprüft, ob die Definition des Interfaces `DoubleToIntFunctionFitter` korrekt ist. Testet, dass …
- das Interface public ist
- das Interface nicht generisch ist
- die im Übungsblatt vorausgesetzte Methode `fitFunction(Integer[])` vorhanden und richtig definiert ist

<br>

## LinearRegressionTest

Setzt voraus, dass `DoubleToIntFunctionFitter` und `LinearDoubleToIntFunction` richtig definiert sind.

### .checkClass()*
Überprüft, ob die Definition der Klasse `LinearRegression` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse nicht generisch ist
- die Klasse das Interface `DoubleToIntFunctionFitter` implementiert und nicht abstrakt ist

### .testFitFunction()
Überprüft, ob die Methode eine Instanz von `LinearDoubleToIntFunction` zurückgibt.
Weiter wird auf statistische Weise geprüft,
dass die Attribute des zurückgegebenen Objekts nicht zu weit abweichen.
Hierbei ist die Toleranzgrenze allerdings sehr hoch.
Um dem entgegenzuwirken,
wird dann noch die durchschnittliche Abweichung verglichen. \
*Wichtig:* Hierzu sind **viele Tests** vonnöten.

<br>

## LinearInterpolationTest

Setzt voraus, dass `DoubleToIntFunctionFitter` und `ArrayDoubleToIntFunction` richtig definiert sind.

### .checkClass()*
Überprüft, ob die Definition der Klasse `LinearInterpolation` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse nicht generisch ist
- die Klasse das Interface `DoubleToIntFunctionFitter` implementiert und nicht abstrakt ist

### .testFitFunction()
Überprüft, ob die Methode eine Instanz von `ArrayDoubleToIntFunction` zurückgibt und ob diese wiederum korrekte Werte zurückgibt (anhand eines gegebenen Arrays).

<br>

## Package `h04.collection`

-----

## ListItemTest

### .checkClass()* / .classDefinitionCorrect()
Überprüft, ob die Definition der Klasse `ListItem` korrekt ist. Testet, dass …
- die Klasse generisch ist und einen Typparameter `T` hat
- die Klasse nicht abstrakt ist
- die Klasse _nur_ den default-Konstruktor hat
- die Klasse _nur_ die Attribute `key` vom Typ `T`und `next` vom Typ `ListItem<T>` hat
- die Klasse keine Methoden besitzt

<br>

## MyCollectionsTest

Setzt voraus, dass `ListToIntFunction` und `ListItem` richtig definiert sind.

### .checkClass()*
Überprüft, ob die Definition der Klasse `LinearInterpolation` korrekt ist. Testet, dass …
- die Klasse public ist
- die Klasse generisch ist und einen Typparameter `T` hat
- die Klasse nicht abstrakt ist
- die Klasse einen public Konstruktor mit formalem Typ `ListToIntFunction` als ersten und `Comparator<? super T>` als zweiten Parameter hat
- die Klasse eine private Objektkonstante vom Typ `ListToIntFunction` und eine zwei vom Typ `Comparator<? super T>` hat
- die im Übungsblatt vorausgesetzten Methoden `sort()`, `adaptiveMergeSortInPlace(ListItem, int)` und `selectionSortInPlace(ListItem)` vorhanden und richtig definiert sind

### .testFields()
Überprüft, ob der Konstruktor die Konstanten richtig initialisiert.

### .testSort(List)
Überprüft, ob die Methode `sort()` eine gegebene Liste korrekt sortiert.

### .testAdaptiveMergeSortInPlace(List, Object)
Überprüft, ob die Methode `adaptiveMergeSortInPlace(ListItem, int)` eine gegebene verkettete Liste korrekt sortiert und dabei nur die vorhandenen Objekte nutzt.

### .testSelectionSortInPlace(List, Object)
Überprüft, ob die Methode `selectionSortInPlace(ListItem)` eine gegebene verkettete Liste korrekt sortiert und dabei nur die vorhandenen Objekte nutzt.
