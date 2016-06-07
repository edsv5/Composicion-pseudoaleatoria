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
  r = new Reproductor();

  //generarPiezaAcordes();

  generarPiezaCriterioA();
}

void draw() {


  r.displayB();

  //text("Frecuencia: " + o.wave.frequency.getLastValue(), 20, 20);
  //text("Amplitud: " + o.wave.amplitude.getLastValue(), 20, 40);
}

// Genera una pieza cuya melodía está generada con el criterio de
// generación estocástica definido en el método nextNota

void generarPiezaCriterioA(){
   // Crea el arreglo
   Vector< Vector<Nota> > arreglo = new  Vector< Vector<Nota> >();

   // Crea el vector de vectores de Notas que representa una "partitura"
   //generarNextNotas(int numTiempos, float duracionNotas, int octava, Waveform wav)
   Vector<Nota> Am = generarNextNotas(8, negra, 4, triangle);
   Vector<Nota> Bm = generarNextNotas(8, negra, 4, triangle);
   Vector<Nota> Cm = generarNextNotas(8, negra, 4, triangle);;

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


// Este método debería dar cada vez que se llama, la siguiente nota, dada una nota actual,
// de esta manera que exista algún criterio de generación de notas menos aleatorio.
/*
  Transiciones que se deben tomar en cuenta:

  <---------------------.---------------->
  I   II    III   IV    V   VI    VII   VIII
    <-.->        <-.---->  <-.     .->

  Tónica (I)

  La tónica no tiene tendencia a moverse hacia ningún lado, ya resuelve.

  Grado II

  Su función tonal es de Subdominante, por contener el cuarto
  grado de la escala. Tiende a moverse con la misma fuerza hacia la tónica
  y hacia dominante.

  Grado III

  Su función tonal es de Tónica. La tendencia de este acorde es moverse
  hacia el acorde el grado VI ó hacia el II ó IV. Es menos estable que el
  grado I aunque pertenezca a la función tonal tónica.

  Grado IV (Subdominante)

  Su función tonal es de Subdominante y su tendencia es moverse con la
  misma fuerza hacia tónica (I) o hacia dominante (V).La cadencia IV-I es de las
  más fuertes y se llama cadencia plagal.

  Los acordes pertenecientes a esta función armónica piden una ligera
  resolución en un acorde de función armónica dominante y, en menor
  medida, en un acorde de función armónica tónica.

  Grado V (Dominante)

  El acorde que se forma es mayor. Su función tonal es de Dominante.
  Su tendencia es moverse hacia tónica. Es el acorde más importante
  de la tonalidad después del acorde I.

  Grado VI

  Al tratarse del relativo menor, tiene cierta función de tónica.
  Su tendencia es a moverse hacia el II o el V.

  Grado VII

  El acorde que se forma es disminuido. Su función tonal es de
  Dominante, por contener los grados IV y VII. Se trata del acorde
  más débil e inestable de la tonalidad.

  Grado VIII

  Aunque no es un acorde diatónico, su uso es muy frecuente en
  progresiones diatónicas. Se trata de un acorde mayor, con
  función no diatónica, pero como contiene el grado IV se le
  asigna cierta función de subdominante.

*/

// Nos da la siguiente nota dada una nota actual y una octava

Nota nextNota(Nota notaActual, Waveform wav, int octava){
  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cuál grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  //print("GRADO: " + notaActual.grado);
  switch (notaActual.grado){
    case 0:
      println("GRADO 0");
      break;
    case 1: // En teoria, la tonica resuelve, pero para darle dinamismo, nos movemos con igual probabilidad de la tonica a cualquier otro grado
      if(randomNum < 0.15){
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }else if(0.15 < randomNum && randomNum > 0.30){
        notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
      }else if(0.30 < randomNum && randomNum > 0.45){
        notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
      }else if(0.45 < randomNum && randomNum > 0.60){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else if(0.60 < randomNum && randomNum > 0.75){
        notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
      }else if(0.75 < randomNum && randomNum > 0.90){
        notaSiguiente = new Nota(notaActual.duracion, 7, octava, wav);
      }else if(0.90 < randomNum){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav); // Con 10% de probabilidad se queda donde esta
      }
    break;

    case 2: // Tiene 45% de probabilidad de moverse a la tonica y 45% de moverse a la dominante, 10% de quedarse donde esta
      if(randomNum < 0.45){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
      }else if (0.45 < randomNum && randomNum < 0.90){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }
    break;

    case 3: //La tendencia es moverse hacia el grado VI ó hacia el II ó IV..o a quedarse donde esta
      if(randomNum < 0.5){ // 50% probabilidad de ir a grado VI
        notaSiguiente = new Nota(notaActual.duracion, 6, octava, wav);
      }else if(0.5 < randomNum && randomNum < 0.70){ // 25% de ir hacia grado II
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }else if(0.70 < randomNum && randomNum < 0.90){ // 25% de ir hacia grado IV
        notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 3, octava, wav);
      }
    break;
    // Grado IV
    // Su función tonal es de Subdominante y su tendencia es moverse con la
    // misma fuerza hacia tónica (I) o hacia dominante (V).La cadencia IV-I es de las
    // más fuertes y se llama cadencia plagal.

    case 4:
      if(randomNum < 0.45){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav); // Cadencia plagal IV -> I
      }else if (0.45 < randomNum && randomNum < 0.90 ){
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 4, octava, wav);
      }
    break;

    case 5: // Se mueve con igual probabilidad hacia la tonica, o se queda donde esta
      if(randomNum < 0.5){
        notaSiguiente = new Nota(notaActual.duracion, 1, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion, 5, octava, wav);
      }
    break;

    case 6: // Su tendencia es a moverse hacia el II o el V.
      if(randomNum < 0.45){
        notaSiguiente = new Nota(notaActual.duracion, 2, octava, wav);
      }else if (0.45 < randomNum && randomNum < 0.90){
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

// Se usa esto para generar una nota conclusiva, que dé caracter de resolución
// Por ahora, la nota conclusiva va a ser del doble del tiempo de la nota actual

Nota nextNotaConclusiva(Nota notaActual, Waveform wav, int octava){
  Nota notaSiguiente = new Nota(); // Crea la nota como silencio, pronto la asignaremos
  // Hace un switch sobre el grado de la nota actual, para ver a cuál grado pasa
  float randomNum = random(0, 1); // Para las probabilidades de pasar a las otras notas
  switch (notaActual.grado){
    case 0:
      println("Grado 0");
      break;
    case 1: // Si estamos sobre la tónica, con 50% de probabilidad, se mueve al grado III para dar variación, o se queda donde está
      if(randomNum < 0.50){
        notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
      }
    break;

    case 2: // Si está en el grado II, tiene 70% de chance de moverse hacia la tónica, 30% de ir hacia grado III
      if(randomNum < 0.70){
        notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
      }

    break;

    case 3: // 50% de chance de moverse hacia la tónica, 50% de quedarse donde está
      if(randomNum < 0.50){
        notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
      }
    break;

    case 4:
      if(randomNum < 0.50){
        notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav); // Cadencia plagal IV -> I
      }else{
        notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
      }
    break;

    case 5: // Grado 5 siempre se mueve hacia la tónica
      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
    break;

    case 6: // Relativa menor,puede quedarse donde está o moverse a la tónica o III
      if(randomNum < 0.33){
        notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);
      }else if (0.33 < randomNum && randomNum < 0.77){
        notaSiguiente = new Nota(notaActual.duracion*2, 3, octava, wav);
      }else{
        notaSiguiente = new Nota(notaActual.duracion*2, 6, octava, wav);
      }
    break;

    case 7: // Se mueve hacia la tonica siempre para resolver en el siguiente tiempo

      notaSiguiente = new Nota(notaActual.duracion*2, 1, octava, wav);

    break;
  }

  return notaSiguiente;
}

