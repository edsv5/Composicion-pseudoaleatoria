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

// TODO: PROGRAMAR UN ALGORITMO PARA MODULAR
// TODO: PROGRAMAR ALGO QUE GENERE CADENCIAS, DOS NOTAS LARGAS PARA DAR CONCLUSI\u00d3N, 5 1, 4 1, 5 6. 5 1 4 1




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

// Declaraci\u00f3n del Reproductor que contiene las rutinas para reproducir
// arreglos de Notas

Reproductor r;

public void setup() {

  tx = 0;
  background(255);
  

  // Se crea la matriz de Markov con la que se van a generar las piezas

  /* 
   I        II       III         IV       V        VI       VII
   I        0.10     0.15     0.15        0.15     0.15     0.15     0.15    Igual probabilidad de moverse hacia cualquier grado, un poco menos baja probabilidad de quedarse donde est\u00e1 para dar m\u00e1s variaci\u00f3n.
   II       0.45     0.10     0           0        0.45     0        0       Alta probabilidad de ir hacia el grado I o grado V, baja probabilidad de quedarse donde est\u00e1.
   III      0.25     0.125    0.125       0.125    0.125    0.125    0.125   Se puede mover con cualquier probabilidad a cualquier lado o quedarse donde est\u00e1, con una probabilidad ligeramente m\u00e1s alta de ir hacia la t\u00f3nica.
   IV       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Tendencia m\u00e1s fuerte a moverse al quinto grado, un poco menos fuerte a la tonica, baja probabilidad de quedarse o irse a otro grado.
   V        0.40     0.10     0.10        0.10     0.10     0.10     0.10    Alta probabilidad de ir hacia la tonica, probabilidad repartida de irse hacia los dem\u00e1s grados. 
   VI       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Se comporta igual que el IV grado
   VII      0.40     0.10     0.10        0.10     0.10     0.10     0.10    Se comporta igual al V grado, con alta tendencia de ir hacia el primero
   
   */

  float [][] matrizMarkov = {{0.10f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f, 0.15f}, 
    {0.45f, 0.10f, 0, 0, 0.45f, 0, 0 }, 
    {0.25f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f, 0.125f }, 
    {0.20f, 0.08f, 0.08f, 0.08f, 0.40f, 0.08f, 0.08f }, 
    {0.40f, 0.10f, 0.10f, 0.10f, 0.10f, 0.10f, 0.10f }, 
    {0.20f, 0.08f, 0.08f, 0.08f, 0.40f, 0.08f, 0.08f }, 
    {0.40f, 0.10f, 0.10f, 0.10f, 0.10f, 0.10f, 0.10f }};

  pruebaMatrices(matrizMarkov);

  r = new Reproductor();
  //generarPiezaAcordes();
  generarPiezaCriterioA(matrizMarkov);
  
  //generarRondo();
}

public void draw() {


  r.displayB();

  //text("Frecuencia: " + o.wave.frequency.getLastValue(), 20, 20);
  //text("Amplitud: " + o.wave.amplitude.getLastValue(), 20, 40);
}

/************************************** M\u00c9TODOS PARA GENERAR PIEZAS **************************************/

// Genera una pieza cuya melod\u00eda est\u00e1 generada con el criterio de
// generaci\u00f3n estoc\u00e1stica definido en el m\u00e9todo nextNota como par\u00e1metro se le
// ingresa la matriz de Markov con la que se desean modelar las probabilidades de transici\u00f3n

public void generarPiezaCriterioA(float[][] matrizMarkov) {
  
  // Crea el arreglo
  Vector< Vector<Nota> > arreglo = new  Vector< Vector<Nota> >();

  // Crea tres series de notas que van a representar la partes A, B y C de nuestra pieza
  // generarNextNotas(int numTiemposPorGenerar, float duracionNotas, int octava, Waveform wav)
  Vector<Nota> Am = generarNextNotas(8, negra, 4, triangle, matrizMarkov); 
  Vector<Nota> Bm = generarNextNotas(8, negra, 4, triangle, matrizMarkov);
  Vector<Nota> Cm = generarNextNotas(8, negra, 4, triangle, matrizMarkov);

  Vector<Nota> AAcordes = generarAcordes(8, sine, 3, 0);
  Vector<Nota> BAcordes = generarAcordes(8, sine, 3, 0);
  Vector<Nota> CAcordes = generarAcordes(8, sine, 3, 0);

  Vector<Nota> melodia = new Vector<Nota>();
  Vector<Nota> acomp = new Vector<Nota>();

  melodia.addAll(Am);
  melodia.addAll(Bm);
  melodia.addAll(Am);
  melodia.addAll(Cm);
  melodia.addAll(Am);

  acomp.addAll(AAcordes);
  acomp.addAll(BAcordes);
  acomp.addAll(AAcordes);
  acomp.addAll(CAcordes);
  acomp.addAll(AAcordes);

  arreglo.add(melodia);
  arreglo.add(acomp);

  r.reproducirVectores(arreglo, 100);
}

