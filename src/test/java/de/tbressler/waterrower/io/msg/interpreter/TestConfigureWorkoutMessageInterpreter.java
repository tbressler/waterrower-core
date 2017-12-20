package de.tbressler.waterrower.io.msg.interpreter;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.out.ConfigureWorkoutMessage;
import org.junit.Before;
import org.junit.Test;

import static de.tbressler.waterrower.io.msg.out.ConfigureWorkoutMessage.MessageType.*;
import static de.tbressler.waterrower.workout.WorkoutUnit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Tests for class ConfigureWorkoutMessageInterpreter.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestConfigureWorkoutMessageInterpreter {

    /* Class under test. */
    private ConfigureWorkoutMessageInterpreter interpreter;


    @Before
    public void setUp() {
        interpreter = new ConfigureWorkoutMessageInterpreter();
    }


    @Test
    public void getMessageTypeChar_returnsNull() {
        assertNull(interpreter.getMessageIdentifier());
    }


    @Test
    public void isSupported_withSupportedMessage_returnsTrue() {
        ConfigureWorkoutMessage msg = mock(ConfigureWorkoutMessage.class, "message");
        assertTrue(interpreter.isSupported(msg));
    }

    @Test
    public void isSupported_withUnsupportedMessage_returnsFalse() {
        AbstractMessage msg = mock(AbstractMessage.class, "message");
        assertFalse(interpreter.isSupported(msg));
    }


    @Test(expected = IllegalStateException.class)
    public void decode_throwsIllegalStateException() {
        interpreter.decode("WSI4020");
    }

    // Encode single workout with distance:

    @Test
    public void encode_withSingleWorkoutWithDistance1000AndUnitMeters_returnsWSI103E8() {
        assertEquals("WSI103E8", interpreter.encode(new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1000, METERS)));
    }

    @Test
    public void encode_withSingleWorkoutWithDistance1AndUnitMiles_returnsWSI20001() {
        assertEquals("WSI20001", interpreter.encode(new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, MILES)));
    }

    @Test
    public void encode_withSingleWorkoutWithDistance5000AndUnitKms_returnsWSI31388() {
        assertEquals("WSI31388", interpreter.encode(new ConfigureWorkoutMessage(SINGLE_WORKOUT, 5000, KMS)));
    }

    @Test
    public void encode_withSingleWorkoutWithDistance5000AndUnitMeters_returnsWSI41388() {
        assertEquals("WSI41388", interpreter.encode(new ConfigureWorkoutMessage(SINGLE_WORKOUT, 5000, STROKES)));
    }

    // Encode single workout with duration:

    @Test
    public void encode_withSingleWorkoutWithDuration1AndUnitMeters_returnsWSU0001() {
        assertEquals("WSU0001", interpreter.encode(new ConfigureWorkoutMessage(SINGLE_WORKOUT, 1, SECONDS)));
    }

    // Encode start interval workout with distance:

    @Test
    public void encode_startIntervalWorkoutWithDistance1AndMeters_returnsWII10001() {
        assertEquals("WII10001", interpreter.encode(new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 1, METERS)));
    }

    @Test
    public void encode_startIntervalWorkoutWithDistance1234AndMiles_returnsWII204D2() {
        assertEquals("WII204D2", interpreter.encode(new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 1234, MILES)));
    }

    @Test
    public void encode_startIntervalWorkoutWithDistance4321AndKms_returnsWII310E1() {
        assertEquals("WII310E1", interpreter.encode(new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 4321, KMS)));
    }

    @Test
    public void encode_startIntervalWorkoutWithDistance5000AndStrokes_returnsWII41388() {
        assertEquals("WII41388", interpreter.encode(new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 5000, STROKES)));
    }

    // Encode start interval workout with duration:

    @Test
    public void encode_startIntervalWorkoutWithDuration1234AndSeconds_returnsWIU04D2() {
        assertEquals("WIU04D2", interpreter.encode(new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 1234, SECONDS)));
    }

    @Test
    public void encode_startIntervalWorkoutWithDuration18000AndSeconds_returnsWIU04D2() {
        assertEquals("WIU4650", interpreter.encode(new ConfigureWorkoutMessage(START_INTERVAL_WORKOUT, 18000, SECONDS)));
    }

    // Encode add interval workout with distance:

    @Test
    public void encode_addIntervalWorkoutWithDistance1AndRestInterval1234AndMeters_returnsWIN04D20001() {
        assertEquals("WIN04D20001", interpreter.encode(new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 1, METERS, 1234)));
    }

    @Test
    public void encode_addIntervalWorkoutWithDistance1234AndRestInterval4321AndMiles_returnsWIN03E804D2() {
        assertEquals("WIN03E804D2", interpreter.encode(new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 1234, MILES, 1000)));
    }

    @Test
    public void encode_addIntervalWorkoutWithDistance4321AndRestInterval1AndKms_returnsWIN000110E1() {
        assertEquals("WIN000110E1", interpreter.encode(new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 4321, KMS, 1)));
    }

    @Test
    public void encode_addIntervalWorkoutWithDistance5000AndRestInterval3600AndStrokes_returnsWIN0E101388() {
        assertEquals("WIN0E101388", interpreter.encode(new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 5000, STROKES, 3600)));
    }

    // Encode add interval workout with duration:

    @Test
    public void encode_addIntervalWorkoutWithDuration1AndRestInterval1234AndMeters_returnsWIN04D20001() {
        assertEquals("WIN04D20001", interpreter.encode(new ConfigureWorkoutMessage(ADD_INTERVAL_WORKOUT, 1, SECONDS, 1234)));
    }

    // Encode end interval workout:

    @Test
    public void encode_endIntervalWorkoutWithDistance1_returnsWINFFFF0001() {
        assertEquals("WINFFFF0001", interpreter.encode(new ConfigureWorkoutMessage(END_INTERVAL_WORKOUT, 1, METERS, 0xFFFF)));
    }

    @Test
    public void encode_endIntervalWorkoutWithDistanceFFFF_returnsWINFFFF0001() {
        assertEquals("WINFFFFFFFF", interpreter.encode(new ConfigureWorkoutMessage(END_INTERVAL_WORKOUT, 0xFFFF, METERS, 0xFFFF)));
    }

}