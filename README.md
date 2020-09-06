# Game-Map-Maker
Java GUI program that enables the user to create maps for 2d games with ease.

## How to use

1. Open the Map_Maker.jar file.<br>
2. Select a block by left clicking a sprite from the sprite sheet on the right. ( You can scroll up / down to reveal more sprites )<br>
3. Click / Drag your mouse on the grid to draw.<br>
4. Delete blocks by right clicking / draging your mouse over them.<br>
5. After finishing the map, make sure to save it by clicking the "Save Map" button on the top right.<br>

* Use the extra features for better control, ease of use, and over all a better experience.

*Please load the "Examle_Map.txt" for reference*

## Features

* Importing / Exporting - Enable's saving and loading of maps.<br>
* Map Resizing - Scale the map size to the perfect size for your game.<br>
* Zoom In/Out - Uses the scroll wheel, to zoom in/out.<br>
* Block Picker Mode - Enable's selection of a block to draw with, from the grid.<br>
* Hand Mode - Enable's movement of the grid.<br>
<br>
* Secret Options - Enable's the user to see the current modes selected.

### How to use

#### Importing / Exporting of the map

On the top right corner of the window, there are 2 button that control the importing / exporting of the map.<br>
The "Load Map" button corresponds to importing the saved map.<br>
The "Save Map" button corresponds to exporting of the map.<br>
<br>
To import a map, click on the "Load Map" button, search for your saved map, and click open.<br>
To export a map, click on the "Save Map" button, and a new file will be created in the directory that the program was lanuched from. ( The exported file name "Map.txt" ) 

#### Map Resizing

On the top right corner of the window, there are 2 text fields. ( Width & Height )<br>
Enter the width and height of the grid, then click the "Resize Grid" button.

#### Zoom In/Out

When hovering on the grid simply use your scroll wheel, to zoom in/out of the grid.

#### Block Picker Mode

When activated the user can select a block from the grid, and can then start drawing with it.<br>
- Press 'b' to activate / deactivate.

#### Hand Mode

When activated, clicking on the grid will "catch" the grid at that point, and you will be able to move the grid freely.<br>
- Press 'v' to activate / deactivate.

#### Secret Options

This mode is used to clear up space in the window.<br>
When activated you will be able to see a menu with all the modes, and their current status.<br>
- Press 'o' to reveal / hide the menu.


## How to use my own sprite sheet ?

Resized your sprite sheet width to 256px. ( The height isn't a factor )<br>
Each sprite should be 64x64.<br>
Change your sprite sheet name to : 'Sprite.png'<br>
Lastly replace the file in './src/ResourcePack/', with your file.<br>
<br>
Note :<br>
* The sprite's top column and the left row are used as a separators. ( each sprite is actually 63x63 with a buffer at the top and left )

## Built With

[JavaFX](https://openjfx.io/) - The GUI framework used.
