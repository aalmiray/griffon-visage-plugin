
Brings the Visage language compiler and libraries
-------------------------------------------------

Plugin page: [http://artifacts.griffon-framework.org/plugin/visage](http://artifacts.griffon-framework.org/plugin/visage)


The Visage plugin enables compiling and running [Visage][1] code on your Griffon application.

Usage
-----
You must place all Visage code under `$appdir/src/visage`, it will be compiled first before any sources available on 
`griffon-app` or `src/main` which means you can't reference any of those sources from your Visage code, while the
Groovy sources can.


[1]: http://code.google.com/p/visage/
[2]: /plugin/lang-bridge

