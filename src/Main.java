import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.application.Application;
import javafx.event.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

public class Main extends Application {

	public static void main(String[] args)
	{
		launch(args); // Starting the application - Starting the function start.
	}

	Dimension ScreenDimension = Toolkit.getDefaultToolkit().getScreenSize(); // Getting Screen Dimension.
	double ScreenWidth = ScreenDimension.getWidth();	// Getting The Screen Width.
	double ScreenHeight = ScreenDimension.getHeight();	// Getting The Screen Height.

	double BlockSize = ScreenHeight*0.05;
	int SpriteLocation=1;

	ArrayList<ArrayList<String>> Options; // Containing A Lot Of Stuff. (BugFixes And Activated Modes) -> See Full Description In SettingTheArray.

	public void start(Stage PrimaryStage) throws Exception
	{
		PrimaryStage.setTitle("Map Maker");	// Creates The Full Frame With The Window.

		Pane MainLayout = new Pane();		// Creates A Layout To Put Stuff On.
		MainLayout.setId("MainLayout");		// Set An ID For The Layout.

		Scene Scene = new Scene(MainLayout,ScreenWidth*0.5,ScreenHeight*0.5);	// Create The Frame Dimensions (Scene).
		Scene.setOnMouseMoved(Event -> MouseMoved(Event,Scene));				// Add An Event When The Mouse Is Moved.
		Scene.setOnMousePressed(Event -> MousePressed(Event,Scene));			// Add An Event When The Mouse Is Pressed.
		Scene.setOnMouseDragged(Event -> MouseDragged(Event,Scene));			// Add An Event When The Mouse Is Dragged.
		Scene.setOnScroll(Event -> OnScroll(Event,Scene));						// Add An Event When The Mouse Is Scrolled.
		Scene.setOnKeyPressed(Event -> KeyPressed(Event,Scene));				// Add An Event When A Key On The Keyboard Is Pressed.

		CreateScreen(Scene);
		SettingTheArray(Scene);
		PrimaryStage.setScene(Scene);
		PrimaryStage.show();
	}

	public void CreateScreen (Scene Scene)
	{
		CreatePane(0,0,ScreenWidth*0.5-ScreenHeight*0.2,ScreenHeight*0.5,"DAE6F3","Grid","MainLayout",Scene);
		CreatePane(0,0,0,0,"FFFFFF","Menu","MainLayout",Scene);
		CreateImageView(ScreenWidth*0.5-ScreenHeight*0.2,0,ScreenHeight*0.2,ScreenHeight,"file:src/ResourcePack/White.png","MenuBackGround",false,0,0,0,0,"Menu", Scene);
		CreateImageView(ScreenWidth*0.5-ScreenHeight*0.2,ScreenHeight*0.1,ScreenHeight*0.2,ScreenHeight*0.9,"file:src/ResourcePack/Sprite.png","Sprite",false,0,0,0,0,"Menu", Scene);
		CreateImageView(ScreenWidth*0.5-ScreenHeight*0.2,0,ScreenHeight*0.2,ScreenHeight*0.1,"file:src/ResourcePack/White.png","MenuControlPanel",false,0,0,0,0,"Menu", Scene);
		CreateLabel(ScreenWidth*0.5-ScreenHeight*0.19,ScreenHeight*0.01,0,0,"X Coordinates : 0\nY Coordinates : 0","Coordinates","Menu",Scene);
		CreateLabel(ScreenWidth*0.5-ScreenHeight*0.19,ScreenHeight*0.04,0,0,"HiddenOptions : False","MoreOptions","Menu",Scene);
		CreateButton(ScreenWidth*0.5-ScreenHeight*0.19,ScreenHeight*0.056,0,0,"Save Map",Event->Save(Scene),"Button_Save","Menu",Scene);
		CreateButton(ScreenWidth*0.5-ScreenHeight*0.13,ScreenHeight*0.056,0,0,"Reset Grid",Event->ResetGrid(Scene),"Button_ResetGrid","Menu",Scene);
		CreateButton(ScreenWidth*0.5-ScreenHeight*0.07,ScreenHeight*0.056,0,0,"Load Map",Event->LoadMap(Scene),"Button_LoadMap","Menu",Scene);
		CreateButton(ScreenWidth*0.5-ScreenHeight*0.07,ScreenHeight*0.078,0,0,"Resize Grid",Event->RescaleGrid(Scene),"Button_NewGrid","Menu",Scene);
		CreateTextField(ScreenWidth*0.5-ScreenHeight*0.19,ScreenHeight*0.078,0,0,"Width","TextField_NewWidth","Menu",Scene);
		CreateTextField(ScreenWidth*0.5-ScreenHeight*0.13,ScreenHeight*0.078,0,0,"Height","TextField_NewHeight","Menu",Scene);		
	}
 
	public void SettingTheArray(Scene Scene) // Setting-Up An Array. -> The Memory Of The System
	{
		// Acting As The Memory Of The System. (Short Term Only)
		// UserSettings: GridWidth,GridHeight,BlockSize,SpriteLocation. () -> Grid Width/Height = Bug Fix | Index In Full Array 0.
		// MoreOptions: On/Off. (An Indicator If You Want To See The Full Options Of The Program) -> Press 'O' To Activate | Index In Full Array 1.
		// HandMode: On/Off,ModeEffecting,MouseXPosition,MouseYPosition. (Moving The Grid/SpriteSheet According To The Mouse Movement) -> Press 'V' To Activate | Index In Full Array 2.
		// BlockPicker: On/Off. (Changing The Indicator Of The Sprite Sheet To The Same As The Object That Was Click On) -> Press 'B' To Activate | Index In Full Array 3.
		// SelectMode: On/Off,FirstMouseXPostion,FirstMouseYPosition,SecondMouseXPosition,SecondMouseYPosition,SelectedObjects. (Selects An Area And Copy's All The Objects In That Area) -> Press 'S' To Activate | Index In Full Array 4.

		Options=new ArrayList<ArrayList<String>>();					// Setting-Up The Array.
		ArrayList<String> UserSettings = new ArrayList<String>();	// Setting-Up The Settings Of The Program.
		UserSettings.add(0,String.valueOf(ScreenWidth*0.5-ScreenHeight*0.2));
		UserSettings.add(1,String.valueOf(ScreenHeight*0.5));
		UserSettings.add(2,String.valueOf(ScreenHeight*0.05));
		UserSettings.add(3,"0");
		ArrayList<String> MoreOptions = new ArrayList<String>();
		MoreOptions.add(0,"False");
		ArrayList<String> HandMode = new ArrayList<String>();		// Setting-Up The HandMode.
		HandMode.add(0,"False");
		HandMode.add(1,null);
		HandMode.add(2,null);
		HandMode.add(3,null);
		ArrayList<String> BlockPicker = new ArrayList<String>();	// Setting-Up The BlockPicker.
		BlockPicker.add(0,"False");
		ArrayList<String> SelectMode = new ArrayList<String>();		// Setting-Up The SelectMode.
		SelectMode.add(0,"False");
		SelectMode.add(1,null);		// First Point.
		SelectMode.add(2,null);
		SelectMode.add(3,null);		// Second Point.
		SelectMode.add(4,null);
		SelectMode.add(5,null);		// Array Of The Objects Selected.
		Options.addAll(Arrays.asList(UserSettings,MoreOptions,HandMode,BlockPicker,SelectMode));
	}

