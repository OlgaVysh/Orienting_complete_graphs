
### Projektaufbau

Dieses Projekt nutzt JDK 21. In unserer Erfahrung funktioniert ortools zur Lösung eines SAT-Modells mit früheren JDK Versionen nicht.

### Punktmengen

Importiere: 
```java
import Points.*;
```

Eine Punktmenge `P = [(0,0),(0,1),(1,0),(1,1)]` wird folgendermaßen deklariert:

```java
List<Point> points = Arrays.asList(
    new Point(0,0),
    new Point(0,1),
    new Point(1,0),
    new Point(1,1)
);
```

Um eine zufällige Punktmenge der Größe `int size` zu generieren:

```java
List<Point> points = PointGenerator.generateUniquePoints(size);
```

Die Klasse kann außerdem konvexe Punktmengen eines bestimmten Radius `int r` und einer bestimmten Größe `int size` erzeugen:

```java
List<Point> points = PointGenerator.generateConvexPoints(r, size);
```

Falls `r = 0` angegeben wird, wird der Radius zufällig generiert.

### Projektstruktur

In der folgenden Tabelle sind die vier Hauptalgorithmen und ihre main-Methoden aufgelistet. Weitere Klassen stellen
entweder Hilfsklassen oder andere Versionen der Algorithmen dar, z.B. wie `SAT` und `SAT34`.
In der Dokumentation zu jeder Klasse ist angegeben, welcher Algorithmus dort implementiert wird und wie er performt.

| Package         | Class              | Main Method(s) |
|---------------|----------------|----------------------------------------------|
| BruteForce    | BruteForceAlgorithm | `bruteforce(points)` – liefert die beste mögliche ODIL zurück |
| ILP          | SAT              | `new SAT(points).binarySearch()` – liefert die beste mögliche ODIL und max. Kantenanzahl im Zyklus zurück (betrachtet alle Zyklen) |
| ILP          | SAT34              | `new SAT34(points).binarySearch()` – liefert die beste mögliche ODIL zurück (betrachtet Zyklen mit 3&4 Kanten) |
| GreedyEdges  | GreedyAlgorithm   | `orient(points)` – erzeugt einen stark zusammenhängenden Graphen G auf P und liefert seine ODIL zurück |
| TwoApproximation | ApproxBrute    | `orient(points)` – liefert ODIL(G auf P)<=2 zurück |

Package `GreedyVersions` enthält andere Versionen der beiden gierigen Algorithmen, die sich nach unseren Experimenten nicht durchgesetzt haben.

### Brute-Force-Algorithmus

Importiere: 
```java
import BruteForce.BruteForceAlgorithm;
```

Die Methode `bruteforce()` in der Klasse `BruteforceAlgorithm` liefert die beste Dilatation der Punktmenge zurück:

```java
double dilation = BruteforceAlgorithm.bruteforce(points);
```

### SAT-Modellierung

Das folgende Modell betrachtet geschlossenen Wege mit 3 bis n Kanten. Dabei werden die Wege mit i Kanten erst betrachtet,
wenn das Model für i-1 nicht gelöst wurde.

Importiere: 
```java
import ILP.SAT;
```

Um mit SAT die minimale Dilatation einer Punktmenge zu bestimmen, muss zunächst ein Objekt der Klasse `SAT` erstellt werden:

```java
SAT model = new SAT(points);
```

Anschließend wird mithilfe der binären Suche der kleinste Wert für `t` und die höchste Anzahl der Kanten im geschlossenen 
Weg gesucht.

```java
double[] results = model.binarySearch(); // gibt double[]{result,length} zurück
double odil = results[0]; //hole odil
int maxEdges = results[1]; //hole max. Kantenanzahl
```

Das folgende Modell löst jedes Modell direkt für Kreise mit 3 und 4 Kanten.

Importiere: 
```java
import ILP.SAT34;
```

Um mit SAT34 die minimale Dilatation einer Punktmenge zu bestimmen, muss zunächst ein Objekt der Klasse `SAT34` erstellt werden:

```java
SAT34 model = new SAT34(points);
```

Anschließend wird mithilfe der binären Suche der kleinste Wert für `t` gesucht:

