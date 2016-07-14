// TODO: PROGRAMAR UN ALGORITMO PARA MODULAR
// TODO: PROGRAMAR ALGO QUE GENERE CADENCIAS, DOS NOTAS LARGAS PARA DAR CONCLUSIÓN, 5 1, 4 1, 5 6. 5 1 4 1

import ddf.minim.*;
import ddf.minim.ugens.*;

// Indice del noise

float tx;

// Duraciones de las notas

float blanca = 2;
float negra = 1;
float corchea = 0.5;

// Waveforms

Waveform sine = Waves.SINE;
Waveform triangle = Waves.TRIANGLE;
Waveform square = Waves.SQUARE;
Waveform phasor = Waves.PHASOR;
Waveform qtp = Waves.QUARTERPULSE;
Waveform saw = Waves.SAW;

// Declaración del Reproductor que contiene las rutinas para reproducir
// arreglos de Notas

Reproductor r;

void setup() {

  tx = 0;
  background(255);
  size(600, 500);

  // Se crea la matriz de Markov con la que se van a generar las piezas

  /* 
   I        II       III         IV       V        VI       VII
   I        0.10     0.15     0.15        0.15     0.15     0.15     0.15    Igual probabilidad de moverse hacia cualquier grado, un poco menos baja probabilidad de quedarse donde está para dar más variación.
   II       0.45     0.10     0           0        0.45     0        0       Alta probabilidad de ir hacia el grado I o grado V, baja probabilidad de quedarse donde está.
   III      0.25     0.125    0.125       0.125    0.125    0.125    0.125   Se puede mover con cualquier probabilidad a cualquier lado o quedarse donde está, con una probabilidad ligeramente más alta de ir hacia la tónica.
   IV       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Tendencia más fuerte a moverse al quinto grado, un poco menos fuerte a la tonica, baja probabilidad de quedarse o irse a otro grado.
   V        0.40     0.10     0.10        0.10     0.10     0.10     0.10    Alta probabilidad de ir hacia la tonica, probabilidad repartida de irse hacia los demás grados. 
   VI       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Se comporta igual que el IV grado
   VII      0.40     0.10     0.10        0.10     0.10     0.10     0.10    Se comporta igual al V grado, con alta tendencia de ir hacia el primero
   
   */

  float [][] matrizMarkov = {{0.10, 0.15, 0.15, 0.15, 0.15, 0.15, 0.15}, 
    {0.45, 0.10, 0, 0, 0.45, 0, 0 }, 
    {0.25, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125 }, 
    {0.20, 0.08, 0.08, 0.08, 0.40, 0.08, 0.08 }, 
    {0.40, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10 }, 
    {0.20, 0.08, 0.08, 0.08, 0.40, 0.08, 0.08 }, 
    {0.40, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10 }};

  pruebaMatrices(matrizMarkov);

  r = new Reproductor();
  //generarPiezaAcordes();
  generarPiezaCriterioA(matrizMarkov);
  
  //generarRondo();
}

void draw() {


  r.displayB();

  //text("Frecuencia: " + o.wave.frequency.getLastValue(), 20, 20);
  //text("Amplitud: " + o.wave.amplitude.getLastValue(), 20, 40);
}

/************************************** MÉTODOS PARA GENERAR PIEZAS **************************************/

// Genera una pieza cuya melodía está generada con el criterio de
// generación estocástica definido en el método nextNota como parámetro se le
// ingresa la matriz de Markov con la que se desean modelar las probabilidades de transición

