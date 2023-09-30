import java.text.DecimalFormat;
import java.util.List;

public class DisplayResults {

     void ErrorMessage(String algorithmName, List<String> shortestPath, double totalDistance, double executionTime) {
         DecimalFormat decimalFormat = new DecimalFormat("#.##");
         String formattedTotalDistance = decimalFormat.format(totalDistance);

         // Display the result in the resultTextArea
        String pathString = String.join(" -> ", shortestPath);
        String message = "Method: " + algorithmName + "\n" +
                "Path: " + pathString + "\n" +
                "Execution time: " + executionTime + " milliseconds\n" +
                "Total Distance: " + formattedTotalDistance + " kilometers\n\n";
        Search.resultTextArea.append(message);
    }
    void ErrorMessage(String string){
         Search.resultTextArea.append(string+"\n\n");
    }
}
