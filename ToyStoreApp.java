import java.io.*;
import java.util.Scanner;

public class ToyStoreApp {
    private static final String TOY_DATA_FILE = "toy_data.txt";

    public static void main(String[] args) {
        ToyStore toyStore = loadToyStoreFromDataFile();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Добавить/Обновить игрушку");
            System.out.println("2. Выбрать призовую игрушку");
            System.out.println("3. Выйти из программы");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    toyStore.addOrUpdateToy(inputToyData());
                    saveToyStoreToDataFile(toyStore);
                    break;
                case 2:
                    Toy prizeToy = toyStore.choosePrizeToy();
                    if (prizeToy != null) {
                        System.out.println("Поздравляем! Вы получили призовую игрушку: " + prizeToy.getName());
                        toyStore.writeToLogFile(prizeToy, "winners.txt");
                        saveToyStoreToDataFile(toyStore);
                    } else {
                        System.out.println("Нет доступных призовых игрушек.");
                    }
                    break;
                case 3:
                    System.out.println("Программа завершена.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static Toy inputToyData() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите ID игрушки: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Введите название игрушки: ");
        String name = scanner.nextLine();

        System.out.print("Введите количество игрушек: ");
        int quantity = scanner.nextInt();

        System.out.print("Введите частоту выпадения игрушки (%): ");
        double frequency = scanner.nextDouble();

        return new Toy(id, name, quantity, frequency);
    }

    private static ToyStore loadToyStoreFromDataFile() {
        ToyStore toyStore = new ToyStore();
        try (BufferedReader reader = new BufferedReader(new FileReader(TOY_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                double frequency = Double.parseDouble(parts[3]);
                Toy toy = new Toy(id, name, quantity, frequency);
                toyStore.addOrUpdateToy(toy);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при загрузке данных из файла: " + e.getMessage());
        }
        return toyStore;
    }

    private static void saveToyStoreToDataFile(ToyStore toyStore) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TOY_DATA_FILE))) {
            for (Toy toy : toyStore.getToys()) {
                writer.write(String.format("%d,%s,%d,%.2f%n", toy.getId(), toy.getName(), toy.getQuantity(), toy.getFrequency()));
            }
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных в файл: " + e.getMessage());
        }
    }
}
