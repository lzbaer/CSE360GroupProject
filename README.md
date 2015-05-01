# CSE360GroupProject

Please read entire file before proceeding.
-------------
**Database Management**
 - Utilizes https://www.parse.com/
 -  Login: atp649@mail.com
 - Password: balasooriya
	 - Login to Parse
	 - select Database
	 - select Core
	 - select _User table

**Version Control System**
 - Utilizes github
 - Repository location:
 https://github.com/lzbaer/CSE360GroupProject/tree/master

**Installation Instructions**
Choose one of two methods:
 - Install using Android Studio
 - Install without using Android Studio
**General Installation Instructions**
 Follow these instructions for either method of installation:
http://www.kingoapp.com/root-tutorials/how-to-enable-usb-debugging-mode-on-android.htm
- Enable installation from unknown sources: 
http://gs4.wonderhowto.com/forum/enable-unknown-sources-android-install-apps-outside-play-store-0150603/
 - Enable USB mass storage
http://androidadvices.com/enable-usb-storage-android-devices/

**Testing without Android Studio**
 1. Connect Android device to PC via microUSB cable
 2. Navigate to CSE360GroupProject/app
 3. Copy app-release.apk to attached device's storage 
 - Disconnect phone once file is finished transfering
 4. Use a file manager to navigate to directory where app-release.apk is located on your mobile device
 5. Tap app-release.apk
 6. Choose Yes to install

**Installation with Android Studio**
1. Start Android Studio
2. Open CSE360GroupProject
3. Allow gradle to index and build project. Run>Rebuild Project if Gradle build fails.
4. File>Invalidate & Restart if gradle build continues to fail
5. Run app on a device or emulator by clicking green play arrow

**User's Guide Notes**
In order to be rated for urgency or potential condition, a patient **must have at least 15 entries**. This is because a sample size smaller than this reports **highly fluctuating data**. Therefore, if a patient has less than 15 entries, their urgency rating is, **by default**, stable.

All symptoms are rated on a scale of 0 to 10 with **10 being the worst for all symptoms except wellbeing**. For wellbeing, 0 is worst possible wellbeling and 10 is best possible wellbeing. This is how wellbeing is displayed to the user, however, in the datastructure it is stored using the same methods as the other symptoms.

**Potential condition** is a value calculated using datamining. It utilizes each patient's known condition and their symptomatic history. It compares a patient's history to all other patient's histories. From detected similarities, the algorithm will suggest a potential illness.  This function is designed to help identify possible illnesses such as cancer before they become untreatable by recognizing trends in patient mood (and other symptoms as tracked by the Edmonton System Assessment Scale).