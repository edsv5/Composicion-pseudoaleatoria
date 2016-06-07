import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import ddf.minim.*; 
import ddf.minim.ugens.*; 
import ddf.minim.*; 
import ddf.minim.ugens.*; 
import ddf.minim.*; 
import ddf.minim.ugens.*; 
import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class arregloNotas extends PApplet {




// Indice del noise

float tx;

// Duraciones de las notas

float blanca = 2;
float negra = 1;
float corchea = 0.5f;

// Waveforms

Waveform sine = Waves.SINE;
Waveform triangle = Waves.TRIANGLE;
Waveform square = Waves.SQUARE;
Waveform phasor = Waves.PHASOR;
Waveform qtp = Waves.QUARTERPULSE;
Waveform saw = Waves.SAW;

// Frecuencias de las notas

float silencio = 0; // Se tiene la frecuencia 0 para poder a\u00f1adir silencios como notas
/*
float C2 = 65.41;
float D2 = 73.42;
float E2 = 82.41;
float F2 = 87.31;
float FS2 = 92.50;
float G2 = 98.00;
float A2 = 110.00;
float B2 = 123.47;

float C3 = 130.81;
float D3 = 146.83;
float E3 = 164.81;
float F3 = 174.61;
float G3 = 196.00;
float A3 = 220.00;
float B3 = 246.94;

float C4 = 261.63;
float D4 = 293.66;
float E4 = 329.63;
float F4 = 349.23;
float FS4 = 369.99;
float G4 = 392.00;
float A4 = 440.00;
float B4 = 493.88;

float C5 = 523.25;
float CS5 = 554.37;
float D5 = 587.33;
float E5 = 659.25;
float F5 = 698.46;
float G5 = 783.99;
float A5 = 880.00;
float B5 = 987.77;

*/

// Declaraci\u00f3n del Reproductor que contiene las rutinas para reproducir
// arreglos de Notas

Reproductor r;


public void setup() {

  tx = 0;
  background(255);
  
  r = new Reproductor();

  generarPiezaAcordes();
  /*
  Vector< Vector<Nota> > arreglo = new  Vector< Vector<Nota> >();
  Vector<Nota> Am = generarNextNotas(8, negra, 4, sine);

  Vector<Nota> melodia = new Vector<Nota>();
  melodia.addAll(Am);

  arreglo.add(melodia);


  r.reproducirVectores(arreglo, 100);
  */

}

public void draw() {


  r.displayB();

  //text("Frecuencia: " + o.wave.frequency.getLastValue(), 20, 20);
  //text("Amplitud: " + o.wave.amplitude.getLastValue(), 20, 40);
}

// Este m\u00e9todo deber\u00eda dar cada vez que se llama, la siguiente nota, dada una nota actual,
// de esta manera que exista alg\u00fan criterio de generaci\u00f3n de notas menos aleatorio.
/*
  Transiciones que se deben tomar en cuenta:

  <---------------------.---------------->
  I   II    III   IV    V   VI    VII   VIII
    <-.->        <-.---->  <-.     .->

  T\u00f3nica (I)

  La t\u00f3nica no tiene tendencia a moverse hacia ning\u00fan lado, ya resuelve.

  Grado II

  Su funci\u00f3n tonal es de Subdominante, por contener el cuarto
  grado de la escala. Tiende a moverse con la misma fuerza hacia la t\u00f3nica
  y hacia dominante.

  Grado III

  Su funci\u00f3n tonal es de T\u00f3nica. La tendencia de este acorde es moverse
  hacia el acorde el grado VI \u00f3 hacia el II \u00f3 IV. Es menos estable que el
  grado I aunque pertenezca a la funci\u00f3n tonal t\u00f3nica.

  Grado IV (Subdominante)

  Su funci\u00f3n tonal es de Subdominante y su tendencia es moverse con la
  misma fuerza hacia t\u00f3nica (I) o hacia dominante (V).La cadencia IV-I es de las
  m\u00e1s fuertes y se llama cadencia plagal.

  Los acordes pertenecientes a esta funci\u00f3n arm\u00f3nica piden una ligera
  resoluci\u00f3n en un acorde de funci\u00f3n arm\u00f3nica dominante y, en menor
  medida, en un acorde de funci\u00f3n arm\u00f3nica t\u00f3nica.

  Grado V (Dominante)

  El acorde que se forma es mayor. Su funci\u00f3n tonal es de Dominante.
  Su tendencia es moverse hacia t\u00f3nica. Es el acorde m\u00e1s importante
  de la tonalidad despu\u00e9s del acorde I.

  Grado VI

  Al tratarse del relativo menor, tiene cierta funci\u00f3n de t\u00f3nica.
  Su tendencia es a moverse hacia el II o el V.

  Grado VII

  El acorde que se forma es disminuido. Su funci\u00f3n tonal es de
  Dominante, por contener los grados IV y VII. Se trata del acorde
  m\u00e1s d\u00e9bil e inestable de la tonalidad.

  Grado VIII

  Aunque no es un acorde diat\u00f3nico, su uso es muy frecuente en
  progresiones diat\u00f3nicas. Se trata de un acorde mayor, con
  funci\u00f3n no diat\u00f3nica, pero como contiene el grado IV se le
  asigna cierta funci\u00f3n de subdominante.

*/

// Nos da la siguiente nota dada una nota actual y una octava

public Nota nextNota(Nota notaActual, Waveform wav, int octava){
  Nota notaSiguiente = new Nota(notaActual.duracion, silencio, wav); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cu\u00e1l grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  //print("GRADO: " + notaActual.grado);
  switch (notaActual.grado){
    case 0:
      println("El grado por alguna razon es 0, fuck you.");
      break;
    case 1: // En teoria, la tonica resuelve, pero para darle dinamismo, nos movemos con igual probabilidad de la tonica a cualquier otro grado
      if(randomNum < 0.15f){
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }else if(0.15f < randomNum && randomNum > 0.30f){
        notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
      }else if(0.30f < randomNum && randomNum > 0.45f){
        notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
      }else if(0.45f < randomNum && randomNum > 0.60f){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else if(0.60f < randomNum && randomNum > 0.75f){
        notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
      }else if(0.75f < randomNum && randomNum > 0.90f){
        notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
      }else if(0.90f < randomNum){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav); // Con 10% de probabilidad se queda donde esta
      }
    break;

    case 2: // Tiene 45% de probabilidad de moverse a la tonica y 45% de moverse a la dominante, 10% de quedarse donde esta
      if(randomNum < 0.45f){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
      }else if (0.45f < randomNum && randomNum < 0.90f){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }
    break;

    case 3: //La tendencia es moverse hacia el grado VI \u00f3 hacia el II \u00f3 IV..o a quedarse donde esta
      if(randomNum < 0.5f){ // 50% probabilidad de ir a grado VI
        notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
      }else if(0.5f < randomNum && randomNum < 0.70f){ // 25% de ir hacia grado II
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }else if(0.70f < randomNum && randomNum < 0.90f){ // 25% de ir hacia grado IV
        notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
      }
    break;
    // Grado IV
    // Su funci\u00f3n tonal es de Subdominante y su tendencia es moverse con la
    // misma fuerza hacia t\u00f3nica (I) o hacia dominante (V).La cadencia IV-I es de las
    // m\u00e1s fuertes y se llama cadencia plagal.

    case 4:
      if(randomNum < 0.45f){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav); // Cadencia plagal IV -> I
      }else if (0.45f < randomNum && randomNum < 0.90f ){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
      }
    break;

    case 5: // Se mueve con igual probabilidad hacia la tonica, o se queda donde esta
      if(randomNum < 0.5f){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }
    break;

    case 6: // Su tendencia es a moverse hacia el II o el V.
      if(randomNum < 0.45f){
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }else if (0.45f < randomNum && randomNum < 0.90f){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
      }
    break;

    case 7: // Se mueve hacia la tonica siempre para resolver en el siguiente tiempo

      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);

    break;
  }

  return notaSiguiente;

}

