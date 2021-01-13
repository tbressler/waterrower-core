package de.tbressler.waterrower;

import de.tbressler.waterrower.io.IConnectionListener;
import de.tbressler.waterrower.io.WaterRowerConnector;
import de.tbressler.waterrower.io.msg.in.ErrorMessage;
import de.tbressler.waterrower.io.msg.in.HardwareTypeMessage;
import de.tbressler.waterrower.io.msg.in.ModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.*;
import de.tbressler.waterrower.io.msg.out.ConfigureWorkoutMessage.MessageType;
import de.tbressler.waterrower.io.transport.SerialDeviceAddress;
import de.tbressler.waterrower.model.ModelInformation;
import de.tbressler.waterrower.subscriptions.ISubscription;
import de.tbressler.waterrower.subscriptions.SubscriptionPollingService;
import de.tbressler.waterrower.watchdog.DeviceVerificationWatchdog;
import de.tbressler.waterrower.watchdog.ITimeoutListener;
import de.tbressler.waterrower.watchdog.PingWatchdog;
import de.tbressler.waterrower.workout.Workout;
import de.tbressler.waterrower.workout.WorkoutUnit;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;

import java.io.IOException;

import static de.tbressler.waterrower.io.msg.out.ConfigureWorkoutMessage.MessageType.*;
import static de.tbressler.waterrower.model.ErrorCode.*;
import static de.tbressler.waterrower.model.MonitorType.WATER_ROWER_S4;
import static de.tbressler.waterrower.watchdog.TimeoutReason.DEVICE_NOT_CONFIRMED_TIMEOUT;
import static de.tbressler.waterrower.watchdog.TimeoutReason.PING_TIMEOUT;
import static de.tbressler.waterrower.workout.WorkoutUnit.METERS;
import static de.tbressler.waterrower.workout.WorkoutUnit.STROKES;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