	public void RescaleGrid(Scene Scene) // Re-Scaling The Grid To The Given Width And Height.
	{
		Pane Grid = (Pane) Scene.lookup("#Grid");		// Loading The Grid.
		ArrayList<String> GridSize = Options.get(0);
		TextField TextField_NewWidth = (TextField) Scene.lookup("#TextField_NewWidth");
		TextField TextField_NewHeight = (TextField) Scene.lookup("#TextField_NewHeight");
		
		if (TextField_NewWidth.getText().length()>0 && TextField_NewHeight.getText().length()>0 && TextField_NewWidth.getText().matches("[0-9]+") && TextField_NewHeight.getText().matches("[0-9]+")) {
		
		int NewWidth = Integer.parseInt(TextField_NewWidth.getText());
		int NewHeight = Integer.parseInt(TextField_NewHeight.getText());
		int MoveBy = NewWidth-(int)(Double.parseDouble(GridSize.get(0))/BlockSize);

		if (Grid.getChildren().size()!=0)				// Checks If The Grid Is Empty.
		{
			ArrayList<Integer> MapObjects = new ArrayList<Integer>(Grid.getChildren().size());				// Array That Will Contain The ID's Of All The Objects On The New Grid.
			ArrayList<ImageView> ObjectsOutOfBounds = new ArrayList<ImageView>(Grid.getChildren().size());	// Array That Will Contain All The Objects That Are Out Of Bounds.

			for(int ObjectNumber=0;ObjectNumber<Grid.getChildren().size();ObjectNumber++)					// Sorting The Objects Between The Arrays.
			{
				ImageView ObjectOnMap = (ImageView) Grid.getChildren().get(ObjectNumber);

				if (Integer.parseInt(ObjectOnMap.getId())%(int)(Double.parseDouble(GridSize.get(0))/BlockSize)>=NewWidth || Integer.parseInt(ObjectOnMap.getId())/(int)(Double.parseDouble(GridSize.get(0))/BlockSize)>=NewHeight)
					ObjectsOutOfBounds.add(ObjectOnMap);
				else
					MapObjects.add(Integer.parseInt(ObjectOnMap.getId()));
			}

			if (MoveBy>0)								// Checking If The Grid Got Smaller Or Bigger. (Width Wise) -> Prevent Overlapping In The ID When Adding/Subtracting From It.
				Collections.sort(MapObjects, Collections.reverseOrder());	// Reversing The Array And Sorting It.
			else
				Collections.sort(MapObjects);								// Sorting The Array.

			for(int ObjectNumber=0;ObjectNumber<ObjectsOutOfBounds.size();ObjectNumber++)	// Removing All The Objects That Are Out Of Bounds.
			{
				Grid.getChildren().remove(ObjectsOutOfBounds.get(ObjectNumber));
			}

			for(int ObjectNumber=0;ObjectNumber<MapObjects.size();ObjectNumber++)			// Adding All The Objects That Are In The New Given Area.
			{
				ImageView ObjectOnMap = (ImageView) Grid.lookup("#"+MapObjects.get(ObjectNumber));
				ObjectOnMap.setId(String.valueOf((Integer.parseInt(ObjectOnMap.getId())/(int)(Grid.getWidth()/BlockSize))*MoveBy+Integer.parseInt(ObjectOnMap.getId())));
			}
		}

		GridSize.set(0,String.valueOf(Integer.parseInt(TextField_NewWidth.getText())*BlockSize));	// Saving The New Grid Width.
		GridSize.set(1,String.valueOf(Integer.parseInt(TextField_NewHeight.getText())*BlockSize));	// Saving The New Grid Height.

		Grid.setPrefSize(NewWidth*BlockSize,NewHeight*BlockSize);					// Re-Scaling The Grid.
		ImageView MenuBackGround = (ImageView) Scene.lookup("#MenuBackGround");		// Loading The Menu Background. (Acting As The Border Of The Grid)

		if(NewWidth*BlockSize<MenuBackGround.getLayoutX())							// Centering The Grid To The Middle. (If Smaller Then The Given Frame)
			Grid.setLayoutX(MenuBackGround.getLayoutX()/2-(NewWidth*BlockSize)/2);
		if(NewHeight*BlockSize<Scene.getHeight())
			Grid.setLayoutY(Scene.getHeight()/2-(NewHeight*BlockSize)/2);
		}
		else
			System.out.println("Wrong Input");
	}

