import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;

class Pair {
    int first;
    int second;

    private Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }
}

public class alg {
    private static Map<String, List<String>> graph;
    static List<int[]> pairs = new ArrayList<>();

    private static String maxPathVertex = "";

    public static void main(String[] args) throws FileNotFoundException {
       

        File wyniki = new File("wyniki.txt");
        try {
            if (wyniki.createNewFile()) {
                System.out.println("File created: " + wyniki.getName());
            } else {
                System.out.println("File already exists.");
               
                wyniki.delete();
                wyniki.createNewFile();

            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        PrintWriter zapis = new PrintWriter(wyniki);
        zapis.println("nazwa:/n oczekiwana wartosc;znaleleziona, dlugosc danych, uzyte slowa, czas w ms");
        String Folder3 = "test/negatywnymi_losowymi";
        zapis.println("Folder: " + Folder3);
        File folder3 = new File(Folder3);
        File[] listOfFiles3 = folder3.listFiles();
        for (File file : listOfFiles3) {
            if (file.isFile()) {
                String name = file.getName();
                int st = name.indexOf('.');
                int stp = name.indexOf('-');
                int oczekiwana = Integer.parseInt(name.substring(st + 1, stp)) + 9;
                System.out.println(name);
                zapis.print(name);
                zapis.print(";" + oczekiwana);
                mmm(Folder3 + "/" + name, zapis);
            }
        }
        String Folder4 = "test/instanneg_wynikajacymi_ powtorzen";
        zapis.println("Folder: " + Folder4);
        File folder4 = new File(Folder4);
        File[] listOfFiles4 = folder4.listFiles();
        for (File file : listOfFiles4) {
            if (file.isFile()) {
                String name = file.getName();
                int st = name.indexOf('.');
                int stp = name.indexOf('-');
                int oczekiwana = Integer.parseInt(name.substring(st + 1, stp)) + 9;

                System.out.println(name);
                zapis.print(name);
                zapis.print(";" + oczekiwana);
                mmm(Folder4 + "/" + name, zapis);
            }
        }

        String Folder = "test/pozytywne_losowe";
        zapis.println("Folder: " + Folder);

        File folder = new File(Folder);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                String name = file.getName();
                int st = name.indexOf('.');
                int stp = name.indexOf('+');
                int oczekiwana = Integer.parseInt(name.substring(st + 1, stp)) + 9;
                System.out.println(name);
                zapis.print(name);
                zapis.print(";" + oczekiwana);
                mmm(Folder + "/" + name, zapis);
            }
        }
        String Folder2 = "test/pozytywne_przeklamania";
        zapis.println("Folder: " + Folder2);
        File folder2 = new File(Folder2);
        File[] listOfFiles2 = folder2.listFiles();
        for (File file : listOfFiles2) {
            if (file.isFile()) {
                String name = file.getName();
                int st = name.indexOf('.');
                int stp = name.indexOf('+');
                int oczekiwana = Integer.parseInt(name.substring(st + 1, stp)) + 9;
                System.out.println(name);
                zapis.print(name);
                zapis.print(";" + oczekiwana);
                mmm(Folder2 + "/" + name, zapis);
            }
        }

        zapis.close();
    }

    private static void mmm(String fileName, PrintWriter zapis) {

        long start = System.currentTimeMillis();

        String dane = readData(fileName);
        // System.out.println("Dane odczytane z pliku: ");
        // System.out.println(dane);

        String[] daneTab = dane.split("\n");

        int length1 = daneTab.length;
        System.out.println("Długość danych: " + length1);
        int l = daneTab[0].length();
        System.out.println("Długość pojedynczego wiersza: " + l);

        znajdzPowtorzenia(daneTab);

        initializeGraph(daneTab);
        int max = 0;
        int minPath = 100000;
        // Dla każdego wierzchołka znajdź najdłuższą ścieżkę
        for (String vertex : graph.keySet()) {
            maxPathVertex = vertex;

            int longestPathForVertex = findLongestPathForVertex(vertex, new HashSet<>());
            // System.out.println("Najdłuższa ścieżka dla wierzchołka " + vertex + ": " +
            // longestPathForVertex);

            if (longestPathForVertex > max) {

                max = longestPathForVertex;

            }
            pairs.add(new int[] { longestPathForVertex, maxPathVertex.length() });
            maxPathVertex = "";

            // System.out.println("Najdłuższa ścieżka dla całego grafu: " + maxPathVertex);
            // System.out.println("Długość najdłuższej ścieżki dla całego grafu: " +
            // maxPathVertex.length());
        }

        for (int[] pair : pairs) {
            if (pair[0] == max) {
                if (pair[1] < minPath) {
                    minPath = pair[1];
                }

            }

        }
        System.out.println("Najkrótsza ścieżka dla najdłuższej ścieżki: " + minPath);
        System.out.println("Najdłuższa ścieżka dla najkrótszej ścieżki: " + max);
        zapis.print(";" + minPath);
        zapis.print(";" + daneTab.length);
        zapis.print(";" + max);

        long end = System.currentTimeMillis();
        long time = end - start;
        System.out.println("Czas wykonania: " + time + " ms");
        zapis.print(";" + time + "\n");
        // zapis.println("Czas wykonania: " + time + " ms");

    }

    private static void initializeGraph(String[] sequences) {
        graph = new HashMap<>();
        for (String seq1 : sequences) {
            graph.put(seq1, new ArrayList<>());
            for (String seq2 : sequences) {
                if (!seq1.equals(seq2)) {
                    int overlap = calculateOverlap(seq1, seq2);
                    if (overlap > 0) {
                        graph.get(seq1).add(overlap + seq2);
                    }
                }
            }
        }
    }

    private static void znajdzPowtorzenia(String[] daneTab) {
        for (int i = 0; i < daneTab.length; i++) {
            for (int j = i + 1; j < daneTab.length; j++) {
                if (daneTab[i].equals(daneTab[j])) {
                    System.out.println("POWTORZENIW");
                }
            }
        }
    }

    private static String readData(String fileName) {
        StringBuilder data = new StringBuilder();
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            return data.toString();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "error";
        }
    }

    private static int calculateOverlap(String seq1, String seq2) {
        int overlap = 0;
        int seq1Length = seq1.length();

        for (int i = 1; i <= seq1Length; i++) {
            if (seq1.substring(seq1Length - i).equals(seq2.substring(0, i))) {
                overlap = i;
            }
        }
        return overlap;
    }

    private static int findLongestPathForVertex(String vertex, Set<String> visitedVertices) {
        int path = 1;
        String nextVertex = null;
        int max = 0;
        visitedVertices.add(vertex);

        for (String neighbour : graph.get(vertex)) {

            int overlap = Integer.parseInt(neighbour.substring(0, 1));

            if (visitedVertices.contains(neighbour.substring(1))) {
                continue;

            }

            if (overlap <= 4) {
                continue;
            }
            String ntemp = neighbour.substring(1);

            if (overlap > max) {
                max = overlap;
                nextVertex = ntemp;
            }

        }

        if (nextVertex == null) {
            return path;
        }

        maxPathVertex += nextVertex.substring(max);

        path += findLongestPathForVertex(nextVertex, visitedVertices);

        return path;
    }

}