// Genera una nota semilla y numTiempos - 1 notas segun el criterio de generacion definido en nextNota

public Vector<Nota> generarNextNotas(int numTiempos, float duracionNotas, int octava, Waveform wav) {
  Nota notaSemilla;
  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea el vector que se va a devolver
  int randomNum = (int) random(1, 7);
  notaSemilla = new Nota(duracionNotas, randomNum , octava, wav);
  println("Nota semilla: " + notaSemilla.nota + " Grado " + notaSemilla.grado + " Duracion " + notaSemilla.duracion + " Octava " + octava);
  vecNotas.add(notaSemilla); // Anade primero la nota semilla
  Nota notaActual = notaSemilla;

  for(int i = 0 ; i < numTiempos - 1 ; i+= duracionNotas){
    notaActual = nextNota(notaActual, wav, octava); // Ahora la actual es la siguiente
    vecNotas.add(notaActual); // Anade al vector de notas
    println("Nota agregada: " + notaActual.nota + " Octava " + octava);
  }
  return vecNotas;
}


// Genera una pieza con unos acordes arpegiados sencillos

public void generarPiezaAcordes() {

  Vector< Vector<Nota> > arreglo = new  Vector< Vector<Nota> >();

  // Crea el vector de vectores de Notas que representa una "partitura"
  Vector<Nota> Am = generarNotas(8, negra, triangle, 4, true, 0);
  Vector<Nota> Bm = generarNotas(8, negra, triangle, 4, false, 0);
  Vector<Nota> Cm = generarNotas(8, negra, triangle, 4, true, 0);

  Vector<Nota> AAcordes = generarAcordes(8, sine, 3, 0);
  Vector<Nota> BAcordes = generarAcordes(8, sine, 3, 0);
  Vector<Nota> CAcordes = generarAcordes(8, sine, 3, 0);

  Vector<Nota> melodia = new Vector<Nota>();
  Vector<Nota> acomp = new Vector<Nota>();


  melodia.addAll(Am);
  melodia.addAll(Bm);
  melodia.addAll(Am);
  melodia.addAll(Cm);

  acomp.addAll(AAcordes);
  acomp.addAll(BAcordes);
  acomp.addAll(AAcordes);
  acomp.addAll(CAcordes);

  arreglo.add(melodia);
  arreglo.add(acomp);

  r.reproducirVectores(arreglo, 100);

}

