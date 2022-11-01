# notification-display-demo
A demo APP to reproduce a bug(?) that notification did not display correctly.

## Detail
This demo APP could send some notifications with time delay and do some update work to them.

These notifications display by time in most devices expect Pixel.
The first send notification which expect displayed at last is displaying on top:


![image](https://user-images.githubusercontent.com/2028196/199209674-66c9d23f-a601-4e35-b2c5-a4e07a797211.png)

- Currently could only reproduced on Pixel device.
- All logic contains in `MainActivity.kt`.
