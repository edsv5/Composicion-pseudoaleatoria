import ddf.minim.*;
import ddf.minim.ugens.*;


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
  
  void noteOn( float duration )
  {
    // start the amplitude envelope
    ampEnv.activate( duration, 0.5f, 0 );
    // attach the oscil to the output so it makes sound
    wave.patch( r.out );
  }
  
  // this is called by the sequencer when the instrument should
  // stop making sound
  void noteOff()
  {
    wave.unpatch( r.out );
  }
}