// Genera una pieza tipo rond\u00f3, ABA CAC ABA

public void generarRondo() {

  Vector< Vector<Nota> > arreglo = new  Vector< Vector<Nota> >();

  Vector<Nota> Am = generarNotas(8, negra, triangle, 4, true, 0);
  Vector<Nota> Aa = generarNotas(8, blanca, sine, 3, true, 0);

  Vector<Nota> Bm = generarNotas(8, negra, triangle, 4, true, 0);
  Vector<Nota> Ba = generarNotas(8, blanca, sine, 3, true, 0);

  Vector<Nota> Cm = generarNotas(8, negra, triangle, 4, true, 0);
  Vector<Nota> Ca = generarNotas(8, blanca, sine, 3, true, 0);

  Vector<Nota> melodia = new Vector<Nota>();
  Vector<Nota> acomp = new Vector<Nota>();

  //////////////////// PIEZA A ////////////////////

  // Planteamiento

  melodia.addAll(Am);
  acomp.addAll(Aa);
  melodia.addAll(Bm);
  acomp.addAll(Ba);
  melodia.addAll(Am);
  acomp.addAll(Aa);

  // Digresi\u00f3n

  melodia.addAll(Cm);
  acomp.addAll(Ca);
  melodia.addAll(Am);
  acomp.addAll(Aa);
  melodia.addAll(Cm);
  acomp.addAll(Ca);

  // Replanteamiento (se hace una variaci\u00f3n en el acompa\u00f1amiento para que sea un poco diferente)

  melodia.addAll(Am);
  acomp.addAll(Ba);
  melodia.addAll(Bm);
  acomp.addAll(Aa);
  melodia.addAll(Am);
  acomp.addAll(Ca);

  // Agrega las dos partes al arreglo principal

  arreglo.add(melodia);
  arreglo.add(acomp);

  // Reproduce ambos vectores

  r.reproducirVectores(arreglo, 100); // Reproduce
}

