# WaterRower Core Library

[![License](https://img.shields.io/badge/License-APL%202.0-green.svg)](https://opensource.org/licenses/Apache-2.0)

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

## Changelog

Currently under development. No releases yet.

## Contribution

Feel free to contribute by forking this repository, making some changes, and submitting pull requests.

## Links

- [WaterRower website](https://www.waterrower.com/world)