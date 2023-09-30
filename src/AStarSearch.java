import java.util.*;
public class AStarSearch {
    private final Graph graph;
    private final Map<String, City> cityMap;

    public AStarSearch(Graph graph, Map<String, City> cityMap) {
        this.graph = graph;
        this.cityMap = cityMap;
    }

    public List<String> Path(String start, String goal) {
        // check file for cities
        DisplayResults displayResults = new DisplayResults();
        if (!cityMap.containsKey(start) ||!cityMap.containsKey(goal)) {
            displayResults.ErrorMessage("start or goal city not in file");
        }
        Map<String, String> previousCities = new HashMap<>();
        Map<String, Double> gScores = new HashMap<>();
        Map<String, Double> fScores = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<CityDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(cd -> fScores.get(cd.city)));

        gScores.put(start, 0.0);
        fScores.put(start, adjencentCityDistance.calculateDistance(cityMap.get(start), cityMap.get(goal)));
        queue.offer(new CityDistance(start, fScores.get(start)));

        while (!queue.isEmpty()) {
            CityDistance current = queue.poll();
            String currentCity = current.city;

            if (visited.contains(currentCity)) {
                continue;
            }

            if (currentCity.equals(goal)) {
                return reconstructPath(previousCities, goal);
            }

            visited.add(currentCity);

            for (String neighbor : graph.getNeighbors(currentCity)) {
                if (!visited.contains(neighbor)) {
                    double tentativeGScore = gScores.get(currentCity) + adjencentCityDistance.calculateDistance(cityMap.get(currentCity), cityMap.get(neighbor));

                    if (!gScores.containsKey(neighbor) || tentativeGScore < gScores.get(neighbor)) {
                        previousCities.put(neighbor, currentCity);
                        gScores.put(neighbor, tentativeGScore);
                        fScores.put(neighbor, tentativeGScore + adjencentCityDistance.calculateDistance(cityMap.get(neighbor), cityMap.get(goal)));
                        queue.offer(new CityDistance(neighbor, fScores.get(neighbor)));
                    }
                }
            }
        }

        // Check if goal city is unreachable
        if (!visited.contains(goal)) {
            System.out.println("Goal city is unreachable.");

        }
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

    private static class CityDistance {
        String city;
        double distance;

        public CityDistance(String city, double distance) {
            this.city = city;
            this.distance = distance;
        }
    }
}