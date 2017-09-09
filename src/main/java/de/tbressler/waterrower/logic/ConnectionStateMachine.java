package de.tbressler.waterrower.logic;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.io.msg.AbstractMessage;
import de.tbressler.waterrower.io.msg.out.ExitCommunicationMessage;
import de.tbressler.waterrower.io.msg.out.RequestModelInformationMessage;
import de.tbressler.waterrower.io.msg.out.StartCommunicationMessage;

import static de.tbressler.waterrower.logic.ConnectionState.*;
import static de.tbressler.waterrower.logic.ConnectionTrigger.*;
import static java.util.Objects.requireNonNull;

/**
 * The connection logic of the Water Rower monitor.
 * The class uses a state machine in order to manage the different states and transitions.
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public abstract class ConnectionStateMachine {

    /* The RXTX communication service. */
    private final RxtxCommunicationService communicationService;

    /* The internal state machine for the connection logic. */
    private StateMachine<ConnectionState, ConnectionTrigger> stateMachine;

    /* Listener for the RXTX communication service. */
    private IRxtxConnectionListener listener = new IRxtxConnectionListener() {

        @Override
        public void onConnected() {
            stateMachine.fire(ON_CONNECTED);
        }

        @Override
        public void onMessageReceived(AbstractMessage msg) {

        }

        @Override
        public void onDisconnected() {
            stateMachine.fire(ON_DISCONNECTED);
        }

        @Override
        public void onError() {
            stateMachine.fire(ON_ERROR);
        }

    };


    /**
     * The logic of the Water Rower monitor.
     *
     * @param communicationService The RXTX communication service, must not be null.
     */
    public ConnectionStateMachine(RxtxCommunicationService communicationService) {
        this.communicationService = requireNonNull(communicationService);
        this.communicationService.addRxtxConnectionListener(listener);
        configureAndInitializeStateMachine();
    }

    /* Configure the state machine and its transitions. */
    private void configureAndInitializeStateMachine() {

        StateMachineConfig<ConnectionState, ConnectionTrigger> configuration = new StateMachineConfig<>();

        // Configure the states and transitions:

        configuration.configure(NOT_CONNECTED)
                .onEntry(this::onDisconnected)
                .permit(DO_CONNECT, CONNECTING);

        configuration.configure(CONNECTING)
                .permit(ON_CONNECTED, CONNECTED_WITH_UNKNOWN_DEVICE)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, NOT_CONNECTED);

        configuration.configure(CONNECTED_WITH_UNKNOWN_DEVICE)
                .onEntry(() -> {
                    // Send 'start communication' message.
                    send(new StartCommunicationMessage());
                })
                .permit(WATER_ROWER_CONFIRMED, CONNECTED_WITH_WATER_ROWER)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, DISCONNECTING)
                .permit(ON_WATCHDOG, DISCONNECTING)
                .permit(ON_PING, CONNECTED_WITH_UNKNOWN_DEVICE);

        configuration.configure(CONNECTED_WITH_WATER_ROWER)
                .onEntry(() -> {
                    // Send 'request model information' message.
                    send(new RequestModelInformationMessage());
                })
                .permit(FIRMWARE_CONFIRMED, CONNECTED_WITH_SUPPORTED_WATER_ROWER)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, DISCONNECTING)
                .permit(ON_WATCHDOG, DISCONNECTING)
                .permit(ON_PING, CONNECTED_WITH_WATER_ROWER);

        configuration.configure(CONNECTED_WITH_SUPPORTED_WATER_ROWER)
                .onEntry(this::onConnectedWithSupportedWaterRower)
                .permit(DO_DISCONNECT, DISCONNECTING)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, DISCONNECTING)
                .permit(ON_PING, CONNECTED_WITH_SUPPORTED_WATER_ROWER);

        configuration.configure(DISCONNECTING)
                .onEntry(() -> {
                    // Send 'exit communication' message.
                    send(new ExitCommunicationMessage());
                })
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, NOT_CONNECTED);

        // Initialize the state machine.
        stateMachine = new StateMachine<>(NOT_CONNECTED, configuration);
    }


    public void connect() {
        stateMachine.fire(DO_CONNECT);
    }


    public void disconnect() {
        stateMachine.fire(DO_DISCONNECT);
    }


    /**
     * Returns true if the state machine is in the given state.
     *
     * @param state The state, must not be null.
     * @return True if state machine is in the given state.
     */
    public boolean isInState(ConnectionState state) {
        return stateMachine.isInState(requireNonNull(state));
    }


    private void onWatchdog() {
        stateMachine.fire(ON_WATCHDOG);
    }


    /**
     * Is called if successfully connected to a Water Rower device with supported firmware.
     */
    abstract void onConnectedWithSupportedWaterRower();


    /**
     * Is called if the connection state machine wants to send messages.
     *
     * @param msg The message to be send.
     */
    abstract void send(AbstractMessage msg);


    /**
     * Is called if disconnected from device.
     */
    abstract void onDisconnected();


    /**
     * Is called if the connection state changed.
     *
     * @param newState The new state.
     */
    // TODO Not used currently!
    abstract void onStateChanged(ConnectionState newState);

}