	public void LoadMap(Scene Scene) // Loading The Requested Map ----------->> Need To Fix BeFor A Release (Change The Path)
	{
		String Path = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString(); // Getting The Path From Where The Program Was Launched.
		Path = Path.replaceAll("\\\\","\\\\\\\\");							// Changing The Path To The Proper Way.

		FileChooser FileChooser = new FileChooser();						// Opening A File Browser In The Program Luncher Location.
		FileChooser.getExtensionFilters().add(new ExtensionFilter("Text Files","*.txt"));
		FileChooser.setInitialDirectory(new File("D:\\Eclipse\\Projects\\MapMaker"));
		File SelectedFile = FileChooser.showOpenDialog(null);

		if (SelectedFile!=null)												// Checking If A File Was Chosen.
		{
			ResetGrid(Scene);												// Reseting The Grid.
			Pane Grid = (Pane) Scene.lookup("#Grid");						// Loading The Grid.
			String MapData = FileReader(SelectedFile.getAbsolutePath());	// Getting The Chosen File Contacts.

			ArrayList<String> UserSettings = Options.get(0);				// Loading The User Settings From Memory. (Using The User Settings As Place Holder)
			UserSettings.set(0,"");											// From GridWidth -> BlocksPerLine.
			UserSettings.set(1,"");											// From GridHeight -> BlocksPerColumns.

			while (MapData.charAt(0)!=',')									// Getting The Chosen Map Width.
			{
				UserSettings.set(0,UserSettings.get(0)+MapData.substring(0,1));
				MapData=MapData.substring(1,MapData.length());
			}

			MapData=MapData.substring(1,MapData.length());

			while (MapData.charAt(0)!=',')									// Getting The Chosen Map Height.
			{
				UserSettings.set(1,UserSettings.get(1)+MapData.substring(0,1));
				MapData=MapData.substring(1,MapData.length());
			}

			MapData=MapData.substring(1,MapData.length());
			Grid.setPrefWidth(Integer.parseInt(UserSettings.get(0))*BlockSize);  // Changing The Grid Width To The Saved Map Width.
			Grid.setPrefHeight(Integer.parseInt(UserSettings.get(1))*BlockSize); // Changing The Grid Height To The Saved Map Height.

			while (MapData.length()!=0)										// Loading All The Map Objects. (Creating The Map)
			{
				UserSettings.set(2,"");										// From BlockSize -> ObjectLocation.
				UserSettings.set(3,"");										// From SpriteLocation -> SpriteLocation.

				while (MapData.charAt(0)!=',')								// Getting The Object Location On The Map.
				{
					UserSettings.set(2,UserSettings.get(2)+MapData.substring(0,1));
					MapData=MapData.substring(1,MapData.length());
				}

				MapData=MapData.substring(1,MapData.length());

				while (MapData.charAt(0)!=',')								// Getting The Sprite Location On The Sprite Sheet.
				{
					UserSettings.set(3,UserSettings.get(3)+MapData.substring(0,1));
					MapData=MapData.substring(1,MapData.length());
				}

				MapData=MapData.substring(1,MapData.length());

				int ImageXLocation=Integer.valueOf(UserSettings.get(2))%(int)(Integer.parseInt(UserSettings.get(0)));
				int ImageYLocation=Integer.valueOf(UserSettings.get(2))/(int)(Integer.parseInt(UserSettings.get(0)));
				int SpriteX=Integer.valueOf(UserSettings.get(3))%4;
				int SpriteY=Integer.valueOf(UserSettings.get(3))/4;

				String ImageID=String.valueOf((int)ImageXLocation+(int)ImageYLocation*(int)(Integer.parseInt(UserSettings.get(0))));

				CreateImageView(BlockSize*ImageXLocation,BlockSize*ImageYLocation,BlockSize,BlockSize,"file:src/ResourcePack/Sprite.png",ImageID,true,SpriteX*64+1,SpriteY*64+1,63,63,"Grid",Scene);
			}
			ImageView MenuBackGround = (ImageView) Scene.lookup("#MenuBackGround"); 				// Loading The MenuBackGround. 
			if((Integer.parseInt(UserSettings.get(0))*BlockSize)<MenuBackGround.getLayoutX())		// Checking If The Gird Width Is Smaller Then The Given Place For It.
				Grid.setLayoutX(MenuBackGround.getLayoutX()/2-(Integer.parseInt(UserSettings.get(0))*BlockSize)/2);
			else
				Grid.setLayoutX(0);
			if((Integer.parseInt(UserSettings.get(1))*BlockSize)<Scene.getHeight())					// Checking If The Gird Height Is Smaller Then The Given Place For It.
				Grid.setLayoutY(Scene.getHeight()/2-(Integer.parseInt(UserSettings.get(1))*BlockSize)/2);
			else
				Grid.setLayoutY(0);
			// Restoring The User Setting To What It Should Be.
			UserSettings.set(0,String.valueOf(Integer.parseInt(UserSettings.get(0))*BlockSize));	// From BlocksPerLine -> GridWidth.
			UserSettings.set(1,String.valueOf(Integer.parseInt(UserSettings.get(1))*BlockSize));	// From BlocksPerColumns -> GridHeight.
			UserSettings.set(2,"ScreenHeight*0.05");												// From ObjectLocation -> BlockSize.
			UserSettings.set(3,"0");																// From SpriteLocation -> SpriteLocation.

			Grid.toBack();																			// Pushing The Grid To The Back Of The Screen.
		}
	}

	public String FileReader(String Path) // Gets The Contents Of A Text File.
	{
		try						// Tries To Read The Given File And String That Value To A String Object.
		{
			byte[] Encoded = Files.readAllBytes(Paths.get(Path));
			String FullString=new String(Encoded,StandardCharsets.UTF_8);
			return FullString;
		}
		catch(IOException error)
		{
			System.out.println("Cant Write To File");
		}
		return null;
	}

	public void Save(Scene Scene) // Creates A File With The Given Grid (Makes A Map).
	{
		Pane Grid = (Pane) Scene.lookup("#Grid");		// Loading The Grid.
		if (Grid.getChildren().size()!=0)				// Checks If The Grid Is Empty.
		{
			String MapData=(int)(Grid.getWidth()/BlockSize)+","+(int)(Grid.getHeight()/BlockSize)+",";	// Part 1 Of The Function:
			int MapObjects[] = new int[Grid.getChildren().size()];							// Assembles An Array With All The Objects ID's In It, And Then Sorts In The Right Order (Smallest To The Biggest).

			for(int ObjectNumber=0;ObjectNumber<Grid.getChildren().size();ObjectNumber++)
			{
				ImageView ObjectOnMap = (ImageView) Grid.getChildren().get(ObjectNumber);
				MapObjects[ObjectNumber] = Integer.parseInt(ObjectOnMap.getId());
			}

			Arrays.sort(MapObjects);
			// Part 2 Of The Function:
			for(int ObjectNumber=0;ObjectNumber<MapObjects.length;ObjectNumber++)	// Creates An String With The Given Objects ID's.
			{
				ImageView ObjectOnMap = (ImageView) Grid.lookup("#"+MapObjects[ObjectNumber]);
				MapData=MapData+MapObjects[ObjectNumber]+","+String.valueOf((int)(ObjectOnMap.getViewport().getMinX())/64+(int)(ObjectOnMap.getViewport().getMinY()/64)*4)+",";
			}
			// Part 3 Of The Function:
			WriteToFile("Map.txt",MapData,true);									// Creates A Text File With The Structure Of The Map (GridWidth,GridHeight,ObjectID,SpriteLocation).
		}
		else
		{
			System.out.println("Cannot Detect Any Objects");
		}
	}

	public void WriteToFile(String FileName,String Text,boolean ResetFile) // Writes To File / Make New File If Requested (Depends On What You Put In ResetFile).
	{
		try									// Trying To Processing The Request.
		{
			if (ResetFile)					// ResetFile Swaps The Request That Was Sent To Function Properly (Reset The File Then Writes When Set To False | Only Writes When Set To True).
				ResetFile=false;
			else
				ResetFile=true;

			File File = new File(FileName);	// Creates The File  With The Given Name.
			FileWriter FileWriter = new FileWriter(File,ResetFile);	
			PrintWriter PrintWriter = new PrintWriter(FileWriter);	

			PrintWriter.print(Text);		// Writes The Give Text To The Text File.
			PrintWriter.close();			// Must Close The File In Order It To Register In The System.

			System.out.println("Done");
		}
		catch(IOException error) 			// In Case Of An Error While Processing The Request.
		{
			System.out.println("Cant Write To File");
		}
	}

