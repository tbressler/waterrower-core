package de.tbressler.waterrower.subscriptions.flags;

import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.in.DataMemoryMessage;
import de.tbressler.waterrower.io.msg.out.ReadMemoryMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;
import de.tbressler.waterrower.model.MiscFlags;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static de.tbressler.waterrower.io.msg.Memory.SINGLE_MEMORY;
import static de.tbressler.waterrower.model.MemoryLocation.FMISC_FLAGS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests for class MiscFlagsSubscription.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestMiscFlagsSubscription {

    // Class under test.
    private MiscFlagsSubscription subscription;

    // Mocks:
    private MiscFlagsSubscription internalSubscription = mock(MiscFlagsSubscription.class, "internalSubscription");


    // Polling:

    @Test
    public void poll_returnsMessageWithSingleMemoryAndFEXTENDED() {
        subscription = newMiscFlagsSubscription();

        ReadMemoryMessage msg = (ReadMemoryMessage) subscription.poll();
        assertEquals(SINGLE_MEMORY, msg.getMemory());
        assertEquals(FMISC_FLAGS.getLocation(), msg.getLocation());
    }


    // Handle:

    @Test
    public void handle_withFEXTENDEDAnd0x01_notifiesOnWorkoutModeUpdated() {
        subscription = newMiscFlagsSubscription();

        DataMemoryMessage msg = new DataMemoryMessage(FMISC_FLAGS.getLocation(), 0xB6);

        subscription.handle((AbstractMessage) msg);

        verify(internalSubscription, times(1)).onMiscFlagsUpdated(argThat(matchesFlags(false, true, true, false, true, true, false, true)));
    }

    @Test
    public void handle_twoTimesWithSameMessages_onlyNotifiesOneTime() {
        subscription = newMiscFlagsSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(FMISC_FLAGS.getLocation(), 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(FMISC_FLAGS.getLocation(), 0x01);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onMiscFlagsUpdated(argThat(matchesFlags(true, false, false, false, false, false, false, false)));
    }

    @Test
    public void handle_twoTimesWithNotSameMessages_notifiesTwoTime() {
        subscription = newMiscFlagsSubscription();

        DataMemoryMessage msg1 = new DataMemoryMessage(FMISC_FLAGS.getLocation(), 0x01);
        DataMemoryMessage msg2 = new DataMemoryMessage(FMISC_FLAGS.getLocation(), 0x80);

        subscription.handle((AbstractMessage) msg1);
        subscription.handle((AbstractMessage) msg2);

        verify(internalSubscription, times(1)).onMiscFlagsUpdated(argThat(matchesFlags(true, false, false, false, false, false, false, false)));
        verify(internalSubscription, times(1)).onMiscFlagsUpdated(argThat(matchesFlags(false, false, false, false, false, false, false, true)));
    }

    @Test
    public void handle_withOtherMessage_doesntNotifyOnClockCountDownUpdated() {
        subscription = newMiscFlagsSubscription();
        subscription.handle(new StartCommunicationMessage());
        verify(internalSubscription, never()).onMiscFlagsUpdated(any(MiscFlags.class));
    }



    // Helper methods:

    private MiscFlagsSubscription newMiscFlagsSubscription() {
        return new MiscFlagsSubscription() {
            @Override
            protected void onMiscFlagsUpdated(MiscFlags flags) {
                internalSubscription.onMiscFlagsUpdated(flags);
            }
        };
    }


    private ArgumentMatcher<MiscFlags> matchesFlags(boolean zoneWork, boolean zoneRest, boolean isLowBat, boolean isPC, boolean isLine, boolean isMmcCd, boolean isMmcUp, boolean isMmcDn) {
        return new ArgumentMatcher<>() {
            @Override
            public boolean matches(Object argument) {
                if (!(argument instanceof MiscFlags))
                    return false;
                MiscFlags flags = (MiscFlags) argument;
                return (flags.isZoneWork() == zoneWork)
                        && (flags.isZoneRest() == zoneRest)
                        && (flags.isMiscLowBat() == isLowBat)
                        && (flags.isMiscPC() == isPC)
                        && (flags.isMiscLine() == isLine)
                        && (flags.isMiscMmcCd() == isMmcCd)
                        && (flags.isMiscMmcUp() == isMmcUp)
                        && (flags.isMiscMmcDn() == isMmcDn);
            }
        };
    }

}