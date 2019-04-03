package pkg24h;

import java.io.*;
import java.net.Socket;

/**
 * Classe de gestion du réseau
 * @author Adrien Jussak
 */
public class Reseau {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter printer;

    /**
     * Constructeur
     * @param adresse Addresse IP du serveur
     * @param port Port du serveur
     * @throws IOException
     */
    public Reseau(String adresse, int port) throws IOException {
        socket = new Socket(adresse, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
    }

    /**
     * Envoyer un message au serveur
     * @param message
     */
    public void envoyer(String message) {
        System.out.println("<"+message);
        this.printer.println(message);
    }

    /**
     * Attendre un message du serveur
     * @return Le message reçu.
     * @throws IOException
     */
    public String recevoir() throws IOException, ErreurServeur {
        String msg = this.reader.readLine();

        if(msg.equals("ERREUR"))
            throw new ErreurServeur();

        System.out.println(">"+msg);

        return msg;
    }

    /**
     * Fermer la connection
     * @throws IOException
     */
    public void fermer() throws IOException {
        this.reader.close();
        this.printer.close();
        this.socket.close();
    }

    // Test
    public static void main(String[] args) {
        try {
            Reseau reseau = new Reseau("127.0.0.1", 1234);
            reseau.envoyer("INSCRIRE");
            System.out.println(reseau.recevoir());
            reseau.fermer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
