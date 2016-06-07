
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
  float nota; // La nota específica (do, re, mi, etc...)
  Waveform waveform; // El waveform de la nota (sine, saw, etc...)
  int grado;

  // Constructor de nota
  // Agregado grado para que represente el grado de la escala
  Nota(float dur, float n, Waveform wav){
    //indice = ind;
    duracion = dur;
    nota = n;
    waveform = wav;
    // Según la nota, decide el grado de la nota en la escala
    // Hace asi las comparaciones porque java apesta comparando floats
    if(C3 - 0.1 < n && n < C3 + 0.1) grado = 1;
    if(D3 - 0.1 < n && n < D3 + 0.1) grado = 2;
    if(E3 - 0.1 < n && n < E3 + 0.1) grado = 3;
    if(F3 - 0.1 < n && n < F3 + 0.1) grado = 4;
    if(G3 - 0.1 < n && n < G3 + 0.1) grado = 5;
    if(A3 - 0.1 < n && n < A3 + 0.1) grado = 6;
    if(B3 - 0.1 < n && n < B3 + 0.1) grado = 7;
    if(C4 - 0.1 < n && n < C4 + 0.1) grado = 1;
    if(D4 - 0.1 < n && n < D4 + 0.1) grado = 2;
    if(E4 - 0.1 < n && n < E4 + 0.1) grado = 3;
    if(F4 - 0.1 < n && n < F4 + 0.1) grado = 4;
    if(G4 - 0.1 < n && n < G4 + 0.1) grado = 5;
    if(A4 - 0.1 < n && n < A4 + 0.1) grado = 6;
    if(B4 - 0.1 < n && n < B4 + 0.1) grado = 7;

    println("El grado de la nota es: " + grado);
  }

  Nota(float dur, float grado, int octava, Waveform wav){
    duracion = dur;
    waveform = wav;
    // Según la nota y la octava, decide el grado de la nota en la escala
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