/* Este m\u00e9todo reproduce directamente una serie de notas generadas al azar

 PAR\u00c1METROS:

 numTiempos: Cantidad de tiempos que se quieren generar
 duracionNotas: Duraci\u00f3n de las notas generadas (negra, blanca, etc.)
 wav: Waveform de las notas por generar
 octava: Rango de las notas que se van a generar, esto es para poder generar tanto
 melod\u00eda como acompa\u00f1amiento
 p: Si se quieren generar notas con Perlin noise o no
 offset: A partir de qu\u00e9 tiempo se quieren colocar las notas (en el caso de que no se quiera empezar a reproducir la parte de inmediato)

 EJEMPLO:

 arreglo.add(generarNotas(16, blanca, sine, 4, false, 4));

 Se generan 16 tiempos con un waveform de seno, sin Perlin Noise, es decir con distribuci\u00f3n uniforme y con
 4 silencios antes de empezar las notas

 NOTAS:

 Por ahora se va a requerir que la serie de notas termine en la t\u00f3nica, mediante, o dominante, pues estos grados dan
 una sensaci\u00f3n de resoluci\u00f3n mayor a los otros grados

 */

public Vector<Nota> generarNotas(int numTiempos, float duracionNotas, Waveform wav, int octava, boolean p, int offset) {

  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea un nuevo vector de notas
  int randomNum;
  println("---- NOTAS ----");
  println("N\u00famero de tiempos " + numTiempos);
  println("Duraci\u00f3n de las notas: " + duracionNotas);
  println("Octava: " + octava);
  println("P: " + p);

  // Primero se insertan los silencios del offset, si es que los hay

  int silenciosColocados = 0;
  for (int s = 0; s < offset; s ++) {
    vecNotas.addElement(new Nota(duracionNotas, silencio, wav)); // Se a\u00f1aden silencios de negra seg\u00fan el offset
    silenciosColocados++; // Se agreg\u00f3 1 silencio, se lleva esta cuenta para empezar a poner notas despu\u00e9s del \u00faltimo silencio colocado
  }

  println("Offset: " + silenciosColocados + " tiempos.");
  // (numTiempos + silenciosColocados) - 1 para que se decida al final la \u00faltima nota, para dar caracter de resoluci\u00f3n
  for (int i =  silenciosColocados; i < (numTiempos + silenciosColocados) - duracionNotas*2; i+=duracionNotas) { // Empieza a poner notas despu\u00e9s de los silencios

    // Si se pide una serie de notas en la octava 3, se generan n\u00fameros entre
    // 1 y 7, si se piden notas en la octava 4, se generan n\u00fameros entre 8 y 15

    if (octava == 3) {

      if (p) {
        randomNum = (int) map(noise(tx), 0, 1, 1, 7);
      } else {
        randomNum = (int) random(1, 7);
      }
    } else {
      if (p) {
        randomNum = (int) map(noise(tx), 0, 1, 8, 15);
      } else {
        randomNum = (int) random(8, 15);
      }
    }

    // Este case parece ser reglas
    // Encontrar un paradigma adecuado
    // Anadir talvez otras reglas de generacion de notas, contemplar las tendencias de los grados a moverse hacia otros grados

    // Generador de melod\u00edas
    switch(randomNum) {
    case 1:
      vecNotas.addElement(new Nota(duracionNotas, C3, wav));
      print("C3 ");
      break;
    case 2:
      vecNotas.addElement(new Nota(duracionNotas, D3, wav));
      print("D3 ");
      break;
    case 3:
      vecNotas.addElement(new Nota(duracionNotas, E3, wav));
      print("E3 ");
      break;
    case 4:
      vecNotas.addElement(new Nota(duracionNotas, F3, wav));
      print("F3 ");
      break;
    case 5:
      vecNotas.addElement(new Nota(duracionNotas, G3, wav));
      print("G3 ");
      break;
    case 6:
      vecNotas.addElement(new Nota(duracionNotas, A3, wav));
      print("A3 ");
      break;
    case 7:
      vecNotas.addElement(new Nota(duracionNotas, B3, wav));
      print("B3 ");
      break;
    case 8:
      vecNotas.addElement(new Nota(duracionNotas, C4, wav));
      print("C4 ");
      break;
    case 9:
      vecNotas.addElement(new Nota(duracionNotas, D4, wav));
      print("D4 ");
      break;
    case 10:
      vecNotas.addElement(new Nota(duracionNotas, E4, wav));
      print("E4 ");
      break;
    case 11:
      vecNotas.addElement(new Nota(duracionNotas, F4, wav));
      print("F4 ");
      break;
    case 12:
      vecNotas.addElement(new Nota(duracionNotas, G4, wav));
      print("G4 ");
      break;
    case 13:
      vecNotas.addElement(new Nota(duracionNotas, A4, wav));
      print("A4 ");
      break;
    case 14:
      vecNotas.addElement(new Nota(duracionNotas, B4, wav));
      print("B4 ");
      break;
    case 15:
      vecNotas.addElement(new Nota(duracionNotas, C5, wav));
      print("C5 ");
      break;
    }
    print("(" + randomNum + ") ");
    // Coloca los espacios que sean necesarios, ej: Si son notas blancas, coloca 1 silencio por cada nota que inserte, si son redondas coloca 3 por cada nota
    for (int j = 1; j < duracionNotas; j++ ) {
      vecNotas.addElement(new Nota(duracionNotas, silencio, wav));
    }

    // Aumenta el indice del noise

    tx += 0.5f;
  }

  // Se genera la nota de resoluci\u00f3n
  // Se trata de resolver con notas en el grado I, III, V o VIII
  // El *2 es para dar mayor caracter de resoluci\u00f3n tambi\u00e9n, coloca una nota del doble del tiempo

  int randomNumF = (int) random(1, 4);
  if (randomNumF == 1) {
    if (octava == 3) {
      vecNotas.addElement(new Nota(duracionNotas*2, C3, wav));
    } else {
      vecNotas.addElement(new Nota(duracionNotas*2, C4, wav));
    }
  } else if (randomNumF == 2) {
    if (octava == 3) {
      vecNotas.addElement(new Nota(duracionNotas*2, E3, wav));
    } else {
      vecNotas.addElement(new Nota(duracionNotas*2, E4, wav));
    }
  } else if (randomNumF == 3) {
    if (octava == 3) {
      vecNotas.addElement(new Nota(duracionNotas*2, G3, wav));
    } else {
      vecNotas.addElement(new Nota(duracionNotas*2, G4, wav));
    }
  } else if (randomNumF == 4) {
    if (octava == 3) {
      vecNotas.addElement(new Nota(duracionNotas*2, C4, wav));
    } else {
      vecNotas.addElement(new Nota(duracionNotas*2, C5, wav));
    }
  }

  // Fin del poner nota de resoluci\u00f3n

  // Coloca los silencios respectivos despu\u00e9s de la nota

  for (int j = 1; j < duracionNotas*2; j++ ) {
    vecNotas.addElement(new Nota(duracionNotas, silencio, wav));
  }

  // Fin de colocar silencios

  println();
  return vecNotas;
}

