package juego;

import java.io.IOException;
import javax.sound.sampled.*;

public class ReproducirMusica {
    public static void reproducirMusicaLoop(String ruta) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(
                    ReproducirMusica.class.getResource("/sound/goldCobra.wav")
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Suena infinitamente
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}