```java
double odil = model.binarySearch(); 
```

### 2-Approximation

Importiere: 
```java
import TwoApproximation.ApproxBrute;
```

Der Hauptalgorithmus ist in der Klasse `ApproxBrute` implementiert. Um eine Orientierung für eine Punktmenge `points` zu erzeugen und die Dilatation zu berechnen:

```java
double dilation = ApproxBrute.orient(points);
```
Die Klasse `TwoApproximationAlgorithm` implementiert die Grundmethoden des Algorithmus und stellt diese zur Verfügung. 
`ApproxBrute` erweitert den 2-Approximationsalgorithmus um Bruteforce.


### Gierige Kanten

Importiere: 
```java
import GreedyEdges.GreedyAlgorithm;
```

Der Hauptalgorithmus ist in der Klasse `GreedyAlgorithm` implementiert. Um eine Orientierung für eine Punktmenge `points` zu erzeugen und die Dilatation zu berechnen:

```java
double dilation = GreedyAlgorithm.orient(points);
```

### Orientierte Dilatation messen

Importiere: 
```java
import Dilation.OrientedDilation;
```

Falls bereits eine Punktmenge `points` und eine bestehende Orientierung `String orientation` vorliegen, kann die orientierte Dilatation wie folgt berechnet werden:

```java
double dilation = OrientedDilation.calculateDilation(points, orientation);
```

### Punktmenge aus einer Datei einlesen

Importiere: 
```java
import InputOutputHandler.Reader;
```

Die Klasse `Reader` liest alle in einer Datei unter `String filePath` gespeicherten Punktmengen ein und speichert sie in einer Liste:

```java
List<List<Point>> points = Reader.readPointSet(filePath);
```

Um die erste Punktmenge zu erhalten:

```java
List<Point> pointSetOne = points.get(0);
```

### Messergebnisse speichern

Importiere: 
```java
import InputOutputHandler.Writer;
```

Die Klasse `Writer` bietet mehrere Möglichkeiten, Ergebnisse in einer Datei unter `String filePath` zu speichern:

- **Einen einzelnen Wert speichern:**

  ```java
  Writer.saveValue(dilation, filePath);
  ```

- **Eine Punktmenge und ihre Dilatation speichern:**

  ```java
  Writer.savePointSet(points, dilation, filePath);
  ```

- **Ein vollständiges Ergebnis speichern:**

  ```java
  Writer.saveResult(numberOfPoints, avg, min, max, filePath);
  ```

  Dabei wird in der Datei die folgende Zeile gespeichert:

  ```
  numberOfPoints;avg;min;max
  ```

### Beispiele

Die wichtigsten Klassen haben eine `main`-Methode, die veranschaulicht, wie diese zu benutzen ist.

In der `Main`-Klasse ist folgende Funktionalität für die Experimente implementiert. Die `main`-Methode erzeugt 100 Punktmengen der Größe 
4 bis 10. Von jeder Punktmenge wird die minimale odil mit SAT bestimmt sowie die odil der Orientierungen von 
2-Approximationsalgorithmus und Gierige Kanten. Für jede Punktmengengröße wird min, avg und max bestimmt und in eine Datei 
geschrieben, die unter Algorithmennamen in `src/main/resources` angelegt ist.


### Erweiterung für 3D Punktmengen

Die Experimente mit 3D Punktmengen habe ich auf einem zusätzlichen Recher durchgeführt.

Da Java kein Nullable erlaubt, müssen folgende Klassen bzw. Methoden in der Klasse `Points.Point` angepasst werden, um die Implementierung
für 3D Punktmengen zu ermöglichen:

- Füge `double z` dem Klassenrumpf hinzu:

```java
public record Point(double x, double y, double z)
```

- Erweitere die beiden Methoden der Klasse um dritte Koordinate: 

```java
  public String toString() {
        return String.format(Locale.US, "(%.3f, %.3f, %.3f)", x, y, z);
    }

    public double calculateDistance(Point p1) {
        double dx = p1.x - this.x;
        double dy = p1.y - this.y;
        double dz = p1.z - this.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
  ```

Andere Klassen und Methoden müssen nicht angepasst werden und funktionieren weiterhin.