// Genera los acordes (conjuntos de 4 notas) segun la cantidad de compases ingresados
//
// generarAcordes(16, negra, sine, 3, false, 0) deber\u00eda generar 4 acordes, cada uno compuesto de 4 notas en la octava 3, con 0 silencios por delante
//
// Por ahora, s\u00f3lo genera acordes de notas negras

public Vector<Nota> generarAcordes(int numTiempos, Waveform wav, int octava, int offset) {
  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea un nuevo vector de notas
  // Primero se insertan los silencios del offset, si es que los hay
  int silenciosColocados = 0;
  for (int s = 0; s < offset; s ++) {
    vecNotas.addElement(new Nota(negra, silencio, wav)); // Se a\u00f1aden silencios de negra seg\u00fan el offset
    silenciosColocados++; // Se agreg\u00f3 1 silencio, se lleva esta cuenta para empezar a poner notas despu\u00e9s del \u00faltimo silencio colocado
  }
  // Fin de insertar silencios
  println("Offset: " + silenciosColocados + " tiempos.");
  // (numTiempos + silenciosColocados) - 1 para que se decida al final la \u00faltima nota, para dar caracter de resoluci\u00f3n
  // Genera n\u00famero aleatorio
  int randomNum;
  for (int i =  silenciosColocados; i < numTiempos + silenciosColocados; i += 4 ) { // Empieza a poner notas despu\u00e9s de los silencios

    if (octava == 3) { // octava 3
      randomNum = (int) random(1, 7);
    } else { // octava 4
      randomNum = (int) random(8, 15);
    }

    switch(randomNum) {
    case 1:
      vecNotas.addElement(new Nota(negra, C3, wav));
      vecNotas.addElement(new Nota(negra, E3, wav));
      vecNotas.addElement(new Nota(negra, G3, wav));
      vecNotas.addElement(new Nota(negra, E3, wav));
      print("Acorde C3M ");
      break;
    case 2:
      vecNotas.addElement(new Nota(negra, D3, wav));
      vecNotas.addElement(new Nota(negra, F3, wav));
      vecNotas.addElement(new Nota(negra, A3, wav));
      vecNotas.addElement(new Nota(negra, F3, wav));
      print("D3m ");
      break;
    case 3:
      vecNotas.addElement(new Nota(negra, E3, wav));
      vecNotas.addElement(new Nota(negra, G3, wav));
      vecNotas.addElement(new Nota(negra, B3, wav));
      vecNotas.addElement(new Nota(negra, G3, wav));
      print("E3m ");
      break;
    case 4:
      vecNotas.addElement(new Nota(negra, F3, wav));
      vecNotas.addElement(new Nota(negra, A3, wav));
      vecNotas.addElement(new Nota(negra, C4, wav));
      vecNotas.addElement(new Nota(negra, A3, wav));
      print("F3M ");
      break;
    case 5:
      vecNotas.addElement(new Nota(negra, G3, wav));
      vecNotas.addElement(new Nota(negra, B3, wav));
      vecNotas.addElement(new Nota(negra, D4, wav));
      vecNotas.addElement(new Nota(negra, B3, wav));
      print("G3M ");
      break;
    case 6:
      vecNotas.addElement(new Nota(negra, A3, wav));
      vecNotas.addElement(new Nota(negra, C4, wav));
      vecNotas.addElement(new Nota(negra, E4, wav));
      vecNotas.addElement(new Nota(negra, C4, wav));
      print("A3m ");
      break;
    case 7:
      vecNotas.addElement(new Nota(negra, B3, wav));
      vecNotas.addElement(new Nota(negra, D4, wav));
      vecNotas.addElement(new Nota(negra, F4, wav));
      vecNotas.addElement(new Nota(negra, D4, wav));
      print("B3dim ");
      break;
    }
  }

  return vecNotas;
}

