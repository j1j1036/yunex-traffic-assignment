import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrafficApp {
    private static final String FILE_PATH = "intersections.json";
    private static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            List<Intersection> items = loadData();
            String command = args[0];

            switch (command) {
                case "--list-items":
                    if (items.isEmpty()) {
                        System.out.println("No items found.");
                    } else {
                        items.forEach(i -> System.out.println(i.getId() + ": " + i.getName()));
                    }
                    break;

                case "--create":
                    if (args.length < 3) throw new IllegalArgumentException("Missing ID or Name");
                    items.add(new Intersection(Integer.parseInt(args[1]), args[2]));
                    saveData(items);
                    System.out.println("Intersection persisted.");
                    break;

                case "--delete":
                    if (args.length < 2) throw new IllegalArgumentException("Missing ID");
                    int idToDelete = Integer.parseInt(args[1]);
                    items.removeIf(i -> i.getId() == idToDelete);
                    saveData(items);
                    System.out.println("Intersection deleted.");
                    break;

                default:
                    System.out.println("Unknown command: " + command);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static List<Intersection> loadData() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        return mapper.readValue(file, new TypeReference<List<Intersection>>() {});
    }

    private static void saveData(List<Intersection> items) throws IOException {
        mapper.writeValue(new File(FILE_PATH), items);
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("  --list-items");
        System.out.println("  --create [id] [name]");
        System.out.println("  --delete [id]");
    }
}