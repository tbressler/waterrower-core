# WaterRower Core Library

[![License](https://img.shields.io/badge/License-APL%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
[![Travis CI](https://travis-ci.org/tbressler/waterrower-core.svg?branch=master)](https://travis-ci.org/tbressler/waterrower-core)

A Java library which connects the WaterRower S4/S5 Performance Monitor with Windows, Linux or macOS.

## Usage

The library requires JDK 9 or higher.

The usage of the library is very simple. Just start with the following examples:

### Connect / disconnect

```Java

// Establish a connection:
WaterRower waterRower = new WaterRower();
waterRower.addConnectionListener(...);
waterRower.connect(...);

...

// Disconnect:
waterRower.disconnect();

```

If you don't want to search for the correct port manually, you can use the class ```WaterRowerAutoDiscovery```. The auto-discovery automatically searches for the available ports and tries to connect the WaterRower.
In this case you don't need to connect the WaterRower yourself.

```Java

// Initialize and start the auto-discovery:
WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower, Executors.newSingleThreadScheduledExecutor());
discovery.start();

...

// Stop the auto-discovery:
discovery.stop();

```

If the connection to the WaterRower gets lost. The auto-discovery tries to reconnect automatically.

Please note, you can use the interface ```IDiscoveryStore``` in order to improve the performance when searching for serial ports.


### Subscribe to values

A simple example:

```Java

// Subscribe to events:
waterRower.subscribe(new StrokeSubscription() {
    public void onStroke(StrokeType strokeType) {
        // ... do your stuff here!
    }
}

...

```

The following value subscriptions are available:

| Subscription | Description |
|---|---|
| ```DisplayedDistanceSubscription``` | A subscription for the *displayed distance* (in meters) on the distance window of the Performance Monitor. The value is set to zero if the Performance Monitor was reset. |
| ```DisplayedDurationSubscription``` | A subscription for the *displayed duration* on the duration window of the Performance Monitor. The duration window displays the time covered (or time to be covered in a duration workout). |
| ```AverageVelocitySubscription``` | A subscription for the *displayed average velocity* (in meters per second) on the intensity window of the Performance Monitor. |
| ```StrokeSubscription``` |  A subscription for *stroke events*. The values will be send immediately by the Performance Monitor and will not be polled by the library. |
| ```StrokeCountSubscription``` | A subscription for the *stroke count* value (number of strokes). |
| ```AverageStrokeRateSubscription``` | A subscription for the *displayed stroke rate* (strokes/min) of a whole stroke which is displayed in the stroke rate window of the Performance Monitor. |
| ```TankVolumeSubscription``` | A subscription for the *tank volume* value (in liters). This is the value the user has set in the Performance Monitor (see manual). |
| ```ClockCountDownSubscription``` | A subscription for *clock count down* values. This value is transmitted if a count down is programmed in the Performance Monitor. |
| ```TotalDistanceSubscription``` | A subscription for the *total distance* values of the Performance Monitor. The value represents the total distance meter counter - this value will be reset to zero when the Performance Monitor is switched off. |
| **Advanced subscriptions:** | | 
| ```PulseCountSubscription``` | A subscription for *pulse count* events. Will be called, when pulse count was updated. The value is representing the number of pulse’s counted during the last 25mS period; this value can range from 1 to 50 typically. (Zero values will not be transmitted). |
| ```TotalVelocitySubscription``` | A subscription for the *total velocity* (in meters per second). |

Don't forget to unsubscribe, if you are no longer interested in a value. For example it makes sense to get the tank volume after the WaterRower was connected. But because it is very unlikely that the value will change during a session, you can unsubscribe this subscription after you got the value once.

The library needs to poll for each value of each subscription synchronously. Thus a high number of subscriptions may lead to performance issues.  

### Configure workouts

Workouts can be a *single* or an *interval workout* (with rest intervals).

```Java

// Send a single workout:
Workout workout = new Workout(2000, METERS);
waterRower.startWorkout(workout);

// Send an interval workout:
Workout workout = new Workout(1000, METERS);
workout.addInterval(30, 2000);
workout.addInterval(30, 2000);
waterRower.startWorkout(workout);

...

```

The unit given in the class `Workout` also determines the units of the intervals.

Overall there are 2 types of workouts based on the unit:

- Duration workout (time)
- Distance workout (distance or strokes)

The workout types or units can not be mixed up in an interval workout.

### Find available serial ports (manually)

```Java

// Get all available serial ports:
SerialPort[] commPorts = SerialPort.getCommPorts();

// Get the name of the ports:
for (SerialPort port : commPorts) {
    String portName = port.getSystemPortName();
}

```

## Compatibility

The following devices were tested:

- WaterRower S4 Performance Monitor (Firmware 2.1)

If you have successfully tested more devices please feel free to add them to issue [#8](https://github.com/tbressler/waterrower-core/issues/8).

Thank you for your support :-)

## License

```
   Copyright 2017 Tobias Bressler

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

## Contribution

Feel free to contribute by forking this repository, making some changes, and submitting pull requests.

## Links

- [WaterRower website](https://www.waterrower.com/world)
- [Netty project](https://netty.io/)