public void mostrarNotas() {
}

public void keyPressed() {
  if (key == 'r') {
    setup();

    //r.reproducirArreglo(generarMelodia(16, negra, triangle, 4), generarMelodia(16, blanca, triangle, 3), 110);
  }
}




// to make an Instrument we must define a class
// that implements the Instrument interface.
class Instrumento implements Instrument
{
  Oscil wave;
  Line  ampEnv;
  Delay myDelay;
  
  Instrumento( float frequency, Waveform w )
  {
    // make a sine wave oscillator
    // the amplitude is zero because 
    // we are going to patch a Line to it anyway
    
    wave   = new Oscil( frequency, 0, w );
    ampEnv = new Line();
    ampEnv.patch( wave.amplitude );
    
    //myDelay = new Delay( 0.4, 0.5, true, true );
    //wave.patch(myDelay);
  }
  
  // this is called by the sequencer when this instrument
  // should start making sound. the duration is expressed in seconds.
  
  public void noteOn( float duration )
  {
    // start the amplitude envelope
    ampEnv.activate( duration, 0.5f, 0 );
    // attach the oscil to the output so it makes sound
    wave.patch( r.out );
  }
  
  // this is called by the sequencer when the instrument should
  // stop making sound
  public void noteOff()
  {
    wave.unpatch( r.out );
  }
}