	public void ResetGrid(Scene Scene) // Resets The Grid That Displays All The Map Objects -> (Resets The Grid By Deleting It And Creating A New One In It's Place).
	{
		ImageView MenuBackGround = (ImageView) Scene.lookup("#MenuBackGround"); // Loading The MenuBackGround. 
		Pane MainLayout = (Pane) Scene.lookup("#MainLayout");				// Loading The Main Layout. 
		Pane Grid = (Pane) MainLayout.lookup("#Grid");						// Loading The Gird (Map).

		// Saves Grid's Properties (Width And Height).

		double Width = Grid.getWidth();
		double Height = Grid.getHeight();

		MainLayout.getChildren().remove(Grid);								// Removing / Deleting The Grid From The Main Layout.

		CreatePane(0,0,Width,Height,"DAE6F3","Grid","MainLayout",Scene); 	// Creating The New Grid.

		Grid = (Pane) Scene.lookup("#Grid");								// Loading The New Gird (Map).

		if(Width<MenuBackGround.getLayoutX())			// Checking If The Gird Width Is Smaller Then The Given Place For It.
			Grid.setLayoutX(MenuBackGround.getLayoutX()/2-Width/2);
		else
			Grid.setLayoutX(0);
		if(Height<Scene.getHeight())					// Checking If The Gird Height Is Smaller Then The Given Place For It.
			Grid.setLayoutY(Scene.getHeight()/2-Height/2);
		else
			Grid.setLayoutY(0);
		Grid.toBack();														// Placing The New Grid Blow Everything.
	}

	public void OnScroll(ScrollEvent OnScroll,Scene Scene)	// When Ever The User Scroll With The Mouse This Function Will Be Activated.
	{
		Pane Menu = (Pane) Scene.lookup("#Menu");									// Loading The Menu.
		Pane Grid = (Pane) Scene.lookup("#Grid");
		ImageView Sprite = (ImageView) Menu.lookup("#Sprite");						// Loading The Sprite.
		ImageView MenuControlPanel = (ImageView) Menu.lookup("#MenuControlPanel");	// Loading The MenuControlPanel.
		ArrayList<String> GridSize = Options.get(0);

		if (OnScroll.getSceneX()>=Sprite.getLayoutX() && OnScroll.getSceneX()<=Sprite.getLayoutX()+Sprite.getFitWidth() && OnScroll.getSceneY()>MenuControlPanel.getFitHeight()) // Checking Whether The User Is Standing On The Frame Of The Sprite.
		{
			if(OnScroll.getDeltaY()<0)		// Checking The User Scroll Direction.
			{
				if(Sprite.getLayoutY()+Sprite.getFitHeight()+OnScroll.getDeltaY()>Scene.getHeight()) // Making Sure The Sprite Doesn't Get Out Of Bounds.
				{
					Sprite.setLayoutY(Sprite.getLayoutY()+OnScroll.getDeltaY());	// Moving The Sprite By The Given Scroll Amount. 
				}
				else
				{
					Sprite.setLayoutY(Scene.getHeight()-Sprite.getFitHeight());		// Setting The Sprite In The Last Position.
				}
			}
			else
			{
				if(Sprite.getLayoutY()<MenuControlPanel.getFitHeight()) // Making Sure The Sprite Doesn't Get Out Of Bounds.
				{
					Sprite.setLayoutY(Sprite.getLayoutY()+OnScroll.getDeltaY());	// Moving The Sprite By The Given Scroll Amount. 
				}
				else
				{
					Sprite.setLayoutY(MenuControlPanel.getFitHeight());							// Setting The Sprite In The Last Position.
				}
			}
		}
		else if (OnScroll.getSceneX()<MenuControlPanel.getLayoutX()) // Checking Whether The User Is Standing On The Given Grid Space.
		{
			int BlockPerLine=(int)(Double.parseDouble(GridSize.get(0))/BlockSize);
			int BlocksPerColumn=(int)(Double.parseDouble(GridSize.get(1))/BlockSize);

			if(OnScroll.getDeltaY()<0)		// Checking The User Scroll Direction.
			{
				if(BlockSize!=ScreenHeight*0.01)
				{
					BlockSize=BlockSize-ScreenHeight*0.01;
					Grid.setPrefSize(BlockPerLine*BlockSize,BlocksPerColumn*BlockSize);
					GridSize.set(0,String.valueOf(BlockPerLine*BlockSize));
					GridSize.set(1,String.valueOf(BlocksPerColumn*BlockSize));

					if (BlockPerLine*BlockSize<=MenuControlPanel.getLayoutX())
						Grid.setLayoutX((MenuControlPanel.getLayoutX()/2)-(BlockPerLine*BlockSize)/2);
					else
					{
						if(Grid.getLayoutX()>0)
							Grid.setLayoutX(0);
						else if(Grid.getLayoutX()+BlockPerLine*BlockSize<MenuControlPanel.getLayoutX())
							Grid.setLayoutX(MenuControlPanel.getLayoutX()-BlockPerLine*BlockSize);
					}
					if (BlocksPerColumn*BlockSize<=Scene.getHeight())
						Grid.setLayoutY((Scene.getHeight()/2)-(BlocksPerColumn*BlockSize)/2);
					else
					{
						if(Grid.getLayoutY()>0)
							Grid.setLayoutY(0);
						else if(Grid.getLayoutY()+BlocksPerColumn*BlockSize<Scene.getHeight())
							Grid.setLayoutY(Scene.getHeight()-BlocksPerColumn*BlockSize);
					}

					if(Grid.getChildren().size()!=0)
					{
						for(int ObjectNumber=0;ObjectNumber<Grid.getChildren().size();ObjectNumber++)
						{
							ImageView ObjectOnMap = (ImageView) Grid.getChildren().get(ObjectNumber);
							ObjectOnMap.setFitWidth(BlockSize);
							ObjectOnMap.setFitHeight(BlockSize);
							ObjectOnMap.setLayoutX((Integer.parseInt(ObjectOnMap.getId())%BlockPerLine)*BlockSize);
							ObjectOnMap.setLayoutY((Integer.parseInt(ObjectOnMap.getId())/BlockPerLine)*BlockSize);
						}
					}
				}
			}
			else
			{
				if(BlockSize!=ScreenHeight*0.1)
				{
					BlockSize=BlockSize+ScreenHeight*0.01;
					Grid.setPrefSize(BlockPerLine*BlockSize,BlocksPerColumn*BlockSize);
					GridSize.set(0,String.valueOf(BlockPerLine*BlockSize));
					GridSize.set(1,String.valueOf(BlocksPerColumn*BlockSize));

					if (BlockPerLine*BlockSize<MenuControlPanel.getLayoutX())
						Grid.setLayoutX((MenuControlPanel.getLayoutX()/2)-(BlockPerLine*BlockSize)/2);
					else
					{
						if(Grid.getLayoutX()>0)
							Grid.setLayoutX(0);
						else if(Grid.getLayoutX()+BlockPerLine*BlockSize<MenuControlPanel.getLayoutX())
							Grid.setLayoutX(MenuControlPanel.getLayoutX()-BlockPerLine*BlockSize);
					}
					if (BlocksPerColumn*BlockSize<Scene.getHeight())
						Grid.setLayoutY((Scene.getHeight()/2)-(BlocksPerColumn*BlockSize)/2);
					else
					{
						if(Grid.getLayoutY()>0)
							Grid.setLayoutY(0);
						else if(Grid.getLayoutY()+BlocksPerColumn*BlockSize<Scene.getHeight())
							Grid.setLayoutX(Scene.getHeight()-BlocksPerColumn*BlockSize);
					}

					if(Grid.getChildren().size()!=0)
					{
						for(int ObjectNumber=0;ObjectNumber<Grid.getChildren().size();ObjectNumber++)
						{
							ImageView ObjectOnMap = (ImageView) Grid.getChildren().get(ObjectNumber);
							ObjectOnMap.setFitWidth(BlockSize);
							ObjectOnMap.setFitHeight(BlockSize);
							ObjectOnMap.setLayoutX((Integer.parseInt(ObjectOnMap.getId())%BlockPerLine)*BlockSize);
							ObjectOnMap.setLayoutY((Integer.parseInt(ObjectOnMap.getId())/BlockPerLine)*BlockSize);
						}
					}
				}
			}
		}
	}