// Genera una pieza con unos acordes arpegiados sencillos con DISTRIBUCI\u00d3N ALEATORIA

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

/************************************** M\u00c9TODOS PARA GENERAR SERIES DE NOTAS **************************************/

// Genera una nota semilla y numTiempos - 1 notas segun el criterio de generacion definido en nextNota
// Itera sobre el m\u00e9todo de nextNota
public Vector<Nota> generarNextNotas(int numTiempos, float duracionNotas, int octava, Waveform wav, float[][] matrizMarkov) {
  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea el vector que se va a devolver
  int randomNum = (int) random(1, 7);

  // Primera iteraci\u00f3n de la generaci\u00f3n de notas, se genera una nota semilla, con grado aleatorio entre 1 y 7

  println(" --- Nota semilla --- ");
  Nota notaActual = new Nota(duracionNotas, randomNum, octava, wav);
  vecNotas.add(notaActual); // Anade primero la nota semilla
  // Despu\u00e9s, va generando notas seg\u00fan la nota anterior generada
  // numTiempos - 1 - duracionNotas * 2
  // Se resta notaSemilla ya generada y la nota final, que ser\u00e1 del doble de la duraci\u00f3n de la nota actual
  for (int i = 0; i < numTiempos - 1 - duracionNotas*2; i+= duracionNotas) {
    notaActual = nextNotaM(notaActual, wav, octava, matrizMarkov); // Ahora la actual es la siguiente
    vecNotas.add(notaActual); // Anade al vector de notas
    //println("Nota agregada: " + notaActual.frecuencia + " Octava " + octava);
  }
  // Genera la nota que d\u00e9 caracter conclusivo para que termine la frase
  // Recordemos que este m\u00e9todo genera frases musicales
  notaActual = nextNotaConclusiva(notaActual, wav, octava);
  vecNotas.add(notaActual);

  // Despu\u00e9s de generar la nota conclusiva, la cual va a ser del doble de tiempos que la nota actual que reciba
  // inserta el silencio correspondiente, es decir, del doble del tiempo de la nota actual - 1

  for (int j = 1; j < notaActual.duracion; j++ ) {
    vecNotas.addElement(new Nota(notaActual.duracion, silencio, wav));
  }

  return vecNotas;
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
      break;
    case 2:
      vecNotas.addElement(new Nota(duracionNotas, D3, wav));
      break;
    case 3:
      vecNotas.addElement(new Nota(duracionNotas, E3, wav));
      break;
    case 4:
      vecNotas.addElement(new Nota(duracionNotas, F3, wav));
      break;
    case 5:
      vecNotas.addElement(new Nota(duracionNotas, G3, wav));
      break;
    case 6:
      vecNotas.addElement(new Nota(duracionNotas, A3, wav));
      break;
    case 7:
      vecNotas.addElement(new Nota(duracionNotas, B3, wav));
      break;
    case 8:
      vecNotas.addElement(new Nota(duracionNotas, C4, wav));
      break;
    case 9:
      vecNotas.addElement(new Nota(duracionNotas, D4, wav));
      break;
    case 10:
      vecNotas.addElement(new Nota(duracionNotas, E4, wav));
      break;
    case 11:
      vecNotas.addElement(new Nota(duracionNotas, F4, wav));
      break;
    case 12:
      vecNotas.addElement(new Nota(duracionNotas, G4, wav));
      break;
    case 13:
      vecNotas.addElement(new Nota(duracionNotas, A4, wav));
      break;
    case 14:
      vecNotas.addElement(new Nota(duracionNotas, B4, wav));
      break;
    case 15:
      vecNotas.addElement(new Nota(duracionNotas, C5, wav));
      break;
    }
    //print("(" + randomNum + ") ");
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
  println("--- Generando acordes ---");
  println("numTiempos: " + numTiempos);
  println("Octava: " + octava);
  println("Offset: " + silenciosColocados + " tiempos");
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
      break;
    case 2:
      vecNotas.addElement(new Nota(negra, D3, wav));
      vecNotas.addElement(new Nota(negra, F3, wav));
      vecNotas.addElement(new Nota(negra, A3, wav));
      vecNotas.addElement(new Nota(negra, F3, wav));
      break;
    case 3:
      vecNotas.addElement(new Nota(negra, E3, wav));
      vecNotas.addElement(new Nota(negra, G3, wav));
      vecNotas.addElement(new Nota(negra, B3, wav));
      vecNotas.addElement(new Nota(negra, G3, wav));
      break;
    case 4:
      vecNotas.addElement(new Nota(negra, F3, wav));
      vecNotas.addElement(new Nota(negra, A3, wav));
      vecNotas.addElement(new Nota(negra, C4, wav));
      vecNotas.addElement(new Nota(negra, A3, wav));
      break;
    case 5:
      vecNotas.addElement(new Nota(negra, G3, wav));
      vecNotas.addElement(new Nota(negra, B3, wav));
      vecNotas.addElement(new Nota(negra, D4, wav));
      vecNotas.addElement(new Nota(negra, B3, wav));
      break;
    case 6:
      vecNotas.addElement(new Nota(negra, A3, wav));
      vecNotas.addElement(new Nota(negra, C4, wav));
      vecNotas.addElement(new Nota(negra, E4, wav));
      vecNotas.addElement(new Nota(negra, C4, wav));
      break;
    case 7:
      vecNotas.addElement(new Nota(negra, B3, wav));
      vecNotas.addElement(new Nota(negra, D4, wav));
      vecNotas.addElement(new Nota(negra, F4, wav));
      vecNotas.addElement(new Nota(negra, D4, wav));
      break;
    }
  }
  println();

  return vecNotas;
}


