package polanski.option;

import org.junit.Test;

import rx.functions.Action0;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class TestUnit {
    @Test
    public void testFrom_returnsUnit() {
        assertEquals(Unit.DEFAULT, Unit.from(mock(Action0.class)));
    }

    @Test
    public void testFrom_callsAction() {
        Action0 action = mock(Action0.class);

        Unit.from(action);

        verify(action).call();
    }

    @Test
    public void testAsUnit_returnsUnit() {
        assertEquals(Unit.DEFAULT, Unit.asUnit(mock(Object.class)));
    }
}
