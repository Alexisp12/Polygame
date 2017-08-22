package com.polyjoule.ylebourlout.apriou.polygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.polyjoule.ylebourlout.apriou.polygame.Game.COEFVEHICULESEVITES;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.DEPLACEMENTBG;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.DUREEAFFICHAGEGO;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.DUREEAFFICHAGETOTALPANNEAUX;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.DUREEEXPLOFINAL;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.GAINCARBURANT;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.NBENNEMIPARCOLONNEMAX;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.NBVEHICULESENNEMIS;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.PERTECARBURANT;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.RATIOTABLEAUSCORE;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.nbVie;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.users;
import static com.polyjoule.ylebourlout.apriou.polygame.Menu.userInfo;

/**
 * Created by Alexis on 19/07/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    // déclaration de l'objet définissant la boucle principale de déplacement et de rendu
    private GameLoopThread gameLoopThread;
    private VehiculePlayer vehicule;
    private VehiculeEnnemi vehiculeEnnemi1;
    private VehiculeEnnemi vehiculeEnnemi2;
    private VehiculeEnnemi vehiculeEnnemi3;
    private VehiculeEnnemi vehiculeEnnemi4;
    private VehiculeEnnemi vehiculeEnnemi5;
    private VehiculeEnnemi vehiculeEnnemi6;
    private Carburant carburant;
    private Boolean canMoveVehicule=false;
    private Boolean initialisation=false;
    public static int cvW; // canva width
    public static int cvH; // canva heigh
    private int dureeExplo=0;
    private int dureeClignotementOn=0;
    private int dureeClignotementOff=0;
    private Background bg;
    private int refreshScore=0;
    private int score=0;
    private int levelCarburant;
    private int levelVie;
    private int longueurBarreCarburant;
    private int longueurBarreVie;
    private int levelCarburantMaxInit;
    private int levelVieMaxInit;
    private int coinGPause;
    private int coinDPause;
    private int coinHPause;
    private int coinBPause;
    private int coinGRestart;
    private int coinDRestart;
    private int coinHRestart;
    private int coinBRestart;
    private int bordStartG;
    private int bordStartD;
    private int bordStartB;
    private int bordStartH;
    private int bordTableauGauche;
    private int bordTableauDroit;
    private int longueurTableau;
    private double ratio = RATIOTABLEAUSCORE;
    private int hauteurTableau;
    int bordTableauHaut;
    int bordTableauBas;
    public static int routeH;
    public static int routeM;
    public static int routeB;
    private int levelCarburantMinInit;
    private int levelVieMinInit;
    private int tempsAffichageGO=0;
    private int dureeAffichageGO=DUREEAFFICHAGEGO;
    private Boolean baisseCarburant=false;
    private Boolean start=false;
    private Boolean pause=false;
    private Boolean perdu=false;
    private Boolean toastHSdone=false;
    private String textStart="Cliquez pour commencer";
    private String textPause="Pause";
    private String textPerdu="GAME OVER";
    private String textHS="Record personnel : ";
    private String classementText="Classement :";
    private String textDistance="Distance parcourue";
    private String textVoitureEvites="Collisions évités";
    private String textScoreTotal="Score total";
    private String textRestart="Restart !";
    private int nbVehicules=0;
    private int comptageScoreFinal=0;
    private int comptageDistance=0;
    private int comptageCollision=0;
    private int scorefinal=0;
    private int rangScore=0;
    private Boolean restartEnable=false;
    private int positionnementV1X;
    private int distanceEntreVehiculesRand;
    private int distanceDoigtVoiture=0;
    private int tailleTexte;
    private Boolean collisionVehicule=false;
    private Boolean comptageVehiculeDone=false;
    private Boolean depart=false;
    private Boolean gestionBitmap=false;
    private int dureeAffichagePanneaux=0;
    private int longueurStart;
    private Drawable cinqdraw;
    private Drawable quatredraw;
    private Drawable troisdraw;
    private Drawable deuxdraw;
    private Drawable undraw;
    private Drawable godraw;
    private Drawable touchtostartdraw;
    private Bitmap cinqBitmap;
    private Bitmap quatreBitmap;
    private Bitmap troisBitmap;
    private Bitmap deuxBitmap;
    private Bitmap unBitmap;
    private Bitmap goBitmap;
    private Bitmap touchtostartBitmap;
    private Bitmap tableauScoreBitmap;
    private Boolean[] stratEnnemiDone; // Une case par rang sur l'écran
    private int [] stratEnnemi;
    private int randomStrat;
    private int colonneEnnemiAVenir=0;
    private Boolean [] vehiculeDispo;
    private Boolean newColonne=true;
    private int highestEnnemi; // Ennemi le plus dans l'écran
    private int distanceEntreVehicules1;
    private int distanceEntreVehicules2;
    private int distanceEntreVehicules3;
    private int distanceEntreVehicules4;

    //private String textRestart="Record : ";


    // création de la surface de dessin
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        gameLoopThread = new GameLoopThread(this);

        // création d'un objet "vehicule", dont on définira la largeur/hauteur
        // selon la largeur ou la hauteur de l'écran
        vehicule = new VehiculePlayer(this.getContext());
        carburant = new Carburant (this.getContext());
        vehiculeEnnemi1 = new VehiculeEnnemi(this.getContext());
        vehiculeEnnemi2 = new VehiculeEnnemi(this.getContext());
        vehiculeEnnemi3 = new VehiculeEnnemi(this.getContext());
        vehiculeEnnemi4 = new VehiculeEnnemi(this.getContext());
        vehiculeEnnemi5 = new VehiculeEnnemi(this.getContext());
        vehiculeEnnemi6 = new VehiculeEnnemi(this.getContext());


        stratEnnemiDone = new Boolean [NBENNEMIPARCOLONNEMAX];
        stratEnnemi = new int [NBENNEMIPARCOLONNEMAX];
        vehiculeDispo = new Boolean [NBVEHICULESENNEMIS];

        for(int i=0;i<stratEnnemiDone.length;i++){
            stratEnnemiDone[i]=false;
        }
        for(int j=0; j<vehiculeDispo.length;j++){
            vehiculeDispo[j]=true;
        }
//        cinqdraw = ContextCompat.getDrawable(this.getContext(),R.drawable.cinq);
//        quatredraw = ContextCompat.getDrawable(this.getContext(),R.drawable.quatre);
//        troisdraw = ContextCompat.getDrawable(this.getContext(),R.drawable.trois);
//        deuxdraw = ContextCompat.getDrawable(this.getContext(),R.drawable.deux);
//        undraw = ContextCompat.getDrawable(this.getContext(),R.drawable.un);
//        godraw = ContextCompat.getDrawable(this.getContext(),R.drawable.go);
//        touchtostartdraw = ContextCompat.getDrawable(this.getContext(),R.drawable.touchtostart);




    }

    // Fonction qui "dessine" un écran de jeu
    public void doDraw(Canvas canvas) {
        if(canvas==null) {return;}

        // Conv dp
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float dp = 30f; // 30 dp
        float dp2 = 50f;
        float dp3=10f;
        float dp4=20f;
        float dp5=14f;
        float dp6=12f;
        float dp7=9f;
        float dp8=25f;
        float fpixels8=metrics.density*dp8;
        float fpixels7=metrics.density*dp7;
        float fpixels6=metrics.density*dp6;
        float fpixels5=metrics.density*dp5;
        float fpixels4=metrics.density*dp4;
        float fpixels3=metrics.density*dp3;
        float fpixels2=metrics.density*dp2;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);
        int pixels2=(int) (fpixels2+0.5f);
        int pixels3=(int) (fpixels3+0.5f);
        int pixels4=(int) (fpixels4+0.5f);
        int pixels5=(int) (fpixels5+0.5f);
        int pixels6=(int) (fpixels6+0.5f);
        int pixels7=(int) (fpixels7+0.5f);
        int pixels8 = (int) (fpixels8 + 0.5f);
        cvH = canvas.getHeight();
        cvW = canvas.getWidth();

        routeH=5*cvW/16-pixels8;
        routeM=cvW/2-pixels8;
        routeB=43*cvW/64-pixels8;

        if(bg!=null) {
            bg.draw(canvas);
        }

        if(!initialisation){

            vehicule.setY(cvH-vehicule.getvehiculePlayerH()-cvH/8);
            vehicule.setX((cvW/2)-vehicule.getvehiculePlayerW()/2);

            vehiculeEnnemi1.setX(cvW);
            vehiculeEnnemi1.setY(-2*vehiculeEnnemi1.getvehiculeEnnemiH());
            vehiculeEnnemi2.setX(cvW);
            vehiculeEnnemi2.setY(-2*vehiculeEnnemi2.getvehiculeEnnemiH());
            vehiculeEnnemi3.setX(cvW);
            vehiculeEnnemi3.setY(-2*vehiculeEnnemi3.getvehiculeEnnemiH());
            vehiculeEnnemi4.setX(cvW);
            vehiculeEnnemi4.setY(-2*vehiculeEnnemi4.getvehiculeEnnemiH());
            vehiculeEnnemi5.setX(cvW);
            vehiculeEnnemi5.setY(-2*vehiculeEnnemi5.getvehiculeEnnemiH());
            vehiculeEnnemi6.setX(cvW);
            vehiculeEnnemi6.setY(-2*vehiculeEnnemi6.getvehiculeEnnemiH());

            distanceEntreVehicules1=-3*vehicule.getvehiculePlayerH()/2;
            distanceEntreVehicules2=-2*vehicule.getvehiculePlayerH();
            distanceEntreVehicules3=-5*vehicule.getvehiculePlayerH()/2;
            distanceEntreVehicules4=-3*vehicule.getvehiculePlayerH();


            carburant.setY(cvH);
            carburant.setX(cvW);

            coinGPause=cvW/2-cvW/64;
            coinHPause=pixels3;
            coinDPause=cvW/2+cvW/64;
            coinBPause=pixels3+pixels/2;

            //start
            bordStartG=cvW/16;
            bordStartD=cvW-bordStartG;
            bordStartH=cvH/3;
            longueurStart=bordStartD-bordStartG;
            bordStartB=bordStartH + (longueurStart/(BitmapFactory.decodeResource(getResources(), R.drawable.touchtostart).getWidth()/BitmapFactory.decodeResource(getResources(), R.drawable.touchtostart).getHeight()));   //bordStartH + ((int) Math.round( longueurStart/RATIOSTART)

            // carburant
            levelCarburantMaxInit=cvW-cvW/50;
            levelCarburantMinInit=cvW-3*cvW/40;
            levelVieMaxInit=cvW-cvW/40;
            levelVieMinInit=cvW-3*cvW/32;
            levelCarburant=levelCarburantMaxInit;
            levelVie=levelVieMaxInit;
            longueurBarreCarburant=levelCarburant-(levelCarburantMinInit);
            longueurBarreVie=levelVie-(cvW-3*cvW/32);

            // tableau score
            bordTableauGauche=cvW/32;
            bordTableauDroit =31*cvW/32;
            longueurTableau = bordTableauDroit - bordTableauGauche;
            hauteurTableau = ((int) Math.round(longueurTableau/ratio));
            bordTableauHaut=cvH/4;
            bordTableauBas=bordTableauHaut+12*(longueurTableau/(BitmapFactory.decodeResource(getResources(), R.drawable.finish).getWidth()/BitmapFactory.decodeResource(getResources(), R.drawable.finish).getHeight()))/16;    //+hauteurTableau;


            if(!gestionBitmap) {
                cinqBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.cinq), longueurStart, bordStartB - bordStartH, false);
                quatreBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.quatre), longueurStart, bordStartB - bordStartH, false);

                troisBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.trois), longueurStart, bordStartB - bordStartH, false);

                deuxBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.deux), longueurStart, bordStartB - bordStartH, false);
                unBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.un), longueurStart, bordStartB - bordStartH, false);
                goBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.go), longueurStart, bordStartB - bordStartH, false);
                touchtostartBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.touchtostart), longueurStart, bordStartB - bordStartH, false);
                tableauScoreBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.finish), longueurTableau, bordTableauBas - bordTableauHaut, false);
                gestionBitmap=true;
            }
//            cinqdraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//            quatredraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//            troisdraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//            deuxdraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//            undraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//            godraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//            touchtostartdraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);


//            if(dureeClignotementOn%15==0 || dureeClignotementOn==0) {
//                // Cliquez pour commencer
//                TextPaint textPaintStart = new TextPaint();
//                textPaintStart.setTextSize(pixels4);
//                textPaintStart.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
//                canvas.drawText(textStart, 5*cvW / 16, 11 * cvH / 16, textPaintStart);
//                if(dureeClignotementOff%20==0 && dureeClignotementOff!=0) {
//                    dureeClignotementOn++;
//                    dureeClignotementOff=0;
//                } else {
//                    dureeClignotementOff++;
//                }
//            } else {
//                dureeClignotementOn++;
//            }



            if(start) {
                carburant.setMove(true);
                //vehiculeEnnemi1.setMove(true);
                initialisation = true;
                bg.setMove(true);
                bg.setVector(-DEPLACEMENTBG);
                dureeClignotementOn=0;
            }

        }

        // ScoreText
        TextPaint textPaintScore = new TextPaint();
        textPaintScore.setTextSize(pixels5);
        textPaintScore.setColor(ContextCompat.getColor(this.getContext(),R.color.colorPrimaryDark));
        canvas.drawText(Integer.toString(score),pixels3/2,2*pixels3,textPaintScore);

        // Level Carburant
        TextPaint paintlevelCarburant = new TextPaint();
        paintlevelCarburant.setTextSize(pixels);

        paintlevelCarburant.setColor(ContextCompat.getColor(this.getContext(),R.color.colorPrimaryDark));
        canvas.drawRect(levelCarburantMinInit,pixels3,levelCarburant,pixels3+pixels/3,paintlevelCarburant);

        // Image carburant
        Drawable carburantDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.carburantlogo);
        //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
        carburantDraw.setBounds(levelCarburantMinInit-pixels2/12-carburant.getcarburantH()/2,pixels3,levelCarburantMinInit-pixels2/12,pixels3+pixels/3);
        carburantDraw.draw(canvas);

//        // Level Vie
//        TextPaint paintlevelVie = new TextPaint();
//        paintlevelVie.setTextSize(pixels);
//        paintlevelVie.setColor(Color.RED);
//        canvas.drawRect(levelVieMinInit-(cvW-(levelVieMinInit-pixels2/5-carburant.getcarburantW()/2)),pixels3,levelVie-(cvW-(levelVieMinInit-pixels2/5-carburant.getcarburantW()/2)),pixels3+pixels/2,paintlevelVie);
//
//        // Image vie
//        Drawable vieDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.coeur);
//        //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
//        vieDraw.setBounds(levelVieMinInit-pixels2/2-carburant.getcarburantW()/2-(cvW-(levelVieMinInit-pixels2/5-carburant.getcarburantW())),pixels3,levelVieMinInit-(pixels3/2)-(cvW-(levelVieMinInit-pixels2/5-carburant.getcarburantW()/2)),pixels3+pixels/2);
//        vieDraw.draw(canvas);

        if(users.size()!=0) {
            if (users.size() == 1) {
                if(users.get(0).getHighScore()!=-1) {
                    // Top 3
//                        TextPaint textPaintClassement = new TextPaint();
//                        textPaintClassement.setTextSize(pixels5);
//                        textPaintClassement.setColor(ContextCompat.getColor(this.getContext(), R.color.primary_dark_material_dark_1));
//                        canvas.drawText(classementText, pixels3, cvH - pixels3, textPaintClassement);

                    TextPaint textPaintTop1 = new TextPaint();
                    textPaintTop1.setTextSize(pixels5);
                    textPaintTop1.setColor(ContextCompat.getColor(this.getContext(), R.color.top1));
                    canvas.drawText(users.get(0).getPseudo() + " : " + users.get(0).highScore, pixels3, cvH - pixels3, textPaintTop1);

                }

            } else {
                if (users.size() == 2) {
                    if(users.get(0).getHighScore()!=-1) {
                        // Top 3
//                        TextPaint textPaintClassement = new TextPaint();
//                        textPaintClassement.setTextSize(pixels5);
//                        textPaintClassement.setColor(ContextCompat.getColor(this.getContext(), R.color.primary_dark_material_dark_1));
//                        canvas.drawText(classementText, pixels3, cvH - pixels3, textPaintClassement);

                        TextPaint textPaintTop1 = new TextPaint();
                        textPaintTop1.setTextSize(pixels5);
                        textPaintTop1.setColor(ContextCompat.getColor(this.getContext(), R.color.top1));
                        canvas.drawText(users.get(0).getPseudo() + " : " + users.get(0).highScore, pixels3, cvH - pixels3, textPaintTop1);

                        if(users.get(1).getHighScore()!=-1) {
                            TextPaint textPaintTop2 = new TextPaint();
                            textPaintTop2.setTextSize(pixels5);
                            textPaintTop2.setColor(ContextCompat.getColor(this.getContext(), R.color.top2));
                            canvas.drawText(users.get(1).getPseudo() + " : " + users.get(1).highScore, cvW/2, cvH - pixels3, textPaintTop2);
                        }
                    }
                } else {


                    if(users.get(0).getHighScore()!=-1) {
                        // Top 3
//                        TextPaint textPaintClassement = new TextPaint();
//                        textPaintClassement.setTextSize(pixels5);
//                        textPaintClassement.setColor(ContextCompat.getColor(this.getContext(), R.color.primary_dark_material_dark_1));
//                        canvas.drawText(classementText, pixels3, cvH - pixels3, textPaintClassement);

                        TextPaint textPaintTop1 = new TextPaint();
                        textPaintTop1.setTextSize(pixels5);
                        textPaintTop1.setColor(ContextCompat.getColor(this.getContext(), R.color.top1));
                        canvas.drawText(users.get(0).getPseudo() + " : " + users.get(0).highScore, pixels3, cvH - pixels3, textPaintTop1);

                        if(users.get(1).getHighScore()!=-1) {
                            TextPaint textPaintTop2 = new TextPaint();
                            textPaintTop2.setTextSize(pixels5);
                            textPaintTop2.setColor(ContextCompat.getColor(this.getContext(), R.color.top2));
                            canvas.drawText(users.get(1).getPseudo() + " : " + users.get(1).highScore, cvW/2, cvH - pixels3, textPaintTop2);
                            if(users.get(2).getHighScore()!=-1){
                                TextPaint textPaintTop3 = new TextPaint();
                                textPaintTop3.setTextSize(pixels5);
                                textPaintTop3.setColor(ContextCompat.getColor(this.getContext(), R.color.top3));
                                canvas.drawText( users.get(2).getPseudo() + " : " + users.get(2).highScore, 3*cvW/4, cvH -  pixels3, textPaintTop3);
                            }
                        }
                    }


                }
            }
        }



        // carburant
        if(carburant.getY()<-carburant.getcarburantH()){
            // Inutile de draw en dehors de l'écran
        } else {
            carburant.draw(canvas);
        }
        // vehicule ennemi1
        if(vehiculeEnnemi1.getY()<-vehiculeEnnemi1.getvehiculeEnnemiH()){
            //vehiculeDispo[0]=true;
        } else {
            vehiculeEnnemi1.draw(canvas);
            //Log.d("vehicule1","nondispo");
            //vehiculeDispo[0]=false;
        }
        // vehicule ennemi2
        if(vehiculeEnnemi2.getY()<-vehiculeEnnemi2.getvehiculeEnnemiH()){
            // Inutile de draw en dehors de l'écran
            //vehiculeDispo[1]=true;
        } else {
            vehiculeEnnemi2.draw(canvas);
            //vehiculeDispo[1]=false;
        }
        // vehicule ennemi1
        if(vehiculeEnnemi3.getY()<-vehiculeEnnemi3.getvehiculeEnnemiH()){
            // Inutile de draw en dehors de l'écran
            //vehiculeDispo[2]=true;
        } else {
            vehiculeEnnemi3.draw(canvas);
            //vehiculeDispo[2]=false;
        }
        // vehicule ennemi1
        if(vehiculeEnnemi4.getY()<-vehiculeEnnemi4.getvehiculeEnnemiH()){
            // Inutile de draw en dehors de l'écran
           // vehiculeDispo[3]=true;
        } else {
            vehiculeEnnemi4.draw(canvas);
            //vehiculeDispo[3]=false;
        }
        // vehicule ennemi1
        if(vehiculeEnnemi5.getY()<-vehiculeEnnemi5.getvehiculeEnnemiH()){
            // Inutile de draw en dehors de l'écran
            //vehiculeDispo[4]=true;
        } else {
            vehiculeEnnemi5.draw(canvas);
            //vehiculeDispo[4]=false;
        }
        // vehicule ennemi1
        if(vehiculeEnnemi6.getY()<-vehiculeEnnemi6.getvehiculeEnnemiH()){
            // Inutile de draw en dehors de l'écran
            //vehiculeDispo[5]=true;
        } else {
            vehiculeEnnemi6.draw(canvas);
            //vehiculeDispo[5]=false;
            //Log.d("vehicule6","nondispo");
        }

        // Vehicule player
        vehicule.draw(canvas);


//        if(depart){
//
//            dureeAffichagePanneaux++;
//
//            if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX){
//                Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.cinq);
//                startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//                startDraw.draw(canvas);
//            } else {
//                if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*2){
//                    Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.quatre);
//                    startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//                    startDraw.draw(canvas);
//                } else {
//                    if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*3){
//                        Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.trois);
//                        startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//                        startDraw.draw(canvas);
//                    } else {
//                        if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*4){
//                            Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.deux);
//                            startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);
//                            startDraw.draw(canvas);
//                        } else {
//                            if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*5) {
//                                Drawable startDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.un);
//                                startDraw.setBounds(bordStartG, bordStartH, bordStartD, bordStartB);
//                                startDraw.draw(canvas);
//                            } else {
//                                if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*6) {
//                                    Drawable startDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.gooooo);
//                                    startDraw.setBounds(bordStartG, bordStartH, bordStartD, bordStartB);
//                                    startDraw.draw(canvas);
//                                } else {
//                                    start = true;
//                                    Game.startMusique();
//                                    depart=false;
//                                    dureeAffichagePanneaux=0;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } else {
//            if(!start) {
//                Drawable startDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.start);
//                //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
//                startDraw.setBounds(bordStartG, bordStartH, bordStartD, bordStartB);
//                startDraw.draw(canvas);
//            }
//        }

        // Image resize
        if(depart){

            dureeAffichagePanneaux++;

            if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX){
                //Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.cinq);
                //startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);

                //cinqdraw.draw(canvas);

                touchtostartBitmap=null;
                canvas.drawBitmap(cinqBitmap,bordStartG,bordStartH,null);

            } else {
                if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*2){
                    //Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.quatre);
                    //startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);

                    //quatredraw.draw(canvas);
                    cinqBitmap=null;
                    canvas.drawBitmap(quatreBitmap,bordStartG,bordStartH,null);
                } else {
                    if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*3){
                        // Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.trois);
                        //startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);

                        //troisdraw.draw(canvas);
                        quatreBitmap=null;
                        canvas.drawBitmap(troisBitmap,bordStartG,bordStartH,null);
                    } else {
                        if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*4){
                            //Drawable startDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.deux);
                            //startDraw.setBounds(bordStartG,bordStartH,bordStartD,bordStartB);

                            //deuxdraw.draw(canvas);
                            troisBitmap=null;
                            canvas.drawBitmap(deuxBitmap,bordStartG,bordStartH,null);
                        } else {
                            if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*5) {
                                //Drawable startDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.un);
                                //startDraw.setBounds(bordStartG, bordStartH, bordStartD, bordStartB);

                                //undraw.draw(canvas);
                                deuxBitmap=null;
                                canvas.drawBitmap(unBitmap,bordStartG,bordStartH,null);
                            } else {
                                if(dureeAffichagePanneaux<DUREEAFFICHAGETOTALPANNEAUX*6) {
                                    //Drawable startDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.go);
                                    //startDraw.setBounds(bordStartG, bordStartH, bordStartD, bordStartB);

                                    //godraw.draw(canvas);
                                    unBitmap=null;
                                    canvas.drawBitmap(goBitmap,bordStartG,bordStartH,null);
                                } else {
                                    start = true;
                                    goBitmap=null;
                                    touchtostartBitmap=null;
                                    Game.startMusique();
                                    depart=false;
                                    dureeAffichagePanneaux=0;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            if(!start) {
                //Drawable startDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.touchtostart);
                //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
                //startDraw.setBounds(bordStartG, bordStartH, bordStartD, bordStartB);

                //touchtostartdraw.draw(canvas);
                canvas.drawBitmap(touchtostartBitmap,bordStartG,bordStartH,null);
            }
        }


        if(collisionVehicule){
            Game.pauseMusique();
            if(dureeExplo!=DUREEEXPLOFINAL) {
                Drawable explo = ContextCompat.getDrawable(this.getContext(), R.drawable.explo);
                explo.setBounds(vehicule.getX()-vehicule.getvehiculePlayerH()/2,vehicule.getY()-vehicule.getvehiculePlayerH()/2,vehicule.getX()+vehicule.getvehiculePlayerW()+vehicule.getvehiculePlayerH()/2,vehicule.getY()+3*vehicule.getvehiculePlayerH()/2);
                explo.draw(canvas);
                dureeExplo++;
            } else {
                perdu=true;
            }
        }

        // Bouton pause
        if(!pause) {
            Drawable pauseDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.pause);
            //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
            pauseDraw.setBounds(coinGPause, coinHPause, coinDPause, coinBPause);
            pauseDraw.draw(canvas);
        } else {
            Drawable pauseDraw = ContextCompat.getDrawable(this.getContext(), R.drawable.lecture);
            //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
            pauseDraw.setBounds(coinGPause, coinHPause, coinDPause, coinBPause);
            pauseDraw.draw(canvas);

            TextPaint textPaintPause = new TextPaint();
            textPaintPause.setTextSize(pixels);
            textPaintPause.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
            canvas.drawText(textPause,12*cvW/32, 5*cvH/12, textPaintPause);


            TextPaint textPaintHS = new TextPaint();
            textPaintHS.setTextSize(pixels4);
            textPaintHS.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));

            if(userInfo!=null) {
                if(userInfo.getHighScore()!=-1) {
                    canvas.drawText(textHS + userInfo.getHighScore(), 9*cvW / 40, 7*cvH/12, textPaintHS);
                } else {
                    canvas.drawText(textHS + "0", 9*cvW / 40, 7*cvH/12, textPaintHS);
                }
            }

        }


        if(perdu){
            Game.pauseMusique();


            vehiculeEnnemi1.setMove(false);
            vehiculeEnnemi2.setMove(false);
            vehiculeEnnemi3.setMove(false);
            vehiculeEnnemi4.setMove(false);
            vehiculeEnnemi5.setMove(false);
            vehiculeEnnemi6.setMove(false);

            // Image Game Over
            //Drawable goDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.finish);
            // resize


//            Drawable goDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.finish);
//
//
//            goDraw.setBounds(bordTableauGauche,bordTableauHaut,bordTableauDroit,bordTableauBas);
//            goDraw.draw(canvas);

            canvas.drawBitmap(tableauScoreBitmap,bordTableauGauche,bordTableauHaut,null);


            // Texte tableau score
            scorefinal=score+((nbVehicules-1)*COEFVEHICULESEVITES);

            if(comptageDistance!=score){
                comptageDistance++;
                comptageScoreFinal++;
            } else {
                if(comptageCollision!=nbVehicules-1){
                    comptageCollision++;
                    comptageScoreFinal+=COEFVEHICULESEVITES;
                } else {
                    if (comptageScoreFinal != scorefinal) {
                        comptageScoreFinal++;
                    } else {

                        //Collections.sort(users, new UsersComparator());

                        for(int i=0; i<users.size();i++){
                            if(scorefinal>users.get((users.size()-1)-i).getHighScore()){
                                rangScore=users.size()-i;
                            }
                        }

                        restartEnable = true;
                        if (userInfo != null) { // + registered//true
                            //Log.d("score",Integer.toString(score));
                            //Log.d("HighScore",Integer.toString(highScore));
                            if (scorefinal > userInfo.getHighScore()) {
                                Game.update(scorefinal); // update local
                                userInfo.setHighScore(scorefinal);
                                Game.saveUserInformation(userInfo);
                                Game.pullHighScore();
                            }
                        }
                    }
                }
            }

            tailleTexte=pixels6;
            int espaceTexte=pixels7;
            TextPaint textPaintTableauAffichage = new TextPaint();
            textPaintTableauAffichage.setTextSize(tailleTexte);
            textPaintTableauAffichage.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
            // Distance
            canvas.drawText(textDistance+" : "+comptageDistance,cvW/4+tailleTexte,cvH/2-espaceTexte,textPaintTableauAffichage);
            // Collision évités
            canvas.drawText(textVoitureEvites+" : "+Integer.toString(comptageCollision),cvW/4+tailleTexte,cvH/2+tailleTexte+espaceTexte-espaceTexte,textPaintTableauAffichage);
            // Score Total
            canvas.drawText(textScoreTotal+" : "+comptageScoreFinal,cvW/4+tailleTexte,cvH/2+2*(tailleTexte+tailleTexte)-espaceTexte,textPaintTableauAffichage);


            if(restartEnable){
                TextPaint textPaintRang = new TextPaint();
                textPaintRang.setTextSize(tailleTexte);
                textPaintRang.setColor(ContextCompat.getColor(this.getContext(), R.color.accent_material_dark_1));

                if(rangScore!=0) {
                    if (rangScore == 1) {
                        canvas.drawText(rangScore + "er", cvW / 2 + 3 * tailleTexte, cvH / 2 + 2 * (tailleTexte + tailleTexte), textPaintRang);
                    } else {
                        canvas.drawText(rangScore + "ème", cvW / 2 + 3 * tailleTexte, cvH / 2 + 2 * (tailleTexte + tailleTexte), textPaintRang);
                    }
                }



                coinGRestart=cvW/2;
                coinHRestart=cvH/2+3*(tailleTexte+tailleTexte);
                coinBRestart=cvH/2+3*(tailleTexte+tailleTexte)+tailleTexte;
                coinDRestart=cvW/2+4*tailleTexte;

                TextPaint textPaintRestart = new TextPaint();
                textPaintRestart.setTextSize(tailleTexte);
                textPaintRestart.setColor(Color.RED);
                // Distance
                canvas.drawText(textRestart,coinGRestart,coinHRestart,textPaintRestart);

            }
        }

    }

    // Fonction appelée par la boucle principale (gameLoopThread)
    // On gère ici le déplacement des objets
    public void update() {

        if(bg!=null) {
            bg.update();
        }

        if(nbVie==0){
            bg.setVector(0);
        }

        if(start) {
            if(!pause) {
                if(!perdu) {
                    refreshScore++;
                }
            }
            if (refreshScore == 10) {
                score++;

                if(userInfo!=null) {
                    if (score > userInfo.getHighScore() && userInfo.getHighScore()!=-1) {
                        if (!toastHSdone) {
                            Game.toastHS();
                            toastHSdone = true;
                        }
                    }
                }


                baisseCarburant = false;
                refreshScore = 0;
            }

            if (score % 5 == 0) {
                if (!baisseCarburant) {
                    if (levelCarburant - PERTECARBURANT * longueurBarreCarburant / 100 > levelCarburantMinInit) {
                        levelCarburant = levelCarburant - PERTECARBURANT * longueurBarreCarburant / 100;
                    } else {
                        perdu=true;
                    }
                    baisseCarburant = true;
                    Game.pullHighScore();
                }
            }
        }

        if(pause || perdu || collisionVehicule){
            carburant.setMove(false);
            vehiculeEnnemi1.setMove(false);
            vehiculeEnnemi2.setMove(false);
            vehiculeEnnemi3.setMove(false);
            vehiculeEnnemi4.setMove(false);
            vehiculeEnnemi5.setMove(false);
            vehiculeEnnemi6.setMove(false);
            if(bg!=null) {
                bg.setMove(false);
            }
        } else {
            carburant.setMove(true);
            if(!vehiculeDispo[0]) {
                vehiculeEnnemi1.setMove(true);
            }
            if(!vehiculeDispo[1]) {
                vehiculeEnnemi2.setMove(true);
            }
            if(!vehiculeDispo[2]) {
                vehiculeEnnemi3.setMove(true);
            }
            if(!vehiculeDispo[3]) {
                vehiculeEnnemi4.setMove(true);
            }
            if(!vehiculeDispo[4]) {
                vehiculeEnnemi5.setMove(true);
            }
            if(!vehiculeDispo[5]) {
                vehiculeEnnemi6.setMove(true);
            }

            if(bg!=null) {
                bg.setMove(true);
            }
        }


        carburant.move();

        if(vehicule.hasBeenTouched(carburant.getX(),carburant.getY(),carburant.getcarburantW(),carburant.getcarburantH())){
            carburant.disparition();
            if(levelCarburant + GAINCARBURANT * longueurBarreCarburant / 100<levelCarburantMaxInit) {
                levelCarburant = levelCarburant + GAINCARBURANT * longueurBarreCarburant / 100;
            } else {
                levelCarburant=levelCarburantMaxInit;
            }
        }

        // Gestion ennemi
        // Disposition


        for (int i = 0; i < stratEnnemiDone.length; i++) {
            if (!stratEnnemiDone[i]) {
                randomStrat = (int) (Math.random() * (100 + 1));
                if (randomStrat < 25) {
                    stratEnnemi[i] = 0;
                } else {
                    if (randomStrat < 50) {
                        stratEnnemi[i] = 0; //1
                    } else {
                        if (randomStrat < 75) {
                            stratEnnemi[i] = 1;//2
                        } else {
                            stratEnnemi[i] = 1;//3
                        }
                    }
                }
                stratEnnemiDone[i] = true;
                Log.d("StratEnnemi["+i+"]",Integer.toString(stratEnnemi[i]));
            }
        }


        if(newColonne) {
            switch (stratEnnemi[colonneEnnemiAVenir]) {
                //NBCASE = NBSTRATEGIES
                case 0:
                    int cpt = 0;
                    for(int i =0 ; i<vehiculeDispo.length;i++){
                        if(vehiculeDispo[i]){
                            cpt=i;
                        }
                    }

                    vehiculeDispo[cpt]=false;

                    Log.d("vehiculeDispoStr1Numero",Integer.toString(cpt));
                    switch (cpt) {
                        // NB CASE = NB VEHICULES
                        case 0:
                            highestEnnemi=1;
                            distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                            if (distanceEntreVehiculesRand < 25) {
                                vehiculeEnnemi1.setY(distanceEntreVehicules1);
                            } else {
                                if (distanceEntreVehiculesRand < 50) {
                                    vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                } else {
                                    if (distanceEntreVehiculesRand < 75) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                    } else {
                                        vehiculeEnnemi1.setY(-3 * vehicule.getvehiculePlayerH());
                                    }
                                }
                            }

                            positionnementV1X = (int) (Math.random() * (100 + 1));

                            if (positionnementV1X < 33) {
                                vehiculeEnnemi1.setX(routeB);
                            } else {
                                if (positionnementV1X < 66) {
                                    vehiculeEnnemi1.setX(routeM);
                                } else {
                                    vehiculeEnnemi1.setX(routeH);
                                }
                            }

                            vehiculeEnnemi1.setMove(true);

                            break;
                        case 1:
                            highestEnnemi=2;
                            distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                            if (distanceEntreVehiculesRand < 25) {
                                vehiculeEnnemi2.setY(distanceEntreVehicules1);
                            } else {
                                if (distanceEntreVehiculesRand < 50) {
                                    vehiculeEnnemi2.setY(distanceEntreVehicules2 );
                                } else {
                                    if (distanceEntreVehiculesRand < 75) {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                    } else {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules4 );
                                    }
                                }
                            }

                            positionnementV1X = (int) (Math.random() * (100 + 1));

                            if (positionnementV1X < 33) {
                                vehiculeEnnemi2.setX(routeB);
                            } else {
                                if (positionnementV1X < 66) {
                                    vehiculeEnnemi2.setX(routeM);
                                } else {
                                    vehiculeEnnemi2.setX(routeH);
                                }
                            }
                            vehiculeEnnemi2.setMove(true);
                            break;
                        case 2:
                            highestEnnemi=3;
                            distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                            if (distanceEntreVehiculesRand < 25) {
                                vehiculeEnnemi3.setY(distanceEntreVehicules1);
                            } else {
                                if (distanceEntreVehiculesRand < 50) {
                                    vehiculeEnnemi3.setY(distanceEntreVehicules2 );
                                } else {
                                    if (distanceEntreVehiculesRand < 75) {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                    } else {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules4 );
                                    }
                                }
                            }

                            positionnementV1X = (int) (Math.random() * (100 + 1));

                            if (positionnementV1X < 33) {
                                vehiculeEnnemi3.setX(routeB);
                            } else {
                                if (positionnementV1X < 66) {
                                    vehiculeEnnemi3.setX(routeM);
                                } else {
                                    vehiculeEnnemi3.setX(routeH);
                                }
                            }
                            vehiculeEnnemi3.setMove(true);
                            break;
                        case 3:
                            highestEnnemi=4;
                            distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                            if (distanceEntreVehiculesRand < 25) {
                                vehiculeEnnemi4.setY(-vehicule.getvehiculePlayerH());
                            } else {
                                if (distanceEntreVehiculesRand < 50) {
                                    vehiculeEnnemi4.setY(distanceEntreVehicules2 );
                                } else {
                                    if (distanceEntreVehiculesRand < 75) {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                    } else {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules4 );
                                    }
                                }
                            }

                            positionnementV1X = (int) (Math.random() * (100 + 1));

                            if (positionnementV1X < 33) {
                                vehiculeEnnemi4.setX(routeB);
                            } else {
                                if (positionnementV1X < 66) {
                                    vehiculeEnnemi4.setX(routeM);
                                } else {
                                    vehiculeEnnemi4.setX(routeH);
                                }
                            }
                            vehiculeEnnemi4.setMove(true);
                            break;
                        case 4:
                            highestEnnemi=5;
                            distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                            if (distanceEntreVehiculesRand < 25) {
                                vehiculeEnnemi5.setY(-vehicule.getvehiculePlayerH());
                            } else {
                                if (distanceEntreVehiculesRand < 50) {
                                    vehiculeEnnemi5.setY(distanceEntreVehicules2 );
                                } else {
                                    if (distanceEntreVehiculesRand < 75) {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                    } else {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                    }
                                }
                            }

                            positionnementV1X = (int) (Math.random() * (100 + 1));

                            if (positionnementV1X < 33) {
                                vehiculeEnnemi5.setX(routeB);
                            } else {
                                if (positionnementV1X < 66) {
                                    vehiculeEnnemi5.setX(routeM);
                                } else {
                                    vehiculeEnnemi5.setX(routeH);
                                }
                            }
                            vehiculeEnnemi5.setMove(true);
                            break;
                        case 5:
                            highestEnnemi=6;
                            distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                            if (distanceEntreVehiculesRand < 25) {
                                vehiculeEnnemi6.setY(-vehicule.getvehiculePlayerH());
                            } else {
                                if (distanceEntreVehiculesRand < 50) {
                                    vehiculeEnnemi6.setY(distanceEntreVehicules2 );
                                } else {
                                    if (distanceEntreVehiculesRand < 75) {
                                        vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                    } else {
                                        vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                    }
                                }
                            }

                            positionnementV1X = (int) (Math.random() * (100 + 1));

                            if (positionnementV1X < 33) {
                                vehiculeEnnemi6.setX(routeB);
                            } else {
                                if (positionnementV1X < 66) {
                                    vehiculeEnnemi6.setX(routeM);
                                } else {
                                    vehiculeEnnemi6.setX(routeH);
                                }
                            }
                            vehiculeEnnemi6.setMove(true);
                            break;
                    }
                    break;
                case 1:
                    // Choix 1ervehicule
                    int cpt1=0;
                    int cpt2=0;
                    for(int i =0 ; i<vehiculeDispo.length;i++){
                        if(vehiculeDispo[i]){
                            cpt1=i;
                        }
                    }

                    vehiculeDispo[cpt1]=false;

                    switch (cpt1){
                        case 0:
                            highestEnnemi=1;
                            // choix 2ème véhicule
                            for(int i =0 ; i<vehiculeDispo.length;i++){
                                if(vehiculeDispo[i]){
                                    cpt2=i;
                                }
                            }
                            vehiculeDispo[cpt2]=false;

                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt1));
                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt2));

                            switch (cpt2){
                                case 0:
                                    // Vehicule 1 & 1 !

                                    break;
                                case 1:
                                    // Vehicule 1 & 2 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi1.setX(routeB);
                                        vehiculeEnnemi2.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi2.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi2.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi1.setMove(true);
                                    vehiculeEnnemi2.setMove(true);

                                    break;
                                case 2:
                                    // Vehicule 1 & 3 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi1.setX(routeB);
                                        vehiculeEnnemi3.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi3.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi3.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi1.setMove(true);
                                    vehiculeEnnemi3.setMove(true);


                                    break;
                                case 3:
                                    // Vehicule 1 & 4 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi1.setX(routeB);
                                        vehiculeEnnemi4.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi4.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi4.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi1.setMove(true);
                                    vehiculeEnnemi4.setMove(true);

                                    break;
                                case 4:
                                    // Vehicule 1 & 5 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi1.setX(routeB);
                                        vehiculeEnnemi5.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi5.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi5.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi1.setMove(true);
                                    vehiculeEnnemi5.setMove(true);

                                    break;
                                case 5:
                                    // Vehicule 1 & 1 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi1.setX(routeB);
                                        vehiculeEnnemi6.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi6.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi6.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi1.setMove(true);
                                    vehiculeEnnemi6.setMove(true);

                                    break;

                            }
                            break;
                        case 1:
                            highestEnnemi=2;
                            // choix 2ème véhicule
                            for(int i =0 ; i<vehiculeDispo.length;i++){
                                if(vehiculeDispo[i]){
                                    cpt2=i;
                                }
                            }
                            vehiculeDispo[cpt2]=false;
                            switch (cpt2){
                                case 0:

                                    // Vehicule 2 & 1 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi2.setX(routeB);
                                        vehiculeEnnemi1.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi1.setX(routeB);
                                        } else {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi1.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi2.setMove(true);
                                    vehiculeEnnemi1.setMove(true);


                                    break;
                                case 1:
                                    // Vehicule 2 & 2 !


                                    break;
                                case 2:
                                    // Vehicule 2 & 3 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi2.setX(routeB);
                                        vehiculeEnnemi3.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi3.setX(routeB);
                                        } else {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi3.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi2.setMove(true);
                                    vehiculeEnnemi3.setMove(true);


                                    break;
                                case 3:
                                    // Vehicule 2 & 4 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi2.setX(routeB);
                                        vehiculeEnnemi4.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi4.setX(routeB);
                                        } else {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi4.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi2.setMove(true);
                                    vehiculeEnnemi4.setMove(true);

                                    break;
                                case 4:
                                    // Vehicule 2 & 5 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi2.setX(routeB);
                                        vehiculeEnnemi5.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi5.setX(routeB);
                                        } else {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi5.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi2.setMove(true);
                                    vehiculeEnnemi5.setMove(true);

                                    break;
                                case 5:
                                    // Vehicule 2 & 6 !

                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi2.setX(routeB);
                                        vehiculeEnnemi6.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi6.setX(routeB);
                                        } else {
                                            vehiculeEnnemi2.setX(routeH);
                                            vehiculeEnnemi6.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi2.setMove(true);
                                    vehiculeEnnemi6.setMove(true);

                                    break;

                            }
                            break;
                        case 2:
                            highestEnnemi=3;
                            // choix 2ème véhicule
                            for(int i =0 ; i<vehiculeDispo.length;i++){
                                if(vehiculeDispo[i]){
                                    cpt2=i;
                                }
                            }
                            vehiculeDispo[cpt2]=false;

                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt1));
                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt2));

                            switch (cpt2){
                                case 0:
                                    // Vehicule 3 & 1 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi3.setX(routeB);
                                        vehiculeEnnemi1.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi1.setX(routeB);
                                        } else {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi1.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi3.setMove(true);
                                    vehiculeEnnemi1.setMove(true);


                                    break;
                                case 1:
                                    // Vehicule 3 & 2 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi3.setX(routeB);
                                        vehiculeEnnemi2.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi2.setX(routeB);
                                        } else {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi2.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi3.setMove(true);
                                    vehiculeEnnemi2.setMove(true);

                                    break;
                                case 2:
                                    // Vehicule 3 & 3 !

                                    break;
                                case 3:
                                    // Vehicule 3 & 4 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi3.setX(routeB);
                                        vehiculeEnnemi4.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi4.setX(routeB);
                                        } else {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi4.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi3.setMove(true);
                                    vehiculeEnnemi4.setMove(true);

                                    break;
                                case 4:
                                    // Vehicule 3 & 5 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi3.setX(routeB);
                                        vehiculeEnnemi5.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi5.setX(routeB);
                                        } else {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi5.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi3.setMove(true);
                                    vehiculeEnnemi5.setMove(true);

                                    break;
                                case 5:
                                    // Vehicule 3 & 6 !

                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi3.setX(routeB);
                                        vehiculeEnnemi6.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi6.setX(routeB);
                                        } else {
                                            vehiculeEnnemi3.setX(routeH);
                                            vehiculeEnnemi6.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi3.setMove(true);
                                    vehiculeEnnemi6.setMove(true);

                                    break;

                            }



                            break;
                        case 3:
                            highestEnnemi=4;
                            // choix 2ème véhicule
                            for(int i =0 ; i<vehiculeDispo.length;i++){
                                if(vehiculeDispo[i]){
                                    cpt2=i;
                                }
                            }
                            vehiculeDispo[cpt2]=false;

                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt1));
                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt2));

                            switch (cpt2){
                                case 0:
                                    // Vehicule 4 & 1 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi4.setX(routeB);
                                        vehiculeEnnemi1.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi1.setX(routeB);
                                        } else {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi1.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi4.setMove(true);
                                    vehiculeEnnemi1.setMove(true);


                                    break;
                                case 1:
                                    // Vehicule 4 & 2 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi4.setX(routeB);
                                        vehiculeEnnemi2.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi2.setX(routeB);
                                        } else {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi2.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi4.setMove(true);
                                    vehiculeEnnemi2.setMove(true);

                                    break;
                                case 2:
                                    // Vehicule 4 & 3 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi4.setX(routeB);
                                        vehiculeEnnemi3.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi3.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi3.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi4.setMove(true);
                                    vehiculeEnnemi3.setMove(true);


                                    break;
                                case 3:
                                    // Vehicule 4 & 4 !


                                    break;
                                case 4:
                                    // Vehicule 4 & 5 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi4.setX(routeB);
                                        vehiculeEnnemi5.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi5.setX(routeB);
                                        } else {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi5.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi4.setMove(true);
                                    vehiculeEnnemi5.setMove(true);

                                    break;
                                case 5:
                                    // Vehicule 5 & 6 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi4.setX(routeB);
                                        vehiculeEnnemi6.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi6.setX(routeB);
                                        } else {
                                            vehiculeEnnemi4.setX(routeH);
                                            vehiculeEnnemi6.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi4.setMove(true);
                                    vehiculeEnnemi6.setMove(true);

                                    break;

                            }


                            break;
                        case 4:
                            highestEnnemi=5;
                            // choix 2ème véhicule
                            for(int i =0 ; i<vehiculeDispo.length;i++){
                                if(vehiculeDispo[i]){
                                    cpt2=i;
                                }
                            }
                            vehiculeDispo[cpt2]=false;

                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt1));
                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt2));

                            switch (cpt2){
                                case 0:
                                    // Vehicule 5 & 1 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi5.setX(routeB);
                                        vehiculeEnnemi1.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi1.setX(routeB);
                                        } else {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi1.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi5.setMove(true);
                                    vehiculeEnnemi1.setMove(true);


                                    break;
                                case 1:
                                    // Vehicule 5 & 2 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi5.setX(routeB);
                                        vehiculeEnnemi2.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi2.setX(routeB);
                                        } else {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi2.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi5.setMove(true);
                                    vehiculeEnnemi2.setMove(true);

                                    break;
                                case 2:
                                    // Vehicule 5 & 3 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi5.setX(routeB);
                                        vehiculeEnnemi3.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi3.setX(routeB);
                                        } else {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi3.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi5.setMove(true);
                                    vehiculeEnnemi3.setMove(true);


                                    break;
                                case 3:
                                    // Vehicule 5 & 4 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi5.setX(routeB);
                                        vehiculeEnnemi4.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi4.setX(routeB);
                                        } else {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi4.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi5.setMove(true);
                                    vehiculeEnnemi4.setMove(true);

                                    break;
                                case 4:
                                    // Vehicule 5 & 5 !


                                    break;
                                case 5:
                                    // Vehicule 5 & 6 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi5.setX(routeB);
                                        vehiculeEnnemi6.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi6.setX(routeB);
                                        } else {
                                            vehiculeEnnemi5.setX(routeH);
                                            vehiculeEnnemi6.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi5.setMove(true);
                                    vehiculeEnnemi6.setMove(true);

                                    break;

                            }

                            break;
                        case 5:
                            highestEnnemi=6;
                            // choix 2ème véhicule
                            for(int i =0 ; i<vehiculeDispo.length;i++){
                                if(vehiculeDispo[i]){
                                    cpt2=i;
                                }
                            }
                            vehiculeDispo[cpt2]=false;

                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt1));
                            Log.d("vehiculeDispoStr2Numero",Integer.toString(cpt2));

                            switch (cpt2){
                                case 0:
                                    // Vehicule 6 & 1 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi1.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi1.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi1.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi1.setX(routeB);
                                        vehiculeEnnemi6.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi6.setX(routeB);
                                        } else {
                                            vehiculeEnnemi1.setX(routeH);
                                            vehiculeEnnemi1.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi1.setMove(true);
                                    vehiculeEnnemi6.setMove(true);


                                    break;
                                case 1:
                                    // Vehicule 6 & 2 !

                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi2.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi2.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi2.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi6.setX(routeB);
                                        vehiculeEnnemi2.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi2.setX(routeB);
                                        } else {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi2.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi6.setMove(true);
                                    vehiculeEnnemi2.setMove(true);

                                    break;
                                case 2:
                                    // Vehicule 6 & 3 !

                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi3.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi3.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi3.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi6.setX(routeB);
                                        vehiculeEnnemi3.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi3.setX(routeB);
                                        } else {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi3.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi6.setMove(true);
                                    vehiculeEnnemi3.setMove(true);


                                    break;
                                case 3:
                                    // Vehicule 6 & 4 !
                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi4.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi4.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi4.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi6.setX(routeB);
                                        vehiculeEnnemi4.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi4.setX(routeB);
                                        } else {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi4.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi6.setMove(true);
                                    vehiculeEnnemi4.setMove(true);

                                    break;
                                case 4:
                                    // Vehicule 6 & 5 !

                                    distanceEntreVehiculesRand = (int) (Math.random() * (100 + 1));

                                    if (distanceEntreVehiculesRand < 25) {
                                        vehiculeEnnemi6.setY(distanceEntreVehicules1);
                                        vehiculeEnnemi5.setY(distanceEntreVehicules1);
                                    } else {
                                        if (distanceEntreVehiculesRand < 50) {
                                            vehiculeEnnemi6.setY(distanceEntreVehicules2);
                                            vehiculeEnnemi5.setY(distanceEntreVehicules2);
                                        } else {
                                            if (distanceEntreVehiculesRand < 75) {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules3);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules3);
                                            } else {
                                                vehiculeEnnemi6.setY(distanceEntreVehicules4);
                                                vehiculeEnnemi5.setY(distanceEntreVehicules4);
                                            }
                                        }
                                    }

                                    positionnementV1X = (int) (Math.random() * (100 + 1));

                                    if (positionnementV1X < 33) {
                                        vehiculeEnnemi6.setX(routeB);
                                        vehiculeEnnemi5.setX(routeM);

                                    } else {
                                        if (positionnementV1X < 66) {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi5.setX(routeB);
                                        } else {
                                            vehiculeEnnemi6.setX(routeH);
                                            vehiculeEnnemi5.setX(routeM);

                                        }
                                    }

                                    vehiculeEnnemi6.setMove(true);
                                    vehiculeEnnemi5.setMove(true);

                                    break;
                                case 5:
                                    // Vehicule 6 & 6 !
                                    break;

                            }


                            break;

                    }
                    break;
                case 2:

                    break;
                case 3:

                    break;
                default:
                    break;
            }
            stratEnnemiDone[colonneEnnemiAVenir]=false;
            newColonne=false;
        }


        switch (highestEnnemi){
            case 1:
                if(vehiculeEnnemi1.getY()>vehiculeEnnemi1.getvehiculeEnnemiH()){
                    newColonne=true;
                    colonneEnnemiAVenir++;
                }
                break;
            case 2:
                if(vehiculeEnnemi2.getY()>vehiculeEnnemi2.getvehiculeEnnemiH()){
                    newColonne=true;
                    colonneEnnemiAVenir++;
                }
                break;
            case 3:
                if(vehiculeEnnemi3.getY()>vehiculeEnnemi3.getvehiculeEnnemiH()){
                    newColonne=true;
                    colonneEnnemiAVenir++;
                }
                break;
            case 4:
                if(vehiculeEnnemi4.getY()>vehiculeEnnemi4.getvehiculeEnnemiH()){
                    newColonne=true;
                    colonneEnnemiAVenir++;
                }
                break;
            case 5:
                if(vehiculeEnnemi5.getY()>vehiculeEnnemi5.getvehiculeEnnemiH()){
                    newColonne=true;
                    colonneEnnemiAVenir++;
                }
                break;
            case 6:
                if(vehiculeEnnemi6.getY()>vehiculeEnnemi6.getvehiculeEnnemiH()){
                    newColonne=true;
                    colonneEnnemiAVenir++;
                }
                break;
            default:
                break;
        }

        if(colonneEnnemiAVenir==NBENNEMIPARCOLONNEMAX){
            colonneEnnemiAVenir=0;
        }


        if(vehiculeEnnemi1.getY()>cvH){
            nbVehicules++;
            comptageVehiculeDone=true;
            vehiculeEnnemi1.setY(-2*vehiculeEnnemi1.getvehiculeEnnemiH());
            vehiculeEnnemi1.setMove(false);
            vehiculeDispo[0]=true;
        }
        if(vehiculeEnnemi2.getY()>cvH){
            nbVehicules++;
            comptageVehiculeDone=true;
            vehiculeEnnemi2.setY(-2*vehiculeEnnemi2.getvehiculeEnnemiH());
            vehiculeEnnemi2.setMove(false);
            vehiculeDispo[1]=true;
        }
        if(vehiculeEnnemi3.getY()>cvH){
            nbVehicules++;
            comptageVehiculeDone=true;
            vehiculeEnnemi3.setY(-2*vehiculeEnnemi3.getvehiculeEnnemiH());
            vehiculeEnnemi3.setMove(false);
            vehiculeDispo[2]=true;
        }
        if(vehiculeEnnemi4.getY()>cvH){
            nbVehicules++;
            comptageVehiculeDone=true;
            vehiculeEnnemi4.setY(-2*vehiculeEnnemi4.getvehiculeEnnemiH());
            vehiculeEnnemi4.setMove(false);
            vehiculeDispo[3]=true;
        }
        if(vehiculeEnnemi5.getY()>cvH){
            nbVehicules++;
            comptageVehiculeDone=true;
            vehiculeEnnemi5.setY(-2*vehiculeEnnemi5.getvehiculeEnnemiH());
            vehiculeEnnemi5.setMove(false);
            vehiculeDispo[4]=true;
        }
        if(vehiculeEnnemi6.getY()>cvH){
            nbVehicules++;
            comptageVehiculeDone=true;
            vehiculeEnnemi6.setY(-2*vehiculeEnnemi6.getvehiculeEnnemiH());
            vehiculeEnnemi6.setMove(false);
            vehiculeDispo[5]=true;
        }

        vehiculeEnnemi1.moveHautBas();
        vehiculeEnnemi2.moveHautBas();
        vehiculeEnnemi3.moveHautBas();
        vehiculeEnnemi4.moveHautBas();
        vehiculeEnnemi5.moveHautBas();
        vehiculeEnnemi6.moveHautBas();

        if(vehicule.hasBeenTouched(vehiculeEnnemi1.getX(),vehiculeEnnemi1.getY(),vehiculeEnnemi1.getvehiculeEnnemiW(),vehiculeEnnemi1.getvehiculeEnnemiH())){
            collisionVehicule=true;
        }
        if(vehicule.hasBeenTouched(vehiculeEnnemi2.getX(),vehiculeEnnemi1.getY(),vehiculeEnnemi2.getvehiculeEnnemiW(),vehiculeEnnemi2.getvehiculeEnnemiH())){
            collisionVehicule=true;
        }
        if(vehicule.hasBeenTouched(vehiculeEnnemi3.getX(),vehiculeEnnemi3.getY(),vehiculeEnnemi3.getvehiculeEnnemiW(),vehiculeEnnemi3.getvehiculeEnnemiH())){
            collisionVehicule=true;
        }
        if(vehicule.hasBeenTouched(vehiculeEnnemi4.getX(),vehiculeEnnemi4.getY(),vehiculeEnnemi4.getvehiculeEnnemiW(),vehiculeEnnemi4.getvehiculeEnnemiH())){
            collisionVehicule=true;
        }
        if(vehicule.hasBeenTouched(vehiculeEnnemi5.getX(),vehiculeEnnemi5.getY(),vehiculeEnnemi5.getvehiculeEnnemiW(),vehiculeEnnemi5.getvehiculeEnnemiH())){
            collisionVehicule=true;
        }
        if(vehicule.hasBeenTouched(vehiculeEnnemi6.getX(),vehiculeEnnemi6.getY(),vehiculeEnnemi6.getvehiculeEnnemiW(),vehiculeEnnemi6.getvehiculeEnnemiH())){
            collisionVehicule=true;
        }


//        if (diffToAdd - bg.getX() < 0) {
//            diffToAdd = diffToAdd - bg.getX();
//        }
//        //Log.d("diffToAdd",Integer.toString(diffToAdd));
//        distancebackground = distancebackground + diffToAdd;
//        //Log.d("distancebg",Integer.toString(distancebackground));
//        diffToAdd = bg.getX();



    }

    // Fonction obligatoire de l'objet SurfaceView
    // Fonction appelée immédiatement après la création de l'objet SurfaceView
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // création du processus GameLoopThread si cela n'est pas fait
//        Bitmap bgBm = BitmapFactory.decodeResource(getResources(), R.drawable.espace3);
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bgBm, getWidth(), getHeight(), false);
//        WIDTHBG = resizedBitmap.getWidth();
//        bg = new SpaceBackground(resizedBitmap);
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.bg));;
        bg.setVector(0);

        if(gameLoopThread.getState()==Thread.State.TERMINATED) {
            gameLoopThread=new GameLoopThread(this);
        }
        gameLoopThread.setRunning(true);
        gameLoopThread.start();


    }

    // Fonction obligatoire de l'objet SurfaceView
    // Fonction appelée juste avant que l'objet ne soit détruit.
    // on tente ici de stopper le processus de gameLoopThread
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
                retry = false;
            }
            catch (InterruptedException e) {}
        }
    }

    // Gère les touchés sur l'écran
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currentX = (int)event.getX();
        int currentY = (int)event.getY();

        switch (event.getAction()) {

            // code exécuté lorsque le doigt touche l'écran.
            case MotionEvent.ACTION_DOWN:
                if(!start){
                    if(currentX>bordStartG && currentX<bordStartD && currentY<bordStartB && currentY>bordStartH) {
                        depart=true;
                        toastHSdone = false;
                    }
                } else {
                    if(!perdu) {
                        if(!collisionVehicule) {
                            if (currentX < coinDPause && currentX > coinGPause && currentY < coinBPause && currentY > coinHPause) {
                                if (!pause) {
                                    pause = true;
                                    Game.pauseMusique();
                                } else {
                                    pause = false;
                                    Game.startMusique();
                                }
                            } else {
                                if (currentX >= vehicule.getX() - vehicule.getvehiculePlayerW() / 8 && currentX <= vehicule.getX() + vehicule.getvehiculePlayerW() + vehicule.getvehiculePlayerW() / 8) {
                                    canMoveVehicule = true;
                                    distanceDoigtVoiture = currentX - vehicule.getX();
                                } else {
                                    canMoveVehicule = false;
                                }
                                break;
                            }
                        }
                    } else {
                        if(restartEnable) {
                            if (currentX < coinDRestart && currentX > coinGRestart && currentY> coinHRestart-tailleTexte && currentY<coinBRestart-tailleTexte){
                                bg=null;
                                ////alertDialogDone=false;
                                Game.restart();
                            }
                        }
                    }
                }



                // code exécuté lorsque le doight glisse sur l'écran.
            case MotionEvent.ACTION_MOVE:


                // Comportement normal
                //if(!pause) {
                if(!perdu) {
                    if (!collisionVehicule) {
                        if (currentX + vehicule.getvehiculePlayerW() / 2 < cvW - cvW / 8 && currentX - vehicule.getvehiculePlayerW() / 2 > cvW / 8) {
                            if (canMoveVehicule) {
                                vehicule.setX(currentX - distanceDoigtVoiture);
                            }
                        }
                    }
                }
                // }
                break;

            // lorsque le doigt quitte l'écran
            case MotionEvent.ACTION_UP:
                // on reprend le déplacement de la vehicule
                vehicule.setMove(false);
                canMoveVehicule=false;

        }

        return true;  // On retourne "true" pour indiquer qu'on a géré l'évènement
    }


    // Fonction obligatoire de l'objet SurfaceView
    // Fonction appelée à la CREATION et MODIFICATION et ONRESUME de l'écran
    // nous obtenons ici la largeur/hauteur de l'écran en pixels
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int w, int h) {
        vehicule.resize(w,h); // on définit la taille de la vehicule selon la taille de l'écran
        carburant.resize(w,h);
        vehiculeEnnemi1.resize(w,h);
        vehiculeEnnemi2.resize(w,h);
        vehiculeEnnemi3.resize(w,h);
        vehiculeEnnemi4.resize(w,h);
        vehiculeEnnemi5.resize(w,h);
        vehiculeEnnemi6.resize(w,h);

    }

} // class GameView