/**
 * Tests for class WaterRower.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class TestWaterRower {

    // Class under test.
    private WaterRower waterRower;

    // Mocks:
    private WaterRowerConnector connector = mock(WaterRowerConnector.class, "connector");
    private PingWatchdog pingWatchdog = mock(PingWatchdog.class, "pingWatchdog");
    private DeviceVerificationWatchdog deviceVerificationWatchdog = mock(DeviceVerificationWatchdog.class, "deviceVerificationWatchdog");
    private SubscriptionPollingService subscriptionPollingService = mock(SubscriptionPollingService.class, "subscriptionPollingService");
    private SerialDeviceAddress address = mock(SerialDeviceAddress.class, "address");
    private IWaterRowerConnectionListener waterRowerConnectionListener = mock(IWaterRowerConnectionListener.class, "waterRowerConnectionListener");
    private ISubscription subscription = mock(ISubscription.class, "subscription");

    private WaterRowerInitializer initializer = mock(WaterRowerInitializer.class, "initializer");

    // Capture:
    private ArgumentCaptor<IConnectionListener> connectionListener = forClass(IConnectionListener.class);
    private ArgumentCaptor<ITimeoutListener> pingListener = forClass(ITimeoutListener.class);
    private ArgumentCaptor<ITimeoutListener> deviceVerificationListener = forClass(ITimeoutListener.class);


    @Before
    public void setUp() throws Exception {
        waterRower = new WaterRower(connector, pingWatchdog, deviceVerificationWatchdog, subscriptionPollingService);
        waterRower.addConnectionListener(waterRowerConnectionListener);

        verify(connector, times(1)).addConnectionListener(connectionListener.capture());
        verify(pingWatchdog, times(1)).setTimeoutListener(pingListener.capture());
        verify(deviceVerificationWatchdog, times(1)).setTimeoutListener(deviceVerificationListener.capture());
    }


    // Constructor:

    @Test(expected = NullPointerException.class)
    public void new1_withNullInitializer_throwsNPE() {
        new WaterRower(null);
    }

    @Test(expected = NullPointerException.class)
    public void new1_withInitializerWhichReturnsNullConnector_throwsNPE() {
        when(initializer.getDeviceVerificationWatchdog()).thenReturn(deviceVerificationWatchdog);
        when(initializer.getPingWatchdog()).thenReturn(pingWatchdog);
        when(initializer.getSubscriptionPollingService()).thenReturn(subscriptionPollingService);
        when(initializer.getWaterRowerConnector()).thenReturn(null);

        new WaterRower(initializer);
    }

    @Test(expected = NullPointerException.class)
    public void new1_withInitializerWhichReturnsNullSubscriptionPollingService_throwsNPE() {
        when(initializer.getDeviceVerificationWatchdog()).thenReturn(deviceVerificationWatchdog);
        when(initializer.getPingWatchdog()).thenReturn(pingWatchdog);
        when(initializer.getSubscriptionPollingService()).thenReturn(null);
        when(initializer.getWaterRowerConnector()).thenReturn(connector);

        new WaterRower(initializer);
    }

    @Test(expected = NullPointerException.class)
    public void new1_withInitializerWhichReturnsNullPingWatchdog_throwsNPE() {
        when(initializer.getDeviceVerificationWatchdog()).thenReturn(deviceVerificationWatchdog);
        when(initializer.getPingWatchdog()).thenReturn(null);
        when(initializer.getSubscriptionPollingService()).thenReturn(subscriptionPollingService);
        when(initializer.getWaterRowerConnector()).thenReturn(connector);

        new WaterRower(initializer);
    }

    @Test(expected = NullPointerException.class)
    public void new1_withInitializerWhichReturnsNullDeviceVerificationWatchdog_throwsNPE() {
        when(initializer.getDeviceVerificationWatchdog()).thenReturn(null);
        when(initializer.getPingWatchdog()).thenReturn(pingWatchdog);
        when(initializer.getSubscriptionPollingService()).thenReturn(subscriptionPollingService);
        when(initializer.getWaterRowerConnector()).thenReturn(connector);

        new WaterRower(initializer);
    }

    @Test
    public void new1_withInitializer_throwsNoException() {
        when(initializer.getDeviceVerificationWatchdog()).thenReturn(deviceVerificationWatchdog);
        when(initializer.getPingWatchdog()).thenReturn(pingWatchdog);
        when(initializer.getSubscriptionPollingService()).thenReturn(subscriptionPollingService);
        when(initializer.getWaterRowerConnector()).thenReturn(connector);

        new WaterRower(initializer);
    }

    @Test(expected = NullPointerException.class)
    public void new2_withNullConnector_throwsNPE() {
        new WaterRower(null, pingWatchdog, deviceVerificationWatchdog, subscriptionPollingService);
    }

    @Test(expected = NullPointerException.class)
    public void new2_withNullPingWatchdog_throwsNPE() {
        new WaterRower(connector, null, deviceVerificationWatchdog, subscriptionPollingService);
    }

    @Test(expected = NullPointerException.class)
    public void new2_withNullDeviceVerificationWatchdog_throwsNPE() {
        new WaterRower(connector, pingWatchdog, null, subscriptionPollingService);
    }

    @Test(expected = NullPointerException.class)
    public void new2_withNullSubscriptionPollingService_throwsNPE() {
        new WaterRower(connector, pingWatchdog, deviceVerificationWatchdog, null);
    }


    // Connect / disconnect:

    @Test
    public void connect_whenNotConnected_callsConnectAtConnector() throws Exception {
        when(connector.isConnected()).thenReturn(false);
        waterRower.connect(address);
        verify(connector, times(1)).connect(address);
    }

    @Test(expected = IOException.class)
    public void connect_whenAlreadyConnected_throwsIOException() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        waterRower.connect(address);
    }


    @Test
    public void callOnConnected_startsVerificationWatchdogAndSendsHello() throws Exception {
        connectionListener.getValue().onConnected();
        verify(deviceVerificationWatchdog, times(1)).start();
        verify(connector, times(1)).send(any(StartCommunicationMessage.class));
    }

    @Test
    public void callOnConnected_connectorThrowsIOException_notifiesOnError() throws Exception {
        doThrow(new IOException("mocked-io-exception")).when(connector).send(any(StartCommunicationMessage.class));
        connectionListener.getValue().onConnected();
        verify(waterRowerConnectionListener, times(1)).onError(eq(COMMUNICATION_FAILED));
    }


    @Test
    public void isConnected_whenNotConnected_returnsFalse() throws Exception {
        when(connector.isConnected()).thenReturn(false);
        assertFalse(waterRower.isConnected());
    }

    @Test
    public void isConnected_whenConnectedButDeviceNotConfirmed_returnsFalse() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(false);
        assertFalse(waterRower.isConnected());
    }

    @Test
    public void isConnected_whenConnectedAndDeviceNotConfirmed_returnsTrue() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(true);
        assertTrue(waterRower.isConnected());
    }


    @Test
    public void callOnError_notifiesOnError() throws Exception {
        connectionListener.getValue().onError();
        verify(waterRowerConnectionListener, times(1)).onError(eq(COMMUNICATION_FAILED));
    }


    @Test
    public void callOnMessageReceived_withHardwareTypeIsWaterRower_requestsModelInformation() throws Exception {
        connectionListener.getValue().onMessageReceived(new HardwareTypeMessage(true));
        verify(connector, times(1)).send(any(RequestModelInformationMessage.class));
    }

    @Test
    public void callOnMessageReceived_withHardwareTypeIsWaterRowerButSendThrowsIOException_notifiesOnError() throws Exception {
        doThrow(new IOException("mocked-io-exception")).when(connector).send(any(RequestModelInformationMessage.class));
        connectionListener.getValue().onMessageReceived(new HardwareTypeMessage(true));
        verify(waterRowerConnectionListener, times(1)).onError(eq(COMMUNICATION_FAILED));
    }

    @Test
    public void callOnMessageReceived_withHardwareTypeIsNotWaterRower_notifiesOnError() throws Exception {
        connectionListener.getValue().onMessageReceived(new HardwareTypeMessage(false));
        verify(connector, never()).send(any(RequestModelInformationMessage.class));
        verify(waterRowerConnectionListener, times(1)).onError(eq(DEVICE_NOT_SUPPORTED));
    }

    @Test
    public void callOnMessageReceived_withModelInformationAndModelIsSupported_confirmsDeviceAndNotifiesOnConnected() throws Exception {
        ModelInformation modelInformation = new ModelInformation(WATER_ROWER_S4, "02.00");
        connectionListener.getValue().onMessageReceived(new ModelInformationMessage(modelInformation));

        verify(deviceVerificationWatchdog, times(1)).setDeviceConfirmed(true);
        verify(deviceVerificationWatchdog, times(1)).stop();
        verify(pingWatchdog, times(1)).start();
        verify(subscriptionPollingService, times(1)).start();
        verify(waterRowerConnectionListener, times(1)).onConnected(eq(modelInformation));
    }

    @Test
    public void callOnMessageReceived_withModelInformationAndModelIsNotSupported_notifiesOnError() throws Exception {
        ModelInformation modelInformation = new ModelInformation(WATER_ROWER_S4, "03.00");
        connectionListener.getValue().onMessageReceived(new ModelInformationMessage(modelInformation));

        verify(deviceVerificationWatchdog, times(1)).setDeviceConfirmed(false);
        verify(deviceVerificationWatchdog, times(1)).stop();
        verify(waterRowerConnectionListener, times(1)).onError(DEVICE_NOT_SUPPORTED);
    }


    @Test
    public void callOnMessageReceived_withErrorMessage_notifiesOnError() throws Exception {
        connectionListener.getValue().onMessageReceived(new ErrorMessage());
        verify(waterRowerConnectionListener, times(1)).onError(ERROR_MESSAGE_RECEIVED);
    }


    @Test
    public void callOnDisconnected_notifiesDisconnect() {
        connectionListener.getValue().onDisconnected();

        verify(pingWatchdog, times(2)).stop();
        verify(deviceVerificationWatchdog, atLeast(1)).stop();
        verify(subscriptionPollingService, atLeast(1)).stop();
        verify(waterRowerConnectionListener, times(1)).onDisconnected();
    }


    @Test
    public void disconnect_whenConnected_disconnects() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(true);

        waterRower.disconnect();

        verify(pingWatchdog, times(1)).stop();
        verify(deviceVerificationWatchdog, times(1)).stop();
        verify(subscriptionPollingService, times(1)).stop();
        verify(connector, times(1)).send(any(ExitCommunicationMessage.class));
        verify(connector, times(1)).disconnect();
    }

    @Test
    public void disconnect_whenNotConnected_disconnectsConnector() throws Exception {
        when(connector.isConnected()).thenReturn(false);

        waterRower.disconnect();

        verify(connector, never()).send(any(ExitCommunicationMessage.class));
        verify(connector, times(1)).disconnect();
    }


    // Perform reset:

    @Test
    public void performReset_whenConnected_sendsPerformReset() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(true);

        waterRower.performReset();

        verify(connector, times(1)).send(any(ResetMessage.class));
    }

    @Test(expected = IOException.class)
    public void performReset_whenNotConnected_throwsIOExcepton() throws Exception {
        when(connector.isConnected()).thenReturn(false);
        waterRower.performReset();
    }

    @Test(expected = IOException.class)
    public void performReset_whenConnectedWithUnsupportedDevice_throwsIOExcepton() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(false);
        waterRower.performReset();
    }


    // Start workout:

    @Test(expected = IOException.class)
    public void startWorkout_whenNotConnected_throwsIOExcepton() throws Exception {
        when(connector.isConnected()).thenReturn(false);
        waterRower.startWorkout(new Workout(1000, METERS));
    }

    @Test(expected = IOException.class)
    public void startWorkout_whenConnectedWithUnsupportedDevice_throwsIOExcepton() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(false);
        waterRower.startWorkout(new Workout(1000, METERS));
    }

    @Test(expected = NullPointerException.class)
    public void startWorkout_withNull_throwsNPE() throws Exception {
        waterRower.startWorkout(null);
    }

    @Test
    public void startWorkout_withSingleWorkout_sendsSingleWorkout() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(true);

        Workout workout = new Workout(1000, STROKES);
        waterRower.startWorkout(workout);

        verify(connector, times(1)).send(argThat(eqMsg(SINGLE_WORKOUT, 1000, STROKES, -1)));
    }

    @Test
    public void startWorkout_withIntervalWorkout_sendsIntervals() throws Exception {
        when(connector.isConnected()).thenReturn(true);
        when(deviceVerificationWatchdog.isDeviceConfirmed()).thenReturn(true);

        Workout workout = new Workout(1000, METERS);
        workout.addInterval(10, 2000);
        workout.addInterval(20, 2000);
        workout.addInterval(30, 1000);

        waterRower.startWorkout(workout);

        InOrder orderVerification = inOrder(connector);

        orderVerification.verify(connector, times(1)).send(argThat(eqMsg(START_INTERVAL_WORKOUT, 1000, METERS, -1)));
        orderVerification.verify(connector, times(1)).send(argThat(eqMsg(ADD_INTERVAL_WORKOUT, 2000, METERS, 10)));
        orderVerification.verify(connector, times(1)).send(argThat(eqMsg(ADD_INTERVAL_WORKOUT, 2000, METERS, 20)));
        orderVerification.verify(connector, times(1)).send(argThat(eqMsg(ADD_INTERVAL_WORKOUT, 1000, METERS, 30)));
        orderVerification.verify(connector, times(1)).send(argThat(eqMsg(END_INTERVAL_WORKOUT, 0xFFFF, METERS, 0xFFFF)));
    }


    // Subscribe / unsubscribe:

    @Test
    public void subscribe_subscribesSubscription() throws Exception {
        waterRower.subscribe(subscription);
        verify(subscriptionPollingService, times(1)).subscribe(eq(subscription));
    }

    @Test
    public void unsubscribe_unsubscribesSubscription() throws Exception {
        waterRower.unsubscribe(subscription);
        verify(subscriptionPollingService, times(1)).unsubscribe(eq(subscription));
    }


    // Watchdogs:

    @Test
    public void onTimeout_withPingTimeout_() {
        pingListener.getValue().onTimeout(PING_TIMEOUT);
        verify(waterRowerConnectionListener, times(1)).onError(TIMEOUT);
    }

    @Test
    public void onTimeout_withDeviceVerificationTimeout_() {
        deviceVerificationListener.getValue().onTimeout(DEVICE_NOT_CONFIRMED_TIMEOUT);
        verify(waterRowerConnectionListener, times(1)).onError(DEVICE_NOT_SUPPORTED);
    }


    // Listeners:

    @Test(expected = NullPointerException.class)
    public void addConnectionListener_withNull_throwsNPE() throws Exception {
        waterRower.addConnectionListener(null);
    }

    @Test(expected = NullPointerException.class)
    public void removeConnectionListener_withNull_throwsNPE() throws Exception {
        waterRower.removeConnectionListener(null);
    }

    @Test
    public void removeConnectionListener_whenTimeout_noNotification() {

        waterRower.removeConnectionListener(waterRowerConnectionListener);

        deviceVerificationListener.getValue().onTimeout(DEVICE_NOT_CONFIRMED_TIMEOUT);
        verify(waterRowerConnectionListener, never()).onError(DEVICE_NOT_SUPPORTED);
    }


    // Helper methods:

    private MessageMatcher eqMsg(MessageType type, int distance, WorkoutUnit unit, int restInterval) {
        return new MessageMatcher(type, distance, unit, restInterval);
    }


    public class MessageMatcher extends ArgumentMatcher<ConfigureWorkoutMessage> {

        private MessageType type;
        private int distance;
        private WorkoutUnit unit;
        private int restInterval;

        public MessageMatcher(MessageType type, int distance, WorkoutUnit unit, int restInterval) {
            this.type = type;
            this.distance = distance;
            this.unit = unit;
            this.restInterval = restInterval;
        }

        @Override
        public boolean matches(Object argument) {
            if (!(argument instanceof ConfigureWorkoutMessage))
                return false;

            ConfigureWorkoutMessage msg = (ConfigureWorkoutMessage) argument;
            if (msg.getMessageType() != type)           return false;
            if (msg.getDistance() != distance)          return false;
            if (msg.getWorkoutUnit() != unit)           return false;
            if (msg.getRestInterval() != restInterval)  return false;

            return true;
        }
    }

}