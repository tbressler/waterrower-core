# Water Rower Core Library
The first Java library for the Water Rower S4/S5 monitor.

## Usage

The usage of the library is very simple. Just start with the following example:

```Java

// Establish a connection:
WaterRower waterRower = new WaterRower();
waterRower.addConnectionListener(...);
waterRower.connect(...);

// Subscribe to events:
waterRower.subscribe(new StrokeSubscription() {
    public void onStroke(StrokeType strokeType) {
        // ... do your stuff here!
    }
}

...

// Disconnect:
waterRower.disconnect();

```

## Compatibility

The following devices were tested:

- S4: 2.xx
- S5: ????