float C2 = 65.41f;
float D2 = 73.42f;
float E2 = 82.41f;
float F2 = 87.31f;
float FS2 = 92.50f;
float G2 = 98.00f;
float A2 = 110.00f;
float B2 = 123.47f;

float C3 = 130.81f;
float D3 = 146.83f;
float E3 = 164.81f;
float F3 = 174.61f;
float G3 = 196.00f;
float A3 = 220.00f;
float B3 = 246.94f;

float C4 = 261.63f;
float D4 = 293.66f;
float E4 = 329.63f;
float F4 = 349.23f;
float FS4 = 369.99f;
float G4 = 392.00f;
float A4 = 440.00f;
float B4 = 493.88f;

float C5 = 523.25f;
float CS5 = 554.37f;
float D5 = 587.33f;
float E5 = 659.25f;
float F5 = 698.46f;
float G5 = 783.99f;
float A5 = 880.00f;
float B5 = 987.77f;

class Nota{

  //int indice; // Cada nota tiene su \u00edndice en la secuencia de notas
  float duracion; // Duracion de la nota (negra, blanca, etc...)
  float nota; // La nota espec\u00edfica (do, re, mi, etc...)
  Waveform waveform; // El waveform de la nota (sine, saw, etc...)
  int grado;

  // Constructor de nota
  // Agregado grado para que represente el grado de la escala
  Nota(float dur, float n, Waveform wav){
    //indice = ind;
    duracion = dur;
    nota = n;
    waveform = wav;
    // Seg\u00fan la nota, decide el grado de la nota en la escala
    // Hace asi las comparaciones porque java apesta comparando floats
    if(C3 - 0.1f < n && n < C3 + 0.1f) grado = 1;
    if(D3 - 0.1f < n && n < D3 + 0.1f) grado = 2;
    if(E3 - 0.1f < n && n < E3 + 0.1f) grado = 3;
    if(F3 - 0.1f < n && n < F3 + 0.1f) grado = 4;
    if(G3 - 0.1f < n && n < G3 + 0.1f) grado = 5;
    if(A3 - 0.1f < n && n < A3 + 0.1f) grado = 6;
    if(B3 - 0.1f < n && n < B3 + 0.1f) grado = 7;
    if(C4 - 0.1f < n && n < C4 + 0.1f) grado = 1;
    if(D4 - 0.1f < n && n < D4 + 0.1f) grado = 2;
    if(E4 - 0.1f < n && n < E4 + 0.1f) grado = 3;
    if(F4 - 0.1f < n && n < F4 + 0.1f) grado = 4;
    if(G4 - 0.1f < n && n < G4 + 0.1f) grado = 5;
    if(A4 - 0.1f < n && n < A4 + 0.1f) grado = 6;
    if(B4 - 0.1f < n && n < B4 + 0.1f) grado = 7;

    println("El grado de la nota es: " + grado);
  }

