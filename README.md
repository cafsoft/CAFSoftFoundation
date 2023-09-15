# Foundation (CAFSoft)

CAFSoftFoundation is a software development framework written in Java, designed to offer some network communication functionalities similar to those of Apple's Foundation framework.

[![](https://jitpack.io/v/cafsoft/CAFSoftFoundation.svg)](https://jitpack.io/#cesarfrancoe/CAFSoftFoundation)


[https://jitpack.io/#cafsoft/CAFSoftFoundation](https://jitpack.io/#cesarfrancoe/CAFSoftFoundation)


To install the library add: 

- Repository to settings.gradle

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

- Library to build.gradle (Module):

 
   ```gradle
   dependencies {
         implementation 'com.github.cafsoft:CAFSoftFoundation:2.5.7'
   }
   ```

# URLSession Example in Swift

## Requirements

- Foundation (Apple)
- Swift 5.0 or later
- iOS 13.0 or later

## How to Use

Simply copy the following Swift code into your project.

```swift
import Foundation

// Initialize URLComponents
var components = URLComponents()
components.scheme = "https"
components.host = "api.example.com"
components.path = "/data"

// Generate the URL from the components
guard let url = components.url else {
    fatalError("Could not create URL from components")
}

// Create a URLSession
let session = URLSession.shared

// Create a network task for the GET request
let task = session.dataTask(with: url) { (data, response, error) in
    // Handle general errors
    if let error = error {
        print("Error: \(error)")
        return
    }
    
    // Verify that the response is an HTTPURLResponse object
    if let httpResponse = response as? HTTPURLResponse {
        // Check the status code
        if httpResponse.statusCode == 200 {
            // Process the received data
            if let data = data {
                print("Data received: \(data)")
            }
        } else {
            print("Received HTTP \(httpResponse.statusCode), expected 200")
        }
    } else {
        print("Response was not HTTPURLResponse")
    }
}

// Start the task
task.resume()
```

# URLSession Example in Java

## Requirements
- Foundation (CAFSoft) 2.5.7
- Java 11 or later

```java
// Initialize URLComponents
var components = new URLComponents();
components.setScheme("https");
components.setHost("api.example.com");
components.setPath("/data");

// Generate the URL from the components
var url = components.getURL();

// Get Default URLSession
var session = URLSession.getShared();

// Create a network task for the GET request
var task = session.dataTask(url, (data, response, error) -> {

    // Handle general errors
    if (error != null){
        // Network error
        return;
    }

    // Check the status code
    if (response instanceof HTTPURLResponse) {
        var httpResponse = (HTTPURLResponse) response;

        // Process the received data
        if (httpResponse.getStatusCode() == 200) {
            if (data != null) {
                System.out.println(data.toText());
            }
        } else {
            // Status code is not equals to 200
        }
    }
});

// Start the task
task.resume();
```


