# WaterRower Core Library

[![License](https://img.shields.io/badge/License-APL%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)

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

If you don't want to search for the port manually, you can use the class ```WaterRowerAutoDiscovery```. The auto-discovery automatically searches for available ports and tries to connect the WaterRower.
In this case you don't need to connect the WaterRower yourself.

```Java

// Initialize and start the auto-discovery:
WaterRowerAutoDiscovery discovery = new WaterRowerAutoDiscovery(waterRower);
discovery.start();

...

// Stop the auto-discovery:
discovery.stop();

```

If the connection to the WaterRower gets lost, the auto-discovery tries to reconnect automatically.

### Subscribe to values

You can subscribe to the different values of the WaterRower Performance Monitor. There is a subscription for every signal available.

A simple example:

```Java

// Subscribe to events:
waterRower.subscribe(new DistanceSubscription() {
    public void onDistanceUpdated(double distance) {
        // ... do your stuff here!
    }
}

...

```

The following subscriptions are available:

| Value | Subscription | Description |
|---|---|---|
| distance | ```DistanceSubscription``` | Subscribes to the current *distance* (in meter). This value will be reset to zero when a new row interval begins or the Performance Monitor was reset. |
| total distance | ```TotalDistanceSubscription``` | Subscribes to the *total distance* (in meter). The value represents the total distance meter counter - this value will be reset to zero when the Performance Monitor is switched off. |
| displayed distance | ```DisplayedDistanceSubscription``` | Subscribes to the *displayed distance* (in meters), which is displayed in the distance window of the Performance Monitor. The value is set to zero when the Performance Monitor was reset. The values can count down or up depending on the workout. |
| displayed duration | ```DisplayedDurationSubscription``` | Subscribes to the *displayed duration*, which is the time covered (or the time to be covered in a duration workout) and shown in the duration window of the Performance Monitor. The values can count down or up depending on the workout. |
| velocity | ```AverageVelocitySubscription``` | Subscribes to the displayed *average velocity* (in meters per second) on the intensity window of the Performance Monitor. |
| stroke event | ```StrokeSubscription```* |  Subscribes to the *stroke events* (start of stroke or end of stroke). The values will be send immediately by the Performance Monitor and will not be polled by the library. |
| stroke count | ```StrokeCountSubscription``` | Subscribes to the *stroke count* value (the number of strokes). This value will be reset to zero when a new row interval begins or the Performance Monitor was reset. |
| stroke rate | ```AverageStrokeRateSubscription``` | Subscribes to the displayed *stroke rate* (strokes/min) of a whole stroke which is displayed in the stroke rate window of the Performance Monitor. |
| tank volume | ```TankVolumeSubscription``` | Subscribes to the *tank volume* (in liters). This is the value the user has set in the Performance Monitor (see manual). |
| clock count down | ```ClockCountDownSubscription``` | Subscribes to the *clock count down*. This value is transmitted if a count down is programmed in the Performance Monitor. |
| power (watt) | ```WattsSubscription``` | Subscribes to the current *power value* (in watt). |
| calories | ```TotalCaloriesSubscription``` | Subscribes to the *total calories* (in cal) burned. |
| heart rate | ```HeartRateSubscription``` | Subscribes to the *heart rate* (in bpm) of the optional heart rate sensor. *This subscription was not tested yet, due to the absence of such a device.* |

Advanced subscriptions:

| Value | Subscription | Description |
|---|---|---|
| pulse count | ```PulseCountSubscription```* | A subscription for *pulse count* events. Will be called, when pulse count was updated. The value is representing the number of pulse’s counted during the last 25mS period; this value can range from 1 to 50 typically. (Zero values will not be transmitted). |
| total velocity | ```TotalVelocitySubscription``` | A subscription for the *total velocity* (in meters per second). |
| misc flags | ```MiscFlagsSubscription``` | A subscription for different flags like *battery low*, *PC connected* or *row/rest interval active*. |

Don't forget to unsubscribe, if you are no longer interested in a value. For example it makes sense to get the tank volume after the WaterRower was connected. But because it is very unlikely that the value will change during a session, you can unsubscribe this subscription after you got the value once.

The library needs to poll for each value of each subscription synchronously. Thus a high number of subscriptions may lead to performance issues. 

*The two subscriptions ```PulseCountSubscription``` and ```StrokeSubscription``` are transmitted by the WaterRower without polling.*

### Configure workouts

Workouts can be a *single* or an *interval workout* (with rest intervals).

```Java

// Send a single workout:
Workout workout = new Workout(2000, METERS);
waterRower.startWorkout(workout);

// Or send an interval workout:
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

For workouts the following subscriptions are available:

| Value | Subscription | Description |
|---|---|---|
| workout flags | ```WorkoutFlagsSubscription``` | Subscribes to the *workout flags*. The returned object has flags for the different workout modes. |
| workout intervals | ```WorkoutIntervalsSubscription``` | Subscribes to the *number of configured workout intervals* at the Performance Monitor. |
| total workout time | ```TotalWorkoutTimeSubscription``` | Subscribes to the *total workout time*. The time is updated by the Performance Monitor after each workout interval and at the end of the workout. |
| total workout distance | ```TotalWorkoutDistanceSubscription``` | Subscribes to the *total workout distance*. The distance is updated by the Performance Monitor after each workout interval and at the end of the workout. |
| total workout strokes | ```TotalWorkoutStrokesSubscription``` | Subscribes to the *total workout strokes* (number of strokes). The stroke value is updated by the Performance Monitor after each workout interval and at the end of the workout. |
| total workout limit | ```TotalWorkoutLimitSubscription``` | Subscribes to the *total workout limit*. |
| workout interval values | ```WorkoutIntervalValueSubscription``` | Subscribes to the values of the *configured workout* and *workout intervals*. |

### Find available serial ports (manually)

Not recommended, but for the sake of completeness:

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

- WaterRower S4 Performance Monitor (Firmware 2.10)

If you have successfully tested more devices please feel free to add them to issue [#8](https://github.com/tbressler/waterrower-core/issues/8).

Thank you for your support :-)

## License

```
   Copyright 2017 - 2024 Tobias Bressler

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