  Nota(float dur, float grado, int octava, Waveform wav){
    duracion = dur;
    waveform = wav;
    // Seg\u00fan la nota y la octava, decide el grado de la nota en la escala
    if(octava == 3){
      if(grado == 1) nota = C3;
      if(grado == 2) nota = D3;
      if(grado == 3) nota = E3;
      if(grado == 4) nota = F3;
      if(grado == 5) nota = G3;
      if(grado == 6) nota = A3;
      if(grado == 7) nota = B3;
    }else if(octava == 4){
      if(grado == 1) nota = C4;
      if(grado == 2) nota = D4;
      if(grado == 3) nota = E4;
      if(grado == 4) nota = F4;
      if(grado == 5) nota = G4;
      if(grado == 6) nota = A4;
      if(grado == 7) nota = B4;
    }

  }


}




class Reproductor {

  Minim minim;
  AudioOutput out;

  float blanca = 2;
  float negra = 1;
  float corchea = 0.5f;

  float C2 = 65.41f;
  float D2 = 73.42f;
  float E2 = 82.41f;
  float F2 = 87.31f;
  float FS2 = 92.50f;
  float G2 = 98.00f;
  float A2 = 110.00f;
  float B2 = 123.47f;

  float C3 = 130.81f;
  float D3 = 146.83f;
  float E3 = 164.81f;
  float F3 = 174.61f;
  float G3 = 196.00f;
  float A3 = 220.00f;
  float B3 = 246.94f;

  float C4 = 261.63f;
  float D4 = 293.66f;
  float E4 = 329.63f;
  float F4 = 349.23f;
  float FS4 = 369.99f;
  float G4 = 392.00f;
  float A4 = 440.00f;
  float B4 = 493.88f;

  float C5 = 523.25f;
  float CS5 = 554.37f;
  float D5 = 587.33f;
  float E5 = 659.25f;
  float F5 = 698.46f;
  float G5 = 783.99f;
  float A5 = 880.00f;
  float B5 = 987.77f;

  Reproductor() {

    minim = new Minim(this);
    out = minim.getLineOut();
  }

  public void displayM() {
    background(255);
    stroke(0xffCCE3CF);
  }

  public void displayB() {

    background(0);
    stroke(0xffCCE3CF);
    strokeWeight(5);
    fill(0);
    for (int i = 0; i < r.out.bufferSize() - 1; i++) {
      point(i, height/2 + r.out.left.get(i)*100 );
    }
  }


  // Prueba 2: Crear un m\u00e9todo que reproduzca un vector de vectores.
  // Cada fila del vector contiene un vector de objetos tipo Nota, tambi\u00e9n
  // ingresados dentro de un vector, se utiliza esta estrucutura de datos porque
  // necesitamos random access y este va a ser sparse.
  // pieza: Arreglo de vectores de notas
  // tempo: Tempo con el que se quieren reproducir las notas

  public void reproducirVectores(Vector < Vector<Nota> > pieza, int tempo) {
    for (int i=0; i <= pieza.size() - 1; i++) {
      reproducirVector(pieza.elementAt(i), tempo); // Pone a reproducir el vector en la posici\u00f3n respectiva
    }
  }

  // Para empezar, un m\u00e9todo que reproduzca un vector de notas

  public void reproducirVector(Vector<Nota> notas, int tempo) {
    out.setTempo(tempo);
    out.pauseNotes();
    float duracionNotas = notas.elementAt(0).duracion; // La duraci\u00f3n de la primera nota es la duraci\u00f3n de las dem\u00e1s
    // Antes era menor o igual
    for (int i=0; i < notas.size(); i+= duracionNotas) {
      //out.playNote(notas.elementAt(i).indice, notas.elementAt(i).duracion, new Instrumento(notas.elementAt(i).nota, notas.elementAt(i).waveform));
      out.playNote(i, notas.elementAt(i).duracion, new Instrumento(notas.elementAt(i).nota, notas.elementAt(i).waveform));
    }
    out.resumeNotes();
  }
}
  public void settings() {  size(600, 500); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "arregloNotas" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