void generarPiezaCriterioA(float[][] matrizMarkov) {
  
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

// Genera una pieza con unos acordes arpegiados sencillos con DISTRIBUCIÓN ALEATORIA

void generarPiezaAcordes() {

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

// Genera una pieza tipo rondó, ABA CAC ABA

void generarRondo() {

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

  // Digresión

  melodia.addAll(Cm);
  acomp.addAll(Ca);
  melodia.addAll(Am);
  acomp.addAll(Aa);
  melodia.addAll(Cm);
  acomp.addAll(Ca);

  // Replanteamiento (se hace una variación en el acompañamiento para que sea un poco diferente)

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

/************************************** MÉTODOS PARA GENERAR SERIES DE NOTAS **************************************/

// Genera una nota semilla y numTiempos - 1 notas segun el criterio de generacion definido en nextNota
// Itera sobre el método de nextNota
Vector<Nota> generarNextNotas(int numTiempos, float duracionNotas, int octava, Waveform wav, float[][] matrizMarkov) {
  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea el vector que se va a devolver
  int randomNum = (int) random(1, 7);

  // Primera iteración de la generación de notas, se genera una nota semilla, con grado aleatorio entre 1 y 7

  println(" --- Nota semilla --- ");
  Nota notaActual = new Nota(duracionNotas, randomNum, octava, wav);
  vecNotas.add(notaActual); // Anade primero la nota semilla
  // Después, va generando notas según la nota anterior generada
  // numTiempos - 1 - duracionNotas * 2
  // Se resta notaSemilla ya generada y la nota final, que será del doble de la duración de la nota actual
  for (int i = 0; i < numTiempos - 1 - duracionNotas*2; i+= duracionNotas) {
    notaActual = nextNotaM(notaActual, wav, octava, matrizMarkov); // Ahora la actual es la siguiente
    vecNotas.add(notaActual); // Anade al vector de notas
    //println("Nota agregada: " + notaActual.frecuencia + " Octava " + octava);
  }
  // Genera la nota que dé caracter conclusivo para que termine la frase
  // Recordemos que este método genera frases musicales
  notaActual = nextNotaConclusiva(notaActual, wav, octava);
  vecNotas.add(notaActual);

  // Después de generar la nota conclusiva, la cual va a ser del doble de tiempos que la nota actual que reciba
  // inserta el silencio correspondiente, es decir, del doble del tiempo de la nota actual - 1

  for (int j = 1; j < notaActual.duracion; j++ ) {
    vecNotas.addElement(new Nota(notaActual.duracion, silencio, wav));
  }

  return vecNotas;
}

/* Este método reproduce directamente una serie de notas generadas al azar
 
 PARÁMETROS:
 
 numTiempos: Cantidad de tiempos que se quieren generar
 duracionNotas: Duración de las notas generadas (negra, blanca, etc.)
 wav: Waveform de las notas por generar
 octava: Rango de las notas que se van a generar, esto es para poder generar tanto
 melodía como acompañamiento
 p: Si se quieren generar notas con Perlin noise o no
 offset: A partir de qué tiempo se quieren colocar las notas (en el caso de que no se quiera empezar a reproducir la parte de inmediato)
 
 EJEMPLO:
 
 arreglo.add(generarNotas(16, blanca, sine, 4, false, 4));
 
 Se generan 16 tiempos con un waveform de seno, sin Perlin Noise, es decir con distribución uniforme y con
 4 silencios antes de empezar las notas
 
 NOTAS:
 
 Por ahora se va a requerir que la serie de notas termine en la tónica, mediante, o dominante, pues estos grados dan
 una sensación de resolución mayor a los otros grados
 
 */

Vector<Nota> generarNotas(int numTiempos, float duracionNotas, Waveform wav, int octava, boolean p, int offset) {

  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea un nuevo vector de notas
  int randomNum;
  println("---- NOTAS ----");
  println("Número de tiempos " + numTiempos);
  println("Duración de las notas: " + duracionNotas);
  println("Octava: " + octava);
  println("P: " + p);

  // Primero se insertan los silencios del offset, si es que los hay

  int silenciosColocados = 0;
  for (int s = 0; s < offset; s ++) {
    vecNotas.addElement(new Nota(duracionNotas, silencio, wav)); // Se añaden silencios de negra según el offset
    silenciosColocados++; // Se agregó 1 silencio, se lleva esta cuenta para empezar a poner notas después del último silencio colocado
  }

  println("Offset: " + silenciosColocados + " tiempos.");
  // (numTiempos + silenciosColocados) - 1 para que se decida al final la última nota, para dar caracter de resolución
  for (int i =  silenciosColocados; i < (numTiempos + silenciosColocados) - duracionNotas*2; i+=duracionNotas) { // Empieza a poner notas después de los silencios

    // Si se pide una serie de notas en la octava 3, se generan números entre
    // 1 y 7, si se piden notas en la octava 4, se generan números entre 8 y 15

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

    // Generador de melodías
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

    tx += 0.5;
  }

  // Se genera la nota de resolución
  // Se trata de resolver con notas en el grado I, III, V o VIII
  // El *2 es para dar mayor caracter de resolución también, coloca una nota del doble del tiempo

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

  // Fin del poner nota de resolución

  // Coloca los silencios respectivos después de la nota

  for (int j = 1; j < duracionNotas*2; j++ ) {
    vecNotas.addElement(new Nota(duracionNotas, silencio, wav));
  }

  // Fin de colocar silencios

  println();
  return vecNotas;
}



// Genera los acordes (conjuntos de 4 notas) segun la cantidad de compases ingresados
//
// generarAcordes(16, negra, sine, 3, false, 0) debería generar 4 acordes, cada uno compuesto de 4 notas en la octava 3, con 0 silencios por delante
//
// Por ahora, sólo genera acordes de notas negras

Vector<Nota> generarAcordes(int numTiempos, Waveform wav, int octava, int offset) {
  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea un nuevo vector de notas
  // Primero se insertan los silencios del offset, si es que los hay
  int silenciosColocados = 0;
  for (int s = 0; s < offset; s ++) {
    vecNotas.addElement(new Nota(negra, silencio, wav)); // Se añaden silencios de negra según el offset
    silenciosColocados++; // Se agregó 1 silencio, se lleva esta cuenta para empezar a poner notas después del último silencio colocado
  }
  // Fin de insertar silencios
  println("--- Generando acordes ---");
  println("numTiempos: " + numTiempos);
  println("Octava: " + octava);
  println("Offset: " + silenciosColocados + " tiempos");
  // (numTiempos + silenciosColocados) - 1 para que se decida al final la última nota, para dar caracter de resolución
  // Genera número aleatorio
  int randomNum;
  for (int i =  silenciosColocados; i < numTiempos + silenciosColocados; i += 4 ) { // Empieza a poner notas después de los silencios

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


/************************************** MÉTODOS PARA GENERAR NOTAS **************************************/

// Este método debería dar cada vez que se llama, la siguiente nota, dada una nota actual,
// de esta manera que exista algún criterio de generación de notas menos aleatorio.
// VER ARCHIVO Notas_sobre_transiciones_entre_grados PARA REFERENCIA SOBRE LA TEORÍA QUE SE TOMÓ EN CUENTA PARA MODELAR LAS PROBABILIDADES DE TRANSICIÓN

/*
 
 MATRIZ 2.0 CON PROBABILIDADES ARREGLADAS
 
 I        II       III         IV       V        VI       VII
 I        0.10     0.15     0.15        0.15     0.15     0.15     0.15    Igual probabilidad de moverse hacia cualquier grado, un poco menos baja probabilidad de quedarse donde está para dar más variación.
 II       0.45     0.10     0           0        0.45     0        0       Alta probabilidad de ir hacia el grado I o grado V, baja probabilidad de quedarse donde está.
 III      0.25     0.125    0.125       0.125    0.125    0.125    0.125   Se puede mover con cualquier probabilidad a cualquier lado o quedarse donde está, con una probabilidad ligeramente más alta de ir hacia la tónica.
 IV       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Tendencia más fuerte a moverse al quinto grado, un poco menos fuerte a la tonica, baja probabilidad de quedarse o irse a otro grado.
 V        0.40     0.10     0.10        0.10     0.10     0.10     0.10    Alta probabilidad de ir hacia la tonica, probabilidad repartida de irse hacia los demás grados. 
 VI       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Se comporta igual que el IV grado
 VII      0.40     0.10     0.10        0.10     0.10     0.10     0.10    Se comporta igual al V grado, con alta tendencia de ir hacia el primero
 
 */
// Nos da la siguiente nota dada una nota actuaL, el waveform y una octava
// Se programó un método igual más abajo, que recibe una matriz de Markov como parámetro
// Se utiliza el constructor de Nota con duración, grado, octava y waveform

Nota nextNota(Nota notaActual, Waveform wav, int octava) {
  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cuál grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  //print("GRADO: " + notaActual.grado);
  switch (notaActual.grado) {
  case 0:
    println("GRADO 0");
    //break; // Si cae en grado 0 no se detenga, para que sea como si cayera en el grado 1
  case 1: // En teoria, la tonica resuelve, pero para darle dinamismo, nos movemos con igual probabilidad de la tonica a cualquier otro grado
    if (randomNum < 0.10) { // 10% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.10 < randomNum && randomNum > 0.25) { // 15% de ir a  II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.25 < randomNum && randomNum > 0.40) { // 15% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.40 < randomNum && randomNum > 0.55) { // 15% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.55 < randomNum && randomNum > 0.70) { // 15% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.70 < randomNum && randomNum > 0.85) { // 15% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.85 < randomNum) { // 15% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 2: 
    if (randomNum < 0.45) { //45% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.45 < randomNum && randomNum < 0.55) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else { // 45% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    }
    break;

  case 3:
    if (randomNum < 0.25) { // 25% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.25 < randomNum && randomNum > 0.375) { // 12.5% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.375 < randomNum && randomNum > 0.50) { // 12.5% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.50 < randomNum && randomNum > 0.625) { // 12.5% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.625 < randomNum && randomNum > 0.75) { // 12.5% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.75 < randomNum && randomNum > 0.875) { // 12.5% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.875 < randomNum) { // 12.5% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 4:
    if (randomNum < 0.20) { // 20% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.20 < randomNum && randomNum > 0.28) { // 8% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.28 < randomNum && randomNum > 0.36) { // 8% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.36 < randomNum && randomNum > 0.44) { // 8% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.44 < randomNum && randomNum > 0.84) { // 40% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.84 < randomNum && randomNum > 0.92) { // 8% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.92 < randomNum) { // 8% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 5: 
    if (randomNum < 0.40) { // 40% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.40 < randomNum && randomNum > 0.50) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.50 < randomNum && randomNum > 0.60) { // 10% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.60 < randomNum && randomNum > 0.70) { // 10% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.70 < randomNum && randomNum > 0.80) { // 10% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.80 < randomNum && randomNum > 0.90) { // 10% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.90 < randomNum) { // 10% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 6: // Se comporta igual que el grado IV
    if (randomNum < 0.20) { // 20% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.20 < randomNum && randomNum > 0.28) { // 8% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.28 < randomNum && randomNum > 0.36) { // 8% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.36 < randomNum && randomNum > 0.44) { // 8% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.44 < randomNum && randomNum > 0.84) { // 40% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.84 < randomNum && randomNum > 0.92) { // 8% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.92 < randomNum) { // 8% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }
    break;

  case 7: // Se comporta igual que el V grado

    if (randomNum < 0.40) { // 40% de ir a I
      notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
    } else if (0.40 < randomNum && randomNum > 0.50) { // 10% de ir a II
      notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
    } else if (0.50 < randomNum && randomNum > 0.60) { // 10% de ir a III
      notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
    } else if (0.60 < randomNum && randomNum > 0.70) { // 10% de ir a IV
      notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
    } else if (0.70 < randomNum && randomNum > 0.80) { // 10% de ir a V
      notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
    } else if (0.80 < randomNum && randomNum > 0.90) { // 10% de ir a VI
      notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
    } else if (0.90 < randomNum) { // 10% de ir a VII
      notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
    }

    break;
  }

  return notaSiguiente;
}

// Igual que nextNota, pero usa una matriz de Markov directamente como parámetro.
/* 
 
 RECUERDE, LA MATRIZ ES ÉSTA:
 
 I        II       III         IV       V        VI       VII
 I        0.10     0.15     0.15        0.15     0.15     0.15     0.15    Igual probabilidad de moverse hacia cualquier grado, un poco menos baja probabilidad de quedarse donde está para dar más variación.
 II       0.45     0.10     0           0        0.45     0        0       Alta probabilidad de ir hacia el grado I o grado V, baja probabilidad de quedarse donde está.
 III      0.25     0.125    0.125       0.125    0.125    0.125    0.125   Se puede mover con cualquier probabilidad a cualquier lado o quedarse donde está, con una probabilidad ligeramente más alta de ir hacia la tónica.
 IV       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Tendencia más fuerte a moverse al quinto grado, un poco menos fuerte a la tonica, baja probabilidad de quedarse o irse a otro grado.
 V        0.40     0.10     0.10        0.10     0.10     0.10     0.10    Alta probabilidad de ir hacia la tonica, probabilidad repartida de irse hacia los demás grados. 
 VI       0.20     0.08     0.08        0.08     0.40     0.08     0.08    Se comporta igual que el IV grado
 VII      0.40     0.10     0.10        0.10     0.10     0.10     0.10    Se comporta igual al V grado, con alta tendencia de ir hacia el primero
 
 */

// Recibe una matriz 7x7 con las probabilidades de transición
Nota nextNotaM(Nota notaActual, Waveform wav, int octava, float [][] matrizProbabilidades) {

  // Primero arma una matriz con los valores acumulados de la matriz de Markov para poder manipularlo como probabilidades
  float [][] matrizProbabilidadesAcum = new float [6][6];
  matrizProbabilidadesAcum[0][0] = matrizProbabilidades[0][0]; // 0.10
  matrizProbabilidadesAcum[1][0] = matrizProbabilidades[1][0];
  matrizProbabilidadesAcum[2][0] = matrizProbabilidades[2][0];
  matrizProbabilidadesAcum[3][0] = matrizProbabilidades[3][0];
  matrizProbabilidadesAcum[4][0] = matrizProbabilidades[4][0];
  matrizProbabilidadesAcum[5][0] = matrizProbabilidades[5][0];

  // matrizProbabilidadesAcum[0][1] = matrizProbabilidadesAcum[0][0] + matrizProbabilidades[0][1]; // 0.10 + 0.15 = 0.25
  // Construye las filas de acumulados, ya todas las filas tienen en su posición 0 su correspondiente acumulado

  for (int i = 0; i < matrizProbabilidadesAcum.length; i++) {
    for (int j = 1; j < matrizProbabilidadesAcum[0].length; j++) { // Desde 1 porque en 0 ya está
      matrizProbabilidadesAcum[i][j] = matrizProbabilidadesAcum[i][j-1] + matrizProbabilidades[i][j];
      ;
    }
  }

  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cuál grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  //print("GRADO: " + notaActual.grado);
  switch (notaActual.grado) {
  case 0:
    println("GRADO 0");
    //break; // Sigue como si hubiera caído en grado I
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

// Se usa esto para generar una nota conclusiva, que dé caracter de resolución
// Por ahora, la nota conclusiva va a ser del doble del tiempo de la nota actual
Nota nextNotaConclusiva(Nota notaActual, Waveform wav, int octava) {
  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cuál grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  switch (notaActual.grado) {
  case 0:
    println("Grado 0");
    break;
  case 1: // Si estamos sobre la tónica, con 50% de probabilidad, se mueve al grado III para dar variación, o se queda donde está
    if (randomNum < 0.50) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    }
    break;
  case 2: // Si está en el grado II, tiene 70% de chance de moverse hacia la tónica, 30% de ir hacia grado III
    if (randomNum < 0.70) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    }

    break;
  case 3: // 50% de chance de moverse hacia la tónica, 50% de quedarse donde está
    if (randomNum < 0.50) {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    }
    break;
  case 4:
    if (randomNum < 0.50) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav); // Cadencia plagal IV -> I
    } else {
      notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
    }
    break;
  case 5: // Grado 5 siempre se mueve hacia la tónica
    notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    break;
  case 6: // Relativa menor,puede quedarse donde está o moverse a la tónica o III
    if (randomNum < 0.33) {
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    } else if (0.33 < randomNum && randomNum < 0.77) {
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


/************************************** MÉTODOS PARA GENERAR MATRICES **************************************/

// Método para probar si el método de calcular la matriz acumulada funciona

void pruebaMatrices(float [][] matrizProbabilidades) {
  println("Matriz de Markov");
  println();
  for (int i = 0; i < matrizProbabilidades.length; i++) {
    for (int j = 0; j < matrizProbabilidades[0].length; j++) { // Desde 1 porque en 0 ya está
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

  // Construye las filas de acumulados, ya todas las filas tienen en su posición 0 su correspondiente acumulado

  for (int i = 0; i < matrizProbabilidadesAcum.length; i++) {
    for (int j = 1; j < matrizProbabilidadesAcum[0].length; j++) { // Desde 1 porque en 0 ya está
      //print(matrizProbabilidadesAcum[i][j-1] + " + " + matrizProbabilidades[i][j] + " = ");
      matrizProbabilidadesAcum[i][j] = matrizProbabilidadesAcum[i][j-1] + matrizProbabilidades[i][j];
      //println(matrizProbabilidadesAcum[i][j]);
    }
  }

  println("Matriz acumulada");
  println();
  for (int i = 0; i < matrizProbabilidadesAcum.length; i++) {
    for (int j = 0; j < matrizProbabilidadesAcum[0].length; j++) { // Desde 1 porque en 0 ya está
      print(matrizProbabilidadesAcum[i][j] + " | ");
    }
    println();
  }
}

void mostrarNotas() {
}

void keyPressed() {
  if (key == 'r') {
    setup();

    //r.reproducirArreglo(generarMelodia(16, negra, triangle, 4), generarMelodia(16, blanca, triangle, 3), 110);
  }
}