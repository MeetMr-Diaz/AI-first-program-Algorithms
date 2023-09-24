import java.util.*;
public class AStarSearch {
    private final Graph graph;
    private final Map<String, City> cityMap;

    public AStarSearch(Graph graph, Map<String, City> cityMap) {
        this.graph = graph;
        this.cityMap = cityMap;
    }

    public List<String> findShortestPath(String start, String goal) {
        long startTime = System.nanoTime();
        Map<String, String> previousCities = new HashMap<>();
        Map<String, Double> gScores = new HashMap<>();
        Map<String, Double> fScores = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<CityDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(cd -> fScores.get(cd.city)));

        gScores.put(start, 0.0);
        fScores.put(start, calculateDistance(cityMap.get(start), cityMap.get(goal)));

        queue.offer(new CityDistance(start, fScores.get(start)));

        while (!queue.isEmpty()) {
            CityDistance current = queue.poll();
            String currentCity = current.city;

            if (visited.contains(currentCity)) {
                continue;
            }

            if (currentCity.equals(goal)) {
                long endTime = System.nanoTime();
                double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds
                System.out.println("Execution time: " + executionTime + " milliseconds");
                return reconstructPath(previousCities, goal);
            }

            visited.add(currentCity);

            for (String neighbor : graph.getNeighbors(currentCity)) {
                if (!visited.contains(neighbor)) {
                    double tentativeGScore = gScores.get(currentCity) + calculateDistance(cityMap.get(currentCity), cityMap.get(neighbor));

                    if (!gScores.containsKey(neighbor) || tentativeGScore < gScores.get(neighbor)) {
                        previousCities.put(neighbor, currentCity);
                        gScores.put(neighbor, tentativeGScore);
                        fScores.put(neighbor, tentativeGScore + calculateDistance(cityMap.get(neighbor), cityMap.get(goal)));
                        queue.offer(new CityDistance(neighbor, fScores.get(neighbor)));
                    }
                }
            }
        }

        // Check if goal city is unreachable
        if (!visited.contains(goal)) {
            System.out.println("Goal city is unreachable.");

        }
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1e6; // Convert to milliseconds
        System.out.println("Execution time: " + executionTime + " milliseconds");
        return null; // No path found
    }

    private List<String> reconstructPath(Map<String, String> previousCities, String currentCity) {
        List<String> path = new ArrayList<>();
        while (currentCity != null) {
            path.add(currentCity);
            currentCity = previousCities.get(currentCity);
        }
        Collections.reverse(path);
        return path;
    }
    private double calculateTotalDistance(List<String> path) {
        double totalDistance = 0.0;
        for (int i = 0; i < path.size() - 1; i++) {
            String city1 = path.get(i);
            String city2 = path.get(i + 1);
            totalDistance += calculateDistance(cityMap.get(city1), cityMap.get(city2));
        }
        return totalDistance;
    }
    public double calculateDistance(City city1, City city2) {
        // Use Haversine formula to calculate the distance between two cities based on their coordinates
        double lat1 = Math.toRadians(city1.latitude);
        double lon1 = Math.toRadians(city1.longitude);
        double lat2 = Math.toRadians(city2.latitude);
        double lon2 = Math.toRadians(city2.longitude);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double radiusOfEarth = 6371.0; // Earth's radius in kilometers
        return radiusOfEarth * c;
    }

    private static class CityDistance {
        String city;
        double distance;

        public CityDistance(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }
    }
}