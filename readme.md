# JustQuit
A quit smoking application which uses accelerometer and gyroscope data from Android smartwatch on bluetooth-connected mobile phone to provide real time detection and real time distraction to the user. Our project was a end to end convergence of several technologies including Human Activity Recognition, Deep Learning, Firebase integrated in an Android Smartwatch and Smartphone Application.

The smartwatch used for the project is Samsung Galaxy Watch 4.

The source code can be found in:
 - <code>./mobile/</code>: phone program
 - <code>./wear/</code>: smartwatch program
 - <code>./shared/</code>: shared .lib which defines common data structures and buffers

### Initial steps:
1.	Unzip the source code.
2.	Open Android Studio, click on New > Open Project.
3.	Select the downloaded project folder.

### To run the watch module on a smartwatch:
1.	Enable developer options in Smartwatch
2.	Enable Debug over Wifi and note down the ip and port number
3.	In Android Studio, open project structure.
4.	Copy the path and navigate to SDK Location
5.	Go to platform-tools option
6.	Press Shift and right click to enable power shell
7.	Enter the command "./adb connect 192.168.43.34:5555" on powershell window (Replace the ip and port number with values retieved in Step 2)
8.	Watch gets detected on Android Studio.
9.	Select wear module and download the app.

### To run the watch module on emulator:
1.	After running the mobile module, select the wear module.
2.	Open the Android Virtual Device Manager by selecting Tools > AVD Manager.
3.	Click Create Virtual Device
4.	In the Category pane, select Wear OS and choose a hardware profile. Click Next.
5.	Select a system image to download (preferably API 30 Android 11.0 (Wear OS)).  Click Next and then click Finish.
6.	Check the Device Manager to check if the wearable virtual device is created.
7.	Click on options for your wearable device. Select Pair Wearable
8.	Select the mobile device that is connected throug USB debugging. Click Next.
9.	After the wearable emulator pairing is finished, run the wearable emulator module.
10.	Press start on the wearable app to start streaming from sensors.

### To run the mobile module:
1.	Enable developer options in Smartphone
2.	Enable Debug over USB
3.	In Android Studio, press Allow to allow USB debugging after the mobile is connected over USB cable.
4.	In Android Studio, open project.
5.	Select the connected device over cable on the device manager
6.	Click run project for the mobile module.

### After both apps are launched:
1.	Login via Google Account
2.	If the pairing between the wearable and mobile application was successful, the screen displays "Connected, ready to stream".
3.	Press Done on the mobile application.


Note: Data Streaming and ML predictions work the best when a physical watch is connected to the smartphone. Using a wear emulator gives delayed/unreliable results.