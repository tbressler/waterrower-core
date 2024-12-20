package de.tbressler.waterrower.subscriptions;

import de.tbressler.waterrower.io.IConnectionListener;
import de.tbressler.waterrower.io.WaterRowerConnector;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.Duration.ofSeconds;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * Tests for class SubscriptionPollingService.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestSubscriptionPollingService {

    // Class under test.
    private SubscriptionPollingService pollingService;

    // Mocks:
    private Duration duration = ofSeconds(2);
    private WaterRowerConnector connector = mock(WaterRowerConnector.class, "connector");
    private ScheduledExecutorService executorService = mock(ScheduledExecutorService.class, "executorService");

    private ISubscription subscription1 = mock(ISubscription.class, "subscription1");
    private ISubscription subscription2 = mock(ISubscription.class, "subscription2");

    private AbstractMessage someMessage1 = mock(AbstractMessage.class, "someMessage1");
    private AbstractMessage someMessage2 = mock(AbstractMessage.class, "someMessage2");
    private AbstractMessage someMessage3 = mock(AbstractMessage.class, "someMessage3");

    // Capture:
    private ArgumentCaptor<Runnable> pollTask = forClass(Runnable.class);
    private ArgumentCaptor<Runnable> sendTask1 = forClass(Runnable.class);
    private ArgumentCaptor<Runnable> sendTask2 = forClass(Runnable.class);
    private ArgumentCaptor<IConnectionListener> listener = forClass(IConnectionListener.class);


    @BeforeEach
    public void setUp() {
        pollingService = new SubscriptionPollingService(connector, executorService, Duration.ofMillis(200));
        verify(connector).addConnectionListener(listener.capture());
    }

    // Constructor:

    @Test
    public void new_withNullConnector_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new SubscriptionPollingService(null, executorService, Duration.ofMillis(200)));
    }

    @Test
    public void new_withNullExecutorService_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new SubscriptionPollingService(connector, null, Duration.ofMillis(200)));
    }

    @Test
    public void new_withNullInterval_throwsNPE() {
        assertThrows(NullPointerException.class, () -> new SubscriptionPollingService(connector, executorService, null));
    }

    // Start:

    @Test
    public void start_schedulesTask() {
        pollingService.start();
        verify(executorService, times(1)).schedule(any(Runnable.class), eq((long) 200), eq(MILLISECONDS));
    }

    // Task execution:

    @Test
    public void callRunnable_pollsSubscriptions() throws IOException {

        subscribe(subscription1, someMessage1);
        subscribe(subscription2, someMessage2);

        pollingService.start();

        verify(executorService, times(1)).schedule(pollTask.capture(), eq((long) 200), eq(MILLISECONDS));

        pollTask.getValue().run();

        verify(executorService, times(2)).schedule(sendTask1.capture(), eq((long) 200), eq(MILLISECONDS));

        sendTask1.getValue().run();

        verify(connector, times(1)).send(someMessage1);
        verify(connector, times(1)).send(someMessage2);
    }

    @Test
    public void callRunnable_afterStop_doesntPollSubscriptions() throws IOException {

        subscribe(subscription1, someMessage1);
        subscribe(subscription2, someMessage2);

        pollingService.start();

        verify(executorService, times(1)).schedule(pollTask.capture(), eq((long) 200), eq(MILLISECONDS));

        pollingService.stop();

        pollTask.getValue().run();

        verify(connector, never()).send(someMessage1);
        verify(connector, never()).send(someMessage2);
    }

    @Test
    public void callRunnable_whenSendFails() throws IOException {

        subscribe(subscription1, someMessage1);
        doThrow(new IOException("some-io-exception")).when(connector).send(someMessage1);

        pollingService.start();

        verify(executorService, times(1)).schedule(pollTask.capture(), eq((long) 200), eq(MILLISECONDS));

        pollTask.getValue().run();
    }

    // Message received:

    @Test
    public void callMessageReceived_whenStarted_passesMessageToSubscriptions() throws IOException {

        subscribe(subscription1, someMessage1);
        subscribe(subscription2, someMessage2);

        pollingService.start();

        verify(executorService, times(1)).schedule(pollTask.capture(), eq((long) 200), eq(MILLISECONDS));

        listener.getValue().onMessageReceived(someMessage3);

        verify(subscription1, times(1)).handle(someMessage3);
        verify(subscription2, times(1)).handle(someMessage3);
    }

    @Test
    public void callMessageReceived_whenStopped_doesntPassMessageToSubscriptions() throws IOException {

        subscribe(subscription1, someMessage1);
        subscribe(subscription2, someMessage2);

        pollingService.start();

        verify(executorService, times(1)).schedule(pollTask.capture(), eq((long) 200), eq(MILLISECONDS));

        pollingService.stop();

        listener.getValue().onMessageReceived(someMessage3);

        verify(subscription1, never()).handle(someMessage3);
        verify(subscription2, never()).handle(someMessage3);
    }

    // Subscriptions:

    @Test
    public void subscribe_withNull_throwsNPE() throws IOException {
        assertThrows(NullPointerException.class, () -> pollingService.subscribe(null));
    }

    @Test
    public void unsubscribe_withNull_throwsNPE() throws IOException {
        assertThrows(NullPointerException.class, () -> pollingService.unsubscribe(null));
    }

    @Test
    public void unsubscribe_pollsSubscriptionsOnly() throws IOException {

        subscribe(subscription1, someMessage1);
        subscribe(subscription2, someMessage2);

        pollingService.unsubscribe(subscription1);

        pollingService.start();

        verify(executorService, times(1)).schedule(pollTask.capture(), eq((long) 200), eq(MILLISECONDS));

        pollTask.getValue().run();

        verify(connector, never()).send(someMessage1);
        verify(connector, times(1)).send(someMessage2);
    }


    // Helper methods:

    private void subscribe(ISubscription subscription, AbstractMessage msg) {
        when(subscription.poll()).thenReturn(msg);
        pollingService.subscribe(subscription);
    }

}