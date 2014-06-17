airbitz-wallet-ui-android
=========================
Setting Up For Mac

1. Download Android Studio: http://developer.android.com/sdk/installing/studio.html

2. Open Image and drag to application folder.

3. Download Github for Mac and install.

4. Open and sign into Github, on the left side click on Airbitz.  Click on the button 'Clone to Computer' for the repository
    'Airbitz/airbitz-wallet-ui-android' and select where you want to save.
    
5. Open Android Studio.  It might ask for a Java Runtime Environment which you can get here: 
      http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html
      
6. Select Import Project, navigate to where you pulled the repository from Github, select 'Airbitz-Prototype_BD-Only' and hit ok.

7. Click through the next pages, making sure that the Gradle Environment and wrapper is selected if it pops up. It has been
      built with Gradle and so shouldn't need it.

8. Sometime during the installation/opening of Android Studio, the SDK manager should open, if it doesn't once you have
      Android Studio open, go to Tools->Android->SDK Manager.
      
9. In the SDK manager you will select the packages you need to install
    
    Currently they are: Tools->Android SDK Tools
                               Android SDK Platform-tools
                               Android SDK Build-tools Rev  19.1
                                                            19.0.3
                                                            19.0.1
                        Android 4.4.2 (API 19)->SDK Platform
                                                Google APIs (x86 System Image)
                                                Google APIs (ARM System Image)
                        Android 4.1.2 (API 16)->SDK PLatform
                                                Google APIs
                        Extras->Android Support Repository
                                Android Support Library
                                Google Play Services
                                Google Repository
    
    Some of the Extras might not be available at first, install the others and they should be there.
    
10. To run Airbitz, click the solid green arrow on the toolbar at the top of the window. It might take a second but it
    will pop up a window in which you can select what to run it on.  If you have devices plugged in via USB they will show
    up here, if not you can create an emulator from the popup but I would recommend not doing this if possible as it takes
    forever to load, and is slow and difficult to interact with. Running on a physical Android device is much better.