# a space project
Welcome to A Space Project. A project involving space...duh.
* Enjoy Galactic Space Exploration in a sate-of-the-art, hyper-realistic physics simulation of the entire universe!
* Get a realistic sense of the cosmic scale; there's literally dozens of planets and traveling between them could take up to minutes!
* You want Faster Than Light Travel, you got it. (yeah it's real bro cuz like quantum anti-dark matter n' stuff yo)
* During your explorations you could find various astronomical bodies including:
    * Unique planetary systems
    * Binary star systems, Trinary star systems, and even quadri... quatro? quadrino-ary? f**k it, as-many-as-you-want star systems!
    * Discover lonely rogue planets who lost their son. :(
* Pilot different spaceships and combat dumb AI written by an even dumber human ;)
* Then when your bored of that sh*t you can land on a planet I guess. The worlds are flat (Ha! take that round Earthers)

Ok, seriously: The are two main views. Space & World.
Space is a finite galaxy, generated by a set of points. Each point is a collection of stars and/or planets.
Worlds are also finite. If you move continuously in one direction you will eventually return to where you started. Wrapped via a 4D torus.

This game is still in alpha phase and suffers severe feature creep. There's not much content yet but I have plans. Stay tuned.



| Control            | Desktop      | Mobile                                   | Controller/Gamepad |
|------------------- | ------------ | ---------------------------------------- | -------------------|
| Movement           | WASD         | Left Joystick                            |                    |
| Aim                | Mouse        | Left Joystick                            |                    |
| Attack / Shoot     | Right-Click  | one of the buttons, you'll figure it out |                    |
| Defence / Shield   | Shift        | todo                                     |                    |
| Defence / Dodge    | ALT + A/D    | todo                                     |                    |
| Land/Take Off      | T            | one of the buttons, you'll figure it out |                    |
| Enter/Exit vehicle | G            | one of the buttons, you'll figure it out |                    |
| Zoom               | Scroll Wheel | Pinch Zoom (not implemented yet)         |                    |
| Toggle Map State   | M            | one of the buttons, you'll figure it out |                    |
| Full screen        | F11          | its always fullscreen on mobile silly    |                    |
| Vsync              | F8           |                                          |                    |
| ECS Debug Viewer   | F9           |                                          |                    |
| Misc debug keys I am too lazy to document rn and won't be permanent anyway   |                    |


## Features
* Open Source bruh!
* DRM-Free!
* Ad-Free!
* Telemetry-Free!
* Cloud-Free!
* Cross-Platform Desktop and Mobile Support
  * Windows, OSX, Linux, Android, IOS
* Controller support: todo
* Developer Tools & Mod support (in progress)
* Multiplayer
  * haha, just kidding. submit a pull request. do I have to do everything around here?
* Unit Tests
  * pfft... my code is perfect, the first time. every time.


## Current Status
Work in progress. Currently in early development. More of an engine than a game at this point as there is not much content, just scaffolding for what will hopefully one day be a game. Actively...and painfully slowly...in progress... 


![screenshot](/Capture.PNG?raw=true)

## Libraries
- [libGDX](https://github.com/libgdx/libgdx)
- [Ashley](https://github.com/libgdx/ashley/wiki)
- [OpenSimplexNoise](https://gist.github.com/KdotJPG/b1270127455a94ac5d19)
- [VisUI](https://github.com/kotcrab/vis-editor/wiki/VisUI)


## Building
**General**
* Set up your [Development Environment](https://libgdx.badlogicgames.com/documentation/gettingstarted/Setting%20Up.html)
* Make sure Android SDK is installed.
* Import project in IDE of choice using gradle.
* If a "File not found" error occurs, check the working directory. Append "android\assets" to the working directory in run configurations.


**Android Studio**
* Desktop
  * create Run Configuration
  * main class = com.spaceproject.desktop.DesktopLauncher
  * use classpath of module 'desktop'
  * working directory = ...\SpaceProject\android\assets
  * (must ensure working directory includes assets so data like fonts and configs can be loaded)
  * build and run!
* Android
  * enable dev options, enable usb debugging
  * connect phone, android studio should detect it
  * build and run!
* IOS
  * dunno fam, this shits broken for me currently. something about the provisioner ¯\_(ツ)_/¯