/************************************** M\u00c9TODOS PARA GENERAR NOTAS **************************************/

// Este m\u00e9todo deber\u00eda dar cada vez que se llama, la siguiente nota, dada una nota actual,
// de esta manera que exista alg\u00fan criterio de generaci\u00f3n de notas menos aleatorio.
// VER ARCHIVO Notas_sobre_transiciones_entre_grados PARA REFERENCIA SOBRE LA TEOR\u00cdA QUE SE TOM\u00d3 EN CUENTA PARA MODELAR LAS PROBABILIDADES DE TRANSICI\u00d3N

/*
 
 MATRIZ 2.0 CON PROBABILIDADES ARREGLADAS
 
 I        II       III         IV       V        VI       VII
 I        0.10     0.15     0.15        0.15     0.15     0.15     0.15    Igual probabilidad de moverse hacia cualquier grado, un poco menos baja probabilidad de quedarse donde est\u00e1 para dar m\u00e1s variaci\u00f3n.
 II       0.45     0.10     0           0        0.45     0        0       Alta probabilidad de ir hacia el grado I o grado V, baja probabilidad de quedarse donde est\u00e1.
 III      0.25     0.125    0.125       0.125    0.125    0.125    0.125   Se puede mover con cualquier probabilidad a cualquier lado o quedarse donde est\u00e1, con una probabilidad ligeramente m\u00e1s alta de ir hacia la t\u00f3nica.
 IV       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Tendencia m\u00e1s fuerte a moverse al quinto grado, un poco menos fuerte a la tonica, baja probabilidad de quedarse o irse a otro grado.
 V        0.40     0.10     0.10        0.10     0.10     0.10     0.10    Alta probabilidad de ir hacia la tonica, probabilidad repartida de irse hacia los dem\u00e1s grados. 
 VI       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Se comporta igual que el IV grado
 VII      0.40     0.10     0.10        0.10     0.10     0.10     0.10    Se comporta igual al V grado, con alta tendencia de ir hacia el primero
 
 */
// Nos da la siguiente nota dada una nota actuaL, el waveform y una octava
// Se program\u00f3 un m\u00e9todo igual m\u00e1s abajo, que recibe una matriz de Markov como par\u00e1metro
// Se utiliza el constructor de Nota con duraci\u00f3n, grado, octava y waveform