// Genera una nota semilla y numTiempos - 1 notas segun el criterio de generacion definido en nextNota

Vector<Nota> generarNextNotas(int numTiempos, float duracionNotas, int octava, Waveform wav) {
  Vector<Nota> vecNotas = new Vector<Nota>(); // Crea el vector que se va a devolver
  int randomNum = (int) random(1, 7);

  // Primera iteración de la generación de notas, se genera una nota semilla, con grado aleatorio entre 1 y 7

  println(" --- Nota semilla --- ");
  Nota notaActual = new Nota(duracionNotas, randomNum , octava, wav);
  vecNotas.add(notaActual); // Anade primero la nota semilla
  // Después, va generando notas según la nota anterior generada
  // numTiempos - 1 - duracionNotas * 2
  // Se resta notaSemilla ya generada y la nota final, que será del doble de la duración de la nota actual
  for(int i = 0 ; i < numTiempos - 1 - duracionNotas*2 ; i+= duracionNotas){
    notaActual = nextNota(notaActual, wav, octava); // Ahora la actual es la siguiente
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

// Genera una pieza con unos acordes arpegiados sencillos

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

void mostrarNotas() {
}

void keyPressed() {
  if (key == 'r') {
    setup();

    //r.reproducirArreglo(generarMelodia(16, negra, triangle, 4), generarMelodia(16, blanca, triangle, 3), 110);
  }
}
