import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException; // Добавьте этот импорт

public class ToyStore {
    private List<Toy> toys = new ArrayList<>();

    public void addOrUpdateToy(Toy toy) {
        int index = findToyIndexById(toy.getId());
        if (index != -1) {
            toys.set(index, toy);
        } else {
            toys.add(toy);
        }
    }

    public Toy choosePrizeToy() {
        double totalFrequency = toys.stream().mapToDouble(Toy::getFrequency).sum();
        double randomValue = Math.random() * totalFrequency;

        for (Toy toy : toys) {
            randomValue -= toy.getFrequency();
            if (randomValue <= 0) {
                Toy prizeToy = toy;
                toy.decreaseQuantity(1);
                toys.remove(toy);
                return prizeToy;
            }
        }

        return null;
    }

    public void writeToLogFile(Toy toy, String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(toy.getId() + "," + toy.getName() + "\n");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    public List<Toy> getToys() {
        return toys;
    }

    private int findToyIndexById(int id) {
        for (int i = 0; i < toys.size(); i++) {
            if (toys.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}