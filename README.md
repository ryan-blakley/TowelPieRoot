INTRO
-------

Towel Pie Root is a one click untethered app to root the Moto X, Moto G and the Droid Series(maybe others but these for sure) running 4.4.3 and below. It does NOT and WILL NOT root 4.4.4 so stop asking about it!

I want to started off by Thanking Geohot for Towelroot and JCase for Pie root, without them this wouldn't have been possible and I take NO credit for their exploits.  The towelroot exploit lib is not included in the source, since I did not create it.

Towel Pie Root combines Towelroot's futex exploit with a modified version of Pie root's xbin.img mount into one app. To achieve a one click untethered root for write protected devices! This DOES NOT remove write protection, and is still technically a temp root, I did include the option to enable root on boot. Which will re-root your device every time you reboot your phone, it also includes the option to perform a full reboot, or a soft reboot from within the app.

Here is a more technical description of what my app does. It includes TR's libexploit.so(aka the native exploit by Geohot) which I use to gain root privileges. Once it has root privileges it then echo's the file path to my modified Pie root file to uevent_helper. It then causes a hotplug event to trigger the kernel to execute the file path just echoed to uevent_helper. My modified Pie root files, kill TR's daemonsu and mounts Pie root's xbin.img over /system/xbin. Then it copies the newer version of SuperSU's su binary and executes the new daemonsu daemon and viola root.  There is also the option to mount a bin.img over /system/bin to take advantage of xposed.  

The source posted is the imported source into Android Studio, the original app that was released was compiled in eclispe.


LICENSE
-------

Copyright (C) 2014-2015 Ryan Blakley

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
General Public License @[gnu.org/licenses/gpl.html](http://gnu.org/licenses/gpl.html>) for more details.
