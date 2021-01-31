import testing.AfterSuite;
import testing.BeforeSuite;
import testing.Test;

public class Tester {
    @AfterSuite
    private void shouldAfterSuit() {
        System.out.println("!!!!! <З");
    }

    @Test(priority = 4)
    void shouldTest4() {
        System.out.println("much");
    }

    @Test(priority = 3)
    void shouldTest3() {
        System.out.println("very ");
    }

    @Test(priority = 2)
    public void shouldTest2() {
        System.out.println("you ");
    }

    @Test(priority = 1)
    public void shouldTest1() {
        System.out.println("thank ");
    }

    @BeforeSuite
    public void shouldBeforeSuite() {
        System.out.println("Max, ");
    }
}
