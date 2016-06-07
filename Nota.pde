float silencio = 0;

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

class Nota{

  //int indice; // Cada nota tiene su índice en la secuencia de notas
  float duracion; // Duracion de la nota (negra, blanca, etc...)
  float frecuencia; // La nota específica (do, re, mi, etc...)
  Waveform waveform; // El waveform de la nota (sine, saw, etc...)
  int grado;
  String nombre; // Para mayor facilidad para identificar las notas, tienen un nombre

  // Constructor de nota
  // Agregado grado para que represente el grado de la escala
  Nota(float dur, float f, Waveform wav){
    //indice = ind;
    duracion = dur;
    frecuencia = f;
    waveform = wav;
    // Según la nota, decide el grado de la nota en la escala
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

  Nota(float dur, float grado, int octava, Waveform wav){
    duracion = dur;
    waveform = wav;
    // Según la nota y la octava, decide el grado de la nota en la escala
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

  }


}
