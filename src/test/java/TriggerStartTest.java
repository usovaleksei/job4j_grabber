import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TriggerStartTest {
    @Test
    public void testMethod() {
        int rsl = TriggerStart.test();
        assertThat(rsl, is(2));
    }
}
