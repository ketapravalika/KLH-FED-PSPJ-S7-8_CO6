
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

class Appointment {
    int id;
    String patientName;
    String doctorName;
    String status;

    Appointment(int id, String patientName,
                String doctorName, String status) {
        this.id = id;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.status = status;
    }

    @Override
    public String toString() {
        return id + " | " + patientName + " | "
                + doctorName + " | " + status;
    }
}

public class ClinicAppointmentSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        List<Appointment> appointments = new ArrayList<>();
        Set<Integer> appointmentIds = new HashSet<>();
        Map<Integer, Appointment> appointmentMap = new HashMap<>();

        try {
            System.out.print("Enter number of appointments: ");
            int n = Integer.parseInt(sc.nextLine().trim());

            if (n < 1) {
                throw new IllegalArgumentException(
                        "Enter at least one appointment.");
            }

            for (int i = 0; i < n; i++) {
                System.out.println("\nAppointment " + (i + 1));

                System.out.print("Enter appointment ID: ");
                int id = Integer.parseInt(sc.nextLine().trim());

                if (!appointmentIds.add(id)) {
                    System.out.println("ID already exists. Try again.");
                    i--;
                    continue;
                }

                System.out.print("Enter patient name: ");
                String patient = sc.nextLine().trim();

                System.out.print("Enter doctor name: ");
                String doctor = sc.nextLine().trim();

                System.out.print("Enter status (Pending/Completed): ");
                String status = sc.nextLine().trim();

                if (patient.isEmpty() || doctor.isEmpty()
                        || !(status.equalsIgnoreCase("Pending")
                        || status.equalsIgnoreCase("Completed"))) {
                    appointmentIds.remove(id);
                    System.out.println("Invalid details. Try again.");
                    i--;
                    continue;
                }

                status = status.substring(0, 1).toUpperCase()
                        + status.substring(1).toLowerCase();

                Appointment a = new Appointment(
                        id, patient, doctor, status);

                appointments.add(a);
                appointmentMap.put(id, a);
            }

            System.out.println("\n--- ALL APPOINTMENTS ---");
            appointments.forEach(System.out::println);

            System.out.print("\nEnter appointment ID to search: ");
            int searchId = Integer.parseInt(sc.nextLine().trim());

            Appointment found = appointmentMap.get(searchId);

            if (found != null) {
                System.out.println("Appointment found: " + found);
            } else {
                System.out.println("Appointment not found.");
            }

            Predicate<Appointment> pending =
                    a -> a.status.equals("Pending");

            System.out.println("\n--- PENDING APPOINTMENTS ---");

            appointments.stream()
                    .filter(pending)
                    .forEach(System.out::println);

            System.out.println("\n--- SORTED BY PATIENT NAME ---");

            appointments.stream()
                    .sorted(Comparator.comparing(a -> a.patientName))
                    .forEach(System.out::println);

            System.out.println("\n--- APPOINTMENT SUMMARY ---");

            Map<String, Long> summary = appointments.stream()
                    .collect(Collectors.groupingBy(
                            a -> a.status,
                            TreeMap::new,
                            Collectors.counting()));

            summary.forEach((status, count) ->
                    System.out.println(status + ": " + count));

            String names = appointments.stream()
                    .map(a -> a.patientName)
                    .distinct()
                    .collect(Collectors.joining(", "));

            System.out.println("\nPatient Names: " + names);

            long total = appointments.stream().count();
            System.out.println("Total Appointments: " + total);

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
            System.out.println("Clinic appointment process completed.");
        }
    }
}