public Nota nextNota(Nota notaActual, Waveform wav, int octava) {
  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cu\u00e1l grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  //print("GRADO: " + notaActual.grado);
  switch (notaActual.grado) {
  case 0:
    println("GRADO 0");
    //break; // Si cae en grado 0 no se detenga, para que sea como si cayera en el grado 1
  case 1: // En teoria, la tonica resuelve, pero para darle dinamismo, nos movemos con igual probabilidad de la tonica a cualquier otro grado
    if (randomNum < 0.10f) { // 10% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.10f < randomNum && randomNum > 0.25f) { // 15% de ir a  II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.25f < randomNum && randomNum > 0.40f) { // 15% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.40f < randomNum && randomNum > 0.55f) { // 15% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.55f < randomNum && randomNum > 0.70f) { // 15% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.70f < randomNum && randomNum > 0.85f) { // 15% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.85f < randomNum) { // 15% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 2: 
    if (randomNum < 0.45f) { //45% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.45f < randomNum && randomNum < 0.55f) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else { // 45% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    }
    break;

  case 3:
    if (randomNum < 0.25f) { // 25% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.25f < randomNum && randomNum > 0.375f) { // 12.5% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.375f < randomNum && randomNum > 0.50f) { // 12.5% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.50f < randomNum && randomNum > 0.625f) { // 12.5% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.625f < randomNum && randomNum > 0.75f) { // 12.5% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.75f < randomNum && randomNum > 0.875f) { // 12.5% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.875f < randomNum) { // 12.5% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 4:
    if (randomNum < 0.20f) { // 20% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.20f < randomNum && randomNum > 0.28f) { // 8% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.28f < randomNum && randomNum > 0.36f) { // 8% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.36f < randomNum && randomNum > 0.44f) { // 8% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.44f < randomNum && randomNum > 0.84f) { // 40% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.84f < randomNum && randomNum > 0.92f) { // 8% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.92f < randomNum) { // 8% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 5: 
    if (randomNum < 0.40f) { // 40% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.40f < randomNum && randomNum > 0.50f) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.50f < randomNum && randomNum > 0.60f) { // 10% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.60f < randomNum && randomNum > 0.70f) { // 10% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.70f < randomNum && randomNum > 0.80f) { // 10% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.80f < randomNum && randomNum > 0.90f) { // 10% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.90f < randomNum) { // 10% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 6: // Se comporta igual que el grado IV
    if (randomNum < 0.20f) { // 20% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.20f < randomNum && randomNum > 0.28f) { // 8% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.28f < randomNum && randomNum > 0.36f) { // 8% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.36f < randomNum && randomNum > 0.44f) { // 8% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.44f < randomNum && randomNum > 0.84f) { // 40% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.84f < randomNum && randomNum > 0.92f) { // 8% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.92f < randomNum) { // 8% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 7: // Se comporta igual que el V grado

    if (randomNum < 0.40f) { // 40% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.40f < randomNum && randomNum > 0.50f) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.50f < randomNum && randomNum > 0.60f) { // 10% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.60f < randomNum && randomNum > 0.70f) { // 10% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.70f < randomNum && randomNum > 0.80f) { // 10% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.80f < randomNum && randomNum > 0.90f) { // 10% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.90f < randomNum) { // 10% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }

    break;
  }

  return notaSiguiente;
}

// Igual que nextNota, pero usa una matriz de Markov directamente como par\u00e1metro.
/* 
 
 RECUERDE, LA MATRIZ ES \u00c9STA:
 
 I        II       III         IV       V        VI       VII
 I        0.10     0.15     0.15        0.15     0.15     0.15     0.15    Igual probabilidad de moverse hacia cualquier grado, un poco menos baja probabilidad de quedarse donde est\u00e1 para dar m\u00e1s variaci\u00f3n.
 II       0.45     0.10     0           0        0.45     0        0       Alta probabilidad de ir hacia el grado I o grado V, baja probabilidad de quedarse donde est\u00e1.
 III      0.25     0.125    0.125       0.125    0.125    0.125    0.125   Se puede mover con cualquier probabilidad a cualquier lado o quedarse donde est\u00e1, con una probabilidad ligeramente m\u00e1s alta de ir hacia la t\u00f3nica.
 IV       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Tendencia m\u00e1s fuerte a moverse al quinto grado, un poco menos fuerte a la tonica, baja probabilidad de quedarse o irse a otro grado.
 V        0.40     0.10     0.10        0.10     0.10     0.10     0.10    Alta probabilidad de ir hacia la tonica, probabilidad repartida de irse hacia los dem\u00e1s grados. 
 VI       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Se comporta igual que el IV grado
 VII      0.40     0.10     0.10        0.10     0.10     0.10     0.10    Se comporta igual al V grado, con alta tendencia de ir hacia el primero
 
 */

// Recibe una matriz 7x7 con las probabilidades de transici\u00f3n
public Nota nextNotaM(Nota notaActual, Waveform wav, int octava, float [][] matrizProbabilidades) {

  // Primero arma una matriz con los valores acumulados de la matriz de Markov para poder manipularlo como probabilidades
  float [][] matrizProbabilidadesAcum = new float [6][6];
  matrizProbabilidadesAcum[0][0] = matrizProbabilidades[0][0]; // 0.10
  matrizProbabilidadesAcum[1][0] = matrizProbabilidades[1][0];
  matrizProbabilidadesAcum[2][0] = matrizProbabilidades[2][0];
  matrizProbabilidadesAcum[3][0] = matrizProbabilidades[3][0];
  matrizProbabilidadesAcum[4][0] = matrizProbabilidades[4][0];
  matrizProbabilidadesAcum[5][0] = matrizProbabilidades[5][0];

  // matrizProbabilidadesAcum[0][1] = matrizProbabilidadesAcum[0][0] + matrizProbabilidades[0][1]; // 0.10 + 0.15 = 0.25
  // Construye las filas de acumulados, ya todas las filas tienen en su posici\u00f3n 0 su correspondiente acumulado

  for (int i = 0; i < matrizProbabilidadesAcum.length; i++) {
    for (int j = 1; j < matrizProbabilidadesAcum[0].length; j++) { // Desde 1 porque en 0 ya est\u00e1
      matrizProbabilidadesAcum[i][j] = matrizProbabilidadesAcum[i][j-1] + matrizProbabilidades[i][j];
      ;
    }
  }

  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cu\u00e1l grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  //print("GRADO: " + notaActual.grado);
  switch (notaActual.grado) {
  case 0:
    println("GRADO 0");
    //break; // Sigue como si hubiera ca\u00eddo en grado I
  case 1: // En teoria, la tonica resuelve, pero para darle dinamismo, nos movemos con igual probabilidad de la tonica a cualquier otro grado
    if (randomNum < matrizProbabilidadesAcum[0][0]) { // 10% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[0][0] < randomNum && randomNum > matrizProbabilidadesAcum[0][1]) { // 15% de ir a  II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (matrizProbabilidadesAcum[0][1] < randomNum && randomNum > matrizProbabilidadesAcum[0][2]) { // 15% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (matrizProbabilidadesAcum[0][2] < randomNum && randomNum > matrizProbabilidadesAcum[0][3]) { // 15% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (matrizProbabilidadesAcum[0][3] < randomNum && randomNum > matrizProbabilidadesAcum[0][4]) { // 15% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (matrizProbabilidadesAcum[0][4] < randomNum && randomNum > matrizProbabilidadesAcum[0][5]) { // 15% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (matrizProbabilidadesAcum[0][5] < randomNum) { // 15% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 2: 
    if (randomNum < matrizProbabilidadesAcum[1][0]) { //45% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[1][0] < randomNum && randomNum < matrizProbabilidadesAcum[1][1]) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else { // 45% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    }
    break;

  case 3:
    if (randomNum < matrizProbabilidadesAcum[2][0]) { // 25% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[2][0] < randomNum && randomNum > matrizProbabilidadesAcum[2][1]) { // 12.5% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (matrizProbabilidadesAcum[2][1] < randomNum && randomNum > matrizProbabilidadesAcum[2][2]) { // 12.5% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (matrizProbabilidadesAcum[2][2] < randomNum && randomNum > matrizProbabilidadesAcum[2][3]) { // 12.5% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (matrizProbabilidadesAcum[2][3] < randomNum && randomNum > matrizProbabilidadesAcum[2][4]) { // 12.5% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (matrizProbabilidadesAcum[2][4] < randomNum && randomNum > matrizProbabilidadesAcum[2][5]) { // 12.5% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (matrizProbabilidadesAcum[2][5] < randomNum) { // 12.5% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 4:
    if (randomNum < matrizProbabilidadesAcum[3][0]) { // 20% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[3][0] < randomNum && randomNum > matrizProbabilidadesAcum[3][1]) { // 8% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (matrizProbabilidadesAcum[3][1] < randomNum && randomNum > matrizProbabilidadesAcum[3][2]) { // 8% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (matrizProbabilidadesAcum[3][2] < randomNum && randomNum > matrizProbabilidadesAcum[3][3]) { // 8% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (matrizProbabilidadesAcum[3][3] < randomNum && randomNum > matrizProbabilidadesAcum[3][4]) { // 40% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (matrizProbabilidadesAcum[3][4] < randomNum && randomNum > matrizProbabilidadesAcum[3][5]) { // 8% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (matrizProbabilidadesAcum[3][5] < randomNum) { // 8% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 5: 
    if (randomNum < matrizProbabilidadesAcum[4][0]) { // 40% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[4][0] < randomNum && randomNum > matrizProbabilidadesAcum[4][1]) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (matrizProbabilidadesAcum[4][1] < randomNum && randomNum > matrizProbabilidadesAcum[4][2]) { // 10% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (matrizProbabilidadesAcum[4][2] < randomNum && randomNum > matrizProbabilidadesAcum[4][3]) { // 10% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (matrizProbabilidadesAcum[4][3] < randomNum && randomNum > matrizProbabilidadesAcum[4][4]) { // 10% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (matrizProbabilidadesAcum[4][4] < randomNum && randomNum > matrizProbabilidadesAcum[4][5]) { // 10% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (matrizProbabilidadesAcum[4][5] < randomNum) { // 10% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 6: // Se comporta igual que el grado IV
    if (randomNum < matrizProbabilidadesAcum[5][0]) { // 20% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[5][0] < randomNum && randomNum > matrizProbabilidadesAcum[5][1]) { // 8% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (matrizProbabilidadesAcum[5][1] < randomNum && randomNum > matrizProbabilidadesAcum[5][2]) { // 8% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (matrizProbabilidadesAcum[5][2] < randomNum && randomNum > matrizProbabilidadesAcum[5][3]) { // 8% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (matrizProbabilidadesAcum[5][3] < randomNum && randomNum > matrizProbabilidadesAcum[5][4]) { // 40% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (matrizProbabilidadesAcum[5][4] < randomNum && randomNum > matrizProbabilidadesAcum[5][5]) { // 8% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (matrizProbabilidadesAcum[5][5] < randomNum) { // 8% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 7: // Se comporta igual que el V grado

    if (randomNum < matrizProbabilidadesAcum[6][0]) { // 40% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (matrizProbabilidadesAcum[6][0] < randomNum && randomNum > matrizProbabilidadesAcum[6][1]) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (matrizProbabilidadesAcum[6][1] < randomNum && randomNum > matrizProbabilidadesAcum[6][2]) { // 10% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (matrizProbabilidadesAcum[6][2] < randomNum && randomNum > matrizProbabilidadesAcum[6][3]) { // 10% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (matrizProbabilidadesAcum[6][3] < randomNum && randomNum > matrizProbabilidadesAcum[6][4]) { // 10% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (matrizProbabilidadesAcum[6][4] < randomNum && randomNum > matrizProbabilidadesAcum[6][5]) { // 10% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (matrizProbabilidadesAcum[6][5] < randomNum) { // 10% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;
  }

  return notaSiguiente;
}

// Se usa esto para generar una nota conclusiva, que d\u00e9 caracter de resoluci\u00f3n
// Por ahora, la nota conclusiva va a ser del doble del tiempo de la nota actual
public Nota nextNotaConclusiva(Nota notaActual, Waveform wav, int octava) {
  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cu\u00e1l grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  switch (notaActual.grado) {
  case 0:
    println("Grado 0");
    break;
  case 1: // Si estamos sobre la t\u00f3nica, con 50% de probabilidad, se mueve al grado III para dar variaci\u00f3n, o se queda donde est\u00e1
    if (randomNum < 0.50f) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    }
    break;
  case 2: // Si est\u00e1 en el grado II, tiene 70% de chance de moverse hacia la t\u00f3nica, 30% de ir hacia grado III
    if (randomNum < 0.70f) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    }

    break;
  case 3: // 50% de chance de moverse hacia la t\u00f3nica, 50% de quedarse donde est\u00e1
    if (randomNum < 0.50f) {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    }
    break;
  case 4:
    if (randomNum < 0.50f) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav); // Cadencia plagal IV -> I
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    }
    break;
  case 5: // Grado 5 siempre se mueve hacia la t\u00f3nica
    notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    break;
  case 6: // Relativa menor,puede quedarse donde est\u00e1 o moverse a la t\u00f3nica o III
    if (randomNum < 0.33f) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    } else if (0.33f < randomNum && randomNum < 0.77f) {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 6, octava, wav);
    }
    break;
  case 7: // Se mueve hacia la tonica siempre para resolver en el siguiente tiempo
    notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    break;
  }
  return notaSiguiente;
}


/************************************** M\u00c9TODOS PARA GENERAR MATRICES **************************************/

// M\u00e9todo para probar si el m\u00e9todo de calcular la matriz acumulada funciona

public void pruebaMatrices(float [][] matrizProbabilidades) {
  println("Matriz de Markov");
  println();
  for (int i = 0; i < matrizProbabilidades.length; i++) {
    for (int j = 0; j < matrizProbabilidades[0].length; j++) { // Desde 1 porque en 0 ya est\u00e1
      print(matrizProbabilidades[i][j] + " | ");
    }
    println();
  }
  println();

  float [][] matrizProbabilidadesAcum = new float [7][7];
  matrizProbabilidadesAcum[0][0] = matrizProbabilidades[0][0]; // 0.10
  matrizProbabilidadesAcum[1][0] = matrizProbabilidades[1][0];
  matrizProbabilidadesAcum[2][0] = matrizProbabilidades[2][0];
  matrizProbabilidadesAcum[3][0] = matrizProbabilidades[3][0];
  matrizProbabilidadesAcum[4][0] = matrizProbabilidades[4][0];
  matrizProbabilidadesAcum[5][0] = matrizProbabilidades[5][0];

  //matrizProbabilidadesAcum[0][1] = matrizProbabilidadesAcum[0][0] + matrizProbabilidades[0][1]; // 0.10 + 0.15 = 0.25
  //matrizProbabilidadesAcum[0][2] = matrizProbabilidadesAcum[0][1] + matrizProbabilidades[0][2]; // 0.25 + 0.15 = 0.40
  //matrizProbabilidadesAcum[0][3] = matrizProbabilidadesAcum[0][2] + matrizProbabilidades[0][3]; // 0.40 + 0.15 = 0.55
  //matrizProbabilidadesAcum[0][4] = matrizProbabilidadesAcum[0][3] + matrizProbabilidades[0][4]; // 0.55 + 0.15 = 0.70
  //matrizProbabilidadesAcum[0][5] = matrizProbabilidadesAcum[0][4] + matrizProbabilidades[0][5]; // 0.70 + 0.15 = 0.85

  // Construye las filas de acumulados, ya todas las filas tienen en su posici\u00f3n 0 su correspondiente acumulado

  for (int i = 0; i < matrizProbabilidadesAcum.length; i++) {
    for (int j = 1; j < matrizProbabilidadesAcum[0].length; j++) { // Desde 1 porque en 0 ya est\u00e1
      //print(matrizProbabilidadesAcum[i][j-1] + " + " + matrizProbabilidades[i][j] + " = ");
      matrizProbabilidadesAcum[i][j] = matrizProbabilidadesAcum[i][j-1] + matrizProbabilidades[i][j];
      //println(matrizProbabilidadesAcum[i][j]);
    }
  }

  println("Matriz acumulada");
  println();
  for (int i = 0; i < matrizProbabilidadesAcum.length; i++) {
    for (int j = 0; j < matrizProbabilidadesAcum[0].length; j++) { // Desde 1 porque en 0 ya est\u00e1
      print(matrizProbabilidadesAcum[i][j] + " | ");
    }
    println();
  }
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
float silencio = 0;

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
  float frecuencia; // La nota espec\u00edfica (do, re, mi, etc...)
  Waveform waveform; // El waveform de la nota (sine, saw, etc...)
  int grado;
  String nombre; // Para mayor facilidad para identificar las notas, tienen un nombre

  // Constructor vacio
  Nota(){}

  // Constructor de nota
  // Agregado grado para que represente el grado de la escala
  Nota(float dur, float f, Waveform wav){
    //indice = ind;
    duracion = dur;
    frecuencia = f;
    waveform = wav;
    // Seg\u00fan la nota, decide el grado de la nota en la escala
    // Hace asi las comparaciones porque java apesta comparando floats
    if(f == 0){grado = 0; nombre = "Silencio";}
    if(C3 - 1 < f && f < C3 + 1) {grado = 1; nombre = "C3";}
    if(D3 - 1 < f && f < D3 + 1) {grado = 2; nombre = "D3";}
    if(E3 - 1 < f && f < E3 + 1) {grado = 3; nombre = "E3";}
    if(F3 - 1 < f && f < F3 + 1) {grado = 4; nombre = "F3";}
    if(G3 - 1 < f && f < G3 + 1) {grado = 5; nombre = "G3";}
    if(A3 - 1 < f && f < A3 + 1) {grado = 6; nombre = "A3";}
    if(B3 - 1 < f && f < B3 + 1) {grado = 7; nombre = "B3";}
    if(C4 - 1 < f && f < C4 + 1) {grado = 1; nombre = "C4";}
    if(D4 - 1 < f && f < D4 + 1) {grado = 2; nombre = "D4";}
    if(E4 - 1 < f && f < E4 + 1) {grado = 3; nombre = "E4";}
    if(F4 - 1 < f && f < F4 + 1) {grado = 4; nombre = "F4";}
    if(G4 - 1 < f && f < G4 + 1) {grado = 5; nombre = "G4";}
    if(A4 - 1 < f && f < A4 + 1) {grado = 6; nombre = "A4";}
    if(B4 - 1 < f && f < B4 + 1) {grado = 7; nombre = "B4";}

    println("Nota: " + nombre + " Grado: " + grado +" Frecuencia: " + frecuencia + " Duracion: " + duracion);
  }

  Nota(float dur, int g, int octava, Waveform wav){
    duracion = dur;
    waveform = wav;
    grado = g;
    // Seg\u00fan la nota y la octava, decide el grado de la nota en la escala
    if(octava == 3){
      if(grado == 0) {frecuencia = silencio; nombre = "Silencio";}
      if(grado == 1) {frecuencia = C3; nombre = "C3";}
      if(grado == 2) {frecuencia = D3; nombre = "D3";}
      if(grado == 3) {frecuencia = E3; nombre = "E3";}
      if(grado == 4) {frecuencia = F3; nombre = "F3";}
      if(grado == 5) {frecuencia = G3; nombre = "G3";}
      if(grado == 6) {frecuencia = A3; nombre = "A3";}
      if(grado == 7) {frecuencia = B3; nombre = "B3";}
    }else if(octava == 4){
      if(grado == 0) {frecuencia = silencio;}
      if(grado == 1) {frecuencia = C4; nombre = "C4";}
      if(grado == 2) {frecuencia = D4; nombre = "D4";}
      if(grado == 3) {frecuencia = E4; nombre = "E4";}
      if(grado == 4) {frecuencia = F4; nombre = "F4";}
      if(grado == 5) {frecuencia = G4; nombre = "G4";}
      if(grado == 6) {frecuencia = A4; nombre = "A4";}
      if(grado == 7) {frecuencia = B4; nombre = "B4";}
    }

    println("Nota: " + nombre + " Grado: " + grado +" Frecuencia: " + frecuencia + " Duracion: " + duracion);

  }


}




class Reproductor {

  Minim minim;
  AudioOutput out;
  AudioRecorder recorder; // Para grabar el output

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
    //recorder = minim.createRecorder(out, "myrecording.wav");
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
    //recorder.beginRecord(); // Empieza a grabar
    for (int i=0; i <= pieza.size() - 1; i++) {
      reproducirVector(pieza.elementAt(i), tempo); // Pone a reproducir el vector en la posici\u00f3n respectiva
    }
    //recorder.endRecord(); // Termina de grabar
    //recorder.save(); // salva
  }

  // Para empezar, un m\u00e9todo que reproduzca un vector de notas

  public void reproducirVector(Vector<Nota> notas, int tempo) {
    out.setTempo(tempo);
    out.pauseNotes();
    float duracionNotas = notas.elementAt(0).duracion; // La duraci\u00f3n de la primera nota es la duraci\u00f3n de las dem\u00e1s
    // Antes era menor o igual
    for (int i=0; i < notas.size(); i+= duracionNotas) {
      //out.playNote(notas.elementAt(i).indice, notas.elementAt(i).duracion, new Instrumento(notas.elementAt(i).nota, notas.elementAt(i).waveform));
      out.playNote(i, notas.elementAt(i).duracion, new Instrumento(notas.elementAt(i).frecuencia, notas.elementAt(i).waveform));
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
