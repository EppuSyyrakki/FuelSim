import org.junit.Test;

public class AppTest {

    /**
     * Test the simple loop function
     */
    @Test
    public void ConsumptionSimpleTests() {
        var table = new Table("FuelTable.csv");
        
        // Ensure that no change in altitude results in 0 consumption:
        assert(table.getConsumption(0 , 0, true) == 0 
        && table.getConsumption(1000, 1000, true) == 0 
        && table.getConsumption(-1000, -1000, true) == 0);

        // Simple calculator gives the same numbers as the assignment examples:
        assert(table.getConsumption(5000, 15000, true) == 180
        && table.getConsumption(1000, 0, true) == 1
        && table.getConsumption(1000, -1000, true) == 2
        && table.getConsumption(0, 30000, true) == 730);       
    }

    /**
     * Test the Comparator function
     */
    @Test
    public void ConsumptionCompareTests() {
        var table = new Table("FuelTable.csv");

        assert(table.getConsumption(0 , 0, false) == 0 
        && table.getConsumption(1000, 1000, false) == 0 
        && table.getConsumption(-1000, -1000, false) == 0);

        assert(table.getConsumption(5000, 15000, false) == 180
        && table.getConsumption(1000, 0, false) == 1
        && table.getConsumption(1000, -1000, false) == 2
        && table.getConsumption(0, 30000, false) == 730);
    }

    /**
     * Test efficiency of comparator function.
     */
    @Test
    public void CompareEfficiencyTest() {
        var table = new Table("FuelTable.csv");

        for (int i = -30000; i < 30000; i += 1) {
            table.getConsumption(i, i * -1, false);
        }
    }

    /**
     * Test efficiency of simple function.
     */
    @Test
    public void SimpleEfficiencyTest() {
        var table = new Table("FuelTable.csv");

        for (int i = -30000; i < 30000; i += 1) {
            table.getConsumption(i, i * -1, true);
        }
    }
}