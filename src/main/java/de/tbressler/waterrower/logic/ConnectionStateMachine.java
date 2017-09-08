package de.tbressler.waterrower.logic;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import de.tbressler.waterrower.io.IRxtxConnectionListener;
import de.tbressler.waterrower.io.RxtxCommunicationService;
import de.tbressler.waterrower.msg.AbstractMessage;

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
                .onEntry(() -> {
                    // TODO Action when not connected.
                })
                .permit(DO_CONNECT, CONNECTING);

        configuration.configure(CONNECTING)
                .onEntry(() -> {
                    // TODO Action when connecting.
                })
                .permit(ON_CONNECTED, CONNECTED_WITH_UNKNOWN_DEVICE)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, NOT_CONNECTED);

        configuration.configure(CONNECTED_WITH_UNKNOWN_DEVICE)
                .onEntry(() -> {
                    // TODO Action when connected to unknown device.
                })
                .permit(WATER_ROWER_CONFIRMED, CONNECTED_WITH_WATER_ROWER)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, DISCONNECTING)
                .permit(ON_WATCHDOG, DISCONNECTING);

        configuration.configure(CONNECTED_WITH_WATER_ROWER)
                .onEntry(() -> {
                    // TODO Action when connected to Water Rower with unknown firmware.
                })
                .permit(FIRMWARE_CONFIRMED, CONNECTED_WITH_SUPPORTED_WATER_ROWER)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, DISCONNECTING)
                .permit(ON_WATCHDOG, DISCONNECTING);

        configuration.configure(CONNECTED_WITH_SUPPORTED_WATER_ROWER)
                .onEntry(() -> {
                    // TODO Action when connected to supported Water Rower.
                })
                .permit(DO_DISCONNECT, DISCONNECTING)
                .permit(ON_DISCONNECTED, NOT_CONNECTED)
                .permit(ON_ERROR, DISCONNECTING);

        configuration.configure(DISCONNECTING)
                .onEntry(() -> {
                    // TODO Action when disconnecting.
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


    private void onWatchdog() {
        stateMachine.fire(ON_WATCHDOG);
    }

}
