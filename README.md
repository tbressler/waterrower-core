# WaterRower Core Library
A Java library which connects the WaterRower S4/S5 Performance Monitor with your multi-platform Java application.

## Usage

The library requires JDK 1.8 or higher.

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

## License

Apache 2.0

## Links

- [WaterRower website](https://www.waterrower.com/world)