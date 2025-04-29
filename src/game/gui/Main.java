package game.gui;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import game.engine.Battle;
import game.engine.exceptions.InsufficientResourcesException;
import game.engine.exceptions.InvalidLaneException;
import game.engine.lanes.Lane;
import game.engine.titans.AbnormalTitan;
import game.engine.titans.ArmoredTitan;
import game.engine.titans.ColossalTitan;
import game.engine.titans.PureTitan;
import game.engine.titans.Titan;
import game.engine.weapons.PiercingCannon;
import game.engine.weapons.SniperCannon;
import game.engine.weapons.VolleySpreadCannon;
import game.engine.weapons.WallTrap;
import game.engine.weapons.Weapon;
import game.engine.weapons.WeaponRegistry;


public class Main extends Application implements EventHandler<ActionEvent> {
	
	
	
	private Battle battle;
	private Stage primaryStage;
	private AnchorPane ap;
	
	
	private Scene menuScene;
	private Label menuTitle;
	private Label instructions;
	private Button startButton;
	private CheckBox easyCBox;
	private CheckBox hardCBox;
	
	private Label[] wallsinfo;
	private Label currentScore;
	private Label currentTurn;
	private Label currentPhase;
	private Label currentResources;
	private HBox weaponShop;
	private VBox []weaponsInLane;
	private Button []WeaponShopButtons=new Button[4];
	private Pane[] lanes;
	private Button passTurn;
	
	private Scene easyScene;
	private Scene hardScene;
	
	
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage=primaryStage;
		
		ap=new AnchorPane();
		menuScene=new Scene(ap,600,600);
		ap.setStyle("-fx-background-color:yellow;");
		primaryStage.setTitle("AOT Utopia");
		primaryStage.setScene(menuScene);
		primaryStage.show();
		
		startButton=new Button("Start");
		easyCBox=new CheckBox("Easy(3 lanes)");
		hardCBox=new CheckBox("Hard(5 lanes)");
		menuTitle=new Label("UTOPIA");
		menuTitle.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24px; -fx-font-weight: bold;");
		instructions=new Label("The walls are under attack!!! \n Buy weapons using Resources to defeat titans \n approaching the walls and don't let the Titans break any wall.");
		instructions.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold;");
		
		addControl(startButton,0,500,600,100);
		startButton.setOnAction(this);
		
		addControl(easyCBox,120,350,100,30);
		easyCBox.setSelected(true);
		easyCBox.setOnAction(this);
		
		addControl(hardCBox,400,350,100,30);
		hardCBox.setOnAction(this);
		
