package de.tbressler.waterrower;

import de.tbressler.waterrower.model.ModelInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 *
 *
 * @author Tobias Bressler
 * @version 1.0
 */
public class WaterRower {

    /* Listeners. */
    private List<IWaterRowerListener> listeners = new ArrayList<>();


    /**
     *
     */
    public WaterRower() {

    }


    /**
     * Connect to the rowing computer.
     *
     * @throws IOException
     */
    public void connect() throws IOException {
        throw new IllegalStateException("Not implemented yet!");
    }

    /**
     * Disconnects from the rowing computer.
     *
     * @throws IOException
     */
    public void disconnect() throws IOException {
        throw new IllegalStateException("Not implemented yet!");
    }


    /**
     * Returns the model information from the rowing computer.
     *
     * @return
     */
    public ModelInformation getModelInformation() {
        throw new IllegalStateException("Not implemented yet!");
    }


    /**
     * Request the rowing computer to perform a reset; this will be identical to the user performing this with the
     * power button. Used prior to configuring the rowing computer from a PC. Interactive mode will be disabled on a
     * reset.
     *
     * @throws IOException
     */
    public void performReset() throws IOException {
        throw new IllegalStateException("Not implemented yet!");
    }


    /**
     * Adds the listener.
     *
     * @param listener The listener, must not be null.
     */
    public void addWaterRowerListener(IWaterRowerListener listener) {
        listeners.add(requireNonNull(listener));
    }

    /**
     * Removes the listener.
     *
     * @param listener The listener that should be removed, must not be null.
     */
    public void removeWaterRowerListener(IWaterRowerListener listener) {
        listeners.remove(requireNonNull(listener));
    }

}
