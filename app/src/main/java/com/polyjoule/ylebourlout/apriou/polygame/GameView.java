package com.polyjoule.ylebourlout.apriou.polygame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.polyjoule.ylebourlout.apriou.polygame.Game.COEFVEHICULESEVITES;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.DEPLACEMENTBG;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.DUREEAFFICHAGEGO;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.GAINCARBURANT;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.PERTECARBURANT;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.RATIOTABLEAUSCORE;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.nbVie;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.rangJoueur;
import static com.polyjoule.ylebourlout.apriou.polygame.Game.users;
import static com.polyjoule.ylebourlout.apriou.polygame.Menu.userInfo;

/**
 * Created by Alexis on 19/07/2017.
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    // déclaration de l'objet définissant la boucle principale de déplacement et de rendu
    private GameLoopThread gameLoopThread;
    private VehiculePlayer vehicule;
    private Carburant carburant;
    private Boolean canMoveVehicule=false;
    private Boolean initialisation=false;
    public static int cvW; // canva width
    public static int cvH; // canva heigh
    public static int dureeExplo;
    private static int dureeClignotementOn=0;
    private static int dureeClignotementOff=0;
    public static int nbKilled=0;
    private int distancebackground=0;
    private int diffToAdd=0;
    private boolean touch=false;
    private Canvas canvasJeu;
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
    private int comptageScore=0;
    private int scorefinal=0;
    private Boolean restartEnable=false;
    private int distanceDoigtVoiture=0;

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


    }

    // Fonction qui "dessine" un écran de jeu
    public void doDraw(Canvas canvas) {
        if(canvas==null) {return;}
        canvasJeu=canvas;
        // Conv dp
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float dp = 30f; // 30 dp
        float dp2 = 50f;
        float dp3=10f;
        float dp4=20f;
        float dp5=14f;
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
        cvH = canvas.getHeight();
        cvW = canvas.getWidth();


        if(bg!=null) {
            bg.draw(canvas);
        }

        if(!initialisation){
            vehicule.setX(cvW/16);
            vehicule.setY((cvH/2)-vehicule.getvehiculePlayerH()/2);
            carburant.setX(cvW);
            coinGPause=cvW/2-cvW/64;
            coinHPause=pixels3;
            coinDPause=cvW/2+cvW/64;
            coinBPause=pixels3+pixels/2;
            levelCarburantMaxInit=cvW-cvW/40;
            levelCarburantMinInit=cvW-3*cvW/32;
            levelVieMaxInit=cvW-cvW/40;
            levelVieMinInit=cvW-3*cvW/32;
            levelCarburant=levelCarburantMaxInit;
            levelVie=levelVieMaxInit;
            longueurBarreCarburant=levelCarburant-(cvW-3*cvW/32);
            longueurBarreVie=levelVie-(cvW-3*cvW/32);
            if(dureeClignotementOn%15==0 || dureeClignotementOn==0) {
                // Cliquez pour commencer
                TextPaint textPaintStart = new TextPaint();
                textPaintStart.setTextSize(pixels4);
                textPaintStart.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
                canvas.drawText(textStart, 5*cvW / 16, 11 * cvH / 16, textPaintStart);
                if(dureeClignotementOff%20==0 && dureeClignotementOff!=0) {
                    dureeClignotementOn++;
                    dureeClignotementOff=0;
                } else {
                    dureeClignotementOff++;
                }
            } else {
                dureeClignotementOn++;
            }
            if(start) {
                carburant.setMove(true);
                initialisation = true;
                bg.setMove(true);
                bg.setVector(-DEPLACEMENTBG);
                dureeClignotementOn=0;
            }
        }

        // ScoreText
        TextPaint textPaintScore = new TextPaint();
        textPaintScore.setTextSize(pixels);
        textPaintScore.setColor(ContextCompat.getColor(this.getContext(),R.color.colorPrimaryDark));
        canvas.drawText(Integer.toString(score),pixels3,pixels,textPaintScore);

        // Level Carburant
        TextPaint paintlevelCarburant = new TextPaint();
        paintlevelCarburant.setTextSize(pixels);

        paintlevelCarburant.setColor(ContextCompat.getColor(this.getContext(),R.color.colorPrimaryDark));
        canvas.drawRect(levelCarburantMinInit,pixels3,levelCarburant,pixels3+pixels/2,paintlevelCarburant);

        // Image carburant
        Drawable carburantDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.carburant2);
        //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);
        carburantDraw.setBounds(levelCarburantMinInit-pixels2/5-carburant.getcarburantW()/2,pixels3,levelCarburantMinInit-pixels2/5,pixels3+pixels/2);
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
                    // Top 1
                    TextPaint textPaintClassement = new TextPaint();
                    textPaintClassement.setTextSize(pixels5);
                    textPaintClassement.setColor(ContextCompat.getColor(this.getContext(), R.color.primary_dark_material_dark_1));
                    canvas.drawText(classementText, pixels3, cvH - pixels3, textPaintClassement);


                    TextPaint textPaintTop1 = new TextPaint();
                    textPaintTop1.setTextSize(pixels5);
                    textPaintTop1.setColor(ContextCompat.getColor(this.getContext(), R.color.top1));
                    canvas.drawText(users.get(0).getPseudo() + " : " + users.get(0).highScore, 3*cvW/16, cvH - pixels3, textPaintTop1);

                }

            } else {
                if (users.size() == 2) {
                    if(users.get(0).getHighScore()!=-1) {
                        // Top 2
                        TextPaint textPaintClassement = new TextPaint();
                        textPaintClassement.setTextSize(pixels5);
                        textPaintClassement.setColor(ContextCompat.getColor(this.getContext(), R.color.primary_dark_material_dark_1));
                        canvas.drawText(classementText, pixels3, cvH - pixels3, textPaintClassement);

                        TextPaint textPaintTop1 = new TextPaint();
                        textPaintTop1.setTextSize(pixels5);
                        textPaintTop1.setColor(ContextCompat.getColor(this.getContext(), R.color.top1));
                        canvas.drawText(users.get(0).getPseudo() + " : " + users.get(0).highScore, 3*cvW/16, cvH - pixels3, textPaintTop1);

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
                        TextPaint textPaintClassement = new TextPaint();
                        textPaintClassement.setTextSize(pixels5);
                        textPaintClassement.setColor(ContextCompat.getColor(this.getContext(), R.color.primary_dark_material_dark_1));
                        canvas.drawText(classementText, pixels3, cvH - pixels3, textPaintClassement);

                        TextPaint textPaintTop1 = new TextPaint();
                        textPaintTop1.setTextSize(pixels5);
                        textPaintTop1.setColor(ContextCompat.getColor(this.getContext(), R.color.top1));
                        canvas.drawText(users.get(0).getPseudo() + " : " + users.get(0).highScore, 3*cvW/16, cvH - pixels3, textPaintTop1);

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





//        if(hasBeenTouched){
//            if(aste1a.getNombrePassage()<dureeClignotementOn+1) {
//                Drawable explo = ContextCompat.getDrawable(this.getContext(), R.drawable.explo);
//                explo.setBounds(vehicule.getX(),vehicule.getY(),vehicule.getX()+vehicule.getvehiculePlayerW(),vehicule.getY()+vehicule.getvehiculePlayerH());
//                explo.draw(canvas);
//            }
//            if(aste1a.getNombrePassage()==dureeClignotementOn+1) {
//                if(nbVie>0) {
//                    Question9.updatePref();
//                }
//                hasBeenTouched=false;
//            }
//        }


        carburant.draw(canvas);
        vehicule.draw(canvas);

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
            canvas.drawText(textPause, cvW/2-cvW/16, cvH/2, textPaintPause);


            TextPaint textPaintHS = new TextPaint();
            textPaintHS.setTextSize(pixels4);
            textPaintHS.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));

            if(userInfo!=null) {
                if(userInfo.getHighScore()!=-1) {
                    canvas.drawText(textHS + userInfo.getHighScore(), cvW / 2 - 5 * cvW / 32, 3 * cvH / 4 - cvH / 32, textPaintHS);
                } else {
                    canvas.drawText(textHS + "0", cvW / 2 - 5 * cvW / 32, 3 * cvH / 4 - cvH / 32, textPaintHS);
                }
            }

        }


        if(perdu){
            Game.pauseMusique();

            // Image Game Over
            Drawable goDraw = ContextCompat.getDrawable(this.getContext(),R.drawable.finish);
            //d.setHotspot(canvas.getWidth()-2*pixels,canvas.getHeight()-pixels);


            int bordTableauGauche=cvW/16;
            int bordTableauDroit =15*cvW/16;
            int longueurTableau = bordTableauDroit - bordTableauGauche;
            double ratio = RATIOTABLEAUSCORE;

            int hauteurTableau = ((int) Math.round(longueurTableau/ratio));

            int bordTableauHaut=cvH/32;

            int bordTableauBas=bordTableauHaut+hauteurTableau;

            
            goDraw.setBounds(bordTableauGauche,bordTableauHaut,bordTableauDroit,bordTableauBas);
            goDraw.draw(canvas);

            // Texte tableau score
            scorefinal=score+(nbVehicules*COEFVEHICULESEVITES);

            if(comptageScore!=scorefinal) {
                comptageScore++;
            } else {
                restartEnable=true;
                if (userInfo != null) { // + registered//true
                    //Log.d("score",Integer.toString(score));
                    //Log.d("HighScore",Integer.toString(highScore));
                    if (score > userInfo.getHighScore()) {
                        Game.update(score); // update local
                        userInfo.setHighScore(score);
                        Game.saveUserInformation(userInfo);
                        Game.pullHighScore();
                    }
                }
            }

            int tailleTexte=pixels4;
            int espaceTexte=pixels5;
            TextPaint textPaintTableauAffichage = new TextPaint();
            textPaintTableauAffichage.setTextSize(tailleTexte);
            textPaintTableauAffichage.setColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimaryDark));
            // Distance
            canvas.drawText(textDistance+" : "+score,cvW/4+tailleTexte,cvH/2,textPaintTableauAffichage);
            // Collision évités
            canvas.drawText(textVoitureEvites+" : "+nbVehicules,cvW/4+tailleTexte,cvH/2+tailleTexte+espaceTexte,textPaintTableauAffichage);
            // Score Total
            canvas.drawText(textScoreTotal+" : "+comptageScore,cvW/4+tailleTexte,cvH/2+2*(tailleTexte+tailleTexte),textPaintTableauAffichage);


            if(restartEnable){
                TextPaint textPaintRang = new TextPaint();
                textPaintRang.setTextSize(tailleTexte);
                textPaintRang.setColor(ContextCompat.getColor(this.getContext(), R.color.accent_material_dark_1));

                if(rangJoueur!=0) {
                    if (rangJoueur == 1) {
                        canvas.drawText(rangJoueur + "er", cvW / 2 + 3 * tailleTexte, cvH / 2 + 2 * (tailleTexte + tailleTexte), textPaintRang);
                    } else {
                        canvas.drawText(rangJoueur + "ème", cvW / 2 + 3 * tailleTexte, cvH / 2 + 2 * (tailleTexte + tailleTexte), textPaintRang);
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

        if(pause || perdu){
            carburant.setMove(false);
            if(bg!=null) {
                bg.setMove(false);
            }
        } else {
            carburant.setMove(true);
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
                    start=true;
                    toastHSdone=false;
                    Game.startMusique();
                } else {
                    if(!perdu) {
                        if (currentX < coinDPause && currentX > coinGPause && currentY < coinBPause && currentY > coinHPause) {
                            if (!pause) {
                                pause = true;
                                Game.pauseMusique();
                            } else {
                                pause = false;
                                Game.startMusique();
                            }
                        } else {
                            if(currentY>= vehicule.getY() - vehicule.getvehiculePlayerH()/8 && currentY <= vehicule.getY()+vehicule.getvehiculePlayerH() + vehicule.getvehiculePlayerH()/8){
                                canMoveVehicule=true;
                                distanceDoigtVoiture=currentY-vehicule.getY();
                            } else {
                                canMoveVehicule=false;
                            }
                            break;
                        }

                    } else {
                        if(restartEnable) {
                            if (currentX < coinDRestart && currentX > coinGRestart && currentY> coinHRestart && currentY<coinBRestart){
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
                if (currentY + vehicule.getvehiculePlayerH() / 2 < cvH - cvH / 8 && currentY - vehicule.getvehiculePlayerH() / 2 > cvH / 8) {
                    if (canMoveVehicule) {
                        vehicule.setY(currentY  - distanceDoigtVoiture);
                    }
                }
                // }
                break;

            // lorsque le doigt quitte l'écran
            case MotionEvent.ACTION_UP:
                // on reprend le déplacement de la vehicule
                vehicule.setMove(false);
                touch=false;
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
    }

} // class GameView