	public void KeyPressed(KeyEvent KeyPressed,Scene Scene)
	{
		Pane Menu = (Pane) Scene.lookup("#Menu");
		Pane HiddenOptions = (Pane) Menu.lookup("#HiddenOptions");

		if (KeyPressed.getCode() == KeyCode.V)
		{
			ArrayList<String> HandMode = Options.get(2);

			if(HandMode.get(0).equals("False"))
				HandMode.set(0,"True");
			else
				HandMode.set(0,"False");

			if(HiddenOptions!=null)
			{
				Label HandModeLabel = (Label) Menu.lookup("#HandMode");
				HandModeLabel.setText("HandMode : " + HandMode.get(0));
			}
		}
		else if (KeyPressed.getCode() == KeyCode.B)
		{
			ArrayList<String> BlockPicker = Options.get(3);

			if(BlockPicker.get(0).equals("False"))
				BlockPicker.set(0,"True");
			else
				BlockPicker.set(0,"False");

			if(HiddenOptions!=null)
			{
				Label BlockPickerLabel = (Label) Menu.lookup("#BlockPicker");
				BlockPickerLabel.setText("BlockPicker : " + BlockPicker.get(0));
			}
		}
		else if (KeyPressed.getCode() == KeyCode.S)
		{
			ArrayList<String> BlockPicker = Options.get(4);

			if(BlockPicker.get(0).equals("False"))
				BlockPicker.set(0,"True");
			else
				BlockPicker.set(0,"False");

			if(HiddenOptions!=null)
			{
				Label SelectModeLabel = (Label) Menu.lookup("#SelectMode");
				SelectModeLabel.setText("SelectMode : " + BlockPicker.set(0,"False"));
			}
		}
		else if (KeyPressed.getCode() == KeyCode.O)
		{	
			Label MoreOptionsLabel = (Label) Menu.lookup("#MoreOptions");

			ArrayList<String> MoreOptions = Options.get(1);
			MoreOptionsLabel.setText("HiddenOptions : " + MoreOptions.get(0));

			if(MoreOptions.get(0).equals("False"))
			{
				MoreOptions.set(0,"True");
				CreatePane(ScreenHeight*0.2,0,ScreenHeight*0.2,ScreenHeight*0.05,"FFFFFF","HiddenOptions","Menu", Scene);

				ArrayList<String> HandMode = Options.get(2);
				CreateLabel(ScreenHeight*0.005,ScreenHeight*0.01,0,0,"HandMode : " + HandMode.get(0),"HandMode","HiddenOptions",Scene);

				ArrayList<String> BlockPicker = Options.get(3);
				CreateLabel(ScreenHeight*0.005,ScreenHeight*0.027,0,0,"BlockPicker : " + BlockPicker.get(0),"BlockPicker","HiddenOptions",Scene);
			}
			else 
			{
				MoreOptions.set(0,"False");
				Menu.getChildren().remove(HiddenOptions);
			}
		}
	}

	public void MouseMoved(MouseEvent Mouse,Scene Scene)
	{
		Pane Menu = (Pane) Scene.lookup("#Menu");

		Label Coordinates = (Label) Menu.lookup("#Coordinates");
		Coordinates.setText("X Coordinates : "+Mouse.getSceneX()+"\nY Coordinates : "+Mouse.getSceneY());
	}