		addLabel(menuTitle,260,50,200,200);
		addLabel(instructions,50,100,600,250);
		
		
	}
	
	
	public void addControl(Control c,int x,int y, int w,int h) {
		c.setLayoutX(x);
		c.setLayoutY(y);
		c.setPrefWidth(w);
		c.setPrefHeight(h);
		ap.getChildren().add(c);
	}
	public void addHbox(HBox hb,int x,int y, int w,int h) {
		hb.setLayoutX(x);
		hb.setLayoutY(y);
		hb.setPrefWidth(w);
		hb.setPrefHeight(h);
		ap.getChildren().add(hb);
	
	}
	public void addVbox(VBox vb,int x,int y, int w,int h) {
		vb.setLayoutX(x);
		vb.setLayoutY(y);
		vb.setPrefWidth(w);
		vb.setPrefHeight(h);
		ap.getChildren().add(vb);
	
	}
	public void addLabel(Label l,int x,int y, int w,int h) {
		l.setLayoutX(x);
		l.setLayoutY(y);
		l.setPrefWidth(w);
		l.setPrefHeight(h);
		ap.getChildren().add(l);
	
	}
	public void addPane(Pane p,int x,int y, int w,int h) {
		p.setLayoutX(x);
		p.setLayoutY(y);
		p.setPrefWidth(w);
		p.setPrefHeight(h);
		ap.getChildren().add(p);
	
	}
	
	public String getTitanImage(Titan t) {
	if(t instanceof PureTitan) {
		return "file:puretitan.png";
	}
	if(t instanceof AbnormalTitan) {
		return "file:abnormal titan.png";
	}
	if(t instanceof ArmoredTitan) {
		return "file:armored-titan.png";
	}
	if(t instanceof ColossalTitan) {
		return "file:collosal titan.png";
	}
	else
		return "";
    }

    public String getWeaponImage(Weapon w) {
	if(w instanceof SniperCannon) {
		return "file:sniper canon.png";
	}
	if(w instanceof PiercingCannon) {
		return "file:p-canon.png";
	}
	if(w instanceof VolleySpreadCannon) {
		return "file:volley spread cannon.png";
	}
	if(w instanceof WallTrap) {
		return "file:wall-trap.png";
	}
	else
		return "";
	
    }
	
	public void errorHandling(String s) {
		Alert a = new Alert(AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText("An error occurred");
        a.setContentText(s); 
        a.showAndWait();
		
	}
	public void easyMode() {
		wallsinfo= new Label[3];
		wallsinfo[0]=new Label();
		addLabel(wallsinfo[0],40,40,100,100);
		wallsinfo[1]=new Label();
		addLabel(wallsinfo[1],40,160,100,100);
		wallsinfo[2]=new Label();
		addLabel(wallsinfo[2],40,280,100,100);
		
		weaponsInLane=new VBox[3];
		weaponsInLane[0]=new VBox();
		addVbox(weaponsInLane[0],150,40,100,100);
		weaponsInLane[1]=new VBox();
		addVbox(weaponsInLane[1],150,160,100,100);
		weaponsInLane[2]=new VBox();
		addVbox(weaponsInLane[2],150,280,100,100);
		
		
		lanes=new Pane[3];
		lanes[0]=new Pane();
		addPane(lanes[0],250,40,400,100);
		lanes[1]=new Pane();
		addPane(lanes[1],250,160,400,100);
		lanes[2]=new Pane();
		addPane(lanes[2],250,280,400,100);
		for(int i=0;i<lanes.length;i++) {
			lanes[i].setStyle("-fx-background-color:grey;");
		}
		
		weaponShop=new HBox();
		addHbox(weaponShop,250,580,450,100);
		//WeaponShopButtons=new Button[4];
		
		WeaponShopButtons[0]=new Button();
		WeaponShopButtons[0].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[0]);
		WeaponRegistry w1=battle.getWeaponFactory().getWeaponShop().get(1);
		String s1=w1.getName() +"\n Price: "+ w1.getPrice()+"\n Damage: " + w1.getDamage();
		WeaponShopButtons[0].setText(s1);
		
		
		WeaponShopButtons[1]=new Button();
		WeaponShopButtons[1].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[1]);
		WeaponRegistry w2=battle.getWeaponFactory().getWeaponShop().get(2);
		String s2=w2.getName() +"\n Price: "+ w2.getPrice() +"\n Damage: "+ w2.getDamage();
		WeaponShopButtons[1].setText(s2);
		
		
		WeaponShopButtons[2]=new Button();
		WeaponShopButtons[2].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[2]);
		WeaponRegistry w3=battle.getWeaponFactory().getWeaponShop().get(3);
		String s3=w3.getName() +"\n Price: "+ w3.getPrice() +"\n Damage: "+ w3.getDamage();
		WeaponShopButtons[2].setText(s3);
		
		
		WeaponShopButtons[3]=new Button();
		WeaponShopButtons[3].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[3]);
		WeaponRegistry w4=battle.getWeaponFactory().getWeaponShop().get(4);
		String s4=w4.getName() +"\n Price: "+ w4.getPrice() +"\n Damage: "+ w4.getDamage();
		WeaponShopButtons[3].setText(s4);
		
		currentScore=new Label();
		addLabel(currentScore,40,520,120,50);
		currentTurn=new Label();
		addLabel(currentTurn,40,550,120,50);
		currentPhase=new Label();
		addLabel(currentPhase,40,580,120,50);
		currentResources=new Label();
		addLabel(currentResources,40,610,120,50);
		
		passTurn=new Button("Pass Turn");
		addControl(passTurn,350,650,100,50);
		passTurn.setOnAction(this);
		
	}
	public void hardMode() {
		wallsinfo= new Label[5];
		wallsinfo[0]=new Label();
		addLabel(wallsinfo[0],40,0,100,100);
		wallsinfo[1]=new Label();
		addLabel(wallsinfo[1],40,120,100,100);
		wallsinfo[2]=new Label();
		addLabel(wallsinfo[2],40,240,100,100);
		wallsinfo[3]=new Label();
		addLabel(wallsinfo[3],40,360,100,100);
		wallsinfo[4]=new Label();
		addLabel(wallsinfo[4],40,480,100,100);
		
		weaponsInLane=new VBox[5];
		weaponsInLane[0]=new VBox();
		addVbox(weaponsInLane[0],150,0,100,100);
		weaponsInLane[1]=new VBox();
		addVbox(weaponsInLane[1],150,120,100,100);
		weaponsInLane[2]=new VBox();
		addVbox(weaponsInLane[2],150,240,100,100);
		weaponsInLane[3]=new VBox();
		addVbox(weaponsInLane[3],150,360,100,100);
		weaponsInLane[4]=new VBox();
		addVbox(weaponsInLane[4],150,480,100,100);
		
		
		
		lanes=new Pane[5];
		lanes[0]=new Pane();
		addPane(lanes[0],250,0,400,100);
		lanes[1]=new Pane();
		addPane(lanes[1],250,120,400,100);
		lanes[2]=new Pane();
		addPane(lanes[2],250,240,400,100);
		lanes[3]=new Pane();
		addPane(lanes[3],250,360,400,100);
		lanes[4]=new Pane();
		addPane(lanes[4],250,480,400,100);
		for(int i=0;i<lanes.length;i++) {
			lanes[i].setStyle("-fx-background-color:grey;");
		}
		
		weaponShop=new HBox();
		addHbox(weaponShop,250,580,450,100);
		//WeaponShopButtons=new Button[4];
		
		WeaponShopButtons[0]=new Button();
		WeaponShopButtons[0].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[0]);
		WeaponRegistry w1=battle.getWeaponFactory().getWeaponShop().get(1);
		String s1=w1.getName() +"\n Price: "+ w1.getPrice()+"\n Damage: " + w1.getDamage();
		WeaponShopButtons[0].setText(s1);
		
		
		WeaponShopButtons[1]=new Button();
		WeaponShopButtons[1].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[1]);
		WeaponRegistry w2=battle.getWeaponFactory().getWeaponShop().get(2);
		String s2=w2.getName() +"\n Price: "+ w2.getPrice() +"\n Damage:"+ w2.getDamage();
		WeaponShopButtons[1].setText(s2);
		
		
		WeaponShopButtons[2]=new Button();
		WeaponShopButtons[2].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[2]);
		WeaponRegistry w3=battle.getWeaponFactory().getWeaponShop().get(3);
		String s3=w3.getName() +"\n Price: "+ w3.getPrice() +"\n Damage: "+ w3.getDamage();
		WeaponShopButtons[2].setText(s3);
		
		
		WeaponShopButtons[3]=new Button();
		WeaponShopButtons[3].setOnAction(this);
		weaponShop.getChildren().add(WeaponShopButtons[3]);
		WeaponRegistry w4=battle.getWeaponFactory().getWeaponShop().get(4);
		String s4=w4.getName() +"\n Price: "+ w4.getPrice() +"\n Damage: "+ w4.getDamage();
		WeaponShopButtons[3].setText(s4);
		
		currentScore=new Label();
		addLabel(currentScore,40,550,120,50);
		currentTurn=new Label();
		addLabel(currentTurn,40,580,120,50);
		currentPhase=new Label();
		addLabel(currentPhase,40,610,120,50);
		currentResources=new Label();
		addLabel(currentResources,40,640,120,50);
		
		passTurn=new Button("Pass Turn");
		addControl(passTurn,350,650,100,50);
		passTurn.setOnAction(this);
		
	}
	public void updateGameValues() {
		currentScore.setText("Score : "+ battle.getScore());
		currentTurn.setText("Turn : "+battle.getNumberOfTurns());
		currentResources.setText("Recources : "+battle.getResourcesGathered());
		currentPhase.setText("Phase : "+battle.getBattlePhase());
		
		for(int i=0;i<battle.getOriginalLanes().size();i++) {
			Lane l=battle.getOriginalLanes().get(i);
			wallsinfo[i].setText("Danger Level:"+l.getDangerLevel()+"\n"+"Lane Health:"+l.getLaneWall().getCurrentHealth());
			
			if(!l.isLaneLost()) {
				weaponsInLane[i].getChildren().clear();
				for(int j=0;j<l.getWeapons().size();j++) {
					Weapon w=l.getWeapons().get(j);
					String s=getWeaponImage(w);
					Button bu=new Button();
					ImageView m=new ImageView(new Image(s));
					m.setFitHeight(30);
					m.setFitWidth(30);
					bu.setGraphic(m);
					weaponsInLane[i].getChildren().add(bu);
					
					
				}
				lanes[i].getChildren().clear();
				
				for(Titan t:l.getTitans()) {
					
					
					String s=getTitanImage(t);
					Button bu=new Button();
					ImageView image=new ImageView(s);
					image.setFitHeight(20);
					image.setFitWidth(20);
					bu.setGraphic(image);
					bu.setText(t.getCurrentHealth()+"");
					
					
					StackPane sp=new StackPane();
					sp.setLayoutX(t.getDistance());
					sp.setLayoutY(Math.random()*55);
					sp.getChildren().add(bu);
					
					lanes[i].getChildren().add(sp);
					
					
				}
				
				
				
				
				
				
			}
			
			
		}
		
		
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void handle(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==easyCBox) {
			hardCBox.setSelected(false);
		}
		if(e.getSource()==hardCBox) {
			easyCBox.setSelected(false);
		}
		if(e.getSource()==startButton) {
			ap=new AnchorPane();
			
			if(easyCBox.isSelected()) {
				//try
				try {
					battle=new Battle(1,0,400,3,250);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					errorHandling("unable to create game");
				}
			
			
			easyScene=new Scene(ap,700,700);
			ap.setStyle("-fx-background-color:green;");
			easyMode();
			primaryStage.setScene(easyScene);
			primaryStage.show();
			}
			if(hardCBox.isSelected()) {
				//try
				try {
					battle=new Battle(1,0,400,5,125);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					errorHandling("unable to create game");
				}
				
				
				hardScene=new Scene(ap,700,700);
				ap.setStyle("-fx-background-color:green;");
				hardMode();
				primaryStage.setScene(hardScene);
				primaryStage.show();
				
			
			}
		}
		if(e.getSource()==passTurn) {
			battle.passTurn();
		}
		for(int i=0;i<WeaponShopButtons.length;i++) {
			if(e.getSource()==WeaponShopButtons[i]) {
				TextInputDialog d=new TextInputDialog();
				d.setHeaderText("Enter Weapon Lane number");
				d.setContentText("Lane Number");
				Optional<String> res=d.showAndWait();
				int laneNumber=Integer.parseInt(res.get());
				//check number of lanes when easy or hard
				if(laneNumber>battle.getOriginalLanes().size()||laneNumber<1) {
					errorHandling("Invalid Lane Number!!!!");
				}
				else {
				Lane l=battle.getOriginalLanes().get(laneNumber-1);
				//try
				try {
					battle.purchaseWeapon(i+1,l);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					errorHandling(e1.getMessage());
				}
				}
			}
			
		}
		if(battle.isGameOver()) {
			errorHandling("Game is OVER!!!!!!!!");
		}
		else
			updateGameValues();
		
	}
}
