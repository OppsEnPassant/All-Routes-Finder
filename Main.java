// MOTHIBA
// KOKETSO
// 4370898
// CSC212 2024-Practical 1-Term 4
// Question1:Algorithm Implementation


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Graph {
    private Map<String, List<String>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    // Load graph from a file
    public void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                String city = parts[0].trim();
                List<String> neighbors = new ArrayList<>();
                if (parts.length > 1 && !parts[1].trim().isEmpty()) {
                    neighbors = Arrays.asList(parts[1].trim().split(","));
                    for (int i = 0; i < neighbors.size(); i++) {
                        neighbors.set(i, neighbors.get(i).trim()); // Trim each neighbor
                    }
                }
                adjacencyList.put(city, neighbors);
            }
        } catch (IOException e) {
            System.err.println("Error reading graph file: " + e.getMessage());
        }
    }

    // Get neighbors of a city
    public List<String> getNeighbors(String city) {
        return adjacencyList.getOrDefault(city, new ArrayList<>());
    }

    // Get all cities
    public Set<String> getAllCities() {
        return adjacencyList.keySet();
    }
}

class CityRoutes {
    private Graph graph;
    private List<String> allRoutes;

    public CityRoutes() {
        this.graph = new Graph();
        this.allRoutes = new ArrayList<>();
    }

    // Find all routes from start to end
    public void findAllRoutes(String start, String end) {
        findAllRoutesHelper(start, end, new ArrayList<>());
    }

    private void findAllRoutesHelper(String start, String end, List<String> path) {
        path.add(start);
        if (start.equals(end)) {
            allRoutes.add(String.join("-", path));
        } else {
            for (String neighbor : graph.getNeighbors(start)) {
                if (!path.contains(neighbor)) {
                    findAllRoutesHelper(neighbor, end, path);
                }
            }
        }
        path.remove(path.size() - 1);
    }

    // Check path existence from start to end
    public boolean checkPathExists(String start, String end) {
        return checkPathExistsHelper(start, end, new HashSet<>());
    }

    private boolean checkPathExistsHelper(String start, String end, Set<String> visited) {
        if (start.equals(end)) {
            return true;
        }
        visited.add(start);
        for (String neighbor : graph.getNeighbors(start)) {
            if (!visited.contains(neighbor) && checkPathExistsHelper(neighbor, end, visited)) {
                return true;
            }
        }
        visited.remove(start);
        return false;
    }

    // Set the graph from a file
    public void loadGraph(String filename) {
        graph.loadFromFile(filename);
    }

    // Get all routes
    public List<String> getAllRoutes() {
        return allRoutes;
    }
}

public class Main {
    public static void main(String[] args) {
        CityRoutes cityRoutes = new CityRoutes();

        // Load graph from graph1_input.txt
        cityRoutes.loadGraph("graph1_input.txt");

        // Finding all routes from L to K
        cityRoutes.findAllRoutes("L", "K");

        // Output all routes found
        System.out.println("All possible routes from L to K:");
        for (String route : cityRoutes.getAllRoutes()) {
            System.out.println(route);
        }

        // Load graph for graph2_input.txt (including city M)
        cityRoutes.loadGraph("graph2_input.txt");

        // Check path existence from L to K through city M
        boolean pathExists = cityRoutes.checkPathExists("L", "K");
        System.out.println("Path exists from L to K through city M: " + pathExists);
    }
}