	public void MousePressed(MouseEvent Mouse,Scene Scene)
	{
		Pane Menu = (Pane) Scene.lookup("#Menu");
		Pane Grid = (Pane) Scene.lookup("#Grid");

		ImageView Sprite = (ImageView) Menu.lookup("#Sprite");
		ArrayList<String> HandMode = Options.get(2);
		ArrayList<String> BlockPicker = Options.get(3);

		if (!HandMode.get(0).equals("True") && !BlockPicker.get(0).equals("True"))
		{
			if(Mouse.getSceneX()>Grid.getLayoutX() && Mouse.getSceneX()<(Grid.getWidth()+Grid.getLayoutX()) && Mouse.getSceneY()>Grid.getLayoutY() && Mouse.getSceneY()<(Grid.getHeight()+Grid.getLayoutY()) && Mouse.getSceneX()<Sprite.getLayoutX()  && Mouse.getButton().equals(MouseButton.PRIMARY))
			{
				double ImageXLocation=(Mouse.getSceneX()-Grid.getLayoutX())/BlockSize;
				double ImageYLocation=(Mouse.getSceneY()-Grid.getLayoutY())/BlockSize;

				String ImageID=String.valueOf((int)ImageXLocation+(int)ImageYLocation*(int)(Grid.getWidth()/BlockSize));

				int SpriteX=SpriteLocation%4;
				int SpriteY=SpriteLocation/4;

				CreateImageView(BlockSize*(int)(ImageXLocation),BlockSize*(int)(ImageYLocation),BlockSize,BlockSize,"file:src/ResourcePack/Sprite.png",ImageID,true,SpriteX*64+1,SpriteY*64+1,63,63,"Grid",Scene);

				if(SpriteLocation==0 || SpriteLocation==1 || SpriteLocation==2 || SpriteLocation==3)
				{	
					CreatePerfectPlatforms(ImageID,Scene);
				}
			}
			else if (Mouse.getSceneX()>Grid.getLayoutX() && Mouse.getSceneX()<(Grid.getWidth()+Grid.getLayoutX()) && Mouse.getSceneY()>Grid.getLayoutY() && Mouse.getSceneY()<(Grid.getHeight()+Grid.getLayoutY()) && Mouse.getButton().equals(MouseButton.SECONDARY))
			{			
				String ImageID=String.valueOf(Math.round(Mouse.getSceneX()-Grid.getLayoutX())/Math.round(BlockSize)+(Math.round(Mouse.getSceneY()-Grid.getLayoutY())/Math.round(BlockSize))*(int)(Grid.getWidth()/BlockSize));
				ImageView ImageToBeRemoved = (ImageView) Grid.lookup("#"+ImageID);
				Grid.getChildren().remove(ImageToBeRemoved);
			}
			else if (Mouse.getSceneX()>=Sprite.getLayoutX() && Mouse.getSceneX()<=Sprite.getLayoutX()+Sprite.getFitWidth() && Mouse.getSceneY()>=Sprite.getLayoutY() && Mouse.getSceneY()<=Sprite.getLayoutY()+Sprite.getFitHeight() && Mouse.getSceneY()>ScreenHeight*0.1)
			{
				double SpriteXLocation=(Mouse.getSceneX()-Sprite.getLayoutX())/(ScreenHeight*0.05);
				double SpriteYLocation=(Mouse.getSceneY()-Sprite.getLayoutY())/(ScreenHeight*0.05);

				SpriteLocation=(int)SpriteXLocation+(int)SpriteYLocation*4;
			}
		}
		else if (HandMode.get(0).equals("True"))
		{
			if (Mouse.getSceneX()>=Sprite.getLayoutX() && Mouse.getSceneX()<=Sprite.getLayoutX()+Sprite.getFitWidth() && Mouse.getSceneY()>=Sprite.getLayoutY() && Mouse.getSceneY()<=Sprite.getLayoutY()+Sprite.getFitHeight())
			{
				HandMode.set(1,"Sprite");
				HandMode.set(2,String.valueOf(Mouse.getSceneY()));
				HandMode.set(3,String.valueOf(Mouse.getSceneX()));	
			}
			else if(Mouse.getSceneX()>=Grid.getLayoutX() && Mouse.getSceneX()<=Grid.getLayoutX()+Grid.getWidth() && Mouse.getSceneY()>=Grid.getLayoutY() && Mouse.getSceneY()<=Grid.getLayoutY()+Grid.getHeight())
			{
				HandMode.set(1,"Grid");
				HandMode.set(2,String.valueOf(Mouse.getSceneY()));
				HandMode.set(3,String.valueOf(Mouse.getSceneX()));
			}
		}
		else if (BlockPicker.get(0).equals("True"))
		{
			if(Mouse.getSceneX()>=Grid.getLayoutX() && Mouse.getSceneX()<=Grid.getLayoutX()+Grid.getWidth() && Mouse.getSceneY()>=Grid.getLayoutY() && Mouse.getSceneY()<=Grid.getLayoutY()+Grid.getHeight())
			{
				double ImageXLocation=(Mouse.getSceneX()-Grid.getLayoutX())/BlockSize;
				double ImageYLocation=(Mouse.getSceneY()-Grid.getLayoutY())/BlockSize;
				String ImageID=String.valueOf((int)ImageXLocation+(int)ImageYLocation*(int)(Grid.getWidth()/BlockSize));
				ImageView ExistenceCheck = (ImageView) Grid.lookup("#"+ImageID);

				if (ExistenceCheck!=null)
				{
					int PickerSpriteLocation=(int) (ExistenceCheck.getViewport().getMinX()/64+(ExistenceCheck.getViewport().getMinY()/64)*4);
					SpriteLocation=PickerSpriteLocation;
					BlockPicker.set(0,"False");

					Pane HiddenOptions = (Pane) Scene.lookup("#HiddenOptions");
					if(HiddenOptions!=null)
					{
						Label BlockPickerLabel = (Label) Scene.lookup("#BlockPicker");
						BlockPickerLabel.setText("BlockPicker : False");
					}
				}
			}
		}
	}

	public void CreatePerfectPlatforms(String ImageID,Scene Scene)
	{
		Pane Grid = (Pane) Scene.lookup("#Grid");

		ImageView ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
		int ObjectSpriteLocation=1;

		while(ObjectOnMap!=null)
		{
			ObjectSpriteLocation=(int) (ObjectOnMap.getViewport().getMinX()/64+(ObjectOnMap.getViewport().getMinY()/64)*4+1);
			if (ObjectSpriteLocation>=1 && ObjectSpriteLocation<=4 && Integer.parseInt(ObjectOnMap.getId())%(Grid.getWidth()/BlockSize)!=0)
			{
				ImageID=String.valueOf(Integer.valueOf(ImageID)-1);
				ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
			}
			else
			{
				break;
			}
		}

		if(Integer.parseInt(ImageID)%(Grid.getWidth()/BlockSize)==0 && ObjectOnMap!=null)
		{
			ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
			ObjectOnMap.setViewport(new Rectangle2D(1,1,63,63));
		}
		else
		{
			ImageID=String.valueOf(Integer.valueOf(ImageID)+1);
			ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
			ObjectOnMap.setViewport(new Rectangle2D(1,1,63,63));
		}

		ImageID=String.valueOf(Integer.valueOf(ImageID)+1);
		ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);

		while(ObjectOnMap!=null)
		{		
			ObjectSpriteLocation=(int) (ObjectOnMap.getViewport().getMinX()/64+(ObjectOnMap.getViewport().getMinY()/64)*4+1);
			if (ObjectSpriteLocation>=1 && ObjectSpriteLocation<=4 && Integer.parseInt(ObjectOnMap.getId())%(Grid.getWidth()/BlockSize)!=(Grid.getWidth()/BlockSize)-1)
			{
				ObjectOnMap.setViewport(new Rectangle2D(129,1,63,63));
				ImageID=String.valueOf(Integer.valueOf(ImageID)+1);
				ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
			}
			else
			{
				break;
			}
		}

