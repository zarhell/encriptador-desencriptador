
# Sistema de Encriptación y Desencriptación

Este proyecto es un sistema de encriptación y desencriptación que utiliza varios algoritmos, incluyendo Huffman. A continuación se detalla el algoritmo de Huffman tanto en teoría como en la implementación de este proyecto.

## Tabla de Contenidos

- [Marco Teórico](#marco-teórico)
  - [Algoritmo de Huffman](#algoritmo-de-huffman)
- [Implementación](#implementación)
  - [Estructura del Proyecto](#estructura-del-proyecto)
  - [Clases Principales](#clases-principales)
  - [Ejemplo de Uso](#ejemplo-de-uso)

## Marco Teórico

### Algoritmo de Huffman

El algoritmo de Huffman es un método de compresión sin pérdida que se utiliza para reducir el tamaño de los datos. El algoritmo se basa en la frecuencia de aparición de cada símbolo en los datos. A continuación se describe el proceso paso a paso:

1. **Frecuencia de Caracteres**: Se calcula la frecuencia de cada carácter en el conjunto de datos.
2. **Construcción del Árbol de Huffman**:
   - Se crea un nodo hoja para cada carácter y se almacena en una cola de prioridad.
   - Se extraen los dos nodos con menor frecuencia de la cola.
   - Se crea un nuevo nodo con estos dos nodos como hijos y con una frecuencia igual a la suma de sus frecuencias.
   - Este nuevo nodo se inserta de nuevo en la cola.
   - Se repiten los pasos anteriores hasta que solo quede un nodo en la cola, que será la raíz del árbol de Huffman.
3. **Generación de Códigos de Huffman**: Se asigna un código binario a cada carácter basado en su posición en el árbol. La regla es:
   - Cada movimiento a la izquierda añade un `0` al código.
   - Cada movimiento a la derecha añade un `1` al código.
4. **Codificación de los Datos**: Se reemplaza cada carácter de los datos originales por su código de Huffman correspondiente.
5. **Decodificación**: Para decodificar los datos, se recorre el árbol de Huffman desde la raíz hasta llegar a una hoja, usando los bits del código binario.

## Implementación

### Estructura del Proyecto

```
└───src
├───main
│   ├───java
│   │   └───com
│   │       └───encriptadordesencriptador
│   │           ├───application
│   │           │   └───port
│   │           ├───domain
│   │           │   ├───model
│   │           │   └───service
│   │           └───infrastructure
│   │               ├───adapter
│   │               ├───cli
│   │               └───config
```

### Clases Principales

#### Clase `CliRunner`

Ubicación: `src/main/java/com/encriptadordesencriptador/infrastructure/cli/CliRunner.java`

```java
package com.encriptadordesencriptador.infrastructure.cli;

import com.encriptadordesencriptador.application.port.EncryptionServicePort;
import com.encriptadordesencriptador.application.port.FileServicePort;
import com.encriptadordesencriptador.domain.service.EncryptionService;
import com.encriptadordesencriptador.domain.service.FileService;
import com.encriptadordesencriptador.infrastructure.adapter.CipherAdapter;

import java.util.Scanner;

public class CliRunner {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EncryptionServicePort encryptionService = new EncryptionService(new CipherAdapter());
    private static final FileServicePort fileService = new FileService();

    public static void main(String[] args) {
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleEncryption();
                    break;
                case "2":
                    handleDecryption();
                    break;
                case "3":
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida, por favor intente nuevamente.");
            }
        }
    }

    private static void handleEncryption() {
        System.out.print("Ingrese el texto a encriptar: ");
        String text = scanner.nextLine();

        printEncryptionOptions();

        String generalKey = encryptionService.getEncryptionKeyFromUser();

        String encryptedText = encryptionService.encryptText(text, generalKey);
        System.out.println("Texto encriptado: " + encryptedText);

        fileService.saveToFile(encryptedText);
    }

    private static void handleDecryption() {
        printEncryptionOptions();

        String generalKey = encryptionService.getEncryptionKeyFromUser();

        String encryptedText = fileService.readFromFile();
        if (encryptedText == null) {
            System.out.println("No se pudo leer el texto encriptado desde el archivo.");
            return;
        }

        String decryptedText = encryptionService.decryptText(encryptedText, generalKey);
        System.out.println("Texto desencriptado: " + decryptedText);
    }

    private static void printMainMenu() {
        System.out.println("=== Sistema de Encriptación ===");
        System.out.println("+-------------------------------------------+");
        System.out.println("| Índice | Opción                           |");
        System.out.println("+-------------------------------------------+");
        System.out.println("|   1    | Encriptar texto                  |");
        System.out.println("|   2    | Desencriptar texto desde archivo |");
        System.out.println("|   3    | Salir                            |");
        System.out.println("+-------------------------------------------+");
        System.out.print("Seleccione una opción: ");
    }

    private static void printEncryptionOptions() {
        System.out.println("+-----------------------+");
        System.out.println("| Índice | Tipo         |");
        System.out.println("+-----------------------+");
        System.out.println("|   1    | Caesar       |");
        System.out.println("|   2    | Vigenere     |");
        System.out.println("|   3    | Transposition|");
        System.out.println("|   4    | Huffman      |");
        System.out.println("+-----------------------+");
        System.out.print("Seleccione el tipo de encriptación: ");
    }
}
```

#### Clase `EncryptionService`

Ubicación: `src/main/java/com/encriptadordesencriptador/domain/service/EncryptionService.java`

```java
package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.EncryptionServicePort;
import com.encriptadordesencriptador.application.port.CipherUseCase;
import com.encriptadordesencriptador.domain.model.CipherText;
import com.encriptadordesencriptador.domain.model.GeneralKey;
import com.encriptadordesencriptador.domain.model.PlainText;

import java.util.Scanner;

public class EncryptionService implements EncryptionServicePort {

    private static final Scanner scanner = new Scanner(System.in);
    private final CipherUseCase cipherUseCase;

    public EncryptionService(CipherUseCase cipherUseCase) {
        this.cipherUseCase = cipherUseCase;
    }

    @Override
    public String getEncryptionKeyFromUser() {
        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                return "caesar";
            case "2":
                return "vigenere";
            case "3":
                return "transposition";
            case "4":
                return "huffman";
            default:
                System.out.println("Opción no válida, se utilizará 'caesar' por defecto.");
                return "caesar";
        }
    }

    @Override
    public String encryptText(String text, String generalKey) {
        PlainText plainText = new PlainText(text);
        GeneralKey key = new GeneralKey(generalKey);
        CipherText encryptedText = cipherUseCase.encrypt(plainText, key);
        return encryptedText.getValue();
    }

    @Override
    public String decryptText(String encryptedText, String generalKey) {
        CipherText cipherText = new CipherText(encryptedText);
        GeneralKey key = new GeneralKey(generalKey);
        PlainText decryptedText = cipherUseCase.decrypt(cipherText, key);
        return decryptedText.getValue();
    }
}
```

#### Clase `FileService`

Ubicación: `src/main/java/com/encriptadordesencriptador/domain/service/FileService.java`

```java
package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.FileServicePort;

import java.io.IOException;
import java.nio.file.*;

public class FileService implements FileServicePort {

    private static final String FILE_NAME = "encryptedText.txt";

    @Override
    public void saveToFile(String text) {
        try {
            Path path = getWritablePath(FILE_NAME);
            Files.write(path, text.getBytes());
            System.out.println("Texto encriptado guardado en " + path.toString());
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo: " + e.getMessage());
        }
    }

    @Override
    public String readFromFile() {
        try {
            Path path = getWritablePath(FILE_NAME);


            return Files.readString(path);
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return null;
        }
    }

    private Path getWritablePath(String fileName) throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), fileName);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }
}
```

# HuffmanCipherService

El `HuffmanCipherService` es un servicio que implementa el algoritmo de compresión de Huffman para encriptar y desencriptar texto. A continuación se detalla cómo funciona este servicio paso a paso.

## Clase `HuffmanCipherService`

### Estructura Principal

La clase `HuffmanCipherService` implementa la interfaz `Cipher` y contiene los métodos necesarios para encriptar y desencriptar texto usando el algoritmo de Huffman. La estructura principal incluye:

- Un mapa `huffmanCodes` para almacenar los códigos de Huffman generados.
- Un nodo `root` que representa la raíz del árbol de Huffman.

### Clase Interna `Node`

La clase `Node` es una clase estática interna que representa un nodo en el árbol de Huffman. Cada nodo tiene:

- `character`: El carácter que representa (solo para nodos hoja).
- `frequency`: La frecuencia de aparición del carácter.
- `leftChild` y `rightChild`: Los hijos izquierdo y derecho del nodo.

```java
private static class Node implements Comparable<Node> {
    final char character;
    final int frequency;
    final Node leftChild, rightChild;

    Node(char character, int frequency, Node leftChild, Node rightChild) {
        this.character = character;
        this.frequency = frequency;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}
```

### Método `encrypt`

El método `encrypt` encripta el texto dado utilizando el algoritmo de Huffman.

```java
@Override
public String encrypt(String text) {
    Map<Character, Integer> frequencyMap = buildFrequencyMap(text);
    root = buildHuffmanTree(frequencyMap);
    huffmanCodes = generateHuffmanCodes(root);
    return encodeText(text);
}
```

1. **`buildFrequencyMap`**: Construye un mapa de frecuencias de caracteres a partir del texto dado.
2. **`buildHuffmanTree`**: Construye el árbol de Huffman a partir del mapa de frecuencias.
3. **`generateHuffmanCodes`**: Genera los códigos de Huffman a partir del árbol de Huffman.
4. **`encodeText`**: Codifica el texto utilizando los códigos de Huffman generados.

### Método `decrypt`

El método `decrypt` desencripta el texto encriptado utilizando el árbol de Huffman.

```java
@Override
public String decrypt(String encodedText) {
    return decodeText(encodedText);
}
```

1. **`decodeText`**: Decodifica el texto encriptado utilizando el árbol de Huffman.

### Métodos Auxiliares

#### `buildFrequencyMap`

Construye un mapa de frecuencias de caracteres a partir del texto dado.

```java
private Map<Character, Integer> buildFrequencyMap(String text) {
    return text.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
}
```

#### `buildHuffmanTree`

Construye el árbol de Huffman a partir del mapa de frecuencias.

```java
private Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
    PriorityQueue<Node> priorityQueue = frequencyMap.entrySet().stream()
            .map(entry -> new Node(entry.getKey(), entry.getValue(), null, null))
            .collect(Collectors.toCollection(PriorityQueue::new));

    while (priorityQueue.size() > 1) {
        Node leftChild = priorityQueue.poll();
        Node rightChild = priorityQueue.poll();
        Node parentNode = new Node('\0', leftChild.frequency + rightChild.frequency, leftChild, rightChild);
        priorityQueue.add(parentNode);
    }

    return priorityQueue.poll();
}
```

1. **Cola de Prioridad**: Se crea una cola de prioridad que contiene nodos para cada carácter, ordenados por frecuencia.
2. **Construcción del Árbol**: Se extraen los dos nodos con menor frecuencia y se crean nodos padres hasta que quede solo uno en la cola, que será la raíz del árbol.

#### `generateHuffmanCodes`

Genera los códigos de Huffman a partir del árbol de Huffman.

```java
private Map<Character, String> generateHuffmanCodes(Node root) {
    Map<Character, String> huffmanCodes = new HashMap<>();
    buildHuffmanCode(root, "", huffmanCodes);
    return huffmanCodes;
}
```

#### `buildHuffmanCode`

Construye los códigos de Huffman recursivamente.

```java
private void buildHuffmanCode(Node node, String code, Map<Character, String> huffmanCodes) {
    if (node.isLeaf()) {
        huffmanCodes.put(node.character, code);
    } else {
        buildHuffmanCode(node.leftChild, code + '0', huffmanCodes);
        buildHuffmanCode(node.rightChild, code + '1', huffmanCodes);
    }
}
```

1. **Recursión**: Se recorre el árbol de Huffman de manera recursiva para construir los códigos binarios para cada carácter.

#### `encodeText`

Codifica el texto utilizando los códigos de Huffman generados.

```java
private String encodeText(String text) {
    return text.chars()
            .mapToObj(c -> (char) c)
            .map(huffmanCodes::get)
            .collect(Collectors.joining());
}
```

1. **Mapeo de Caracteres**: Cada carácter del texto se reemplaza por su código de Huffman correspondiente.

#### `decodeText`

Decodifica el texto encriptado utilizando el árbol de Huffman.

```java
private String decodeText(String encodedText) {
    return encodedText.chars()
            .mapToObj(bit -> (char) bit)
            .map(bit -> bit == '0' ? '0' : '1')
            .collect(() -> new Object[]{new StringBuilder(), root}, (acc, bit) -> {
                Node currentNode = (Node) acc[1];
                currentNode = bit == '0' ? currentNode.leftChild : currentNode.rightChild;
                if (currentNode.isLeaf()) {
                    ((StringBuilder) acc[0]).append(currentNode.character);
                    acc[1] = root;
                } else {
                    acc[1] = currentNode;
                }
            }, (acc1, acc2) -> {
                throw new UnsupportedOperationException("Parallel execution not supported");
            })[0].toString();
}
```

1. **Recorrido del Árbol**: Se recorre el árbol de Huffman utilizando los bits del texto encriptado hasta llegar a los nodos hoja, donde se encuentran los caracteres originales.

### Ejemplo de Uso

Para ejecutar el programa, compila y ejecuta la clase `CliRunner`. El programa presentará un menú en la consola donde podrás seleccionar la opción de encriptar texto, desencriptar texto desde un archivo o salir del programa. Cuando elijas encriptar o desencriptar, se te pedirá que selecciones el tipo de encriptación y que ingreses el texto o la clave correspondiente.

```sh
javac -d bin src/main/java/com/encriptadordesencriptador/infrastructure/cli/CliRunner.java
java -cp bin com.encriptadordesencriptador.infrastructure.cli.CliRunner
```

Sigue las instrucciones en la consola para interactuar con el sistema de encriptación y desencriptación.

### Ejemplo de Flujo en la Consola

```
=== Sistema de Encriptación ===
+-------------------------------------------+
| Índice | Opción                           |
+-------------------------------------------+
|   1    | Encriptar texto                  |
|   2    | Desencriptar texto desde archivo |
|   3    | Salir                            |
+-------------------------------------------+
Seleccione una opción: 1
Ingrese el texto a encriptar: Hola Mundo
+-----------------------+
| Índice | Tipo         |
+-----------------------+
|   1    | Caesar       |
|   2    | Vigenere     |
|   3    | Transposition|
|   4    | Huffman      |
+-----------------------+
Seleccione el tipo de encriptación: 4
Texto encriptado: 011010100110110101010
Texto encriptado guardado en /home/usuario/encryptedText.txt
```
