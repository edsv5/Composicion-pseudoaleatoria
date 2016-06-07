import ddf.minim.*;
import ddf.minim.ugens.*;
import java.util.*;

class Reproductor {

  Minim minim;
  AudioOutput out;

  float blanca = 2;
  float negra = 1;
  float corchea = 0.5;

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

  Reproductor() {

    minim = new Minim(this);
    out = minim.getLineOut();
  }

  void displayM() {
    background(255);
    stroke(#CCE3CF);
  }

  void displayB() {

    background(0);
    stroke(#CCE3CF);
    strokeWeight(5);
    fill(0);
    for (int i = 0; i < r.out.bufferSize() - 1; i++) {
      point(i, height/2 + r.out.left.get(i)*100 );
    }
  }


  // Prueba 2: Crear un método que reproduzca un vector de vectores.
  // Cada fila del vector contiene un vector de objetos tipo Nota, también
  // ingresados dentro de un vector, se utiliza esta estrucutura de datos porque
  // necesitamos random access y este va a ser sparse.
  // pieza: Arreglo de vectores de notas
  // tempo: Tempo con el que se quieren reproducir las notas

  void reproducirVectores(Vector < Vector<Nota> > pieza, int tempo) {
    for (int i=0; i <= pieza.size() - 1; i++) {
      reproducirVector(pieza.elementAt(i), tempo); // Pone a reproducir el vector en la posición respectiva
    }
  }

  // Para empezar, un método que reproduzca un vector de notas

  void reproducirVector(Vector<Nota> notas, int tempo) {
    out.setTempo(tempo);
    out.pauseNotes();
    float duracionNotas = notas.elementAt(0).duracion; // La duración de la primera nota es la duración de las demás
    // Antes era menor o igual
    for (int i=0; i < notas.size(); i+= duracionNotas) {
      //out.playNote(notas.elementAt(i).indice, notas.elementAt(i).duracion, new Instrumento(notas.elementAt(i).nota, notas.elementAt(i).waveform));
      out.playNote(i, notas.elementAt(i).duracion, new Instrumento(notas.elementAt(i).frecuencia, notas.elementAt(i).waveform));
    }
    out.resumeNotes();
  }
}