		if(Integer.parseInt(ImageID)%(Grid.getWidth()/BlockSize)==(Grid.getWidth()/BlockSize)-1  && ObjectOnMap!=null)
		{
			ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
			ObjectOnMap.setViewport(new Rectangle2D(193,1,63,63));
		}
		else
		{
			ImageID=String.valueOf(Integer.valueOf(ImageID)-1);
			ObjectOnMap = (ImageView) Grid.lookup("#"+ImageID);
			ObjectOnMap.setViewport(new Rectangle2D(193,1,63,63));
		}

	}

	public void MouseDragged(MouseEvent Mouse,Scene Scene)
	{	
		Pane Grid = (Pane) Scene.lookup("#Grid");
		ArrayList<String> HandMode = Options.get(2);

		if(!HandMode.get(0).equals("True"))	// HandMode -> Is Activated When The Button 'V' Is Pressed On The Keyboard.
		{
			if(Mouse.getSceneX()>Grid.getLayoutX() && Mouse.getSceneX()<(Grid.getWidth()+Grid.getLayoutX()) && Mouse.getSceneY()>Grid.getLayoutY() && Mouse.getSceneY()<(Grid.getHeight()+Grid.getLayoutY()) && Mouse.getSceneX()<ScreenWidth*0.5-ScreenHeight*0.2 && Mouse.getButton().equals(MouseButton.PRIMARY))
			{
				double ImageXLocation=(Mouse.getSceneX()-Grid.getLayoutX())/BlockSize;		// Gets 
				double ImageYLocation=(Mouse.getSceneY()-Grid.getLayoutY())/BlockSize;

				String ImageID=String.valueOf((int)ImageXLocation+(int)ImageYLocation*(int)(Grid.getWidth()/BlockSize));

				int SpriteX=SpriteLocation%4;
				int SpriteY=SpriteLocation/4;

				CreateImageView(BlockSize*(int)(ImageXLocation),BlockSize*(int)(ImageYLocation),BlockSize,BlockSize,"file:src/ResourcePack/Sprite.png",ImageID,true,SpriteX*64+1,SpriteY*64+1,63,63,"Grid",Scene);	

				if(SpriteLocation>=0 && SpriteLocation<=3)
				{	
					CreatePerfectPlatforms(ImageID,Scene);
				}
			}
			else if (Mouse.getSceneX()>Grid.getLayoutX() && Mouse.getSceneX()<(Grid.getWidth()+Grid.getLayoutX()) && Mouse.getSceneY()>Grid.getLayoutY() && Mouse.getSceneY()<(Grid.getHeight()+Grid.getLayoutY()) && Mouse.getButton().equals(MouseButton.SECONDARY) && Mouse.getSceneX()<ScreenWidth*0.5-ScreenHeight*0.2)
			{
				String ImageID=String.valueOf(Math.round(Mouse.getSceneX()-Grid.getLayoutX())/Math.round(BlockSize)+(Math.round(Mouse.getSceneY()-Grid.getLayoutY())/Math.round(BlockSize))*(int)(Grid.getWidth()/BlockSize));
				ImageView ImageToBeRemoved = (ImageView) Grid.lookup("#"+ImageID);
				Grid.getChildren().remove(ImageToBeRemoved);
			}
		}
		else
		{
			Pane Menu = (Pane) Scene.lookup("#Menu");
			ImageView Sprite = (ImageView) Menu.lookup("#Sprite");

			if(HandMode.get(1).equals("Grid"))
			{
				if(Grid.getWidth()>Sprite.getLayoutX())
				{
					if(Mouse.getSceneX()-Double.parseDouble(HandMode.get(3))<0)
					{
						if(Grid.getLayoutX()+Grid.getWidth()+(Mouse.getSceneX()-Double.parseDouble(HandMode.get(3)))>ScreenWidth*0.5-ScreenHeight*0.2)
						{
							Grid.setLayoutX(Grid.getLayoutX()+(Mouse.getSceneX()-Double.parseDouble(HandMode.get(3))));
						}
						else
						{
							Grid.setLayoutX(Scene.getWidth()-ScreenHeight*0.2-Grid.getWidth());
						}
					}
					else
					{
						if(Grid.getLayoutX()+(Mouse.getSceneX()-Double.parseDouble(HandMode.get(3)))<0)
						{
							Grid.setLayoutX(Grid.getLayoutX()+(Mouse.getSceneX()-Double.parseDouble(HandMode.get(3))));
						}
						else
						{
							Grid.setLayoutX(0);
						}
					}
					HandMode.set(3,String.valueOf(Mouse.getSceneX()));
				}
				if(Grid.getHeight()>Scene.getHeight())
				{
					if(Mouse.getSceneY()-Double.parseDouble(HandMode.get(2))<0)
					{
						if(Grid.getLayoutY()+Grid.getHeight()+Mouse.getSceneY()-Double.parseDouble(HandMode.get(2))>ScreenHeight*0.5)
						{
							Grid.setLayoutY(Grid.getLayoutY()+Mouse.getSceneY()-Double.parseDouble(HandMode.get(2)));
						}
						else
						{
							Grid.setLayoutY(Scene.getHeight()-Grid.getHeight());
						}
					}
					else
					{
						if(Grid.getLayoutY()+(Mouse.getSceneY()-Double.parseDouble(HandMode.get(2)))<0)
						{
							Grid.setLayoutY(Grid.getLayoutY()+Mouse.getSceneY()-Double.parseDouble(HandMode.get(2)));
						}
						else
						{
							Grid.setLayoutY(0);
						}
					}
					HandMode.set(2,String.valueOf(Mouse.getSceneY()));
				}
			}
			else if (HandMode.get(1).equals("Sprite"))
			{
				if(Mouse.getSceneY()-Double.parseDouble(HandMode.get(2))<0)
				{
					if(Sprite.getLayoutY()+Sprite.getFitHeight()+Mouse.getSceneY()-Double.parseDouble(HandMode.get(2))>ScreenHeight*0.5)
					{
						Sprite.setLayoutY(Sprite.getLayoutY()+Mouse.getSceneY()-Double.parseDouble(HandMode.get(2)));
					}
					else
					{
						Sprite.setLayoutY(Scene.getHeight()-Sprite.getFitHeight());
					}
				}
				else
				{
					if(Sprite.getLayoutY()<ScreenHeight*0.1)
					{
						Sprite.setLayoutY(Sprite.getLayoutY()+Mouse.getSceneY()-Double.parseDouble(HandMode.get(2)));
					}
					else
					{
						Sprite.setLayoutY(ScreenHeight*0.1);
					}
				}
				HandMode.set(2,String.valueOf(Mouse.getSceneY()));
			}
		}
	}

	// Builders :
	
	public void CreatePane(double X,double Y,double Width,double Height,String ColorID,String ID,String AddTo,Scene Scene) // Creates A Layout With The Given Request.
	{
		Pane Pane = new Pane();									// Creates A New Layout.
		Pane.setPrefSize(Width,Height);							// Setting The Size Of The Layout.
		Pane.setStyle("-fx-background-color: "+ColorID+";");	// Setting The Color Of The Layout.
		Pane.setId(ID);											// Setting The ID Of The Layout.
		Pane.setLayoutX(X);										// Setting The X And Y Coordinates Of The Layout.
		Pane.setLayoutY(Y);

		Pane MainLayout = (Pane) Scene.lookup("#"+AddTo);		// Creating The Requested Layout.
		MainLayout.getChildren().addAll(Pane);					// Adding The TextField To The Requested Layout.
	}

	public void CreateImageView(double X,double Y,double Width,double Height,String ImageLocation,String ID,boolean UseSprite,int SpriteX,int SpriteY,int SpriteWidth,int SpriteHeight,String AddTo,Scene Scene) // Creates A ImageView With The Given Request.
	{
		Pane MainLayout = (Pane) Scene.lookup("#"+AddTo);	// Creating The Requested Layout.

		ImageView ExistenceCheck = (ImageView) MainLayout.lookup("#"+ID);

		if(ExistenceCheck == null)
		{
			ImageView Image = new ImageView();				// Creating A New ImageView.
			Image ImageSrc = new Image(ImageLocation);		// Creating A New Image.
			Image.setImage(ImageSrc);						// Setting The Image Of The ImageView.
			Image.setFitWidth(Width);						// Setting The Width And The Height Of The ImageView.
			Image.setFitHeight(Height);
			Image.setLayoutX(X);							// Setting The X And Y Coordinates Of The ImageView.
			Image.setLayoutY(Y);
			Image.setId(ID);								// Setting The ID Of The ImageView.
			//Image.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,1),0,0,0,0);");
			Image.setCache(true);
			Image.setCacheHint(CacheHint.SPEED);
			if (UseSprite)									// Checking Within The Request For A Need To Use Sprite.
				Image.setViewport(new Rectangle2D(SpriteX,SpriteY,SpriteWidth,SpriteHeight));

			MainLayout.getChildren().add(Image);			// Adding The ImageView To The Requested Layout.
		}
		else
		{
			System.out.println("ID Already In Use : "+ID+"\nCheck For Weird Stuff Or Other Errors \nChanging Frame And Image ");

			Image ImageSrc = new Image(ImageLocation);		// Creating A New Image.
			ExistenceCheck.setImage(ImageSrc);				// Setting The Image Of The ImageView.
			ExistenceCheck.setFitWidth(Width);				// Setting The Width And The Height Of The ImageView.
			ExistenceCheck.setFitHeight(Height);
			ExistenceCheck.setLayoutX(X);					// Setting The X And Y Coordinates Of The ImageView.
			ExistenceCheck.setLayoutY(Y);
			ExistenceCheck.setCache(true);
			ExistenceCheck.setCacheHint(CacheHint.SPEED);

			if (UseSprite)									// Checking Within The Request For A Need To Use Sprite.
				ExistenceCheck.setViewport(new Rectangle2D(SpriteX,SpriteY,SpriteWidth,SpriteHeight));
		}
	}

	public void CreateLabel(double X,double Y,double MaxWidth,double MaxHeight,String Text,String ID,String AddTo,Scene Scene) // Creates A Label With The Given Request.
	{
		Pane MainLayout = (Pane) Scene.lookup("#"+AddTo);	// Creating The Requested Layout.

		Label ExistenceCheck = (Label) MainLayout.lookup("#"+ID);

		if(ExistenceCheck == null)					// Checking If The Given ID Already In Use.
		{
			Label Label = new Label();				// Creates A New Label.
			if (MaxWidth!=0)						// Checking Within The Request For A Specified Width.
				Label.setMaxWidth(MaxWidth);
				
			if (MaxHeight!=0)						// Checking Within The Request For A Specified Height.
				Label.setMaxHeight(MaxHeight);
			
			Label.setId(ID);						// Setting The ID Of The Label.
			Label.setText(Text);					// Setting The Text Of The Label.
			Label.setLayoutX(X);					// Setting The X And Y Coordinates Of The Label.
			Label.setLayoutY(Y);	

			MainLayout.getChildren().add(Label);	// Adding The Label To The Requested Layout.
		}
		else
		{
			System.out.println("ID Already In Use : "+ID+"\nCheck For Weird Stuff Or Other Errors \nChanging Frame And Text");

			if (MaxWidth!=0)						// Checking Within The Request For A Specified Width.
				ExistenceCheck.setMaxWidth(MaxWidth);

			if (MaxHeight!=0)						// Checking Within The Request For A Specified Height.
				ExistenceCheck.setMaxHeight(MaxHeight);

			ExistenceCheck.setText(Text);			// Setting The Text Of The Label.
			ExistenceCheck.setLayoutX(X);			// Setting The X And Y Coordinates Of The Label.
			ExistenceCheck.setLayoutY(Y);
		}
	}

	public void CreateButton(double X,double Y,double Width,double Height,String Text,EventHandler<ActionEvent> Action,String ID,String AddTo,Scene Scene) // Creates A Button With The Given Request.
	{
		Button Button = new Button(Text);		// Creates A New Button And Gives It A Text To Display.
		if (Width!=0)							// Checking Within The Request For A Specified Width.
			Button.setPrefWidth(Width);
		
		if (Height!=0)							// Checking Within The Request For A Specified Height.
			Button.setPrefHeight(Height);
		
		Button.setId(ID);						// Setting The ID Of The Button.
		Button.setLayoutX(X);					// Setting The X And Y Coordinates Of The Button.
		Button.setLayoutY(Y);
		Button.setOnAction(Action);				// Setting An Action (Function) For The Button To Perform Upon Activation.

		Pane RequestedLayout = (Pane) Scene.lookup("#"+AddTo);	// Creating The Requested Layout.
		RequestedLayout.getChildren().addAll(Button);			// Adding The Button To The Requested Layout.
	}

	public void CreateTextField(double X,double Y,double Width,double Height,String HintText,String ID,String AddTo,Scene Scene) // Creates A TextField With The Given Request.
	{
		TextField TextField = new TextField();	// Creates A New TextField And Gives It A Text To Display.
		TextField.setPromptText(HintText);		// Setting The Hint Of The TextField.
		
		if (Width!=0)							// Checking Within The Request For A Specified Width.
			TextField.setPrefWidth(Width);
		else
			TextField.setPrefWidth(TextField.getPromptText().length()*(int)(TextField.getFont().getSize()));
		
		if (Height!=0)							// Checking Within The Request For A Specified Height.
			TextField.setPrefHeight(Height);
		
		TextField.setId(ID);					// Setting The ID Of The TextField.
		TextField.setLayoutX(X);				// Setting The X And Y Coordinates Of The TextField.
		TextField.setLayoutY(Y);

		Pane RequestedLayout = (Pane) Scene.lookup("#"+AddTo);	// Creating The Requested Layout.
		RequestedLayout.getChildren().addAll(TextField);		// Adding The TextField To The Requested Layout.
	}

}