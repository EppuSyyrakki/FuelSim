public class App {
    

    public static void main(String[] args) throws Exception {
        Table table = new Table("FuelTable.csv");
        int currentAlt = 0;
        int targetAlt = 0;

        try {
            currentAlt = Integer.parseInt(args[0]);
            targetAlt = Integer.parseInt(args[1]);        
        } catch (NumberFormatException nfe) {
            System.err.println("Failed to parse arguments. Must start with 2 integers.");
            return;
        } catch (ArrayIndexOutOfBoundsException obe) {
            System.err.println("Must provide 2 arguments as integers: Current altitude, target altitude");
            return;
        }

        int result = table.getConsumption(currentAlt, targetAlt, true);
        System.out.print("From altitude " + currentAlt + " ft to altitude " + targetAlt + " ft, ");
        System.out.println("fuel consumption: " + result + " lbs");
    } 
}
